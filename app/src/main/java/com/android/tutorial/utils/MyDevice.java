package com.android.tutorial.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mannan on 10/23/2017.
 */

public class MyDevice {

    public static String getDeviceID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();

        return deviceId;
    }

    public static String getDeviceName() {
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = "";

        try {
            if (myDevice.getName() != null) {
                deviceName = myDevice.getName();
            }
        } catch (Exception e) {
            Log.d("MyDevice", "getDeviceName Exception: " + e.getMessage());
        }

        return deviceName;
    }

    public static String getDeviceEmail(Context context) {
        String possibleEmail = "";
        try {
            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            Account[] accounts = AccountManager.get(context).getAccounts();
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    possibleEmail = account.name;
                }
            }
        } catch (Exception e) {
            possibleEmail = "unknown";
        }
        return possibleEmail;
    }

    public static String getDeviceEmailName(Context context) {
        String possibleEmail = "";
        try {
            Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
            Account[] accounts = AccountManager.get(context).getAccounts();
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    possibleEmail = account.name;
                }
            }
            Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
            Matcher match = pt.matcher(possibleEmail);
            while (match.find()) {
                String s = match.group();
                possibleEmail = possibleEmail.replaceAll("\\" + s, "");
            }
        } catch (Exception e) {
            possibleEmail = "unknown";
        }
        return possibleEmail;
    }

    public static String getDeviceOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceOSName() {

        Field[] fields = Build.VERSION_CODES.class.getFields();
        String deviceOSName = fields[Build.VERSION.SDK_INT + 1].getName();

        return deviceOSName;
    }

}
