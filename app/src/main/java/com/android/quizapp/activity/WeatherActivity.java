package com.android.quizapp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.quizapp.R;
import com.android.quizapp.utils.GetWeatherUtil;

public class WeatherActivity extends AppCompatActivity {

    TextView resultTextView;
    EditText zipCodeEditText;
    Button submitBtn;
    GetWeatherUtil weatherUtil;

    String POSTAL_CODE = "94117";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab();

        resultTextView = (TextView) findViewById(R.id.weatherResultTextView);
        zipCodeEditText = (EditText) findViewById(R.id.weatherZipCodeEditText);
        submitBtn = (Button) findViewById(R.id.weatherSubmitBtn);
        weatherUtil = new GetWeatherUtil();

        zipCodeEditText.setText(POSTAL_CODE);
        
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!zipCodeEditText.getText().toString().equals(""))
                    POSTAL_CODE = zipCodeEditText.getText().toString();
                new letsGetWeather().execute();
            }
        });

    }

    // //////////////////////////////
    // AsyncTask - Load Weather data
    // //////////////////////////////
    private class letsGetWeather extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {

            String weatherData = weatherUtil.getWeatherData(POSTAL_CODE);

            return weatherData;
        }

        protected void onPostExecute(String weatherData) {

            String temperature = weatherUtil.getTemperature(weatherData);
            String conditions = weatherUtil.getConditions(weatherData);
            String windDirection = weatherUtil.getWindDirection(weatherData);
            String windSpeed = weatherUtil.getWindSpeed(weatherData);

            resultTextView.setText("Temperature: " + temperature + "\n" + "Conditions: " + conditions +
                    "\n" + "WindDirection: " + windDirection + "\n" + "WindSpeed: " + windSpeed);
        }
    }

    private void fab() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
