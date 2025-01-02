package com.ndm.ptit.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndm.ptit.R;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.Speciality;
import com.ndm.ptit.enitities.services.DoctorService;
import com.ndm.ptit.enitities.services.DoctorServiceResponse;
import com.ndm.ptit.enitities.speciality.SpecialityResponse;
import com.ndm.ptit.helper.Dialog;
import com.ndm.ptit.helper.GlobalVariable;
import com.ndm.ptit.helper.LoadingScreen;
import com.ndm.ptit.recyclerview.DoctorRecyclerView;
import com.ndm.ptit.recyclerview.DoctorRecyclerViewSpeciality;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecialitypageActivity extends AppCompatActivity {

    private final String TAG = "Speciality-page Activity";
    private TextView txtName;
    private WebView wvwDescription;
    private RecyclerView recyclerViewDoctor;


    private GlobalVariable globalVariable;
    private String specialityId;

    private LoadingScreen loadingScreen;
    private Dialog dialog;
    private ImageView imgAvatar;

    private ImageButton btnBack;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialitypage);

        setupComponent();
        setupEvent();
        fetchDoctorServiceResponse();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * @since 21-11-2022
     * setup component
     */
    private void setupComponent()
    {
        txtName = findViewById(R.id.txtName);
        wvwDescription = findViewById(R.id.wvwDescription);
        recyclerViewDoctor = findViewById(R.id.recyclerViewDoctor);


        globalVariable = (GlobalVariable) this.getApplication();
        sharedPreferences = this.getApplication()
                .getSharedPreferences(globalVariable.getSharedReferenceKey(), MODE_PRIVATE);
        specialityId = getIntent().getStringExtra("specialityId");


        loadingScreen = new LoadingScreen(this);
        dialog = new Dialog(this);
        imgAvatar = findViewById(R.id.imgAvatar);

        btnBack = findViewById(R.id.btnBack);
    }


    private void setupEvent()
    {
        btnBack.setOnClickListener(view->finish());
    }

    private void fetchDoctorServiceResponse() {
        SharedPreferences prefs = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) {
            DialogUtils.showErrorDialog(this, "Token không tồn tại. Vui lòng đăng nhập lại.");
            return;
        }

        if (specialityId == null || specialityId.isEmpty()) {
            DialogUtils.showErrorDialog(this, "Không tìm thấy ID dịch vụ.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<DoctorServiceResponse> call = apiService.getDoctorBySpecial("Bearer " + token, Integer.parseInt(specialityId));

        call.enqueue(new Callback<DoctorServiceResponse>() {
            @Override
            public void onResponse(Call<DoctorServiceResponse> call, Response<DoctorServiceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DoctorServiceResponse doctorResponse = response.body();
                    Log.d("response.body()", doctorResponse.toString());
                    if (doctorResponse.getResult() == 1) {
                        List<DoctorService> doctors = doctorResponse.getData();
                        printSpecialityInformation(doctors.get(0).getSpecialityId());
                        setupDoctorRecyclerView(doctors);
                    } else {
                        DialogUtils.showErrorDialog(SpecialitypageActivity.this, doctorResponse.getMsg());
                    }
                } else {
                    DialogUtils.showErrorDialog(SpecialitypageActivity.this, "Không thể tải thông tin bác sĩ.");
                }
            }

            @Override
            public void onFailure(Call<DoctorServiceResponse> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                DialogUtils.showErrorDialog(SpecialitypageActivity.this, "Lỗi kết nối: " + t.getMessage());
            }
        });
    }


    private void setupDoctorRecyclerView(List<DoctorService> list)
    {
        DoctorRecyclerViewSpeciality doctorAdapter = new DoctorRecyclerViewSpeciality(this, list);
        recyclerViewDoctor.setAdapter(doctorAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewDoctor.setLayoutManager(manager);
    }


    private void printSpecialityInformation(SpecialityResponse speciality)
    {
        String name = speciality.getName();
        String description = "<html>" +
                "<style>body{font-size: 11px}</style>"+
                "<body>"+  speciality.getDescription() +
                "</body>" +
                "</html>";
        String image = speciality.getImage();

        txtName.setText(name);
        Picasso.get().load(image).into(imgAvatar);
        wvwDescription.loadDataWithBaseURL(null, description, "text/HTML", "UTF-8", null);
    }
}