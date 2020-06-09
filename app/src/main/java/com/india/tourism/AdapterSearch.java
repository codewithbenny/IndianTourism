package com.india.tourism;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.india.tourism.Guide.Tourist.DetailBookActivity;
import com.india.tourism.Model.Places;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.Viewholder> {
    private ArrayList<Places>placename;
    Context context;

    public AdapterSearch(ArrayList<Places> placename, Context context) {
        this.placename = placename;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterSearch.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.places,parent,false);
        AdapterSearch.Viewholder viewholder=new AdapterSearch.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSearch.Viewholder holder, int position) {
     final Places places=placename.get(position);
     holder.txtplacename.setText(places.getName());
      Picasso.get().load(places.getImg()).into(holder.imageView);
      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(context, DetailBookActivity.class);
              intent.putExtra("pid",places.getPid());
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent);
          }
      });
    }

    @Override
    public int getItemCount() {
        return placename.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public TextView txtplacename;
        public ImageView imageView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.place_image_item);
            txtplacename=itemView.findViewById(R.id.place_name_item);
        }
    }
}
