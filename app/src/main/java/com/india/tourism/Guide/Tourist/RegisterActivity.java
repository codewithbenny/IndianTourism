package com.india.tourism.Guide.Tourist;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.tourism.R;

import java.util.HashMap;

import javax.security.auth.callback.Callback;

public class RegisterActivity extends AppCompatActivity {
private EditText rephone,rePassword,rename,rlanguage;
private Button createaccount;
private Callback mCallbacks;
private FirebaseAuth auth;
private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rename=findViewById(R.id.input_name_register);
        rePassword=findViewById(R.id.input_password_register);
        rephone=findViewById(R.id.input_phn_register);
        createaccount=findViewById(R.id.CreateAccountbtn);
        rlanguage=findViewById(R.id.input_language_register);
        loadingbar=new ProgressDialog(this);
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Verifyuser();
            }
        });

    }
    public void Verifyuser(){
        String Name=rename.getText().toString();
        String Password=rePassword.getText().toString();
        String Phone=rephone.getText().toString();
        String Language=rlanguage.getText().toString();
        if(Name.isEmpty()){
            Toast.makeText(this, "Please Enter Your FullName ", Toast.LENGTH_SHORT).show();
        }else if(Password.isEmpty()){
            Toast.makeText(this, "Please set a Password", Toast.LENGTH_SHORT).show();
        }else if(Phone.isEmpty()) {
            Toast.makeText(this, "Please set a Phone", Toast.LENGTH_SHORT).show();
        }else if(Language.isEmpty()) {
            Toast.makeText(this, "Please set Your language", Toast.LENGTH_SHORT).show();
        }else {
            loadingbar.setTitle("Creating Account");
            loadingbar.setMessage("Please Wait while we are checking the Credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            Valiadatephoneno(Name,Phone,Password,Language);
        }
    }

    private void Valiadatephoneno(final String name, final String phone, final String password,final String language) {
        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String,Object> UserdataMap=new HashMap<>();
                    UserdataMap.put("phone",phone);
                    UserdataMap.put("password",password);
                    UserdataMap.put("name",name);
                    UserdataMap.put("language",language);

                    Rootref.child("Users").child(phone).updateChildren(UserdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Successfully Account Created", Toast.LENGTH_SHORT)
                                        .show();
                                loadingbar.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, LogintouristActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegisterActivity.this, "Network Error Please Try Again", Toast.LENGTH_SHORT)
                                        .show();
                                loadingbar.dismiss();
                            }
                        }
                    });

                }else{
                    Toast.makeText(RegisterActivity.this, "This"+phone+"already have an Account", Toast.LENGTH_SHORT)
                            .show();
                    loadingbar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please Try again with another phone Number", Toast.LENGTH_SHORT)
                            .show();
                    Intent intent=new Intent(RegisterActivity.this,IamActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RegisterActivity.this, " Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
