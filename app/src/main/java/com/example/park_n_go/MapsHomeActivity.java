/*
 * Author: Gursharan Singh
 * Description: This class is the main activity class for the homescreen
 */

package com.example.park_n_go;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
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
import com.google.firebase.database.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;


public class MapsHomeActivity extends  AppCompatActivity implements OnMapReadyCallback,LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    // field variables
    private GoogleMap mMap;
    private static FirebaseDatabase database;
    private static DatabaseReference databaseRef;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private static final int ZOOM_LEVEL = 12;
    private static final int TILT_LEVEL = 1;
    private static final int BEARING_LEVEL = 1;
    private static final int MY_LOCATION_REQUEST_CODE = 400;
    private String searchAddress;
    private static String TAG="TAG";
    private Button searchBtn;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    private Double shareLatitude;
    private Double shareLongitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_home);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        searchBtn=(Button) findViewById(R.id.search_button);
        // Calling styleSearchButton
        styleSearchBtn();
        enableMyLocation();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);

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
        mMap.setMyLocationEnabled(true);
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
//        This functionality is currently deprecated
        /*
        final ToggleButton mapsStyleToggleBtn=(ToggleButton)findViewById(R.id.maps_style_toggle);
        mapsStyleToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // For loading customized styles for map
                    try {
                        boolean success = mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(), R.raw.styles_map_night));

                        if (!success) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Unable to load style file", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } catch (Resources.NotFoundException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                    Drawable whiteCircle= ResourcesCompat.getDrawable(getApplicationContext().getResources(),R.drawable.circle_white_with_image,null);
                    mapsStyleToggleBtn.setBackgroundDrawable(whiteCircle);
                }
                else{
                    try {
                        boolean success = mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(), R.raw.styles_map_standard));

                        if (!success) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Unable to load style file", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    } catch (Resources.NotFoundException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });*/
        // For loading customized styles for map
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getApplicationContext(), R.raw.styles_map_standard));

            if (!success) {
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to load style file", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (Resources.NotFoundException e) {
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }



    }

    private void fillMapWithMarkers(final List<Parking> parkings) throws IOException {
       int count=0;
       searchBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
                  search(parkings);
         }
      });

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

    private List<Parking> readData(@NonNull DataSnapshot dataSnapshot)
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

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    //Location permission region starts
    public void checkLocationPermission(GoogleMap mMap) {

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
        mMap.setMyLocationEnabled(true);
    } else {
        // Show rationale and request permission.
        Toast toast = Toast.makeText(getApplicationContext(), "Enter address in the search text box", Toast.LENGTH_LONG);
        toast.show();
        // Set search text box to focus.
    }


    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//
//
//        if (requestCode == MY_LOCATION_REQUEST_CODE) {
//            if (permissions.length == 1 &&
//                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast toast = Toast.makeText(getApplicationContext(), "Location shared", Toast.LENGTH_LONG);
//                toast.show();
//            } else {
//                Toast toast = Toast.makeText(getApplicationContext(), "Enter address in the search text box", Toast.LENGTH_LONG);
//                toast.show();
//            }
//        }
//    }
    // location permission region ends

//    This method search the address entered by the user
    private void search(List<Parking> parkings)
    {
        EditText searchET=findViewById(R.id.search_et);
        searchAddress=searchET.getText().toString();
        for(Parking parking: parkings){
            if(parking.name.toLowerCase().contains(searchAddress.toLowerCase()))
            {
               Log.d("Location found",parking.name);
                LatLng positionForCamera=new LatLng(Double.parseDouble(parking.getLatitude()),Double.parseDouble(parking.getLongitude()));
                CameraPosition camPos = new CameraPosition(positionForCamera, 15, TILT_LEVEL, BEARING_LEVEL);
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camPos));
                break;
            }

            else{
                Toast toast = Toast.makeText(getApplicationContext(), "Could not able to locate", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }

    // This method moves the camera based on the location found
    public void moveCamera(GoogleMap mMap,LatLng position){

//        CameraPosition camPos = new CameraPosition(position, ZOOM_LEVEL, TILT_LEVEL, BEARING_LEVEL);
//        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camPos));
    }

    //This method styles the search button
    public void styleSearchBtn(){
        EditText searchET=findViewById(R.id.search_et);
       searchET.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               Drawable whiteCircle= ResourcesCompat.getDrawable(getApplicationContext().getResources(),R.drawable.circle_white_with_image,null);
               searchBtn.setBackgroundDrawable(whiteCircle);
               searchBtn.setTextColor(getResources().getColor(R.color.black,null));
               searchBtn.setEnabled(false);
               searchBtn.setVisibility(View.INVISIBLE);
           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               Drawable blackCircle= ResourcesCompat.getDrawable(getApplicationContext().getResources(),R.drawable.circle_black,null);
               searchBtn.setBackgroundDrawable(blackCircle);
               searchBtn.setTextColor(getResources().getColor(R.color.white,null));
               searchBtn.setEnabled(true);
               searchBtn.setVisibility(View.VISIBLE);
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
    }



    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
     shareLatitude=location.getLatitude();
     shareLongitude=location.getLongitude();
     LatLng positionforCamera=new LatLng(shareLatitude,shareLongitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
