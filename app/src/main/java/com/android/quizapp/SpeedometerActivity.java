package com.android.quizapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.anastr.speedviewlib.AwesomeSpeedometer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SpeedometerActivity extends AppCompatActivity {

    AwesomeSpeedometer awesomeSpeedometer;
    SeekBar seekBar;
    Button speedTo, realSpeedTo, stop;
    TextView textSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedometer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab();

        awesomeSpeedometer = (AwesomeSpeedometer) findViewById(R.id.awesomeSpeedometer);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        speedTo = (Button) findViewById(R.id.speedTo);
        realSpeedTo = (Button) findViewById(R.id.realSpeedTo);
        stop = (Button) findViewById(R.id.stop);
        textSpeed = (TextView) findViewById(R.id.textSpeed);

        speedTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awesomeSpeedometer.speedTo(seekBar.getProgress());
            }
        });

        realSpeedTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awesomeSpeedometer.realSpeedTo(seekBar.getProgress());
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awesomeSpeedometer.stop();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textSpeed.setText(String.format("%d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        checkInternetSpeed();
    }

    private void checkInternetSpeed() {
        new MySpeedAsync().execute("http://stackoverflow.com/questions/7554799/best-way-to-evaluate-connection-speed");
    }

    long takenTime = 0, endTime = 0, startTime = 0;

    private class MySpeedAsync extends AsyncTask<String, String, String> {
        protected String doInBackground(String... urls) {
            String response = "";

            startTime = System.currentTimeMillis();
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {

                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();


                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                    endTime = System.currentTimeMillis();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            long dataSize = result.length() / 1024;
            takenTime = endTime - startTime;
            long s = takenTime / 1000;
            double speed = dataSize / s;

            awesomeSpeedometer.realSpeedTo(Math.round(s));
            textSpeed.setText(/*String.format("%d", */speed + "kbps");
//            Toast.makeText(getApplicationContext(), "" + s + "kbps", Toast.LENGTH_SHORT).show();

        }

    }

    private void fab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
