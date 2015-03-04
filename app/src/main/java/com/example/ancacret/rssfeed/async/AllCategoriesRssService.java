package com.example.ancacret.rssfeed.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ancacret.rssfeed.pojo.CategoryItem;
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


public class AllCategoriesRssService extends AsyncTask<String[], Void, List<CategoryItem> > {

    private String[] categories;
    private String[] urls;
    private List<CategoryItem> categoryItems;

    public AllCategoriesRssService(String[] categories, String[] urls) {
        this.categories = categories;
        this.urls = urls;
    }
    /* return list of RssItems from each category link */
    @Override
    protected List<CategoryItem> doInBackground(String[]... params) {
        categoryItems = new ArrayList<CategoryItem>();
        String[] urlStrings = urls;
        String[] categoryNames = categories;
        /* for each link return a RssItem*/
        int i = 0;
        for(String url: urlStrings){
            CategoryItem newItem = new CategoryItem();
            newItem.setRSSItem(fetchXml(url));
            newItem.setUrl(url);
            newItem.setName(categoryNames[i]);
            categoryItems.add(newItem);
            i++;
        }
        return categoryItems;
    }

    private RSSItem fetchXml(String urlString) {
        XmlPullParserFactory xmlFactoryObj;
        RSSItem item = new RSSItem();
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
            item = readFeed(xmlParser);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return item;
    }


    /* looking  for item tags in the xml document */
    private RSSItem readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        RSSItem item;
        // parser.require(XmlPullParser.START_TAG, parser.getNamespace(), "rss");
        int next = parser.next();
        while (next != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (name.equalsIgnoreCase(MfConstants.ITEM)) {
                    item = readFirstEntry(parser);
                    return item;
                }
            }
            next = parser.next();
        }
        return null;
    }

    /* parse the content of the item */
    private RSSItem readFirstEntry(XmlPullParser parser) {
        RSSItem rssItem = new RSSItem();
        try {
            parser.require(XmlPullParser.START_TAG, parser.getNamespace(), MfConstants.ITEM);
            /* we want to take the start tags <title>, <link> and get the content*/
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                Log.v(MfConstants.TAG_LOG, name);
                if (name.equals(MfConstants.TITLE)) {
                    rssItem.setTitle(readTagContent(parser, MfConstants.TITLE));
                } else if (name.equals(MfConstants.DESCRIPTION)) {
                    rssItem.setDescription(readTagContent(parser, MfConstants.DESCRIPTION));
                } else if (name.equals(MfConstants.CATEGORY)) {
                    rssItem.setCategory(readTagContent(parser, MfConstants.CATEGORY));
                } else if (name.equals(MfConstants.PUB_DATE)) {
                    rssItem.setPubDate(readTagContent(parser, MfConstants.PUB_DATE));
                } else if (name.equals(MfConstants.LINK)) {
                    rssItem.setLink(readTagContent(parser, MfConstants.LINK));
                } else if(name.equals(MfConstants.ENCLOSURE)){
                    rssItem.setEnclosureUrl(readTagContent(parser, MfConstants.ENCLOSURE));
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

    private String readDescriptionTag(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, parser.getNamespace(), tagName);
        String descriptionText = readText(parser);
        /* ignore links and spaces */
        String ignoredText = descriptionText.replaceAll("http://core.ad20([a-z0-9\\-\\.?=&_/]+)", "");
        parser.require(XmlPullParser.END_TAG, parser.getNamespace(), tagName);
        return descriptionText;
    }

    private String readEnclosureTag(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, parser.getNamespace(), tagName);
        String urlValue = parser.getAttributeValue(null, "url");
        if (!urlValue.equals(null)) {
            return urlValue;
        }
        return null;
    }




}
