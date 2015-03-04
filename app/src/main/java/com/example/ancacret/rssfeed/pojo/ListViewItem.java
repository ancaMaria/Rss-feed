package com.example.ancacret.rssfeed.pojo;


import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;

public abstract class ListViewItem implements Parcelable{

    public abstract View getView(LayoutInflater inflater, View convertView);
    public abstract int getViewType();
}


/*public interface ListViewItem {

    public View getView(LayoutInflater inflater, View convertView);
    public int getViewType();
}*/
