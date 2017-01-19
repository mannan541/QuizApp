package com.android.quizapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.quizapp.utils.PrefManager;
import com.android.quizapp.R;

public class SplashActivity extends AppCompatActivity {

    CountDownTimer timer;
    int TIME = 1000 * 2;
    int INTERVAL = 1000;
    TextView textView;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefManager = new PrefManager(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        textView = (TextView) findViewById(R.id.timeTextView);
        timer = new CountDownTimer(TIME, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText("" + (millisUntilFinished / 1000 - 1));
            }

            @Override
            public void onFinish() {
                if (!prefManager.isFirstTimeLaunch()) {
                    launchHomeScreen();
                    finish();
                } else {
                    launchWelcomeScreen();
                }
            }
        };
        timer.start();
    }

    private void launchWelcomeScreen() {
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

}
