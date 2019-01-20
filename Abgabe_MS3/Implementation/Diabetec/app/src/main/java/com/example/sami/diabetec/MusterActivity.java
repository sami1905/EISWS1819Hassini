package com.example.sami.diabetec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusterActivity extends AppCompatActivity {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    private TextView textViewLow;

    List<Value> valuesToday = new ArrayList<Value>();
    List<Value> valuesTodayMinusOne = new ArrayList<Value>();
    List<Value> valuesTodayMinusTwo = new ArrayList<Value>();
    List<Value> valuesTodayMinusThree = new ArrayList<Value>();
    List<Value> valuesTodayMinusFour = new ArrayList<Value>();
    List<Value> valuesTodayMinusFive = new ArrayList<Value>();
    List<Value> valuesTodayMinusSix = new ArrayList<Value>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muster);



        jsonPlaceHolderApi = RestService.getRestService().create(JsonPlaceHolderApi.class);
        textViewLow = findViewById(R.id.text_view_muster);

        getValuesHigh();


    }

    private void getValuesLow(){
        Call<List<Value>> call = jsonPlaceHolderApi.getValuesLow("2019-01-13");

        call.enqueue(new Callback<List<Value>>() {
            @Override
            public void onResponse(Call<List<Value>> call, Response<List<Value>> response) {
                List<Value> userValues = response.body();

                //Value[] lowValues = new Value[userValues.size()];
                //lowValues = userValues.toArray(lowValues);

                for(Value lowvalue : userValues){
                    String content = "";
                    content += "Date: " + lowvalue.getDate() + "\n";
                    content += "Time: " + lowvalue.getTime() + "\n";
                    content += "Value: " + lowvalue.getValue() + "\n\n";

                    textViewLow.append(content);
                }


            }

            @Override
            public void onFailure(Call<List<Value>> call, Throwable t) {

            }
        });

    }
    private void getValuesHigh(){
        Call<List<Value>> call = jsonPlaceHolderApi.getValuesHigh("2019-01-13");

        call.enqueue(new Callback<List<Value>>() {
            @Override
            public void onResponse(Call<List<Value>> call, Response<List<Value>> response) {
                List<Value> userValues = response.body();

                //Value[] lowValues = new Value[userValues.size()];
                //lowValues = userValues.toArray(lowValues);


                String currentDate = dateFormat.format(calendar.getTime()).toString();

                for(int i = 0; i < userValues.size(); i++){
                    if(userValues.get(i).getDate().contains(currentDate)){
                        valuesToday.add(userValues.get(i));
                    }
                }

                calendar.add(Calendar.DATE, -1);

                currentDate = dateFormat.format(calendar.getTime()).toString();

                for(int i = 0; i < userValues.size(); i++){
                    if(userValues.get(i).getDate().contains(currentDate)){
                        valuesTodayMinusOne.add(userValues.get(i));
                    }
                }

                calendar.add(Calendar.DATE, -1);

                currentDate = dateFormat.format(calendar.getTime()).toString();

                for(int i = 0; i < userValues.size(); i++){
                    if(userValues.get(i).getDate().contains(currentDate)){
                        valuesTodayMinusTwo.add(userValues.get(i));
                    }
                }

                calendar.add(Calendar.DATE, -1);

                currentDate = dateFormat.format(calendar.getTime()).toString();

                for(int i = 0; i < userValues.size(); i++){
                    if(userValues.get(i).getDate().contains(currentDate)){
                        valuesTodayMinusThree.add(userValues.get(i));
                    }
                }

                calendar.add(Calendar.DATE, -1);

                currentDate = dateFormat.format(calendar.getTime()).toString();

                for(int i = 0; i < userValues.size(); i++){
                    if(userValues.get(i).getDate().contains(currentDate)){
                        valuesTodayMinusFour.add(userValues.get(i));
                    }
                }

                calendar.add(Calendar.DATE, -1);

                currentDate = dateFormat.format(calendar.getTime()).toString();

                for(int i = 0; i < userValues.size(); i++){
                    if(userValues.get(i).getDate().contains(currentDate)){
                        valuesTodayMinusFive.add(userValues.get(i));
                    }
                }

                calendar.add(Calendar.DATE, -1);

                currentDate = dateFormat.format(calendar.getTime()).toString();

                for(int i = 0; i < userValues.size(); i++){
                    if(userValues.get(i).getDate().contains(currentDate)){
                        valuesTodayMinusSix.add(userValues.get(i));
                    }
                }



                for(Value highValue : valuesTodayMinusSix){
                    String content = "";
                    content += "Date: " + highValue.getDate() + "\n";
                    content += "Time: " + highValue.getTime() + "\n";
                    content += "Value: " + highValue.getValue() + "\n\n";

                    textViewLow.append(content);
                }


            }

            @Override
            public void onFailure(Call<List<Value>> call, Throwable t) {

            }
        });

    }
}
