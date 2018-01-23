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

public class coinListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final JSONArray coinObjects;


    public coinListAdapter(Activity context, JSONArray coinObjects, ArrayList coinNameList) {
        super(context, R.layout.rowlayout, coinNameList);
        this.context = context;
        this.coinObjects = coinObjects;

    }

    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.rowlayout, null,true);

        TextView txtRank = rowView.findViewById(R.id.rankField);
        TextView txtTitle = rowView.findViewById(R.id.name);
        ImageView imageView = rowView.findViewById(R.id.icon);
        TextView symboltxt = rowView.findViewById(R.id.symbolField);
        TextView changetxt = rowView.findViewById(R.id.changeField);
        TextView pricetxt = rowView.findViewById(R.id.priceField);
        TextView pricetxtBtc = rowView.findViewById(R.id.priceFieldBtc);
        ImageView favIcon = rowView.findViewById(R.id.favIcon);

        try {
            JSONObject coinObject = coinObjects.getJSONObject(position);
            Log.d("adapter", coinObject.toString());
            txtRank.setText(coinObject.get("rank").toString());
            txtTitle.setText(coinObject.get("name").toString());
            pricetxt.setText("€" + coinObject.get("price_eur").toString());
            pricetxtBtc.setText("btc" + coinObject.get("price_btc").toString());

            Glide.with(context)
                    .load("https://files.coinmarketcap.com/static/img/coins/64x64/" + coinObject.get("id").toString() + ".png")
                    .into(imageView);

            symboltxt.setText(coinObject.get("symbol").toString());


            SharedPreferences favPrefs = getContext().getSharedPreferences("Favorites", MODE_PRIVATE);

            Map<String, ?> allEntries = favPrefs.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                if (entry.getValue().toString().equals("true") && entry.getKey().equals(coinObject.get("name").toString())){
                    favIcon.setImageResource(android.R.drawable.btn_star_big_on);
                }
            }

            changetxt.setText(coinObject.get("percent_change_24h").toString() + "%(24h)");
            try{
                if(Float.parseFloat(coinObject.get("percent_change_24h").toString()) < 0.0){
                    changetxt.setTextColor(0xffff4444);
                }
                else{changetxt.setTextColor(0xff669900);}
            } catch (Exception e){
                Log.d("ParseError",e.toString());}

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONException",e.toString());
        }
        return rowView;

    }
}