package com.mobileapp.temkesbabytrackingapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BreastFeedingDB extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ImageView arrowIcon;
    Button startBtn;
    EditText dateTimeResult, breastFeedingNotes;
    Button saveBtn;
    Spinner leftBreastSpinner, rightBreastSpinner;
    FirebaseAuth auth;
    String breastFeeding, date, time;
    private int dayFinal, monthFinal, yearFinal;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breast_feeding_db);
        auth = FirebaseAuth.getInstance();

        addItemOnSpinner();
        addListenerOnButton();
        addListnerOnSpinner();

        arrowIcon = findViewById(R.id.arrow);
        saveBtn = (Button) findViewById(R.id.save_button);
        startBtn = (Button) findViewById(R.id.start_button);
        dateTimeResult = (EditText) findViewById(R.id.date_and_time_breastFeeding);
        leftBreastSpinner = (Spinner) findViewById(R.id.left_breast_feed_spinner);
        rightBreastSpinner = (Spinner) findViewById(R.id.right_breast_feed_spinner);
        breastFeedingNotes = (EditText) findViewById(R.id.breast_feeding_notes);

        arrowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BreastFeedingDB.this,FeedingDBActivity.class));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateAndTime = dateTimeResult.getText().toString();
                String breastFeedingNote = breastFeedingNotes.getText().toString();
                String leftBreastTime = leftBreastSpinner.getSelectedItem().toString();
                String rightBreastTime = rightBreastSpinner.getSelectedItem().toString();

                leftBreastSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String leftTime = adapterView.getItemAtPosition(i).toString();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                rightBreastSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String rightTime = adapterView.getItemAtPosition(i).toString();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                String user_id = auth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("FeedingTracking")
                        .child(user_id).child("Feeding").child("Breast Feeding").child("Timestamp");
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                Map newPost = new HashMap();
                newPost.put("Left_Breast_Feeding_Time", leftBreastTime);
                newPost.put("Right_Breast_Feeding_Time", rightBreastTime);
                newPost.put("Breast_Feeding_Note", breastFeedingNote);
                newPost.put("Date_and_Time", dateAndTime);

                current_user_db.child(date).child(time).setValue(newPost);
                Intent breastfeeding = new Intent(getApplicationContext(), FeedingDBActivity.class);
                startActivity(breastfeeding);
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog( BreastFeedingDB.this,
                        BreastFeedingDB.this,year,month,day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day){
        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = day;

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(BreastFeedingDB.this, BreastFeedingDB.this,
                hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

        date = monthFinal + "-" + dayFinal + "-" + yearFinal;

    }


    private void addListnerOnSpinner() {
        leftBreastSpinner = findViewById(R.id.left_breast_feed_spinner);
    }

    private void addListenerOnButton() {
        leftBreastSpinner = findViewById(R.id.left_breast_feed_spinner);
        rightBreastSpinner = findViewById(R.id.right_breast_feed_spinner);
    }

    private void addItemOnSpinner() {
        rightBreastSpinner = findViewById(R.id.right_breast_feed_spinner);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        String updateTime = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(calendar.getTime());
        dateTimeResult.setText(monthFinal + "/" + dayFinal + "/" + yearFinal + " (" + updateTime + ")");

        time = " (" + updateTime + " )";
    }

}