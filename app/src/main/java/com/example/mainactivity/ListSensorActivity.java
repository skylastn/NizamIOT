package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageView;
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
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSensorActivity extends AppCompatActivity {
    AdapterSensor mAdapter;
    Context mContext;
    RecyclerView mRecyclerView;
    InterfaceIOT mApiInterface;
    List<SemuaSensor>listSensor;
    String cekBack;
    ImageView next, home;
    Intent intent;
    TextView title;
//    ImageView back, home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sensor);

        mContext = ListSensorActivity.this;
        mRecyclerView = findViewById(R.id.recyler);
        next = findViewById(R.id.nextlist_sensor);
        home = findViewById(R.id.homelistsensor);
        title = findViewById(R.id.textsensorlist);

        intent = getIntent();
        cekBack = intent.getStringExtra("back");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ListSensorActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });

        if (cekBack.equals("sensormasuk")){
            title.setText("SENSOR MASUK");
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(ListSensorActivity.this, ListSensorActivity.class);
                    intent1.putExtra("back", "sensorkeluar");
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                }
            });
        }else if (cekBack.equals("sensorkeluar")){
            title.setText("SENSOR KELUAR");
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(ListSensorActivity.this, ListSensorActivity.class);
                    intent1.putExtra("back", "sensormasuk");
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                }
            });
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mApiInterface = ApiClient.getAPIService();
        refreshListSensor();

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

                                if (convertfield3>200){

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
                                    notificationIntent.putExtra("back", cekBack);

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

                                listSensor = new ArrayList<>();
                                SemuaSensor semuaSensor;
                                mAdapter = new AdapterSensor( mContext, listSensor, cekBack);
                                mRecyclerView.setAdapter(mAdapter);
                                for(int index=0;index<subArray2.length();index++){
                                    JSONObject mStrcreated_at=subArray2.optJSONObject(index);
                                    JSONObject mStrentry_id=subArray2.optJSONObject(index);
                                    JSONObject mstrfield1=subArray2.optJSONObject(index);
                                    JSONObject mstrfield2=subArray2.optJSONObject(index);
                                    JSONObject mStrfield3=subArray2.optJSONObject(index);
                                    JSONObject mStrfield4=subArray2.optJSONObject(index);
                                    JSONObject mStrfield5=subArray2.optJSONObject(index);
//                                    JSONObject mStrUpdated=subArray2.optJSONObject(index);
//                                    JSONObject mStrCategory=subArray2.optJSONObject(index);
//                                    JSONObject mStrMerk=subArray2.optJSONObject(index);
//                                    JSONObject mStrPrice=subArray2.optJSONObject(index);
//                                    JSONObject mStrPurchase=subArray2.optJSONObject(index);
//                                    JSONObject mStrStatus=subArray2.optJSONObject(index);
//                                    JSONObject mStrStock=subArray2.optJSONObject(index);
//                                    JSONObject mStrStockmin=subArray2.optJSONObject(index);
//                                    JSONObject mStrLokasi=subArray2.optJSONObject(index);
//                                    JSONObject mStrImage=subArray2.optJSONObject(index);

                                    // get image urls

                                    strcreated_at=mStrcreated_at.optString("created_at");
                                    strentry_id=mStrentry_id.optString("entry_id");
                                    strfield1 = mstrfield1.optString("field1");
                                    strfield2 = mstrfield2.optString("field2");
                                    strfield3 = mStrfield3.optString("field3");
                                    strfield4 = mStrfield4.optString("field4");
                                    strfield5 = mStrfield5.optString("field5");
//                                    strupdated = mStrUpdated.optString("updated_at");
//                                    strcategory = mStrCategory.optString("category_id");
//                                    strmerk = mStrMerk.optString("merk");
//                                    strprice = mStrPrice.optString("price");
//                                    strpurchase = mStrPurchase.optString("purchase_price");
//                                    strstatus = mStrStatus.optString("status");
//                                    strstock = mStrStock.optString("stock");
//                                    strstockmin = mStrStockmin.optString("stock_minim");
//                                    strlokasi = mStrLokasi.optString("lokasi");
//                                    strimage = mStrImage.optString("image");
//                                    Log.d("Cek Data Semua Grade", ":"+strcreated_at+":"+ strentry_id+":"+ strfield1+":"+
//                                            strfield2+":"+ strfield3+":"+ strfield4+":"+ strfield5);
                                    semuaSensor = new SemuaSensor(strcreated_at, strentry_id, strfield1,
                                            strfield2, strfield3, strfield4,strfield5);
                                    listSensor.add(semuaSensor);

                                }

                                mAdapter.notifyDataSetChanged();

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

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent(ListSensorActivity.this, MainActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(mIntent);

        super.onBackPressed();

    }
}