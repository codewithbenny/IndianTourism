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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.tourism.Guide.AdminAddnewplaceActivity;
import com.india.tourism.Model.Users;
import com.india.tourism.R;

import io.paperdb.Paper;

public class LogintouristActivity extends AppCompatActivity {
    private TextView phoneno, ppassword, noaccount, admin, notadmin;
    private Button login;
    private CheckBox rememberme;
    private ProgressDialog loadingbar;
    private String dbname = "Users";
    String Phone,Password;
    TextView forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logintourist);
        phoneno = findViewById(R.id.input_phn);
        ppassword = findViewById(R.id.input_password);
        noaccount = findViewById(R.id.createaac);
        login = findViewById(R.id.login_btn);
        forget=findViewById(R.id.forgetpassword);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/Quicksand-Bold.otf");
        login.setTypeface(typeface);
        loadingbar = new ProgressDialog(this);
forget.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(LogintouristActivity.this,ResetPasswordActivity.class);
        intent.putExtra("check","login");
        startActivity(intent);
    }
});
        noaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LogintouristActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getdata();
            }
        });
    }

    private void getdata() {
        String Password = ppassword.getText().toString();
        String Phone = phoneno.getText().toString();

        if (Password.isEmpty()) {
            Toast.makeText(this, "Please set a Password", Toast.LENGTH_SHORT).show();
        } else if (Phone.isEmpty()) {
            Toast.makeText(this, "Please set a Phone", Toast.LENGTH_SHORT).show();
        } else {
            loadingbar.setTitle("Logging in");
            loadingbar.setMessage("Please Wait while we are checking the Credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();


            logintourist(Phone, Password);

        }
    }

    private void logintourist(final String phone, final String password) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(dbname).child(phone).exists()) {
                    Users usersdata = dataSnapshot.child("Users").child(phone)
                            .getValue(Users.class);
                    if (usersdata.getPhone().equals(phone)) {
                        Paper.book().write("savedid",phone);

                        if (usersdata.getPassword().equals(password)) {
                            SharedPreferences preferences=getApplicationContext().getSharedPreferences("myinfo",0);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("userphone",phone);
                            editor.putString("userpassword",password);
                            editor.commit();
                            if (dbname.equals("Users")) {
                                if(!dataSnapshot.child(dbname).child(phone).child("image").exists()) {
                                    Intent intent = new Intent(LogintouristActivity.this, SettingsActivity.class);
                                    intent.putExtra("setting", "setting");
                                    // user ki id
                                    loadingbar.dismiss();
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(LogintouristActivity.this,HomeActivity.class);
                                    loadingbar.dismiss();
                                    startActivity(intent);
                                }
                            }

                        } else {
                            Toast.makeText(LogintouristActivity.this, "Password is Incorrect", Toast.LENGTH_SHORT)
                                    .show();
                            loadingbar.dismiss();
                        }

                    } else {
                        Toast.makeText(LogintouristActivity.this, "You don't have an account", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogintouristActivity.this, RegisterActivity.class);
                        startActivity(intent);
                    }
                }
//            }else if(Phone=="9759142453"&&Password=="Ravi"){
//                    Intent intent=new Intent(LogintouristActivity.this,AdminAddnewplaceActivity.class);
//                    Toast.makeText(LogintouristActivity.this, "Welcome Admin You are Welcomed", Toast.LENGTH_SHORT).show();
//                    startActivity(intent);
//                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        }

    }

