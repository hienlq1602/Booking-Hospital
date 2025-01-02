package com.ndm.ptit.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ndm.ptit.R;
import com.ndm.ptit.activity.BookingpageInfoActivity;
import com.ndm.ptit.enitities.booking.Booking;
import com.ndm.ptit.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookingRecyclerView extends RecyclerView.Adapter<BookingRecyclerView.ViewHolder> {

    private final Context context;
    private final List<Booking> list;

    public BookingRecyclerView(Context context, List<Booking> list)
    {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_view_element_booking, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking element = list.get(position);

        String id = String.valueOf(element.getId());

        String serviceName = element.getService().getName();
        holder.serviceName.setText(serviceName);


        if(element.getService().getAvatar() != null)
        {
            String serviceImage = element.getService().getAvatar();
            Picasso.get().load(serviceImage).into(holder.serviceImage);
        }

        String datetime = element.getAppointmentTime();
        holder.datetime.setText(datetime);

        String bookingNameText = element.getBookingName() != null ? element.getBookingName() : Utils.user.getData().getName();
        String bookingName = context.getString(R.string.bookingName2) + ": " + bookingNameText;
        holder.bookingName.setText(bookingName);

        String bookingPhoneText = element.getBookingPhone() != null ? element.getBookingPhone() : Utils.user.getData().getPhone();
        String bookingPhone = context.getString(R.string.bookingPhone2) + ": " + bookingPhoneText;
        holder.bookingPhone.setText(bookingPhone);

        String patientName = context.getString(R.string.patientName2) + ": " + element.getName();
        holder.patientName.setText(patientName);

        String patientReason = context.getString(R.string.patientReason) + ": " + element.getReason();
        holder.patientReason.setText(patientReason);


        String status = element.getStatus();
        /*Show appointment status or button remind me*/
        switch (status){
            case "VERIFIED":
                holder.statusDone.setVisibility(View.VISIBLE);
                holder.statusCancel.setVisibility(View.GONE);
                holder.statusProcessing.setVisibility(View.GONE);
                break;
            case "CANCEL":
                holder.statusDone.setVisibility(View.GONE);
                holder.statusCancel.setVisibility(View.VISIBLE);
                holder.statusProcessing.setVisibility(View.GONE);
                break;
            case "PROCESSING":
                holder.statusDone.setVisibility(View.GONE);
                holder.statusCancel.setVisibility(View.GONE);
                holder.statusProcessing.setVisibility(View.VISIBLE);
        }


        holder.layout.setOnClickListener(view -> {
            Intent intent = new Intent(context, BookingpageInfoActivity.class);
            intent.putExtra("id", id);
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        private final LinearLayout layout;

        private final ImageView serviceImage;
        private final TextView serviceName;

        private final TextView datetime;
        private final TextView bookingName;
        private final TextView bookingPhone;
        private final TextView patientName;
        private final TextView patientReason;
        private final TextView statusDone;
        private final TextView statusCancel;
        private final TextView statusProcessing;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.elementLayout);

            serviceImage = itemView.findViewById(R.id.elementServiceImage);
            serviceName = itemView.findViewById(R.id.elementServiceName);

            datetime = itemView.findViewById(R.id.elementDatetime);
            bookingName = itemView.findViewById(R.id.elementBookingName);
            bookingPhone = itemView.findViewById(R.id.elementBookingPhone);
            patientName = itemView.findViewById(R.id.elementPatientName);
            patientReason = itemView.findViewById(R.id.elementPatientReason);
            statusCancel = itemView.findViewById(R.id.elementStatusCancel);
            statusDone = itemView.findViewById(R.id.elementStatusDone);
            statusProcessing = itemView.findViewById(R.id.elementStatusProcessing);
        }
    }
}
