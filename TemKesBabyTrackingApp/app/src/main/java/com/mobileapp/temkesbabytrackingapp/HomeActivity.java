package com.mobileapp.temkesbabytrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final float END_SCALE = 0.7f;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    FirebaseAuth auth;
    LinearLayout contentView;
    RelativeLayout feedingDB, diaperDB, sleepingDB, ttDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        feedingDB = findViewById(R.id.feedingDB);
        diaperDB = findViewById(R.id.diaperDB);
        sleepingDB = findViewById(R.id.sleepingDB);
        ttDB = findViewById(R.id.ttDB);

        navigationDrawer();

        contentView=findViewById(R.id.content);

        feedingDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,FeedingDBActivity.class));
            }
        });

        diaperDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,DiaperDBActivity.class));
            }
        });

        sleepingDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,SleepingDBActivity.class));
            }
        });

        ttDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,TummyDBActivity.class));
            }
        });

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Dialog dialog = new Dialog(HomeActivity.this, R.style.Dialog);
//                dialog.setContentView(R.layout.dialog_layout);
//                TextView yesBtn, noBtn;
//                yesBtn = dialog.findViewById(R.id.yesBtn);
//                noBtn = dialog.findViewById(R.id.noBtn);
//                yesBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        FirebaseAuth.getInstance().signOut();
//                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                    }
//                });
//
//                noBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//            }
//        });

//        auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser() == null) {
//            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//        }
    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                     drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent_home = new Intent(this, HomeActivity.class);
                startActivity(intent_home);
                break;
//            case R.id.weight:
//                Intent intent_weight = new Intent(this, WeightTrackingActivity.class);
//                startActivity(intent_weight);
//                break;
            case R.id.summary:
                Intent intent_summary = new Intent(this, SummaryActivity.class);
                startActivity(intent_summary);
                break;
//            case R.id.chart:
//                Intent intent_chart = new Intent(this, ChartActivity.class);
//                startActivity(intent_chart);
//                break;
            case R.id.edit_profile:
                Intent intent_profile = new Intent(this, EditProfileActivity.class);
                startActivity(intent_profile);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            default:
                return true;
        }
        return true;
    }
}