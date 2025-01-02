package com.ndm.ptit.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ndm.ptit.R;

public class LoadingScreen2 {
    private Activity activity;
    private AlertDialog dialog;

    private TextView textView;

    private String textView2;

    public LoadingScreen2(Activity activity, String textView2) {
        this.activity = activity;
        this.textView2 = textView2;
    }

    @SuppressLint("InflateParams")
    public void start() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        // Inflate view from layout
        View view = inflater.inflate(R.layout.loading_screen, null);
        builder.setView(view);
        builder.setCancelable(false);

        // Now get the TextView from the inflated view
        textView = view.findViewById(R.id.textView);
        // Set the text for the TextView (optional)
        textView.setText(textView2);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public void stop() {
        if (dialog == null) return;
        dialog.dismiss();
    }
}
