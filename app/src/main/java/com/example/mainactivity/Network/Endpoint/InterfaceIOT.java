package com.example.mainactivity.Network.Endpoint;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface InterfaceIOT {

    @GET("feeds.json")
    Call<ResponseBody> getItem();

}
