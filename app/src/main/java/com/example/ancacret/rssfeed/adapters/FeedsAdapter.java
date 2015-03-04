package com.example.ancacret.rssfeed.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.pojo.RSSItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class FeedsAdapter extends BaseAdapter {

    private Context mContext;
    private List<RSSItem> mRSSItems = new ArrayList<RSSItem>();
    Typeface typeface, typefaceTitle;

    public FeedsAdapter(Context context) {
        mContext = context;
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/RobotoCondensed-Light.ttf");
        typefaceTitle = Typeface.createFromAsset(mContext.getAssets(), "fonts/Roboto-Regular.ttf");
    }

    public void updateList(List<RSSItem> rssItems) {
        this.mRSSItems = rssItems;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mRSSItems.size();
    }

    @Override
    public RSSItem getItem(int position) {
        return mRSSItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.row_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.enclosure);
            holder.title = (TextView) convertView.findViewById(R.id.feedTitle);
            holder.content = (TextView) convertView.findViewById(R.id.description);
            holder.pubDate = (TextView) convertView.findViewById(R.id.publish_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setTypeface(typefaceTitle);
        holder.title.setText(getItem(position).getTitle());
        holder.content.setTypeface(typeface);
        String description = cutUnnecessaryText(getItem(position).getDescription());
        holder.content.setText(cutExtraSpaces(description));
        holder.pubDate.setText(getItem(position).getPubDate());

        String picUrl = getItem(position).getEnclosureUrl();
        /* check first if image was cached to memory .. */

        Picasso picasso = Picasso.with(mContext);
        picasso.setIndicatorsEnabled(true);
        picasso.setDebugging(true);
        picasso.setLoggingEnabled(true);
        picasso.load(picUrl).into(holder.icon);

        /*int memClass, totalAppheap;
        memClass = ((ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE)).getLargeMemoryClass();
        totalAppheap = 1024 * 1024 * memClass;
        Log.v(Constants.TAG_LOG, "memory size --------- " + memClass);
        Log.v(Constants.TAG_LOG, "totalAppheap --------- " + totalAppheap);
        Picasso picasso = new Picasso.Builder(mContext)
                .memoryCache(new LruCache(totalAppheap/4))
                .build();
        picasso.setIndicatorsEnabled(true);
        picasso.setDebugging(true);
        picasso.setLoggingEnabled(true);
        picasso.load(picUrl).into(holder.icon);
*/


       /* final ImageView imageView = holder.icon;
        imageView.setTag(picUrl);
        imageView.setImageResource(R.drawable.cat);
        new AsyncGetImage(picUrl){
            @Override
            protected void onPostExecute(Drawable drawable) {
                super.onPostExecute(drawable);

                if ((imageView.getTag() != null) &&
                        (imageView.getTag().toString().equals(getPicUrl())))
                    imageView.setImageDrawable(drawable);
            }
        }.execute();*/

        return convertView;
    }



    private class ViewHolder {
        private ImageView icon;
        private TextView title, content, pubDate;

        private ViewHolder() {
        }
    }

    private String cutExtraSpaces(String text) {
        String newText = text.trim().replace(" +", "");
        return newText;
    }

    private String cutUnnecessaryText(String str) {
        int i = str.indexOf("<br/><br/><a href=\"http://core.ad20.net");
        int j = str.indexOf("<br /><a href='http://stiri.tvr.ro/");
        if (i > 0) {
            return str.substring(0, i);
        }
        if(j > 0){
            return str.substring(0, j);
        }
        return str;
    }


}
