package com.example.ancacret.rssfeed.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by anca.cret on 1/28/2015.
 */
public class AsyncGetBitmap extends AsyncTask<String, Void, Bitmap> {

    private String mPicUrl;

    public AsyncGetBitmap(String picUrl) {
        this.mPicUrl = picUrl;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String picUrl = mPicUrl;
        try {
            URL url = new URL(picUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream stream = connection.getInputStream();
            Bitmap bm = BitmapFactory.decodeStream(stream);
            return bm;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Error getting bitmap from url", e.getMessage());
            return null;
        }
        return null;
    }
}
