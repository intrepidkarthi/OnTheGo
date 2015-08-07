package com.looksphere.goindia.utils;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by SPARK on 16/06/15.
 */
public class SharedPreferencesController {

    private static final String SHARED_PREF_NAME = "whistlr";
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
        editor.putString(preferenceName, data).commit();
    }

    public String getString(String preferenceName)
    {
        return sharedPreferences.getString(preferenceName, "");
    }

    public void putInt(String preferenceName, int data)
    {
        editor.putInt(preferenceName, data).commit();
    }

    public int getInt(String preferenceName)
    {
        return sharedPreferences.getInt(preferenceName, 0);
    }

    public void removePreference(String preferenceName)
    {
        editor.remove(preferenceName).commit();
    }

}