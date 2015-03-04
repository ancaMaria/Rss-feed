package com.example.ancacret.rssfeed.communication;


public class StoredCategoriesEvent {

    private String message;

    public StoredCategoriesEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
