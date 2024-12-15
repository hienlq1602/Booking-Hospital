package com.ndm.ptit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ndm.ptit.R;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.login.LoginRequest;
import com.ndm.ptit.enitities.login.LoginRespone;
import com.ndm.ptit.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity{
    private EditText txtPhoneNumber, txtPassWord;
    private TextView tv_signup;
    private Button btnLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        initListener();
    }

    private void initUI() {
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtPassWord = findViewById(R.id.txtPassWord);
        tv_signup = findViewById(R.id.tv_signup);
        btnLogin = findViewById(R.id.btnLogin);
        progressDialog = new ProgressDialog(this);
    }

    private void initListener() {
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignIn();
            }
        });
    }

    private void onClickSignIn() {
        String strEmail = txtPhoneNumber.getText().toString().trim();
        String strPassword = txtPassWord.getText().toString().trim();

        if (strEmail.isEmpty() || strPassword.isEmpty()) {
            DialogUtils.showErrorDialog(LogInActivity.this, "Vui lòng nhập đầy đủ email và mật khẩu");
            return;
        }

        progressDialog.setMessage("Đang đăng nhập...");
        progressDialog.show();

        // Call the login API
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest("patient", strEmail, strPassword);

        Call<LoginRespone> call = apiService.login(loginRequest);
        call.enqueue(new Callback<LoginRespone>() {
            @Override
            public void onResponse(Call<LoginRespone> call, Response<LoginRespone> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    LoginRespone loginRespone = response.body();
                    if (loginRespone != null && loginRespone.getResult() == 1) {
                        saveToken(loginRespone.getAccessToken());
                        Utils.user = loginRespone;
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        DialogUtils.showErrorDialog(LogInActivity.this, loginRespone.getMsg());
                    }
                } else {
                    DialogUtils.showErrorDialog(LogInActivity.this, "Request failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginRespone> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                DialogUtils.showErrorDialog(LogInActivity.this, t.getMessage());
                Log.d("Login", t.getMessage());
            }
        });
    }

    private void saveToken(String token) {
        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .putString("token", token)
                .apply();
    }

}
