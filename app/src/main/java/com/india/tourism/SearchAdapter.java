package com.india.tourism;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.india.tourism.Guide.Tourist.ConfirmGuide;
import com.india.tourism.Guide.Tourist.GuideDetailsforUserActivity;
import com.india.tourism.Guide.Tourist.LogintouristActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Viewholder>{
    ArrayList<String> profile;
    ArrayList<String>city;
    ArrayList<String> languages;
    ArrayList<String> place;
    ArrayList<String> name;
    ArrayList<String> phone;
    ArrayList<String> Exp;
    ArrayList<String> pay;
    Context context;

    public SearchAdapter(ArrayList<String> profile, ArrayList<String> city, ArrayList<String> languages, ArrayList<String> place, ArrayList<String> name, ArrayList<String> phone, ArrayList<String> exp, ArrayList<String> pay, Context context) {
        this.profile = profile;
        this.city = city;
        this.languages = languages;
        this.place = place;
        this.name = name;
        this.phone = phone;
        this.Exp = exp;
        this.pay = pay;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.searchguidelayout,parent,false);
        Viewholder viewholder=new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.Viewholder holder, final int position) {
        if(name.get(position)==null){
            Toast.makeText(context, "Sorry", Toast.LENGTH_SHORT).show();
        }
        holder.name1.setText("Name:" + name.get(position));
        holder.language1.setText("Language: " + languages.get(position));
        holder.place1.setText("Places: " + place.get(position));
        holder.phone1.setText(phone.get(position));
        holder.city1.setText("city: " + city.get(position));
        holder.pay1.setText("Pay: " + pay.get(position) + "INR");
//       holder.experience1.setText("Exp: " + Exp.get(position) + "years");
       Picasso.get().load(profile.get(position)).into(holder.imageprofile);
      holder.chat.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String url = "https://api.whatsapp.com/send?phone="+"91"+phone.get(position);
              Intent intent = new Intent(Intent.ACTION_VIEW);
              intent.setData(Uri.parse(url));
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent);
          }
      });
      holder.book1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              SharedPreferences preferences=context.getSharedPreferences("myinfo",0);

              String phonekey=preferences.getString("userphone",null);
              if(phonekey==null){
                  Toast.makeText(context, "Please Login to Book", Toast.LENGTH_SHORT).show();
                  Intent intent=new Intent(context, LogintouristActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  context.startActivity(intent);
              }else {
                  Intent intent = new Intent(context, ConfirmGuide.class);
                  intent.putExtra("phonekey", phone.get(position));
                  intent.putExtra("name", name.get(position));
                  intent.putExtra("image", profile.get(position));
                  Paper.book().write("only4u2", phone.get(position));
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  context.startActivity(intent);
              }
          }
      });
      holder.phone1.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",phone.get(position), null));
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent);
          }
      });
    }
    @Override
    public int getItemCount() {
        return languages.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public CircleImageView imageprofile;
        public TextView name1,place1,language1,pay1,experience1,phone1,city1;
        public Button chat,book1;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name1=itemView.findViewById(R.id.name97);
            place1=itemView.findViewById(R.id.place97);
            language1=itemView.findViewById(R.id.language97);
            imageprofile=itemView.findViewById(R.id.image97);
            pay1=itemView.findViewById(R.id.payscale97);
            experience1=itemView.findViewById(R.id.exp97);
            phone1=itemView.findViewById(R.id.phn97);
            city1=itemView.findViewById(R.id.city97);
            chat=itemView.findViewById(R.id.chat97);
            book1=itemView.findViewById(R.id.book97);
        }
    }
}