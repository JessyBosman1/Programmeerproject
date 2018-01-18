package com.example.jebo.eindproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // return to start fragment by replacing current fragment in fragment_containter.
                    FragmentManager fmList = getSupportFragmentManager();

                    Bundle argumentsStandard = new Bundle();
                    argumentsStandard.putString("method", "standard");

                    MainListFragment fragmentList = new MainListFragment();
                    fragmentList.setArguments(argumentsStandard);
                    FragmentTransaction ftList = fmList.beginTransaction();
                    ftList.replace(R.id.fragment_container, fragmentList, "MainListFragment");
                    ftList.commit();
                    return true;

                case R.id.navigation_favorite:
                    // return to start fragment by replacing current fragment in fragment_containter.
                    FragmentManager fmFav = getSupportFragmentManager();
                    Bundle argumentsFav = new Bundle();
                    argumentsFav.putString("method", "favorite");

                    MainListFragment favorites = new MainListFragment();
                    favorites.setArguments(argumentsFav);
                    FragmentTransaction ftFav = fmFav.beginTransaction();
                    ftFav.replace(R.id.fragment_container, favorites, "FavFragment");
                    ftFav.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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


}
