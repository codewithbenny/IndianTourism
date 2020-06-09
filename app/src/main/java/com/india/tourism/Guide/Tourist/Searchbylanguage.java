package com.india.tourism.Guide.Tourist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.tourism.Model.ApprovedGuide;
import com.india.tourism.R;
import com.india.tourism.ViewHolder.GuideViewHolder;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class Searchbylanguage extends AppCompatActivity {
    private EditText searchcity;
    private Button search;
    private RecyclerView guidelist;
    String searchinput,phonekey,language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbylanguage);
     //   search=findViewById(R.id.searchbtnlanguage);
        guidelist=findViewById(R.id.guide_list_language);
        guidelist.setHasFixedSize(true);
        guidelist.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myinfo", 0);

        phonekey = preferences.getString("userphone", null);
       onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ApprovedGuides");
        DatabaseReference useref=FirebaseDatabase.getInstance().getReference().child("Users").child(phonekey);

        useref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 language=dataSnapshot.child("language").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseRecyclerOptions<ApprovedGuide> options = new FirebaseRecyclerOptions.Builder<ApprovedGuide>()
                .setQuery(reference.orderByChild("Aglanguage").startAt(language), ApprovedGuide.class).build();
        FirebaseRecyclerAdapter<ApprovedGuide, GuideViewHolder> adapter = new FirebaseRecyclerAdapter<ApprovedGuide, GuideViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GuideViewHolder guideViewHolder, int i, @NonNull final ApprovedGuide guides) {
                guideViewHolder.name.setText("Name:" + guides.getAgname());
                guideViewHolder.place.setText("Places:" + guides.getAgplace());
                guideViewHolder.language.setText("Languages:" + guides.getAglanguage());
                Picasso.get().load(guides.getAgimage()).into(guideViewHolder.imageprofile);

                guideViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(Searchbylanguage.this, GuideDetailsforUserActivity.class);
                        intent.putExtra("guideid",guides.getAgphone());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public GuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guide_layout, parent, false);
                GuideViewHolder ViewHolder = new GuideViewHolder(view);
                return ViewHolder;
            }
        };
        guidelist.setAdapter(adapter);
        adapter.startListening();
    }
    }

