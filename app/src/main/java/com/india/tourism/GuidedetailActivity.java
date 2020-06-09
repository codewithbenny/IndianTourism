package com.india.tourism;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GuidedetailActivity extends AppCompatActivity {
    private TextView name, city, phone, language, places, experience, payscale;
    private ImageView imageView,call,message;
    private Button booknow, chat;
    String phonekey,iname,iphone,iimage,ilanguage,icity,iplace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidedetail);
        name = findViewById(R.id.usernamese);
        city = findViewById(R.id.cityse);
        message=findViewById(R.id.messagese);
        call=findViewById(R.id.callse);
        phone = findViewById(R.id.userphnse);
        places = findViewById(R.id.placesse);
        experience = findViewById(R.id.experiencese);
        language = findViewById(R.id.languagese);
        payscale = findViewById(R.id.payscalese);
        imageView = findViewById(R.id.guideprofilese);
        chat = findViewById(R.id.chatse);

        iname=getIntent().getStringExtra("Name");
        ilanguage=getIntent().getStringExtra("Language");
        iplace=getIntent().getStringExtra("places");
        iphone=getIntent().getStringExtra("phonekey");
        iimage=getIntent().getStringExtra("image");



    }
}
