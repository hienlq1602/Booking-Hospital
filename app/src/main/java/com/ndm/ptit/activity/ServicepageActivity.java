package com.ndm.ptit.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndm.ptit.R;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.BaseResponse;
import com.ndm.ptit.enitities.services.DoctorService;
import com.ndm.ptit.enitities.services.DoctorServiceResponse;
import com.ndm.ptit.enitities.services.Services;
import com.ndm.ptit.enitities.services.ServicesResponse;
import com.ndm.ptit.helper.LoadingScreen;
import com.ndm.ptit.recyclerview.DoctorRecyclerView;
import com.ndm.ptit.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicepageActivity extends AppCompatActivity {
    private final String TAG = "Service-page Activity";
    private String serviceId;

    private Dialog dialog;
    private LoadingScreen loadingScreen;

    private WebView wvwDescription;
    private TextView txtName;
    private ImageView imgAvatar;

    private ImageButton btnBack;
    private AppCompatButton btnCreateBooking;
    private RecyclerView doctorRecyclerView;

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_servicepage);
        setupComponent();
        setupEvent();
        fetchSpecialityResponse();
        fetchDoctorServiceResponse();
        setupEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Tooltip.setLocale(this, sharedPreferences);
    }

    private void setupComponent()
    {
        serviceId = getIntent().getStringExtra("serviceId");
        Utils.service = serviceId;

        dialog = new Dialog(this);
        loadingScreen = new LoadingScreen(this);

        wvwDescription = findViewById(R.id.wvwDescription);
        txtName = findViewById(R.id.txtName);
        imgAvatar = findViewById(R.id.imgAvatar);

        btnBack = findViewById(R.id.btnBack);
        btnCreateBooking = findViewById(R.id.btnCreateBooking);
        doctorRecyclerView = findViewById(R.id.doctorRecyclerView);

        btnCreateBooking.setVisibility(View.GONE);
    }

    private void printServiceInformation(Services service)
    {
        String image = service.getImage();
        String name = service.getName();
        String description = "<html>"+
                "<style>body{font-size: 11px}</style>"+
                service.getDescription() + "</body></html>";

        txtName.setText(name);

        if( service.getImage() != null)
        {
            Picasso.get().load(image).into(imgAvatar);
        }
        wvwDescription.loadDataWithBaseURL(null, description, "text/HTML", "UTF-8", null);
    }
    private void setupEvent()
    {
        btnBack.setOnClickListener(view->finish());


//        btnCreateBooking.setOnClickListener(view->{
//            Intent intent = new Intent(this, BookingpageActivity.class);
//            intent.putExtra("serviceId", serviceId);
//            startActivity(intent);
//        });
    }


    private void fetchSpecialityResponse() {
        SharedPreferences prefs = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) {
            DialogUtils.showErrorDialog(this, "Token không tồn tại. Vui lòng đăng nhập lại.");
            return;
        }

        // Kiểm tra serviceId
        if (serviceId == null || serviceId.isEmpty()) {
            DialogUtils.showErrorDialog(this, "Không tìm thấy ID dịch vụ.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<ServicesResponse> call = apiService.getServiceById("Bearer " + token, Integer.parseInt(serviceId));

        call.enqueue(new Callback<ServicesResponse>() {
            @Override
            public void onResponse(Call<ServicesResponse> call, Response<ServicesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ServicesResponse specialityResponse = response.body();
                    if (specialityResponse.getResult() == 1) {
                        Services servicesRespons = specialityResponse.getData();
                        Log.d("servicesRespons",servicesRespons.toString());
                        printServiceInformation(servicesRespons);
                    } else {
                        DialogUtils.showErrorDialog(ServicepageActivity.this, specialityResponse.getMsg());
                    }
                } else {
                    DialogUtils.showErrorDialog(ServicepageActivity.this, "Không thể tải thông tin dịch vụ.");
                }
            }

            @Override
            public void onFailure(Call<ServicesResponse> call, Throwable t) {
                Log.d("getMessage",t.getMessage());
                DialogUtils.showErrorDialog(ServicepageActivity.this, "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void fetchDoctorServiceResponse() {
        SharedPreferences prefs = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) {
            DialogUtils.showErrorDialog(this, "Token không tồn tại. Vui lòng đăng nhập lại.");
            return;
        }

        if (serviceId == null || serviceId.isEmpty()) {
            DialogUtils.showErrorDialog(this, "Không tìm thấy ID dịch vụ.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<DoctorServiceResponse> call = apiService.getDoctorServiceById("Bearer " + token, Integer.parseInt(serviceId));

        call.enqueue(new Callback<DoctorServiceResponse>() {
            @Override
            public void onResponse(Call<DoctorServiceResponse> call, Response<DoctorServiceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DoctorServiceResponse doctorResponse = response.body();
                    Log.d("response.body()", doctorResponse.toString());
                    if (doctorResponse.getResult() == 1) {
                        List<DoctorService> doctors = doctorResponse.getData();
                        setupRecyclerView(doctors);
                    } else {
                        DialogUtils.showErrorDialog(ServicepageActivity.this, doctorResponse.getMsg());
                    }
                } else {
                    DialogUtils.showErrorDialog(ServicepageActivity.this, "Không thể tải thông tin bác sĩ.");
                }
            }

            @Override
            public void onFailure(Call<DoctorServiceResponse> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                DialogUtils.showErrorDialog(ServicepageActivity.this, "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void setupRecyclerView(List<DoctorService> list)
    {
        DoctorRecyclerView doctorAdapter = new DoctorRecyclerView(this, list);
        doctorRecyclerView.setAdapter(doctorAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        doctorRecyclerView.setLayoutManager(manager);
    }


}