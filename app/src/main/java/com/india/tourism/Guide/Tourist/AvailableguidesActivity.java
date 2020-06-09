package com.india.tourism.Guide.Tourist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.india.tourism.Model.ApprovedGuide;
import com.india.tourism.R;
import com.india.tourism.ViewHolder.GuideViewHolder;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class AvailableguidesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String guidestate="";
    private String Pname;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availableguides);
        recyclerView = findViewById(R.id.recycler1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Pname=getIntent().getStringExtra("Pname");
        toolbar=findViewById(R.id.searchtoolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AvailableguidesActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ApprovedGuides");
            FirebaseRecyclerOptions<ApprovedGuide> options = new FirebaseRecyclerOptions.Builder<ApprovedGuide>()
                    .setQuery(reference, ApprovedGuide.class).build();
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
                            Intent intent=new Intent(AvailableguidesActivity.this, GuideDetailsforUserActivity.class);
                            intent.putExtra("guideid",guides.getAgphone());
                            Paper.init(getApplicationContext());
                            Paper.book().write("only4u",guides.getAgphone());
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
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }
    }




