package com.example.ancacret.rssfeed.pojo;


import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.adapters.DrawerCategoriesAdapter;
import com.example.ancacret.rssfeed.interfaces.DrawerListItem;

public class DrawerSectionHeader extends ListViewItem implements DrawerListItem {

    private String mSectionName;
    private int mColor;

    public DrawerSectionHeader(String sectionName, int color) {
        mSectionName = sectionName;
        mColor = color;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public int getColor() {
        return mColor;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        DrawerViewHolder1 holder;
        if(convertView == null || convertView.getTag() instanceof DrawerViewHolder1){
            holder = new DrawerViewHolder1();
            convertView = inflater.inflate(R.layout.row_item_separator, null);
            holder.separator = (TextView) convertView.findViewById(R.id.separator_title);
            convertView.setTag(holder);
        } else {
            holder = (DrawerViewHolder1) convertView.getTag();
        }
        holder.separator.setText(mSectionName);
        holder.separator.setTextColor(mColor);
        return convertView;
    }

    private class DrawerViewHolder1 {
        TextView separator;

        private DrawerViewHolder1() {
        }
    }

    @Override
    public int getViewType() {
        return DrawerCategoriesAdapter.ROW_TYPE.HEADER_ITEM.ordinal();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }


}
