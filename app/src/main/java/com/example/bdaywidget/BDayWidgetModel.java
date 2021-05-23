package com.example.bdaywidget;


import android.content.Context;
import android.util.Log;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class BDayWidgetModel extends APrefWidgetModel {
    private static String tag = "BDayWidgetModel";
    private static String BDAY_WIDGET_PROVIDER_NAME = "com.example.bdaywidget";
    private String name = "anon";
    private static String F_NAME = "name";
    private String bday = "1/1/2001";
    private static String F_BDAY = "bday";

    public BDayWidgetModel(int instanceId) {
        super(instanceId);
    }

    public BDayWidgetModel(int instanceId, String inName, String inBday) {
        super(instanceId);
        this.name = inName;
        this.bday = inBday;
    }

    public void init() {
    }

    public void setName(String inname) {
        this.name = inname;
    }

    public void setBday(String inbday) {
        this.bday = inbday;
    }

    public String getName() {
        return this.name;
    }

    public String getBday() {
        return this.bday;
    }

    public long howManyDays() {
        try {
            return Utils.howfarInDaysThisYear(Utils.getDate(this.bday));
        } catch (ParseException var2) {
            return 20000L;
        }
    }

    public void setValueForPref(String key, String value) {
        if (key.equals(this.getStoredKeyForFieldName(F_NAME))) {
            Log.d(tag, "Setting name to:" + value);
            this.name = value;
        } else if (key.equals(this.getStoredKeyForFieldName(F_BDAY))) {
            Log.d(tag, "Setting bday to:" + value);
            this.bday = value;
        } else {
            Log.d(tag, "Sorry the key does not match:" + key);
        }
    }

    public String getPrefname() {
        return BDAY_WIDGET_PROVIDER_NAME;
    }

    public Map<String, String> getPrefsToSave() {
        Map<String, String> map = new HashMap();
        map.put(F_NAME, this.name);
        map.put(F_BDAY, this.bday);
        return map;
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("iid:" + this.iid);
        sbuf.append("name:" + this.name);
        sbuf.append("bday:" + this.bday);
        return sbuf.toString();
    }

    public static void clearAllPreferences(Context ctx) {
        APrefWidgetModel.clearAllPreferences(ctx, BDAY_WIDGET_PROVIDER_NAME);
    }

    public static BDayWidgetModel retrieveModel(Context ctx, int widgetId) {
        BDayWidgetModel m = new BDayWidgetModel(widgetId);
        boolean found = m.retrievePrefs(ctx);
        return found ? m : null;
    }
}

