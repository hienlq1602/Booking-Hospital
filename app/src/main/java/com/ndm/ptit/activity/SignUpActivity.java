package com.ndm.ptit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ndm.ptit.R;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.enitities.login.Patient;
import com.ndm.ptit.enitities.signup.SignUpRequest;
import com.ndm.ptit.enitities.signup.SignUpResponse;
import com.ndm.ptit.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtPhoneNumber, edtPassword, edtConfirmPassword;

    private TextView tv_login;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Find views
        edtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        edtPassword = findViewById(R.id.txtPassword);
        tv_login = findViewById(R.id.tv_login);
        edtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Set click listener for register button
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtPhoneNumber.getText().toString();
                String password = edtPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();


                if (phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }


                SignUpRequest request = new SignUpRequest();
                request.setEmail("user@gmail.com");
                request.setType("patient");
                request.setPhone(phone);
                request.setPassword(password);
                request.setPasswordConfirm(confirmPassword);
                request.setName("User");
                request.setGender(null);
                request.setBirthday("Unknown");
                request.setAddress("Unknown");
                request.setAvatar(null);
                request.setCreateAt(new java.util.Date().toString());
                request.setUpdateAt(new java.util.Date().toString());

                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                Call<SignUpResponse> call = apiService.signup(request);

                call.enqueue(new Callback<SignUpResponse>() {
                    @Override
                    public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            SignUpResponse signUpResponse = response.body();
                            Patient patient = new Patient();
                            patient.setId(new Long(signUpResponse.getData().getId()));
                            patient.setName(signUpResponse.getData().getName());
                            patient.setPhone(signUpResponse.getData().getPhone());
                            Utils.user.setData(patient);
                            if (signUpResponse.getResult() == 1) {
                                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUpActivity.this, signUpResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SignUpResponse> call, Throwable t) {
                        Toast.makeText(SignUpActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
