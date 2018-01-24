package com.example.jebo.eindproject;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class WalletFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ArrayList<String> namesWallet = new ArrayList<>();
    private ArrayList<String> amountWallet = new ArrayList<>();
    private ArrayList<String> autoCompleteArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        setListeners(view);
        setAutoComplete();
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

    private void setAutoComplete() {
        SharedPreferences storedName = getActivity().getSharedPreferences("storedPrice", MODE_PRIVATE);

        Map<String, ?> allEntries = storedName.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            addSuggestionAutocomplete(entry.getKey());
        }
    }

    private void addSuggestionAutocomplete(String name) {
        autoCompleteArray.add(name);
    }

    private void getData() {
        namesWallet.clear();
        amountWallet.clear();
        SharedPreferences walletPrefs = getActivity().getSharedPreferences("Wallet", MODE_PRIVATE);
        if (walletPrefs.getAll().size() > 0) {
            Map<String, ?> allEntries = walletPrefs.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                addData(entry.getKey(), entry.getValue().toString());
            }
            createList();
        }
    }

    private void addData(String name, String amount) {
        namesWallet.add(name);
        amountWallet.add(amount);
    }

    private void createList() {
        WalletAdapter adapter;
        adapter = new WalletAdapter(getActivity(), namesWallet, amountWallet);
        ListView list = view.findViewById(R.id.walletList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                editItemDialog(namesWallet.get(position), amountWallet.get(position));
            }
        });
    }

    private void addItemDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(dialogView);

        final AutoCompleteTextView currencyName = dialogView.findViewById(R.id.currencyName);
        final AutoCompleteTextView currencyAmount = dialogView.findViewById(R.id.currencyAmount);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, autoCompleteArray);

        currencyName.setAdapter(adapter);

        dialogBuilder.setTitle("Add Currency");
        //dialogBuilder.setMessage("Enter currency and amount");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = currencyName.getText().toString();
                String amount = currencyAmount.getText().toString();

                if (autoCompleteArray.contains(name)) {
                    try {
                        float temp = Float.parseFloat(amount);
                        changeItemsMemory(name, amount);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Error: amount should be a number", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error: currency not in Database", Toast.LENGTH_LONG).show();
                }
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

    private void editItemDialog(String name, String amount) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(dialogView);

        final AutoCompleteTextView currencyName = dialogView.findViewById(R.id.currencyName);
        final AutoCompleteTextView currencyAmount = dialogView.findViewById(R.id.currencyAmount);
        currencyName.setEnabled(false);
        currencyName.setInputType(InputType.TYPE_NULL);
        currencyName.setText(name);
        currencyAmount.setText(amount);

        dialogBuilder.setTitle("Edit Currency");
        //dialogBuilder.setMessage("Enter currency and amount");
        dialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = currencyName.getText().toString();
                String amount = currencyAmount.getText().toString();
                try {
                    float temp = Float.parseFloat(amount);
                    changeItemsMemory(name, amount);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Error: amount should be a number", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = currencyName.getText().toString();
                changeItemsMemory(name, null);
            }
        });

        dialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void changeItemsMemory(String name, String amount) {
        SharedPreferences walletPrefs = getActivity().getSharedPreferences("Wallet", MODE_PRIVATE);
        if (amount != null) {
            walletPrefs.edit().putString(name, amount).apply();
        } else {
            walletPrefs.edit().remove(name).apply();
        }
        getData();
    }

}
