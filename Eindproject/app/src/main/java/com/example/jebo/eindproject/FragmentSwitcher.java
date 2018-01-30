package com.example.jebo.eindproject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.json.JSONObject;

/**
 * Created by Jessy.
 */

class FragmentSwitcher {
    void switchToDetailed(JSONObject coinObject, FragmentManager fm) {
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
