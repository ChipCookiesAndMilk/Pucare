package com.example.a01363207.pucare.UserPlantPackage;

import android.database.Cursor;
import android.util.Log;

import com.example.a01363207.pucare.PlantPackage.PlantDP;
import com.example.a01363207.pucare.PlantPackage.PlantDatabaseController;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class OperationsHandler {
    private static final String TAG = "OperationsHandler";

    PlantDatabaseController plantController;

    public String getDate() {
        // YYYY-MM-DD HH:MM:mm
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.toString();
    }

    public String nextWater(String lW, String wW) {
        /* LAST TIME THE PLANT WAS WATERED */
        // YYYY-MM-DD HH:MM:mm
        String date = "", time = "";
        String year = "", month = "", day = "", hours = "", minutes = "", mili = "";
        String num = "", indicator = "";

        String localIndicator = "";
        String[] arrayIndicator = new String[]{"hours", "days", "weeks", "months"};

        StringTokenizer st = new StringTokenizer(lW);
        while (st.hasMoreTokens()) {
            date = (String) st.nextElement().toString();
            time = (String) st.nextElement().toString();
        }

        st = new StringTokenizer(date, "-");
        while (st.hasMoreTokens()) {
            year = (String) st.nextElement().toString();
            month = (String) st.nextElement().toString();
            day = (String) st.nextElement().toString();
        }

        st = new StringTokenizer(time, ":");
        while (st.hasMoreTokens()) {
            hours = (String) st.nextElement().toString();
            minutes = (String) st.nextElement().toString();
            mili = (String) st.nextElement().toString();
        }
        /* WHEN THE DB TELL US TO WATER */
        st = new StringTokenizer(wW);
        while (st.hasMoreTokens()) {
            num = (String) st.nextElement().toString();
            indicator = (String) st.nextElement().toString();
        }

        // we need to know to what we will be adding: week, day or hour
        for (int i = 0; i < arrayIndicator.length; i++) {
            if (indicator.equals(arrayIndicator[i])) {
                localIndicator = arrayIndicator[i];
            }
        }

        //if(){}

        return "";
    }

    // request when to water a plant to the DB
    public String getWater(String plantName) {
        String water = "";

        Log.d(TAG, "IN getWater method ");

        Cursor cursor = plantController.selectPlantsWater(plantName);
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    water = cursor.getString(cursor.getColumnIndex(PlantDP.COLUMN_WATER));
                    Log.d(TAG, "getWater; water: " + water);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception. getWater; exception: " + e.getMessage());
        }

        cursor.close();
        return water;
    }

    public String getImage(String plantName) {
        return "";
    }
}
