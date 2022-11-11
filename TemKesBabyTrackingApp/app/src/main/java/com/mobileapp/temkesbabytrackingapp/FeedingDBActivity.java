package com.mobileapp.temkesbabytrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FeedingDBActivity extends AppCompatActivity {

    ImageView arrowIcon;
    RelativeLayout breastFeedingDB, bottleDB, mealDB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeding_dbactivity);

        arrowIcon = findViewById(R.id.arrow);
        breastFeedingDB = findViewById(R.id.breastFeedingDB);
        bottleDB = findViewById(R.id.bottleDB);
        mealDB = findViewById(R.id.mealDB);


        arrowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedingDBActivity.this,HomeActivity.class));
            }
        });

        breastFeedingDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedingDBActivity.this, BreastFeedingDB.class));
            }
        });

        bottleDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedingDBActivity.this, BottleDB.class));
            }
        });

        mealDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedingDBActivity.this, MealDB.class));
            }
        });
    }

}