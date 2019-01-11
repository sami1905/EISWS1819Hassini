package com.example.sami.diabetec;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private LineChart mChart;

    private Button dexcomButton;
    private Button addEventButton;
    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    //Values hinzufügen
    ArrayList<Entry> yValues = new ArrayList<>();

    int[] dv = new int[288];

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    String date = sdf.format(new Date());



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dexcomButton = view.findViewById(R.id.button_dexcom);
        addEventButton = view.findViewById(R.id.button_addEvent);
        textViewResult = view.findViewById(R.id.text_view_resultHome);
        mChart = view.findViewById(R.id.lineChart);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.10:3000/").
                addConverterFactory(GsonConverterFactory.create()).build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        //Chart anlegen
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setGridBackgroundColor(Color.rgb(127,255,0));


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
        String[] values = new String[] {"00h", "1h", "2h", "3h", "4h", "5h", "6h", "7h", "8h", "9h", "10h", "11h", "12h", "13h", "14h", "15h", "16h", "17h", "18h", "19h", "20h", "21h", "22h", "23h", "00h"};
        xAxis.setValueFormatter(new MyXAxisValues(values));
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);



        createAuthorization();


        //DexcomApi bei clicken auf Button
        dexcomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                createDexcomValues();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getDexcomValues();

            }
        });

        //AddEventActivity bei clicken auf Button öffnen
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddEventActivity();
            }
        });



        return view;
    }


    //Values für X-Achse
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

    public void openAddEventActivity(){
        Intent intent = new Intent(getActivity(), addEventActivity.class);
        startActivity(intent);
    }

    private void getDexcomValues(){
        Call<List<DexcomValues>> call = jsonPlaceHolderApi.getDexcomValues();

        call.enqueue(new Callback<List<DexcomValues>>() {





            @Override
            public void onResponse(Call<List<DexcomValues>> call, Response<List<DexcomValues>> response) {

                if(!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<DexcomValues> dexcomValues = response.body();
                int i = 0;
                float x = 1.0f;

                for (DexcomValues dexcomValue : dexcomValues) {

                    String content = "";
                    content += "Date: " + dexcomValue.getDate() + "\n";
                    content += "Blutzuckerwert: " + dexcomValue.getValue() + "\n\n";

                    dv[i] = dexcomValue.getValue();

                    textViewResult.append(content);

                    yValues.add(new Entry(x, dv[i++]));

                    x = x + 0.08333f;




                    //Graphen erstellen
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


                }
            }
            @Override
            public void onFailure(Call<List<DexcomValues>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }



        });


    }

    private void createAuthorization(){

        Call<Authorization> call = jsonPlaceHolderApi.createAuthorization();

        call.enqueue(new Callback<Authorization>(){
            @Override
            public void onResponse(Call<Authorization> call, Response<Authorization> response) {

            }

            @Override
            public void onFailure(Call<Authorization> call, Throwable t) {

            }
        });

    }

    private void createDexcomValues(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String date = sdf.format(new Date());



        Call<DexcomValues> call = jsonPlaceHolderApi.createDexcomValues(date);

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
