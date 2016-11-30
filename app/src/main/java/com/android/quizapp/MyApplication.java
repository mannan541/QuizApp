package com.android.quizapp;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by Mannan on 11/23/2016.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);

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
