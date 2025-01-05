package com.ndm.ptit.activity;

import static com.ndm.ptit.utils.Utils.BASE_URL;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndm.ptit.R;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.BaseResponse;
import com.ndm.ptit.enitities.BaseResponse2;
import com.ndm.ptit.enitities.BaseResponse3;
import com.ndm.ptit.enitities.booking.Booking;
import com.ndm.ptit.enitities.booking.BookingImage;
import com.ndm.ptit.helper.Dialog_cus;
import com.ndm.ptit.helper.LoadingScreen;
import com.ndm.ptit.recyclerview.BookingPhotoRecyclerView;
import com.ndm.ptit.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingpageInfoActivity extends AppCompatActivity {
    private final String TAG = "Booking-page Info Activity";

    private String bookingId;
    private String bookingStatus;
    private Booking service;
    private List<BookingImage> list = new ArrayList<>();

    private TextView txtBookingName, txtBookingPhone, txtPatientName, txtPatientGender,txtDoctorName;
    private TextView txtPatientBirthday, txtPatientAddress, txtPatientReason, txtDatetime;
    private TextView txtBookingStatus, txtServiceName;
    private ImageView imgDoctorAvatar;
    private androidx.appcompat.widget.AppCompatButton  btnCancel;
    private ImageButton btnBack;

    private Dialog_cus dialogCus;
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

        dialogCus = new Dialog_cus(this);
        loadingScreen = new LoadingScreen(this);


        txtBookingName = findViewById(R.id.txtBookingName);
        txtDoctorName = findViewById(R.id.txtDoctorName);
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

        imgDoctorAvatar = findViewById(R.id.imgDoctorAvatar);
        txtServiceName = findViewById(R.id.txtServiceName);

        recyclerView = findViewById(R.id.recyclerView);
    }
    private void setupRecyclerView() {

        //-------------------------------------------------Click to view image----------------------------------//
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(recyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
//                    Toast.makeText(rv.getContext(), "Clicked: " + position, Toast.LENGTH_SHORT).show();
                    Dialog dialog = new Dialog(BookingpageInfoActivity.this);
                    dialog.setContentView(R.layout.dialog_image_view);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    // Tìm ImageView trong Dialog và tải ảnh vào
                    ImageView dialogImageView = dialog.findViewById(R.id.dialogImageView);
//                    for (BookingImage image : list) {
//                        Uri fileUri = Uri.parse(image.getUrl());
//                        Picasso.get()
//                                .load(fileUri)
//                                .into(dialogImageView);
//
//                        dialog.show();
//                    }
                    Uri fileUri = Uri.parse(list.get(position).getUrl());
                    Picasso.get()
                            .load(fileUri)
                            .into(dialogImageView);

                    dialog.show();

                    return true;
                }
                return false;
            }
        });

        //-------------------------------------------------Click to view image----------------------------------//
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
                        for(BookingImage item : bookingResponse.getData()){
                            list = bookingResponse.getData();
                            String uploadUrl  = BASE_URL + item.getUrl();
                            item.setUrl(uploadUrl);
                        }
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
                    fetchBooking();
                    fetchBookingImage();
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
        txtServiceName.setText(booking.getService().getName());
        txtDoctorName.setText("Bác sĩ " + booking.getDoctor().getName());
        if (booking.getDoctor().getAvatar().length() >= 0) {
            Picasso.get().load(BASE_URL+booking.getDoctor().getAvatar()).placeholder(R.drawable.default_avatar).into(imgDoctorAvatar);
        }

        String status = booking.getStatus();
        bookingStatus = status;
        Log.d("bookingStatus",bookingStatus);
        if(bookingStatus.equals("PROCESSING")){
            btnCancel.setVisibility(View.VISIBLE);
        } else {
            btnCancel.setVisibility(View.GONE);
        }


    }

    private void bindBookingImageData(List<BookingImage> list) {
        bookingPhotoAdapter = new BookingPhotoRecyclerView(BookingpageInfoActivity.this, list);
        recyclerView.setAdapter(bookingPhotoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupRecyclerView();
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
