package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sensor);

        mContext = ListSensorActivity.this;
        mRecyclerView = findViewById(R.id.recyler);

        intent = getIntent();
        cekBack = intent.getStringExtra("back");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mApiInterface = ApiClient.getAPIService();
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