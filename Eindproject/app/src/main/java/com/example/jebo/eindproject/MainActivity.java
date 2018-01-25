package com.example.jebo.eindproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager FM = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // return to start fragment by replacing current fragment in fragment_containter.
                    Bundle argumentsStandard = new Bundle();
                    argumentsStandard.putString("method", "standard");

                    MainListFragment fragmentList = new MainListFragment();
                    fragmentList.setArguments(argumentsStandard);
                    FragmentTransaction ftList = FM.beginTransaction();
                    ftList.replace(R.id.fragment_container, fragmentList, "MainListFragment");
                    ftList.commit();
                    return true;

                case R.id.navigation_favorite:
                    // return to start fragment by replacing current fragment in fragment_containter.
                    Bundle argumentsFav = new Bundle();
                    argumentsFav.putString("method", "favorite");

                    MainListFragment favorites = new MainListFragment();
                    favorites.setArguments(argumentsFav);
                    FragmentTransaction ftFav = FM.beginTransaction();
                    ftFav.replace(R.id.fragment_container, favorites, "FavFragment");
                    ftFav.commit();
                    return true;

                case R.id.navigation_compare:
                    CompareFragment compare = new CompareFragment();
                    FM.beginTransaction().replace(R.id.fragment_container, compare, "FavFragment").commit();
                    return true;

                case R.id.navigation_wallet:
                    WalletFragment wallet = new WalletFragment();
                    FM.beginTransaction().replace(R.id.fragment_container, wallet, "FavFragment").commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences settings = getSharedPreferences("Settings", MODE_PRIVATE);
        final Boolean nightMode = settings.getBoolean("DayNightMode", false);
        Log.d("nightMode", nightMode.toString());

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton buttonDayNight = findViewById(R.id.buttonDayNight);

        if (nightMode) {buttonDayNight.setImageResource(R.drawable.sun);}
        else{buttonDayNight.setImageResource(R.drawable.moon);}

        buttonDayNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nightMode) {
                    settings.edit().putBoolean("DayNightMode", true).apply();
                    resetMainActivity();
                    Log.d("DayNightMode", "true");
                } else {
                    settings.edit().putBoolean("DayNightMode", false).apply();
                    resetMainActivity();
                    Log.d("DayNightMode", "false");

                }
            }
        });

        startListView();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    private void startListView(){
        // return to start fragment by replacing current fragment in fragment_containter.
        Bundle arguments = new Bundle();
        arguments.putString("method", "standard");

        FragmentManager fmList = getSupportFragmentManager();
        MainListFragment fragmentList = new MainListFragment();
        fragmentList.setArguments(arguments);
        FragmentTransaction ftList = fmList.beginTransaction();
        ftList.replace(R.id.fragment_container, fragmentList, "StartFragment");
        ftList.commit();
    }

    public void resetMainActivity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
