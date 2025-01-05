package com.ndm.ptit.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ndm.ptit.R;
import com.ndm.ptit.activity.RecordpageActivity;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.BaseResponse;
import com.ndm.ptit.enitities.BaseResponse2;
import com.ndm.ptit.enitities.record.RecordRespone;
import com.ndm.ptit.enitities.treatment.Treatment;
import com.ndm.ptit.helper.LoadingScreen;
import com.ndm.ptit.recyclerview.TreatmentRecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Phong-Kaster
 * @since 30-11-2022
 *
 * this fragment will show all appointments to users selects one of them
 */
public class TreatmentFragment1 extends Fragment {

    private final String TAG = "Treatment Fragment 1";
    private Context context;
    private Activity activity;
    private Dialog dialog;
    private LoadingScreen loadingScreen;

    private Map<String, String> header ;

    private RecyclerView treatmentRecyclerView;
    private String appointmentId;


    private AppCompatButton btnSetTime;
    private String message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_treatment1, container, false);

        setupComponent(view);
        fetchTreatment();
        setupEvent();
        return view;
    }
    private void setupComponent(View view)
    {
        context = requireContext();
        activity = requireActivity();
        dialog = new Dialog(context);
        loadingScreen = new LoadingScreen(activity);


        appointmentId = activity.getIntent().getStringExtra("appointmentId");

        treatmentRecyclerView = view.findViewById(R.id.treatmentRecyclerView);

        btnSetTime = view.findViewById(R.id.btnSetAlarm);
    }

    private void fetchTreatment() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        int id = Integer.parseInt(appointmentId);

        Log.d("hello", String.valueOf(id));

        if (token == null || token.isEmpty()) {
            DialogUtils.showErrorDialog(getContext(), "Token is missing. Please log in again.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse<Treatment>> call = apiService.getTreatmentByID("Bearer " + token, id);

        call.enqueue(new Callback<BaseResponse<Treatment>>() {
            @Override
            public void onResponse(Call<BaseResponse<Treatment>> call, Response<BaseResponse<Treatment>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<Treatment> recordResponse = response.body();
                    if (recordResponse != null && recordResponse.getResult() == 1) {
                        // Bổ sung đoạn này để hiển thị danh sách
//                        List<Treatment> arr = new ArrayList<>();
                        List<Treatment> arr = new ArrayList<>((Collection) recordResponse.getData());
//                        Treatment treatments = recordResponse.getData();
//                        arr.add((arr));
                        setupRecyclerView(arr);
                    } else {
                        String errorMessage = recordResponse != null ? recordResponse.getMsg() : "Unknown error";
                        DialogUtils.showErrorDialog(getContext(), errorMessage);
                    }
                } else {
                    DialogUtils.showErrorDialog(getContext(), "Failed to fetch record.");
                }
            }


//            private void fetchTreatment() {
//                SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
//                String token = prefs.getString("token", null);
//                int id = Integer.parseInt(appointmentId);
//
//                Log.d("hello", String.valueOf(id));
//
//                if (token == null || token.isEmpty()) {
//                    DialogUtils.showErrorDialog(getContext(), "Token is missing. Please log in again.");
//                    return;
//                }
//
//                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
//                Call<BaseResponse<Treatment>> call = apiService.getTreatmentByID("Bearer " + token, id);
//
//                call.enqueue(new Callback<BaseResponse<Treatment>>() {
//                    @Override
//                    public void onResponse(Call<BaseResponse<Treatment>> call, Response<BaseResponse<Treatment>> response) {
//                        if (response.isSuccessful()) {
//                            BaseResponse<Treatment> recordResponse = response.body();
//                            if (recordResponse != null && recordResponse.getResult() == 1) {
//                                // Bổ sung đoạn này để hiển thị danh sách
//                                List<Treatment> arr = new ArrayList<>(recordResponse.getData());
////                        Treatment treatments = recordResponse.getData();
////                        arr.add((treatments));
//                                setupRecyclerView(arr);
//                            } else {
//                                String errorMessage = recordResponse != null ? recordResponse.getMsg() : "Unknown error";
//                                DialogUtils.showErrorDialog(getContext(), errorMessage);
//                            }
//                        } else {
//                            DialogUtils.showErrorDialog(getContext(), "Failed to fetch record.");
//                        }
//                    }
//
//
//                    @Override
//                    public void onFailure(Call<BaseResponse<Treatment>> call, Throwable t) {
//                        Log.d(TAG, Objects.requireNonNull(t.getMessage()));
//                        DialogUtils.showErrorDialog(getContext(), "Error: " + t.getMessage());
//                    }
//                });
//            }
            @Override
            public void onFailure(Call<BaseResponse<Treatment>> call, Throwable t) {
                Log.d(TAG, Objects.requireNonNull(t.getMessage()));
                DialogUtils.showErrorDialog(getContext(), "Error: " + t.getMessage());
            }
        });
    }



    private void setupRecyclerView(List<Treatment> list)
    {
        TreatmentRecyclerView treatmentAdapter = new TreatmentRecyclerView(context, list);
        treatmentRecyclerView.setAdapter(treatmentAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        treatmentRecyclerView.setLayoutManager(manager);

    }

    /**
     * set up event
     * @since 30-11-2022
     */
    private void setupEvent()
    {
        btnSetTime.setOnClickListener(view -> {
            String fragmentTag = "AlarmFragment";
            Fragment nextFragment = new AlarmpageFragment();// this fragment creates alarm

            Bundle bundle = new Bundle();
            bundle.putString("message", message);
            nextFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, nextFragment, fragmentTag)
                    .addToBackStack(fragmentTag)
                    .commit();
        });
    }

}