package com.example.ancacret.rssfeed.pojo;

import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.adapters.DrawerCategoriesAdapter;
import com.example.ancacret.rssfeed.interfaces.DrawerListItem;

public class DrawerCategoryListItem extends ListViewItem implements DrawerListItem {

    private String mCategoryLink;
    private HeaderItem mItem;

    public DrawerCategoryListItem(String categoryLink, HeaderItem item) {
        mCategoryLink = categoryLink;
        mItem = item;
    }

    public String getCategoryLink() {
        return mCategoryLink;
    }

    public HeaderItem getItem() {
        return mItem;
    }

    public void setItem(HeaderItem item) {
        mItem = item;
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        DrawerViewHolder2 holder;
        if(convertView == null || convertView.getTag() instanceof DrawerViewHolder2){
            holder = new DrawerViewHolder2();
            convertView = inflater.inflate(R.layout.drawer_list_item, null);
            holder.icon = convertView.findViewById(R.id.item_icon);
            holder.name = (TextView) convertView.findViewById(R.id.itemText);
            convertView.setTag(holder);
        } else {
            holder = (DrawerViewHolder2) convertView.getTag();
        }
        holder.name.setText(mItem.getCategory());
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(100);
        drawable.setColor(mItem.getColor());
        holder.icon.setBackground(drawable);
        return convertView;
    }

    private class DrawerViewHolder2{
        TextView name;
        View icon;

        private DrawerViewHolder2() {
        }

    }

    @Override
    public int getViewType() {
        return DrawerCategoriesAdapter.ROW_TYPE.LIST_ITEM.ordinal();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
