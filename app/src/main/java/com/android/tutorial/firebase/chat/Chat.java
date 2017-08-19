package com.android.tutorial.firebase.chat;

/**
 * Created by Mannan on 8/19/2017.
 */

public class Chat {
    private String mSender;
    private String mBody;

    public Chat(String sender, String body) {
        this.mSender = sender;
        this.mBody = body;
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        mSender = sender;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }
}
