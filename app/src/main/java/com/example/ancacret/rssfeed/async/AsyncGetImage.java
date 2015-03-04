package com.example.ancacret.rssfeed.async;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by anca.cret on 1/22/2015.
 */
public class AsyncGetImage  extends AsyncTask<String, Void, Drawable> {

    private String mPicUrl;

    public AsyncGetImage(String picUrl) {
        mPicUrl = picUrl;
    }

    @Override
    protected Drawable doInBackground(String... params) {
        Drawable d = loadImageFromUrl(mPicUrl);
        return d;
    }

    private Drawable loadImageFromUrl(String url) {
        try {
            InputStream stream = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(stream, "name");
            return d;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPicUrl() {
        return mPicUrl;
    }
}
