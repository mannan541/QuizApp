package com.android.tutorial.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tutorial.utils.NetworkAccessInfo;
import com.android.tutorial.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class LedControllActivity extends AppCompatActivity {

    TextView led_on_off;
    Switch ledSwitch;
    String bit = "OFF";
    String SetServerString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_controll);
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

        led_on_off = (TextView) findViewById(R.id.led_on_off);
        ledSwitch = (Switch) findViewById(R.id.ledSwitch);
        ledSwitchListener();

    }

    public void ledSwitchListener() {
        ledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (NetworkAccessInfo.isNetworkAvailable(getApplicationContext())) {
                    new MyAsyncTask().execute();
                } else {
                    Toast.makeText(LedControllActivity.this, "Internet Not Connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (bit.equals("OFF")) {
                bit = "ON";
            } else {
                bit = "OFF";
            }
            http_get_request(bit);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (bit.equals("ON")) {
                led_on_off.setText("Turn LED OFF");
//                ledSwitch.setChecked(true);
            } else {
                led_on_off.setText("Turn LED ON");
//                ledSwitch.setChecked(true);
            }
            if (SetServerString.contains("Exception")) {
                Toast.makeText(LedControllActivity.this, SetServerString, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void http_get_request(String bit) {
        try {
            String URL = "http://192.168.0.103/LED=" + bit;
            HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpGet httpget = new HttpGet(URL);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                SetServerString = httpClient.execute(httpget, responseHandler);
                if (SetServerString.contains("Led pin is now:")) {
                    if (bit.equals("OFF")) {
                        bit = "ON";
                    } else {
                        bit = "OFF";
                    }
                } else {
                    SetServerString = "Something went wrong!";
                }
//                Toast.makeText(this, "SetServerString: " + SetServerString, Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                SetServerString = "Exception: " + ex;
//                Toast.makeText(this, "Exception: " + ex, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            SetServerString = "Exception: " + e;
//            Toast.makeText(this, "Exception: " + e, Toast.LENGTH_SHORT).show();
        }
    }

}
