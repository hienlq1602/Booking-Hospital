package com.ndm.ptit.recyclerview;

import static com.ndm.ptit.utils.Utils.BASE_URL;

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

//import com.example.do_an_tot_nghiep.Configuration.Constant;
//import com.example.do_an_tot_nghiep.Doctorpage.DoctorpageActivity;
//import com.example.do_an_tot_nghiep.Model.Doctor;
//import com.example.do_an_tot_nghiep.R;
import com.ndm.ptit.R;
import com.ndm.ptit.activity.DoctorpageActivity;
import com.ndm.ptit.enitities.services.DoctorService;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DoctorRecyclerView extends RecyclerView.Adapter<DoctorRecyclerView.ViewHolder> {

    private final Context context;
    private final List<DoctorService> list;


    public DoctorRecyclerView(Context context, List<DoctorService> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_view_element_doctor, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoctorService element = list.get(position);

        int id = element.getId();

        String specialityName = element.getSpecialityId() != null ? element.getSpecialityId().getName() : "Không xác định";
        String speciality = context.getString(R.string.speciality) + " " + specialityName;

        String name = context.getString(R.string.doctor) + " " + element.getName();

        if (element.getAvatar() != null && !element.getAvatar().isEmpty()) {
            Picasso.get().load(BASE_URL+element.getAvatar()).placeholder(R.drawable.default_avatar).into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.default_avatar);
        }

        holder.name.setText(name);
        holder.speciality.setText(speciality);
        holder.NumberAppoimentByDate.setText(String.valueOf(element.getAppointment_number()));
        holder.layout.setOnClickListener(view -> {
//            DialogUtils.showConfirmDialog(context, "Failed to retrieve booking details.",String.valueOf(id));
//            if(element.getAppointment_number() >= 10){
//                DialogUtils.showConfirmDialog(context, "Failed to retrieve booking details.",String.valueOf(id));
//
//            }else{
                Intent intent = new Intent(context, DoctorpageActivity.class);
                intent.putExtra("doctorId", String.valueOf(id));
                context.startActivity(intent);
//            }

            Log.d("abc",element.toString());




        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        private final LinearLayout layout;
        private final ImageView image;
        private final TextView name;
        private final TextView speciality;
        private final TextView NumberAppoimentByDate;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.elementLayout);
            image = itemView.findViewById(R.id.elementImage);
            name = itemView.findViewById(R.id.elementName);
            speciality = itemView.findViewById(R.id.elementSpeciality);
            NumberAppoimentByDate = itemView.findViewById(R.id.NumberAppoimentByDate);

        }
    }
}
