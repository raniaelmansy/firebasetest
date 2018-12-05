package com.example.rania.test.config;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class test extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);

    }
}
