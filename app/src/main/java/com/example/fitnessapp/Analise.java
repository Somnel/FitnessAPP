package com.example.fitnessapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Analise extends AppCompatActivity {

    private final List<String> xValues = Arrays.asList("Maths", "Science", "English", "IT");

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analise);
        init();
    }

    private void init() {
        BarChart analiseChart = findViewById(R.id.analise_chart);
        analiseChart.getAxisRight().setDrawLabels(false);




        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 45f));
        entries.add(new BarEntry(1, 80f));
        entries.add(new BarEntry(2, 68f));
        entries.add(new BarEntry(3, 38f));

        YAxis yAxis = analiseChart.getAxisLeft();
        yAxis.setAxisMaximum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);

        BarDataSet dataSet = new BarDataSet(entries, "Subjects");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
        analiseChart.setData(barData);

        analiseChart.getDescription().setEnabled(true);
        analiseChart.invalidate();



        analiseChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        analiseChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        analiseChart.getXAxis().setGranularity(1f);
        analiseChart.getXAxis().setGranularityEnabled(true);
    }
}