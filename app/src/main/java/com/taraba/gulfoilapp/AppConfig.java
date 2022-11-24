package com.taraba.gulfoilapp;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

/**
 * Created by android on 12/18/15.
 */
public class AppConfig extends Application {


    public AppConfig() {

    }

    private static AppConfig singleton;
    public static boolean goToMilestone = false;
    public static boolean isAppSurveyDialogOpen = false;
    public static boolean isAppSurveySessionActive = false;
    public static AppConfig getInstance() {
        return singleton;
    }

    public static boolean isSplashPopUpSessionActive = false;

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        initializeCalligraphy();

        // Initialize the Parse SDK.
        // Parse.initialize(this, "MhjrFCvysMeomsTKjAAluNZPaY3IrnU2YUXfXq9V", "VpufNNmoWefi6fpZLx1d8JpGKxrSNSTkidl8o25F");
        // Parse.initialize(this, "LnKlAdbGj06DCPTXHIlXOVvPmx93BYLY5Nyu655r", "eSYeBq8b3IgZkeavl5AWzOM5GlCHFN7CUFnEZQjv");

        // Specify an Activity to handle all pushes by default.
        //  PushService.setDefaultPushCallback(this, NotificationActivity.class, R.drawable.logo);

        //    ParseInstallation.getCurrentInstallation().saveInBackground();

    }

    //Added by Pravin Dharam on 03042021 to support Android Q
    private void initializeCalligraphy() {
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}