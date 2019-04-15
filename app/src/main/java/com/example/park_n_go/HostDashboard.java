package com.example.park_n_go;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HostDashboard extends AppCompatActivity {

    // private variables
    private FirebaseAuth mAuth;
    private static FirebaseDatabase database;
    private static DatabaseReference databaseRef;
    private List<Parking> hostData;
    private List parkingNames;
    private ListView listView;
    private Button addBtn;
    private TextView message;
    public final static String TAG="com.example.park_n_go_host_dashboard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_dashboard);

        // Calling InstantiateUIComponents
        instantiateUIComponents();
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getParkingNames(retrieveData(dataSnapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Populating listview
        ArrayAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,parkingNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String parkingName=(String)parent.getItemAtPosition(position);
                Intent intent=new Intent(HostDashboard.this,EditParking.class);
                intent.putExtra(TAG,parkingName);
                startActivity(intent);
            }
        });

        //Calling updateMessage if there is no data
  //      updateMessage();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
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

    private void getParkingNames(@NonNull List<Parking> data){

        String hostEmail=mAuth.getCurrentUser().getEmail();

        for(Parking p:data){
           if(p.getHostEmail().equals(hostEmail)){
Log.d("Name",p.getName());
               parkingNames.add(p.getName());
           }

        }
     }





//    // This method updates the message
//    private void updateMessage(){
//        if(parkingNames.isEmpty()){
//           message.setVisibility(View.VISIBLE);
//           listView.setVisibility(View.INVISIBLE);
//        }
//        else{
//            message.setVisibility(View.INVISIBLE);
//        }
//
//    }

    // Instantiating all the widgets
    private void instantiateUIComponents(){
        //Instantiating private variables
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseRef=database.getReference();
        hostData=new ArrayList<Parking>();
        parkingNames=new ArrayList();
        listView=findViewById(R.id.parkings_list);
        addBtn=findViewById(R.id.add_btn);
        message=findViewById(R.id.update_messgaeTV);
    }

    // Starts another activity
    private void add(){
        Intent intent=new Intent(HostDashboard.this,AddParking.class);
        startActivity(intent);
    }



}
