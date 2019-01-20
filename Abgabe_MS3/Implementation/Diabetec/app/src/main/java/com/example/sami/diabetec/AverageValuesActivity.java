package com.example.sami.diabetec;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AverageValuesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();

    private Spinner spinnerNumberOfDays;
    private TextView textViewNValue;
    private TextView textViewLowestValue;
    private TextView textViewHighestValue;
    private TextView textViewAverageValue;
    private TextView textViewHbA1c;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_values);

        calendar.add(Calendar.DATE, -7);

        String dateForSearch = dateFormat.format(calendar.getTime()).toString();

        spinnerNumberOfDays = findViewById(R.id.spinner_number_of_days);
        textViewNValue = findViewById(R.id.text_view_numberOfBZ);
        textViewLowestValue = findViewById(R.id.text_view_lowest);
        textViewHighestValue = findViewById(R.id.text_view_highest);
        textViewAverageValue = findViewById(R.id.text_view_averageBZ);
        textViewHbA1c = findViewById(R.id.text_view_hba1c_report);
        jsonPlaceHolderApi = RestService.getRestService().create(JsonPlaceHolderApi.class);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numberOfDays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumberOfDays.setAdapter(adapter);
        spinnerNumberOfDays.setOnItemSelectedListener(this);

        getStatics(dateForSearch);

        calendar.add(Calendar.DATE, + 7);



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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if(position == 0 ){
            calendar.add(Calendar.DATE, -7);
            String dateForSearch = dateFormat.format(calendar.getTime()).toString();
            getStatics(dateForSearch);
            calendar.add(Calendar.DATE, +7);

        }

        else if(position == 1 ){
            calendar.add(Calendar.DATE, -14);
            String dateForSearch = dateFormat.format(calendar.getTime()).toString();
            getStatics(dateForSearch);
            calendar.add(Calendar.DATE, +14);

        }

        else if(position == 2 ){
            calendar.add(Calendar.DATE, -30);
            String dateForSearch = dateFormat.format(calendar.getTime()).toString();
            getStatics(dateForSearch);
            calendar.add(Calendar.DATE, +30);

        }

        if(position == 3 ){

            getStatics("0");

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getStatics(String date){
        Call<List<Statics>> call = jsonPlaceHolderApi.getStatics(date);

        call.enqueue(new Callback<List<Statics>>() {
            @Override
            public void onResponse(Call<List<Statics>> call, Response<List<Statics>> response) {
                if(!response.isSuccessful() || response.code() == 500){
                    Toast.makeText(getBaseContext(), "FEHLER-CODE " + response.code() + ": " + "'HbA1C' & 'Durchschnittlicher Blutzuckerwert' nicht verfügbar", Toast.LENGTH_LONG).show();
                    return;
                }

                List<Statics> currentStatics = response.body();
                Statics[] statics = new  Statics[currentStatics.size()];
                statics = currentStatics.toArray(statics);


                float hba1c = (statics[0].getHba1c());
                int averageValue = floatIntoInt(statics[0].getAverageValue());
                DecimalFormat df = new DecimalFormat("#.##");
                textViewNValue.setText(Integer.toString(statics[0].getnValues()));
                textViewLowestValue.setText(Integer.toString(statics[0].getMinValue()) + " mg/dl\n");
                textViewHighestValue.setText(Integer.toString(statics[0].getMaxValue()) + " mg/dl\n");
                textViewHbA1c.setText(df.format(hba1c) + " %\n");
                textViewAverageValue.setText(averageValue + " mg/dl\n");

            }

            @Override
            public void onFailure(Call<List<Statics>> call, Throwable t) {
                // Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
