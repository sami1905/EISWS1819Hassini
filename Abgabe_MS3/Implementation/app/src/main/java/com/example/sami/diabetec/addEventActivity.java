package com.example.sami.diabetec;

import android.content.Intent;
import android.media.audiofx.AcousticEchoCanceler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddEventActivity extends AppCompatActivity {

    private Button quitButton;

    JsonPlaceHolderApi jsonPlaceHolderApi;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);


        //bei Click, MainActivity öffnen
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });


        editText = findViewById(R.id.edit_Date);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.10:3000/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        editText.setText( sdf.format( new Date() ));

       // createEvent();
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void createEvent(){
        Event event = new Event("newDate", "newTime", 0, 0,
                0, 0, 0, 0, 0);

        //POST auf /events
        Call<Event> call = jsonPlaceHolderApi.postEvent(event);
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                //Fehlermeldung ausgeben
                if(!response.isSuccessful()){
                    //textViewResult.setText("Code: " + response.code());
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

               // textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                //textViewResult.setText(t.getMessage());

            }
        });
    }
}
