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
                    MainListFragment fragmentList = new MainListFragment();
                    FragmentTransaction ftList = fmList.beginTransaction();
                    ftList.replace(R.id.fragment_container, fragmentList, "MainListFragment");
                    ftList.commit();
                    return true;

                case R.id.navigation_favorite:
                    // return to start fragment by replacing current fragment in fragment_containter.
                    FragmentManager fmInfo = getSupportFragmentManager();
                    InfoActivity fragmentInfo = new InfoActivity();
                    FragmentTransaction ftInfo = fmInfo.beginTransaction();
                    ftInfo.replace(R.id.fragment_container, fragmentInfo, "InfoFragment");
                    ftInfo.commit();
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
        FragmentManager fmList = getSupportFragmentManager();
        MainListFragment fragmentList = new MainListFragment();
        FragmentTransaction ftList = fmList.beginTransaction();
        ftList.replace(R.id.fragment_container, fragmentList, "StartFragment");
        ftList.commit();
    }


}
