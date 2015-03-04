package com.example.ancacret.rssfeed.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ancacret.rssfeed.pojo.RSSItem;
import com.example.ancacret.rssfeed.utils.MfConstants;

import java.util.ArrayList;
import java.util.List;

/*  first fragment shown will have the url with the clicked category */
public class MyPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments = new ArrayList<Fragment>();

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(List<Fragment> fragments) {
        this.fragments = fragments;
    }

   /* public MyPagerAdapter(FragmentManager fm, List<Fragment> frags) {
        super(fm);
        fragments = frags;
    }*/


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (fragments.get(position).getArguments().getParcelable(MfConstants.FRAG_ARG) != null) {
            RSSItem rssItem = fragments.get(position).getArguments().getParcelable(MfConstants.FRAG_ARG);
            return rssItem.getCategory();
        }
        return super.getPageTitle(position);
    }



}
