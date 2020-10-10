package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainactivity.Adapter.AdapterSensor;
import com.example.mainactivity.Model.SemuaSensor;
import com.example.mainactivity.Network.ApiClient;
import com.example.mainactivity.Network.Endpoint.InterfaceIOT;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView sensormsk, sensorklr;
    Context mContext;
    InterfaceIOT mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensormsk = findViewById(R.id.sensor_masuk);
        sensorklr = findViewById(R.id.sensor_keluar);
        mContext = MainActivity.this;
        mApiInterface = ApiClient.getAPIService();

        sensormsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListSensorActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("back", "sensormasuk");

                startActivity(intent);
            }
        });
        
        sensorklr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListSensorActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("back", "sensorkeluar");

                startActivity(intent);
            }
        });

        refreshListSensor();

    }

    private void refreshListSensor() {
        mApiInterface.getItem()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: BERHASIL");
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
//                                JSONArray subArray1 = jsonRESULTS.getJSONArray("channel");
                                JSONArray subArray2 = jsonRESULTS.getJSONArray("feeds");
                                String strcreated_at="", strentry_id="", strfield1="",strfield2="",strfield3="",strfield4=""
                                        ,strfield5="";
//                                String strupdated="", strcategory="",strmerk="", strprice="",
//                                        strpurchase="", strstatus="", strstock="", strstockmin="", strlokasi="",
//                                        strimage="";
                                JSONObject lastfield3 = subArray2.optJSONObject(subArray2.length()-1);;
                                double convertfield3 = lastfield3.optDouble("field3");

                                if (convertfield3<200){

                                    String strcreated_at2="", strentry_id2="", strfield12="",strfield22="",strfield32="";
                                    strcreated_at2=lastfield3.optString("created_at");
                                    strentry_id2=lastfield3.optString("entry_id");
                                    strfield12 = lastfield3.optString("field1");
                                    strfield22 = lastfield3.optString("field2");
                                    strfield32 = lastfield3.optString("field3");

                                    NotificationManager mNotificationManager;
                                    NotificationCompat.Builder mBuilder;
                                    String NOTIFICATION_CHANNEL_ID = "10001";
                                    mBuilder = new NotificationCompat.Builder(mContext);
                                    mBuilder.setSmallIcon(getNotificationIcon(mBuilder));

                                    mBuilder.setContentText("Terjadi Kebocoran > 200 mL/s")
                                            .setContentTitle("BERITA KEBOCORAN")
                                            .setAutoCancel(false)
                                            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);


                                    Intent notificationIntent = new Intent(mContext, DetailSensorMasukActivity.class);
                                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    notificationIntent.putExtra("created_at", strcreated_at2);
                                    notificationIntent.putExtra("entry_id", strentry_id2);
                                    notificationIntent.putExtra("field1", strfield12);
                                    notificationIntent.putExtra("field2", strfield22);
                                    notificationIntent.putExtra("field3", strfield32);
                                    notificationIntent.putExtra("back", "sensormasuk");

                                    PendingIntent conPendingIntent = PendingIntent.getActivity(mContext,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                                    mBuilder.setContentIntent(conPendingIntent);
                                    mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        int importance = NotificationManager.IMPORTANCE_HIGH;
                                        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Chat Kelas", importance);
                                        notificationChannel.enableLights(true);
                                        notificationChannel.setLightColor(Color.RED);
                                        notificationChannel.enableVibration(true);
                                        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                                        assert mNotificationManager != null;
                                        mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                                        mNotificationManager.createNotificationChannel(notificationChannel);
                                    }
                                    assert mNotificationManager != null;
                                    mNotificationManager.notify(0 /* Request Code */, mBuilder.build());

                                }

//                                Toast.makeText(getApplicationContext(), "Mohon Maaf Username dan Password Tidak Cocok", Toast.LENGTH_SHORT).show();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.i("debug", "onResponse: GA BERHASIL");
                            Toast.makeText(getApplicationContext(), "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("result_", t.toString());
                        Toast.makeText(mContext, "Mohon Maaf koneksi anda sedang bermasalah ", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private int getNotificationIcon(NotificationCompat.Builder
                                            notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x008000;
            notificationBuilder.setColor(color);
            return R.drawable.ic_notif;

        }
        return R.drawable.ic_notif;
    }
}