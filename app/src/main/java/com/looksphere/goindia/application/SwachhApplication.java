package com.looksphere.goindia.application;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.util.Base64;
import android.util.Log;

import com.looksphere.goindia.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SwachhApplication extends android.app.Application {

    private static SwachhApplication context;
    public static Typeface roboticThin, roboticLight, abelRegular;

    public SwachhApplication() {
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
                .showImageOnLoading(R.color.swachh_post_bg) // resource or drawable
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

    public static SwachhApplication getApplicationObject() {
        return (SwachhApplication) context;
    }


    public static String getAppKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = SwachhApplication.getApplicationObject().getPackageManager().getPackageInfo(
                    "com.looksphere.goindia",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", keyHash);
                return keyHash;
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return null;
    }


}
