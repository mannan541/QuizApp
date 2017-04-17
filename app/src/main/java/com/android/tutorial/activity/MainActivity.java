package com.android.tutorial.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.tutorial.R;
import com.android.tutorial.models.User;
import com.android.tutorial.utils.NetworkAccessInfo;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.vansuita.library.CheckNewAppVersion;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

import iammert.com.library.ConnectionStatusView;
import iammert.com.library.Status;

public class MainActivity extends AppCompatActivity {

    ConnectionStatusView statusView;
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

        statusView = (ConnectionStatusView) findViewById(R.id.statusView);
        edit_text = (EditText) findViewById(R.id.edit_text);
        led_on_off = (Button) findViewById(R.id.led_on_off);
        ledSwitch = (Switch) findViewById(R.id.ledSwitch);
        ledSwitchListener();

        new CheckNewAppVersion(getApplicationContext()).setOnTaskCompleteListener(new CheckNewAppVersion.ITaskComplete() {
            @Override
            public void onTaskComplete(CheckNewAppVersion.Result result) {

                //Checks if there is a new version available on Google Play Store.
                result.hasNewVersion();

                //Get the new published version code of the app.
                result.getNewVersionCode();

                //Get the app current version code.
                result.getOldVersionCode();

                //Opens the Google Play Store on your app page to do the update.
//                result.openUpdateLink();
            }
        }).execute();
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
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
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
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void checkInternetClickListener(View view) {
        if (NetworkAccessInfo.isNetworkAvailable(getApplicationContext())) {
//            statusView.setStatus(Status.LOADING);
//            statusView.setStatus(Status.ERROR);
            statusView.setStatus(Status.COMPLETE);
//            statusView.setStatus(Status.IDLE); //hide status
        } else {
//            statusView.setStatus(Status.LOADING);
            statusView.setStatus(Status.ERROR);
//            statusView.setStatus(Status.COMPLETE);
//            statusView.setStatus(Status.IDLE); //hide status
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

            List<User> userList = User.find(User.class, "email=?", userEmail);
            String email = null;
            for (User user : userList) {
                user.delete();
            }

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

    public void notificationBtnClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationManager nm = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        Notification notification = builder
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("Title")
                .setTicker("Ticker Title")
                .setContentText("Notification Description Content.")
                .setDefaults(Notification.DEFAULT_SOUND)
                .getNotification();
        nm.notify(0, notification);
    }

    public void webViewClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), WebVeiwActivity.class);
        startActivity(intent);
    }

    public void webViewInAppBrowserClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), WebViewInAppBrowserActivity.class);
        startActivity(intent);
    }

    public void phoneCallClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), PhoneCallActivity.class);
        startActivity(intent);
    }

    public void sendEmailListner(View view) {
        //        String[] recipients = {recipient.getText().toString()};
        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        // prompts email clients only
        email.setType("message/rfc822");
//        email.putExtra(Intent.EXTRA_EMAIL, recipients);
        email.putExtra(Intent.EXTRA_SUBJECT, "Subject Text");
        email.putExtra(Intent.EXTRA_TEXT, "\n\nRegards, \nDIYGEEKS Team");
        try {
            // the user can choose the email client
            startActivity(Intent.createChooser(email, "Choose an email client from..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "ActivityNotFoundException: " + ex, Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSMSClickListener(View view) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("03044422122", null, "From Android Tutorial App message", null, null);
    }

    public void speedometerBtnClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), SpeedometerActivity.class);
        startActivity(intent);
    }

    public void qrCodeClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), QRCodeActivity.class);
        startActivity(intent);
    }

    public void compasClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), CompassActivity.class);
        startActivity(intent);
    }

    public void aboutClickListener(View view) {
        new LibsBuilder()
                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .withAboutIconShown(true)
                .withActivityTitle("About App")
                .withAboutVersionShown(true)
                .withAboutDescription("This is a small sample which can be set in the about my app description file.<br /><b>You can style this with html markup :D</b>")
                //start the activity
                .start(this);
    }

    public void counDownClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), CountDownActivity.class);
        startActivity(intent);
    }

    public void stopwatchClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), StopwatchActivity.class);
        startActivity(intent);
    }

    public void gestureDetectorClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), GestureDetectorActivity.class);
        startActivity(intent);
    }

    public void recorderClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), VoiceRecorderActivity.class);
        startActivity(intent);
    }

    public void altitudeClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), AltitudeActivity.class);
        startActivity(intent);
    }

    public void nfcReaderClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), NFCActivity.class);
        startActivity(intent);
    }

    public void scrollingClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), ScrollingActivity.class);
        startActivity(intent);
    }

    public void heartBeatClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), HeartBeatActivity.class);
        startActivity(intent);
    }

    public void FingerPrintClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), FingerprintActivity.class);
        startActivity(intent);
    }

    public void FirebaseClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), FireBaseNotificationActivity.class);
        startActivity(intent);
    }

    public void screenShotClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), ScreenCaptureActivity.class);
        startActivity(intent);
    }

    public void weatherClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
        startActivity(intent);
    }

    public void mqttIOTClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), MQTT_IOT_Activity.class);
        startActivity(intent);
    }

    public void loginSignupClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginSignupActivity.class);
        startActivity(intent);
    }

    public void todoClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), TodoActivity.class);
        startActivity(intent);
    }

    public void cardFormClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), CardFormActivity.class);
        startActivity(intent);
    }

    public void apkExtractClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), APKExtractActivity.class);
        startActivity(intent);
    }

    public void accelerometerClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), AccelerometerPlayActivity.class);
        startActivity(intent);
    }

    public void ocrClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), OCR_RecognitionActivity.class);
        startActivity(intent);
    }

    public void colorPickerClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), ColorPickerActivity.class);
        startActivity(intent);
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
            String URL = "http://10.50.75.217/LED=" + bit;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
//        startActivity(intent);
    }
}
