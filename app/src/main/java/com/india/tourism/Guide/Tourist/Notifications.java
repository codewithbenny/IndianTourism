package com.india.tourism.Guide.Tourist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.tourism.Guide.GuideHomeActivity;
import com.india.tourism.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Notifications extends AppCompatActivity {
String Saveduserid,guidename,guideprofile,guidephn,Guideid,state;
private DatabaseReference referencenotifi;
private TextView name,phn,state1;
private Toolbar toolbar;
private ImageView snd;
private TextView nonotification;
private LinearLayout mlayout;
private CircleImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Paper.init(this);
        Saveduserid=Paper.book().read("savedid");
        name=findViewById(R.id.notificationusername);
        toolbar=findViewById(R.id.Notificationtoolbar);
        phn=findViewById(R.id.notificatiophn);
        snd=findViewById(R.id.messsend1);
        nonotification=findViewById(R.id.nonotifi);
        mlayout=findViewById(R.id.i_am_Linear);
        state1=findViewById(R.id.notificationstate);
        profile=findViewById(R.id.notificationprofile);

        toolbar.setTitle("Notifications");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Notifications.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        snd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("smsto:"+guidephn)); // This ensures only SMS apps respond
                intent.putExtra("sms_body", "Hii");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        referencenotifi= FirebaseDatabase.getInstance().getReference().child("Users").child(Saveduserid)
                .child("Your Guides");
        referencenotifi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mlayout.setVisibility(View.VISIBLE);
                    nonotification.setVisibility(View.INVISIBLE);
                     state= dataSnapshot.child("state").getValue().toString();
                     Guideid=dataSnapshot.child("guideid").getValue().toString();
                    if(state!=null) {
                        final DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                                .child("ApprovedGuides")
                                .child(Guideid);
                        database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists()) {

                                    guidename = dataSnapshot.child("Agname").getValue().toString();
                                    guidephn = dataSnapshot.child("Agphone").getValue().toString();
                                    guideprofile = dataSnapshot.child("Agimage").getValue().toString();
                                    Toast.makeText(Notifications.this, guidename, Toast.LENGTH_SHORT).show();
                                    name.setText(guidename);
                                    phn.setText(guidephn);
                                    state1.setText("Your Reguest is Confirmed! Contact me");
                                    if (guideprofile != null) {
                                        Picasso.get().load(guideprofile).into(profile);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    else if(state=="cancel") {
                        name.setText(guidename);
                        phn.setText(guidephn);
                        state1.setText("Your Reguest is Cancel! Sorry for inconvinient");
                        if (guideprofile != null) {
                            Picasso.get().load(guideprofile).into(profile);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
