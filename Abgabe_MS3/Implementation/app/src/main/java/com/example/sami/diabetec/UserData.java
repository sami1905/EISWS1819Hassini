package com.example.sami.diabetec;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("name")
    String name;

    @SerializedName("firstname")
    String firstname;

    @SerializedName("age")
    int age;

    @SerializedName("correcturPerUnit")
    int correcturPerUnit;

    @SerializedName("beFactor")
    float beFactor;

    public String getName() {
        return name;
    }

    public String getFirstname() {
        return firstname;
    }

    public int getAge() {
        return age;
    }

    public int getCorrecturPerUnit() {
        return correcturPerUnit;
    }

    public float getBeFactor() {
        return beFactor;
    }

    public UserData(String name, String firstname, int age, int correcturPerUnit, float beFactor) {
        this.name = name;
        this.firstname = firstname;
        this.age = age;
        this.correcturPerUnit = correcturPerUnit;
        this.beFactor = beFactor;
    }
}
