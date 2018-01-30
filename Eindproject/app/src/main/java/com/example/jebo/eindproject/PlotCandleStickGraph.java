package com.example.jebo.eindproject;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jessy Bosman
 * Minor Programmeren UvA
 * Programmeer Project
 */

// helper Class to plot candleStickGraph
class PlotCandleStickGraph {
    PlotCandleStickGraph(View view, JSONArray dataArray, String urlParam) {
        try {
            CandleStickChart candleStickChart = view.findViewById(R.id.candleStick);
            // create empty list to store datapoints
            ArrayList<CandleEntry> entries = new ArrayList<>();
            // create empty HashMap to store x-axis labels
            final HashMap<Integer, String> numMap = new HashMap<>();

            Log.d("response", String.valueOf(dataArray.length()));
            // Check if there actually is data to plot
            if (dataArray.length() != 0) {
                // create datapoints for candleStickGraph
                for (int i = 0; i < dataArray.length(); i++) {
                    // Convert UNIX time to format and store pos X and time label
                    numMap.put(i, UnixConverter.convertUnix(urlParam, dataArray.getJSONObject(i).getString("time")));

                    // get values
                    float high = Float.parseFloat(dataArray.getJSONObject(i).getString("high"));
                    float low = Float.parseFloat(dataArray.getJSONObject(i).getString("low"));
                    float open = Float.parseFloat(dataArray.getJSONObject(i).getString("open"));
                    float close = Float.parseFloat(dataArray.getJSONObject(i).getString("close"));
                    // add values as datapoint
                    entries.add(new CandleEntry(i, high, low, open, close));

                }
                // set graph visual appearance
                CandleDataSet cds = new CandleDataSet(entries, "Entries");
                cds.setColor(Color.rgb(80, 80, 80));
                cds.setShadowColor(Color.DKGRAY);
                cds.setShadowWidth(0.7f);
                cds.setDecreasingColor(Color.RED);
                cds.setDecreasingPaintStyle(Paint.Style.FILL);
                cds.setIncreasingColor(Color.rgb(122, 242, 84));
                cds.setIncreasingPaintStyle(Paint.Style.FILL);
                cds.setValueTextColor(R.color.textColorPrimary);

                // plot datapoints in graph
                CandleData cd = new CandleData(cds);
                candleStickChart.setData(cd);

                // Convert X-axis from incrementation (1,2,3 etc,) to timestamps (e.g. 12-1-2018, 13-1-2018)
                XAxis xAxis = candleStickChart.getXAxis();
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return numMap.get((int) value);
                    }
                });
                // set labels and modify Axes
                xAxis.setLabelRotationAngle(-50);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setTextColor(R.color.textColorPrimary);
                candleStickChart.getAxisLeft().setTextColor(R.color.textColorPrimary);
                candleStickChart.getAxisRight().setEnabled(false);
                candleStickChart.setDescription(null);

                // disable legend
                Legend legend = candleStickChart.getLegend();
                legend.setEnabled(false);

                // show visualization
                candleStickChart.animateY(1000);
                candleStickChart.invalidate();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
