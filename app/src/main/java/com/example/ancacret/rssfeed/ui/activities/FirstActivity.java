package com.example.ancacret.rssfeed.ui.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.adapters.FeedsAdapter;
import com.example.ancacret.rssfeed.async.RssService;
import com.example.ancacret.rssfeed.pojo.RSSItem;
import com.example.ancacret.rssfeed.utils.MfConstants;

import java.util.List;

public class FirstActivity extends ListActivity {

    private FeedsAdapter mAdapter;
    private String[] urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_layout);
        urls = new String[]{
                MfConstants.URL_ECONOMIC,
                MfConstants.URL_ALL_TOPICS,
                MfConstants.URL_CULTURA,
                MfConstants.URL_EXTERNE,
                MfConstants.URL_LIFE_INEDIT,
                MfConstants.URL_POLITIC,
                MfConstants.URL_SOCIAL,
                MfConstants.URL_SPORT,
                MfConstants.URL_STIINTA_SANATATE
        };
        setupViews();
        mAdapter = new FeedsAdapter(this);
        setListAdapter(mAdapter);
        /* get list items of one url using a separate thread  */

        new RssService(urls[0]) {
            @Override
            protected void onPostExecute(List<RSSItem> rssItems) {
                super.onPostExecute(rssItems);
                mAdapter.updateList(rssItems);
            }
        }.execute();


    }




    private void setupViews() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
