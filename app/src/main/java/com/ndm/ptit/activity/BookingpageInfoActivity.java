package com.ndm.ptit.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ndm.ptit.R;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.BaseResponse;
import com.ndm.ptit.enitities.BaseResponse2;
import com.ndm.ptit.enitities.BaseResponse3;
import com.ndm.ptit.enitities.booking.Booking;
import com.ndm.ptit.enitities.booking.BookingImage;
import com.ndm.ptit.helper.Dialog;
import com.ndm.ptit.helper.LoadingScreen;
import com.ndm.ptit.recyclerview.BookingPhotoRecyclerView;
import com.ndm.ptit.utils.Utils;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingpageInfoActivity extends AppCompatActivity {
    private final String TAG = "Booking-page Info Activity";

    private String bookingId;
    private String bookingStatus;

    private TextView txtBookingName, txtBookingPhone, txtPatientName, txtPatientGender;
    private TextView txtPatientBirthday, txtPatientAddress, txtPatientReason, txtDatetime;
    private TextView txtBookingStatus, txtServiceName;
    private ImageView imgServiceAvatar;
    private androidx.appcompat.widget.AppCompatButton  btnCancel;
    private ImageButton btnBack;

    private Dialog dialog;
    private LoadingScreen loadingScreen;

    private BookingPhotoRecyclerView bookingPhotoAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingpage_info);
        setupComponent();
        fetchBooking();
        fetchBookingImage();
        setupBackButton();
    }

    private void setupComponent() {
        bookingId = getIntent().getStringExtra("id");

        dialog = new Dialog(this);
        loadingScreen = new LoadingScreen(this);


        txtBookingName = findViewById(R.id.txtBookingName);
        txtBookingPhone = findViewById(R.id.txtBookingPhone);
        txtPatientName = findViewById(R.id.txtPatientName);
        txtPatientAddress = findViewById(R.id.txtPatientAddress);
        txtPatientBirthday = findViewById(R.id.txtPatientBirthday);
        txtDatetime = findViewById(R.id.txtDatetime);
        txtPatientGender = findViewById(R.id.txtPatientGender);
        txtPatientReason = findViewById(R.id.txtPatientReason);
        txtBookingStatus = findViewById(R.id.txtBookingStatus);

        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);

        imgServiceAvatar = findViewById(R.id.imgServiceAvatar);
        txtServiceName = findViewById(R.id.txtServiceName);

        recyclerView = findViewById(R.id.recyclerView);
    }

    private void fetchBooking() {
        SharedPreferences prefs = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        int id = Integer.parseInt(bookingId);

        if (token == null || token.isEmpty()) {
            DialogUtils.showErrorDialog(this, "Token is missing. Please log in again.");
            return;
        }
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse2<Booking>> call = apiService.getBookingByID("Bearer " + token, id);

        call.enqueue(new Callback<BaseResponse2<Booking>>() {
            @Override
            public void onResponse(Call<BaseResponse2<Booking>> call, Response<BaseResponse2<Booking>> response) {
                if (response.isSuccessful()) {
                    BaseResponse2<Booking> bookingResponse = response.body();
                    if (bookingResponse != null && bookingResponse.getResult() == 1) {
                        bindData(bookingResponse.getData());
                    } else {
                        String errorMessage = bookingResponse != null ? bookingResponse.getMsg() : "Unknown error";
                        DialogUtils.showErrorDialog(BookingpageInfoActivity.this, errorMessage);
                    }
                } else {
                    DialogUtils.showErrorDialog(BookingpageInfoActivity.this, "Failed to fetch booking information.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse2<Booking>> call, Throwable t) {
                Log.e(TAG, "Error: " + Objects.requireNonNull(t.getMessage()));
                DialogUtils.showErrorDialog(BookingpageInfoActivity.this, "Error: " + t.getMessage());
            }
        });
    }


    private void fetchBookingImage() {
        SharedPreferences prefs = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        int id = Integer.parseInt(bookingId);

        if (token == null || token.isEmpty()) {
            DialogUtils.showErrorDialog(this, "Token is missing. Please log in again.");
            return;
        }
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse<BookingImage>> call = apiService.getBookingImageByID("Bearer " + token, id);

        call.enqueue(new Callback<BaseResponse<BookingImage>>() {
            @Override
            public void onResponse(Call<BaseResponse<BookingImage>> call, Response<BaseResponse<BookingImage>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<BookingImage> bookingResponse = response.body();
                    if (bookingResponse != null && bookingResponse.getResult() == 1) {
                        bindBookingImageData(bookingResponse.getData());
                    } else {
                        String errorMessage = bookingResponse != null ? bookingResponse.getMsg() : "Unknown error";
                        DialogUtils.showErrorDialog(BookingpageInfoActivity.this, errorMessage);
                    }
                } else {
                    DialogUtils.showErrorDialog(BookingpageInfoActivity.this, "Failed to fetch booking information.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<BookingImage>> call, Throwable t) {
                Log.e(TAG, "Error: " + Objects.requireNonNull(t.getMessage()));
                DialogUtils.showErrorDialog(BookingpageInfoActivity.this, "Error: " + t.getMessage());
            }
        });
    }

    private void fetchCancel () {
        SharedPreferences prefs = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        int id = Integer.parseInt(bookingId);

        if (token == null || token.isEmpty()) {
            DialogUtils.showErrorDialog(this, "Token is missing. Please log in again.");
            return;
        }
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse3> call = apiService.putBookingCancel("Bearer " + token, id);

        call.enqueue(new Callback<BaseResponse3>() {
            @Override
            public void onResponse(Call<BaseResponse3> call, Response<BaseResponse3> response) {
                if (response.isSuccessful()) {
                    BaseResponse3 baseResponse3 = response.body();
                    DialogUtils.showErrorDialog(BookingpageInfoActivity.this, baseResponse3.getMsg());
                } else {
                    DialogUtils.showErrorDialog(BookingpageInfoActivity.this, "Failed to fetch booking information.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse3> call, Throwable t) {
                Log.e(TAG, "Error: " + Objects.requireNonNull(t.getMessage()));
                DialogUtils.showErrorDialog(BookingpageInfoActivity.this, "Error: " + t.getMessage());
            }
        });
    }

    private void bindData(Booking booking) {
        // Gán dữ liệu vào các TextView
        txtBookingName.setText(Utils.user.getData().getName());
        txtBookingPhone.setText(booking.getBookingPhone() != null ? booking.getBookingPhone() : Utils.user.getData().getPhone());
        txtPatientName.setText(booking.getName());
        txtPatientAddress.setText(booking.getAddress());
        txtPatientBirthday.setText(booking.getBirthday());
        txtDatetime.setText(booking.getAppointmentTime());
        txtPatientGender.setText(booking.getGender() == 0 ? "Nam" : "Nữ");
        txtPatientReason.setText(booking.getReason());
        txtBookingStatus.setText(booking.getStatus());

        String status = booking.getStatus();
        bookingStatus = status;
        if(bookingStatus.equals("PROCESSING")){
            btnCancel.setVisibility(View.VISIBLE);
        }


    }

    private void bindBookingImageData(List<BookingImage> list) {
        bookingPhotoAdapter = new BookingPhotoRecyclerView(BookingpageInfoActivity.this, list);
        recyclerView.setAdapter(bookingPhotoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void setupBackButton() {
        btnBack.setOnClickListener(view->finish());

        btnCancel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCancel();
            }
        }));
    }
}
