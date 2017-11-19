package com.asherelgar.myfinalproject;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;


public class AppManager extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}
