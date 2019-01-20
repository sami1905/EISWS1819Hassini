package com.example.sami.diabetec;

public class ExampleItem {

    private String title;
    private String description;

    public ExampleItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void changeTitle(String text){
        title = text;
    }
}
