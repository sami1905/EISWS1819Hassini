package com.example.sami.diabetec;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.audiofx.AcousticEchoCanceler;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.widget.Toast.LENGTH_LONG;
import static java.security.AccessController.getContext;

public class AddEventActivity extends AppCompatActivity {
    private static final String TAG = "AddEventActivity";

    private EditText editTextDate;
    private EditText editTextTime;
    private EditText editTextValue;
    private EditText editTextCarbohydrates;
    private EditText editTextBe;
    private EditText editTextCorrection;
    private EditText editTextMeal;
    private EditText editTextInsulinUnits;
    private EditText editTextInsulinType;

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    private Event event;

    private UserData user;




    private Button quitButton;
    private Button saveButton;



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editTextDate = findViewById(R.id.edit_text_date);
        editTextTime = findViewById(R.id.edit_text_time);
        editTextValue = findViewById(R.id.edit_text_value);
        editTextCarbohydrates = findViewById(R.id.edit_text_carbohydrates);
        editTextBe = findViewById(R.id.edit_text_be);
        editTextCorrection = findViewById(R.id.edit_text_correction);
        editTextMeal = findViewById(R.id.edit_text_meal);
        editTextInsulinUnits = findViewById(R.id.edit_text_insulin_units);
        editTextInsulinType = findViewById(R.id.edit_text_insulin_type);

        jsonPlaceHolderApi = RestService.getRestService().create(JsonPlaceHolderApi.class);

        editTextValue.addTextChangedListener(correctionTextWatcher);
        editTextCarbohydrates.addTextChangedListener(beTextWatcher);
        editTextBe.addTextChangedListener(insulinUnitsTextWatcher);
        editTextCorrection.addTextChangedListener(insulinUnitsTextWatcher);

        getUserData();



        quitButton = findViewById(R.id.back_button);
        saveButton = findViewById(R.id.save_button);

        //bei Click, MainActivity öffnen
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
            }
        });

    }

    private TextWatcher correctionTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String valueInput = editTextValue.getText().toString().trim();
            if(!valueInput.isEmpty() && Integer.parseInt(valueInput) >= 180){
                float correction = Float.valueOf(valueInput);
                correction -= 100;
                correction = correction/user.correcturPerUnit;

                editTextCorrection.setText(Float.toString(correction));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };

    private TextWatcher beTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String carbohydrateInput = editTextCarbohydrates.getText().toString().trim();
            if(!carbohydrateInput.isEmpty()){
                float be = Float.valueOf(carbohydrateInput);
                be = be/12;

                editTextBe.setText(Float.toString(be));
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextWatcher insulinUnitsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String beInput = editTextBe.getText().toString().trim();
            String correctionInput = editTextCorrection.getText().toString().trim();
            if(!beInput.isEmpty() && !correctionInput.isEmpty()){
                float insulinUnits = Float.valueOf(correctionInput);
                float be = Float.valueOf(beInput);
                insulinUnits = insulinUnits + (be * user.beFactor);

                editTextInsulinUnits.setText(Float.toString(insulinUnits));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void saveEvent(){
        int value = 0;
        int carbohydrates = 0;
        float be = 0;
        float correction = 0;
        float insulin_units = 0;

        String date = editTextDate.getText().toString();
        String time = editTextTime.getText().toString();
        String strValue = editTextValue.getText().toString();
        String strCarbohydrates = editTextCarbohydrates.getText().toString();
        String strBe = editTextBe.getText().toString();
        String strCorrection = editTextCorrection.getText().toString();
        String meal = editTextMeal.getText().toString();
        String strInsulin_units = editTextInsulinUnits.getText().toString();
        String insulin_type = editTextInsulinType.getText().toString();

        try {
            value = Integer.parseInt(strValue);
        } catch (NumberFormatException e){
            strValue = "";
        }

        try {
            carbohydrates = Integer.parseInt(strCarbohydrates);
        } catch (NumberFormatException e){
            strCarbohydrates = "";
        }

        try {
            be = Float.valueOf(strBe);
        } catch (NumberFormatException e){
            strBe = "";
        }

        try {
            correction = Float.valueOf(strCorrection);
        } catch (NumberFormatException e){
            strCorrection = "";
        }

        try {
            insulin_units = Float.valueOf(strInsulin_units);
        } catch (NumberFormatException e){
            strInsulin_units = "";
        }

        if(date.trim().isEmpty() || time.trim().isEmpty()){
            Toast.makeText(this, "Bitte gibt Datum, Uhrzeit und mindestens einen weiteren Parameter ein!", Toast.LENGTH_LONG).show();
            return;
        }

        if(value != 0 || carbohydrates != 0
                || be != 0|| correction != 0 || !meal.trim().isEmpty() || insulin_units != 0
                || !insulin_type.trim().isEmpty()){

            Event newEvent = new Event (date + "T" + time, value, carbohydrates, be,
                    correction, meal, insulin_units, insulin_type);


            postEvent(newEvent);
            Toast.makeText(this , "Ereignis wurde erfolgreich gespeichert!", Toast.LENGTH_LONG).show();

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            openMainActivity();

        }

        else {
            Toast.makeText(this, "Bitte gibt Datum, Uhrzeit und mindestens einen weiteren Parameter ein!", Toast.LENGTH_LONG).show();
            return;
        }

    }

    private void getUserData(){
        Call<UserData> call = jsonPlaceHolderApi.getUserData();

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if(!response.isSuccessful() || response.code() == 406){
                    Toast.makeText(getBaseContext(), "FEHLER-CODE " + response.code() + ": " + "Benutzerdaten sind nicht verfügbar!", Toast.LENGTH_LONG).show();
                    return;
                }

                user = response.body();
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {

            }
        });

    }

    private void postEvent(Event event){

        Call<Event> call = jsonPlaceHolderApi.postEvent(event);

        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                if(!response.isSuccessful() || response.code() == 406){
                    Toast.makeText(getBaseContext(), "FEHLER-CODE " + response.code() + ": " + "Eingabe ist unvollständig", Toast.LENGTH_LONG).show();
                    return;
                }


            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_event:
                saveEvent();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
