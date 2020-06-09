package com.india.tourism.Guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.tourism.R;

import io.paperdb.Paper;

public class GuideLogin extends AppCompatActivity {
    private TextView phoneno, ppassword, noaccount;
    private Button login;
    private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_login);
        phoneno = findViewById(R.id.input_phn1);
        ppassword = findViewById(R.id.input_password1);
        noaccount = findViewById(R.id.createaac1);
        login = findViewById(R.id.login_btn1);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/Quicksand-Bold.otf");
        login.setTypeface(typeface);
        loadingbar=new ProgressDialog(this);
        noaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GuideLogin.this,GuideRegisterActivity.class);
                startActivity(intent);
            }
        });
        loadingbar.setTitle("Logging in");
        loadingbar.setMessage("Please Wait While we are Preparing for you");
        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingbar.show();
                loginguide();
            }
        });
    }

    private void loginguide() {
        final String phn=phoneno.getText().toString();
        final String pass=ppassword.getText().toString();
        if(phn.isEmpty()&&pass.isEmpty()){
            Toast.makeText(this, "Please provide Full info", Toast.LENGTH_SHORT).show();
        }else{
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("ApprovedGuides");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(phn).exists()){
                        if(dataSnapshot.child(phn).child("Agpassword").exists()){
                            String Password=dataSnapshot.child(phn).child("Agpassword").getValue().toString();
                               if(pass.equals(Password)){
                                   loadingbar.dismiss();

                                   SharedPreferences preferences=getApplicationContext().getSharedPreferences("myfile",0);
                                   SharedPreferences.Editor editor=preferences.edit();
                                   editor.putString("guidephn",phn);
                                   editor.putString("guidepassword",pass);
                                   editor.commit();
                                Intent intent=new Intent(GuideLogin.this,GuideSettingsActivity.class);
                                startActivity(intent);
                            }else{
                                   loadingbar.dismiss();
                                Toast.makeText(GuideLogin.this, "Password is Incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(GuideLogin.this, "No Account Create one", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            Intent intent=new Intent(GuideLogin.this,GuideRegisterActivity.class);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
}
