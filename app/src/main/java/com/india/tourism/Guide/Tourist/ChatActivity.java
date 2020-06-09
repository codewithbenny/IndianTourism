package com.india.tourism.Guide.Tourist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.tourism.MessageAdapter;
import com.india.tourism.Model.Message;
import com.india.tourism.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private EditText chatedit;
    Button send;
    List<Message> messageList;
    MessageAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatedit = findViewById(R.id.edittext_chatbox);
        send = findViewById(R.id.button_chatbox_send);
        recyclerView=findViewById(R.id.reyclerview_message_list);


        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(layoutManager);
        String userid2=getIntent().getStringExtra("userid");
        String ek=getIntent().getStringExtra("guide");
        String userid = getIntent().getStringExtra("userid");
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("myinfo",0);
        SharedPreferences preferences1=getApplicationContext().getSharedPreferences("myfile",0);


        String iam=preferences1.getString("guidephn",null);
        final String iam1=preferences.getString("userphone",null);
        if(ek=="guide"&&iam==null&&userid==null){
           iam=iam1;
           userid=userid2;
        }


        final String finalIam = iam;
        final String finalUserid = userid;
        final String finalUserid1 = userid;
        send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String message = chatedit.getText().toString();
                    if (message.equals("")) {
                        Toast.makeText(ChatActivity.this, " Plaese Enter Messge", Toast.LENGTH_SHORT).show();
                    } else {

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                .child("ApprovedGuides").child(finalUserid1).child("messages");
                        HashMap<String, Object> chatmap = new HashMap<>();
                        chatmap.put("message", message);
                        reference.push().setValue(chatmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ChatActivity.this, "Mesage sent Successfully", Toast.LENGTH_SHORT).show();
                           chatedit.setText("");
                            }
                        });
                    }
                }
            });
        }


        }


