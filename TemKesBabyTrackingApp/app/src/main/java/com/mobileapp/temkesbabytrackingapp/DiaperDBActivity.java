package com.mobileapp.temkesbabytrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DiaperDBActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
//
//    ImageView arrowIcon;
    Button startBtn, saveBtn;
    EditText lastDiaperChangeTime, diaperNote;
    FirebaseAuth auth;
    Spinner diaperStatusSpinner;
    String date, time;
    private int dayFinal, monthFinal, yearFinal;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaper_dbactivity);
        auth = FirebaseAuth.getInstance();

//        arrowIcon = findViewById(R.id.arrow);
        startBtn = (Button) findViewById(R.id.start_button);
        saveBtn = (Button) findViewById(R.id.save_button);

        addListenerOnSpinnerItemSelected();
        addListenerOnButton();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateAndTime = lastDiaperChangeTime.getText().toString();
                String diaperStatus = diaperStatusSpinner.getSelectedItem().toString();
                String diaperTxt = diaperNote.getText().toString();

                diaperStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String diaper_status = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                String user_id = auth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("DiaperTracking")
                        .child(user_id).child("Diaper Status").child("Timestamp");
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                Map newPost = new HashMap();
                newPost.put("last_diaper_status", diaperStatus);
                newPost.put("last_diaper_note", diaperTxt);
                newPost.put("date_and_time", dateAndTime);

                current_user_db.child(date).child(time).setValue(newPost);

                Intent diaper = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(diaper);
            }
        });

        startBtn = (Button) findViewById(R.id.start_button);
        lastDiaperChangeTime = (EditText) findViewById(R.id.date_and_time);
        diaperNote = (EditText) findViewById(R.id.diaper_text);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(DiaperDBActivity.this,
                        DiaperDBActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });

//        arrowIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(DiaperDBActivity.this, HomeActivity.class));
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(DiaperDBActivity.this, DiaperDBActivity.this,
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
        lastDiaperChangeTime.setText(monthFinal + "/" + dayFinal + "/" + yearFinal + " (" + updateTime + ")");

        time = " (" + updateTime + " )";
    }

    private void addListenerOnSpinnerItemSelected() {
        diaperStatusSpinner = (Spinner) findViewById(R.id.sp_diaper_status);
    }

    private void addListenerOnButton() {
        diaperStatusSpinner = (Spinner) findViewById(R.id.sp_diaper_status);
    }

}