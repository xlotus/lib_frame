package com.xlotus.lib.frame.demo;

import android.app.Application;

import com.xlotus.lib.core.lang.ObjectStore;

public class FrameDemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ObjectStore.setContext(this);
    }

}
