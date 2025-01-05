package com.ndm.ptit.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndm.ptit.R;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.BaseResponse;
import com.ndm.ptit.enitities.booking.Booking;
import com.ndm.ptit.helper.Dialog;
import com.ndm.ptit.helper.LoadingScreen;
import com.ndm.ptit.recyclerview.BookingRecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingHistoryActivity extends AppCompatActivity {

    private final String TAG = "Booking History Activity";
    private ImageButton btnBack;
    private RecyclerView bookingRecyclerView;
    private Dialog dialog;
    private LoadingScreen loadingScreen;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        setupComponent();
        setupEvent();
        fetchBooking();
    }


    private void setupComponent() {
        btnBack = findViewById(R.id.btnBack);
        bookingRecyclerView = findViewById(R.id.bookingRecyclerView);
        dialog = new Dialog(this);
        loadingScreen = new LoadingScreen(this);
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
    }

    private void setupEvent() {
        btnBack.setOnClickListener(view -> finish());
    }

    private void setupRecyclerView(List<Booking> list) {
        BookingRecyclerView appointmentAdapter = new BookingRecyclerView(this, list);
        bookingRecyclerView.setAdapter(appointmentAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bookingRecyclerView.setLayoutManager(manager);
    }

    private void fetchBooking() {
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            DialogUtils.showErrorDialog(this, "Authentication failed. Please login again.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse<Booking>> call = apiService.bookingReadALl("Bearer " + token);

        call.enqueue(new Callback<BaseResponse<Booking>>() {
            @Override
            public void onResponse(Call<BaseResponse<Booking>> call, Response<BaseResponse<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Booking> bookings = response.body().getData();
                    Log.d("bookings", String.valueOf(bookings.get(0).toString()));
                    if (bookings != null && !bookings.isEmpty()) {
                        setupRecyclerView(bookings);
                    } else {
                        DialogUtils.showErrorDialog(BookingHistoryActivity.this, "No bookings found.");
                    }
                } else {
                    DialogUtils.showErrorDialog(BookingHistoryActivity.this, "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Booking>> call, Throwable t) {
                DialogUtils.showErrorDialog(BookingHistoryActivity.this, "Error: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        fetchBooking();
        super.onResume();
    }
}
