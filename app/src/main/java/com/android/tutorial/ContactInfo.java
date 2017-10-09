package com.android.tutorial;

/**
 * Created by Mannan on 10/9/2017.
 */

/**
 * A model object containing contact data.
 */
public class ContactInfo {

    private String mDisplayName;
    private String mPhoneNumber;

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }
    
}
