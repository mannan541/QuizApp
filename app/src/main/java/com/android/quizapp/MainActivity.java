package com.android.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainActivity extends AppCompatActivity {

    EditText edit_text;
    String editTextInputString = "Dummy Content";
    Button led_on_off;
    Switch ledSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignatureActivity.class);
//                startActivity(intent);

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        edit_text = (EditText) findViewById(R.id.edit_text);
        led_on_off = (Button) findViewById(R.id.led_on_off);
        ledSwitch = (Switch) findViewById(R.id.ledSwitch);
        ledSwitchListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void signatureClickListener(View view) {
        editTextInputString = edit_text.getText().toString();
        MyApplication myApplication = new MyApplication();
        myApplication.setGlobalTitle(editTextInputString);
        Intent intent = new Intent(MainActivity.this, SignatureActivity.class);
        intent.putExtra("title", editTextInputString);
        startActivity(intent);
    }

    public void calculatorClickListener(View view) {
        editTextInputString = edit_text.getText().toString();
        MyApplication myApplication = new MyApplication();
        myApplication.setGlobalTitle(editTextInputString);
        Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
        intent.putExtra("title", editTextInputString);
        startActivity(intent);
    }

    public void listViewClickListener(View view) {
        editTextInputString = edit_text.getText().toString();
        MyApplication myApplication = new MyApplication();
        myApplication.setGlobalTitle(editTextInputString);
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        intent.putExtra("title", editTextInputString);
        startActivity(intent);
    }

    public void gridViewClickListener(View view) {
        editTextInputString = edit_text.getText().toString();
        MyApplication myApplication = new MyApplication();
        myApplication.setGlobalTitle(editTextInputString);
        Intent intent = new Intent(MainActivity.this, GridActivity.class);
        intent.putExtra("title", editTextInputString);
        startActivity(intent);
    }

    public void loginClickListener(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        boolean isLoggedIn = pref.getBoolean("IsLoggedIn", false);
        String userEmail = pref.getString("email", "");
        if (isLoggedIn) {
            Toast.makeText(this, "Already Logged in as : " + userEmail + ".\nLog out First.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void checkInternetClickListener(View view) {
        if (NetworkAccessInfo.isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(this, "Internet Connected", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Internet Not Connected", Toast.LENGTH_SHORT).show();
        }
    }

    public void drawerClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
        startActivity(intent);
    }

    public void logoutClickListener(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        boolean isLoggedIn = pref.getBoolean("IsLoggedIn", false);
        String userEmail = pref.getString("email", "");
        if (isLoggedIn) {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            showAlertDialog(userEmail);
//            Toast.makeText(this, "User " + userEmail + " Logout Successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Already logged out.", Toast.LENGTH_SHORT).show();
        }

    }

    public void showAlertDialog(String userEmail) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Logout");
        alertDialog.setMessage(userEmail + " Logged out successfully!");
        alertDialog.show();
    }

    String bit = "OFF";
    String SetServerString = "";

    public void ledOnOFFClickListener(View view) {
        if (NetworkAccessInfo.isNetworkAvailable(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), LedControllActivity.class);
            startActivity(intent);
//            new MyAsyncTask().execute();
        } else {
            Toast.makeText(this, "Internet Not Connected", Toast.LENGTH_SHORT).show();
        }
    }

    public void ledSwitchListener() {
        ledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (NetworkAccessInfo.isNetworkAvailable(getApplicationContext())) {
                    new MyAsyncTask().execute();
                } else {
                    Toast.makeText(MainActivity.this, "Internet Not Connected", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, SetServerString, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void http_get_request(String bit) {
        try {
            String URL = "http://192.168.0.101/LED=" + bit;
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
