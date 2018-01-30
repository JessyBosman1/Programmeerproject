package com.example.jebo.eindproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jessy Bosman
 * Minor Programmeren UvA
 * Programmeer Project
 */

public class SplashScreen extends AppCompatActivity {
    // duration of splash screen
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    // called when the activity is first created
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_splash_screen);

        // New Handler to start Main Menu and close Splash Screen*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // intent to main menu
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}