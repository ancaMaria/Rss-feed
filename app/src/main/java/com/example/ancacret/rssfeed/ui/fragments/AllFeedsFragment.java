package com.example.ancacret.rssfeed.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.adapters.CategoriesAdapter;
import com.example.ancacret.rssfeed.pojo.CategoryListItem;
import com.example.ancacret.rssfeed.pojo.HeaderItem;
import com.example.ancacret.rssfeed.pojo.ListViewItem;
import com.example.ancacret.rssfeed.ui.activities.DetailsPager;
import com.example.ancacret.rssfeed.utils.MfConstants;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AllFeedsFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private CategoriesAdapter mAdapter;
    private ProgressBar mProgressBar;
    String[] urlList;
    private List<ListViewItem> mListViewItems;
    private List<HeaderItem> mHeaderItems;

    public static AllFeedsFragment newInstance(List<ListViewItem> listItems, List<HeaderItem> headerItems) {
        AllFeedsFragment frag = new AllFeedsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(MfConstants.FEEDS_FRAG_ARG, (ArrayList<? extends android.os.Parcelable>) listItems);
        args.putParcelableArrayList(MfConstants.HEADER_ITEMS_ARG, (ArrayList<? extends android.os.Parcelable>) headerItems);
        frag.setArguments(args);
        return frag;
    }

    public void updateFragLists(List<ListViewItem> listItems, List<HeaderItem> headerItems){
        getArguments().putParcelableArrayList(MfConstants.FEEDS_FRAG_ARG, (ArrayList<? extends android.os.Parcelable>) listItems);
        getArguments().putParcelableArrayList(MfConstants.HEADER_ITEMS_ARG, (ArrayList<? extends android.os.Parcelable>) headerItems);
        mAdapter.notifyDataSetChanged();
        if (mListViewItems != null) {
            getListView().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_fragment_layout, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.loading_progress);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /* get from intent the list of items, without new requests */
        mListViewItems = getArguments().getParcelableArrayList(MfConstants.FEEDS_FRAG_ARG);
        mHeaderItems = getArguments().getParcelableArrayList(MfConstants.HEADER_ITEMS_ARG);
        getListView().setOnItemClickListener(this);
        //  mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new CategoriesAdapter(getActivity().getApplicationContext(), mListViewItems);
        setListAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        if (mListViewItems != null) {
            getListView().setVisibility(View.VISIBLE);
        }
    }

    /* send all feeds and the url from the clicked item
    */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), DetailsPager.class);
        /* send url to get description of the feed and a unique id from the clicked item
        * adapter does not know about the type of item
        * */
        ListViewItem clickedFeed = mAdapter.getItem(position);
        if (clickedFeed instanceof CategoryListItem) {
            CategoryListItem clickedItem = (CategoryListItem) clickedFeed;
            handleClick(intent, clickedItem);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Click on a feed to read more", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleClick(Intent intent, CategoryListItem clickedItem) {
          /* we need the url or id to set the current page of the pager*/
        String clickedUrl = clickedItem.getRSSItem().getLink();
        intent.putExtra(MfConstants.CLICKED_URL_ITEM, clickedUrl);
            /* pass to pager only the feed items from this category */
        List<CategoryListItem> clickedCategoryItems = new ArrayList<CategoryListItem>();
            /* not really nice - go through whole list to search the items from this category */
        for (ListViewItem item : mListViewItems) {
            if (item.getViewType() == CategoriesAdapter.RowType.LIST_ITEM.ordinal()) {
                if (((CategoryListItem) item).getRSSItem().getCategory().equals(clickedItem.getRSSItem().getCategory())) {
                    clickedCategoryItems.add((CategoryListItem) item);
                }
            }
        }

        if (clickedCategoryItems != null) {
            intent.putParcelableArrayListExtra(MfConstants.FEED_ITEMS_INTENT_KEY, (ArrayList<? extends android.os.Parcelable>) clickedCategoryItems);
        }
            /* send the header item from this category */
        for (HeaderItem headerItem : mHeaderItems) {
            if (headerItem.getCategory().equalsIgnoreCase(clickedItem.getRSSItem().getCategory())) {
                intent.putExtra(MfConstants.HEADER_ITEM_INTENT_KEY, headerItem);
            }
        }
        startActivity(intent);
    }


    /*  */
   /* @Override
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
    }*/


}




