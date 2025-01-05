package com.ndm.ptit.activity;

import static com.ndm.ptit.utils.Utils.BASE_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ndm.ptit.R;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.BaseResponse2;
import com.ndm.ptit.enitities.record.RecordRespone;
import com.ndm.ptit.helper.Dialog;
import com.ndm.ptit.helper.LoadingScreen;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordpageActivity extends AppCompatActivity {

    private final String TAG = "Record-page activity";
    private String appointmentId;

    private Dialog dialog;
    private LoadingScreen loadingScreen;

    private ImageView imgDoctorAvatar;
    private TextView txtDoctorName;
    private TextView txtSpecialityName;
    private TextView txtDatetime;

    private TextView txtAppointmentReason;
    private TextView txtStatusBefore;
    private TextView txtStatusAfter;

    private TextView txtReason;
    private WebView wvwDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordpage);
        setupComponent();
        fetchRecord();
    }

    private void setupComponent() {
        dialog = new Dialog(this);
        loadingScreen = new LoadingScreen(this);

        appointmentId = getIntent().getStringExtra("appointmentId");
        if (TextUtils.isEmpty(appointmentId)) {
            Log.d(TAG, "appointmentId is empty !");
            dialog.announce();
            dialog.show(R.string.attention, getString(R.string.oops_there_is_an_issue), R.drawable.ic_close);
            dialog.btnOK.setOnClickListener(view -> {
                dialog.close();
                this.finish();
            });
        }

        imgDoctorAvatar = findViewById(R.id.imgDoctorAvatar);
        txtDoctorName = findViewById(R.id.txtDoctorName);

        txtSpecialityName = findViewById(R.id.txtSpecialityName);
        txtDatetime = findViewById(R.id.txtDatetime);

        txtAppointmentReason = findViewById(R.id.txtAppointmentReason);
        txtStatusAfter = findViewById(R.id.txtStatusAfter);

        txtStatusBefore = findViewById(R.id.txtStatusBefore);
        txtReason = findViewById(R.id.txtReason);
        wvwDescription = findViewById(R.id.wvwDescription);
    }

    private void fetchRecord() {
        SharedPreferences prefs = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        int id = Integer.parseInt(appointmentId);

        Log.d("hello", String.valueOf(id));

        if (token == null || token.isEmpty()) {
            DialogUtils.showErrorDialog(this, "Token is missing. Please log in again.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse2<RecordRespone>> call = apiService.getRecordReadByID("Bearer " + token, id);

        call.enqueue(new Callback<BaseResponse2<RecordRespone>>() {
            @Override
            public void onResponse(Call<BaseResponse2<RecordRespone>> call, Response<BaseResponse2<RecordRespone>> response) {
                if (response.isSuccessful()) {
                    BaseResponse2<RecordRespone> recordResponse = response.body();
                    if (recordResponse != null && recordResponse.getResult() == 1) {
                        bindData(recordResponse.getData());
                    } else {
                        String errorMessage = recordResponse != null ? recordResponse.getMsg() : "Unknown error";
                        DialogUtils.showErrorDialog(RecordpageActivity.this, errorMessage);
                    }
                } else {
                    DialogUtils.showErrorDialog(RecordpageActivity.this, "Failed to fetch record.");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse2<RecordRespone>> call, Throwable t) {
                Log.d(TAG, Objects.requireNonNull(t.getMessage()));
                DialogUtils.showErrorDialog(RecordpageActivity.this, "Error: " + t.getMessage());
            }
        });
    }

    private void bindData(RecordRespone record) {
        txtDoctorName.setText(record.getDoctorName());
        txtSpecialityName.setText(record.getRoom().getName());
        txtDatetime.setText(record.getCreatedAt());
        txtAppointmentReason.setText(record.getReason());
        txtStatusBefore.setText(record.getStatusBefore());
        txtStatusAfter.setText(record.getStatusAfter());
        txtReason.setText(record.getDescription());
        wvwDescription.loadData(record.getDescription(), "text/html", "UTF-8");

        // Tải hình ảnh bác sĩ
        Glide.with(this)
                .load(BASE_URL + record.getDoctorName())
                .placeholder(R.drawable.ic_person_round)
                .into(imgDoctorAvatar);
    }
}
