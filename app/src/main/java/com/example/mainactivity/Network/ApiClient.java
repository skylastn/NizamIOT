package com.example.mainactivity.Network;

import com.example.mainactivity.Network.Endpoint.InterfaceIOT;

public class ApiClient {

    public static final String BASE_URL = "https://api.thingspeak.com/channels/1114597/";
    public static InterfaceIOT getAPIService(){
        return RetrofitClient.getClient(BASE_URL).create(InterfaceIOT.class);
    }

}
