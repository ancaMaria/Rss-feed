package com.example.ancacret.rssfeed.ui;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by anca.cret on 2/24/2015.
 */
public class SwipeRefreshLayoutWithLV extends SwipeRefreshLayout {

    private Context mContext;
    private ListView mListView;

    public SwipeRefreshLayoutWithLV(Context context) {
        super(context);
    }

    public SwipeRefreshLayoutWithLV(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    /**
     * Disable the pull to refresh gesture unless the listView cannot scroll up anymore.
     */
    @Override
    public boolean canChildScrollUp() {
        if (mListView == null) {
            return super.canChildScrollUp();
        }
        return !(mListView.getFirstVisiblePosition() == 0 && (mListView.getChildAt(0) != null &&
                mListView.getChildAt(0).getTop() == 0));
    }

    public void setListView(ListView listView) {
        mListView = listView;
    }

}
