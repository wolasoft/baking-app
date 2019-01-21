package com.wolasoft.bakingapp;

import android.app.Application;

import com.wolasoft.bakingapp.di.AppComponent;
import com.wolasoft.bakingapp.di.ContextModule;
import com.wolasoft.bakingapp.di.DaggerAppComponent;

public class BakingApplication extends Application {

    private AppComponent appComponent;
    private static BakingApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .build();
    }

    public static BakingApplication app() {
        return application;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
