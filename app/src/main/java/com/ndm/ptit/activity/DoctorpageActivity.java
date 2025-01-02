package com.ndm.ptit.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.ndm.ptit.R;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.BaseResponse2;
import com.ndm.ptit.enitities.doctor.DoctorResponse;
import com.ndm.ptit.helper.LoadingScreen;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorpageActivity extends AppCompatActivity {
    private static final String TAG = "Doctor-page Activity";

    private String doctorId;
    private String speciality;
    private CircleImageView imgAvatar;
    private TextView txtName;
    private TextView txtSpeciality;
    private TextView txtPhoneNumber;
    private WebView wvwDescription;

    private LoadingScreen loadingScreen;

    private ImageButton btnBack;
    private AppCompatButton btnCreateBooking;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorpage);

        setupComponent();
        setupEvent();
        fetchDoctorDetails();
    }

    private void setupComponent() {
        doctorId = getIntent().getStringExtra("doctorId");
        imgAvatar = findViewById(R.id.imgAvatar);
        txtName = findViewById(R.id.txtName);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtSpeciality = findViewById(R.id.txtSpeciality);
        wvwDescription = findViewById(R.id.wvwDescription);

        loadingScreen = new LoadingScreen(this);
        btnBack = findViewById(R.id.btnBack);
        btnCreateBooking = findViewById(R.id.btnCreateBooking);
        speciality = getIntent().getStringExtra("speciality");
        if(speciality != null){
            btnCreateBooking.setVisibility(View.GONE);
        }
    }

    private void setupEvent() {
        btnBack.setOnClickListener(view -> finish());

        btnCreateBooking.setOnClickListener(view -> {
            Intent intent = new Intent(this, BookingpageActivity.class);
            intent.putExtra("doctorId", doctorId);
            startActivity(intent);
        });
    }

    private void fetchDoctorDetails() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse2<DoctorResponse>> call = apiService.getDoctorID("Bearer " + token, Integer.parseInt(doctorId));

        call.enqueue(new Callback<BaseResponse2<DoctorResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse2<DoctorResponse>> call, Response<BaseResponse2<DoctorResponse>> response) {
                if (response.isSuccessful()) {
                    BaseResponse2<DoctorResponse> doctorResponse = response.body();
                    if (doctorResponse != null && doctorResponse.getResult() == 1) {
                        DoctorResponse doctor = doctorResponse.getData();
                        txtName.setText(doctor.getName());
                        txtPhoneNumber.setText(doctor.getPhone());
                        txtSpeciality.setText(doctor.getSpecialityName());
                        wvwDescription.loadData(doctor.getDescription(), "text/html", "UTF-8");


                        if (!doctor.getAvatar().isEmpty()) {
                            Glide.with(DoctorpageActivity.this)
                                    .load(doctor.getAvatar())
                                    .into(imgAvatar);
                        }
                    } else {
                        DialogUtils.showErrorDialog(DoctorpageActivity.this, doctorResponse.getMsg());
                    }
                } else {
                    DialogUtils.showErrorDialog(DoctorpageActivity.this, "Request failed");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse2<DoctorResponse>> call, Throwable t) {
                DialogUtils.showErrorDialog(DoctorpageActivity.this, t.getMessage());
            }
        });
    }
}
