package com.ndm.ptit.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ndm.ptit.R;
import com.ndm.ptit.enitities.appointment.Appointment_queue;

import java.util.List;

public class AppointmentQueueRecyclerView extends RecyclerView.Adapter<AppointmentQueueRecyclerView.ViewHolder> {

    private final Context context;
    private final List<Appointment_queue> list;
    private final int myPosition;

    public AppointmentQueueRecyclerView(Context context, List<Appointment_queue> list, int  myPosition)
    {
        this.context = context;
        this.list = list;
        this.myPosition = myPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_view_element_appointment_queue, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment_queue appointment = list.get(position);


        int yourPosition = appointment.getPosition();
        int yourPositionIndex = list.indexOf(appointment);
        String numericalOrder = String.valueOf(appointment.getNumericalOrder());
        String patientName = appointment.getPatientName();
        String status = appointment.getStatus();

//        holder.elementNumericalOrder.setText(String.valueOf(yourPosition)+". ");
        holder.elementNPositiom.setText(String.valueOf(yourPositionIndex+1)+". ");
        holder.elementPatientName.setText(patientName  + " " + numericalOrder);


        /*position == 0 means he is the first one of the list*/
        if( status.equals("EXAMINATING") )
        {
            holder.elementStatus.setText(R.string.now);
            holder.elementStatus.setVisibility(View.VISIBLE);
            holder.elementStatus.setTextColor(context.getResources().getColor(R.color.colorGreen, null));
            holder.elementNPositiom.setTextColor(context.getResources().getColor(R.color.colorGreen, null));
            holder.elementPatientName.setTextColor(context.getResources().getColor(R.color.colorGreen, null));
        }
        else
        {
            holder.elementStatus.setVisibility(View.GONE);
            holder.elementNPositiom.setTextColor(context.getResources().getColor(R.color.colorTextBlack, null));
            holder.elementPatientName.setTextColor(context.getResources().getColor(R.color.colorTextBlack, null));
        }

        /*if your position in queue lays on the list-> highlight your position with orange color & send notification*/
        if( yourPosition == myPosition)
        {
            /*to mau CAM cho ten nguoi dung*/
            holder.elementStatus.setVisibility(View.VISIBLE);
            holder.elementStatus.setText("Bạn");

            holder.elementStatus.setTextColor(context.getResources().getColor(R.color.colorOrange, null));
            holder.elementPatientName.setTextColor(context.getResources().getColor(R.color.colorOrange, null));
            holder.elementNPositiom.setTextColor(context.getResources().getColor(R.color.colorOrange, null));

            /*tao noi dung cho Notification*/
//            com.example.do_an_tot_nghiep.Helper.Notification notification = new com.example.do_an_tot_nghiep.Helper.Notification(context);
//            String title = context.getString(R.string.app_name);
//            String text = context.getString(R.string.it_is_your_turn);
//            String bigText = patientName + " ơi! Hãy chuẩn bị, sắp tới lượt khám của bạn rồi!";
//            notification.setup(title, text, bigText );
//            notification.show();
        }
        if( yourPosition == myPosition && status.equals("EXAMINATING"))
        {
            /*to mau CAM cho ten nguoi dung*/
            holder.elementStatus.setVisibility(View.VISIBLE);
            holder.elementStatus.setText("Bạn - Hiện tại");

            holder.elementStatus.setTextColor(context.getResources().getColor(R.color.colorGreen, null));
            holder.elementPatientName.setTextColor(context.getResources().getColor(R.color.colorGreen, null));
            holder.elementNPositiom.setTextColor(context.getResources().getColor(R.color.colorGreen, null));

            /*tao noi dung cho Notification*/
//            com.example.do_an_tot_nghiep.Helper.Notification notification = new com.example.do_an_tot_nghiep.Helper.Notification(context);
//            String title = context.getString(R.string.app_name);
//            String text = context.getString(R.string.it_is_your_turn);
//            String bigText = patientName + " ơi! Hãy chuẩn bị, sắp tới lượt khám của bạn rồi!";
//            notification.setup(title, text, bigText );
//            notification.show();
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView elementNPositiom;
        private final TextView elementPatientName;
        private final TextView elementStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            elementNPositiom = itemView.findViewById(R.id.elementNumericalOrder);
            elementPatientName = itemView.findViewById(R.id.elementPatientName);
            elementStatus = itemView.findViewById(R.id.elementStatus);
        }
    }
}
