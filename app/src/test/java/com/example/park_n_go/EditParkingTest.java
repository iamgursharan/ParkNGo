package com.example.park_n_go;

import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
class EditParkingTest extends AppCompatActivity {
    @Test
    public void checkLatitude() {
        LatLng position=EditParking.getLocationFromAddress(EditParkingTest.this,"5 Church Street, Toronto");
        double expectedLatitude=position.latitude;
        double actualLatitude=43.647680;
        assertEquals(expectedLatitude, actualLatitude);
    }

    @Test
    public void checkLongitude() {
        LatLng position=EditParking.getLocationFromAddress(EditParkingTest.this,"5 Church Street, Toronto");
        double expectedLongitude=position.longitude;
        double actualLongitude=-79.373140;
        assertEquals(expectedLongitude, actualLongitude);
    }
}