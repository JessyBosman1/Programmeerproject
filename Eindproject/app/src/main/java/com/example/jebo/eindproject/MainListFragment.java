package com.example.jebo.eindproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jebo.eindproject.helperClasses.FragmentSwitcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;

public class MainListFragment extends Fragment {
    private View view;
    // parcelable used to restore scroll location in list if returning to fragment
    Parcelable state;
    // list of all names of coins to create array adapter
    private ArrayList<String> coinNameList = new ArrayList<String>();
    // method of list filtering (favorite or normal)
    private String Method;
    // Bool to track searchbar state
    private Boolean searchBarActive;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_list, container, false);
        getListData("");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        // Save ListView state @onPause to restore scroll location in list if returning to fragment
        ListView list = view.findViewById(R.id.list);
        state = list.onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get variables from bundle, to obtain the method of the list display.
        Bundle bundle = this.getArguments();
        // Check if bundle exists.
        if (bundle != null) {
            Method = bundle.getString("method", "standard");
        }
        Log.d("method", bundle.toString());
    }

    /* get data from API and send to list Adapter */
    public void getListData(final String filterText) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // get data from API, coins with value converted to Euros
        String url = "https://api.coinmarketcap.com/v1/ticker/?convert=EUR";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // make sure coin list is empty (useful if recreating list filtering)
                            coinNameList.clear();
                            // create response array and optional array used if filtering
                            JSONArray jsonArray = new JSONArray(response);
                            JSONArray filteredArray = new JSONArray();

                            // if something filled-in in searchbar: filter list with input
                            if (!filterText.equals("")) {
                                createListView(filteredArray);

                                // only add object if object contains input from search
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject coinObject = jsonArray.getJSONObject(i);
                                    if (coinObject.getString("name").toLowerCase().contains(filterText.toLowerCase())) {
                                        addObjectToLists(coinObject.getString("name"));
                                        filteredArray.put(coinObject);
                                    }
                                }
                            }
                            // if there is nothing filled-in in the searchbar simply create whole list from api
                            else {
                                createListView(jsonArray);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject coinObject = jsonArray.getJSONObject(i);
                                    addObjectToLists(coinObject.getString("name"));
                                }
                            }
                            // only initiate searchbar of not created, to avoid duplicate
                            if (searchBarActive == null) {
                                searchBarActive = true;
                                initSearchBar();
                            }
                            // save names and prices offline to save data and create autoComplete fields data
                            saveOffline(jsonArray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    /* save names and prices offline to save data and create autoComplete fields data */
                    private void saveOffline(JSONArray response) {
                        try {
                            SharedPreferences stored = getActivity().getSharedPreferences("storedPrice", MODE_PRIVATE);
                            SharedPreferences storedSym = getActivity().getSharedPreferences("storedSymbol", MODE_PRIVATE);

                            // save as (String name, String price)
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject coinObject = response.getJSONObject(i);
                                stored.edit().putString(coinObject.getString("name"), coinObject.getString("price_eur")).apply();
                                storedSym.edit().putString(coinObject.getString("name"), coinObject.getString("symbol")).apply();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /* add object to public list, depending on method (favorite or standard) */
    private void addObjectToLists(String Name) {
        Log.d("Method", Method);
        // if standard, simply add name
        if (Method.equals("standard")) {
            coinNameList.add(Name);

        }
        // if favorite, only add object if it is favorited by the user
        else if (Method.equals("favorite")) {
            SharedPreferences favPrefs = getActivity().getSharedPreferences("Favorites", MODE_PRIVATE);
            Log.d("favPrefs", "enter");
            Boolean prefExist = favPrefs.getBoolean(Name, false);

            if (prefExist) {
                coinNameList.add(Name);
            }
        }
    }

    /* create list view based on Method and Filter */
    public void createListView(final JSONArray response) {
        Log.d("test", coinNameList.toString());
        Log.d("test", response.toString());

        // create new array to select only favorites if requested
        final JSONArray favoriteCoins = new JSONArray();
        CoinListAdapter adapter;

        Log.d("Method", Method);
        // create adapter based on Method
        if (Method.equals("favorite")) {
            try {
                // get favorites of user
                SharedPreferences favPrefs = getActivity().getSharedPreferences("Favorites", MODE_PRIVATE);
                // add Object only if in shared prefs of user
                for (int i = 0; i < response.length(); i++) {
                    JSONObject coinObject = response.getJSONObject(i);
                    Boolean prefExist = favPrefs.getBoolean((coinObject.getString("name")), false);
                    Log.d("FavoriteName", coinObject.getString("name"));

                    if (prefExist) {
                        favoriteCoins.put(coinObject);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // set adapter with filtered data (only favorites)
            adapter = new CoinListAdapter(getActivity(), favoriteCoins, coinNameList);
        } else {
            // set adapter with all available data
            adapter = new CoinListAdapter(getActivity(), response, coinNameList);
        }
        // clear adapter from all data left from previous adapters and filters
        adapter.clear();

        // set adapter to list
        ListView list = view.findViewById(R.id.list);
        list.setAdapter(adapter);

        // Restore previous state(scroll position) if available
        if (state != null) {
            list.onRestoreInstanceState(state);
        }

        // set onClick listener on individual list items
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // check correct reference list to find clicked item correctly
                Log.d("Method", Method);
                try {
                    if (Method.equals("favorite")) {
                        JSONObject coinObject = favoriteCoins.getJSONObject(position);
                        // go to detailed information of clicked item
                        new FragmentSwitcher().switchToDetailed(coinObject, getActivity().getSupportFragmentManager());

                    } else if (Method.equals("standard")) {
                        JSONObject coinObject = response.getJSONObject(position);
                        // go to detailed information of clicked item
                        new FragmentSwitcher().switchToDetailed(coinObject, getActivity().getSupportFragmentManager());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSONException", e.toString());
                }
            }
        });

        // set onLongClick listener on individual list items
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // check correct reference list to find clicked item correctly
                Log.d("Method", Method);
                try {
                    JSONObject coinObject;
                    if (Method.equals("favorite")) {
                        coinObject = favoriteCoins.getJSONObject(position);
                    } else {
                        coinObject = response.getJSONObject(position);
                    }
                    // get fav icon (star) from list item container
                    ImageView favIcon = view.findViewById(R.id.favIcon);

                    // Check if item exists in favorites
                    SharedPreferences favPrefs = getActivity().getSharedPreferences("Favorites", MODE_PRIVATE);
                    Boolean prefExist = favPrefs.getBoolean(coinObject.getString("name"), false);

                    // if it exists, remove from favorites
                    if (prefExist) {
                        favIcon.setImageResource(android.R.drawable.btn_star_big_off);
                        updateFavorites(coinObject.getString("name"), false);
                    }
                    // if it not yet exists, add to favorites
                    else {
                        favIcon.setImageResource(android.R.drawable.btn_star_big_on);
                        updateFavorites(coinObject.getString("name"), true);
                    }
                    // recreate list with new favorites and no filter
                    getListData("");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSONException", e.toString());
                }
                return true;
            }

            /* add or remove favorite depending on parameter isFavorite*/
            private void updateFavorites(String coinName, Boolean isFavorite) {
                SharedPreferences favPrefs = getActivity().getSharedPreferences("Favorites", MODE_PRIVATE);

                if (isFavorite) {
                    favPrefs.edit().putBoolean(coinName, true).apply();
                } else {
                    favPrefs.edit().remove(coinName).apply();
                }
            }
        });
    }

    // set listener to searchbar, updating list when searching
    private void initSearchBar() {
        final EditText inputSearch = view.findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            // Start a timer when the user starts typing
            final long DELAY = 500; // milliseconds
            Timer timer = new Timer();

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {}

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {}

            @Override
            public void afterTextChanged(Editable arg0) {
                // if the user types again before the timer expires, reset timer
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        // If the timer expires, the user is done typing and the list will be filtered by input
                        new TimerTask() {
                            @Override
                            public void run() {
                                getListData(inputSearch.getText().toString());
                            }
                        },
                        DELAY
                );
            }

        });


    }
}

