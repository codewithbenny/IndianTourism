package com.india.tourism.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.india.tourism.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView Rimageprofile;
    public ImageView message;
    public TextView Rname,Rdate,Rphn,Rvisitorlen;
    public RequestViewHolder(@NonNull View itemView) {

        super(itemView);
         Rname=itemView.findViewById(R.id.requestusername);
        Rphn=itemView.findViewById(R.id.requestphn);
        Rdate=itemView.findViewById(R.id.userdate);
        Rvisitorlen=itemView.findViewById(R.id.len);
          Rimageprofile=itemView.findViewById(R.id.requestprofile);
          message=itemView.findViewById(R.id.messsend);
    }
}
