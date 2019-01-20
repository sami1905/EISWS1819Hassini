package com.example.sami.diabetec;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    SimpleDateFormat dot = new SimpleDateFormat("yyyy-MM-dd");
    String dateOfToday = dot.format(new Date());

    private LineChart mChart;
    private Button addButton;
    private Button dexcomButton;

    private NestedScrollView textViewHome;
    private TextView textViewLastBZ;
    private TextView textViewMinValue;
    private TextView textViewMaxValue;
    private TextView textViewAverageValue;
    private TextView textViewTimeWithinRange;
    private TextView textViewHbA1c;
    private TextView textViewAverageValueSinceDay1;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    //Value hinzufügen
    ArrayList<Entry> yValues = new ArrayList<>();
    int[] dv = new int[24];
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    ArrayList<ILineDataSet> todayDataSets = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        mChart = view.findViewById(R.id.lineChart_home);

        //Chart anlegen
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setGridBackgroundColor(Color.rgb(127,255,0));

        //show first mChart
        for (int i = 0; i < dv.length; ++i)
        {
            dv[i] = 500;
        }

        //LimitLine anlegen
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

        //Y-Achse einstellen
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upper_limit);
        leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaximum(400f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 0f, 0f);
        leftAxis.setDrawLimitLinesBehindData(true);

        //X-Achse einstellen
        mChart.getAxisRight().setEnabled(false);
        XAxis xAxis = mChart.getXAxis();
        String[] values = new String[] {"00h", "1h", "2h", "3h", "4h", "5h", "6h", "7h", "8h",
                "9h", "10h", "11h", "12h", "13h", "14h", "15h", "16h", "17h", "18h", "19h", "20h",
                "21h", "22h", "23h", "00h"};
        xAxis.setValueFormatter(new MyXAxisValues(values));
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        for(int i = 0; i < dv.length; i++){
            yValues.add(new Entry(i, dv[i]));
        }

        //Graphen erstellen
        LineDataSet firstSet = new LineDataSet(yValues, " ");
        firstSet.setFillAlpha(110);
        firstSet.setColor(Color.TRANSPARENT);
        firstSet.setValueTextColor(Color.TRANSPARENT);
        firstSet.setCircleColor(Color.BLACK);
        firstSet.setCircleRadius(3f);
        firstSet.setCircleHoleRadius(3f);
        dataSets.add(firstSet);
        LineData data = new LineData(dataSets);
        mChart.setData(data);

        addButton = view.findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddEventActivity();

            }
        });

        dexcomButton = view.findViewById(R.id.dexcom_button);

        dexcomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAuthorization();

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                postDexcomValues();

                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                postValues();

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();

            }
        });




        textViewHome = view.findViewById(R.id.text_view_home);
        textViewLastBZ = view.findViewById(R.id.text_view_lastBZ);
        textViewMinValue = view.findViewById(R.id.text_view_minValue);
        textViewMaxValue = view.findViewById(R.id.text_view_maxValue);
        textViewAverageValue = view.findViewById(R.id.text_view_averageValue);
        textViewTimeWithinRange = view.findViewById(R.id.text_view_timeInRange);
        textViewHbA1c = view.findViewById(R.id.text_view_hba1c);
        textViewAverageValueSinceDay1 = view.findViewById(R.id.text_view_averageValue2);

        jsonPlaceHolderApi = RestService.getRestService().create(JsonPlaceHolderApi.class);


        getValuesFromDate();
        getValuesInPercent();
        getStatics();






        return view;
    }


    //Value für X-Achse
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

    public float getTimeToFloat(String time){

        float flt= 0f;
        String hours = time.substring(0, 2);
        float minutes = Float.parseFloat(time.substring(3, 5));

        minutes = ((minutes/60.0f)*100.0f);

        if (minutes < 10){
            hours = hours + "." + Float.toString(minutes).substring(0,1);
        }

        else hours = hours + "." + Float.toString(minutes).substring(0,2);


        flt = Float.parseFloat(hours);

        return  flt;
    };

    public void openAddEventActivity(){
        Intent intent = new Intent(getContext(), AddEventActivity.class);
        startActivity(intent);
    }

    public int floatIntoInt(float flt){
        int a = (int) flt;
        flt = flt - a;
        if(flt < 0.5){
            flt = 0;
        }
        else if (flt > 0.4){
            flt = 1;
        }

        int rt = a + (int) flt;
        return rt;
    }

    private void getValuesFromDate(){

        Call<List<Value>> call = jsonPlaceHolderApi.getValuesFromDate(dateOfToday);

        call.enqueue(new Callback<List<Value>>() {

            @Override
            public void onResponse(Call<List<Value>> call, Response<List<Value>> response) {

                if(!response.isSuccessful() || response.code() == 500){
                    Toast.makeText(getContext(), "FEHLER-CODE " + response.code() + ": " + "Blutzuckerwerte nicht verfügbar", Toast.LENGTH_LONG).show();
                    return;
                }

                List<Value> userValues = response.body();

                yValues.clear();

                Value[] userValuesArr = new Value[userValues.size()];
                userValuesArr = userValues.toArray(userValuesArr);
                int averageValue = 0;
                int nValues = 0;


                for (int i = 0; i < userValuesArr.length; i++){
                    yValues.add(new Entry(getTimeToFloat(userValuesArr[i].getTime()), userValuesArr[i].getValue()));

                    averageValue += userValuesArr[i].getValue();
                    nValues++;
                }

                yValues.add(new Entry(24, 500));

                //Graphen erstellen
                LineDataSet todaySet = new LineDataSet(yValues, "");
                todaySet.setFillAlpha(110);
                todaySet.setColor(Color.TRANSPARENT);
                todaySet.setValueTextColor(Color.TRANSPARENT);
                todaySet.setCircleColor(Color.BLACK);
                todaySet.setCircleRadius(3f);
                todaySet.setCircleHoleRadius(3f);
                todayDataSets.add(todaySet);
                LineData todayData = new LineData(todayDataSets);
                mChart.setData(todayData);

                Value lastValue = userValues.get(userValues.size()-1);
                Value lowestValue = userValuesArr[0];
                Value highestValue = userValuesArr[0];

                for (int i = 0; i < userValuesArr.length; i++){
                    if(lowestValue.getValue() > userValuesArr[i].getValue()){
                        lowestValue = userValuesArr[i];
                    }
                    if(highestValue.getValue() < userValuesArr[i].getValue()){
                        highestValue = userValuesArr[i];
                    }


                }

                textViewLastBZ.setText(lastValue.getTime().substring(0,5) + " Uhr:               "+ lastValue.getValue() + " mg/dl");
                textViewMinValue.setText(lowestValue.getTime().substring(0,5) + " Uhr:               "+lowestValue.getValue() + " mg/dl");
                textViewMaxValue.setText(highestValue.getTime().substring(0,5) + " Uhr:               "+ highestValue.getValue() + " mg/dl");
                textViewAverageValue.setText((averageValue/nValues) + " mg/dl");




            }
            @Override
            public void onFailure(Call<List<Value>> call, Throwable t) {
               // Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }



        });


    }

    private void postValues(){
        Call<Value> call = jsonPlaceHolderApi.postValues();

        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {



            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {

                //Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void getValuesInPercent(){
        Call<List<ValuesInPercent>> call = jsonPlaceHolderApi.getValuesInPercent("0");

        call.enqueue(new Callback<List<ValuesInPercent>>() {
            @Override
            public void onResponse(Call<List<ValuesInPercent>> call, Response<List<ValuesInPercent>> response) {

                if(!response.isSuccessful() || response.code() == 500){
                    Toast.makeText(getContext(), "FEHLER-CODE " + response.code() + ": " + "'Zeit im Zielbereich' nicht verfügbar", Toast.LENGTH_LONG).show();
                    return;
                }

                List<ValuesInPercent> currentResponse = response.body();
                ValuesInPercent[] valuesInPercents = new ValuesInPercent[currentResponse.size()];
                valuesInPercents = currentResponse.toArray(valuesInPercents);


                int veryLowInPercent = floatIntoInt((valuesInPercents[0].getPercentVeryLow() * 100));
                int lowInPercent = floatIntoInt((valuesInPercents[0].getPercentLow() * 100));
                int withinRangeInPercent = floatIntoInt((valuesInPercents[0].getPercentWithinRange() * 100));
                int highInPercent = floatIntoInt((valuesInPercents[0].getPercentHigh() * 100));

                textViewTimeWithinRange.setText("< 55 mg/dl:    " + Integer.toString(veryLowInPercent) + " %\n" +
                        "55 mg/dl - 80 mg/dl:   " + Integer.toString(lowInPercent) + " %\n" + "80 mg/dl - 180 mg/dl:    " +
                        Integer.toString(withinRangeInPercent) + " %\n" +
                        "> 180 mg/dl:   " + Integer.toString(highInPercent) + " %\n");




            }

            @Override
            public void onFailure(Call<List<ValuesInPercent>> call, Throwable t) {
                //Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getStatics(){
        Call<List<Statics>> call = jsonPlaceHolderApi.getStatics("0");

        call.enqueue(new Callback<List<Statics>>() {
            @Override
            public void onResponse(Call<List<Statics>> call, Response<List<Statics>> response) {
                if(!response.isSuccessful() || response.code() == 500){
                    Toast.makeText(getContext(), "FEHLER-CODE " + response.code() + ": " + "'HbA1C' & 'Durchschnittlicher Blutzuckerwert' nicht verfügbar", Toast.LENGTH_LONG).show();
                    return;
                }

                List<Statics> currentStatics = response.body();
                Statics[] statics = new  Statics[currentStatics.size()];
                statics = currentStatics.toArray(statics);

                float hba1c = (statics[0].getHba1c());
                int averageValue = floatIntoInt(statics[0].getAverageValue());
                DecimalFormat df = new DecimalFormat("#.##");
                textViewHbA1c.setText(df.format(hba1c) + " %\n");

                textViewAverageValueSinceDay1.setText(averageValue + " mg/dl\n");
            }

            @Override
            public void onFailure(Call<List<Statics>> call, Throwable t) {
               // Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void postAuthorization(){

        Call<Authorization> call = jsonPlaceHolderApi.postAuthorization();

        call.enqueue(new Callback<Authorization>(){
            @Override
            public void onResponse(Call<Authorization> call, Response<Authorization> response) {

            }

            @Override
            public void onFailure(Call<Authorization> call, Throwable t) {

            }
        });

    }

    private void postDexcomValues(){

        SimpleDateFormat dot2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dateOfToday2 = dot2.format(new Date());


        Call<DexcomValues> call = jsonPlaceHolderApi.postDexcomValues(dateOfToday2);

        call.enqueue(new Callback<DexcomValues>() {
            @Override
            public void onResponse(Call<DexcomValues> call, Response<DexcomValues> response) {

            }

            @Override
            public void onFailure(Call<DexcomValues> call, Throwable t) {

            }
        });{

        }

    }


}

