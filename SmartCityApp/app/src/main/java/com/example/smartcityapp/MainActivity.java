package com.example.smartcityapp;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int FINAL_UPDATE_INTERVAL = 5;
    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    private static final int PERMISSION_FINE_LOCATION = 99;
    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_update, tv_address,tv_count;
    Switch sw_locationUpdates, sw_gps;
    Button btn_newpoint , btn_showWaylist , btn_viewList;
    List<Location> savedLocation;
    Location Currentloaction;
    boolean updateOn = false;
    LocationRequest locationRequest;

    LocationCallback locationCallBack;
    //this is the heart of the location map
    FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);

        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_update = findViewById(R.id.tv_update);
        tv_address = findViewById(R.id.tv_address);
        sw_locationUpdates = findViewById(R.id.sw_locationsupdates);
        sw_gps = findViewById(R.id.sw_gps);
        btn_newpoint = findViewById(R.id.newWaypoint);
        btn_showWaylist = findViewById(R.id.btn_showWaylist);
        tv_count = findViewById(R.id.tv_Countofcrum);
        btn_viewList = findViewById(R.id.tv_viewmap);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FINAL_UPDATE_INTERVAL);

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationCallBack = new LocationCallback(){

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                updateUIValues(location);
            }
        };

        btn_viewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });



        btn_newpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           mapSurface mapsurface = (mapSurface)getApplicationContext();
           savedLocation = mapsurface.getMylocation();
           savedLocation.add(Currentloaction);
            }
        });

        btn_showWaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this,ShowList.class);
               startActivity(intent);
            }
        });





        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sw_gps.isChecked()){
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                tv_sensor.setText("Using GPS Sensor");
                }
                else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using Tower + WIFI");
                }
            }
        });

        sw_locationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sw_locationUpdates.isChecked()){
                   startLocationUpdate();
                }else {
                    stopLocationupdate();
                }
            }


            private void stopLocationupdate() {
                tv_update.setText("STOP");
                tv_lat.setText("STOP");
                tv_lon.setText("STOP");
                tv_speed.setText("STOP");
                tv_sensor.setText("STOP");
                tv_accuracy.setText("STOP");
                tv_altitude.setText("STOP");
                tv_update.setText("Location is being tracked");
            }


            @SuppressLint("MissingPermission")
            private void startLocationUpdate() {

           fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallBack,null);
                updateGPS();
            }
        });


        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_FINE_LOCATION:
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            updateGPS();
        }
        else {
            Toast.makeText(this,"this app required to be granted permission",Toast.LENGTH_SHORT).show();
            finish();
        }
        break;
        }
    }


    private  void updateGPS(){
      fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
      if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
               updateUIValues(location);
            }
        });
      }else {
          if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
              requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_FINE_LOCATION);
          }
      }
    }

    private void updateUIValues(Location location) {
      tv_lat.setText(String.valueOf(location.getLatitude()));
      tv_lon.setText(String.valueOf(location.getLongitude()));
      if(location.hasAltitude()) {
          tv_altitude.setText(String.valueOf(location.getLatitude()));
      }else{
          tv_altitude.setText("Not available");
      }
      if(location.hasSpeed()) {
          tv_speed.setText(String.valueOf(location.getLatitude()));
      }else {
          tv_speed.setText("Not available");
      }
        Geocoder geocoder = new Geocoder(MainActivity.this);
      try {
          List<Address>addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
          tv_address.setText(addresses.get(0).getAddressLine(0));
      }
      catch (Exception e){
          tv_address.setText("Unable to get street addresh");
      }
        mapSurface mapsurface = (mapSurface)getApplicationContext();
        savedLocation = mapsurface.getMylocation();
        tv_count.setText(Integer.toString(savedLocation.size()));
    }
}


   