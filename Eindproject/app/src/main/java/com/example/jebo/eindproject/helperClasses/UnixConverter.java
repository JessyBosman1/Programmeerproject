package com.example.jebo.eindproject.helperClasses;

import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * Created by Jessy Bosman
 * Minor Programmeren UvA
 * Programmeer Project
 */
public class UnixConverter {
    /* Convert UNIX timestamp to given format */
    public static String convertUnix(String urlParam, String unixTime){
        long UNIX = Long.parseLong(unixTime) * 1000;
        Log.d("UNIX", unixTime);
        switch (urlParam) {
            // Format UNIX according to given format
            case "&tsym=EUR&limit=60": {
                return new SimpleDateFormat("HH:mm").format(new java.util.Date(UNIX));
            }
            case "&tsym=EUR&limit=12": {
                return new SimpleDateFormat("HH:mm").format(new java.util.Date(UNIX));
            }
            case "&tsym=EUR&limit=24": {
                return new SimpleDateFormat("HH:mm").format(new java.util.Date(UNIX));
            }
            case "&tsym=EUR&limit=7": {
                return new SimpleDateFormat("dd/MM").format(new java.util.Date(UNIX));
            }
        }
        return "";
    }
}
