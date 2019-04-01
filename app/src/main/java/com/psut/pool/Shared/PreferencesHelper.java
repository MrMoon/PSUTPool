package com.psut.pool.Shared;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    public static final String KEY_CHAPTER = "chapter";
    private static final String FILE_NAME = "com.yourpackage.yourapp.prefs";
    private SharedPreferences prefs;

    public PreferencesHelper(Context context) {
        prefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }


    //Save the specified value to the shared preferences
    public void save(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }


    //Load the specified value from the shared preferences
    public String loadString(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

}
