package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailSensorMasukActivity extends AppCompatActivity {

    Intent intent;
    String cekback, field1, field2, field3, field4, field5;
    TextView debit, kecepatan, selisih, status;

    ImageView back, home;
    int nilaistat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sensor_masuk);

        back = findViewById(R.id.backsensor);
        home = findViewById(R.id.home);
        debit = findViewById(R.id.debitsensor);
        kecepatan = findViewById(R.id.kecepatanaliran);
        selisih = findViewById(R.id.selisih);
        status = findViewById(R.id.status);


        intent = getIntent();
        field1 = intent.getStringExtra("field1");
        field2 = intent.getStringExtra("field2");
        field3 = intent.getStringExtra("field3");
        field4 = intent.getStringExtra("field4");
        field5 = intent.getStringExtra("field5");

        if (field1.equals("null")||field1.isEmpty()){
            field1 = "0";
        }
        if (field2.equals("null")||field2.isEmpty()){
            field2 = "0";
        }
        if (field3.equals("null")||field3.isEmpty()){
            field3 = "0";
        }
        if (field4.equals("null")||field4.isEmpty()){
            field4 = "0";
        }
        if (field5.equals("null")||field5.isEmpty()){
            field5 = "0";
            nilaistat = 0;
        }else {
            nilaistat = Integer.parseInt(field5);
        }

        cekback = intent.getStringExtra("back");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(DetailSensorMasukActivity.this, MainActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntent);
            }
        });
        if (nilaistat<51){
            status.setText("Tidak Aman");
        }else {
            status.setText("Aman");
        }


        if (cekback.equals("sensormasuk")){
        debit.setText("Debit Sensor Masuk\n-> "+field1);
        kecepatan.setText("Kecepatan Aliran Masuk \n -> "+field3);
        selisih.setText("Selisih\nSensor Masuk - Sensor Keluar\n-> "+field5);


        }else if (cekback.equals("sensormasuk")){
            debit.setText("Debit Sensor Keluar\n -> "+field2);
            kecepatan.setText("Kecepatan Aliran Keluar \n -> "+field4);
            selisih.setText("Selisih\nSensor Masuk - Sensor Keluar\n-> "+field5);
        }

    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent(DetailSensorMasukActivity.this, ListSensorActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mIntent.putExtra("back", cekback);
        startActivity(mIntent);
        super.onBackPressed();
    }
}