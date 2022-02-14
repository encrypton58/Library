package com.em_and_ei.company.librarycourse.models;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesSingleton {

    private static SharedPreferencesSingleton singleton;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    private SharedPreferencesSingleton(Context context){
        preferences = context.getSharedPreferences("books", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static synchronized SharedPreferencesSingleton getInstance(Context context){
        if(singleton == null){
            singleton = new SharedPreferencesSingleton(context);
        }
        return singleton;
    }

    public boolean saveString(String key, String value){
        editor.putString(key, value);
        return editor.commit();
    }

    public String getString(String key){
        return preferences.getString(key, "");
    }

}
