package com.india.tourism;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.india.tourism.Guide.Tourist.ChatActivity;
import com.india.tourism.Model.Message;

import java.net.PortUnreachableException;
import java.util.List;
import java.util.zip.Inflater;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Holder> {
    public static int LEFT=0;
    public static int RIGHT=1;
    private Context context;
    private List<Message> messageList;

    public MessageAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chatright, parent, false);
            return new MessageAdapter.Holder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.chatleft, parent, false);
            return new MessageAdapter.Holder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Message message=messageList.get(position);
        holder.msg.setText(message.getMessage());
//        holder.msg1.setText(message.getMessage());
    }



    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView msg,msg1;
        public Holder(@NonNull View itemView) {
            super(itemView);
            msg=itemView.findViewById(R.id.textright);

        }
    }

    @Override
    public int getItemViewType(int position) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        SharedPreferences preferences=context.getSharedPreferences("myinfo",0);
        SharedPreferences preferences1=context.getSharedPreferences("myfile",0);
        String iam=preferences.getString("userphone",null);
        if(iam==null){
            iam=preferences1.getString("guide",null);
        }
        if(messageList.get(position).getSender().equals(reference.child("Users").child(iam))) {
            return RIGHT;
        } else{
                return LEFT;
            }
        }
    }

