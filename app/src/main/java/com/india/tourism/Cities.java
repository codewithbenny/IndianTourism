package com.india.tourism;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Cities extends AppCompatActivity {
private RecyclerView recyclerView;
private List<String> city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        city=new ArrayList<>();
        recyclerView=findViewById(R.id.cityview);
        city.add("Agra");
        city.add("Chennai");
        city.add("Darjeeling");
        city.add("Delhi");
        city.add("Jaipur");
        city.add("Manali");
        city.add("Mumbai");
        city.add("Shimla");

        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),city);
        recyclerView.setAdapter(arrayAdapter);






    }
}
