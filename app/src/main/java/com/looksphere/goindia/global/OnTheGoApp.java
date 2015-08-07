package com.looksphere.goindia.global;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.looksphere.goindia.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

/**
 * Created by SPARK on 09/06/15.
 */
public class OnTheGoApp extends Application {

    private static OnTheGoApp context;
    public static Typeface roboticThin, roboticLight, abelRegular;

    public OnTheGoApp() {
        context = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //initGlobalModelStore();
        setupImageDownloader();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "cIHxT0e0QYq8ryvmP45zjlUsQIVnSoKr9eDRZ55F", "m9neclWS6n7RGl8fLtZYKQWmWe4o9yFIXCExECDe");

        // Specify an Activity to handle all pushes by default.
        //PushService.setDefaultPushCallback(this, SplashActivity.class);


        roboticThin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        roboticLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        abelRegular = Typeface.createFromAsset(getAssets(), "fonts/Abel-Regular.ttf");

        ParsePush.subscribeInBackground("swachh_broadcast");

        // Associate the device with a user
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        //installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();
    }

    public void setupImageDownloader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.download) // resource or drawable
                .showImageForEmptyUri(R.drawable.download) // resource or drawable
                .showImageOnFail(R.drawable.error) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .cacheInMemory(true) // default
                .cacheOnDisk(true)
                .build();

        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .defaultDisplayImageOptions(defaultOptions)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(20 * 1024 * 1024) // 20 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        //.writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }

    public void initGlobalModelStore() {
        //initiate controllers
    }

    public static Context getContext() {
        return context;
    }

    public static OnTheGoApp getApplicationObject() {
        return (OnTheGoApp) context;
    }





}





