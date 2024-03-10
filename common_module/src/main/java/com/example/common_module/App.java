package com.example.common_module;

import android.app.Application;
import android.util.Log;


public class App extends Application {
    private static App context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

    }

    public static App getContext() {
        return context;
    }
}
