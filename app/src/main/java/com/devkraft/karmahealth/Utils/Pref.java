package com.devkraft.karmahealth.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;

    public static final String KEY_LANG = "lang";
    public static final String APP_LANG_ENGLISH = "en";
    public static final String APP_LANG_HINDI = "hi";


    public Pref(Context mcontext){
        sharedPreferences = mcontext.getSharedPreferences("MyData",Context.MODE_PRIVATE);
        myEdit = sharedPreferences.edit();


    }


    public void setLand(String value){
        myEdit.putString(KEY_LANG, value);
        myEdit.apply();
    }
    public String getLand(){
      String value =   sharedPreferences.getString(KEY_LANG, APP_LANG_HINDI);
        return value;
    }




}
