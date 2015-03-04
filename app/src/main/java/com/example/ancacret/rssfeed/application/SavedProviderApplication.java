package com.example.ancacret.rssfeed.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.ancacret.rssfeed.pojo.NewsProvider;


public class SavedProviderApplication extends Application {

    private SharedPreferences mPrefs;
    public static final String PREF_KEY = "provider";
    private static SavedProviderApplication mInstance;

    public SavedProviderApplication() {
        //mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public SavedProviderApplication getInstance() {
        if (mInstance == null) {
            return new SavedProviderApplication();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = getInstance();
    }

    public void saveProvider(String provider) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_KEY, provider);
        editor.commit();
    }

    public String getProviderName() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return mPrefs.getString(PREF_KEY, "");
    }
}
