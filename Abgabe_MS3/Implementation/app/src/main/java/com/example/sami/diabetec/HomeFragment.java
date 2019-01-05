package com.example.sami.diabetec;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private TextView mTextViewResult;
    private RequestQueue mQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mTextViewResult = view.findViewById(R.id.text_view_result);
        Button buttonParse = view.findViewById(R.id.button_parse);

        mQueue = Volley.newRequestQueue(getContext());

        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });

        return view;
    }

    private void jsonParse(){
        String url = "http://192.168.0.10:3000/events";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("event");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject event = jsonArray.getJSONObject(i);

                        int id = event.getInt("id");
                        String date = event.getString("date");
                        int value = event.getInt("value");
                        int carbohydrates = event.getInt("carbohydrates");
                        int be = event.getInt("be");
                        int meal_id  = event.getInt("meal_id");
                        int insulin_units = event.getInt("insulin_units");
                        int insulin_type = event.getInt("insulin_type");

                        mTextViewResult.append("Datum: " + date + "\n" + "Blutzuckerwert: " + String.valueOf(value) + "\n\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }
}
