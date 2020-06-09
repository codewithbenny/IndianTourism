package com.india.tourism.Guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class AdminApproveguideActivity extends AppCompatActivity {
private CircleImageView profileguide;
private TextView name,phone,place,city,language,pay,experience,password;
private Button approve;
String naam,bhasha,sal,pla,sahar,ph,exp,pass;
String phonekey;
private DatabaseReference reference;
private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_approveguide);
        name=findViewById(R.id.name);
        city=findViewById(R.id.city);
        place=findViewById(R.id.places);
        phone=findViewById(R.id.phn);
        language=findViewById(R.id.language);
        password=findViewById(R.id.pass);
        pay=findViewById(R.id.payscale);
        experience=findViewById(R.id.experience);
        profileguide=findViewById(R.id.guideprofile);
        approve=findViewById(R.id.approve);
        phonekey=getIntent().getStringExtra("phone");
        getguidedata(phonekey);
        loadingbar=new ProgressDialog(this);
        loadingbar.setTitle("Approving Guide");
        loadingbar.setMessage("Please wait while we are Approving guide");
        Paper.init(this);
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingbar.show();
               final DatabaseReference reference1=FirebaseDatabase.getInstance().getReference();
                reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HashMap<String,Object> approvemap=new HashMap<>();
                            approvemap.put("Agname",naam);
                            approvemap.put("Agcity",sahar);
                            approvemap.put("Agplace",pla);
                            approvemap.put("Aglanguage",bhasha);
                            approvemap.put("Apayscale",sal);
                            approvemap.put("Agphone",ph);
                            approvemap.put("Agexperience",exp);
                            approvemap.put("Agpassword",pass);


                            reference1.child("ApprovedGuides").child(phonekey).updateChildren(approvemap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(AdminApproveguideActivity.this, "Guide approved Succesfully", Toast.LENGTH_SHORT).show();
                                    loadingbar.dismiss();
                                    FirebaseDatabase.getInstance().getReference().child("Guides").child(phonekey).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(AdminApproveguideActivity.this, "Finally task Successfull", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    }
                                }
                            });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    loadingbar.dismiss();
                    }
                });
            }
        });

    }

    private void getguidedata(final String phonekey) {
        reference= FirebaseDatabase.getInstance().getReference().child("Guides").child(phonekey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                     naam=dataSnapshot.child("gname").getValue().toString();
                     sahar=dataSnapshot.child("gcity").getValue().toString();
                     bhasha=dataSnapshot.child("glanguage").getValue().toString();
                     sal=dataSnapshot.child("gpayscale").getValue().toString();
                     ph=dataSnapshot.child("gphone").getValue().toString();
                     exp=dataSnapshot.child("gexperience").getValue().toString();
                     pla=dataSnapshot.child("gplace").getValue().toString();
                     pass=dataSnapshot.child("gpassword").getValue().toString();
                 //   String img=dataSnapshot.child(phonekey).getValue().toString();
                    name.setText("Name:  "+naam);
                    city.setText("City:  "+sahar);
                    language.setText("Language:  "+bhasha);
                    pay.setText("PayScale:  "+sal);
                    phone.setText("Phone:  "+ph);
                    experience.setText("Experience:  "+exp);
                    place.setText("Places About i knows:  "+pla);
                    password.setText(pass);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
