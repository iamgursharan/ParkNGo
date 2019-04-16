package com.example.park_n_go;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddParkingTest extends AppCompatActivity {
    @Test
    public void checkGenerateString() {
        EditText editText=findViewById(R.id.parking_name);
        String actualOutput=AddParking.generateString(editText);
        String expectedOutput="Parking_Name";
        assertEquals(expectedOutput,actualOutput);


    }

    @Test
    public void checkValidateFields() {
        String actualOutput=AddParking.generateKey();
        String expectedOutput="NsuPGCLJuGNCgz5lUsuS2vvYo2A21862";
        assertEquals(expectedOutput,actualOutput);
    }
}
