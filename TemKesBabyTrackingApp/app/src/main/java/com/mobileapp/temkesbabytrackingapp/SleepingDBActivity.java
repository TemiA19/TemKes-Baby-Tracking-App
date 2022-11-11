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
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SleepingDBActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

//    ImageView arrowIcon;
    Button startBtn, sleepSaveBtn;
    EditText lastSleepTime;
    TextView sleepTime;
    NumberPicker noPicker;
    FirebaseAuth auth;
    String date, time;
    private  int dayFinal, monthFinal, yearFinal;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sleeping_dbactivity);

//        arrowIcon = findViewById(R.id.arrow);
        startBtn = (Button) findViewById(R.id.start_button);
        sleepSaveBtn = (Button) findViewById(R.id.save_button_sleep);
        lastSleepTime = (EditText) findViewById(R.id.date_and_time);
        sleepTime = (TextView) findViewById(R.id.txt_sleep_time_view);
        noPicker = findViewById(R.id.number_picker_sleep);

        noPicker.setMaxValue(60);
        noPicker.setMinValue(0);
        noPicker.setWrapSelectorWheel(true);

        noPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sleepTime.setText(String.valueOf(newVal));
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SleepingDBActivity.this, SleepingDBActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });

        sleepSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateAndTime = lastSleepTime.getText().toString();
                String sleep_time = sleepTime.getText().toString();
                String user_id = auth.getCurrentUser().getUid();
                DatabaseReference current_user_id = FirebaseDatabase.getInstance().getReference().child("sleepingTracker").child(user_id)
                        .child("Nap Tracking").child("Timestamp");
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                Map newPost = new HashMap();
                newPost.put("sleep_time", sleep_time);
                newPost.put("date_and_time", dateAndTime);

                current_user_id.child(date).child(time).setValue(newPost);

                Intent sleep = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(sleep);
                startActivity(new Intent(SleepingDBActivity.this,HomeActivity.class));
            }
        });

//        arrowIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SleepingDBActivity.this,HomeActivity.class));
//            }
//        });

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = day;

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(SleepingDBActivity.this, SleepingDBActivity.this,
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
        lastSleepTime.setText(monthFinal + "/" + dayFinal + "/" + yearFinal + " (" + updateTime + ")");

        time = " (" + updateTime + " )";
    }
}
