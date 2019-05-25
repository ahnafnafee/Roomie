package com.example.roomieprototype.messages.Fragments;

import com.example.roomieprototype.messages.Notifications.MyResponse;
import com.example.roomieprototype.messages.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key= AIzaSyATUZ6c1niFGoBnkBqp4weni16EYLMSCBM"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
