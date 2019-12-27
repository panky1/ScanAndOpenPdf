package com.bcil.scanandopenpdf;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.balsikandar.crashreporter.BuildConfig;
import com.balsikandar.crashreporter.CrashReporter;

public class MyApp extends Application {
    private static final String TAG = MyApp.class.getSimpleName();
    private static MyApp sInstance;
    public MyApp() {
        Log.e(TAG,"Application Started");
    }
    public static Context getInstance() {
        return sInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        if (BuildConfig.DEBUG) {
            //initialise reporter with external path
            CrashReporter.initialize(this);
        }
    }

}
