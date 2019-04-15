package com.example.park_n_go;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageUsers extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static FirebaseDatabase database;
    private static DatabaseReference databaseRef;
    List names;
    List types;
    ListView listview;
    public static String TAG="com.example.park_n_go_manageusers";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseRef=database.getReference();
        listview=findViewById(R.id.list);
        names=new ArrayList<String>();
        types=new ArrayList<String>();

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                //Populating listview
                CustomListAdapter adapter=new CustomListAdapter(getApplicationContext(),getNames(retrieveData(dataSnapshot)),getTypes(retrieveData(dataSnapshot)));
                listview.setAdapter(adapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String searchName=(String)(parent.getItemAtPosition(position));
                        String[] userData=getObject(retrieveData(dataSnapshot),searchName);
                        Intent intent=new Intent(ManageUsers.this,ViewUser.class);
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

    //This method collects data from users collection
    private List<User> retrieveData(DataSnapshot snapshot){
        List<User> data=new ArrayList<User>();
        for(DataSnapshot sp: snapshot.child("users").getChildren()){
            User user=sp.getValue(User.class);
            data.add(user);
        }
        return data;

    }

    private List getNames(List<User> data){
         List userData=new ArrayList();
        for(User user:data){
            userData.add(user.getName());
        }
      return userData;
     }

    private List getTypes(List<User> data){
        List userData=new ArrayList();
        for(User user:data){
            userData.add(user.getRole());
        }
        return userData;
    }

    public String[] getObject(List<User> data,String name){
        String[] result=new String[4];
        for(User user:data){
            if(user.getName().equals(name)){
                result[0]= user.getName();
                result[1]=user.getEmail();
                result[2]=user.getPassword();
                result[3]=user.getRole();
            }
        }
        return result;
    }


}
