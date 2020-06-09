package com.india.tourism.Guide.Tourist;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.india.tourism.AdapterSearch;
import com.india.tourism.MessageAdapter;
import com.india.tourism.Model.Places;
import com.india.tourism.R;
import com.india.tourism.SearchAdapter;
import com.india.tourism.TestActivity;
import com.india.tourism.ViewHolder.PlacesViewHolder;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    DatabaseReference reference;
    private ActionBarDrawerToggle drawerToggle;
    String phonekey,userprofile,name;
    private TextView login;
    FirebaseRecyclerOptions options;
    private EditText searchedit;
    private ArrayList<Places>placename;
    private CircleImageView profile;
    private ImageView search;
    private FloatingActionButton Notification1;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recycler);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Notification1=findViewById(R.id.fab121);
        searchedit=findViewById(R.id.search_edit_text);
        search=findViewById(R.id.nsearch);
        placename=new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Places");
        Notification1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,Notifications.class);
                startActivity(intent);
            }
        });
        searchedit.addTextChangedListener(new TextWatcher() {
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
        toolbar.setTitle("Places");

        reference.keepSynced(true);
        setSupportActionBar(toolbar);
        recyclerView.setHasFixedSize(true);
        Paper.init(this);
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("myinfo",0);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setVisibility(View.GONE);
                searchedit.setVisibility(View.VISIBLE);

            }
        });

        phonekey=preferences.getString("userphone",null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);

            }
        });
        loadingbar=new ProgressDialog(this);
        loadingbar.setTitle("Please wait ");
        loadingbar.setMessage("While We are Preparing for you");

        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        login = headerView.findViewById(R.id.login);
        profile = headerView.findViewById(R.id.profile_circle);
        if (phonekey == null) {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeActivity.this, LogintouristActivity.class);
                    startActivity(intent);
                }
            });
        }else {
            updateprofile();

        }



    }

    private void sea(String toString) {
       final DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        Query query=reference.orderByChild("Name").startAt(toString)
                .endAt(toString+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    placename.clear();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        final Places places1=snapshot.getValue(Places.class);
                        placename.add(places1);
                    }
                    AdapterSearch adapterSearch=new AdapterSearch(placename,getApplicationContext());
                    recyclerView.setAdapter(adapterSearch);
                    adapterSearch.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            Intent intent = new Intent(HomeActivity.this, AvailableguidesActivity.class);
            //     Toast.makeText(this, "Available Guides", Toast.LENGTH_SHORT).show();
            startActivity(intent);
    //    }
//        } else if (id == R.id.nav_availableplaces) {
//            SharedPreferences preferences = getApplicationContext().getSharedPreferences("myinfo", 0);
//
//            String phonekey121 = preferences.getString("userphone", null);
//            if (phonekey121 == null) {
//                Toast.makeText(this, "Sorry Please login to explore More", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(HomeActivity.this, LogintouristActivity.class);
//                startActivity(intent);
//            }else {
//            Intent intent = new Intent(HomeActivity.this, Searchbylanguage.class);
//            startActivity(intent);
//        }
        } else if (id == R.id.nav_city) {
            Intent intent=new Intent(HomeActivity.this, TestActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_settings) {
            if (phonekey!=null) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Please Login to go to Settings", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_logOut) {
            SharedPreferences preferences=getApplicationContext().getSharedPreferences("myinfo",0);
            SharedPreferences.Editor editor=preferences.edit();
            editor.clear();
            editor.commit();
            Paper.init(this);
            Paper.book().destroy();
            Intent intent=new Intent(HomeActivity.this,LogintouristActivity.class);
            startActivity(intent);
            finish();
        }else if(id==R.id.nav_share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://drive.google.com/file/d/1_zdIU6VMEgkPbncnbIoQA69HzXXZrdhk/view");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {

        super.onStart();
        loadingbar.show();
         options=
                new FirebaseRecyclerOptions.Builder<Places>().setQuery(reference,Places.class).build();
        FirebaseRecyclerAdapter<Places, PlacesViewHolder>adapter=new FirebaseRecyclerAdapter<Places, PlacesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PlacesViewHolder placesViewHolder, int i, @NonNull final Places places) {
                loadingbar.dismiss();
                placesViewHolder.txtplacename.setText(places.getName());
            Picasso.get().load(places.getImg()).into(placesViewHolder.imageView);
            placesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(HomeActivity.this, DetailBookActivity.class);
                    intent.putExtra("pid",places.getPid());
                    startActivity(intent);
                }
            });
            }

            @NonNull
            @Override
            public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.places,parent,false);
                PlacesViewHolder productViewHolder=new PlacesViewHolder(view);
                return productViewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    public void updateprofile(){
        DatabaseReference updateref=FirebaseDatabase.getInstance().getReference().child("Users").child(phonekey);
        updateref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                if(dataSnapshot.child("image").exists()) {
                    userprofile = dataSnapshot.child("image").getValue().toString();
                }
                    name = dataSnapshot.child("name").getValue().toString();

                    login.setText(name);
                  if(userprofile!=null) {
               Picasso.get().load(userprofile).into(profile);
    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    }



