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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

        // Get variables from bundle, to obtain scores from last game.
        Bundle bundle = this.getArguments();

        // Check if bundle exists.
        if (bundle != null) {
            String coinObject = bundle.getString("coinObject", "");
            try {
                selectedCoin = new JSONObject(coinObject);
                getPlotData(selectedCoin.getString("symbol"),"https://min-api.cryptocompare.com/data/histominute?fsym=","&tsym=EUR&limit=60");
                fillDataFields(view, selectedCoin);
            }catch (JSONException e) {
                Toast.makeText(getContext(), "cannot find coin data", Toast.LENGTH_SHORT).show();
            }
        }
        setToolbar(view);

        return view;
    }

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

    private void getPlotData( String coinName, String urlBase, final String urlParam) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // get data from API for the specific coin
        String url = urlBase+coinName+urlParam;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        plotSimpleGraph(view, response, urlParam);
                        plotCandleStick(view, response, urlParam);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void plotSimpleGraph(View view, String response, String urlParam){
        try {
            LineChart lineChart = (LineChart) view.findViewById(R.id.graph);
            ArrayList<Entry> entries = new ArrayList<>();

            JSONObject newObject = (JSONObject) new JSONTokener(response).nextValue();
            JSONArray dataArray = newObject.getJSONArray("Data");

            final HashMap<Integer, String> numMap = new HashMap<>();

            for (int i = 0; i < dataArray.length(); i++) {

                long UNIX = Long.parseLong(dataArray.getJSONObject(i).getString("time"))*1000;

                if(urlParam.equals("&tsym=EUR&limit=60")){
                    String format = new SimpleDateFormat("hh:mm").format(new java.util.Date(UNIX));
                    numMap.put(i,format);
                }
                else if (urlParam.equals("&tsym=EUR&limit=12")){
                    String format = new SimpleDateFormat("hh:mm").format(new java.util.Date(UNIX));
                    numMap.put(i,format);
                }
                else if (urlParam.equals("&tsym=EUR&limit=24")){
                    String format = new SimpleDateFormat("hh:mm").format(new java.util.Date(UNIX));
                    numMap.put(i,format);
                }
                else if (urlParam.equals("&tsym=EUR&limit=7")){
                    String format = new SimpleDateFormat("dd/MM").format(new java.util.Date(UNIX));
                    numMap.put(i,format);
                }

                entries.add(new Entry(i, Float.parseFloat(dataArray.getJSONObject(i).getString("high"))));

            }

            LineDataSet dataset = new LineDataSet(entries, "# of Calls");
            dataset.setColor(Color.rgb(117, 206, 255));
            dataset.setLineWidth(4);
            dataset.setDrawValues(false);

            LineData data = new LineData(dataset);
            lineChart.setData(data);

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return numMap.get((int)value);
                    }
            });

            xAxis.setLabelRotationAngle(-50);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            lineChart.getAxisRight().setEnabled(false);

            Legend legend = lineChart.getLegend();
            legend.setEnabled(false);

            lineChart.animateY(1000);
            lineChart.invalidate();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void plotCandleStick(View view, String response, String urlParam){
        try {
            CandleStickChart candleStickChart = view.findViewById(R.id.candleStick);

            JSONObject newObject = (JSONObject) new JSONTokener(response).nextValue();
            JSONArray dataArray = newObject.getJSONArray("Data");

            ArrayList<CandleEntry> entries = new ArrayList<>();
            final HashMap<Integer, String> numMap = new HashMap<>();

            for (int i = 0; i < dataArray.length(); i++) {
                long UNIX = Long.parseLong(dataArray.getJSONObject(i).getString("time"))*1000;

                if(urlParam.equals("&tsym=EUR&limit=60")){
                    String format = new SimpleDateFormat("hh:mm").format(new java.util.Date(UNIX));
                    numMap.put(i,format);
                }
                else if (urlParam.equals("&tsym=EUR&limit=12")){
                    String format = new SimpleDateFormat("hh:mm").format(new java.util.Date(UNIX));
                    numMap.put(i,format);
                }
                else if (urlParam.equals("&tsym=EUR&limit=24")){
                    String format = new SimpleDateFormat("hh:mm").format(new java.util.Date(UNIX));
                    numMap.put(i,format);
                }
                else if (urlParam.equals("&tsym=EUR&limit=7")){
                    String format = new SimpleDateFormat("dd/MM").format(new java.util.Date(UNIX));
                    numMap.put(i,format);
                }

                float high = Float.parseFloat(dataArray.getJSONObject(i).getString("high"));
                float low = Float.parseFloat(dataArray.getJSONObject(i).getString("low"));
                float open = Float.parseFloat(dataArray.getJSONObject(i).getString("open"));
                float close = Float.parseFloat(dataArray.getJSONObject(i).getString("close"));

                entries.add(new CandleEntry(i, high, low, open, close));

            }

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
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return numMap.get((int)value);
                }
            });
            xAxis.setLabelRotationAngle(-50);
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
            valueBtc.setText("btc " + selectedCoin.getString("price_btc"));

            change1h.setText(selectedCoin.getString("percent_change_1h") + "% (1h)");
            try{
                if(Float.parseFloat(selectedCoin.get("percent_change_1h").toString()) < 0.0){
                    change1h.setTextColor(0xffff4444);
                }
                else{change1h.setTextColor(0xff669900);}
            } catch (Exception e){Log.d("ParseError",e.toString());}

            change24h.setText(selectedCoin.getString("percent_change_24h") + "% (1d)");
            try{
                if(Float.parseFloat(selectedCoin.get("percent_change_24h").toString()) < 0.0){
                    change24h.setTextColor(0xffff4444);
                }
                else{change24h.setTextColor(0xff669900);}
            } catch (Exception e){Log.d("ParseError",e.toString());}

            change7d.setText(selectedCoin.getString("percent_change_7d") + "% (7d)");
            try{
                if(Float.parseFloat(selectedCoin.get("percent_change_7d").toString()) < 0.0){
                    change7d.setTextColor(0xffff4444);
                }
                else{change7d.setTextColor(0xff669900);}
            } catch (Exception e){Log.d("ParseError",e.toString());}

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

    @Override
    public void onClick(View v) {
        final CandleStickChart candleStickChart = view.findViewById(R.id.candleStick);
        final LineChart simpleChart = view.findViewById(R.id.graph);
        try{
            String symbolSelected = selectedCoin.getString("symbol");

        switch (v.getId()) {
            case R.id.toggleLinechart:
                buttonLinechart.setChecked(true);
                buttonCandlestick.setChecked(false);
                candleStickChart.setVisibility(view.INVISIBLE);
                simpleChart.setVisibility(view.VISIBLE);
                break;

            case R.id.toggleCandlestick:
                buttonLinechart.setChecked(false);
                buttonCandlestick.setChecked(true);
                candleStickChart.setVisibility(view.VISIBLE);
                simpleChart.setVisibility(view.INVISIBLE);
                break;

            case R.id.toggle1H:
                    button1H.setChecked(true);
                    button12H.setChecked(false);
                    button1D.setChecked(false);
                    button7D.setChecked(false);

                getPlotData(symbolSelected ,"https://min-api.cryptocompare.com/data/histominute?fsym=","&tsym=EUR&limit=60");

                    break;

            case R.id.toggle12H:
                button1H.setChecked(false);
                button12H.setChecked(true);
                button1D.setChecked(false);
                button7D.setChecked(false);

                getPlotData(symbolSelected,"https://min-api.cryptocompare.com/data/histohour?fsym=","&tsym=EUR&limit=12");

                break;

            case R.id.toggle1D:
                button1H.setChecked(false);
                button12H.setChecked(false);
                button1D.setChecked(true);
                button7D.setChecked(false);

                getPlotData(symbolSelected,"https://min-api.cryptocompare.com/data/histohour?fsym=","&tsym=EUR&limit=24");

                break;

            case R.id.toggle7D:
                button1H.setChecked(false);
                button12H.setChecked(false);
                button1D.setChecked(false);
                button7D.setChecked(true);

                getPlotData(symbolSelected,"https://min-api.cryptocompare.com/data/histoday?fsym=","&tsym=EUR&limit=7");

                break;
            }
        } catch (JSONException e) {
        e.printStackTrace();
        Log.d("JSONException",e.toString());

        }
    }

}
