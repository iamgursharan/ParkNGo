/*
 * Author: Gursharan Singh
 * Description: This class is the main activity class for the homescreen
 */

package com.example.park_n_go;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class MapsHomeActivity extends  FragmentActivity implements OnMapReadyCallback{

    // field variables
    private GoogleMap mMap;
    private static FirebaseDatabase database;
    private static DatabaseReference databaseRef;
    private static final int ZOOM_LEVEL = 12;
    private static final int TILT_LEVEL = 1;
    private static final int BEARING_LEVEL = 50;
    private static final int MY_LOCATION_REQUEST_CODE = 400;
    private String searchAddress;
    private static String TAG="TAG";
    private GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        EditText searchET=(EditText)findViewById(R.id.search_et);
        searchAddress=searchET.getText().toString();
   }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkLocationPermission(mMap);
        database = FirebaseDatabase.getInstance();
        databaseRef=database.getReference();
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    fillMapWithMarkers(readData(dataSnapshot)); //Calling 'fillMapWithMarkers' method by passing list from readData
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast toast = Toast.makeText(getApplicationContext(),"Server error", Toast.LENGTH_LONG);
                toast.show();
            }


        });

     // For loading customized styles for map
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.styles_map_standard));

            if (!success) {
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to load style file", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (Resources.NotFoundException e) {
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }


    }

    private void fillMapWithMarkers(List<Parking> parkings) throws IOException {
       int count=0;

       search(parkings,searchAddress);

       if(mMap==null)
            return;
        for(Parking parking:parkings)
         { count++;
             final LatLng position = new LatLng(Double.parseDouble(parking.getLatitude()),
                    Double.parseDouble(parking.getLongitude()));//Converting string latlng to LatLng class
                final String parkingName=parking.getName();


            mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map))).setTitle(parkingName);

            if (count<=parkings.size()) {
                CameraPosition camPos = new CameraPosition(position, ZOOM_LEVEL, TILT_LEVEL, BEARING_LEVEL);
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camPos));
            }
        }
    }

    private List<Parking> readData(DataSnapshot dataSnapshot)
    {
        List<Parking> parkings=new ArrayList<>();
        for(DataSnapshot snapshot: dataSnapshot.getChildren())
        {
            Parking parking=snapshot.getValue(Parking.class);
            parkings.add(parking);
        }

        return parkings;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //Location permission region starts
    public void checkLocationPermission(GoogleMap mMap) {

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
        mMap.setMyLocationEnabled(true);


        Toast toast = Toast.makeText(getApplicationContext(), "Latitude:longitude:", Toast.LENGTH_LONG);
        toast.show();
    } else {
        // Show rationale and request permission.
        Toast toast = Toast.makeText(getApplicationContext(), "Enter address in the search text box", Toast.LENGTH_LONG);
        toast.show();
        // Set search text box to focus.
    }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast toast = Toast.makeText(getApplicationContext(), "Location shared", Toast.LENGTH_LONG);
                toast.show();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Enter address in the search text box", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
    // location permission region ends

//    This method search the address entered by the user
    private void search(List<Parking> parkings,String address)
    {
        for(Parking parking: parkings){
            if(address.toLowerCase().contentEquals(parking.toString().toLowerCase()))
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Address found", Toast.LENGTH_LONG);
                toast.show();
            }

            else{
                Toast toast = Toast.makeText(getApplicationContext(), "Address not found", Toast.LENGTH_LONG);
                toast.show();
            }
        }


    }


}
