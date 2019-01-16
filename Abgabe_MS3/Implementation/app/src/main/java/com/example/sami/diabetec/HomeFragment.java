package com.example.sami.diabetec;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

    private LineChart mChart;

    private Button dexcomButton;
    private TextView textView;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    //Value hinzufügen
    ArrayList<Entry> yValues = new ArrayList<>();
    ArrayList<Entry> yValues2 = new ArrayList<>();
    int[] dv = new int[288];
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    ArrayList<ILineDataSet> todayDataSets = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mChart = view.findViewById(R.id.lineChart);

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

        float firstXValues = 0f;
        for(int i = 0; i < dv.length-1; i++){
            yValues.add(new Entry(firstXValues, dv[i]));
            firstXValues += 0.08333f;
        }
        yValues.add(new Entry(24, dv[dv.length-1]));

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

        dexcomButton = view.findViewById(R.id.button_dexcom);
        textView = view.findViewById(R.id.text_view_result_home);

        jsonPlaceHolderApi = RestService.getRestService().create(JsonPlaceHolderApi.class);

        //postAuthorization();
        //postDexcomValues();
        //postValues();
        getValuesFromDate();


        //DexcomApi bei clicken auf Button
        dexcomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //createDexcomValues();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //getDexcomValues();

            }
        });



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
        Intent intent = new Intent(getActivity(), AddEventActivity.class);
        startActivity(intent);
    }

    private void getValuesFromDate(){
        SimpleDateFormat dot = new SimpleDateFormat("yyyy-MM-dd");
        String dateOfToday = dot.format(new Date());
        String test = "2019-01-15";

        Call<List<Value>> call = jsonPlaceHolderApi.getValuesFromDate(test);

        call.enqueue(new Callback<List<Value>>() {

            @Override
            public void onResponse(Call<List<Value>> call, Response<List<Value>> response) {

                if(!response.isSuccessful()){
                    textView.setText("Code: " + response.code());
                    return;
                }

                List<Value> userValues = response.body();

                for (Value value : userValues) {
                    String content = "";
                    content += "Date: " + value.getDate() + "\n";
                    content += "Blutzuckerwert: " + value.getValue() + "\n\n";
                    textView.append(content);
                }

                Value[] userValuesArr = new Value[userValues.size()];
                userValuesArr = userValues.toArray(userValuesArr);

                for (int i = 0; i < userValuesArr.length; i++){
                    yValues2.add(new Entry(getTimeToFloat(userValuesArr[i].getTime()), userValuesArr[i].getValue()));
                }

                yValues2.add(new Entry(24, 500));

                //Graphen erstellen
                LineDataSet todaySet = new LineDataSet(yValues2, "Heutige Blutzuckerwerte");
                todaySet.setFillAlpha(110);
                todaySet.setColor(Color.TRANSPARENT);
                todaySet.setValueTextColor(Color.TRANSPARENT);
                todaySet.setCircleColor(Color.BLACK);
                todaySet.setCircleRadius(3f);
                todaySet.setCircleHoleRadius(3f);
                todayDataSets.add(todaySet);
                LineData todayData = new LineData(todayDataSets);
                mChart.setData(todayData);


            }
            @Override
            public void onFailure(Call<List<Value>> call, Throwable t) {
                textView.setText(t.getMessage());
            }



        });


    }

    private void createAuthorization(){

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

    /*private void createDexcomValues(){




        Call<DexcomValues> call = jsonPlaceHolderApi.postDexcomValues(date);

        call.enqueue(new Callback<DexcomValues>() {
            @Override
            public void onResponse(Call<DexcomValues> call, Response<DexcomValues> response) {

            }

            @Override
            public void onFailure(Call<DexcomValues> call, Throwable t) {

            }
        });{

        }

    }*/


}
