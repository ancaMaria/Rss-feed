package com.example.ancacret.rssfeed.async;


import android.os.AsyncTask;
import android.util.Log;

import com.example.ancacret.rssfeed.interfaces.IOnTaskCompleted;
import com.example.ancacret.rssfeed.pojo.RSSItem;
import com.example.ancacret.rssfeed.utils.MfConstants;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RssService extends AsyncTask<String, Void, List<RSSItem>> {

    private String url = null;
    private List<RSSItem> rSSItems;

    private IOnTaskCompleted mListener;

    public RssService(String urlString) {
        url = urlString;
    }

    public RssService(String urlString, IOnTaskCompleted listener){
        this.url = urlString;
        mListener = listener;
    }

    /* read feed and store items in list  */
    @Override
    protected List<RSSItem> doInBackground(String... urls) {
        /* finish task if cancel was called */
        if(!isCancelled()) {
            rSSItems = new ArrayList<RSSItem>();
            XmlPullParserFactory xmlFactoryObj;
            // String feed = urls[0];
            String urlString = url;
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                InputStream stream = connection.getInputStream();
                xmlFactoryObj = XmlPullParserFactory.newInstance();
                XmlPullParser xmlParser = xmlFactoryObj.newPullParser();
                xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xmlParser.setInput(stream, null);
                rSSItems = readFeed(xmlParser);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return rSSItems;
        }
        return null;
    }

    /* looking  for item tags in the xml document */
    private List<RSSItem> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<RSSItem> entries = new ArrayList<RSSItem>();
      // parser.require(XmlPullParser.START_TAG, parser.getNamespace(), "rss");
        int next = parser.next();
        while (next != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equalsIgnoreCase(MfConstants.ITEM)) {
                    entries.add(readEntry(parser));
                } else {
                    //skip(parser);
                }
            }
            next = parser.next();
        }
        return entries;
    }

    /* skips tags we're not interested in */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    /* parse the content of an item */
    private RSSItem readEntry(XmlPullParser parser) {
        RSSItem rssItem = new RSSItem();
        try {
            parser.require(XmlPullParser.START_TAG, parser.getNamespace(), MfConstants.ITEM);
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals(MfConstants.TITLE)) {
                    rssItem.setTitle(readTagContent(parser, MfConstants.TITLE));
                } else if (name.equals(MfConstants.DESCRIPTION)) {
                    rssItem.setDescription(readTagContent(parser, MfConstants.DESCRIPTION));
                } else if (name.equals(MfConstants.CATEGORY)) {
                    rssItem.setCategory(readTagContent(parser, MfConstants.CATEGORY));
                } else if (name.equals(MfConstants.PUB_DATE)) {
                    rssItem.setPubDate(readTagContent(parser, MfConstants.PUB_DATE));
                } else if(name.equals(MfConstants.ENCLOSURE)){
                    rssItem.setEnclosureUrl(readImageUrl(parser, MfConstants.ENCLOSURE));
                } else if (name.equals(MfConstants.LINK)) {
                    rssItem.setLink(readTagContent(parser, MfConstants.LINK));
                } else {
                    skip(parser);
                }
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rssItem;
    }

    private String readTagContent(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, parser.getNamespace(), tagName);
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, parser.getNamespace(), tagName);
        return name;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private String readImageUrl(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, parser.getNamespace(), tagName);
        String picUrl = parser.getAttributeValue(null, "url");
        if(picUrl != null){
            return picUrl;
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        if(mListener != null){
            mListener.onPreExecuting();
        }
    }

    @Override
    protected void onPostExecute(List<RSSItem> rssItems) {
        //super.onPostExecute(rssItems);
        if(mListener != null){
            mListener.onRequestFinished(url, rssItems);
        }
    }

    /*  Runs on the UI thread after cancel(boolean) is invoked and doInBackground(Object[]) has finished. */
    @Override
    protected void onCancelled(List<RSSItem> rssItems) {
        //super.onCancelled(rssItems);
        if(mListener != null){
            mListener.onCanceling();
        }
    }
}
