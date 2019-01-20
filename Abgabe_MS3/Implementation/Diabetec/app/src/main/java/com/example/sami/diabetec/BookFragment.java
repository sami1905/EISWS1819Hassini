package com.example.sami.diabetec;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookFragment extends Fragment {

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    SimpleDateFormat dot = new SimpleDateFormat("yyyy-MM-dd");
    String dateOfToday = dot.format(new Date());

    private ArrayList<String> dates = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private LineChart mChart;
    private int nValues = 2000;
    private int averageValue = 120;
    private int veryLowValues = 1;
    private int lowValues = 9;
    private int inRangeValues = 81;
    private int highValues = 9;
    private String strEvents = "\n";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        jsonPlaceHolderApi = RestService.getRestService().create(JsonPlaceHolderApi.class);



        mRecyclerView = view.findViewById(R.id.recycler_view_book);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());

        getEventFromDate();
        getValues();


        return view;
    }

    private void getEventFromDate()
    {
        Call<List<Event>> call = jsonPlaceHolderApi.getEventFromDate(dateOfToday);

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {

                if(!response.isSuccessful() || response.code() == 500){
                    Toast.makeText(getContext(), "FEHLER-CODE " + response.code() + ": " + "Ereignisse nicht verfügbar", Toast.LENGTH_LONG).show();
                    return;
                }

                List<Event> events = response.body();

                for(Event event: events){
                    strEvents += event.getTime() + " Uhr \n";
                    strEvents += Integer.toString(event.getValue()) + " mg/dl \n";
                    strEvents += Integer.toString(event.getCarbohydrates()) + " g Kohlenhydrate \n";
                    strEvents += Float.valueOf(event.getBe()) + " BEs \n";
                    strEvents += Float.valueOf(event.getCorrection()) + " Korrektureinheiten \n";
                    strEvents += "Mahlzeit: " + event.getMeal_id() + "\n";
                    strEvents += Float.valueOf(event.getInsulin_units()) + " Insulineinheiten \n";
                    strEvents += "Insulinart: " + event.getInsulin_type() + "\n";
                    strEvents += "______________________ \n\n";
                }


            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {

            }
        });
    }

    private void getValues(){

        Call<List<Value>> call = jsonPlaceHolderApi.getValues();

        call.enqueue(new Callback<List<Value>>() {
            @Override
            public void onResponse(Call<List<Value>> call, Response<List<Value>> response) {

                if(!response.isSuccessful() || response.code() == 500){
                    Toast.makeText(getContext(), "FEHLER-CODE " + response.code() + ": " + "Blutzuckerwerte nicht verfügbar", Toast.LENGTH_LONG).show();
                    return;
                }

                List<Value> userValues = response.body();
                Value[] userValuesArr = new Value[userValues.size()];
                userValuesArr = userValues.toArray(userValuesArr);

                dates.add(userValuesArr[0].getDate());
                boolean availableDate = false;

                for(int i = 0; i < userValuesArr.length; i++){
                    for(int x = 0; x < dates.size(); x++){
                        if(dates.get(x).contains(userValuesArr[i].getDate())){
                            availableDate = true;
                        }
                    }
                    if(availableDate == false){
                        dates.add(userValuesArr[i].getDate());
                    }
                    else availableDate = false;
                }





                ArrayList<BookItem> bookList = new ArrayList<>();
                for(int i = dates.size()-1; i >= 0; i--){
                    bookList.add(new BookItem(dates.get(i), mChart, nValues, averageValue, veryLowValues, lowValues, inRangeValues, highValues, strEvents));

                }


                mAdapter = new BookAdapter(bookList);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);







            }

            @Override
            public void onFailure(Call<List<Value>> call, Throwable t) {

            }
        });

    }
}

