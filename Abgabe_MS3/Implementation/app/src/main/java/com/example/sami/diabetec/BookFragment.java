package com.example.sami.diabetec;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BookFragment extends Fragment {

    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        textViewResult = view.findViewById(R.id.text_view_result);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.10:3000/").
                addConverterFactory(GsonConverterFactory.create()).build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getEvents();



        return view;
    }

    private void getEvents(){
        Call<List<Event>> call = jsonPlaceHolderApi.getEvents();

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {

                if(!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Event> events = response.body();

                for (Event event : events) {
                    String content = "";
                    //content += "ID: " + event.getId() + "\n";
                    content += "Date: " + event.getDate() + "\n";
                    //content += "Time: " + event.getTime() + "\n";
                    if(event.getValue() != 0){
                        content += "Blutzuckerwert: " + event.getValue() + "\n";
                    }

                    if(event.getCarbohydrates() != 0){
                        content += "Kohlenhydrate: " + event.getCarbohydrates() + "\n";
                    }

                    if(event.getBe() != 0){
                        content += "BEs: " + event.getBe() + "\n";
                    }

                    if(event.getCorrection() != 0){
                        content += "Korrektureinheiten: " + event.getCorrection() + "\n";
                    }

                    //if(event.getMeal_id() != 0){
                       // content += "Mahlzeit: " + event.getMeal_id() + "\n";
                    //}

                    if(event.getInsulin_units() != 0){
                        content += "Insulineinheiten: " + event.getInsulin_units() + "\n";
                    }

                    //if(event.getInsulin_type() != 0) {
                        //content += "Insulinart: " + event.getInsulin_type() + "\n";
                   // }

                    content += "_________________________________________\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });

    }


}
