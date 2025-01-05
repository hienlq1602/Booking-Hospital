package com.ndm.ptit.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
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
import com.ndm.ptit.helper.Tooltip;
import com.ndm.ptit.recyclerview.DoctorRecyclerView;
import com.ndm.ptit.utils.Utils;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
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
    private EditText txtDate;
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
    }

    private void setupComponent()
    {
        serviceId = getIntent().getStringExtra("serviceId");
        Utils.service = serviceId;

        dialog = new Dialog(this);
        loadingScreen = new LoadingScreen(this);
        wvwDescription = findViewById(R.id.wvwDescription);
        txtDate = findViewById(R.id.txtDate);
        txtName = findViewById(R.id.txtName);
        imgAvatar = findViewById(R.id.imgAvatar);
//        imgAvatar = findViewById(R.id.imgAvatar);

        btnBack = findViewById(R.id.btnBack);
        btnCreateBooking = findViewById(R.id.btnCreateBooking);
        doctorRecyclerView = findViewById(R.id.doctorRecyclerView);

        btnCreateBooking.setVisibility(View.GONE);
        txtDate.setText(Tooltip.getToday());
        Utils.bookingTime=txtDate.getText().toString().trim();

    }

    private void printServiceInformation(Services service)
    {
        int id = service.getId();
//        String image = service.getImage();
        String name = service.getName();
        String description = "<html>"+
                "<style>body{font-size: 11px}</style>"+
                service.getDescription() + "</body></html>";

        txtName.setText(name);

//        if( service.getImage() != null)
//        {
//            Picasso.get().load(image).into(imgAvatar);
//        }
        switch (id){
            case 1:
                imgAvatar.setImageResource(R.drawable.img_niengrang);
                break;
            case 2 :
                imgAvatar.setImageResource(R.drawable.img_khamxoang);
                break;
            case 3 :
                imgAvatar.setImageResource(R.drawable.img_diennaodo);
                break;
            case 4 :
                imgAvatar.setImageResource(R.drawable.img_sankhoa);
                break;
            case 5 :
                imgAvatar.setImageResource(R.drawable.img_khamrang);
                break;
            case 6 :
                imgAvatar.setImageResource(R.drawable.img_khammat);
                break;
            case 7 :
                imgAvatar.setImageResource(R.drawable.img_daychang);
                break;
            default:
                imgAvatar.setImageResource(R.drawable.img_dauxuongkhop);
                break;
        }

//        if(id==1){
//            imgAvatar.setImageResource(R.drawable.img_niengrang);
//        }
        wvwDescription.loadDataWithBaseURL(null, description, "text/HTML", "UTF-8", null);
    }
    private void setupEvent()
    {
        btnBack.setOnClickListener(view->finish());

        txtDate.setOnClickListener(v -> {
            // Lấy ngày hiện tại để thiết lập mặc định cho DatePickerDialog
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

//            String defaultDate = String.format("%04d-%02d-%02d", year, month, day);
//            txtDate.setText(defaultDate); // Hiển thị ngày mặc định
//            fetchDoctorServiceResponse();

            // Tạo DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Khi người dùng chọn ngày xo
                        String selectedDate = String.format("%04d-%02d-%02d",selectedYear,(selectedMonth), selectedDay);
                        txtDate.setText(selectedDate); // Hiển thị ngày trong EditText
                        Utils.bookingTime=txtDate.getText().toString().trim();
                        // Gọi API sau khi chọn ngày
                        fetchDoctorServiceResponse();
                        Log.d("vao day","abc");
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });


//        btnCreateBooking.setOnClickListener(view->{
//            Intent intent = new Intent(this, BookingpageActivity.class);
//            intent.putExtra("serviceId", serviceId);
//            startActivity(intent);
//        });
    }


    private void fetchSpecialityResponse() {
        SharedPreferences prefs = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
//        String date = txtDate.getText().toString().trim();

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
        String date = txtDate.getText().toString().trim();
        if (token == null) {
            DialogUtils.showErrorDialog(this, "Token không tồn tại. Vui lòng đăng nhập lại.");
            return;
        }

        if (serviceId == null || serviceId.isEmpty()) {
            DialogUtils.showErrorDialog(this, "Không tìm thấy ID dịch vụ.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<DoctorServiceResponse> call = apiService.getDoctorServiceById("Bearer " + token, Integer.parseInt(serviceId),date);

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