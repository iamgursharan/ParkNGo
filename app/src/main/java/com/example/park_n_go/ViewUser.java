package com.example.park_n_go;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ViewUser extends AppCompatActivity {

//    field variables
    private String[] userInfo;
    private EditText nameET,emailET,roleET,pwdET;
    private Button submitBtn,editBtn,deleteBtn;
    private FirebaseAuth mAuth;
    private static FirebaseDatabase database;
    private static DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        initializeUIComponents();

        userInfo=getIntent().getStringArrayExtra(ManageUsers.TAG);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseRef=database.getReference();

        for(int i=0;i<userInfo.length;i++){
            nameET.setText(userInfo[0]);
            emailET.setText(userInfo[1]);
            pwdET.setText(userInfo[2]);
            roleET.setText(userInfo[3]);
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
                        for(DataSnapshot snapshot:dataSnapshot.child("users").getChildren()){
                            for(DataSnapshot sp:snapshot.getChildren()){

                                if(sp.getValue().equals(emailET.getText().toString())){
                                    String key= snapshot.getKey();
                                    writeData(key);
                                    Intent intent=new Intent(ViewUser.this,ManageUsers.class);
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

                                if(sp.getValue().equals(emailET.getText().toString())){
                                    String key= snapshot.getKey();
                                    databaseRef.child("users").child(key).removeValue();
                                    Intent intent=new Intent(ViewUser.this,ManageUsers.class);
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


    public void edit(){
        nameET.setEnabled(true);
        pwdET.setEnabled(true);
        roleET.setEnabled(true);

        submitBtn.setEnabled(true);
        Drawable submitBackground= ResourcesCompat.getDrawable(getApplicationContext().getResources(),R.drawable.submit_button,null);
        submitBtn.setBackgroundDrawable(submitBackground);
    }

    public void writeData(String key){
        String setKey=key;
        String email=emailET.getText().toString();
        String name=nameET.getText().toString();
        String role=roleET.getText().toString();
        String pwd=pwdET.getText().toString();

        databaseRef.child("users").child(setKey).setValue(new User(name,role,email,pwd,mAuth.getUid())).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void initializeUIComponents(){
        nameET=findViewById(R.id.user_nameET);
        emailET=findViewById(R.id.user_email_et);
        pwdET=findViewById(R.id.user_pwd_et);
        roleET=findViewById(R.id.user_role_et);
        submitBtn=findViewById(R.id.submit_btn);
        editBtn=findViewById(R.id.edit_btn);
        deleteBtn=findViewById(R.id.delete_btn);
 }


}
