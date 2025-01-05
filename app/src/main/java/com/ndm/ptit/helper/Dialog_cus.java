package com.ndm.ptit.helper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ndm.ptit.R;

public class Dialog_cus {
    private View viewAlert;
    private AlertDialog alert;
    private Context context;
    private TextView msgText, alertTitle;
    private ImageView iconAlert;
    public Button btnOK;
    public Button btnCancel;

    /******* CONSTRUCTOR *******/
    public Dialog_cus(Context context) {
        this.context = context;
    }

    /******* CONSTRUCTOR *******/
    public Dialog_cus(Context context, int type) {
        this.context = context;
        if(type == 1){
            this.announce();
        }else{
            this.confirm();
        }
    }

    /**
     * hien thi mot dialog dang thong bao
     */
    public void announce(){
        viewAlert = View.inflate(context, R.layout.dialog_annouce, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewAlert);

        alert = builder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.setCancelable(false);

        setControl();
    }

    /**
     * hien thi mot dialog de nguoi dung lua chon giua "yes" va "no"
     */
    public void confirm(){
        viewAlert = View.inflate(context, R.layout.dialog_confirm, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(viewAlert);

        alert = builder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.setCancelable(false);

        setControl();
    }

    /**
     * anh xa cac thanh phan voi XML layout
     */
    private void setControl(){
        msgText = viewAlert.findViewById(R.id.msgText);
        alertTitle = viewAlert.findViewById(R.id.alertTitle);
        iconAlert = viewAlert.findViewById(R.id.iconAlert);
        btnOK = viewAlert.findViewById(R.id.btnOK);
        btnCancel = viewAlert.findViewById(R.id.btnCancel);
    }

    /**
     *
     * @param title la tieu de cua dialog. Vi du: thong bao, chu y,...
     * @param msg la noi dung cua dialog. Vi du: ban co chac chan khong ?
     * @param ico la icon cua dialog the hien noi dung ma no dang hien thi
     */
    @SuppressLint("NonConstantResourceId")
    public void show(String title, String msg, Integer ico){
        if (ico == R.drawable.ic_close) {
            iconAlert.setBackgroundResource(R.drawable.dialog_background_danger);
        } else if (ico == R.drawable.ic_info) {
            iconAlert.setBackgroundResource(R.drawable.dialog_background_info);
        } else if (ico == R.drawable.ic_check) {
            iconAlert.setBackgroundResource(R.drawable.dialog_background_success);
        }
        iconAlert.setImageResource(ico);
        msgText.setText(msg);
        alertTitle.setText(title);
        alert.show();
    }


    /**
     *
     * @param resid la tieu de cua dialog nhung la dang su dung R.id.string.
     * Vi du: thong bao, chu y,...
     * @param msg la noi dung cua dialog. Vi du: ban co chac chan khong ?
     * @param ico la icon cua dialog the hien noi dung ma no dang hien thi
     */
    @SuppressLint("NonConstantResourceId")
    public void show(Integer resid, String msg, Integer ico){
        String title = context.getResources().getString(resid);
        if (ico == R.drawable.ic_close) {
            iconAlert.setBackgroundResource(R.drawable.dialog_background_danger);
        } else if (ico == R.drawable.ic_info) {
            iconAlert.setBackgroundResource(R.drawable.dialog_background_info);
        } else if (ico == R.drawable.ic_check) {
            iconAlert.setBackgroundResource(R.drawable.dialog_background_success);
        }
        iconAlert.setImageResource(ico);
        msgText.setText(msg);
        alertTitle.setText(title);
        alert.show();
    }

    /**
     * dong dialog lai hoan toan
     */
    public void close(){
        alert.dismiss();
    }
}
