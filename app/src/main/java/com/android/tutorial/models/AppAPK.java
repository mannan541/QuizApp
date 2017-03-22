package com.android.tutorial.models;

import android.graphics.drawable.Drawable;

/**
 * Created by Mannan on 3/22/2017.
 */

public class AppAPK {

    private String appLabel;
    private String appPackage;
    private Drawable appLoadIcon;

    public AppAPK() {
    }

    public AppAPK(String appLabel, String appPackage, Drawable appLoadIcon) {
        this.appLabel = appLabel;
        this.appPackage = appPackage;
        this.appLoadIcon = appLoadIcon;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public Drawable getAppLoadIcon() {
        return appLoadIcon;
    }

    public void setAppLoadIcon(Drawable appLoadIcon) {
        this.appLoadIcon = appLoadIcon;
    }

}
