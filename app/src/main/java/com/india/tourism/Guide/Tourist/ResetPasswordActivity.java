package com.india.tourism.Guide.Tourist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import io.paperdb.Paper;

public class ResetPasswordActivity extends AppCompatActivity {
    private String check="";
    private TextView pagetitle,titleq;
    private EditText phn,q1,q2;
    private Button verifybtn;
    String phonekey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        check=getIntent().getStringExtra("check");
        pagetitle=findViewById(R.id.pagetitle);
        titleq=findViewById(R.id.title_questions);
        phn=findViewById(R.id.find_phone_number);
        q1=findViewById(R.id.question_1);
        q2=findViewById(R.id.question_2);
        verifybtn=findViewById(R.id.verify_btn);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myinfo", 0);
//        name = preferences.getString("Name", null);
        phonekey = preferences.getString("userphone", null);
//        userprofile = preferences.getString("img", null);
        phn.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(check.equals("Settings")){
            displaypreviousanswers();
            pagetitle.setText("Set Questions");
            titleq.setText("Please Answer the Security Questions");
            verifybtn.setText("Set");
            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setanswers();

                }
            });


        }else if (check.equals("login")){
            phn.setVisibility(View.VISIBLE);
            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyuser();
                }
            });


        }
    }

    private void verifyuser() {
        final String phnn = phn.getText().toString();
        final String ans1 = q1.getText().toString().toLowerCase();
        final String ans2 = q2.getText().toString().toLowerCase();
        if (!phnn.equals("")) {
            final DatabaseReference reference = FirebaseDatabase.getInstance()
                    .getReference().child("Users").child(phnn);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild("Security Questions")){
                        String Ans1=dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                        String Ans2=dataSnapshot.child("Security Questions").child("answer2").getValue().toString();
                        if(!ans1.equals(dataSnapshot.child("Security Questions").child("answer1").getValue().toString())){
                            Toast.makeText(ResetPasswordActivity.this, "Your First answer is wrong", Toast.LENGTH_SHORT).show();
                        }else if(!ans2.equals(Ans2)){
                            Toast.makeText(ResetPasswordActivity.this, "Your Second answer is wrong", Toast.LENGTH_SHORT).show();
                        }else{
                            AlertDialog.Builder builder=new AlertDialog.Builder(ResetPasswordActivity.this);
                            builder.setTitle("New Password");
                            final EditText newpassword=new EditText(ResetPasswordActivity.this);
                            newpassword.setHint("Enter your New Password");
                            builder.setView(newpassword);
                            builder.setPositiveButton("change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(!newpassword.getText().toString().equals(""));
                                    reference.child("password").setValue(newpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(ResetPasswordActivity.this, "Password Changed Succesfully", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(ResetPasswordActivity.this, LogintouristActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            builder.show();

                        }

                    }

                    else{
                        Toast.makeText(ResetPasswordActivity.this, "This Phone number doesnot exist", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            Toast.makeText(this, "Please Provide full info", Toast.LENGTH_SHORT).show();
        }

    }

    private void setanswers(){
        String ans1=q1.getText().toString().toLowerCase();
        String ans2=q2.getText().toString().toLowerCase();
        if(ans1.isEmpty()){
            Toast.makeText(ResetPasswordActivity.this, "Please Answer the both questions", Toast.LENGTH_SHORT).show();
        } else if(ans2.isEmpty()){
            Toast.makeText(ResetPasswordActivity.this, "Please Answer the both questions", Toast.LENGTH_SHORT).show();

        }else{
            DatabaseReference reference= FirebaseDatabase.getInstance()
                    .getReference().child("Users").child(phonekey);
            reference.child("Security Questions");
            HashMap<String,Object> securitymap=
                    new HashMap<>();
            securitymap.put("answer1",ans1);
            securitymap.put("answer2",ans2);
            reference.child("Security Questions")
                    .updateChildren(securitymap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ResetPasswordActivity.this, "Answers Store Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(ResetPasswordActivity.this, SettingsActivity.class);
                        Toast.makeText(ResetPasswordActivity.this, "Please click on Update button to update Settings", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
            });
        }
    }
    private void displaypreviousanswers(){
        Paper.init(this);

        DatabaseReference reference= FirebaseDatabase.getInstance()
                .getReference().child("Users").child(phonekey);
        reference.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String ans1=dataSnapshot.child("answer1").getValue().toString();
                    String ans2=dataSnapshot.child("answer2").getValue().toString();
                    q1.setText(ans1);
                    q2.setText(ans2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    }

