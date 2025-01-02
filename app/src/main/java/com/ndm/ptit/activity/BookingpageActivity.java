package com.ndm.ptit.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ndm.ptit.R;
import com.ndm.ptit.fragment.BookingFragment1;

public class BookingpageActivity extends AppCompatActivity {
    private final String TAG = "Booking-page Activity";
    private ImageButton btnBack;
    private final FragmentManager manager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingpage);
        setupBookingFragment1();
        setupComponent();
        setupEvent();
    }

    private void setupBookingFragment1()
    {
        String fragmentTag = "BookingFragment1";
        Fragment fragment = new BookingFragment1();


        /*Step 1*/
        FragmentTransaction transaction = manager.beginTransaction();


        String serviceId = getIntent().getStringExtra("serviceId");
        String doctorId = getIntent().getStringExtra("doctorId");

        Bundle bundle = new Bundle();
        bundle.putString("serviceId", serviceId);
        bundle.putString("doctorId", doctorId);
        fragment.setArguments(bundle);


        transaction.replace(R.id.frameLayout, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void setupComponent()
    {
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupEvent()
    {
        btnBack.setOnClickListener(view-> finish());
    }

}