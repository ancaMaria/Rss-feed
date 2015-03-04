package com.example.ancacret.rssfeed.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.ancacret.rssfeed.db.DBHandler;


public class ClearTablesAsync extends AsyncTask<Void, Void, String> {

    private Context mContext;

    public ClearTablesAsync(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        DBHandler dbHandler = new DBHandler(mContext);
        return dbHandler.recreateTables();
    }
}
