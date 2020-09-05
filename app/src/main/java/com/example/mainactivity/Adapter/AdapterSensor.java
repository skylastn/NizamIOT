package com.example.mainactivity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mainactivity.DetailSensorMasukActivity;
import com.example.mainactivity.ListSensorActivity;
import com.example.mainactivity.MainActivity;
import com.example.mainactivity.Model.SemuaSensor;
import com.example.mainactivity.R;

import java.util.List;

public class AdapterSensor extends RecyclerView.Adapter<AdapterSensor.MyViewHolder> {
    List<SemuaSensor> mArtikellist ;
    Context mContext2;
    String cekBack;

    public AdapterSensor(Context context, List<SemuaSensor> dosenList, String cekBack ){
        this.mContext2 = context;
        mArtikellist = dosenList;
        this.cekBack = cekBack;
    }

    @Override
    public AdapterSensor.MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sensor, parent, false);
        mContext2 = parent.getContext();
        return new AdapterSensor.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder (final AdapterSensor.MyViewHolder holder, final int position){

        final SemuaSensor item = mArtikellist.get(position);
        int mposisi = position+1;
        holder.field.setText("Posisi Field Ke : "+item.getEntry_id());
        holder.tgl.setText("Tgl : "+item.getCreated_at());
        holder.btn_Open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext2, DetailSensorMasukActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mIntent.putExtra("created_at", item.getCreated_at());
                mIntent.putExtra("entry_id", item.getEntry_id());
                mIntent.putExtra("field1", item.getField1());
                mIntent.putExtra("field2", item.getField2());
                mIntent.putExtra("field3", item.getField3());
                mIntent.putExtra("field4", item.getField4());
                mIntent.putExtra("field5", item.getField5());
                mIntent.putExtra("back", cekBack);

                view.getContext().startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount () {
        return mArtikellist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView field, tgl;
        public Button btn_Open;
        public Context context ;

        public MyViewHolder(View itemView) {
            super(itemView);
            field = itemView.findViewById(R.id.tgl_sensor);
            tgl = itemView.findViewById(R.id.posisifield);
            btn_Open = itemView.findViewById(R.id.btn_buka_sensor);

        }
    }
}