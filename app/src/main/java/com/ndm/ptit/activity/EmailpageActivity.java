package com.ndm.ptit.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.ndm.ptit.fragment.EmailFragment1;
import com.ndm.ptit.helper.Tooltip;

public class EmailpageActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private final FragmentManager manager = getSupportFragmentManager();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailpage);

        setupEmailFragment1();
        setupComponent();
        setupEvent();
    }


    private void setupEmailFragment1()
    {
        String fragmentTag = "EmailFragment1";
        Fragment fragment = new EmailFragment1();

        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.frameLayout, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setupComponent()
    {
        btnBack = findViewById(R.id.btnBack);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Tooltip.setLocale(this, sharedPreferences);
    }

    private void setupEvent()
    {
        btnBack.setOnClickListener(view-> finish());
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}