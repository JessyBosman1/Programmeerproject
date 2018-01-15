package com.example.jebo.eindproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
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


public class MainListFragment extends Fragment {
    public View view;
    public ArrayList<String> coinNameList = new ArrayList<String>();
    public ArrayList<String> coinSymbolList = new ArrayList<String>();


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
    }



    public class CustomListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final JSONArray coinObjects;

        public CustomListAdapter(Activity context, JSONArray coinObjects) {
            super(context, R.layout.rowlayout, coinNameList);
            this.context = context;
            this.coinObjects = coinObjects;
        }

        public View getView(int position, View view, @NonNull ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.rowlayout, null,true);

            TextView txtRank = rowView.findViewById(R.id.rankField);
            TextView txtTitle = rowView.findViewById(R.id.item);
            ImageView imageView = rowView.findViewById(R.id.icon);
            TextView symboltxt = rowView.findViewById(R.id.symbolField);
            TextView changetxt = rowView.findViewById(R.id.changeField);

            try {
                JSONObject coinObject = coinObjects.getJSONObject(position);

                txtRank.setText(coinObject.get("rank").toString());
                txtTitle.setText(coinObject.get("name").toString());

                Glide.with(context)
                        .load("https://files.coinmarketcap.com/static/img/coins/64x64/" + coinObject.get("id").toString() + ".png")
                        .into(imageView);

                symboltxt.setText(coinObject.get("symbol").toString());
                changetxt.setText(coinObject.get("percent_change_24h").toString());
                try{
                if(Float.parseFloat(coinObject.get("percent_change_24h").toString()) < 0.0){
                    changetxt.setTextColor(0xffff4444);
                }
                else{changetxt.setTextColor(0xff669900);}
                } catch (Exception e){Log.d("ParseError",e.toString());}
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("JSONException",e.toString());
            }
            return rowView;

        }
    }

    public void getListData(){
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getActivity());

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
                                    addObjectToLists(coinObject.getString("name"), coinObject.getString("symbol"));
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

    public void addObjectToLists(String Name, String Symbol){
        coinNameList.add(Name);
        coinSymbolList.add(Symbol);
    }


    public void createListView(final JSONArray response){
        CustomListAdapter adapter = new CustomListAdapter(getActivity(), response);
        adapter.clear();

        ListView list = view.findViewById(R.id.list);

        list.setAdapter(adapter);
        initSearchBar(adapter, response);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try{
                    ;
                    JSONObject coinObject = response.getJSONObject(position);
                    goToDetailed(coinObject);
                    Toast.makeText(getContext(), coinObject.getString("name"), Toast.LENGTH_SHORT).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSONException",e.toString());
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

    // set listener to searchbar, updating list when searching
    private void initSearchBar(final CustomListAdapter adapter, final JSONArray Array){
        EditText inputSearch = getActivity().findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

            }


            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });


    }
}

