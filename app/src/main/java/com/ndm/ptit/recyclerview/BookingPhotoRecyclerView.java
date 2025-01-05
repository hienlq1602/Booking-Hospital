package com.ndm.ptit.recyclerview;

import static com.ndm.ptit.utils.Utils.BASE_URL;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ndm.ptit.R;
import com.ndm.ptit.enitities.booking.BookingImage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookingPhotoRecyclerView extends RecyclerView.Adapter<BookingPhotoRecyclerView.ViewHolder> {

    private final Context context;
    private final List<BookingImage> list;

    public BookingPhotoRecyclerView(Context context, List<BookingImage> list)
    {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recycler_view_element_booking_photo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingImage element = list.get(position);
//        String url = BASE_URL + element.getUrl();
        String url =  element.getUrl();

        Uri uri = Uri.parse(url);
        Picasso.get()
                .load(uri)
                .into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView photo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.elementPhoto);
        }
    }
}
