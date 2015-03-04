package com.example.ancacret.rssfeed.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.adapters.FeedsAdapter;
import com.example.ancacret.rssfeed.pojo.RSSItem;
import com.example.ancacret.rssfeed.ui.activities.DetailsPager;
import com.example.ancacret.rssfeed.utils.MfConstants;

import java.lang.reflect.Field;
import java.util.List;

public class CategoryFragForDrawer extends ListFragment implements AdapterView.OnItemClickListener {

    private FeedsAdapter mAdapter;
    private ProgressBar mProgressBar;
    private String mLink;
    private List<RSSItem> mRSSItems;


    public static CategoryFragForDrawer newInstance(List<RSSItem> rssItems) {
        CategoryFragForDrawer frag = new CategoryFragForDrawer();
        Log.v(MfConstants.TAG_LOG, "setting frags args");
        Bundle args = new Bundle();
        args.putParcelableArrayList(MfConstants.DRAWER_FRAG_ARG, (java.util.ArrayList<? extends android.os.Parcelable>) rssItems);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_fragment_layout, container, false);
        mAdapter = new FeedsAdapter(getActivity().getApplicationContext());
        mProgressBar = (ProgressBar) view.findViewById(R.id.loading_progress);
        // mLink = getArguments().getString(Constants.DRAWER_FRAG_ARG);
        setListAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getFeeds(mLink);
        mRSSItems = getArguments().getParcelableArrayList(MfConstants.DRAWER_FRAG_ARG);
        mAdapter.updateList(mRSSItems);
        if(mRSSItems != null){
            getListView().setVisibility(View.VISIBLE);
            getListView().setOnItemClickListener(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /* send list of items and the link from the clicked rssitem to pager activity.. */
        Intent intent = new Intent(getActivity().getApplicationContext(), DetailsPager.class);
        intent.putParcelableArrayListExtra(MfConstants.RSSITEMS_INTENT_KEY, (java.util.ArrayList<? extends android.os.Parcelable>) mRSSItems);
        intent.putExtra(MfConstants.CLICKED_ITEM_FROM_DRAWER_LISTING, mAdapter.getItem(position).getLink());
        startActivity(intent);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
