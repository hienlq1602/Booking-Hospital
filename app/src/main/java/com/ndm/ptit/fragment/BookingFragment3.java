package com.ndm.ptit.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndm.ptit.R;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.BaseResponse2;
import com.ndm.ptit.enitities.booking.Booking;
import com.ndm.ptit.enitities.booking.BookingImage;
import com.ndm.ptit.helper.Dialog;
import com.ndm.ptit.helper.LoadingScreen;
import com.ndm.ptit.recyclerview.BookingPhotoRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingFragment3 extends Fragment {

    private String bookingId;
    private AppCompatButton btnNext, btnUpload;
    private RecyclerView recyclerView;
    private BookingPhotoRecyclerView adapter;

    private LinearLayout layout;
    private Context context;
    private Activity activity;

    private LoadingScreen loadingScreen;
    private Dialog dialog;
    private List<BookingImage> list = new ArrayList<>();

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Map<String, String> body;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking3, container, false);


        Bundle bundle = getArguments();
        if (bundle != null) {
            body = (Map<String, String>) bundle.getSerializable("body");
        }

        Log.d("body 2 ", body.toString());

        setupComponent(view);
        setupEvent();
        setupRecyclerView();

        return view;
    }

    private void setupComponent(View view) {
        Bundle bundle = getArguments();
        assert bundle != null;

        context = requireContext();
        activity = requireActivity();
        dialog = new Dialog(context);
        loadingScreen = new LoadingScreen(activity);

        recyclerView = view.findViewById(R.id.recyclerView);
        layout = view.findViewById(R.id.linearLayout);
        btnUpload = view.findViewById(R.id.btnUpload);
        btnNext = view.findViewById(R.id.btnNext);


    }

    private void setupRecyclerView() {
        adapter = new BookingPhotoRecyclerView(context, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // Không hỗ trợ kéo thả
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Lấy vị trí của item bị vuốt
                int position = viewHolder.getAdapterPosition();

                // Xóa ảnh khỏi danh sách
                list.remove(position);

                // Cập nhật adapter
                adapter.notifyItemRemoved(position);
            }
        });

        // Gán ItemTouchHelper cho RecyclerView
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupEvent() {
        btnUpload.setOnClickListener(view -> {
            verifyStoragePermissions(activity);
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        btnNext.setOnClickListener(view -> {
            createBookingApiCall();
        });
    }

    private void uploadPhotoToServer(Uri fileUri, int bookingId) {
        File file = new File(getRealPathFromURI(fileUri));
        if (!file.exists()) {
            DialogUtils.showErrorDialog(getContext(), "File không tồn tại, vui lòng kiểm tra.");
            return;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody bookingIdPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(bookingId));

        SharedPreferences prefs = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null || token.isEmpty()) {
            DialogUtils.showErrorDialog(getContext(), "Token bị thiếu. Vui lòng đăng nhập lại.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.uploadBookingPhoto("Bearer " + token, body, bookingIdPart)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            BookingFragment2 nextFragment = new BookingFragment2();
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frameLayout, nextFragment)
                                    .addToBackStack("bookingFragment2")
                                    .commitAllowingStateLoss();
                        } else {
                            DialogUtils.showErrorDialog(getContext(), "Lỗi tải ảnh lên: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        DialogUtils.showErrorDialog(getContext(), "uploadPhotoToServer thất bại: " + t.getMessage());
                    }
                });
    }


    private void createBookingApiCall() {
        SharedPreferences prefs = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null || token.isEmpty()) {
            DialogUtils.showErrorDialog(getContext(), "Token is missing. Please log in again.");
            return;
        }

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.createBooking("Bearer " + token, body)
                .enqueue(new Callback<BaseResponse2<Booking>>() {
                    @Override
                    public void onResponse(Call<BaseResponse2<Booking>> call, Response<BaseResponse2<Booking>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getResult() == 1) {
                            Booking bookings = response.body().getData();
                            if (bookings != null) {
                                if (list.size() > 0) {
                                    uploadImagesAfterBookingCreation(bookings.getId());
                                } else {
                                    BookingFragment2 nextFragment = new BookingFragment2();
                                    requireActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.frameLayout, nextFragment)
                                            .addToBackStack("bookingFragment2")
                                            .commitAllowingStateLoss();
                                }
                            } else {
                                DialogUtils.showErrorDialog(getContext(), "Failed to retrieve booking details.");
                            }
                        } else {
                            // Handle server error or validation error
                            String errorMessage = "Thời gian bị trùng";
                            if (response.body() != null && response.body().getMsg() != null) {
                                 errorMessage = response.body().getMsg(); // Get error message from server response
                            }
                            DialogUtils.showErrorDialog(getContext(), errorMessage);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse2<Booking>> call, Throwable t) {
                        DialogUtils.showErrorDialog(getContext(), "Booking creation failed: " + t.getMessage());
                    }
                });
    }


    private void uploadImagesAfterBookingCreation(int bookingId) {
        for (BookingImage image : list) {
            Uri fileUri = Uri.parse(image.getUrl());
            uploadPhotoToServer(fileUri, bookingId);
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
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
            Uri selectedImageUri = data.getData();
            BookingImage bookingImage = new BookingImage();
            bookingImage.setUrl(selectedImageUri.toString());
            list.add(bookingImage);
            adapter.notifyItemInserted(list.size() - 1);

        }
    }
}
