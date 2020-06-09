package com.india.tourism.Guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.tourism.R;

import java.util.HashMap;

public class GuideRegisterActivity extends AppCompatActivity {
private EditText gname,gphone,gpassword,glanguage,gcity,gplaces,gpayscale,gexperience;
private Button createaccount;
private String name,city,language,phone,password,payscale,experience,places,language2,language3,place2,place3,place4;
private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_register);
        gname=findViewById(R.id.guidename);
        gcity=findViewById(R.id.Enter_city);
        gphone=findViewById(R.id.guidephn);
        gpassword=findViewById(R.id.guidepassword);
        glanguage=findViewById(R.id.Language_spoken);
        gplaces=findViewById(R.id.places_known);
        gpayscale=findViewById(R.id.payscale);
        gexperience=findViewById(R.id.experience);
        createaccount=findViewById(R.id.register);
        loadingbar=new ProgressDialog(this);
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingbar.setTitle("Creating Account");
                loadingbar.setMessage("Please Wait while we are preparing for you");
                loadingbar.show();
                getdatafromuser();
            }
        });



    }

    private void getdatafromuser() {
         name=gname.getText().toString();
         city=gcity.getText().toString();
         phone=gphone.getText().toString();
         password=gpassword.getText().toString();
         language=glanguage.getText().toString();
         places=gplaces.getText().toString();
         payscale=gpayscale.getText().toString();
        experience=gexperience.getText().toString();

       if(name.isEmpty()){
           loadingbar.dismiss();
           Toast.makeText(this, "Enter Your name", Toast.LENGTH_SHORT).show();
       }else if (phone.isEmpty()||phone.length() < 10){
           loadingbar.dismiss();
           Toast.makeText(this, "Enter a valid mobile no.", Toast.LENGTH_SHORT).show();
       }else if(password.isEmpty()){
           loadingbar.dismiss();

           Toast.makeText(this, "Enter Your Password", Toast.LENGTH_SHORT).show();
       }else if(language.isEmpty()){
           loadingbar.dismiss();
           Toast.makeText(this, "Enter Your language", Toast.LENGTH_SHORT).show();
       }else if(city.isEmpty()){
           loadingbar.dismiss();
           Toast.makeText(this, "Enter Your city", Toast.LENGTH_SHORT).show();
       }else if(places.isEmpty()){
           loadingbar.dismiss();
           Toast.makeText(this, "Enter your Places of visit", Toast.LENGTH_SHORT).show();
       }else if(payscale.isEmpty()){
           loadingbar.dismiss();
           Toast.makeText(this, "Enter your payscale", Toast.LENGTH_SHORT).show();
       }else if(experience.isEmpty()){
           loadingbar.dismiss();
           Toast.makeText(this, "Enter Your Experience", Toast.LENGTH_SHORT).show();
       }else {
           loadingbar.show();
           Createaccount(phone,password);
       }
    }

    private void Createaccount(final String phone, final String password) {
        final DatabaseReference guideref= FirebaseDatabase.getInstance().getReference().child("ApprovedGuides");
        guideref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child(phone).exists()){
                    HashMap<String,Object> guidemap=
                            new HashMap<>();
                    guidemap.put("Agname",name);
                    guidemap.put("Agphone",phone);
                    guidemap.put("Agpassword",password);
                    guidemap.put("Aglanguage",language);
                    guidemap.put("Agcity",city);
                    guidemap.put("Agplace",places);
                    guidemap.put("Apayscale",payscale);
                    guidemap.put("Agexperience",experience);

                     guideref.child(phone).updateChildren(guidemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(GuideRegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                               Intent intent=new Intent(GuideRegisterActivity.this,GuideLogin.class);
                               startActivity(intent);
                               loadingbar.dismiss();

                           }
                         }
                     });
                }else {
                    loadingbar.dismiss();
                    Toast.makeText(GuideRegisterActivity.this, "This phone already have an Account", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(GuideRegisterActivity.this,GuideLogin.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GuideRegisterActivity.this, "Error in Creating Account Please try after Sometime", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
