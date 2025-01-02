package com.ndm.ptit.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.ndm.ptit.R;
import com.ndm.ptit.activity.MainActivity;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.BaseResponse;
import com.ndm.ptit.enitities.BaseResponse2;
import com.ndm.ptit.enitities.notification.Notification;
import com.ndm.ptit.enitities.notification.ReadResponse;
import com.ndm.ptit.enitities.speciality.SpecialityResponse;
import com.ndm.ptit.enitities.treatment.Treatment;
import com.ndm.ptit.helper.LoadingScreen;
import com.ndm.ptit.helper.LoadingScreen2;
import com.ndm.ptit.recyclerview.NotificationRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author Phong-Kaster
 * @since 18-11-2022
 */
public class NotificationFragment extends Fragment implements NotificationRecyclerView.Callback {

    private final String TAG = "NotificationFragment";

    private RecyclerView recyclerView;

    private Dialog dialog;
    private LoadingScreen2 loadingScreen;
    private Context context;
    private Activity activity;

    private TextView txtMarkAllAsRead;

    private SwipeRefreshLayout swipeRefreshLayout;
    private Map<String, String> header;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        setupComponent(view);
        setupEvent();
        fetchTreatment();
        return view;
    }

    private void setupComponent(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);

        context = requireContext();
        activity = requireActivity();

        dialog = new Dialog(context);
        loadingScreen = new LoadingScreen2(activity, "loading");

        txtMarkAllAsRead = view.findViewById(R.id.txtMarkAllAsRead);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
    }


    private void setupEvent() {
        txtMarkAllAsRead.setOnClickListener(view -> {
            allAsRead();
            fetchTreatment();
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshNotifications();
        });

    }

    private void setupRecyclerView(List<Notification> list) {
        NotificationRecyclerView notificationAdapter = new NotificationRecyclerView(context, list, this);
        recyclerView.setAdapter(notificationAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void markAsRead(String notificationId) {
        AsReadByID(notificationId);
    }

    private void AsReadByID (String notificationId) {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        int id = Integer.parseInt(notificationId);
        if (token == null || token.isEmpty()) {
            DialogUtils.showErrorDialog(getContext(), "Token is missing. Please log in again.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ReadResponse> call = apiService.AsReadByID("Bearer " + token, id);

        call.enqueue(new Callback<ReadResponse>() {
            @Override
            public void onResponse(Call<ReadResponse> call, Response<ReadResponse> response) {
                if (response.isSuccessful()) {
                    ReadResponse notificationBaseResponse = response.body();
                    if (notificationBaseResponse != null && notificationBaseResponse.getResult() == 1) {
                    } else {
                        DialogUtils.showErrorDialog(getContext(), notificationBaseResponse.getMsg());
                    }
                } else {
                    DialogUtils.showErrorDialog(getContext(), "Failed to fetch record.");
                }
            }

            @Override
            public void onFailure(Call<ReadResponse> call, Throwable t) {
                Log.d(TAG, Objects.requireNonNull(t.getMessage()));
                DialogUtils.showErrorDialog(getContext(), "Error: " + t.getMessage());
            }
        });
    }



    private void allAsRead () {
        loadingScreen.start();
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null || token.isEmpty()) {
            DialogUtils.showErrorDialog(getContext(), "Token is missing. Please log in again.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ReadResponse> call = apiService.allAsRead("Bearer " + token);

        call.enqueue(new Callback<ReadResponse>() {
            @Override
            public void onResponse(Call<ReadResponse> call, Response<ReadResponse> response) {
                if (response.isSuccessful()) {
                    ReadResponse notificationBaseResponse = response.body();
                    if (notificationBaseResponse != null && notificationBaseResponse.getResult() == 1) {
                        loadingScreen.stop();
                    } else {
                        DialogUtils.showErrorDialog(getContext(), notificationBaseResponse.getMsg());
                    }
                } else {
                    DialogUtils.showErrorDialog(getContext(), "Failed to fetch record.");
                }
            }

            @Override
            public void onFailure(Call<ReadResponse> call, Throwable t) {
                Log.d(TAG, Objects.requireNonNull(t.getMessage()));
                DialogUtils.showErrorDialog(getContext(), "Error: " + t.getMessage());
            }
        });
    }


    private void fetchTreatment() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null || token.isEmpty()) {
            DialogUtils.showErrorDialog(getContext(), "Token is missing. Please log in again.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse<Notification>> call = apiService.getAllNoti("Bearer " + token);

        call.enqueue(new Callback<BaseResponse<Notification>>() {
            @Override
            public void onResponse(Call<BaseResponse<Notification>> call, Response<BaseResponse<Notification>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Notification> notificationBaseResponse = response.body();
                    if (notificationBaseResponse != null && notificationBaseResponse.getResult() == 1) {
                        // Lấy danh sách chuyên khoa
                        List<Notification> specialities = notificationBaseResponse.getData();
                        setupRecyclerView(specialities);
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        DialogUtils.showErrorDialog(getContext(), notificationBaseResponse.getMsg());
                    }
                } else {
                    DialogUtils.showErrorDialog(getContext(), "Failed to fetch record.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Notification>> call, Throwable t) {
                Log.d(TAG, Objects.requireNonNull(t.getMessage()));
                DialogUtils.showErrorDialog(getContext(), "Error: " + t.getMessage());
            }
        });
    }

    private void refreshNotifications() {
        swipeRefreshLayout.setRefreshing(true);
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null || token.isEmpty()) {
            DialogUtils.showErrorDialog(getContext(), "Token is missing. Please log in again.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse<Notification>> call = apiService.getAllNoti("Bearer " + token);

        call.enqueue(new Callback<BaseResponse<Notification>>() {
            @Override
            public void onResponse(Call<BaseResponse<Notification>> call, Response<BaseResponse<Notification>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Notification> notificationBaseResponse = response.body();
                    if (notificationBaseResponse != null && notificationBaseResponse.getResult() == 1) {
                        // Lấy danh sách chuyên khoa
                        List<Notification> specialities = notificationBaseResponse.getData();
                        setupRecyclerView(specialities);
                        swipeRefreshLayout.setRefreshing(false);
                    } else {
                        DialogUtils.showErrorDialog(getContext(), notificationBaseResponse.getMsg());
                    }
                } else {
                    DialogUtils.showErrorDialog(getContext(), "Failed to fetch record.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Notification>> call, Throwable t) {
                Log.d(TAG, Objects.requireNonNull(t.getMessage()));
                DialogUtils.showErrorDialog(getContext(), "Error: " + t.getMessage());
            }
        });
    }
}