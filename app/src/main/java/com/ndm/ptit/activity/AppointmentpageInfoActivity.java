package com.ndm.ptit.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ndm.ptit.R;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.enitities.appointment.Appointment;
import com.ndm.ptit.enitities.appointment.Appointment_queue;
import com.ndm.ptit.enitities.appointment.DetailAppointment;
import com.ndm.ptit.helper.LoadingScreen;
import com.ndm.ptit.helper.Tooltip;
import com.ndm.ptit.recyclerview.AppointmentQueueRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentpageInfoActivity extends AppCompatActivity {

    private final String TAG = "Appointment-page Info Activity";
    private Map<String, String> header;

    private Dialog dialog;
    private LoadingScreen loadingScreen;

    /*Data from recycler view*/
    private String appointmentId;// is the id of appointment that the patient are waiting for his/her turn
    private String myPosition;// is the patient position in queue
    private int myPosition2;// is the patient position in queue
    private String doctorId;
    private boolean appointmentStatus = true;// is appointment status == false, we hide recycler view appointment queue
    private CircleImageView imgDoctorAvatar;
    private TextView txtDoctorName;

    private TextView txtSpecialityName;
    private TextView txtLocation;

    private TextView txtPatientName;
    private TextView txtNumericalOrder;
    private TextView txtPatientBirthday;

    private TextView txtPosition;
    private TextView txtPatientPhone;
    private TextView txtPatientReason;

    private TextView txtAppointmentDate;
    private TextView txtAppointmentTime;

    private TextView txtStatusCancel;
    private TextView txtStatusDone;
    private TextView txtStatusProcessing;

    private AppCompatButton btnWatchMedicalRecord;
    private AppCompatButton btnWatchMedicalTreatment;

    private ImageButton btnBack;
    private RecyclerView appointmentQueueRecyclerView;
    private TextView appointmentQueueTitle;
    private SwipeRefreshLayout swipeRefreshLayout;

    private SharedPreferences sharedPreferences;

    private Handler handler;
    private Runnable fetchDetailsRunnable;
    private final long FETCH_INTERVAL = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointmentpage_info);

        setupComponent();
        fetchAppointmentDetails();
        initListen();

        handler = new Handler();
        fetchDetailsRunnable = new Runnable() {
            @Override
            public void run() {
                fetchAppointmentDetails();
                handler.postDelayed(this, FETCH_INTERVAL);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(fetchDetailsRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(fetchDetailsRunnable);
    }


    private void setupComponent() {
        /*data from appointment recycler view*/
        appointmentId = getIntent().getStringExtra("id");
        myPosition = getIntent().getStringExtra("position");
        doctorId = getIntent().getStringExtra("doctorId");

        txtPosition = findViewById(R.id.txtPosition);
        imgDoctorAvatar = findViewById(R.id.imgDoctorAvatar);
        txtDoctorName = findViewById(R.id.txtDoctorName);

        txtSpecialityName = findViewById(R.id.txtSpecialityName);
        txtLocation = findViewById(R.id.txtLocation);

        txtPatientName = findViewById(R.id.txtPatientName);
        txtNumericalOrder = findViewById(R.id.txtNumericalOrder);
        txtPatientBirthday = findViewById(R.id.txtPatientBirthday);

        txtPatientPhone = findViewById(R.id.txtPatientPhone);
        txtPatientReason = findViewById(R.id.txtPatientReason);

        txtAppointmentDate = findViewById(R.id.txtDate);
        txtAppointmentTime = findViewById(R.id.txtAppointmentTime);

        txtStatusCancel = findViewById(R.id.txtStatusCancel);
        txtStatusDone = findViewById(R.id.txtStatusDone);
        txtStatusProcessing = findViewById(R.id.txtStatusProcessing);

        btnWatchMedicalRecord = findViewById(R.id.btnWatchMedicalRecord);
        btnWatchMedicalTreatment = findViewById(R.id.btnWatchMedicalTreatment);

        btnBack = findViewById(R.id.btnBack);
        appointmentQueueRecyclerView = findViewById(R.id.appointmentQueueRecyclerView);
        appointmentQueueTitle = findViewById(R.id.appointmentQueueTitle);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
    }

    private void initListen(){

        btnBack.setOnClickListener(view->finish());

        btnWatchMedicalTreatment.setOnClickListener(view-> {
            Intent intent = new Intent(this, TreatmentpageActivity.class);
            intent.putExtra("appointmentId", appointmentId);
            this.startActivity(intent);
        });

        btnWatchMedicalRecord.setOnClickListener(view -> {
            Intent intent = new Intent(this, RecordpageActivity.class);
            intent.putExtra("appointmentId", appointmentId);
            this.startActivity(intent);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
//            getAppointmentQueue();
        });

    }


    private void fetchAppointmentDetails() {
        SharedPreferences prefs = this.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        int id = Integer.parseInt(appointmentId);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<DetailAppointment> call = apiService.getDetailAppointment("Bearer " + token, id);
        call.enqueue(new Callback<DetailAppointment>() {
            @Override
            public void onResponse(Call<DetailAppointment> call, Response<DetailAppointment> response) {
                Log.e(TAG, "Request failed with code: " + response.body());
                if (response.isSuccessful() && response.body() != null) {
                    DetailAppointment detailAppointment = response.body();
                    displayAppointmentDetails(detailAppointment.getData());
                    setupRecyclerView(detailAppointment.getAppointment_queue());
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "Error response: " + errorBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "Request failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<DetailAppointment> call, Throwable t) {
                Toast.makeText(AppointmentpageInfoActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayAppointmentDetails(Appointment detail) {
        Log.d("detail",detail.toString());

        txtDoctorName.setText(detail.getDoctorName());
        txtSpecialityName.setText(detail.getRoom().getName());
        txtLocation.setText(detail.getRoom().getLocation());
        txtPatientName.setText(detail.getPatientName());
        txtPatientBirthday.setText(detail.getPatientBirthday());
        txtPatientReason.setText(detail.getPatientReason());
        txtPatientPhone.setText(detail.getPatientPhone());
        txtAppointmentDate.setText(detail.getDate());
        txtAppointmentTime.setText(detail.getAppointmentTime());
//        txtNumericalOrder.setText(detail.getNumericalOrder());

        myPosition2 = detail.getPosition();
        Log.d("position", String.valueOf(myPosition));
//        txtPosition.setText(myPosition2);
        switch (detail.getStatus()) {
            case "PROCESSING":
                txtStatusProcessing.setText("Đang chờ ");
                txtStatusProcessing.setVisibility(View.VISIBLE);
                txtStatusDone.setVisibility(View.GONE);
                txtStatusCancel.setVisibility(View.GONE);
//                appointmentStatus = true;// we show recycler view appointment queue and send GET request to server
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                appointmentQueueRecyclerView.setVisibility(View.VISIBLE);
                appointmentQueueTitle.setVisibility(View.VISIBLE);
                btnWatchMedicalTreatment.setVisibility(View.GONE);
                btnWatchMedicalRecord.setVisibility(View.GONE);

                break;
                case "EXAMINATING":

                txtStatusProcessing.setText("Đang khám");
                txtStatusProcessing.setVisibility(View.VISIBLE);
                txtStatusDone.setVisibility(View.GONE);
                txtStatusCancel.setVisibility(View.GONE);
//                appointmentStatus = true;// we show recycler view appointment queue and send GET request to server
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                appointmentQueueRecyclerView.setVisibility(View.VISIBLE);
                appointmentQueueTitle.setVisibility(View.VISIBLE);
                btnWatchMedicalTreatment.setVisibility(View.GONE);
                btnWatchMedicalRecord.setVisibility(View.GONE);

                break;
            case "DONE":
                txtStatusProcessing.setVisibility(View.GONE);
                txtStatusDone.setVisibility(View.VISIBLE);
                txtStatusCancel.setVisibility(View.GONE);
//                appointmentStatus = false;// we hide recycler view appointment queue and never send GET request to server
                swipeRefreshLayout.setVisibility(View.GONE);
                appointmentQueueRecyclerView.setVisibility(View.GONE);
                appointmentQueueTitle.setVisibility(View.GONE);
                break;
            case "CANCEL":
                txtStatusProcessing.setVisibility(View.GONE);
                txtStatusDone.setVisibility(View.GONE);
                txtStatusCancel.setVisibility(View.VISIBLE);
//                appointmentStatus = false;// we hide recycler view appointment queue and never send GET request to server
                swipeRefreshLayout.setVisibility(View.GONE);
                appointmentQueueRecyclerView.setVisibility(View.GONE);
                appointmentQueueTitle.setVisibility(View.GONE);
                break;
        }
    }
    private void setupRecyclerView(List<Appointment_queue> appointmentQueue) {
        appointmentQueue.removeIf(item -> "CANCEL".equals(item.getStatus()) || "DONE".equals(item.getStatus()));
        AppointmentQueueRecyclerView adapter = new AppointmentQueueRecyclerView(this,appointmentQueue,myPosition2);
        appointmentQueueRecyclerView.setAdapter(adapter);
        appointmentQueueRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}