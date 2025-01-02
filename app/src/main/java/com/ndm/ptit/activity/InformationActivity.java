package com.ndm.ptit.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ndm.ptit.R;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.enitities.BaseResponse2;

import com.ndm.ptit.enitities.booking.BookingImage;
import com.ndm.ptit.enitities.login.Patient;
import com.ndm.ptit.helper.Dialog;
import com.ndm.ptit.helper.LoadingScreen;

import com.ndm.ptit.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformationActivity extends AppCompatActivity {
    private final String TAG = "Information Activity";

    private static final int REQUEST_PERMISSION = 1;

    private CircleImageView imgAvatar;
    private TextView txtHealthInsuranceNumber;

    private TextView txtEmail;
    private TextView txtName;

    private TextView txtPhone;
    private RadioGroup rgGender;
    private TextView txtBirthday;

    private TextView txtAddress;
    private TextView txtCreateAt;
    private TextView txtUpdateAt;

    private AppCompatButton btnSave;
    private Dialog dialog;
    private LoadingScreen loadingScreen;

    private Uri uriAvatar;
    private AppCompatButton btnUploadAvatar;
    private SharedPreferences sharedPreferences;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        setupComponent();
        showInfo2();
        initListen();
    }
    private void setupComponent()
    {
        imgAvatar = findViewById(R.id.imgAvatar);
        txtHealthInsuranceNumber = findViewById(R.id.txtHealthInsuranceNumber);

        txtEmail = findViewById(R.id.txtEmail);
        txtName = findViewById(R.id.txtName);

        txtPhone = findViewById(R.id.txtPhone);
        rgGender = findViewById(R.id.rgGender);

        txtBirthday = findViewById(R.id.txtBirthday);
        txtAddress = findViewById(R.id.txtAddress);

        txtCreateAt = findViewById(R.id.txtCreateAt);
        txtUpdateAt = findViewById(R.id.txtUpdateAt);

        btnSave = findViewById(R.id.btnSave);
        btnUploadAvatar = findViewById(R.id.btnAvatarUpload);

        dialog = new Dialog(this);
        loadingScreen = new LoadingScreen(this);
    }

    private void initListen(){
        btnSave.setOnClickListener(v -> updateInformation());
        btnUploadAvatar.setOnClickListener(v ->
             uploadAvatar(uriAvatar)
        );

        imgAvatar.setOnClickListener(view -> {
            verifyStoragePermissions(this);
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Tooltip.setLocale(this, sharedPreferences);
    }

    private void showInfo(Patient user)
    {

        String avatar = user.getAvatar();
        String id = String.valueOf(user.getId());

        String email = user.getEmail();
        String name = user.getName();

        String phone = user.getPhone();
        int gender = user.getGender();

        String birthday = user.getBirthday();
        String address = user.getAddress();

        String createAt = user.getCreateAt();
        String updateAt = user.getUpdateAt();

        if( user.getAvatar().length() > 0)
        {
            Picasso.get().load(avatar).into(imgAvatar);
        }
        txtHealthInsuranceNumber.setText(id);
        txtEmail.setText(email);

        txtName.setText(name);
        txtPhone.setText(phone);


        if(gender == 1)
        {
            rgGender.check(R.id.rdMale);
        }
        else
        {
            rgGender.check(R.id.rdFemale);
        }
        txtBirthday.setText(birthday);
        txtAddress.setText(address);
        txtCreateAt.setText(createAt);
        txtUpdateAt.setText(updateAt);
    }

    private void showInfo2() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);


        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse2<Patient>> call = apiService.readPersionalInformation("Bearer " + token);
        call.enqueue(new Callback<BaseResponse2<Patient>>() {
            @Override
            public void onResponse(Call<BaseResponse2<Patient>> call, Response<BaseResponse2<Patient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Patient patient = response.body().getData();
                    showInfo(patient);
                } else {
                    Toast.makeText(InformationActivity.this, "Failed to load personal information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse2<Patient>> call, Throwable t) {
                Toast.makeText(InformationActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadAvatar(Uri imageUri) {
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(getRealPathFromURI(imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Token is missing. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }


        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.uploadAvatar(body, "Bearer " + token, Integer.parseInt(String.valueOf(Utils.user.getData().getId()))).enqueue(new Callback<BaseResponse2<String>>() {
            @Override
            public void onResponse(Call<BaseResponse2<String>> call, Response<BaseResponse2<String>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InformationActivity.this, "Avatar updated successfully", Toast.LENGTH_SHORT).show();
                    showInfo2();
                } else {
                    Log.d("response",response.toString());
                    Toast.makeText(InformationActivity.this, "Failed to upload avatar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse2<String>> call, Throwable t) {
                Log.d("Upload", t.getMessage());
                Toast.makeText(InformationActivity.this, "Upload failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateInformation() {
        // Lấy thông tin từ các trường nhập liệu
        String name = txtName.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();
        String address = txtAddress.getText().toString().trim();
        String birthday = txtBirthday.getText().toString().trim();

        // Kiểm tra nếu có trường nào để trống (có thể thêm validation nếu cần)
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || birthday.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy giới tính từ RadioGroup
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        String gender = selectedGenderId == R.id.rdMale ? "1" : "0";  // 1: Male, 0: Female

        // Tạo Map chứa các dữ liệu cần cập nhật
        Map<String, String> body = new HashMap<>();
        body.put("action", "personal");
        body.put("name", name);
        body.put("email", email);
        body.put("phone", phone);
        body.put("address", address);
        body.put("birthday", birthday);
        body.put("gender", gender);

        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse2<Patient>> call = apiService.changeInformation("Bearer " + token, body);
        call.enqueue(new Callback<BaseResponse2<Patient>>() {
            @Override
            public void onResponse(Call<BaseResponse2<Patient>> call, Response<BaseResponse2<Patient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(InformationActivity.this, "Information updated successfully", Toast.LENGTH_SHORT).show();
                    showInfo2();
                } else {
                    // Nếu có lỗi trong khi cập nhật thông tin
                    Toast.makeText(InformationActivity.this, "Failed to update information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse2<Patient>> call, Throwable t) {
                // Lỗi khi gọi API
                Toast.makeText(InformationActivity.this, "Failed to update information: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriAvatar = data.getData();

            if (uriAvatar != null) {
                Picasso.get()
                        .load(uriAvatar)
                        .into(imgAvatar);
            }
        }
    }

}