package com.india.tourism.Guide.Tourist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.india.tourism.TestActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import io.paperdb.Paper;

public class ConfirmGuide extends AppCompatActivity {
    private EditText datepick, visitorlen;
    private TextView message;
    private Button Confirm;
    private Toolbar toolbar;
    private String phonekey, name, userprofile, touristphn, name1;
    private String usersavedid;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_guide);
        datepick = findViewById(R.id.dateofvisit);
        visitorlen = findViewById(R.id.goingtovisit);
        Confirm = findViewById(R.id.Confirmguide);
        message = findViewById(R.id.message1);
        toolbar=findViewById(R.id.toolbar12123);
        Paper.init(this);
        usersavedid=Paper.book().read("savedid");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ConfirmGuide.this,GuideDetailsforUserActivity.class);
                startActivity(intent);
            }
        });

           SharedPreferences preferences = getApplicationContext().getSharedPreferences("myinfo", 0);
        name1 = preferences.getString("Name", null);
        phonekey = preferences.getString("userphone", null);
//        userprofile = preferences.getString("img", null);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(phonekey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 name = dataSnapshot.child("name").getValue().toString();
                if(dataSnapshot.child("image").exists()){
                    userprofile=dataSnapshot.child("image").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //phonekey = getIntent().getStringExtra("phonekey");
        if (userprofile == null && name == null) {
            userprofile = getIntent().getStringExtra("image");
            name = getIntent().getStringExtra("name");
            phonekey = getIntent().getStringExtra("phonekey");
        }

        Paper.book().write("only4u", phonekey);
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datepick.getText().toString().isEmpty()) {
                    Toast.makeText(ConfirmGuide.this, "Please enter date of visit", Toast.LENGTH_SHORT).show();
                } else {
                    sendRequesttoguide();
                }
            }
        });
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        datepick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ConfirmGuide.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void sendRequesttoguide() {
        final DatabaseReference sendref = FirebaseDatabase.getInstance().getReference().child("ApprovedGuides").child(phonekey);

        sendref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) ;
                if (datepick.getText().toString() == null) {
                    Toast.makeText(ConfirmGuide.this, "Select Visiting date", Toast.LENGTH_SHORT).show();
                } else if (visitorlen.getText().toString() == null) {
                    Toast.makeText(ConfirmGuide.this, "Please enter No of visitors", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> sendmap = new HashMap<>();
                    sendmap.put("Rdate", datepick.getText().toString());
                    sendmap.put("Rvisitlen", visitorlen.getText().toString());
                    sendmap.put("Rimage", userprofile);
                    sendmap.put("Rphn", usersavedid);
                    sendmap.put("Rname", name);
                    sendmap.put("state", "notconfirm");

                    sendref.child("Request").child(datepick.getText().toString()).updateChildren(sendmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ConfirmGuide.this, "Your Request is send", Toast.LENGTH_SHORT).show();
                                datepick.setVisibility(View.GONE);
                                Confirm.setVisibility(View.GONE);
                                visitorlen.setVisibility(View.GONE);
                                message.setVisibility(View.VISIBLE);
                                message.setText("Your Request for guide is Successfully Send You will receive a confirmation " +
                                        "Thanks for choosing us");

                                final DatabaseReference dataref=FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(usersavedid).child("Your Guides");
                                dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        HashMap<String,Object> mybook=new HashMap<>();
                                        mybook.put("guideid",phonekey);
                                        mybook.put("state","notconfirmed");
                                        dataref.updateChildren(mybook).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(ConfirmGuide.this, "State saved", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                Paper.init(getApplicationContext());


                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void updateLabel() {
        String myFormat = "dd MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        datepick.setText(sdf.format(myCalendar.getTime()));
    }
    public void sendback(){
        if(name1==null){
            Intent intent=new Intent(ConfirmGuide.this, TestActivity.class);
            startActivity(intent);

        }else{
            Intent intent=new Intent(ConfirmGuide.this, GuideDetailsforUserActivity.class);
            startActivity(intent);
        }
    }
}
