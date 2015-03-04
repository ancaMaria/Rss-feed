package com.example.ancacret.rssfeed.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.ancacret.rssfeed.pojo.ListViewItem;

import java.util.ArrayList;
import java.util.List;


/* used for both Drawer list and main content */
public class CategoriesAdapter extends ArrayAdapter<ListViewItem> {

    private LayoutInflater mLayoutInflater;
    private List<ListViewItem> mListViewItems = new ArrayList<ListViewItem>();

    public enum RowType{
        LIST_ITEM, HEADER_ITEM
    }

    public CategoriesAdapter(Context context, List<ListViewItem> listViewItems){
        super(context, 0, listViewItems);
        mLayoutInflater = LayoutInflater.from(context);
        mListViewItems = listViewItems;
    }

    public void updateList(List<ListViewItem> listViewItems){
        mListViewItems = listViewItems;
        notifyDataSetChanged();
    }


    @Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(mLayoutInflater, convertView);
    }


}
