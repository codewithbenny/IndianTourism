package com.india.tourism.Guide.Tourist;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.tourism.R;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class GuideDetailsforUserActivity extends AppCompatActivity {
    private TextView name, city, phone, language, places, experience, payscale;
    private ImageView imageView,call,message;
    private Button booknow, chat;
    String phonekey;
    Toolbar toolbar;
    String a,b,c,d;
    private String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_detailsfor_user);
        name = findViewById(R.id.username1);
        city = findViewById(R.id.city1);
        message=findViewById(R.id.message);
        call=findViewById(R.id.call);
        phone = findViewById(R.id.userphn1);
        toolbar=findViewById(R.id.tool);
        places = findViewById(R.id.places1);
        experience = findViewById(R.id.experience1);
        language = findViewById(R.id.language1);
        payscale = findViewById(R.id.payscale12);
        imageView = findViewById(R.id.guideprofile1);
        chat = findViewById(R.id.chat);
         toolbar.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(GuideDetailsforUserActivity.this,AvailableguidesActivity.class);
                 startActivity(intent);
             }
         });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",phonekey, null));
                startActivity(intent);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("smsto:"+phonekey)); // This ensures only SMS apps respond
                intent.putExtra("sms_body", "Hello your Guide is Confirmed");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imageuri= Paper.book().read("Agimage");
                Picasso.get().load(imageuri).into(imageView);

                String url = "https://api.whatsapp.com/send?phone="+"91"+phonekey;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        phonekey = getIntent().getStringExtra("guideid");
        booknow=findViewById(R.id.booknow);
        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("myinfo", 0);
              String  phonekey121 = preferences.getString("userphone", null);
                  if(phonekey121==null){
                      Toast.makeText(GuideDetailsforUserActivity.this, "Dear Customer Please Login before Book", Toast.LENGTH_SHORT).show();
                      Intent intent=new Intent(GuideDetailsforUserActivity.this,LogintouristActivity.class);
                      startActivity(intent);
                  }else {
                      Intent intent = new Intent(GuideDetailsforUserActivity.this, ConfirmGuide.class);
                      intent.putExtra("phonekey", phonekey);
                      startActivity(intent);
                  }
            }
        });




        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("ApprovedGuides").child(phonekey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String name1=dataSnapshot.child("Agname").getValue().toString();
                    String city1=dataSnapshot.child("Agcity").getValue().toString();
                    String lan=dataSnapshot.child("Aglanguage").getValue().toString();
                    String exp=dataSnapshot.child("Agexperience").getValue().toString();
                   String pay=dataSnapshot.child("Apayscale").getValue().toString();
                    String phn1=dataSnapshot.child("Agphone").getValue().toString();
                    String place=dataSnapshot.child("Agplace").getValue().toString();
                    if(dataSnapshot.child("Agimage").exists()) {
                       img = dataSnapshot.child("Agimage").getValue().toString();
                        Picasso.get().load(img).placeholder(R.drawable.download).into(imageView);
                    }

                    name.setText("Name:  "+name1);
                    city.setText("City:  "+city1);
                    language.setText("Language:  "+lan);
                    experience.setText("Experience :"+exp+" years");
                    payscale.setText("Pay:  "+pay);
                    phone.setText("Phone:  "+phn1);
                    places.setText("Place:  "+place);

//                    SharedPreferences preferences =getApplicationContext().getSharedPreferences("myinfo",0);
//                    SharedPreferences.Editor editor=preferences.edit();
//                    editor.putString("Name",name1);
//                    editor.putString("phone",phn1);
//                    if(img!=null){
//                        editor.putString("img",img);
//                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
