package com.india.tourism.Guide.Tourist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.india.tourism.AdapterSearch;
import com.india.tourism.Model.ApprovedGuide;
import com.india.tourism.Model.Places;
import com.india.tourism.PlaceAdapter;
import com.india.tourism.R;
import com.india.tourism.ViewHolder.GuideViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Searchbyplace extends AppCompatActivity {
    private EditText searchcity;
    private Button search;
    private RecyclerView guidelist;
    String searchinput;
    private DatabaseReference reference;
    Query query;
    private ArrayList<ApprovedGuide> placeguide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbyplace);
        searchcity = findViewById(R.id.search_edit_place);
        search = findViewById(R.id.searchbtn);
        reference = FirebaseDatabase.getInstance().getReference().child("ApprovedGuides");

        guidelist = findViewById(R.id.guide_list_place);
        guidelist.setHasFixedSize(true);
        guidelist.setLayoutManager(new LinearLayoutManager(this));
        searchinput = getIntent().getStringExtra("Pname");

        searchcity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().isEmpty()){
                    sea(editable.toString());
                }else {
                    sea("");
                }

            }
        });
    }

    private void sea(String toString) {
        Query query=reference.orderByChild("Name").startAt(toString)
                .endAt(toString+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    placeguide.clear();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        final ApprovedGuide places1=snapshot.getValue(ApprovedGuide.class);
                        placeguide.add(places1);
                    }
                    PlaceAdapter placeAdapter=new PlaceAdapter(placeguide,getApplicationContext());
                    guidelist.setAdapter(placeAdapter);
                    placeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ApprovedGuide> options = new FirebaseRecyclerOptions.Builder<ApprovedGuide>()
                .setQuery(query
                       , ApprovedGuide.class).build();
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
                        Intent intent=new Intent(Searchbyplace.this, GuideDetailsforUserActivity.class);
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


