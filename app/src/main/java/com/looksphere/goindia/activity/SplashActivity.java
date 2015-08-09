package com.looksphere.goindia.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.looksphere.goindia.R;
import com.looksphere.goindia.global.Constants;
import com.looksphere.goindia.utils.SharedPreferencesController;

public class SplashActivity extends AppCompatActivity {
        //variable declarations
        private TextView appName;
        private SharedPreferencesController sharedPreferencesController;
        private ImageView appLogo;
        private Animation rotateAnimation, animationFadeIn;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
        navigateToHome();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


    }


        //initialize UI
    private void initUI() {
        appName = (TextView) findViewById(R.id.app_name);
        appLogo = (ImageView) findViewById(R.id.applogo);
        sharedPreferencesController = SharedPreferencesController.getSharedPreferencesController(this);
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_logo_anim);
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.anim_fade_in);
        appLogo.startAnimation(rotateAnimation);
        appName.startAnimation(animationFadeIn);
        appName.setTypeface(Typeface.createFromAsset(getAssets(), Constants.REGULARFONT));

    }

    //Call to load home screen
    private void navigateToHome() {

        if (sharedPreferencesController.getString("APIKey").length() > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }, Constants.SPLASH_TIMER);

        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), TutorialActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }, Constants.SPLASH_TIMER);
        }
    }



}
