package com.example.ancacret.rssfeed.interfaces;


import android.view.LayoutInflater;
import android.view.View;

public interface DrawerListItem {

    public abstract View getView(LayoutInflater inflater, View convertView);
    public abstract int getViewType();

    /*public boolean isSectionHeader();
    public boolean isListItem();*/
}
