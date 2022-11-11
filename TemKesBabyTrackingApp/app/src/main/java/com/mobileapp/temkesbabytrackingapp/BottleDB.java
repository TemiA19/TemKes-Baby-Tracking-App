package com.mobileapp.temkesbabytrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BottleDB extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ImageView arrowIcon;
    Button startBtn, saveBtn;
    EditText dateAndTime;
    EditText amtInOz, bottleFeedingNotes;
    FirebaseAuth auth;
    String date;
    String time;
    private int dayFinal, monthFinal, yearFinal;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle_db);
        auth = FirebaseAuth.getInstance();

        arrowIcon = findViewById(R.id.arrow);
        startBtn = findViewById(R.id.start_button);
        saveBtn = findViewById(R.id.save_button);
        amtInOz = findViewById(R.id.amt_in_oz);
        bottleFeedingNotes = findViewById(R.id.bottle_feeding_notes);
        dateAndTime = findViewById(R.id.date_and_time);

        arrowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BottleDB.this,FeedingDBActivity.class));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountOfOz = amtInOz.getText().toString();
                String bottleNote = bottleFeedingNotes.getText().toString();
                String dateTime = dateAndTime.getText().toString();

                String user_id = auth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("FeedingTracking")
                        .child(user_id).child("Feeding").child("Bottle Feeding").child("Timestamp");

                Map newPost = new HashMap();
                newPost.put("Amount_In_OZ", amountOfOz);
                newPost.put("Bottle_Feeding_Notes", bottleNote);
                newPost.put("Date_And_Time", dateTime);

                current_user_db.child(date).child(time).setValue(newPost);

                Intent bottle = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(bottle);
            }
        });

        startBtn = findViewById(R.id.start_button);
        dateAndTime = findViewById(R.id.date_and_time);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(BottleDB.this, BottleDB.this, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = day;

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(BottleDB.this, BottleDB.this,
                hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

        date = monthFinal + "-" + dayFinal + "-" + yearFinal;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        String updateTime = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(calendar.getTime());
        dateAndTime.setText(monthFinal + "/" + dayFinal + "/" + yearFinal + " (" + updateTime + ")");

        time = " ("+ updateTime + ")";
    }
}