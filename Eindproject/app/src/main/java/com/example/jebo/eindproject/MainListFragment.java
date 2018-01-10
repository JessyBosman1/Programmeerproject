package com.example.jebo.eindproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class MainListFragment extends Fragment {
    public View view;

    public ArrayList<String> coinNameList = new ArrayList<String>();
    public ArrayList<String> coinSymbolList = new ArrayList<String>();
    String[] itemname ={
            "Safari",
            "Camera",
            "Global",
            "FireFox",
            "UC Browser",
            "Android Folder",
            "VLC Player",
            "Cold War"
    };

    Integer[] imgid={
            R.drawable.bitcoin,
            R.drawable.ethereum,
            R.drawable.iota,
            R.drawable.bitcoin,
            R.drawable.ethereum,
            R.drawable.iota,
            R.drawable.bitcoin,
            R.drawable.ethereum,
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_list, container, false);
        setListView();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public class CustomListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] itemname;
        private final Integer[] imgid;

        public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid) {
            super(context, R.layout.rowlayout, itemname);
            this.context=context;
            this.itemname=itemname;
            this.imgid=imgid;
        }

        public View getView(int position, View view, @NonNull ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.rowlayout, null,true);

            TextView txtTitle = rowView.findViewById(R.id.item);
            ImageView imageView = rowView.findViewById(R.id.icon);
            TextView extratxt = rowView.findViewById(R.id.textView1);

            txtTitle.setText(itemname[position]);
            imageView.setImageResource(imgid[position]);
            extratxt.setText("Description "+itemname[position]);
            return rowView;

        };
/*
        public CustomListAdapter(Activity context, ArrayList itemname, Integer[] imgid) {
            super(context, R.layout.rowlayout, itemname);
            this.context=context;
            this.itemname=itemname;
            this.imgid=imgid;
        }

        public View getView(int position, View view, @NonNull ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.rowlayout, null,true);

            TextView txtTitle = rowView.findViewById(R.id.item);
            ImageView imageView = rowView.findViewById(R.id.icon);
            TextView extratxt = rowView.findViewById(R.id.textView1);

            txtTitle.setText(itemname.get(position).toString());
            imageView.setImageResource(imgid[position]);
            extratxt.setText("Description ");
            return rowView;

        };*/
    }

    public void getListData(){
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getActivity());

            // get data from API for the specific coin
            String url = "https://api.coinmarketcap.com/v1/ticker/";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JSONArray dataArray;

                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject coinObject = jsonArray.getJSONObject(i);
                                    addObjectToLists(coinObject.getString("name"), coinObject.getString("symbol"));
                                    Log.d("test",coinObject.toString());
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

    public void setListView() {
        getListData();
        CustomListAdapter adapter = new CustomListAdapter(getActivity(), itemname, imgid);
        ListView list = view.findViewById(R.id.list);
        TextView testTextView = view.findViewById(R.id.testTextView);
        testTextView.setText("test");

        Log.d("test", testTextView.getText().toString());
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String Slecteditem= itemname[+position];
                Toast.makeText(getContext(), Slecteditem, Toast.LENGTH_SHORT).show();

            }
        });
    }
}

