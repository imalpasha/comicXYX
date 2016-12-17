package com.app.comic.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;

//import com.crashlytics.android.Crashlytics;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;

import com.app.comic.Modules;
import com.app.comic.api.ApiRequestHandler;
import com.app.comic.api.ApiService;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import dagger.ObjectGraph;
//import io.fabric.sdk.android.Fabric;

//import me.firefly.rhymecity.Modules;

public class MainApplication extends MultiDexApplication {

    private ObjectGraph objectGraph;
    private static Activity instance;

    @Inject Bus bus;
    @Inject ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();

        //Crashlytics crashlytics = new Crashlytics.Builder().disabled(BuildConfig.DEBUG).build();
        //Fabric.with(this, crashlytics, new Crashlytics());
        //Fabric.with(this, new Crashlytics());

        //FacebookSdk.sdkInitialize(this);
        //AppEventsLogger.activateApp(this);

        buildObjectGraphAndInject();
        createApiRequestHandler();

    }

    private void buildObjectGraphAndInject() {
        objectGraph = ObjectGraph.create(Modules.list("ASJ3wq8YnBmshFGszZZFHEntCFOUp1xhB2Sjsn4QZMpC3KV6kk"));
        objectGraph.inject(this);
        //getString(R.string.api_key)
    }

    private void createApiRequestHandler() {
        bus.register(new ApiRequestHandler(bus, apiService));
    }

    public ObjectGraph createScopedGraph(Object module) {
        return objectGraph.plus(module);
    }

    public static MainApplication get(Context context) {
        return (MainApplication) context.getApplicationContext();
    }

    public static Activity getContext() {
        return instance;
        //return instance;

    }

    private Intent getLauncherIntent(Context context){
        return context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
    }


}
