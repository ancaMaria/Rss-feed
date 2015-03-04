package com.example.ancacret.rssfeed.utils;

import android.content.Context;
import android.content.res.Resources;

import com.example.ancacret.rssfeed.R;
import com.example.ancacret.rssfeed.pojo.HeaderItem;

import java.util.Map;

/**
 * Created by anca.cret on 2/27/2015.
 */
public class ProviderInit {

    private Map<String, HeaderItem> mMediafaxMap, mTvrMap;
    private Context mContext;

    public ProviderInit(Context context, Map<String, HeaderItem> mediafaxMap, Map<String, HeaderItem> tvrMap) {
        mMediafaxMap = mediafaxMap;
        mTvrMap = tvrMap;
        mContext = context;
    }

    public void addItems(){
        Resources resources = mContext.getResources();
        // mMediafaxMap.put(MfConstants.URL_ALL_TOPICS, new HeaderItem(MfConstants.TOATE, resources.getColor(R.color.pink_900)));
        mMediafaxMap.put(MfConstants.URL_ECONOMIC, new HeaderItem(MfConstants.ECONOMIC, resources.getColor(R.color.pink_100)));
        mMediafaxMap.put(MfConstants.URL_CULTURA, new HeaderItem(MfConstants.CULTURA, resources.getColor(R.color.orange_A100)));
        mMediafaxMap.put(MfConstants.URL_EXTERNE, new HeaderItem(MfConstants.EXTERNE, resources.getColor(R.color.lime_200)));
        mMediafaxMap.put(MfConstants.URL_POLITIC, new HeaderItem(MfConstants.POLITIC, resources.getColor(R.color.brown_300)));
        mMediafaxMap.put(MfConstants.URL_SOCIAL, new HeaderItem(MfConstants.SOCIAL, resources.getColor(R.color.blue_300)));
        mMediafaxMap.put(MfConstants.URL_STIINTA_SANATATE, new HeaderItem(MfConstants.STIINTA_SANATATE, resources.getColor(R.color.purple_100)));
        mMediafaxMap.put(MfConstants.URL_LIFE_INEDIT, new HeaderItem(MfConstants.LIFE_INEDIT, resources.getColor(R.color.lime_500)));
        mMediafaxMap.put(MfConstants.URL_SPORT, new HeaderItem(MfConstants.SPORT, resources.getColor(R.color.orange_A100)));

        mTvrMap.put(TvrProviderConstants.URL_TOATE, new HeaderItem(TvrProviderConstants.TOATE, resources.getColor(R.color.green_200)));
        mTvrMap.put(TvrProviderConstants.URL_HOMEPAGE, new HeaderItem(TvrProviderConstants.HOMEPAGE, resources.getColor(R.color.pink_100)));
        mTvrMap.put(TvrProviderConstants.URL_ACTUALITATE, new HeaderItem(TvrProviderConstants.ACTUALITATE, resources.getColor(R.color.purple_300)));
        mTvrMap.put(TvrProviderConstants.URL_ECONOMIE, new HeaderItem(TvrProviderConstants.ECONOMIE, resources.getColor(R.color.blue_200)));
        mTvrMap.put(TvrProviderConstants.URL_CULTURA, new HeaderItem(TvrProviderConstants.CULTURA, resources.getColor(R.color.green_200)));
        mTvrMap.put(TvrProviderConstants.URL_DOCUMENTARE, new HeaderItem(TvrProviderConstants.DOCUMENTARE, resources.getColor(R.color.orange_A100)));
        mTvrMap.put(TvrProviderConstants.URL_EXTERN, new HeaderItem(TvrProviderConstants.EXTERN, resources.getColor(R.color.green_200)));
        mTvrMap.put(TvrProviderConstants.URL_POLITIC, new HeaderItem(TvrProviderConstants.POLITIC, resources.getColor(R.color.brown_300)));
        mTvrMap.put(TvrProviderConstants.URL_OPINII, new HeaderItem(TvrProviderConstants.OPINII, resources.getColor(R.color.material_deep_teal_200)));
        mTvrMap.put(TvrProviderConstants.URL_SCI_TECH, new HeaderItem(TvrProviderConstants.SCI_TECH, resources.getColor(R.color.green_200)));
        mTvrMap.put(TvrProviderConstants.URL_SOCIAL, new HeaderItem(TvrProviderConstants.SOCIAL, resources.getColor(R.color.material_deep_teal_500)));
        mTvrMap.put(TvrProviderConstants.URL_SPECIAL, new HeaderItem(TvrProviderConstants.SPECIAL, resources.getColor(R.color.accent_material_dark)));
        mTvrMap.put(TvrProviderConstants.URL_SPORT, new HeaderItem(TvrProviderConstants.SPORT, resources.getColor(R.color.pink_100)));
        mTvrMap.put(TvrProviderConstants.URL_VREMEA, new HeaderItem(TvrProviderConstants.VREMEA, resources.getColor(R.color.green_200)));
        mTvrMap.put(TvrProviderConstants.URL_VACANTA, new HeaderItem(TvrProviderConstants.VACANTA, resources.getColor(R.color.purple_300)));

    }

}
