package com.ndm.ptit.fragment;

import static com.ndm.ptit.utils.Utils.BASE_URL;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;


import com.ndm.ptit.R;
import com.ndm.ptit.activity.ServicepageActivity;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.Doctor;
import com.ndm.ptit.enitities.login.LoginRespone;
import com.ndm.ptit.enitities.services.DoctorService;
import com.ndm.ptit.enitities.services.DoctorServiceResponse;
import com.ndm.ptit.enitities.services.Services;
import com.ndm.ptit.enitities.services.ServicesResponse;
import com.ndm.ptit.helper.Dialog;
import com.ndm.ptit.helper.LoadingScreen;
import com.ndm.ptit.helper.Tooltip;
import com.ndm.ptit.utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class  BookingFragment1 extends Fragment {

    private final String TAG = "BookingFragment1";

    private String serviceId;// nếu serviceId == null thì nó sẽ bằng 1 bởi vì API cần serviceId do rằng buộc dữ liệu trong database
    private String doctorId;// doctorId == null thì nó sẽ bằng 0, vì chúng ta không cần
    private String doctorName;// doctorId == null thì nó sẽ bằng 0, vì chúng ta không cần
    private String doctorNameShow;// doctorId == null thì nó sẽ bằng 0, vì chúng ta không cần
    private String doctorAvatar;// doctorId == null thì nó sẽ bằng 0, vì chúng ta không cần
    private LoadingScreen loadingScreen;

    private Dialog dialog;

    private ImageView imgDoctorAvatar;
    private TextView txtServiceName;
    private TextView txtDoctorName;

    private Activity activity;
    private Context context;
    private AppCompatButton btnConfirm;

    /*FORM*/
    private EditText txtBookingName;
    private EditText txtBookingPhone;
    private EditText txtPatientName;

    private RadioGroup rdPatientGender;
    private EditText txtPatientBirthday;

    private EditText txtPatientAddress;
    private EditText txtPatientReason;
    private EditText txtAppointmentDate;
    private EditText txtAppointmentTime;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking1, container, false);

        setupComponent(view);
        setupEvent(view);

        return view;
    }


    private void setupComponent(View view)
    {

        activity = requireActivity();
        context = requireContext();


        loadingScreen = new LoadingScreen(activity);
        dialog = new Dialog(context);
        LoginRespone user = Utils.user;

        Bundle bundle = getArguments();
        assert bundle != null;
        serviceId = bundle.getString("serviceId") != null ? bundle.getString("serviceId") : Utils.service;
        doctorId = bundle.getString("doctorId") != null ? bundle.getString("doctorId") : "0";
        doctorName = bundle.getString("doctorName") != null ? bundle.getString("doctorName") : "0";
        doctorNameShow = "Bác sĩ " + doctorName;
        doctorAvatar = bundle.getString("doctorAvatar") != null ? bundle.getString("doctorAvatar") : "0";

        Log.d("ServiceId",serviceId);
        System.out.println(TAG);
        System.out.println("serviceId: " + serviceId);
        System.out.println("doctorId: " + doctorId);

        /*FORM*/
        imgDoctorAvatar = view.findViewById(R.id.imgDoctorAvatar);
        txtServiceName = view.findViewById(R.id.txtServiceName);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        txtDoctorName = view.findViewById(R.id.txtDoctorName);


        txtBookingName = view.findViewById(R.id.txtBookingName);
        txtBookingPhone = view.findViewById(R.id.txtBookingPhone);
        txtPatientName = view.findViewById(R.id.txtPatientName);

        rdPatientGender = view.findViewById(R.id.rdPatientGender);

        txtPatientBirthday = view.findViewById(R.id.txtPatientBirthday);
        txtPatientAddress = view.findViewById(R.id.txtPatientAddress);
        txtPatientReason = view.findViewById(R.id.txtPatientReason);

        txtAppointmentDate = view.findViewById(R.id.txtAppointmentDate);
        txtAppointmentTime = view.findViewById(R.id.txtAppointmentTime);

        /*SET UP FORM*/
        txtBookingName.setText(user.getData().getName());
        txtPatientName.setText(user.getData().getName());
        txtBookingPhone.setText(user.getData().getPhone());
        txtPatientBirthday.setText(user.getData().getBirthday());
        txtPatientAddress.setText(user.getData().getAddress());
        txtAppointmentDate.setText(Utils.bookingTime);
        txtAppointmentTime.setText(R.string.default_appointment_time);
        txtDoctorName.setText(doctorNameShow);
        if(doctorAvatar.length()>0){
            Picasso.get().load(BASE_URL+doctorAvatar).into(imgDoctorAvatar);
        }
        fetchSpecialityResponse();
    }


    private void printServiceInformation(Services service)
    {
        String name = service.getName();
        txtServiceName.setText(name);
    }

    private void setupEvent(View view)
    {
        /*-************************PREPARE TIME & DATE PICKER FOR BUTTON**************************************/
        /*GET TODAY*/
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        /*DATE PICKER FOR BIRTHDAY - if day or month less than 10, we will insert 0 in front of the value*/
        DatePickerDialog.OnDateSetListener birthday = (view13, year1, month1, day1) -> {
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, month1);
            calendar.set(Calendar.DAY_OF_MONTH, day1);

            String dayFormatted = day1 < 10 ? "0" + day1 : String.valueOf(day1);
            String monthFormatted = (month1 + 1) < 10 ? "0" + (month1 + 1) : String.valueOf(month1 + 1);

            String output = year1 + "-" + monthFormatted + "-" + dayFormatted;
            txtPatientBirthday.setText(output);
        };

        /*DATE PICKER FOR APPOINTMENT DATE - if day or month less than 10, we will insert 0 in front of the value*/
        DatePickerDialog.OnDateSetListener appointmentDateDialog = (view13, year1, month1, day1) -> {
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, month1);
            calendar.set(Calendar.DAY_OF_MONTH, day1);

            String dayFormatted = day1 < 10 ? "0" + day1 : String.valueOf(day1);
            String monthFormatted = (month1 + 1) < 10 ? "0" + (month1 + 1) : String.valueOf(month1 + 1);

            String output = year1 + "-" + monthFormatted + "-" + dayFormatted;
            txtAppointmentDate.setText(output);
        };


        /*TIME PICKER FOR APPOINTMENT TIME*/
        TimePickerDialog.OnTimeSetListener appointmentTimeDialog = (timePicker, hour, minute) -> {
            String hourFormatted = String.valueOf(hour);
            String minuteFormatted = String.valueOf(minute);
            if(hour < 10)
            {
                hourFormatted = "0" + hour;
            }
            if( minute < 10)
            {
                minuteFormatted = "0" + minute;
            }
            String output = hourFormatted + ":" + minuteFormatted;
            txtAppointmentTime.setText(output);
        };


        /* *************************LISTEN CLICK EVENT FOR BUTTONS**************************************/
        /*EDIT TEXT BIRTHDAY*/
        txtPatientBirthday.setOnClickListener(birthdayView -> new DatePickerDialog(context,birthday,year,month,day).show());

        /*EDIT TEXT APPOINTMENT DATE*/
        txtAppointmentDate.setOnClickListener(appointmentDateView-> new DatePickerDialog(context, appointmentDateDialog, year, month, day).show());

        /*EDIT TEXT APPOINTMENT TIME*/
        txtAppointmentTime.setOnClickListener(appointmentTimeView-> new TimePickerDialog(context, appointmentTimeDialog, 9, 0, true).show() );

        /*BUTTON CONFIRM*/
        btnConfirm.setOnClickListener(view1->{
            /*Step 1 - user must fill up all mandatory fields*/
            boolean flag = areMandatoryFieldsFilledUp();
            if( !flag ){
                return;
            }

            /*Step 2 - get date that user enters*/
            String bookingName = txtBookingName.getText().toString();
            String bookingPhone = txtBookingPhone.getText().toString();
            String patientName = txtPatientName.getText().toString();

            int selectedId = rdPatientGender.getCheckedRadioButtonId();
            RadioButton radioButton = view.findViewById(selectedId);
            String patientGender = radioButton.getHint().toString();
            String patientAddress = txtPatientAddress.getText().toString();
            String patientReason = txtPatientReason.getText().toString();

            String patientBirthday = txtPatientBirthday.getText().toString();
            String appointmentDate = txtAppointmentDate.getText().toString();
            String appointmentTime = txtAppointmentTime.getText().toString();


            // Tạo Map chứa dữ liệu
            Map<String, String> body = new HashMap<>();
            body.put("service_id", serviceId);
            body.put("patient_id", String.valueOf(Utils.user.getData().getId()));
            body.put("doctor_id", doctorId);
            body.put("booking_name", bookingName);
            body.put("booking_phone", bookingPhone);
            body.put("name", patientName);
            body.put("gender", patientGender);
            body.put("address", patientAddress);
            body.put("reason", patientReason);
            body.put("birthday", patientBirthday);
            body.put("appointment_time", appointmentDate + " " + appointmentTime);
//            body.put("appointmentDate", appointmentDate);


            Bundle bundle = new Bundle();
            bundle.putSerializable("body", (Serializable) body);

            Log.d("body 1 ",body.toString());


            String fragmentTag = "bookingFragment3";
            BookingFragment3 nextFragment = new BookingFragment3();
            nextFragment.setArguments(bundle);


            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, nextFragment, fragmentTag)
                    .addToBackStack(fragmentTag)
                    .commit();

        });
    }

    private boolean areMandatoryFieldsFilledUp()
    {
        String bookingName = txtBookingName.getText().toString();
        String bookingPhone = txtBookingPhone.getText().toString();
        String patientName = txtPatientName.getText().toString();
        String appointmentDate = txtAppointmentDate.getText().toString();
        String appointmentTime = txtAppointmentTime.getText().toString();

        String[] requiredFields = { bookingName, bookingPhone, patientName, appointmentTime, appointmentDate };

        for (String element : requiredFields) {
            if (TextUtils.isEmpty(element)) {
                dialog.announce();
                dialog.show(R.string.attention, context.getString(R.string.you_do_not_fill_mandatory_field_try_again), R.drawable.ic_info);
                dialog.btnOK.setOnClickListener(view -> dialog.close());
                return false;
            }
        }
        return true;
    }
    private void fetchSpecialityResponse() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
//        String date = txtDate.getText().toString().trim();

        if (token == null) {
            DialogUtils.showErrorDialog(getContext(), "Token không tồn tại. Vui lòng đăng nhập lại.");
            return;
        }

        // Kiểm tra serviceId
        if (serviceId == null || serviceId.isEmpty()) {
            DialogUtils.showErrorDialog(getContext(), "Không tìm thấy ID dịch vụ.");
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
                        DialogUtils.showErrorDialog(getContext(), specialityResponse.getMsg());
                    }
                } else {
                    DialogUtils.showErrorDialog(getContext(), "Không thể tải thông tin dịch vụ.");
                }
            }

            @Override
            public void onFailure(Call<ServicesResponse> call, Throwable t) {
                Log.d("getMessage",t.getMessage());
                DialogUtils.showErrorDialog(getContext(), "Lỗi kết nối: " + t.getMessage());
            }
        });
    }




}