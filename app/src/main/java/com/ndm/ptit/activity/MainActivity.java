package com.ndm.ptit.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ndm.ptit.R;
import com.ndm.ptit.fragment.AppointmentpageFragment;
import com.ndm.ptit.fragment.MainFragment;
import com.ndm.ptit.fragment.NotificationFragment;
import com.ndm.ptit.fragment.SettingsFragment;

import java.lang.ref.WeakReference;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Homepage Activity";
    private Dialog dialog;
//    private GlobalVariable globalVariable;

    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private String fragmentTag;

    private SharedPreferences sharedPreferences;

    /*Weak Activity & GETTER*/
    public static WeakReference<MainActivity> weakActivity;

//    public MainActivity(BottomNavigationView bottomNavigationView) {
//        this.bottomNavigationView = bottomNavigationView;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationMenu);

        fragment = new MainFragment();
        fragmentTag = "homeFragment";
        enableFragment(fragment, fragmentTag);

        setupVariable();
        setupEvent();
        setNumberOnNotificationIcon();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        setNumberOnNotificationIcon();
//        Tooltip.setLocale(this, sharedPreferences);
    }

    private void setupVariable() {
//        globalVariable = (GlobalVariable) this.getApplication();
        dialog = new Dialog(this);

//        sharedPreferences = this.getApplication()
//                .getSharedPreferences(globalVariable.getSharedReferenceKey(), MODE_PRIVATE);
//        bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
    }


    /**
     * @since 17-11-2022
     * setup event
     */
    @SuppressLint("NonConstantResourceId")
    private void setupEvent() {
        /* Set up event when users click on an item in bottom navigation view */
        bottomNavigationView.setOnItemSelectedListener(item -> {
            /* Whenever users click on any icon, update the number of unread notifications */

            int shortcut = item.getItemId();
            if (shortcut == R.id.shortcutHome) {
                // setNumberOnNotificationIcon();
                fragment = new MainFragment();
                fragmentTag = "homeFragment";
            } else if (shortcut == R.id.shortcutNotification) {
                // setNumberOnNotificationIcon();
                fragment = new NotificationFragment();
                fragmentTag = "notificationFragment";
            } else if (shortcut == R.id.shortcutAppointment) {
                fragment = new AppointmentpageFragment();
                fragmentTag = "appointmentFragment";
            } else if (shortcut == R.id.shortcutPersonality) {

                fragment = new SettingsFragment();
                fragmentTag = "settingsFragment";
            }

            enableFragment(fragment, fragmentTag);
            return true;
        });
    }

    /**
     * @author Phong-Kaster
     * activate a fragment right away
     */
    public void enableFragment(Fragment fragment, String fragmentTag) {
        /*Step 0 - update again fragmentTag to handle onBackPress()*/
        this.fragmentTag = fragmentTag;

        /*Step 1*/
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();


        /*Step 2*/
//        Map<String, String > headers = ((GlobalVariable)getApplication()).getHeaders();
//        String accessToken = headers.get("Authorization");
//        String contentType = headers.get("Content-Type");


        /*Step 3*/
//        Bundle bundle = new Bundle();
//        bundle.putString("accessToken", accessToken);
//        bundle.putString("contentType", contentType);
//        fragment.setArguments(bundle);


        /*Step 4*/
        transaction.replace(R.id.frameLayout, fragment, fragmentTag);
        transaction.commit();
    }

    //
//    /**
//     * @since 17-11-2022
//     * if users click back button by accident or on purpose
//     * a confirm dialog will pop up to make sure they want to exit the application
//     */
//    @Override
//    public void onBackPressed() {
//        dialog.confirm();
//        dialog.show(getString(R.string.attention),
//                getString(R.string.are_you_sure_about_that), R.drawable.ic_info);
//        dialog.btnOK.setOnClickListener(view->{
//            super.onBackPressed();
//            finish();
//        });
//        dialog.btnCancel.setOnClickListener(view-> dialog.close());
//    }
//
//    /**
//     * @since 24-11-2022
//     * this function sets number on the right-top on notification icon
//     * which lays on Bottom Navigation View
//     */
    public void setNumberOnNotificationIcon() {
        /*Step 1 - setup Retrofit*/
//        Retrofit service = HTTPService.getInstance();
//        HTTPRequest api = service.create(HTTPRequest.class);
//
//        /*Step 2 - prepare header*/
//        Map<String, String> header = globalVariable.getHeaders();
//
//        /*Step 3*/
//        Call<NotificationReadAll> container = api.notificationReadAll(header);
//
//        /*Step 4*/
//        container.enqueue(new Callback<NotificationReadAll>() {
//            @Override
//            public void onResponse(@NonNull Call<NotificationReadAll> call, @NonNull Response<NotificationReadAll> response) {
//                /*if successful, update the number of unread notification*/
//                if(response.isSuccessful())
//                {
//                    NotificationReadAll content = response.body();
//                    assert content != null;
//                    /*update the number of unread notification*/
//                    int quantityUnread = content.getQuantityUnread();
//                    bottomNavigationView
//                            .getOrCreateBadge(R.id.shortcutNotification)
//                            .setNumber(quantityUnread);
//                }
//                /*if fail, show exception*/
//                if(response.errorBody() != null)
//                {
//                    System.out.println(response);
//                    try
//                    {
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                        System.out.println( jObjError );
//                    }
//                    catch (Exception e) {
//                        System.out.println(TAG);
//                        System.out.println("Exception: " + e.getMessage() );
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<NotificationReadAll> call, @NonNull Throwable t) {
//                System.out.println(TAG);
//                System.out.println("setNumberOnNotificationIcon - error: " + t.getMessage());
//            }
//        });
    }
//
//
//    /**
//     * @since 01-12-2022
//     * exit the application
//     * this function is called in settingRecyclerView - line 64
//     */
//    public void exit()
//    {
//        SharedPreferences sharedPreferences = this.getApplication()
//                .getSharedPreferences(globalVariable.getSharedReferenceKey(), MODE_PRIVATE);
//
//        sharedPreferences.edit().putString("accessToken", null).apply();
//        sharedPreferences.edit().putInt("darkMode", 1).apply();// 1 is off, 2 is on
//        sharedPreferences.edit().putString("language", getString(R.string.vietnamese)).apply();
//
//
//        System.out.println(TAG);
//        System.out.println("access token: " + sharedPreferences.getString("accessToken", null) );
//
//        Intent intent = new Intent(this, LoginActivity.class);
//        finish();
//        startActivity(intent);
//    }
}