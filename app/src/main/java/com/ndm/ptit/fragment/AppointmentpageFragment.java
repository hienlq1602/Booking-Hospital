package com.ndm.ptit.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndm.ptit.R;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.BaseResponse;
import com.ndm.ptit.enitities.appointment.Appointment;
import com.ndm.ptit.enitities.appointment.AppointmentRequest;
import com.ndm.ptit.helper.Dialog;
import com.ndm.ptit.helper.LoadingScreen;
import com.ndm.ptit.recyclerview.AppointmentRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentpageFragment extends Fragment {
    private final String TAG = "Appointment-page Fragment";
    private RecyclerView appointmentRecyclerView;
    private Context context;
    private Activity activity;
    private Dialog dialog;
    private LoadingScreen loadingScreen;
    private LinearLayout lytNoAppointment;

    private boolean isLoading = false;
    private int size = 5;
    private int page = 1;

    private AppointmentRecyclerView appointmentAdapter;
    private List<Appointment> appointmentsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointmentpage, container, false);
        setupComponent(view);
        setupRecyclerView();
        fetchAppointment(page); // Gọi API lần đầu
        return view;
    }

    private void setupComponent(View view) {
        context = requireContext();
        activity = requireActivity();
        loadingScreen = new LoadingScreen(activity);
        dialog = new Dialog(context);
        lytNoAppointment = view.findViewById(R.id.lytNoAppointment);
        appointmentRecyclerView = view.findViewById(R.id.recyclerView);
    }

    private void setupRecyclerView() {
        appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        appointmentAdapter = new AppointmentRecyclerView(context, appointmentsList);
        appointmentRecyclerView.setAdapter(appointmentAdapter);
        appointmentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    isLoading = true;
                    page++;
                    fetchAppointment(page);
                }
            }
        });
    }

    private void fetchAppointment(int page) {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null || token.isEmpty()) {
            DialogUtils.showErrorDialog(getContext(), "Token is missing. Please log in again.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse<Appointment>> call = apiService.getAllAppointment("Bearer " + token, size, page);

        call.enqueue(new Callback<BaseResponse<Appointment>>() {
            @Override
            public void onResponse(Call<BaseResponse<Appointment>> call, Response<BaseResponse<Appointment>> response) {
                isLoading = false;

                if (response.isSuccessful()) {
                    BaseResponse<Appointment> appointmentResponse = response.body();
                    if (appointmentResponse != null && appointmentResponse.getResult() == 1) {
                        List<Appointment> newAppointments = appointmentResponse.getData();

                        if (newAppointments != null && !newAppointments.isEmpty()) {
                            lytNoAppointment.setVisibility(View.GONE);
                            appointmentRecyclerView.setVisibility(View.VISIBLE);
                            appointmentsList.addAll(newAppointments);
                            appointmentAdapter.notifyDataSetChanged();
                        } else if (appointmentsList.isEmpty()) {
                            lytNoAppointment.setVisibility(View.VISIBLE);
                            appointmentRecyclerView.setVisibility(View.GONE);
                        }

                    } else {
                        String errorMessage = appointmentResponse != null ? appointmentResponse.getMsg() : "Unknown error";
                        DialogUtils.showErrorDialog(getContext(), errorMessage);
                    }
                } else {
                    DialogUtils.showErrorDialog(getContext(), "Failed to fetch appointments.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Appointment>> call, Throwable t) {
                isLoading = false;
                Log.d(TAG, Objects.requireNonNull(t.getMessage()));
                DialogUtils.showErrorDialog(getContext(), "Error: " + t.getMessage());
            }
        });
    }
}
