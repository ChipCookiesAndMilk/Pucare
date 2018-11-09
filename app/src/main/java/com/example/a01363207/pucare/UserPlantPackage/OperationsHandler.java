package com.example.a01363207.pucare.UserPlantPackage;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class OperationsHandler {
    private static final String TAG = "OperationsHandler";

    /*
    *   gets current date and hour
    *   Format : YYYY-MM-DD HH:MM:mm
    * */
    public String getDate() {
        //Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return f.format(new Date());
    }


    /*
     *   request the last day of a month
     * */
    private int getLastDay() {
        return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /*
    * INPUT:   last date watered, rough info about when to water the plant
    * RETURN:  date, when to water
    * PURPOSE: this method takes the info of when to water a plant from DB
    * and calculates when in days or hours the user should water
    * the plant
    * */
    public String nextWater(String lW, String wW) {
        /* LAST TIME THE PLANT WAS WATERED */
        // YYYY-MM-DD HH:MM:mm
        String date = "", time = "";
        String year = "", month = "", day = "", hours = "", minutes = "", mili = "";
        String num  = "", indicator = "";

        String localIndicator = "";
        String[] arrayIndicator = new String[]{"hours", "days", "weeks"};

        StringTokenizer st = new StringTokenizer(lW);
        while (st.hasMoreTokens()) {
            date = (String) st.nextElement().toString();
            time = (String) st.nextElement().toString();
        }
        Log.d(TAG,"--->> Date: "+date+" ---- Time: "+time);
        st = new StringTokenizer(date, "/");
        while (st.hasMoreTokens()) {
            year = (String) st.nextElement().toString();
            month = (String) st.nextElement().toString();
            day = (String) st.nextElement().toString();
        }
        Log.d(TAG,"--->> year: "+year+" ---- month: "+month+" ---- day: "+day);
        st = new StringTokenizer(time, ":");
        while (st.hasMoreTokens()) {
            hours = (String) st.nextElement().toString();
            minutes = (String) st.nextElement().toString();
            mili = (String) st.nextElement().toString();
        }
        Log.d(TAG,"--->> hours: "+hours+" ---- minutes: "+minutes+" ---- mili: "+mili);
        /* WHEN THE DB TELL US TO WATER */
        st = new StringTokenizer(wW);
        while (st.hasMoreTokens()) {
            num = (String) st.nextElement().toString();
            indicator = (String) st.nextElement().toString();
        }

        // we need to know to what we will be adding: week, day or hour
        if(indicator.equals(arrayIndicator[0])){
            /* HOURS */

            int h = Integer.parseInt(hours);
            int n = Integer.parseInt(num);

            if(h+n == 24){
                hours = "00";
            }
            else if(h+n > 24){
                // find the difference and add a day
                int nh = h+n;
                int nd = 1;

                while(nh>24){
                    nh = nh-24;
                    nd++;
                }
                hours = ""+nh;

                int lastDay = getLastDay();
                int d = Integer.parseInt(day);

                int mnd = d+nd;
                if(mnd>lastDay){
                    // add month
                    int mon = Integer.parseInt(month);
                    int nI = mon+1;
                    month = ""+nI;
                    day   = ""+nh;
                }else{
                    day = ""+mnd;
                }
            }
            else{
                int nh = h+n;
                hours = ""+nh;
            }
        }
        else if(indicator.equals(arrayIndicator[1])){
            /* DAYS */
            int n = Integer.parseInt(num);
            int lastDay = getLastDay();
            int d = Integer.parseInt(day);

            int nd = d+n;
            if(nd>lastDay){
                // add month
                int mon = Integer.parseInt(month);
                int nI = mon+1;
                month = ""+nI;
                nd = nd - lastDay;
                day = ""+nd;
            }else{
                day = ""+nd;
            }
        }
        else if(indicator.equals(arrayIndicator[2])){
            /* WEEKS */
            int n = Integer.parseInt(num);

            int lastDay = getLastDay();
            int d = Integer.parseInt(day);

            int mnd = d+(n*7);

            if(mnd>lastDay){
                // add month
                int mon = Integer.parseInt(month);
                int nI = mon+1;
                month = ""+nI;
                mnd = mnd - lastDay;
                day = ""+mnd;
            }else{
                day = ""+mnd;
            }
        }
        return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+mili;
    }
}
