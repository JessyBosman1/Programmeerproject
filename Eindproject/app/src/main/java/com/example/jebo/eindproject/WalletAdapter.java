package com.example.jebo.eindproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jessy Bosman
 * Minor Programmeren UvA
 * Programmeer Project
 */

public class WalletAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList walletItems;
    private final ArrayList walletAmounts;
    private final SharedPreferences stored = getContext().getSharedPreferences("storedPrice", MODE_PRIVATE);

    WalletAdapter(Activity context, ArrayList walletCoinNames, ArrayList amountWallet) {
        super(context, R.layout.rowlayout_wallet, walletCoinNames);
        // set variables
        this.context = context;
        this.walletItems = walletCoinNames;
        this.walletAmounts = amountWallet;
    }

    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.rowlayout_wallet, null, true);

        // get references for current row
        TextView txtName = rowView.findViewById(R.id.name);
        TextView txtAmount = rowView.findViewById(R.id.amount);
        TextView txtValue = rowView.findViewById(R.id.value);

        // set text fields
        txtName.setText(walletItems.get(position).toString());
        txtAmount.setText(walletAmounts.get(position).toString());

        // calculate the value of the amount of coins
        String price_EUR = stored.getString(walletItems.get(position).toString(), null);
        try {
            Float value = Float.parseFloat(price_EUR) * Float.parseFloat(walletAmounts.get(position).toString());
            txtValue.setText("€" + value.toString());
        }
        // if not possible, set text unknown
        catch (Exception e) {
            txtValue.setText("unknown");
        }
        return rowView;

    }
}