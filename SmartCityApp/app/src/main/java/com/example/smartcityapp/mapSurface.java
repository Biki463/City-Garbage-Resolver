package com.example.smartcityapp;

import android.location.Location;
import android.os.Bundle;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class mapSurface extends Application {
    private static mapSurface singleton;


    public List<Location> getMylocation() {
        return mylocation;
    }

    public void setMylocation(List<Location> mylocation) {
        this.mylocation = mylocation;
    }

    private List<Location>mylocation;
    public mapSurface getInstance(){
        return singleton;


    }
    public  void onCreate(){
        super.onCreate();
        singleton = this;
        mylocation = new ArrayList<>();
    }
}
