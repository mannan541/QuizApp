package com.android.tutorial.network;

import com.android.tutorial.models.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Mannan on 5/31/2017.
 */

public interface ApiInterface {

    @GET("inbox.json")
    Call<List<Message>> getInbox();

}
