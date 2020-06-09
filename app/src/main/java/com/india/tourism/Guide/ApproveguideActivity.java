package com.india.tourism.Guide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.india.tourism.Model.Guides;
import com.india.tourism.R;
import com.india.tourism.ViewHolder.GuideViewHolder;

public class ApproveguideActivity extends AppCompatActivity {
private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approveguide);
        recyclerView=findViewById(R.id.approvelist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }
    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Guides");
        FirebaseRecyclerOptions<Guides> options=new FirebaseRecyclerOptions.Builder<Guides>()
                .setQuery(reference,Guides.class).build();
        FirebaseRecyclerAdapter<Guides, GuideViewHolder> adapter=new FirebaseRecyclerAdapter<Guides, GuideViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GuideViewHolder guideViewHolder, int i, @NonNull final Guides guides) {
                guideViewHolder.name.setText("Name:"+guides.getGname());
                guideViewHolder.place.setText("Places:"+guides.getGplace());
                guideViewHolder.language.setText("Languages:"+guides.getGlanguage());
                guideViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(ApproveguideActivity.this,AdminApproveguideActivity.class);
                        intent.putExtra("phone",guides.getGphone());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public GuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.guide_layout,parent,false);
                GuideViewHolder ViewHolder=new GuideViewHolder(view);
                return ViewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}


