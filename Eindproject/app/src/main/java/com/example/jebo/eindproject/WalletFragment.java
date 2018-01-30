package com.example.jebo.eindproject;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
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

/**
 * Created by Jessy Bosman
 * Minor Programmeren UvA
 * Programmeer Project
 */

public class WalletFragment extends Fragment {
    private View view;
    // create lists to store acquired memory items
    private ArrayList<String> namesWallet = new ArrayList<>();
    private ArrayList<String> amountWallet = new ArrayList<>();
    private ArrayList<String> autoCompleteArray = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        // set listener on add button
        setListener(view);
        // initialize autocomplete
        setAutoComplete();
        getData();

        return view;
    }

    /* set listener on add button */
    private void setListener(View view) {
        FloatingActionButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // create dialog to add item
                addItemDialog();
            }
        });
    }

    /* initialize autocomplete */
    private void setAutoComplete() {
        SharedPreferences storedName = getActivity().getSharedPreferences("storedPrice", MODE_PRIVATE);

        // get all storedNames in shared prefs to add as suggestion of the autocomplete
        Map<String, ?> allEntries = storedName.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            addSuggestionAutocomplete(entry.getKey());
        }
    }

    /* add to global Auto Complete Suggestions */
    private void addSuggestionAutocomplete(String name) {
        autoCompleteArray.add(name);
    }

    /* get all previous additions from saved data */
    private void getData() {
        // clear to avoid duplication when refreshing list
        namesWallet.clear();
        amountWallet.clear();

        SharedPreferences walletPrefs = getActivity().getSharedPreferences("Wallet", MODE_PRIVATE);
        // get all entries saved in memory from previous sessions
        if (walletPrefs.getAll().size() > 0) {
            Map<String, ?> allEntries = walletPrefs.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                addData(entry.getKey(), entry.getValue().toString());
            }
            // create actual list from data
            createList();
        }
    }

    /* add values to global lists */
    private void addData(String name, String amount) {
        namesWallet.add(name);
        amountWallet.add(amount);
    }

    /* // create list from data from previous sessions */
    private void createList() {
        WalletAdapter adapter;
        // get references
        adapter = new WalletAdapter(getActivity(), namesWallet, amountWallet);
        ListView list = view.findViewById(R.id.walletList);

        // set adapter
        list.setAdapter(adapter);
        // set individual item click listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // on click; pop up edit/remove dialog
                editItemDialog(namesWallet.get(position), amountWallet.get(position));
            }
        });
    }

    /* Build dialog to add item */
    private void addItemDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(dialogView);

        final AutoCompleteTextView currencyName = dialogView.findViewById(R.id.currencyName);
        final AutoCompleteTextView currencyAmount = dialogView.findViewById(R.id.currencyAmount);

        // set auto complete field
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, autoCompleteArray);
        currencyName.setAdapter(adapter);

        dialogBuilder.setTitle("Add Currency");
        // create add button
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // get input from fields
                String name = currencyName.getText().toString();
                String amount = currencyAmount.getText().toString();

                // check if currency is available
                if (autoCompleteArray.contains(name)) {
                    try {
                        // temp variable; if not parseble, go to except
                        float temp = Float.parseFloat(amount);
                        changeItemsMemory(name, amount);
                    }
                    catch (NumberFormatException e) {
                        Log.d("NumberFormatException", e.toString());
                        Toast.makeText(getContext(), "Error: amount should be a number", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error: currency not in Database", Toast.LENGTH_LONG).show();
                }
            }
        });
        // set cancel button
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    /* Build dialog to edit or remove item item */
    private void editItemDialog(String name, String amount) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(dialogView);

        final AutoCompleteTextView currencyName = dialogView.findViewById(R.id.currencyName);
        final AutoCompleteTextView currencyAmount = dialogView.findViewById(R.id.currencyAmount);
        // make currencyName not editable, so only amount can be added
        currencyName.setEnabled(false);
        currencyName.setInputType(InputType.TYPE_NULL);
        // set text from current name and amount before editing
        currencyName.setText(name);
        currencyAmount.setText(amount);

        dialogBuilder.setTitle("Edit Currency");
        // set edit button
        dialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // overWrite existing record, or throw error if something is wrong
                String name = currencyName.getText().toString();
                String amount = currencyAmount.getText().toString();
                try {
                    // temp variable; if not parseble, go to except
                    float temp = Float.parseFloat(amount);
                    changeItemsMemory(name, amount);
                }
                catch (NumberFormatException e) {
                    Log.d("NumberFormatException", e.toString());
                    Toast.makeText(getContext(), "Error: amount should be a number", Toast.LENGTH_LONG).show();
                }
            }
        });
        // set delete button
        dialogBuilder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // remove item from memory
                String name = currencyName.getText().toString();
                changeItemsMemory(name, null);
            }
        });
        // set neutral button
        dialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // simply cancel the dialog
                dialog.cancel();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    /* edit or remove item in memory, and refresh list */
    private void changeItemsMemory(String name, String amount) {
        SharedPreferences walletPrefs = getActivity().getSharedPreferences("Wallet", MODE_PRIVATE);
        if (amount != null) {
            // change value or add value if amount is given
            walletPrefs.edit().putString(name, amount).apply();
        } else {
            // remove record if value is null
            walletPrefs.edit().remove(name).apply();
        }
        // refresh list
        getData();
    }

}
