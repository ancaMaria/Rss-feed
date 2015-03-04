package com.example.ancacret.rssfeed.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.pojo.HeaderItem;

import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    /* providers names */
    //private List<DrawerListItem> mainElements;

    /* map with provider name and a map that has the urls and the category names */
    private Map<String, List<HeaderItem> > mCollections;
    private List<String> mProviders;


    public ExpandableListAdapter(Context context, Map<String, List<HeaderItem>> collections, List<String> providers) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mCollections = collections;
        mProviders = providers;
    }

    @Override
    public int getGroupCount() {
        return mProviders.size();
    }

    /* # of children in a group */
    @Override
    public int getChildrenCount(int groupPosition) {
       return mCollections.get(mProviders.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mProviders.get(groupPosition);
    }

    /*  */
    @Override
    public HeaderItem getChild(int groupPosition, int childPosition) {
        return mCollections.get(mProviders.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String providerName = (String) getGroup(groupPosition);
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.row_item_separator, null);
            final TextView separator = (TextView) convertView.findViewById(R.id.separator_title);
            separator.setText(providerName);
        } else {

        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        HeaderItem categoryName = getChild(groupPosition, childPosition);

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            final View icon = convertView.findViewById(R.id.item_icon);
            final TextView category = (TextView) convertView.findViewById(R.id.itemText);
            if(categoryName != null){
                category.setText(categoryName.getCategory());
            }
            if(icon != null){
                icon.setBackgroundColor(categoryName.getColor());
            }
        }


        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
