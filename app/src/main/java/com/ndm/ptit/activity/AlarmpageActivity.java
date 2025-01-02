package com.ndm.ptit.activity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ndm.ptit.R;
import com.ndm.ptit.fragment.AlarmpageFragment;

public class AlarmpageActivity extends AppCompatActivity {

    private final String TAG = "Alarm-page Activity";
    private final FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmpage);

        String fragmentTag = "AlarmFragment";
        Fragment fragment = new AlarmpageFragment();



        FragmentTransaction transaction = manager.beginTransaction();

        try
        {

            transaction.replace(R.id.frameLayout, fragment, fragmentTag);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        catch (Exception ex)
        {
            System.out.println(TAG);
            ex.getStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        this.finish();
    }
}