package com.example.jebo.eindproject;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class InfoActivity extends Fragment {

    private TextView mTextMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_info, container, false);
        loadData("ETH");
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void loadData(String coinName) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // get data from API for the specific coin
        String url = "https://min-api.cryptocompare.com/data/histoday?fsym="+coinName+"&tsym=USD&limit=10";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mTextMessage = (TextView) getActivity().findViewById(R.id.message);
                        JSONArray dataArray;

                        GraphView graph = (GraphView) getActivity().findViewById(R.id.graph);

                        try {
                            JSONObject newObject = (JSONObject) new JSONTokener(response).nextValue();

                            dataArray = newObject.getJSONArray("Data");

                            DataPoint[] dp = new DataPoint[dataArray.length()];
                            for (int i = 0; i < dataArray.length(); i++) {
                                //int X = Integer.parseInt(dataArray.getJSONObject(i).getString("time"));
                                float Y = Float.parseFloat(dataArray.getJSONObject(i).getString("open"));

                                dp[i] = new DataPoint(i, Y);

                            }

                            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);

                            graph.addSeries(series);

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

}
