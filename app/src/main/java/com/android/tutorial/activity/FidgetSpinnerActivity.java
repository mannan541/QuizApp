package com.android.tutorial.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.tutorial.R;
import com.android.tutorial.view.FidgetSpinner;

import static com.android.tutorial.R.drawable.spinner;

public class FidgetSpinnerActivity extends AppCompatActivity {

    TextView spinnerTV;
    FidgetSpinner fidgetSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fidget_spinner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerTV = (TextView) findViewById(R.id.spinnerTV);
        fidgetSpinner = (FidgetSpinner) findViewById(R.id.fidgetspinner);
        fidgetSpinner.setImageDrawable(spinner, spinnerTV);
    }

}
