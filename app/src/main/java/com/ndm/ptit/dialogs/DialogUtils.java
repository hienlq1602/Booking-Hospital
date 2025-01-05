package com.ndm.ptit.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.ndm.ptit.R;
import com.ndm.ptit.activity.DoctorpageActivity;
import com.ndm.ptit.enitities.BaseResponse2;
import com.ndm.ptit.enitities.login.Patient;

import retrofit2.Callback;

public class DialogUtils {

    public static void showErrorDialog(Context context, String message) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_error);
        dialog.setCancelable(true);

        TextView txtMessage = dialog.findViewById(R.id.txtErrorMessage);
        Button btnDismiss = dialog.findViewById(R.id.btnDismiss);

        txtMessage.setText(message);
        btnDismiss.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }
    public static void showConfirmDialog(Context context, String message,String id) {
        Dialog dialogConfirm = new Dialog(context);
        dialogConfirm.setContentView(R.layout.dialog_confirm);
        dialogConfirm.setCancelable(true);

        TextView txtMessage2 = dialogConfirm.findViewById(R.id.txtErrorMessage);
        Button btnOK = dialogConfirm.findViewById(R.id.btnOK);
        Button btnCancel = dialogConfirm.findViewById(R.id.btnCancel);

        txtMessage2.setText(message);

//        btnOK.setOnClickListener(view -> dialogConfirm.dismiss(); intent = new Intent(context, DoctorpageActivity.class);
//        intent.putExtra("doctorId", String.valueOf(id));
//        context.startActivity(intent);)
        btnOK.setOnClickListener(view -> {
                dialogConfirm.dismiss();
                Intent intent = new Intent(context, DoctorpageActivity.class);
                intent.putExtra("doctorId", String.valueOf(id));
                context.startActivity(intent);

        });
        btnCancel.setOnClickListener(view -> dialogConfirm.dismiss());
        dialogConfirm.show();
    }
//    public static void showSucccessDialog(Callback<BaseResponse2<Patient>> context, String message) {
//        Dialog dialogSuccess = new Dialog(context);
//        dialogSuccess.setContentView(R.layout.dialog_error);
//        dialogSuccess.setCancelable(true);
//
//        TextView txtMessage = dialogSuccess.findViewById(R.id.msgText);
//        Button btnOK = dialogSuccess.findViewById(R.id.btnOK);
//
//        txtMessage.setText(message);
//        btnOK.setOnClickListener(view -> dialogSuccess.dismiss());
//
//        dialogSuccess.show();
//    }




//    public static void showSuccessDialog(Context context, String message) {
//        Dialog dialog = new Dialog(context);
//        dialog.setContentView(R.layout.dialog_success);
//        dialog.setCancelable(true);
//
//        TextView txtMessage = dialog.findViewById(R.id.txtSuccessMessage);
//        Button btnDismiss = dialog.findViewById(R.id.btnDismiss);
//
//        txtMessage.setText(message);
//        btnDismiss.setOnClickListener(view -> dialog.dismiss());
//
//        dialog.show();
//    }
}
