package com.india.tourism;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ArrayAdapter extends RecyclerView.Adapter<ArrayAdapter.myViewHolder> {

    Context context;
    List<String> city;

    public ArrayAdapter(Context context, List<String> city) {
        this.context = context;
        this.city = city;
    }

    @NonNull
    @Override
    public ArrayAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v=LayoutInflater.from(context).inflate(R.layout.array,parent,false);
       myViewHolder myViewHolder=new myViewHolder(v);
       return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArrayAdapter.myViewHolder holder, final int position) {
     holder.cityname.setText(city.get(position));
     holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Toast.makeText(context, city.get(position), Toast.LENGTH_SHORT).show();
         }
     });
    }

    @Override
    public int getItemCount() {
        return city.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private TextView cityname;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            cityname=itemView.findViewById(R.id.cityr);
        }
    }
}
