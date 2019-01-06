package com.example.sami.diabetec;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private LineChart mChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mChart = view.findViewById(R.id.lineChart);

        //mChart.setOnChartGestureListener(HomeFragment.this);
        //mChart.setOnChartValueSelectedListener(HomeFragment.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setVisibleYRangeMaximum(400,YAxis.AxisDependency.LEFT );
        mChart.setVisibleXRangeMaximum(24);
        mChart.setVisibleXRangeMinimum(24);

        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0, 460f));
        yValues.add(new Entry(1.2f, 50f));
        yValues.add(new Entry(2, 50f));
        yValues.add(new Entry(3, 25f));
        yValues.add(new Entry(4, 75f));
        yValues.add(new Entry(5, 30f));
        yValues.add(new Entry(6, 65f));

        LineDataSet set1 = new LineDataSet(yValues, " ");

        set1.setFillAlpha(110);
        set1.setColor(Color.TRANSPARENT);
        set1.setValueTextColor(Color.TRANSPARENT);
        set1.setCircleColor(Color.BLACK);
        set1.setCircleRadius(3f);
        set1.setCircleHoleRadius(3f);



        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);

        mChart.setData(data );
        return view;
    }
}
