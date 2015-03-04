package com.example.ancacret.rssfeed.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ancacret.rssfeed.db.DBHandler;
import com.example.ancacret.rssfeed.pojo.RSSItem;
import com.example.ancacret.rssfeed.utils.MfConstants;

import java.util.List;

/**
 * Created by anca.cret on 2/17/2015.
 */
public class StoreFeedsAsync extends AsyncTask<List<RSSItem>, Void, List<RSSItem>> {

    private String result = MfConstants.RESULT_SUCCESS;
    private Context mContext;
    private List<RSSItem> mRSSItems;

    public StoreFeedsAsync(Context context, List<RSSItem> RSSItems) {
        mContext = context;
        mRSSItems = RSSItems;
    }

    @Override
    protected List<RSSItem> doInBackground(List<RSSItem>... params) {
        DBHandler dbHandler = new DBHandler(mContext);
        dbHandler.addAllfeeds(mRSSItems);
        return dbHandler.getAllRssItems();
    }
}
