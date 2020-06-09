package com.india.tourism.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.india.tourism.R;

public class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtplacename,txtplaceDescription;
    public ImageView imageView;



    public PlacesViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.place_image_item);
        txtplacename=itemView.findViewById(R.id.place_name_item);
    }


    @Override
    public void onClick(View view) {

    }
}
