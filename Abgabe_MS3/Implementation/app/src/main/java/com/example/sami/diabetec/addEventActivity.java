package com.example.sami.diabetec;

import android.content.Intent;
import android.media.audiofx.AcousticEchoCanceler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class addEventActivity extends AppCompatActivity {

    private Button quitButton;

    JsonPlaceHolderApi jsonPlaceHolderApi;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        quitButton = findViewById(R.id.button_quit);
        //bei Click, MainActivity öffnen
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });


        textViewResult = findViewById(R.id.text_view_result_addEvent);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.10:3000/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        createEvent();
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void createEvent(){
        Event event = new Event("newDate", 0, 0,
                0, 0, 0, 0, 0);

        //POST auf /events
        Call<Event> call = jsonPlaceHolderApi.createEvent(event);
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                //Fehlermeldung ausgeben
                if(!response.isSuccessful()){
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                //Response ausgeben
                Event eventResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + eventResponse.getId() + "\n";
                content += "Date: " + eventResponse.getDate() + "\n";
                content += "Blutzuckerwert: " + eventResponse.getValue() + "\n";
                content += "Kohlenhydrate: " + eventResponse.getCarbohydrates() + "\n";
                content += "BEs: " + eventResponse.getBe() + "\n";
                content += "Korrektureinheiten: " + eventResponse.getCorrection() + "\n";
                content += "Mahlzeit ID: " + eventResponse.getMeal_id() + "\n";
                content += "Insulineinheiten: " + eventResponse.getInsulin_units() + "\n";
                content += "Insulinart: " + eventResponse.getInsulin_type() + "\n\n";

                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                textViewResult.setText(t.getMessage());

            }
        });
    }
}
