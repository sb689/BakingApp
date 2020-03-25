package com.example.bakingapp.utiils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class DisplayUtils {

    public static int mScreenHeight;
    public static int mScreenWidth;

    public static void getScreenSize(Activity activity)
    {
        /*the display matrix code is copied from stackOverflow
         */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
    }
}
