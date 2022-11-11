package com.mobileapp.temkesbabytrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class SummaryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    static final float END_SCALE = 0.7f;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    LinearLayout contentView;
    private TextView lastBottleFeedTime, lastBottleFeedOZ, lastBottleFeedNote;
    private TextView lastBreastFeedTime, leftBreastFeed, rightBreastFeed, lastBreastFeedNote;
    private TextView lastMealFeedTime, lastMealConsumed, lastSupplement, lastMealNote;
    private TextView lastDiaperTime, lastDiaperStatus, lastDiaperNote;
    private TextView lastNapTime, mLastNapDuration;

    private ValueEventListener valueEventListener, valueEventListener2, valueEventListener3, valueEventListener4, valueEventListener5;
    private DatabaseReference current_user_db, current_user_db2, current_user_db3, current_user_db4, current_user_db5;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        lastBottleFeedTime = findViewById(R.id.last_date_and_time);
        lastBottleFeedOZ = findViewById(R.id.last_amount_in_oz);
        lastBottleFeedNote = findViewById(R.id.txt_last_bf_note);
        lastBreastFeedTime = findViewById(R.id.last_date_and_time_breast_feed);
        leftBreastFeed = findViewById(R.id.left_breast_feed_time);
        rightBreastFeed = findViewById(R.id.right_breast_feed_time);
        lastBreastFeedNote = findViewById(R.id.txt_note_breast_feed);
        lastMealFeedTime = findViewById(R.id.last_time_stamp_meals);
        lastMealConsumed = findViewById(R.id.last_meal_consumed);
        lastSupplement = findViewById(R.id.last_supplement_consumed);
        lastMealNote = findViewById(R.id.txt_note_meal);
        lastDiaperTime = findViewById(R.id.diaper_last_timestamp);
        lastDiaperStatus = findViewById(R.id.edt_diaper_status);
        lastDiaperNote = findViewById(R.id.edt_diaper_note);
        lastNapTime = findViewById(R.id.nap_last_timestamp);
        mLastNapDuration = findViewById(R.id.edt_nap_duration);

        navigationDrawer();
        contentView=findViewById(R.id.content);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        current_user_db = FirebaseDatabase.getInstance().getReference()
               .child("FeedingTracking").child(user_id).child("Feeding").child("Bottle Feeding").child("Timestamp");

        valueEventListener = current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    final String date = child.getKey();
//                    lastBottleFeedTime.setText(Date_and_Time);
                    current_user_db.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()){
                                String time = child.getKey();
                                //String Amount_In_Oz = child.getKey();
                                lastBottleFeedTime.setText(date +" "+time);
                                //lastBottleFeedOZ.setText(Amount_In_Oz);
                                String Amount_In_Oz = child.child("Amount_In_OZ").getValue(String.class);
                                lastBottleFeedOZ.setText(Amount_In_Oz);
                                String Bottle_Feeding_Notes = child.child("Bottle_Feeding_Notes").getValue(String.class);
                                lastBottleFeedNote.setText(Bottle_Feeding_Notes);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        // end of bottle feeding

        // beginning of breast feeding
        current_user_db2 = FirebaseDatabase.getInstance().getReference()
                .child("FeedingTracking").child(mAuth.getCurrentUser().getUid())
                .child("Feeding").child("Breast Feeding").child("Timestamp");
//
        valueEventListener2 = current_user_db2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    final String date = child.getKey();

                    current_user_db2.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()){
                                String time = child.getKey();
                                lastBreastFeedTime.setText(date + " "+ time);
                                String Left_Breast_Feeding_Time = child.child("Left_Breast_Feeding_Time").getValue(String.class);
                                leftBreastFeed.setText(Left_Breast_Feeding_Time);
                                String Right_Breast_Feeding_Time = child.child("Right_Breast_Feeding_Time").getValue(String.class);
                                rightBreastFeed.setText(Right_Breast_Feeding_Time);
                                String Breast_Feeding_Note = child.child("Breast_Feeding_Note").getValue(String.class);
                                lastBreastFeedNote.setText(Breast_Feeding_Note);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // beginning of meal feeding
        current_user_db3 = FirebaseDatabase.getInstance().getReference()
                .child("MealTracker").child(mAuth.getCurrentUser().getUid())
                .child("Feeding").child("Meal Feeding").child("Timestamp");

        valueEventListener3 = current_user_db3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    final String date = child.getKey();

                    current_user_db3.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child: dataSnapshot.getChildren()){
                                String time = child.getKey();
                                lastMealFeedTime.setText(date + " "+ time);
                                String Meal_Consumed = child.child("Meal_Consumed").getValue(String.class);
                                lastMealConsumed.setText(Meal_Consumed);
                                String Supplement_Consumed = child.child("Supplement_Consumed").getValue(String.class);
                                lastSupplement.setText(Supplement_Consumed);
                                String Meal_Note = child.child("Meal_Note").getValue(String.class);
                                lastMealNote.setText(Meal_Note);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // beginning of Diaper Status
        current_user_db4 = FirebaseDatabase.getInstance().getReference()
                .child("DiaperTracking").child(mAuth.getCurrentUser().getUid())
                .child("Diaper Status").child("Timestamp");
//
        valueEventListener4 = current_user_db4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    final String date = child.getKey();

                    current_user_db4.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                String time = child.getKey();
                                lastDiaperTime.setText(date + " " + time);
                                String last_diaper_status = child.child("last_diaper_status").getValue(String.class);
                                lastDiaperStatus.setText(last_diaper_status);
                                String last_diaper_note = child.child("last_diaper_note").getValue(String.class);
                                lastDiaperNote.setText(last_diaper_note);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Beginning of Sleep Status

        current_user_db5 = FirebaseDatabase.getInstance().getReference()
                .child("sleepingTracker").child(mAuth.getCurrentUser().getUid())
                .child("Nap Entry").child("Timestamp");

        valueEventListener5 = current_user_db5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    final String date = child.getKey();

                    current_user_db5.child(date).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                String time = child.getKey();
                                lastNapTime.setText(date + " " + time);
                                String lastNapDuration = child.child("sleep_time").getValue(String.class);
                                int lastNapDuration2 = Integer.parseInt(lastNapDuration);
                                if (lastNapDuration2 > 1) {
                                    mLastNapDuration.setText(lastNapDuration2 + " mins");
                                } else {
                                    mLastNapDuration.setText(lastNapDuration + " min");
                                }
                                mLastNapDuration.setText(lastNapDuration + " mins");
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        current_user_db.removeEventListener(valueEventListener);
        current_user_db2.removeEventListener(valueEventListener2);
        current_user_db3.removeEventListener(valueEventListener3);
        current_user_db4.removeEventListener(valueEventListener4);
        current_user_db5.removeEventListener(valueEventListener5);
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