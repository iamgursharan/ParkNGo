/*
 * Author: Gursharan Singh
 * Description: This class is view parking class which shows additional parking details
 */
package com.example.park_n_go;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;


public class ViewParkingInfo extends AppCompatActivity {

    private static List<Parking> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_parking_info);

        // Retrieving views
        TextView name=findViewById(R.id.parking_name);
        TextView address=findViewById(R.id.address);
        TextView price=findViewById(R.id.price);
        TextView hostType=findViewById(R.id.hostType);
        TextView time=findViewById(R.id.time);
        TextView occupancy=findViewById(R.id.occupancy);
        TextView occupied=findViewById(R.id.occupied);


        // Reading values
        Gson gson=new Gson();
        Intent intent=getIntent();
        String data = intent.getStringExtra(MapsHomeActivity.PARK);
        Type type = new TypeToken<List<Parking>>(){}.getType();
        list=gson.fromJson(data,type);
        String latitude=intent.getStringExtra(MapsHomeActivity.LAT);
        String longitude=intent.getStringExtra(MapsHomeActivity.LONG);

/**
 * Assign values.
 */
       for(Parking p: list){
          if((p.getLatitude().equals(latitude))&&(p.getLongitude().equals(longitude))){
               name.setText(p.getName());
              address.setText(p.getAddress());
              price.setText("$"+p.getPrice());
              time.setText(p.getTime()+" min");
              hostType.setText(p.getHostType());
              occupancy.setText(p.getOccupancy());
              occupied.setText(p.getOccupied());

           }

       }
    }

}