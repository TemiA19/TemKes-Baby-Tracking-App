package com.mobileapp.temkesbabytrackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class TummyDBActivity extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 300000;

    TextView countDown;
    Button startPauseButton, resetButton;
    CountDownTimer countDownTimer;
    private boolean timeRunning;
    private long timeLeftInMillis = START_TIME_IN_MILLIS;

//    ImageView arrowIcon;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tummy_dbactivity);

        countDown = findViewById(R.id.countdown_text);
        startPauseButton = findViewById(R.id.countdown_button); 
        resetButton = findViewById(R.id.button_reset); 
        
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeRunning) {
                    pauseTimer();
                }else {
                    startTimer();
                }
            }
        });
        
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        
        updateCountDownText(); 

//        arrowIcon = findViewById(R.id.arrow);


//        arrowIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TummyDBActivity.this,HomeActivity.class));
//            }
//        });

    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes,seconds);
        countDown.setText(timeLeftFormatted);
    }

    private void resetTimer() {
        timeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        resetButton.setVisibility(View.INVISIBLE);
        startPauseButton.setVisibility(View.VISIBLE);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeRunning = false;
                startPauseButton.setText("Start");
                startPauseButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.VISIBLE);
            }
        }.start();
        timeRunning = true;
        startPauseButton.setText("Pause");
        resetButton.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timeRunning = false;
        startPauseButton.setText("Start");
        resetButton.setVisibility(View.VISIBLE);
    }

}