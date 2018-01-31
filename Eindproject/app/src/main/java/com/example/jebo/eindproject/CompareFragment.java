package com.example.jebo.eindproject;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jebo.eindproject.helperClasses.UnixConverter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Jessy Bosman
 * Minor Programmeren UvA
 * Programmeer Project
 */
public class CompareFragment extends Fragment {
    private View view;
    private ArrayList<String> autoCompleteArray = new ArrayList<>();
    LineData chartData = new LineData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_compare, container, false);
        setAutoComplete();
        setButtonListener();

        return view;
    }

    /* create handler for compare button */
    private void setButtonListener() {
        Button compareButton = view.findViewById(R.id.compareButton);
        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set new data entry with new datapoints
                chartData = new LineData();

                // get input from fields
                AutoCompleteTextView compareField1 = getActivity().findViewById(R.id.compareField1);
                AutoCompleteTextView compareField2 = getActivity().findViewById(R.id.compareField2);

                // translate input to symbol for API
                SharedPreferences storedSym = getActivity().getSharedPreferences("storedSymbol", MODE_PRIVATE);
                String symbol1 = storedSym.getString(compareField1.getText().toString(), null);
                String symbol2 = storedSym.getString(compareField2.getText().toString(), null);

                // if input is known, plot first (blue) line
                if (symbol1 != null) {
                    getPlotData(symbol1, "https://min-api.cryptocompare.com/data/histohour?fsym=", "&tsym=EUR&limit=24", 61, 122, 234);
                }// else throw error
                else {
                    Toast.makeText(getContext(), "Sorry, i don't know the first one", Toast.LENGTH_LONG).show();
                } // if input is known, plot second (orange) line
                if (symbol2 != null) {
                    getPlotData(symbol2, "https://min-api.cryptocompare.com/data/histohour?fsym=", "&tsym=EUR&limit=24", 255, 144, 64);
                } // else throw error
                else {
                    Toast.makeText(getContext(), "Sorry, i don't know the second one", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /* set auto complete suggestions from memory */
    private void setAutoComplete() {
        // get suggestions from memory
        SharedPreferences storedName = getActivity().getSharedPreferences("storedPrice", MODE_PRIVATE);

        Map<String, ?> allEntries = storedName.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            addSuggestionAutocomplete(entry.getKey());
        }

        // add suggestions to auto complete fields
        AutoCompleteTextView compareField1 = view.findViewById(R.id.compareField1);
        AutoCompleteTextView compareField2 = view.findViewById(R.id.compareField2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, autoCompleteArray);
        compareField1.setAdapter(adapter);
        compareField2.setAdapter(adapter);
    }
    /* add suggestions to auto complete memory */
    private void addSuggestionAutocomplete(String name) {
        autoCompleteArray.add(name);
    }

    /* get datapoints from the API */
    private void getPlotData(String coinName, String urlBase, final String urlParam, final int r, final int g, final int b) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // get data from API for the specific coin
        String url = urlBase + coinName + urlParam;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // add datapoints to the chartData
                        addDataSetGraph(response, r, g, b);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /* Convert data to percentages and add datapoints to the chartData */
    private void addDataSetGraph(String response, int r, int g, int b) {
        try {
            // create empty list to store datapoints
            ArrayList<Entry> entries = new ArrayList<>();

            JSONObject newObject = (JSONObject) new JSONTokener(response).nextValue();
            JSONArray dataArray = newObject.getJSONArray("Data");

            // create list to store label references
            final HashMap<Integer, String> numMap = new HashMap<>();

            // set baseline as 100% reference
            float baseline = Float.parseFloat(dataArray.getJSONObject(0).getString("high"));

            for (int i = 0; i < dataArray.length(); i++) {
                // convert UNIX time to HH:mm format
                numMap.put(i, UnixConverter.convertUnix("&tsym=EUR&limit=60", dataArray.getJSONObject(i).getString("time")));

                // calculate current percentage based on baseline and add as datapoint
                float percentage = Float.parseFloat(dataArray.getJSONObject(i).getString("high")) * 100 / baseline;
                entries.add(new Entry(i, percentage));
            }

            // set chart parameters
            LineDataSet dataset = new LineDataSet(entries, "# of Calls");
            dataset.setColor(Color.rgb(r, g, b));
            dataset.setLineWidth(4);
            dataset.setDrawValues(false);

            // add datapoints to set
            chartData.addDataSet(dataset);

            // plot line in graph
            plotGraph(numMap);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* plot lines from chart Data in graph */
    private void plotGraph(final HashMap<Integer, String> numMap) {
        LineChart lineChart = (LineChart) view.findViewById(R.id.graph);
        lineChart.setData(chartData);

        // Convert x-axis from standard labeling to HH:mm format
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return numMap.get((int) value);
            }
        });

        // set chart layout
        xAxis.setLabelRotationAngle(-50);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(getResources().getColor(R.color.textColorPrimary));
        lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.textColorPrimary));
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setExtraBottomOffset(15);
        // hide legend
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        // show chart
        lineChart.animateY(1000);
        lineChart.invalidate();

    }

}
