package com.example.jebo.eindproject;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class WalletFragment extends Fragment implements View.OnClickListener{
    private View view;
    private ArrayList<String> namesWallet = new ArrayList<String>();
    private ArrayList<String> amountWallet = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        setListeners(view);
        getData();

        return view;
    }


    private void setListeners(View view) {
        FloatingActionButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                    addItemDialog();
                break;
        }
    }

    private void getData(){
        SharedPreferences walletPrefs = getActivity().getSharedPreferences("Wallet", MODE_PRIVATE);
        if(walletPrefs.getAll().size() > 0){
            Map<String, ?> allEntries = walletPrefs.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                addData(entry.getKey(), entry.getValue().toString());
            }
            createList();
        }
    }

    private void addData(String name, String amount){
        namesWallet.add(name);
        amountWallet.add(amount);
    }

    private void createList(){
        WalletAdapter adapter;
        adapter = new WalletAdapter(getActivity(), namesWallet, amountWallet);
        ListView list = view.findViewById(R.id.walletList);
        list.setAdapter(adapter);
    }

    private void addItemDialog(){
         AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText currencyName = dialogView.findViewById(R.id.currencyName);
        final EditText currencyAmount = dialogView.findViewById(R.id.currencyAmount);

        dialogBuilder.setTitle("Add Currency");
        //dialogBuilder.setMessage("Enter currency and amount");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = currencyName.getText().toString();
                String amount = currencyAmount.getText().toString();

                if(!name.equals("") && !amount.equals("")){
                    changeItemsMemory(name, amount);}
                else{Toast.makeText(getContext(), "Error: empty field", Toast.LENGTH_SHORT).show();}
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void changeItemsMemory(String name, String amount){
        SharedPreferences walletPrefs = getActivity().getSharedPreferences("Wallet", MODE_PRIVATE);
        if (amount != null){walletPrefs.edit().putString(name, amount).apply();}
        else {walletPrefs.edit().remove(name).apply();}
        getData();
    }

}
