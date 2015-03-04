package com.example.ancacret.rssfeed.communication;

/**
 * Created by anca.cret on 2/19/2015.
 */
public class EventNoStoredData {

    private boolean dataIsStored;

    public EventNoStoredData(boolean dataIsStored) {
        this.dataIsStored = dataIsStored;
    }

    public boolean isDataIsStored() {
        return dataIsStored;
    }
}
