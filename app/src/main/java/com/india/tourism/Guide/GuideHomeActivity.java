package com.india.tourism.Guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.tourism.Guide.Tourist.ChatActivity;
import com.india.tourism.GuidechatActivity;
import com.india.tourism.Model.Requests;
import com.india.tourism.R;
import com.india.tourism.ViewHolder.RequestViewHolder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import io.paperdb.Paper;

import static com.india.tourism.Guide.Tourist.NotificationChannel.CHANNEL_1_ID;

public class GuideHomeActivity extends AppCompatActivity {
private Toolbar toolbar;
private RecyclerView requestlist;
private String phonekey,date,phn;
    NotificationManagerCompat notificationManager;
private DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_home);
        toolbar=findViewById(R.id.toolbar1);
        requestlist=findViewById(R.id.guiderecycler);
        setSupportActionBar(toolbar);
        requestlist.setHasFixedSize(true);
        requestlist.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("myfile",0);

        Paper.init(this);
          phonekey=preferences.getString("guidephn",null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.guide_menu, menu);
        return true;
            }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.Settings){
            Intent intent=new Intent(GuideHomeActivity.this,GuideSettingsActivity.class);
            startActivity(intent);
        }else if(id==R.id.Logout){
            SharedPreferences preferences=getApplicationContext().getSharedPreferences("myfile",0);

            SharedPreferences.Editor editor=preferences.edit();
            editor.clear();
            editor.commit();
            finish();
            Intent intent=new Intent(GuideHomeActivity.this,GuideLogin.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("ApprovedGuides")
                .child(phonekey).child("Request");
        FirebaseRecyclerOptions<Requests> options=
                new FirebaseRecyclerOptions.Builder<Requests>().setQuery(reference,Requests.class).build();
        FirebaseRecyclerAdapter<Requests, RequestViewHolder> adapter=new FirebaseRecyclerAdapter<Requests, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RequestViewHolder requestViewHolder, int i, @NonNull final Requests places) {

              requestViewHolder.Rname.setText("Name:"+places.getRname());
                requestViewHolder.Rphn.setText("Phone:"+places.getRphn());
                requestViewHolder.Rdate.setText("DOV:"+places.getRdate());
                requestViewHolder.Rvisitorlen.setText("Visitors:"+places.getRvisitlen());
                Picasso.get().load(places.getRimage()).into(requestViewHolder.Rimageprofile);

                phn=places.getRphn();
                date=places.getRdate();

                 requestViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                   CharSequence options[]=new CharSequence[]{
                           "Confirm",
                           "Cancel",
                           "Remove",
                           "Completed"
                   };
                         AlertDialog.Builder builder=new AlertDialog.Builder(GuideHomeActivity.this);
                         builder.setTitle("Select Action");
                         builder.setItems(options, new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 ref=FirebaseDatabase.getInstance().getReference().child("ApprovedGuides").child(phonekey)
                                         .child("Request").child(places.getRdate());

                                 if(i==0){
                                     ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                             HashMap<String,Object> map=new HashMap<>();
                                             map.put("state",null);
                                             ref.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     if(task.isSuccessful()){

                                                         final DatabaseReference reference121=FirebaseDatabase.getInstance().getReference()
                                                                 .child("Users").child(phn).child("Your Guides");
                                                         reference121.addListenerForSingleValueEvent(new ValueEventListener() {
                                                             @Override
                                                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                 if(dataSnapshot.exists()){
                                                                     HashMap<String,Object> touriststate=new HashMap<>();
                                                                     touriststate.put("state","Confirm");
                                                                     reference121.updateChildren(touriststate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                         @Override
                                                                         public void onComplete(@NonNull Task<Void> task) {
                                                                             if(task.isSuccessful()){
                                                                                 Toast.makeText(GuideHomeActivity.this, "Tourist state confirmed", Toast.LENGTH_SHORT).show();
                                                                             }
                                                                         }
                                                                     });
                                                                 }
                                                             }

                                                             @Override
                                                             public void onCancelled(@NonNull DatabaseError databaseError) {

                                                             }
                                                         });


                                                         Intent intent = new Intent(Intent.ACTION_SEND);
                                                         intent.setData(Uri.parse("smsto:"+places.getRphn())); // This ensures only SMS apps respond
                                                         intent.putExtra("sms_body", "Hello your Guide is Confirmed");
                                                         if (intent.resolveActivity(getPackageManager()) != null) {
                                                             startActivity(intent);

                                                         Toast.makeText(GuideHomeActivity.this, "Status is Confirmed", Toast.LENGTH_SHORT).show();
                                                    }
                                                 }
                                             };

                                         });
                                         }

                                         @Override
                                         public void onCancelled(@NonNull DatabaseError databaseError) {

                                         }
                                     });

                                 }else if(i==1){
                                     final DatabaseReference cancel=FirebaseDatabase.getInstance().getReference()
                                             .child("ApprovedGuides").child(phonekey).child("Request").child(places.getRdate());
                                     cancel.addListenerForSingleValueEvent(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                             HashMap<String,Object>cancelmap=new HashMap<>();
                                             cancelmap.put("state","cancel");
                                             cancel.updateChildren(cancelmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     if(task.isSuccessful()){
                                                         Toast.makeText(GuideHomeActivity.this, "Tourist Request Cancelled", Toast.LENGTH_SHORT).show();
                                                     }
                                                 }
                                             });
                                         }

                                         @Override
                                         public void onCancelled(@NonNull DatabaseError databaseError) {

                                         }
                                     });

                                 }
                                 else if(i==2) {
                                     ref=FirebaseDatabase.getInstance().getReference().child("ApprovedGuides").child(phonekey).child("Request").child(places.getRdate());

                                     ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {
                                             if(task.isSuccessful()){
                                                 Intent intent = new Intent(Intent.ACTION_SEND);
                                                 intent.setData(Uri.parse("smsto:"+places.getRphn())); // This ensures only SMS apps respond
                                                 intent.putExtra("sms_body", "Hello Dear Sorry to say we are unable to process your request Please try again" +
                                                         "team Tourism");
                                                 if (intent.resolveActivity(getPackageManager()) != null) {
                                                     startActivity(intent);
                                                     Toast.makeText(GuideHomeActivity.this, "Node"+places.getRphn()+"is removed", Toast.LENGTH_SHORT).show();
                                                 }
                                             }
                                         }
                                     });





                            //         Toast.makeText(GuideHomeActivity.this, "Remove", Toast.LENGTH_SHORT).show();
                                 }
                                 else if(i==2) {
                                     ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                         @Override
                                         public void onComplete(@NonNull Task<Void> task) {
                                             if(task.isSuccessful()){
                                                 Intent intent = new Intent(Intent.ACTION_SEND);
                                                 intent.setData(Uri.parse("smsto:"+places.getRphn())); // This ensures only SMS apps respond
                                                 intent.putExtra("sms_body", "Hello Dear Please Rate us " +
                                                         "team Tourism");
                                                 if (intent.resolveActivity(getPackageManager()) != null) {
                                                     startActivity(intent);
                                                     Toast.makeText(GuideHomeActivity.this, "Completed Successfully"+places.getRphn()+"is removed", Toast.LENGTH_SHORT).show();
                                                 }
                                             }
                                         }
                                     });

                                 }

                             }
                         });
                         builder.show();
                     }
                 });

                requestViewHolder.message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setData(Uri.parse("smsto:"+phonekey)); // This ensures only SMS apps respond
                        intent.putExtra("sms_body", "Hello your Guide is Confirmed");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });

            }

            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout,parent,false);
                RequestViewHolder productViewHolder=new RequestViewHolder(view);
                return productViewHolder;
            }
        };
        requestlist.setAdapter(adapter);
        adapter.startListening();
    }

    private void sendNotification() {
        Notification notification=new  NotificationCompat.Builder(this,CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Guide Approved").setContentText("You Successfuly Booked Your Guide")
                .setPriority(NotificationCompat.PRIORITY_LOW).setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1,notification);

    }
}
