package com.example.ancacret.rssfeed.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.ancacret.rssfeed.interfaces.DrawerListItem;

import java.util.List;

public class DrawerCategoriesAdapter extends ArrayAdapter<DrawerListItem>  {

    private LayoutInflater mLayoutInflater;

    public enum ROW_TYPE{
        LIST_ITEM, HEADER_ITEM
    }

    public DrawerCategoriesAdapter(Context context, List<DrawerListItem> drawerListItems) {
        super(context, 0, drawerListItems);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return ROW_TYPE.values().length;
    }

    @Override
    public DrawerListItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(mLayoutInflater, convertView);
    }
}
