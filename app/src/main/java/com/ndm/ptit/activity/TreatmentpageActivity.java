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
import com.ndm.ptit.fragment.TreatmentFragment1;

public class TreatmentpageActivity extends AppCompatActivity {

    private final String TAG = "Treatment-page Activity";
    private SharedPreferences sharedPreferences;

    private ImageButton btnBack;
    private final FragmentManager manager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatmentpage);

        setupComponent();
        setupEvent();
        setupTreatmentFragment1();
    }
    private void setupComponent()
    {
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupTreatmentFragment1()
    {
        String fragmentTag = "TreatmentFragment1";
        Fragment fragment = new TreatmentFragment1();


        /*Step 1*/
        FragmentTransaction transaction = manager.beginTransaction();

        /*Step 2*/
        transaction.replace(R.id.frameLayout, fragment, fragmentTag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void setupEvent()
    {
        btnBack.setOnClickListener(view-> this.finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


}