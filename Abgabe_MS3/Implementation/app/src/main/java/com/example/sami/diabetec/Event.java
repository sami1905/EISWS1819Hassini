package com.example.sami.diabetec;

import com.google.gson.annotations.SerializedName;

public class Event {

    @SerializedName("id")
    private Integer id;

    @SerializedName("date")
    private String date;

    @SerializedName("value")
    private int value;

    @SerializedName("carbohydrates")
    private int carbohydrates;

    @SerializedName("be")
    private float be;

    @SerializedName("correction")
    private float correction;

    @SerializedName("meal_id")
    private String meal_id;

    @SerializedName("insulin_units")
    private float insulin_units;

    @SerializedName("insulin_type")
    private String insulin_type;


    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getValue() {
        return value;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public float getBe() {
        return be;
    }

    public float getCorrection() {
        return correction;
    }

    public String getMeal_id() {
        return meal_id;
    }

    public float getInsulin_units() {
        return insulin_units;
    }

    public String getInsulin_type() {
        return insulin_type;
    }

    public Event(String date, int value, int carbohydrates, float be, float correction, String meal_id, float insulin_units, String insulin_type) {
        this.date = date;
        this.value = value;
        this.carbohydrates = carbohydrates;
        this.be = be;
        this.correction = correction;
        this.meal_id = meal_id;
        this.insulin_units = insulin_units;
        this.insulin_type = insulin_type;
    }
}
