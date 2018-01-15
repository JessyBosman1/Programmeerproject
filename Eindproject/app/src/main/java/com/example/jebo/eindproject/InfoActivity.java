package com.example.jebo.eindproject;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class InfoActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_info, container, false);

        // Get variables from bundle, to obtain scores from last game.
        Bundle bundle = this.getArguments();

        // Check if bundle exists.
        if (bundle != null) {
            String coinObject = bundle.getString("coinObject", "");
            try {
                JSONObject selectedCoin = new JSONObject(coinObject);
                getPlotData(view, selectedCoin.getString("symbol"));
                fillDataFields(view, selectedCoin);
            }catch (JSONException e) {
                Toast.makeText(getContext(), "cannot find coin data", Toast.LENGTH_SHORT).show();
            }
        }
        setToolbar(view);
        setGraphSwitch(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getPlotData(final View view, String coinName) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // get data from API for the specific coin
        String url = "https://min-api.cryptocompare.com/data/histoday?fsym="+coinName+"&tsym=USD&limit=20";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        plotCandleStick(view, response);
                        plotSimpleGraph(view, response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void plotSimpleGraph(View view, String response){
        try {
            JSONArray dataArray;

            GraphView graph = getActivity().findViewById(R.id.graph);

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

    private void plotCandleStick(View view, String response){
        try {
            JSONArray dataArray;
            CandleStickChart candleStickChart = view.findViewById(R.id.candleStick);

            JSONObject newObject = (JSONObject) new JSONTokener(response).nextValue();
            dataArray = newObject.getJSONArray("Data");

            ArrayList<CandleEntry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<String>();
            for (int i = 0; i < dataArray.length(); i++) {
                //int X = Integer.parseInt(dataArray.getJSONObject(i).getString("time"));
                float high = Float.parseFloat(dataArray.getJSONObject(i).getString("high"));
                float low = Float.parseFloat(dataArray.getJSONObject(i).getString("low"));
                float open = Float.parseFloat(dataArray.getJSONObject(i).getString("open"));
                float close = Float.parseFloat(dataArray.getJSONObject(i).getString("close"));

                entries.add(new CandleEntry(i, high, low, open, close));
                labels.add(dataArray.getJSONObject(i).getString("time"));
            }
            /*
            entries.add(new CandleEntry(0, 4.62f, 2.02f, 2.70f, 4.13f));
            entries.add(new CandleEntry(1, 5.50f, 2.70f, 3.35f, 4.96f));
            entries.add(new CandleEntry(2, 5.25f, 3.02f, 3.50f, 4.50f));
            entries.add(new CandleEntry(3, 6f,    3.25f, 4.40f, 5.0f));
            entries.add(new CandleEntry(4, 5.57f, 2f,    2.80f, 4.5f));
            */

            CandleDataSet cds = new CandleDataSet(entries, "Entries");
            cds.setColor(Color.rgb(80, 80, 80));
            cds.setShadowColor(Color.DKGRAY);
            cds.setShadowWidth(0.7f);
            cds.setDecreasingColor(Color.RED);
            cds.setDecreasingPaintStyle(Paint.Style.FILL);
            cds.setIncreasingColor(Color.rgb(122, 242, 84));
            cds.setIncreasingPaintStyle(Paint.Style.FILL);
            cds.setValueTextColor(Color.BLACK);
            CandleData cd = new CandleData(cds);

            candleStickChart.setData(cd);

            XAxis xAxis = candleStickChart.getXAxis();
            xAxis.setLabelRotationAngle(-90);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            candleStickChart.getAxisRight().setEnabled(false);

            Legend legend = candleStickChart.getLegend();
            legend.setEnabled(false);

            candleStickChart.animateY(1000);
            candleStickChart.invalidate();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillDataFields(View view, JSONObject selectedCoin) {
        TextView title = view.findViewById(R.id.title);
        TextView valueEur = view.findViewById(R.id.valueEur);
        TextView valueBtc = view.findViewById(R.id.valueBtc);
        TextView change1h = view.findViewById(R.id.change1h);
        TextView change24h = view.findViewById(R.id.change24h);
        TextView change7d = view.findViewById(R.id.change7d);

        ImageView imageView = view.findViewById(R.id.icon);
        try{
            title.setText(selectedCoin.getString("name"));
            valueEur.setText("€" + selectedCoin.getString("price_eur"));
            valueBtc.setText(selectedCoin.getString("price_btc"));

            change1h.setText(selectedCoin.getString("percent_change_1h") + "%");
            change24h.setText(selectedCoin.getString("percent_change_24h") + "%");

            change7d.setText(selectedCoin.getString("percent_change_7d") + "%");

            Glide.with(this.getContext())
                    .load("https://files.coinmarketcap.com/static/img/coins/64x64/" + selectedCoin.get("id").toString() + ".png")
                    .into(imageView);
        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONException",e.toString());
        }
    }

    private void setToolbar(View view){

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ;
            }
        });

    }

    private void setGraphSwitch(final View view){
        final Switch graphSwitch = view.findViewById(R.id.graphSwitch);
        final CandleStickChart candleStickChart = view.findViewById(R.id.candleStick);
        final GraphView simpleChart = view.findViewById(R.id.graph);

        graphSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   graphSwitch.setText("Advanced");
                   candleStickChart.setVisibility(view.VISIBLE);
                   simpleChart.setVisibility(view.INVISIBLE);
               }
               else{
                   graphSwitch.setText("Simple");
                   candleStickChart.setVisibility(view.INVISIBLE);
                   simpleChart.setVisibility(view.VISIBLE);
               }
            }
        });

    }
}
