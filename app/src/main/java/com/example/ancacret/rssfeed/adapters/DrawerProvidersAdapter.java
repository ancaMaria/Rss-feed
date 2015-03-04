package com.example.ancacret.rssfeed.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.pojo.DrawerSectionHeader;
import com.example.ancacret.rssfeed.pojo.HeaderItem;

import java.util.ArrayList;
import java.util.List;


public class DrawerProvidersAdapter extends BaseAdapter {

    private Context mContext;
    private List<DrawerSectionHeader> mDrawersList = new ArrayList<DrawerSectionHeader>();

    public DrawerProvidersAdapter(Context context) {
        mContext = context;
    }

    public void updateList(List<DrawerSectionHeader> drawersList){
        this.mDrawersList = drawersList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDrawersList.size();
    }

    @Override
    public DrawerSectionHeader getItem(int position) {
        return mDrawersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row_item_separator, null);
            //holder.icon =  convertView.findViewById(R.id.item_icon);
            holder.textView = (TextView)convertView.findViewById(R.id.separator_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(getItem(position).getSectionName());
        holder.textView.setTextColor(getItem(position).getColor());
        holder.textView.setBackgroundColor(mContext.getResources().getColor(R.color.grey_300));
        return convertView;
    }

    private class ViewHolder{
        TextView textView;
        View icon;

        private ViewHolder() {
        }
    }
}
