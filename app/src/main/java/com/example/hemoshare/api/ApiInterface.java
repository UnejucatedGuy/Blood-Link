package com.example.hemoshare.api;

import static com.example.hemoshare.Model.Constants.CONTENT_TYPE;
import static com.example.hemoshare.Model.Constants.SERVER_KEY;

import com.example.hemoshare.Model.PushNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    @Headers({"Authorization: key="+SERVER_KEY,"Content-Type:"+CONTENT_TYPE})
    @POST("fcm/send")
    Call<PushNotification> sendNotification(@Body PushNotification notification);
}
