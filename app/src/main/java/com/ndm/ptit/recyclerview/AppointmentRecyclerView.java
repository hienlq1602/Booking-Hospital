package com.ndm.ptit.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.ndm.ptit.R;
import com.ndm.ptit.activity.AlarmpageActivity;
import com.ndm.ptit.activity.AppointmentpageInfoActivity;
import com.ndm.ptit.enitities.Doctor;
import com.ndm.ptit.enitities.appointment.Appointment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AppointmentRecyclerView extends RecyclerView.Adapter<AppointmentRecyclerView.ViewHolder> {

    private final Context context;
    private final List<Appointment> list;

    public AppointmentRecyclerView(Context context, List<Appointment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_view_element_appointment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int index) {
        Appointment appointment = list.get(index);

        int bookingid = appointment.getBookingId();
        int recordId = appointment.getId();
        int doctorId = appointment.getDoctorId();
        int patientPosition = appointment.getPosition();
        String doctorName = context.getString(R.string.doctor) + " " + appointment.getDoctorName();
//        String doctorAvatar = appointment.getDoctorAvatarUrl(); // assuming there's a method to get doctor avatar URL
        String location = appointment.getRoom().getLocation() + ", " + appointment.getRoom().getName();

        String position = context.getString(R.string.your_position) + " " + appointment.getPosition();
        String reason = context.getString(R.string.your_reason) + " " + appointment.getPatientReason();

        String status = appointment.getStatus();

        // Load doctor avatar
//        if (doctorAvatar != null && !doctorAvatar.isEmpty()) {
//            Picasso.get().load(doctorAvatar).into(holder.doctorAvatar);
//        }

        // Set status and visibility for buttons
        int avatar = R.drawable.default_avatar;
//        if(bookingid == 1){
//            avatar = R.drawable.img_dauxuongkhop;
//        }
        switch (bookingid){
            case 1:
//                serviceImage.setImageResource(R.drawable.img_niengrang);
                avatar = R.drawable.img_niengrang;
                break;
            case 2 :
//                serviceImage.setImageResource(R.drawable.img_khamxoang);
                avatar = R.drawable.img_khamxoang;
                break;
            case 3 :
//                serviceImage.setImageResource(R.drawable.img_diennaodo);
                avatar = R.drawable.img_diennaodo;
                break;
            case 4 :
//                serviceImage.setImageResource(R.drawable.img_sankhoa);
                avatar = R.drawable.img_sankhoa;
                break;
            case 5 :
//                serviceImage.setImageResource(R.drawable.img_khamrang);
                avatar = R.drawable.img_khamrang;
                break;
            case 6 :
//                serviceImage.setImageResource(R.drawable.img_khammat);
                avatar = R.drawable.img_khammat;
                break;
            case 7 :
//                serviceImage.setImageResource(R.drawable.img_daychang);
                avatar = R.drawable.img_daychang;
                break;
            default:
//                serviceImage.setImageResource(R.drawable.img_dauxuongkhop);
                avatar = R.drawable.img_dauxuongkhop;
                break;
        }

        Picasso.get().load(avatar).into(holder.doctorAvatar);

        switch (status) {
            case "EXAMINATING":
                holder.default_reason2.setVisibility(View.VISIBLE);
                holder.btnRemindMe.setVisibility(View.GONE);
                holder.statusDone.setVisibility(View.GONE);
                holder.statusCancel.setVisibility(View.GONE);
                break;
            case "PROCESSING":
                holder.btnRemindMe.setVisibility(View.VISIBLE);
                holder.statusDone.setVisibility(View.GONE);
                holder.statusCancel.setVisibility(View.GONE);
                break;
            case "DONE":
                holder.btnRemindMe.setVisibility(View.GONE);
                holder.statusDone.setVisibility(View.VISIBLE);
                holder.statusCancel.setVisibility(View.GONE);
                break;
            case "CANCELLED":  //cancelled
                holder.btnRemindMe.setVisibility(View.GONE);
                holder.statusDone.setVisibility(View.GONE);
                holder.statusCancel.setVisibility(View.VISIBLE);
                break;
            default:
                holder.btnRemindMe.setVisibility(View.GONE);
                holder.statusDone.setVisibility(View.GONE);
                holder.statusCancel.setVisibility(View.GONE);
                break;
        }

        holder.reason.setText(reason);
        holder.position.setText(position);
        holder.location.setText(location);
        holder.doctorName.setText(doctorName);

        // Remind Me button action
        holder.btnRemindMe.setOnClickListener(view -> {
//            Toast.makeText(context, context.getString(R.string.you_will_get_notification_as_soon_as_your_turn), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, AlarmpageActivity.class);
            context.startActivity(intent);

        });

        // Set click listener for layout
        holder.layout.setOnClickListener(view -> {
            Intent intent = new Intent(context, AppointmentpageInfoActivity.class);
            intent.putExtra("id", String.valueOf(recordId));
            intent.putExtra("position", String.valueOf(patientPosition));
            intent.putExtra("doctorId", String.valueOf(doctorId));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout layout;
        private final ImageView doctorAvatar;
        private final TextView doctorName;
        private final TextView location;
        private final TextView position;
        private final TextView reason;
        private final AppCompatButton btnRemindMe;
        private final TextView statusDone;
        private final TextView statusCancel;
        private final TextView default_reason2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.elementLayout);
            doctorAvatar = itemView.findViewById(R.id.elementDoctorImage);
            doctorName = itemView.findViewById(R.id.elementDoctorName);
            location = itemView.findViewById(R.id.elementLocation);
            position = itemView.findViewById(R.id.elementPosition);
            reason = itemView.findViewById(R.id.elementReason);
            btnRemindMe = itemView.findViewById(R.id.elementBtnRemindMe);
            statusDone = itemView.findViewById(R.id.elementStatusDone);
            statusCancel = itemView.findViewById(R.id.elementStatusCancel);
            default_reason2 = itemView.findViewById(R.id.default_reason2);
        }
    }
}
