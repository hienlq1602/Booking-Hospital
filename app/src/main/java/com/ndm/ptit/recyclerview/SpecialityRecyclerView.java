package com.ndm.ptit.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.do_an_tot_nghiep.Configuration.Constant;
//import com.example.do_an_tot_nghiep.Model.Speciality;
//import com.example.do_an_tot_nghiep.R;
//import com.example.do_an_tot_nghiep.Specialitypage.SpecialitypageActivity;
import com.ndm.ptit.R;
import com.ndm.ptit.activity.SpecialitypageActivity;
import com.ndm.ptit.enitities.speciality.SpecialityResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author Phong-Kaster
 * @since 22-11-2022
 * speciality recycler view
 */
public class SpecialityRecyclerView extends RecyclerView.Adapter<SpecialityRecyclerView.ViewHolder> {

    private Context context;
    private List<SpecialityResponse> list;
    private int layoutElement;// is the layout for the recyclerView, include 2 layouts: recycler_view_element_speciality
    // & recycler_view_element_speciality_2

    public SpecialityRecyclerView(Context context, List<SpecialityResponse> list, int layoutElement)
    {
        this.context = context;
        this.list = list;
        this.layoutElement = layoutElement;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(layoutElement, parent, false);

        return new SpecialityRecyclerView.ViewHolder(view);
    }

    @Override
    @SuppressLint("ResourceType")
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SpecialityResponse element = list.get(position);
        String uploadUri = element.getImage();

        int id = element.getId();
        String name = element.getName();
        String image = element.getImage().length() > 0 ?
                uploadUri + element.getImage() : context.getString(R.drawable.img_dauxuongkhop);


        if( element.getImage().length() > 0)
        {
            Picasso.get().load(image).into(holder.image);
        }

        holder.name.setText(name);
        holder.layout.setOnClickListener(view->{
            Intent intent = new Intent(context, SpecialitypageActivity.class);
            intent.putExtra("specialityId",String.valueOf(id) );
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout layout;
        private ImageView image;
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.elementLayout);
            image = itemView.findViewById(R.id.elementImage);
            name = itemView.findViewById(R.id.elementName);
        }
    }
}
