package com.android.tutorial.application;

import android.support.multidex.MultiDexApplication;

import com.firebase.client.Firebase;
import com.orm.SugarContext;
import com.splunk.mint.Mint;

/**
 * Created by Mannan on 11/23/2016.
 */

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);

        Mint.initAndStartSession(getApplicationContext(), "73b50873");

        Firebase.setAndroidContext(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    public static String globalTitle = "Im Global!";

    public MyApplication() {
    }

    public MyApplication(String globalTitle) {
        this.globalTitle = globalTitle;
    }

    public static String getGlobalTitle() {
        return globalTitle;
    }

    public void setGlobalTitle(String globalTitle) {
        this.globalTitle = globalTitle;
    }

}
