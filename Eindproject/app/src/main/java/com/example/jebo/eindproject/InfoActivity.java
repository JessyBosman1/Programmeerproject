package com.example.jebo.eindproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class InfoActivity extends Fragment implements View.OnClickListener {
    private ToggleButton button1H;
    private ToggleButton button12H;
    private ToggleButton button1D;
    private ToggleButton button7D;
    private ToggleButton buttonLinechart;
    private ToggleButton buttonCandlestick;
    private JSONObject selectedCoin;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_info, container, false);
        ToggleListeners(view);

        // Get selected object from bundle
        Bundle bundle = this.getArguments();

        // Check if bundle exists.
        if (bundle != null) {
            String coinObject = bundle.getString("coinObject", "");
            try {
                selectedCoin = new JSONObject(coinObject);
                getPlotData(selectedCoin.getString("symbol"), "https://min-api.cryptocompare.com/data/histominute?fsym=", "&tsym=EUR&limit=60");
                fillDataFields(view, selectedCoin);
            } catch (JSONException e) {
                Log.d("JSONException", e.toString());
                Toast.makeText(getContext(), "cannot find coin data", Toast.LENGTH_SHORT).show();
            }
        }
        return view;
    }

    /* set listeners for the different toggle buttons */
    private void ToggleListeners(View view) {
        buttonLinechart = view.findViewById(R.id.toggleLinechart);
        buttonCandlestick = view.findViewById(R.id.toggleCandlestick);
        buttonLinechart.setOnClickListener(this);
        buttonCandlestick.setOnClickListener(this);

        button1H = view.findViewById(R.id.toggle1H);
        button12H = view.findViewById(R.id.toggle12H);
        button1D = view.findViewById(R.id.toggle1D);
        button7D = view.findViewById(R.id.toggle7D);
        button1H.setOnClickListener(this);
        button12H.setOnClickListener(this);
        button1D.setOnClickListener(this);
        button7D.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getPlotData(String coinName, String urlBase, final String urlParam) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // get data from API for the specific coin
        String url = urlBase + coinName + urlParam;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            JSONObject newObject = (JSONObject) new JSONTokener(response).nextValue();
                            JSONArray dataArray = newObject.getJSONArray("Data");

                            // plot the graphs with the data from the API
                            new PlotSimpleGraph(view, dataArray, urlParam);
                            new PlotCandleStickGraph(view, dataArray, urlParam);
                        } catch (JSONException e) {
                            Log.d("JSONException", e.toString());
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError", error.toString());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /* fill the data field with data from the stored object */
    private void fillDataFields(View view, JSONObject selectedCoin) {
        // get references
        TextView title = view.findViewById(R.id.title);
        TextView valueEur = view.findViewById(R.id.valueEur);
        TextView valueBtc = view.findViewById(R.id.valueBtc);
        TextView change1h = view.findViewById(R.id.change1h);
        TextView change24h = view.findViewById(R.id.change24h);
        TextView change7d = view.findViewById(R.id.change7d);
        ImageView imageView = view.findViewById(R.id.icon);

        try {
            // fill in the placeholders
            title.setText(selectedCoin.getString("name"));
            valueEur.setText("€" + selectedCoin.getString("price_eur"));
            valueBtc.setText("btc " + selectedCoin.getString("price_btc"));

            // change text color according to positive or negative percentage
            change1h.setText(selectedCoin.getString("percent_change_1h") + "% (1h)");
            try {
                if (Float.parseFloat(selectedCoin.get("percent_change_1h").toString()) < 0.0) {
                    change1h.setTextColor(0xffff4444);
                } else {
                    change1h.setTextColor(0xff669900);
                }
            } catch (Exception e) {
                Log.d("ParseError", e.toString());
            }

            // change text color according to positive or negative percentage
            change24h.setText(selectedCoin.getString("percent_change_24h") + "% (1d)");
            try {
                if (Float.parseFloat(selectedCoin.get("percent_change_24h").toString()) < 0.0) {
                    change24h.setTextColor(0xffff4444);
                } else {
                    change24h.setTextColor(0xff669900);
                }
            } catch (Exception e) {
                Log.d("ParseError", e.toString());
            }

            // change text color according to positive or negative percentage
            change7d.setText(selectedCoin.getString("percent_change_7d") + "% (7d)");
            try {
                if (Float.parseFloat(selectedCoin.get("percent_change_7d").toString()) < 0.0) {
                    change7d.setTextColor(0xffff4444);
                } else {
                    change7d.setTextColor(0xff669900);
                }
            } catch (Exception e) {
                Log.d("ParseError", e.toString());
            }

            // load icon with Glide
            Glide.with(this.getContext())
                    .load("https://files.coinmarketcap.com/static/img/coins/64x64/" + selectedCoin.get("id").toString() + ".png")
                    .into(imageView);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONException", e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        // handle toggle buttons
        final CandleStickChart candleStickChart = view.findViewById(R.id.candleStick);
        final LineChart simpleChart = view.findViewById(R.id.graph);
        try {
            String symbolSelected = selectedCoin.getString("symbol");
            switch (v.getId()) {
                // show linechart and change button
                case R.id.toggleLinechart:
                    buttonLinechart.setChecked(true);
                    buttonCandlestick.setChecked(false);
                    candleStickChart.setVisibility(view.INVISIBLE);
                    simpleChart.setVisibility(view.VISIBLE);
                    break;

                // show candleStick chart and change button
                case R.id.toggleCandlestick:
                    buttonLinechart.setChecked(false);
                    buttonCandlestick.setChecked(true);
                    candleStickChart.setVisibility(view.VISIBLE);
                    simpleChart.setVisibility(view.INVISIBLE);
                    break;

                // change toggle buttons and show corresponding graph
                case R.id.toggle1H:
                    button1H.setChecked(true);
                    button12H.setChecked(false);
                    button1D.setChecked(false);
                    button7D.setChecked(false);

                    getPlotData(symbolSelected, "https://min-api.cryptocompare.com/data/histominute?fsym=", "&tsym=EUR&limit=60");
                    break;
                // change toggle buttons and show corresponding graph
                case R.id.toggle12H:
                    button1H.setChecked(false);
                    button12H.setChecked(true);
                    button1D.setChecked(false);
                    button7D.setChecked(false);

                    getPlotData(symbolSelected, "https://min-api.cryptocompare.com/data/histohour?fsym=", "&tsym=EUR&limit=12");
                    break;
                // change toggle buttons and show corresponding graph
                case R.id.toggle1D:
                    button1H.setChecked(false);
                    button12H.setChecked(false);
                    button1D.setChecked(true);
                    button7D.setChecked(false);

                    getPlotData(symbolSelected, "https://min-api.cryptocompare.com/data/histohour?fsym=", "&tsym=EUR&limit=24");
                    break;
                // change toggle buttons and show corresponding graph
                case R.id.toggle7D:
                    button1H.setChecked(false);
                    button12H.setChecked(false);
                    button1D.setChecked(false);
                    button7D.setChecked(true);

                    getPlotData(symbolSelected, "https://min-api.cryptocompare.com/data/histoday?fsym=", "&tsym=EUR&limit=7");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONException", e.toString());

        }
    }

}
