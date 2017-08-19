package com.android.tutorial.adapters;

import android.app.Activity;
import android.view.View;

import com.android.tutorial.firebase.chat.Message;
import com.firebase.client.Query;

/**
 * Created by Mannan on 8/19/2017.
 */

public class ChatListAdapter  extends FireBaseListAdapter<Message> {


    private  String mUsername;
    public ChatListAdapter(Query ref, Activity activity, int layout, String username) {
        super(ref, Message.class, layout, activity);
        this.mUsername = username;
    }

    @Override
    protected void populateView(View view, Message message) {
        String user = message.getUserName();


    }
}