package com.example.jebo.eindproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class WalletAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList walletItems;
    private final ArrayList walletAmounts;
    private final SharedPreferences stored = getContext().getSharedPreferences("storedPrice", MODE_PRIVATE);


    public WalletAdapter(Activity context, ArrayList walletCoinNames, ArrayList amountWallet) {
        super(context, R.layout.rowlayout_wallet, walletCoinNames);
        this.context = context;
        this.walletItems = walletCoinNames;
        this.walletAmounts = amountWallet;

        }

    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.rowlayout_wallet, null, true);

        TextView txtName = rowView.findViewById(R.id.name);
        TextView txtAmount = rowView.findViewById(R.id.amount);
        TextView txtValue = rowView.findViewById(R.id.value);
        //Button editButton = rowView.findViewById(R.id.editButton);
        //Button removeButton = rowView.findViewById(R.id.removeButton);

        txtName.setText(walletItems.get(position).toString());
        txtAmount.setText(walletAmounts.get(position).toString());

        String price_EUR = stored.getString(walletItems.get(position).toString(), null);
        try{Float value = Float.parseFloat(price_EUR) * Float.parseFloat(walletAmounts.get(position).toString());
        txtValue.setText("€" + value.toString());}
        catch(Exception e){txtValue.setText("unknown");}

        return rowView;

    }
}