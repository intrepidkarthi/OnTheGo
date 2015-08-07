package com.looksphere.goindia.controller;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by karthi on 12/04/14.
 */
public class SharedPreferencesController {

    private static final String SHARED_PREF_NAME = "yourdeals_pref";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //***********************SINGLETON FACTORY***********************************

    static SharedPreferencesController sharedPreferenceController = null;

    public static SharedPreferencesController getSharedPreferencesController(Activity activity) {
        if (sharedPreferenceController == null) {
            sharedPreferenceController = new SharedPreferencesController(activity);
        }
        return sharedPreferenceController;
    }
    //**************************************************************************

    //private constructor
    SharedPreferencesController(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(SHARED_PREF_NAME, 0);
        editor = sharedPreferences.edit();
    }


    public SharedPreferences getSharedPreferences()
    {
        return this.sharedPreferences;
    }

    public SharedPreferences.Editor getSharedPreferencesEditor()
    {

        return this.editor;
    }



    public void putString(String preferenceName, String data)
    {
        editor.putString(preferenceName, data);
        editor.commit();
    }

    public String getString(String preferenceName)
    {
        return  sharedPreferences.getString(preferenceName, "");
    }

    public void removePreference(String preferenceName)
    {
        editor.remove(preferenceName);
        editor.commit();
    }

}
