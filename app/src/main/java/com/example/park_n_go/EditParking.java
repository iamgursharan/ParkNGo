/*
 * Author: Gursharan Singh
 * Description: This class edit new parking locations entered by the host*/
package com.example.park_n_go;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditParking extends AppCompatActivity {
    //field variables
    private String parkingName="";
    private EditText parkingNameET,addressET,priceET,time,hostTypeET,occupancyET,occupiedET,timeET;
    private Button submitBtn,editBtn,deleteBtn;
    private FirebaseAuth mAuth;
    private static FirebaseDatabase database;
    private static DatabaseReference databaseRef;
    private List<Parking> parkingData;
    private String dataKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_parking);

        parkingName=getIntent().getStringExtra(HostDashboard.TAG);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseRef=database.getReference();

        initializeUIComponents();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                populateData(searchData(retrieveData(dataSnapshot)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Could not load data", Toast.LENGTH_LONG).show();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.child("parkingcollection").getChildren()){
                            for(DataSnapshot sp:snapshot.getChildren()){

                                if(sp.getValue().equals(mAuth.getCurrentUser().getEmail())){
                                   String key= snapshot.getKey();
//                                   Rendering into console
//                                   Log.d("key",key);
                                   writeData(key);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseRef.child("parkingcollection").child(dataKey).setValue(null);
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });


    }

    //This method collects data from host_parking collection
    private List<Parking> retrieveData(DataSnapshot snapshot){
        List<Parking> data=new ArrayList<Parking>();
        for(DataSnapshot sp: snapshot.child("parkingcollection").getChildren()){
            Parking hostParking=sp.getValue(Parking.class);
            data.add(hostParking);
        }
        return data;

    }

    private List<Parking> searchData(List<Parking> data){
       List<Parking> resultData=new ArrayList<Parking>();
        for(Parking parking:data){
           if(parking.getName().equals(parkingName)){

             resultData.add(parking);
           }
       }
       return resultData;
    }

    private void populateData(List<Parking> parkingData){
        for (Parking p:parkingData){
            parkingNameET.setText(p.getName());
            addressET.setText(p.getAddress());
            priceET.setText(p.getPrice());
            timeET.setText(p.getTime());
            hostTypeET.setText(p.getHostType());
            occupancyET.setText(p.getOccupancy());
            occupiedET.setText(p.getOccupied());
        }
    }

    public void initializeUIComponents(){
        parkingNameET=findViewById(R.id.parking_name);
        addressET=findViewById(R.id.address);
        priceET=findViewById(R.id.price);
        hostTypeET=findViewById(R.id.hostType);
        occupancyET=findViewById(R.id.occupancy);
        occupiedET=findViewById(R.id.occupied);
        submitBtn=findViewById(R.id.submit_btn);
        timeET=findViewById(R.id.time);
        editBtn=findViewById(R.id.edit_btn);
        deleteBtn=findViewById(R.id.delete_btn);
        parkingData=new ArrayList<Parking>();

    }

    public void edit(){
       parkingNameET.setEnabled(true);
        addressET.setEnabled(true);
        priceET.setEnabled(true);
        timeET.setEnabled(true);
        hostTypeET.setEnabled(true);
        occupiedET.setEnabled(true);
        occupancyET.setEnabled(true);
    }

    public void writeData(String key){
        String setKey=key;
        String hostId = mAuth.getCurrentUser().getUid();
        String hostEmail=mAuth.getCurrentUser().getEmail();
        String parkingName = generateString(parkingNameET);
        String address = generateString(addressET);
        String price = generateString(priceET);
        String hostType = generateString(hostTypeET);
        String occupancy = generateString(occupancyET);
        String occupied = generateString(occupiedET);
        String latitude=Double.toString(getLocationFromAddress(this,address).latitude);
        String longitude=Double.toString(getLocationFromAddress(this,address).longitude);
        String time=generateString(timeET);
        databaseRef.child("parkingcollection").child(setKey).setValue(new Parking(address,hostType,hostEmail,hostId,
                latitude,longitude,parkingName,"",occupancy,occupied,price,time)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Data updated", Toast.LENGTH_LONG).show();

            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error updating", Toast.LENGTH_LONG).show();
            }
        });
    }

    //    This method generates the string of an edittext - editText(EditText)
    private String generateString(EditText editText){
        return editText.getText().toString();
    }

    // This method generates latitude and longitude taking address and context as an input-context(Context),strAddress(String)
    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }


}
