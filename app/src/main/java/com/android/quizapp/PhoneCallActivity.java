package com.android.quizapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PhoneCallActivity extends AppCompatActivity {

    EditText editText;
    Button callBtn;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);
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

        editText = (EditText) findViewById(R.id.number_et);
        callBtn = (Button) findViewById(R.id.callBtn);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialCall(editText.getText().toString());
            }
        });
    }

    private void dialCall(String phNumber) {
        Intent intent = new Intent("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + phNumber));
        this.startActivity(intent);
        this.finish();
    }

    public void phoneCall(String phoneNum) {
        PhoneCallActivity.PhoneCallListener phoneListener = new PhoneCallActivity.PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService("phone");
        telephonyManager.listen(phoneListener, 32);
        Intent make_call_intent = new Intent("android.intent.action.CALL");
        make_call_intent.setData(Uri.parse("tel:" + phoneNum));

        try {
            this.startActivity(make_call_intent);
            this.finish();
        } catch (Exception var6) {
            Toast.makeText(this, "Allow permissions for call in Settings", Toast.LENGTH_SHORT).show();
        }

    }

    private class PhoneCallListener extends PhoneStateListener {
        private boolean isPhoneCalling;
        String LOG_TAG;

        private PhoneCallListener() {
            this.isPhoneCalling = false;
            this.LOG_TAG = "LOGGING 123";
        }

        public void onCallStateChanged(int state, String incomingNumber) {
            if (1 == state) {
                Log.i(this.LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (2 == state) {
                Log.i(this.LOG_TAG, "OFFHOOK");
                this.isPhoneCalling = true;
            }

            if (0 == state) {
                Log.i(this.LOG_TAG, "IDLE");
                if (this.isPhoneCalling) {
                    Log.i(this.LOG_TAG, "restart app");
                    Intent i = PhoneCallActivity.this.getBaseContext().getPackageManager().getLaunchIntentForPackage(PhoneCallActivity.this.getBaseContext().getPackageName());
                    i.addFlags(67108864);
                    this.isPhoneCalling = false;
                }
            }

        }
    }

}
