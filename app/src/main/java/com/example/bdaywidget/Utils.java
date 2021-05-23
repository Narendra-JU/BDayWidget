package com.example.bdaywidget;


import android.content.Context;
import android.util.Log;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    private static String tag = "Utils";

    public Utils() {
    }

    public static Date getDate(String dateString) throws ParseException {
        DateFormat a = getDateFormat();
        Date date = a.parse(dateString);
        return date;
    }

    public static String test(String sdate) {
        try {
            Date d = getDate(sdate);
            DateFormat a = getDateFormat();
            String s = a.format(d);
            return s;
        } catch (Exception var4) {
            Log.d(tag, "problem");
            return "problem with date:" + sdate;
        }
    }

    public static DateFormat getDateFormat() {
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        df.setLenient(false);
        return df;
    }

    public static boolean validateDate(String dateString) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            df.setLenient(false);
            df.parse(dateString);
            return true;
        } catch (ParseException var3) {
            return false;
        }
    }

    public static long howfarInDays(Date date) {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        long today_ms = today.getTime();
        long target_ms = date.getTime();
        return (target_ms - today_ms) / 86400000L;
    }

    public static long howfarInDaysThisYear(Date date) {
        Calendar bdayCal = Calendar.getInstance();
        bdayCal.setTime(date);
        int bday = bdayCal.get(5);
        int bmonth = bdayCal.get(2);
        Calendar todayCalendar = Calendar.getInstance();
        Calendar bdaycalThisYear = Calendar.getInstance();
        bdaycalThisYear.set(5, bday);
        bdaycalThisYear.set(2, bmonth);
        bdaycalThisYear.set(1, todayCalendar.get(1));
        long today_ms = todayCalendar.getTimeInMillis();
        long bday_ms = bdaycalThisYear.getTimeInMillis();
        return (bday_ms - today_ms) / 86400000L;
    }

    public static void testPrefSave(Context ctx) {
        BDayWidgetModel.clearAllPreferences(ctx);
        BDayWidgetModel m = new BDayWidgetModel(1, "Satya", "1/2/2009");
        m.savePreferences(ctx);
        BDayWidgetModel m1 = BDayWidgetModel.retrieveModel(ctx, 1);
        if (m1 == null) {
            Log.d(tag, "Cant locate the wm");
        } else {
            Log.d(tag, m1.toString());
            m1.setName("Satya2");
            m1.setBday("1/3/2009");
            m1.savePreferences(ctx);
            m1.retrievePrefs(ctx);
            Log.d(tag, "Retrieved m1");
            Log.d(tag, m1.toString());
            BDayWidgetModel m3 = new BDayWidgetModel(3, "Satya3", "1/3/2009");
            m3.savePreferences(ctx);
            BDayWidgetModel m3r = BDayWidgetModel.retrieveModel(ctx, 3);
            Log.d(tag, "Retrieved m3");
            Log.d(tag, m3.toString());
        }
    }
}
