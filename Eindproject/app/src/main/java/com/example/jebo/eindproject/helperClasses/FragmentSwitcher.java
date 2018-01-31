package com.example.jebo.eindproject.helperClasses;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.example.jebo.eindproject.InfoActivity;
import com.example.jebo.eindproject.R;

import org.json.JSONObject;

/**
 * Created by Jessy.
 */

public class FragmentSwitcher {
    public void switchToDetailed(JSONObject coinObject, FragmentManager fm) {
        // add Object to Bundle, so it is available in the detail fragment.
        Bundle arguments = new Bundle();
        arguments.putString("coinObject", coinObject.toString());

        // go to info fragment.
        InfoActivity fragment = new InfoActivity();
        fragment.setArguments(arguments);

        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.fragment_container, fragment)
                .addToBackStack(null).commit();
    }
}
