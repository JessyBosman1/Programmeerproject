package com.example.jebo.eindproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Jessy Bosman
 * Minor Programmeren UvA
 * Programmeer Project
 */

public class MainActivity extends AppCompatActivity {
    // create bottom navigation listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager FM = getSupportFragmentManager();
            // manage onclick of different navigation buttons.
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // set method to Standard in Bundle so that the standard list is generated.
                    Bundle argumentsStandard = new Bundle();
                    argumentsStandard.putString("method", "standard");

                    // go to start fragment by replacing current fragment in fragment_containter.
                    MainListFragment fragmentList = new MainListFragment();
                    fragmentList.setArguments(argumentsStandard);
                    FM.beginTransaction().replace(R.id.fragment_container, fragmentList, "MainListFragment").commit();
                    return true;

                case R.id.navigation_favorite:
                    // set method to Favorite in Bundle so that the favorite list is generated.
                    Bundle argumentsFav = new Bundle();
                    argumentsFav.putString("method", "favorite");

                    // go to favorite fragment by replacing current fragment in fragment_containter.
                    MainListFragment favorites = new MainListFragment();
                    favorites.setArguments(argumentsFav);
                    FM.beginTransaction().replace(R.id.fragment_container, favorites, "FavFragment").commit();
                    return true;

                case R.id.navigation_compare:
                    // go to compare fragment by replacing current fragment in fragment_containter.
                    CompareFragment compare = new CompareFragment();
                    FM.beginTransaction().replace(R.id.fragment_container, compare, "compareFragment").commit();
                    return true;

                case R.id.navigation_wallet:
                    // go to wallet fragment by replacing current fragment in fragment_containter.
                    WalletFragment wallet = new WalletFragment();
                    FM.beginTransaction().replace(R.id.fragment_container, wallet, "walletFragment").commit();
                    return true;
            }
            return false;
        }
    };

    // set reselect listener on navigation
    private BottomNavigationView.OnNavigationItemReselectedListener onNavigationItemReselectedListener
            = new BottomNavigationView.OnNavigationItemReselectedListener() {

        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
            // If fragment is already active, do not recreate fragment but stay idle.
            Log.d("navigation", "reselected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // get UI preferences of user (Dark or Light color scheme)
        final SharedPreferences settings = getSharedPreferences("Settings", MODE_PRIVATE);
        final Boolean nightMode = settings.getBoolean("DayNightMode", false);
        Log.d("nightMode", nightMode.toString());

        // set color scheme according to preference
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // day night button reference
        ImageButton buttonDayNight = findViewById(R.id.buttonDayNight);

        // set corresponding icon of mode
        if (nightMode) {
            buttonDayNight.setImageResource(R.drawable.sun);
        } else {
            buttonDayNight.setImageResource(R.drawable.moon);
        }

        // create toggle for day and mode
        buttonDayNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nightMode) {
                    settings.edit().putBoolean("DayNightMode", true).apply();
                    // change color scheme to Dark
                    resetMainActivity(v);
                    Log.d("DayNightMode", "true");
                } else {
                    settings.edit().putBoolean("DayNightMode", false).apply();
                    // change color scheme to Light
                    resetMainActivity(v);
                    Log.d("DayNightMode", "false");

                }
            }
        });

        // load main list fragment
        startListView();

        // enable bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnNavigationItemReselectedListener(onNavigationItemReselectedListener);

    }

    /* place start screen in container */
    private void startListView() {
        // set method to standard in Bundle so that the standard list is generated.
        Bundle arguments = new Bundle();
        arguments.putString("method", "standard");

        // create start fragment in fragment_containter
        FragmentManager fmList = getSupportFragmentManager();
        MainListFragment fragmentList = new MainListFragment();
        fragmentList.setArguments(arguments);
        fmList.beginTransaction().replace(R.id.fragment_container, fragmentList, "StartFragment").commit();
    }

    /* recreate first fragment with fade to fade day night transition */
    public void resetMainActivity(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        // recreate menu with custom fade in & fade out animation
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
