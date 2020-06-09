package com.india.tourism.ViewHolder;

import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.india.tourism.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class GuideViewHolder extends  RecyclerView.ViewHolder {
public CircleImageView imageprofile;
public TextView name,place,language;
public GuideViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.username);
        place=itemView.findViewById(R.id.userplace);
        language=itemView.findViewById(R.id.userlanguage);
        imageprofile=itemView.findViewById(R.id.userprofile);
        }


        }
