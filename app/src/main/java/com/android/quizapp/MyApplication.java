package com.android.quizapp;

import android.app.Application;

/**
 * Created by Mannan on 11/23/2016.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

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
