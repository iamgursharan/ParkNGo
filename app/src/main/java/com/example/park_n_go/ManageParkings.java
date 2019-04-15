package com.example.park_n_go;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageParkings extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static FirebaseDatabase database;
    private static DatabaseReference databaseRef;
    List names;
    List addresses;
    ListView listview;
    public static String TAG="com.example.park_n_go_manageparkings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_parkings);

        //Initializing variables
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseRef=database.getReference();
        listview=findViewById(R.id.list_parkings);
        names=new ArrayList<String>();
        addresses=new ArrayList<String>();

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                //Populating listview
                CustomListAdapter adapter=new CustomListAdapter(getApplicationContext(),getNames(retrieveData(dataSnapshot)),getAddresses(retrieveData(dataSnapshot)));
                listview.setAdapter(adapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String searchName=(String)(parent.getItemAtPosition(position));
                        String[] userData=getObject(retrieveData(dataSnapshot),searchName);
                        Intent intent=new Intent(ManageParkings.this,ViewAllParkings.class);
                        intent.putExtra(TAG,userData);
                        startActivity(intent);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //This method collects data from parking collection
    private List<Parking> retrieveData(DataSnapshot snapshot){
        List<Parking> data=new ArrayList<Parking>();
        for(DataSnapshot sp: snapshot.child("parkingcollection").getChildren()){
            Parking parking=sp.getValue(Parking.class);
            data.add(parking);
        }
        return data;

    }

    private List getNames(List<Parking> data){
        List parkingData=new ArrayList();
        for(Parking parking:data){
            parkingData.add(parking.getName());
        }
        return parkingData;
    }

    private List getAddresses(List<Parking> data){
        List parkingData=new ArrayList();
        for(Parking parking:data){
            parkingData.add(parking.getAddress());
        }
        return parkingData;
    }

    public String[] getObject(List<Parking> data,String name){
        String[] result=new String[12];
        for(Parking parking:data){
            if(parking.getName().equals(name)){
                result[0]= parking.getName();
                result[1]=parking.getAddress();
                result[2]=parking.getHostEmail();
                result[3]=parking.getHostType();
                result[4]=parking.getId();
                result[5]=parking.getLatitude();
                result[6]=parking.getLongitude();
                result[7]=parking.getNotes();
                result[8]=parking.getOccupancy();
                result[9]=parking.getOccupied();
                result[10]=parking.getPrice();
                result[11]=parking.getTime();
            }
        }
        return result;
    }
}
