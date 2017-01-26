package com.android.quizapp.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.quizapp.R;
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;

public class GestureDetectorActivity extends AppCompatActivity {

    TextView gestureResult;
    ShakeDetector.ShakeListener shakeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_detector);
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

        gestureResult = (TextView) findViewById(R.id.gesture_result);

        Sensey.getInstance().init(getApplicationContext());

        ShakeDetector.ShakeListener shakeListener = new ShakeDetector.ShakeListener() {
            @Override
            public void onShakeDetected() {
                // Shake detected, do something
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                gestureResult.setText("Shaking...");
            }
        };

        Sensey.getInstance().startShakeDetection(shakeListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Sensey.getInstance().stopShakeDetection(shakeListener);

    }
}
