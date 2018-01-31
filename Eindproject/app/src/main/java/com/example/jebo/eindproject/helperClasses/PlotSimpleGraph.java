package com.example.jebo.eindproject.helperClasses;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.example.jebo.eindproject.R;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jessy Bosman
 * Minor Programmeren UvA
 * Programmeer Project
 */


// helper Class to plot simpleGraph
public class PlotSimpleGraph {
    public PlotSimpleGraph(View view, JSONArray dataArray, String urlParam) {
        try {
            LineChart lineChart = (LineChart) view.findViewById(R.id.graph);
            // create empty list to store datapoints
            ArrayList<Entry> entries = new ArrayList<>();
            // create empty HashMap to store x-axis labels
            final HashMap<Integer, String> numMap = new HashMap<>();

            Log.d("response", String.valueOf(dataArray.length()));

            // Check if there actually is data to plot
            if (dataArray.length() != 0) {
                for (int i = 0; i < dataArray.length(); i++) {
                    // Convert UNIX time to format and store pos X and time label
                    numMap.put(i, UnixConverter.convertUnix(urlParam, dataArray.getJSONObject(i).getString("time")));
                    // add values as datapoint
                    entries.add(new Entry(i, Float.parseFloat(dataArray.getJSONObject(i).getString("high"))));

                }
                // set graph visual appearance
                LineDataSet dataset = new LineDataSet(entries, "# of Calls");
                dataset.setColor(Color.rgb(117, 206, 255));
                dataset.setLineWidth(4);
                dataset.setDrawValues(false);

                // plot datapoints in graph
                LineData data = new LineData(dataset);
                lineChart.setData(data);

                // Convert X-axis from incrementation (1,2,3 etc,) to timestamps (e.g. 12-1-2018, 13-1-2018)
                XAxis xAxis = lineChart.getXAxis();
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
                lineChart.getAxisLeft().setTextColor(R.color.textColorPrimary);
                lineChart.getAxisRight().setEnabled(false);
                lineChart.setExtraBottomOffset(15);
                lineChart.setDescription(null);

                // disable legend
                Legend legend = lineChart.getLegend();
                legend.setEnabled(false);

                // show visualization
                lineChart.animateY(1000);
                lineChart.invalidate();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
