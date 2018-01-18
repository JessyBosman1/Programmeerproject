package com.example.jebo.eindproject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AndroidException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class MainListFragment extends Fragment {
    public View view;
    public ArrayList<String> coinNameList = new ArrayList<String>();
    public String Method;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_list, container, false);

        getListData();

        return view;
    }

    @Override
    public void onResume() {
        getListData();
        super.onResume();
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

    public void getListData(){
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getContext());

            // get data from API for the specific coin
            String url = "https://api.coinmarketcap.com/v1/ticker/?convert=EUR";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                createListView(jsonArray);

                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject coinObject = jsonArray.getJSONObject(i);
                                    addObjectToLists(coinObject.getString("name"));
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

    public void addObjectToLists(String Name){
        if (Method.equals("standard")){
            coinNameList.add(Name);
        }
        else if(Method.equals("favorite")){
            SharedPreferences favPrefs = getActivity().getSharedPreferences("Favorites", MODE_PRIVATE);
            Log.d("favPrefs","enter");
            Boolean prefExist = favPrefs.getBoolean(Name, false);
            if (prefExist){
                coinNameList.add(Name);
            }
        }
    }


    public void createListView(final JSONArray response){
        JSONArray favoriteCoins = new JSONArray();
        coinListAdapter adapter;

        if (Method.equals("favorite")) {
            try {
                SharedPreferences favPrefs = getActivity().getSharedPreferences("Favorites", MODE_PRIVATE);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject coinObject = response.getJSONObject(i);

                    Boolean prefExist = favPrefs.getBoolean((coinObject.getString("name")), false);
                    if (prefExist){
                        favoriteCoins.put(coinObject);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.d("method",favoriteCoins.toString());
            Log.d("method",coinNameList.toString());
            adapter = new coinListAdapter(getActivity(), favoriteCoins, coinNameList);
        }

        else{
            adapter = new coinListAdapter(getActivity(), response, coinNameList);
        }

        adapter.clear();

        ListView list = view.findViewById(R.id.list);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try{
                    JSONObject coinObject = response.getJSONObject(position);
                    goToDetailed(coinObject);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSONException",e.toString());
                }
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                try{
                    JSONObject coinObject = response.getJSONObject(position);

                    Log.d("parent", coinObject.getString("name"));

                    ImageView favIcon = view.findViewById(R.id.favIcon);

                    SharedPreferences favPrefs = getActivity().getSharedPreferences("Favorites", MODE_PRIVATE);
                    Boolean prefExist = favPrefs.getBoolean(coinObject.getString("name"), false);

                    if (prefExist){
                        favIcon.setImageResource(android.R.drawable.btn_star_big_off);
                        updateFavorites(coinObject.getString("name"), false);
                    }
                    else {
                        favIcon.setImageResource(android.R.drawable.btn_star_big_on);
                        updateFavorites(coinObject.getString("name"), true);
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSONException",e.toString());
                }
                return true;
            }
            private void updateFavorites(String coinName, Boolean isFavorite){
                SharedPreferences favPrefs = getActivity().getSharedPreferences("Favorites", MODE_PRIVATE);

                if (isFavorite){
                favPrefs.edit().putBoolean(coinName, isFavorite).apply();
                }
                else {
                    favPrefs.edit().remove(coinName).apply();
                }

            }
        });
    }

    private void goToDetailed(JSONObject coinObject) {
        FragmentManager fm = getActivity().getSupportFragmentManager();

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

