package com.example.sami.diabetec;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private LineChart mChart;

    private Button dexcomButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dexcomButton = view.findViewById(R.id.button_dexcom);

        dexcomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.dexcom.com/v2/oauth2/login?client_id=LiIbbgsBtr7VqpYkNBveXaOs9vzdnGtw&redirect_uri=http://schemas.android.com/apk/res/android&response_type=code&scope=offline_access"));
                startActivity(browserIntent);
            }
        });
        mChart = view.findViewById(R.id.lineChart);

        //mChart.setOnChartGestureListener(HomeFragment.this);
        //mChart.setOnChartValueSelectedListener(HomeFragment.this);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setGridBackgroundColor(Color.rgb(127,255,0));



        LimitLine upper_limit = new LimitLine(180f, " ");
        upper_limit.setLineWidth(1f);
        upper_limit.enableDashedLine(10f, 0f, 0f);
        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upper_limit.setLineColor(Color.rgb(255, 165, 0));


        LimitLine lower_limit = new LimitLine(70f, " ");
        lower_limit.setLineWidth(1f);
        lower_limit.enableDashedLine(10f, 0f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setLineColor(Color.rgb(165, 42, 42));

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upper_limit);
        leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaximum(400f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 0f, 0f);
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);


        ArrayList<Entry> yValues = new ArrayList<>();


        yValues.add(new Entry(0, 25f));
        yValues.add(new Entry(1, 75f));
        yValues.add(new Entry((float) 1.2, 75f));
        yValues.add(new Entry(2, 30f));
        yValues.add(new Entry(3, 65f));
        yValues.add(new Entry(4, 25f));
        yValues.add(new Entry(5, 75f));
        yValues.add(new Entry(6, 30f));
        yValues.add(new Entry(7, 65f));
        yValues.add(new Entry(8, 25f));
        yValues.add(new Entry(9, 75f));
        yValues.add(new Entry(10, 30f));
        yValues.add(new Entry(11, 65f));
        yValues.add(new Entry(12, 25f));

        yValues.add(new Entry(22, 30f));
        yValues.add(new Entry(23, 65f));
        yValues.add(new Entry(24, 65f));

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

        String[] values = new String[] {"00h", "1h", "2h", "3h", "4h", "5h", "6h", "7h", "8h", "9h", "10h", "11h", "12h", "13h", "14h", "15h", "16h", "17h", "18h", "19h", "20h", "21h", "22h", "23h", "00h"};

        XAxis xAxis = mChart.getXAxis();
        // xAxis.setAxisMinimum(0f);
        //xAxis.setAxisMaximum(24f);
        //xAxis.setAxisMaximum(24f);
        xAxis.setValueFormatter(new MyXAxisValues(values));
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        return view;
    }

    public class MyXAxisValues implements IAxisValueFormatter{
        private String[] mValues;
        public MyXAxisValues(String[] values){
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int)value];
        }
    }


}
