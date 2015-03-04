package com.example.ancacret.rssfeed.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.pojo.RSSItem;
import com.example.ancacret.rssfeed.utils.MfConstants;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;


/* contains the detailed description of the clicked item
*  on swipe see the description of the next feed
* */
public class CategoryFragForPager extends Fragment {

    private TextView mFeedTitle, mFeedDescription, mLabel;
    private ImageView mFeedPicture;
    private RSSItem mRSSItem;
    Typeface typefaceContent, typefaceTitle;
    //FragmentManager fm = getFragmentManager();


    public CategoryFragForPager() {
    }

    public static CategoryFragForPager newInstance(RSSItem rssItem, Integer color) {
        CategoryFragForPager frag = new CategoryFragForPager();
        Log.v(MfConstants.TAG_LOG, "setting frags args");
        Bundle args = new Bundle();
        /*  set tag */
        args.putString(MfConstants.FRAG_TAG1, "tag1");
        args.putParcelable(MfConstants.FRAG_ARG, rssItem);
        args.putInt(MfConstants.HEADER_ITEMS_ARG, color);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.description_frag, container, false);
        mFeedPicture = (ImageView) view.findViewById(R.id.feedImage);
        mLabel = (TextView) view.findViewById(R.id.categoryLabel);
        mFeedTitle = (TextView) view.findViewById(R.id.feed_title);
        mFeedDescription = (TextView) view.findViewById(R.id.feed_description);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String feedDescription;
        typefaceContent = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/RobotoCondensed-Light.ttf");
        typefaceTitle = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/Roboto-Regular.ttf");
        mRSSItem = getArguments().getParcelable(MfConstants.FRAG_ARG);
        int color = getArguments().getInt(MfConstants.HEADER_ITEMS_ARG);
        if(mRSSItem != null){
            mLabel.setText(mRSSItem.getCategory());
            if(color != 0){
                mLabel.setBackgroundColor(color);
            }
            mFeedTitle.setText(mRSSItem.getTitle());
            mFeedTitle.setTypeface(typefaceTitle);
            feedDescription = cutUnnecessaryText(mRSSItem.getDescription());
            mFeedDescription.setText(cutExtraSpaces(feedDescription) );
            mFeedDescription.setTypeface(typefaceContent);
            String picUrl = mRSSItem.getEnclosureUrl();
            if(picUrl != null){
                loadPicture(picUrl);
            }
        }
    }

    private void loadPicture(String picUrl) {
        Picasso picasso = Picasso.with(getActivity().getApplicationContext());
        picasso.setIndicatorsEnabled(true);
        picasso.setDebugging(true);
        picasso.setLoggingEnabled(true);
        picasso.load(picUrl).into(mFeedPicture);

    }

    private String cutUnnecessaryText(String str) {
        int i = str.indexOf("<br/><br/><a href=\"http://core.ad20.net");
        int j = str.indexOf("<br /><a href='http://stiri.tvr.ro/");
        if (i > 0) {
            return str.substring(0, i);
        }
        if(j > 0){
            return str.substring(0, j);
        }
        return str;
    }

    private String cutExtraSpaces(String text) {
        String newText = text.trim().replace(" +", "");
        return newText;
    }

    @Override
    public void onStart() {
        super.onStart();

    }



}
