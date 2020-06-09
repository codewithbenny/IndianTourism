package com.india.tourism;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.india.tourism.Guide.Tourist.Dialog1;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.security.PrivateKey;
import java.util.ArrayList;

import io.paperdb.Paper;

public class TestActivity extends AppCompatActivity {
    private EditText search;
    private TextView noguide;
    private RecyclerView list;
    private DatabaseReference ref;
    ArrayList<String> profile;
    private ImageView Asearch;
    ArrayList<String> city;
    SearchView searchView;
    private  EditText editText;
    ArrayList<String> languages;
    ArrayList<String> place;
    ArrayList<String> name;
    ArrayList<String> phone;
    ArrayList<String> phonekey;
    ArrayList<String> Exp;
    ArrayList<String> pay;
    SearchAdapter adapter;
    String profilepic;
    String Pname;
    RelativeLayout relativeLayout;
    ToolTipsManager mToolTipsManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Asearch=findViewById(R.id.nsearch1);
        list = findViewById(R.id.list);
        noguide=findViewById(R.id.noguide);
        search = findViewById(R.id.search_edit_test);
        city = new ArrayList<>();
        languages = new ArrayList<>();
        place = new ArrayList<>();
        name = new ArrayList<>();
        profile = new ArrayList<>();
        phone = new ArrayList<>();
        phonekey = new ArrayList<>();
        pay=new ArrayList<>();
        Exp=new ArrayList<>();
        mToolTipsManager = new ToolTipsManager();

        ToolTip.Builder builder = new ToolTip.Builder(this, Asearch, relativeLayout, "Click to search", ToolTip.POSITION_BELOW);

        Pname=getIntent().getStringExtra("Pname");
         if(Pname!=null){
             openDialog1();
         }
         search.setFocusable(true);
        Asearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Pname!=null){
                    search.setText(Pname);
                    search.setVisibility(View.INVISIBLE);
                    Asearch.setVisibility(View.INVISIBLE);
                    noguide.setVisibility(View.VISIBLE);

                }else {
                    Asearch.setVisibility(View.INVISIBLE);
                    search.setVisibility(View.VISIBLE);
                    openDialog();
                    noguide.setVisibility(View.GONE);
                }
            }
        });

        ref = FirebaseDatabase.getInstance().getReference().child("ApprovedGuides");

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);
        list.removeAllViews();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                   if
                      (!editable.toString().isEmpty()) {
                          setadapter(editable.toString());
                      }

                }


        });

    }

    private void openDialog() {
        com.india.tourism.Dialog dialog=new Dialog();
        dialog.show(getSupportFragmentManager(),"dialog");
    }
    private void openDialog1() {
       Dialog1 dialog1=new Dialog1();
        dialog1.show(getSupportFragmentManager(),"dialog");
    }
    public void setadapter(final String searchedstring) {
        languages.clear();
        city.clear();
        place.clear();
        name.clear();
        place.clear();
        phone.clear();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String phonekey = snapshot.getKey();
//                    String eexp=snapshot.child("Agexperience").getValue().toString();
                        String ppay = snapshot.child("Apayscale").getValue().toString();
                        String phoee = snapshot.child("Agphone").getValue().toString();
                        String glanguage = snapshot.child("Aglanguage").getValue().toString();
                        String gplaces = snapshot.child("Agplace").getValue().toString();
                        String gcity = snapshot.child("Agcity").getValue().toString();
                        String gname = snapshot.child("Agname").getValue().toString();
                        String profilepic1 = snapshot.child("Agimage").getValue().toString();




                        if (glanguage.contains(searchedstring)) {
                            languages.add(glanguage);
                            city.add(gcity);
                            place.add(gplaces);
                            name.add(gname);
                            profile.add(profilepic1);
                            phone.add(phoee);
                            pay.add(ppay);
                        } else if (gplaces.contains(searchedstring)) {
                            languages.add(glanguage);
                            city.add(gcity);
                            place.add(gplaces);
                            name.add(gname);
                            profile.add(profilepic1);
                            phone.add(phoee);
                            pay.add(ppay);
                        } else if (gcity.contains(searchedstring)) {
                            languages.add(glanguage);
                            city.add(gcity);
                            place.add(gplaces);
                            name.add(gname);
                               profile.add(profilepic1);
                               phone.add(phoee);
                            pay.add(ppay);
                        } else if (gname.contains(searchedstring)) {
                            languages.add(glanguage);
                            city.add(gcity);
                            place.add(gplaces);
                            name.add(gname);
                            profile.add(profilepic1);
                            phone.add(phoee);
                            pay.add(ppay);
                        }

                        adapter = new SearchAdapter(profile, city, languages, place, name, phone, Exp, pay, getApplicationContext());
                        list.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}