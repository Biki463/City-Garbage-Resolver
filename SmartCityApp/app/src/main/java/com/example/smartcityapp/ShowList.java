package com.example.smartcityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ShowList extends AppCompatActivity {
   ListView lv_savedLocation1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        lv_savedLocation1 = findViewById(R.id.lv_waypointlist);


        mapSurface mapsurface = (mapSurface)getApplicationContext();
        List<Location> savedLocation = mapsurface.getMylocation();
        List<Location> validLocations = new ArrayList<>();
        for (Location location : savedLocation) {
            if (location != null) {
                validLocations.add(location);
            }
        }

        lv_savedLocation1.setAdapter(new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1,savedLocation));
    }
}