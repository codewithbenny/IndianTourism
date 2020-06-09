package com.india.tourism.Guide.Tourist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.tourism.Guide.GuideHomeActivity;
import com.india.tourism.Guide.GuideLogin;
import com.india.tourism.Guide.GuideRegisterActivity;
import com.india.tourism.R;

import io.paperdb.Paper;

public class IamActivity extends AppCompatActivity {
    private Button touristbtn, guidebtn;
    private DatabaseReference reference;
    private ProgressDialog loadingbar;
    private String phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iam);
        touristbtn = findViewById(R.id.tourist);
        guidebtn = findViewById(R.id.guide);
        Paper.init(this);
        phone = Paper.book().read("phonekey");
        password = Paper.book().read("passwordkey");
        loadingbar = new ProgressDialog(this);
        loadingbar.setTitle("Already logged in");
        loadingbar.setMessage("Please wait.........");
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/Quicksand-Bold.otf");
        touristbtn.setTypeface(typeface);
        guidebtn.setTypeface(typeface);
        touristbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checktouriststatus();


            }
        });
        guidebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(IamActivity.this,GuideHomeActivity.class);
//                startActivity(intent);
                checkguidestatus();

            }
        });

    }

    private void checktouriststatus() {
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("myinfo",0);
        final String touristphn =preferences.getString("userphone",null);
        final String touristpass = preferences.getString("userpassword",null);
        if (touristphn !=null && touristpass!=null) {
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Users");
            reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(touristphn).exists()) {

                        String password = dataSnapshot.child(touristphn).child("password").getValue().toString();
                        if (touristpass.equals(password)) {
                            loadingbar.show();
                            if (!dataSnapshot.child(touristphn).child("image").exists()) {
                                Intent intent = new Intent(IamActivity.this, SettingsActivity.class);
                                Toast.makeText(IamActivity.this, "Please Update Your Profile", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            }else {
                                loadingbar.show();
                                Intent intent=new Intent(IamActivity.this,HomeActivity.class);
                                startActivity(intent);
                              //  loadingbar.dismiss();
                                Toast.makeText(IamActivity.this, "Welcome Tourist You Logged in Successfully", Toast.LENGTH_SHORT).show();

                            }
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else{

            Intent intent = new Intent(IamActivity.this,LogintouristActivity.class);
            startActivity(intent);
        }
    }
    public void checkguidestatus() {
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("myfile",0);


        final String Guidephn =preferences.getString("guidephn",null);;
        final String Guidepass = preferences.getString("guidepassword",null);

        if (Guidephn != null && Guidepass!=null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ApprovedGuides");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(Guidephn).exists()) {
                        Toast.makeText(IamActivity.this, "hloo", Toast.LENGTH_SHORT).show();
                        String password = dataSnapshot.child(Guidephn).child("Agpassword").getValue().toString();
                        Toast.makeText(IamActivity.this, Guidepass+"and"+password, Toast.LENGTH_SHORT).show();
                        if (Guidepass.equals(password)) {
                            loadingbar.show();
                            Intent intent = new Intent(IamActivity.this, GuideHomeActivity.class);
                           // loadingbar.dismiss();
                            Toast.makeText(IamActivity.this, "Welcome Guide You Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Intent intent=new Intent(IamActivity.this,GuideLogin.class);
                            startActivity(intent);
//                            keText(IamActivity.this, "Please enter your Correct Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                       // loadingbar.dismiss();
                        Toast.makeText(IamActivity.this, "No Account with this Number", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(IamActivity.this, GuideRegisterActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }else {
            Intent intent=new Intent(this,GuideLogin.class);
            startActivity(intent);
        }
    }
}