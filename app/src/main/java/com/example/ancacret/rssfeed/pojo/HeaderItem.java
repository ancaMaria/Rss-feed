package com.example.ancacret.rssfeed.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.adapters.CategoriesAdapter;
import com.example.ancacret.rssfeed.interfaces.DrawerListItem;

/* used as header for the main content list
 * used as item for drawer's providers list
  * */
public class HeaderItem extends ListViewItem implements Parcelable, DrawerListItem {

    private String category;
    private int color;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public static Creator<HeaderItem> CREATOR = new Creator<HeaderItem>() {
        @Override
        public HeaderItem createFromParcel(Parcel source) {
            return new HeaderItem(source);
        }

        @Override
        public HeaderItem[] newArray(int size) {
            return new HeaderItem[0];
        }
    };

    public HeaderItem(String name, int color) {
        this.category = name;
        this.color = color;
    }

    public HeaderItem(Parcel source) {
        category = source.readString();
        color = source.readInt();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        ViewHolder1 holder1;
        if(convertView == null || !(convertView.getTag() instanceof ViewHolder1) ){
            holder1 = new ViewHolder1();
            convertView = inflater.inflate(R.layout.row_item_separator, null);
            holder1.separator = (TextView) convertView.findViewById(R.id.separator_title);
            convertView.setTag(holder1);
        }
        holder1 = (ViewHolder1) convertView.getTag();
        holder1.separator.setText(category);
        holder1.separator.setBackgroundColor(color);
        return convertView;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeInt(color);
    }

    private class ViewHolder1{
        TextView separator;

        private ViewHolder1() {
        }
    }

    @Override
    public int getViewType() {
        return CategoriesAdapter.RowType.HEADER_ITEM.ordinal();
    }


}
