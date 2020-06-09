package com.india.tourism.Guide.Tourist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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
import com.india.tourism.About;
import com.india.tourism.R;
import com.india.tourism.TestActivity;
import com.squareup.picasso.Picasso;

public class DetailBookActivity extends AppCompatActivity {
private TextView pname,pdescription,timings,entryfee;
private Button Showguides;
private String Pname;
private ImageView imageView;
private DatabaseReference detailref;
private Toolbar toolbar;
String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        pname=findViewById(R.id.place_name);
        Showguides=findViewById(R.id.bookguide);
        imageView=findViewById(R.id.place_image);
        toolbar=findViewById(R.id.toolbar1213);
        pdescription=findViewById(R.id.place_description);
        timings=findViewById(R.id.place_timings);
        entryfee=findViewById(R.id.place_entryfee);
        detailref=FirebaseDatabase.getInstance().getReference().child("Places");
        String Pid=getIntent().getStringExtra("pid");
        getfulldescription(Pid);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DetailBookActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });


    }
    public void getfulldescription(String pid){
      detailref.child(pid).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if(dataSnapshot.exists()){
                  String image=dataSnapshot.child("Img").getValue().toString();
                   Pname=dataSnapshot.child("Name").getValue().toString();
                  String fee=dataSnapshot.child("EntryFee").getValue().toString();
                  String time=dataSnapshot.child("Timings").getValue().toString();
                final String url=dataSnapshot.child("url").getValue().toString();
                                 entryfee.setText(fee);
                  timings.setText("Timings are:"+time);
                   s = Pname.substring(0, Math.min(Pname.length(), 3));
                  pname.setText(Pname);
                  Showguides.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent intent=new Intent(DetailBookActivity.this, TestActivity.class);
                          intent.putExtra("Pname",s);
                          startActivity(intent);
                      }
                  });
//                  pdescription.setText(fulldes);
                  pdescription.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View view) {
                          Intent intent=new Intent(DetailBookActivity.this, About.class);
                          intent.putExtra("url",url);
                          startActivity(intent);
                      }
                  });
                  Picasso.get().load(image).into(imageView);
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });
    }
}
