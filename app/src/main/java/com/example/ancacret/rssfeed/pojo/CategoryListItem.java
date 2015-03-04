package com.example.ancacret.rssfeed.pojo;


import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.adapters.CategoriesAdapter;
import com.squareup.picasso.Picasso;

import java.util.Comparator;

public class CategoryListItem extends ListViewItem implements Parcelable {

    private RSSItem mRSSItem;
    private Context mContext;
    Typeface typeface, typefaceTitle;

    public CategoryListItem(RSSItem rssItem, Context context) {
        this.mRSSItem = rssItem;
        mContext = context;
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/RobotoCondensed-Light.ttf");
        typefaceTitle = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
    }

    public CategoryListItem(Parcel source){
        mRSSItem = source.readParcelable(RSSItem.class.getClassLoader());
    }

    public RSSItem getRSSItem() {
        return mRSSItem;
    }

    public void setRSSItem(RSSItem RSSItem) {
        mRSSItem = RSSItem;
    }

    @Override
    public int getViewType() {
        return CategoriesAdapter.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        ViewHolder holder;
        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_item, null);
            holder.icon = (ImageView)convertView.findViewById(R.id.enclosure);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.title = (TextView) convertView.findViewById(R.id.feedTitle);
            holder.pubDate = (TextView) convertView.findViewById(R.id.publish_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setTypeface(typefaceTitle);
        holder.title.setText(mRSSItem.getTitle());
        holder.description.setTypeface(typeface);
        String description = cutExtraSpaces(mRSSItem.getDescription());
        holder.description.setText(cutUnnecessaryText(description));
        holder.pubDate.setText("updated on "+ mRSSItem.getPubDate());

        String urlPic = mRSSItem.getEnclosureUrl();
        Picasso picasso = Picasso.with(mContext);
        picasso.setIndicatorsEnabled(true);
        picasso.setDebugging(true);
        picasso.setLoggingEnabled(true);
        picasso.load(urlPic).into(holder.icon);

        return convertView;
    }

    private class ViewHolder {
        private ImageView icon;
        private TextView title, description, pubDate;

        private ViewHolder() {
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mRSSItem, flags);
    }

    public static final Creator<CategoryListItem> CREATOR = new Creator<CategoryListItem>() {
        @Override
        public CategoryListItem createFromParcel(Parcel source) {
            CategoryListItem listItem = new CategoryListItem(source);
            return listItem;
        }

        @Override
        public CategoryListItem[] newArray(int size) {
            return new CategoryListItem[0];
        }
    };


    private String cutExtraSpaces(String text) {
        String newText = text.trim().replace(" +", "");
        return newText;
    }

    private String cutUnnecessaryText(String str) {
        int i = str.indexOf("<br/><br/><a href=\"http://core.ad20.net");
        if (i > 0) {
            return str.substring(0, i);
        }
        return str;
    }


}
