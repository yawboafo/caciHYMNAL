package com.truelight.cacihymn.Models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.truelight.cacihymn.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MemoryData {

    public static Hymn activeHymn;
    public static List<Hymn> activeHymnList;
    public static List<FavoriteHymn> activeFavHymnList;
    public  static  boolean isNighMode ;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    public static Hymn getActiveHymn() {
        return activeHymn;
    }



    public static void setActiveHymn(Hymn activeHymn) {
        MemoryData.activeHymn = activeHymn;
    }

    public static List<Hymn> getActiveHymnList() {
        return activeHymnList;
    }

    public static void setActiveHymnList(List<Hymn> activeHymnList) {
        MemoryData.activeHymnList = activeHymnList;
    }


    public static boolean isIsNighMode() {
        return isNighMode;
    }

    public static void setIsNighMode(boolean isNighMode) {
        MemoryData.isNighMode = isNighMode;
    }



    public static void saveIsNightMode(Activity activity,boolean value){



        SharedPreferences.Editor editor = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean("nightMode", value);
       // editor.putInt("idName", 12);
        editor.apply();
    }


    public static void saveFontSize(Activity activity,Integer value){



        SharedPreferences.Editor editor = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("FontSize", value);
        // editor.putInt("idName", 12);
        editor.apply();
    }

    public static Integer getFontSize(Activity activity){
        //SharedPreferences sharedPref = activity.getPreferences(MODE_PRIVATE);
        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        // int defaultValue = activity.getResources().getInteger(R.integer.saved_high_score_default_key);
        Integer isnight = prefs.getInt("FontSize", 18);


        return  isnight;
    }

    public static boolean getIsNightMode(Activity activity){
        //SharedPreferences sharedPref = activity.getPreferences(MODE_PRIVATE);
        SharedPreferences prefs = activity.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        // int defaultValue = activity.getResources().getInteger(R.integer.saved_high_score_default_key);
        boolean isnight = prefs.getBoolean("nightMode", false);


        return  isnight;
    }

    public static void setActiveFavHymnList(List<FavoriteHymn> products) {

        MemoryData.activeFavHymnList = products;
    }
}
