//package com.ndm.ptit.activity;
//
//import android.os.Bundle;
//import android.widget.ImageView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.ndm.ptit.R;
//import com.squareup.picasso.Picasso;
//
//public class ImageViewerActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image_viewer);
//
//        ImageView imageView = findViewById(R.id.fullScreenImageView);
//
//        // Nhận URL từ Intent
//        String imageUrl = getIntent().getStringExtra("imageUrl");
//
//        // Tải ảnh bằng Picasso
//        Picasso.get()
//                .load(imageUrl)
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.error)
//                .into(imageView);
//    }
//}