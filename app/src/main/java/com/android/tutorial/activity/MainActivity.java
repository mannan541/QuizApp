package com.android.tutorial.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tutorial.R;
import com.android.tutorial.application.MyApplication;
import com.android.tutorial.models.User;
import com.android.tutorial.utils.LocationGetter;
import com.android.tutorial.utils.NetworkAccessInfo;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.noveogroup.android.log.Log;
import com.vansuita.library.CheckNewAppVersion;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import iammert.com.library.ConnectionStatusView;
import iammert.com.library.Status;

public class MainActivity extends AppCompatActivity {

    ConnectionStatusView statusView;
    EditText edit_text;
    public TextView text_view;
    String editTextInputString = "Dummy Content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("HelloBoy", "helalksdgfjkladsj");
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
        text_view = (TextView) findViewById(R.id.ip_text);

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
        if (id == R.id.action_search) {
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

    public void memoryInfoClickListener(View view) {
        Intent intent = new Intent(MainActivity.this, MemoryInfoActivity.class);
        startActivity(intent);
    }

    public void socketClickListener(View view) {
        Intent intent = new Intent(MainActivity.this, SocketActivity.class);
        startActivity(intent);
    }

    public void paginatedListClickListener(View view) {
        editTextInputString = edit_text.getText().toString();
        MyApplication myApplication = new MyApplication();
        myApplication.setGlobalTitle(editTextInputString);
        Intent intent = new Intent(MainActivity.this, PaginatedListViewActivity.class);
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

    public void ledOnOffClickListener(View view) {
        if (NetworkAccessInfo.isNetworkAvailable(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), LedControllActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Internet Not Connected", Toast.LENGTH_SHORT).show();
        }
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

    public void retrofitClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), RetrofitSignupActivity.class);
        startActivity(intent);
    }

    public void ticTacToeClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), TicTacToeActivity.class);
        startActivity(intent);
    }

    public void businessCardClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), BusinessCardActivity.class);
        startActivity(intent);
    }

    public void smsListClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), SMSActivity.class);
        startActivity(intent);
    }

    public void imageEffectsClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), ImageFilterEffectsActivity.class);
        startActivity(intent);
    }

    public void doorSignViewClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), DoorSignViewActivity.class);
        startActivity(intent);
    }

    public void fidgetSpinnerClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), FidgetSpinnerActivity.class);
        startActivity(intent);
    }

    public void youtubeVideoDownlaoderClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), YoutubeDownloaderActivity.class);
        startActivity(intent);
    }

    public void firebaseChatClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), FireBaseChatActivity.class);
        startActivity(intent);
    }

    public void webViewYoutubeClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), YoutubeInEmbededWebviewActivity.class);
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

    public void recyclerClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), RecyclerViewActivity.class);
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

    public void fileDownloadClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), DownloadFileActivity.class);
        startActivity(intent);
    }

    public void thermometerClickListener(View view) {
        Intent intent = new Intent(getApplicationContext(), ThermometerActivity.class);
        startActivity(intent);
    }

    public void getGPSlocation(View view) {
        final LocationGetter userLocation = new LocationGetter();
        LocationGetter.LocationResult locationResult = new LocationGetter.LocationResult() {
            @Override
            public void gotLocation(Location location) {

                Toast.makeText(MainActivity.this, "Your Location: " + location, Toast.LENGTH_SHORT).show();
            }
        };

        boolean locationEnabled = userLocation.getLocation(this, locationResult);
        if (!locationEnabled) {
            Toast.makeText(this, "Enable location on your device.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
//        startActivity(intent);
    }

    public void rtpClickListener(View view) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audio.setMode(AudioManager.MODE_IN_COMMUNICATION);
            AudioGroup audioGroup = new AudioGroup();
            audioGroup.setMode(AudioGroup.MODE_NORMAL);
            AudioStream audioStream = new AudioStream(InetAddress.getByAddress(getLocalIPAddress()));
            audioStream.setCodec(AudioCodec.PCMU);
            audioStream.setMode(RtpStream.MODE_NORMAL);
            //set receiver(vlc player) machine ip address(please update with your machine ip)
            audioStream.associate(InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, (byte) 217, (byte) 2}), 22222);
            audioStream.join(audioGroup);
            text_view.setText("IP: 192.168.10.6:22222");
        } catch (Exception e) {
            text_view.setText("Exception: " + e.getMessage());
            Log.d("----------------------", e.toString());
            e.printStackTrace();
        }
    }

    public static byte[] getLocalIPAddress() {
        byte ip[] = null;
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ip = inetAddress.getAddress();
                    }
                }
            }
        } catch (SocketException ex) {
//            text_view.setText("SocketException: " + ex.getMessage());
            Log.d("SocketException ", ex.toString());
        }
        return ip;
    }

}
