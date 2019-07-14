package com.maiajam.camera2app.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSharedPrefrnce {

    private static SharedPreferences sharedPreferences ;
    private static String SelectedCamera = "selectedCamera";
    private static String FirstVisit = "firsitVisit";

    public static void setSelectedCameraId(Context context, int cameraId) {

        sharedPreferences = context.getSharedPreferences(SelectedCamera,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selectedCamera_Id",cameraId);
        editor.commit();

    }

    public static Boolean isFirstVisit(Context context) {

        sharedPreferences = context.getSharedPreferences(FirstVisit,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("first",true);

    }

    public static void setNotFirstVisit(Context context) {

        sharedPreferences = context.getSharedPreferences(FirstVisit,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first",true);
        editor.commit();

    }

    public static int getSelectedCameraId(Context context) {
        sharedPreferences = context.getSharedPreferences(SelectedCamera,Context.MODE_PRIVATE);
        return sharedPreferences.getInt("selectedCamera_Id",0);
    }
}
