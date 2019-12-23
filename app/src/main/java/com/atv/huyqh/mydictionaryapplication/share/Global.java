package com.atv.huyqh.mydictionaryapplication.share;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Global {

    //Save state by key and value in memory
    public static void saveState(Activity activity, String key, String value) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    //Get state by key
    public static String getState(Activity activity, String key) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }
}
