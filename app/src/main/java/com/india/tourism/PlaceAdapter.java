package com.india.tourism;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.india.tourism.Model.ApprovedGuide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.Viewholder> {
    ArrayList<ApprovedGuide> approvedGuides;
    Context context;

    public PlaceAdapter(ArrayList<ApprovedGuide> approvedGuides, Context context) {
        this.approvedGuides = approvedGuides;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaceAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.guide_layout,parent,false);
        PlaceAdapter.Viewholder viewholder=new PlaceAdapter.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.Viewholder holder, int position) {
    ApprovedGuide approvedGuide=approvedGuides.get(position);
    holder.name.setText(approvedGuide.getAgplace());
    holder.language.setText(approvedGuide.getAglanguage());
    holder.place.setText(approvedGuide.getAgplace());
    }

    @Override
    public int getItemCount() {
        return approvedGuides.size();

    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public CircleImageView imageprofile;
        public TextView name,place,language;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.username);
            place=itemView.findViewById(R.id.userplace);
            language=itemView.findViewById(R.id.userlanguage);
            imageprofile=itemView.findViewById(R.id.userprofile);
        }
    }
}
