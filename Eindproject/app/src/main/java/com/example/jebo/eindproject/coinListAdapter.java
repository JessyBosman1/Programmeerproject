package com.example.jebo.eindproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jessy Bosman
 * Minor Programmeren UvA
 * Programmeer Project
 */

public class CoinListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final JSONArray coinObjects;

    CoinListAdapter(Activity context, JSONArray coinObjects, ArrayList coinNameList) {
        super(context, R.layout.rowlayout, coinNameList);
        this.context = context;
        this.coinObjects = coinObjects;

    }

    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.rowlayout, null, true);

        // get references
        TextView txtRank = rowView.findViewById(R.id.rankField);
        TextView txtTitle = rowView.findViewById(R.id.name);
        ImageView imageView = rowView.findViewById(R.id.icon);
        TextView symboltxt = rowView.findViewById(R.id.symbolField);
        TextView changetxt = rowView.findViewById(R.id.changeField);
        TextView pricetxt = rowView.findViewById(R.id.priceField);
        TextView pricetxtBtc = rowView.findViewById(R.id.priceFieldBtc);
        ImageView favIcon = rowView.findViewById(R.id.favIcon);

        try {
            // fill in the text field of the row view
            JSONObject coinObject = coinObjects.getJSONObject(position);
            Log.d("adapter", coinObject.toString());
            txtRank.setText(coinObject.get("rank").toString());
            txtTitle.setText(coinObject.get("name").toString());
            pricetxt.setText("€" + coinObject.get("price_eur").toString());
            pricetxtBtc.setText("btc" + coinObject.get("price_btc").toString());

            // load Icon Image with Glide
            Glide.with(context)
                    .load("https://files.coinmarketcap.com/static/img/coins/64x64/" + coinObject.get("id").toString() + ".png")
                    .into(imageView);

            symboltxt.setText(coinObject.get("symbol").toString());

            // check if item if favorite according to memory
            SharedPreferences favPrefs = getContext().getSharedPreferences("Favorites", MODE_PRIVATE);
            Map<String, ?> allEntries = favPrefs.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                if (entry.getValue().toString().equals("true") && entry.getKey().equals(coinObject.get("name").toString())) {
                    // if favorite, change icon to mark favorite
                    favIcon.setImageResource(android.R.drawable.btn_star_big_on);
                }
            }
            // set percentage, and change color to green if positive or red if negative
            changetxt.setText(coinObject.get("percent_change_24h").toString() + "%(24h)");
            // set textcolor according to positive or negative value
            changetxt.setTextColor(colorSelector(coinObject.get("percent_change_24h").toString()));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONException", e.toString());
        }
        return rowView;
    }

    /* select textcolor according to positive or negative value */
    private static Integer colorSelector(String Object) {
        try {
            if (Float.parseFloat(Object) < 0.0) {
                return (0xffff4444);
            } else {
                return (0xff669900);
            }
        } catch (Exception e) {
            Log.d("ParseError", e.toString());
        }
        return (0xffffffff);

    }
}