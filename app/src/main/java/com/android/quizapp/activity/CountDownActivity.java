package com.android.quizapp.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quizapp.R;
import com.android.quizapp.interfaces.TimerListener;

public class CountDownActivity extends AppCompatActivity implements TimerListener {

    CountDownTimer timer;
    TextView timerTV;
    TextView hourTV;
    TextView hourUnitTV;
    TextView minTV;
    TextView minUnitTV;
    TextView secTV;
    TextView secUnitTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timerTV = (TextView) findViewById(R.id.timer_tv);
        hourTV = (TextView) findViewById(R.id.hoursTV);
        hourUnitTV = (TextView) findViewById(R.id.hours_unit_tv);
        minTV = (TextView) findViewById(R.id.minutesTV);
        minUnitTV = (TextView) findViewById(R.id.minutes_unit_tv);
        secTV = (TextView) findViewById(R.id.secondsTV);
        secUnitTV = (TextView) findViewById(R.id.seconds_unit_tv);

        timer = new CountDownTimer(70000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;

                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;

                String yy = String.format("%02dh %02dm %02ds", elapsedHours, elapsedMinutes, elapsedSeconds);
                timerTV.setText(yy);

                if (elapsedHours == 0) {
                    hourTV.setVisibility(View.GONE);
                    hourUnitTV.setVisibility(View.GONE);
                }
                if (elapsedMinutes == 0 && elapsedHours == 0) {
                    hourTV.setVisibility(View.GONE);
                    hourUnitTV.setVisibility(View.GONE);
                    minTV.setVisibility(View.GONE);
                    minUnitTV.setVisibility(View.GONE);
                }

                String hour = String.format("%02d", elapsedHours);
                String minute = String.format("%02d", elapsedMinutes);
                String seconds = String.format("%02d", elapsedSeconds);
                hourTV.setText(hour);
                minTV.setText(minute);
                secTV.setText(seconds);
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();

//        CountDownView cdv = (CountDownView) findViewById(R.id.countdownview);
//        cdv.setInitialTime(30000); // Initial time of 30 seconds.
//        cdv.start();
//        cdv.stop();
//        cdv.reset();
//
//        cdv.setListener(CountDownActivity.this);
    }


    @Override
    public void timerElapsed() {
        //Do something here
        Toast.makeText(this, "timerElapsed", Toast.LENGTH_SHORT).show();
    }

}
