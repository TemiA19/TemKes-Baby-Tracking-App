package com.mobileapp.temkesbabytrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeightTrackingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    static final float END_SCALE = 0.7f;
    final Calendar myCalendar = Calendar.getInstance();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    FirebaseAuth auth;
    LinearLayout contentView;
    DatePickerDialog.OnDateSetListener DateSetListener;
    EditText BabyBirthDate, editText;
    TextView EditWeight;
    int import_weight = 0;
    TextView savebutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_tracking);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);

        navigationDrawer();
        contentView=findViewById(R.id.content_weight);

        BabyBirthDate = (EditText)findViewById(R.id.baby_birthday);
        savebutton = (TextView) findViewById(R.id.savebutton);
        EditWeight = (TextView) findViewById(R.id.baby_weight_changeweight);
        auth =FirebaseAuth.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        NumberPicker noPicker;
        final TextView lb_w;
        lb_w=findViewById(R.id.baby_weight_changeweight);
        noPicker=findViewById(R.id.np);
        noPicker.setMaxValue(100);
        noPicker.setMinValue(0);
        noPicker.setWrapSelectorWheel(true);
        noPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                lb_w.setText(String.valueOf(newVal));
                import_weight = newVal;
            }
        });

//        String user_id = auth.getCurrentUser().getUid();
//        final DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference("Users/" +user_id+"/Info/weight");

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = Integer.toString(import_weight);
                weight = EditWeight.getText().toString();
                if (weight.isEmpty()){
                    EditWeight.setError("Weight Required");
                    EditWeight.requestFocus();
                    return;
                }
                Intent i =new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(i);
            }
        });
    }

    private void updateLabel() {
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(dateFormat.format(myCalendar.getTime()));
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
//            case R.id.summary:
//                Intent intent_summary = new Intent(this, SummaryActivity.class);
//                startActivity(intent_summary);
//                break;
//            case R.id.chart:
//                Intent intent_chart = new Intent(this, ChartActivity.class);
//                startActivity(intent_chart);
//                break;

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