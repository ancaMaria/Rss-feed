package com.example.ancacret.rssfeed.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ancacret.rssfeed.db.DBHandler;
import com.example.ancacret.rssfeed.utils.MfConstants;

/**
 * Created by anca.cret on 2/23/2015.
 */
public class DeleteRowsAsync  extends AsyncTask<Integer, Void, Integer>{

    private Context mContext;
    private Integer mId;

    public DeleteRowsAsync(Context context, int id) {
        mContext = context;
        mId = id;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        DBHandler dbHandler = new DBHandler(mContext);
        return dbHandler.deleteRows(mId);
    }
}
