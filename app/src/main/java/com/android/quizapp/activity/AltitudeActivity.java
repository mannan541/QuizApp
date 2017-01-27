package com.android.quizapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.quizapp.R;
import com.android.quizapp.view.AttitudeView;

public class AltitudeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altitude);
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

        AttitudeView av = (AttitudeView) findViewById(R.id.attitudeView);
// Specify the property like layout params
        av.setPitch(20); // Update the Pitch value in degrees.
        float pitch = av.getPitch(); // Update get the actual pitch value degrees.
        av.setRoll(10); // Update the Roll value in degrees.
        float roll = av.getRoll(); // Update get the actual roll value degrees.
    }

}
