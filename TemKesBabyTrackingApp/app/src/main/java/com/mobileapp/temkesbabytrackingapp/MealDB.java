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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MealDB extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ImageView arrowIcon;
    Button startBtn, saveBtn;
    TextView dateAndTime;
    Spinner mealSpinner, supplementSpinner;
    EditText mealNote;
    FirebaseAuth auth;
    String date;
    String time;
    private int dayFinal, monthFinal, yearFinal;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_db);
        auth = FirebaseAuth.getInstance();

        arrowIcon = findViewById(R.id.arrow);
        startBtn = findViewById(R.id.start_button);
        saveBtn = findViewById(R.id.save_button);
        dateAndTime = findViewById(R.id.timeDate);
        mealSpinner = findViewById(R.id.meal_spinner);
        supplementSpinner = findViewById(R.id.supplement_spinner);
        mealNote = findViewById(R.id.meal_note);

        arrowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MealDB.this,FeedingDBActivity.class));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mealPicked = mealSpinner.getSelectedItem().toString();
                String supplementPicked = supplementSpinner.getSelectedItem().toString();
                String dateTime = dateAndTime.getText().toString();
                String mealTxt = mealNote.getText().toString();

                mealSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String meal_arrays = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                supplementSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String supplement_arrays = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                String user_id = auth.getCurrentUser().getUid();
                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("MealTracker").child(user_id)
                        .child("Feeding").child("Meal Feeding").child("Timestamp");
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                Map newPost = new HashMap();
                newPost.put("Meal_Consumed", mealPicked);
                newPost.put("Supplement_Consumed", supplementPicked);
                newPost.put("Date_and_Time", dateTime);
                newPost.put("Meal_Note", mealTxt);

                current_user_db.child(date).child(time).setValue(newPost);
                Intent meal = new Intent(getApplicationContext(), FeedingDBActivity.class);
                startActivity(meal);
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MealDB.this, MealDB.this, year, month, day);
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(MealDB.this, MealDB.this,
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