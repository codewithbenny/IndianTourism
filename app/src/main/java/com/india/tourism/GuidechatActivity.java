package com.india.tourism;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.india.tourism.Guide.Tourist.ChatActivity;
import com.india.tourism.Model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuidechatActivity extends AppCompatActivity {
private EditText guidemsg;
private Button sendguidemsg;
List<Message> messageList;
MessageAdapter adapter;
private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidechat);
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("myfile",0);
         final String myid=preferences.getString("guidephn",null);
         final String userid=getIntent().getStringExtra("userphone");
        Toast.makeText(this, "userid", Toast.LENGTH_SHORT).show();
         guidemsg=findViewById(R.id.edittext_chatbox_guide);
         sendguidemsg=findViewById(R.id.button_chatbox_send_guide);
         recyclerView=findViewById(R.id.reyclerview_message_listguide);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
         layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        readmessage(myid,userid);
         sendguidemsg.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
               final String msg=guidemsg.getText().toString();
                 if(TextUtils.isEmpty(msg)){
                     Toast.makeText(GuidechatActivity.this, "Please enter your message", Toast.LENGTH_SHORT).show();
                 }else {
                     sendmessage(msg,myid,userid);
                 }
             }
         });


    }

    private void sendmessage(final String msg, final String myid, final String userid) {
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("chats");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,Object> guidechat=new HashMap<>();
                guidechat.put("Reciever",userid);
                guidechat.put("sender",myid);
                guidechat.put("message",msg);
                reference.push().setValue(guidechat).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                     if(task.isSuccessful()){
                         guidemsg.setText("");
                         Toast.makeText(GuidechatActivity.this, "Message sent Successfully", Toast.LENGTH_SHORT).show();
                     }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void readmessage(final String myid,final String userid){
        messageList=new ArrayList<>();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("chats");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Message message=snapshot.getValue(Message.class);
                    if(message.getReciever().equals(myid) && message.getSender().equals(userid)
                            ||message.getReciever().equals(userid)&& message.getSender().equals(myid)){
                        messageList.add(message);
                    }
                    adapter=new MessageAdapter(GuidechatActivity.this,messageList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
