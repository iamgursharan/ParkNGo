package com.example.park_n_go;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ViewAllParkings extends AppCompatActivity {

    //    field variables
    private String[] parkingInfo;
    private EditText parkingnameET,addressET,priceET,timeET,occupancyET,occupiedET,hostTypeET;
    private Button submitBtn,editBtn,deleteBtn;
    private FirebaseAuth mAuth;
    private static FirebaseDatabase database;
    private static DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_parkings);

        initializeUIComponents();

        parkingInfo=getIntent().getStringArrayExtra(ManageParkings.TAG);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseRef=database.getReference();

        for(int i=0;i<parkingInfo.length;i++){
            parkingnameET.setText(parkingInfo[0]);
            addressET.setText(parkingInfo[1]);
            hostTypeET.setText(parkingInfo[3]);
            occupancyET.setText(parkingInfo[8]);
            occupiedET.setText(parkingInfo[9]);
            priceET.setText(parkingInfo[10]);
            timeET.setText(parkingInfo[11]);
        }

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
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

                                if(sp.getValue().equals(parkingnameET.getText().toString())){
                                    String key= snapshot.getKey();
                                    writeData(key);
                                    Intent intent=new Intent(ViewAllParkings.this,ManageUsers.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    } @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.child("users").getChildren()){
                            for(DataSnapshot sp:snapshot.getChildren()){

                                if(sp.getValue().equals(parkingnameET.getText().toString())){
                                    String key= snapshot.getKey();
                                    databaseRef.child("parkingcollection").child(key).removeValue();
                                    Intent intent=new Intent(ViewAllParkings.this,ManageUsers.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }@Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    public void writeData(String key){
        String setKey=key;
        String name=parkingnameET.getText().toString();
        String address=addressET.getText().toString();
        String hostEmail=parkingInfo[2];
        String id=parkingInfo[4];
        String latitude=parkingInfo[5];
        String longitude=parkingInfo[6];
        String notes=parkingInfo[7];
        String price=priceET.getText().toString();
        String time=timeET.getText().toString();
        String hostType=hostTypeET.getText().toString();
        String occupancy=occupancyET.getText().toString();
        String occupied=occupiedET.getText().toString();


        databaseRef.child("parkingcollection").child(setKey).setValue(new Parking(address,hostType,hostEmail,id,latitude,longitude,name,notes,occupancy,occupied,price,time)).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void edit(){
        priceET.setEnabled(true);
        timeET.setEnabled(true);
        hostTypeET.setEnabled(true);
        occupancyET.setEnabled(true);
        occupiedET.setEnabled(true);
        submitBtn.setEnabled(true);
        Drawable submitBackground= ResourcesCompat.getDrawable(getApplicationContext().getResources(),R.drawable.submit_button,null);
        submitBtn.setBackgroundDrawable(submitBackground);
    }

    public void initializeUIComponents(){
        parkingnameET=findViewById(R.id.parking_name_et);
        addressET=findViewById(R.id.address_et);
        priceET=findViewById(R.id.price_et);
        timeET=findViewById(R.id.time_et);
        hostTypeET=findViewById(R.id.hostType_et);
        occupancyET=findViewById(R.id.occupancy_et);
        occupiedET=findViewById(R.id.occupied_et);
        submitBtn=findViewById(R.id.submit_btn);
        editBtn=findViewById(R.id.edit_btn);
        deleteBtn=findViewById(R.id.delete_btn);
    }
}
