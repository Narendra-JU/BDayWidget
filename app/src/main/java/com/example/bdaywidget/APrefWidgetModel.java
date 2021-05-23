package com.example.bdaywidget;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import java.util.Iterator;
import java.util.Map;

public abstract class APrefWidgetModel implements IWidgetModelSaveContract {
    private static String tag = "AWidgetModel";
    public static int STATUS_ACTIVE = 1;
    public static int STATUS_DELETED = 2;
    public int iid;
    public int status;

    public APrefWidgetModel(int instanceId) {
        this.status = STATUS_ACTIVE;
        this.iid = instanceId;
    }

    public void setStatus(int inStatus) {
        this.status = inStatus;
    }

    public int getStatus() {
        return this.status;
    }

    public void setDeleted() {
        this.status = STATUS_DELETED;
    }

    public boolean isDeleted() {
        return this.status == STATUS_DELETED;
    }

    public abstract String getPrefname();

    public abstract void init();

    public abstract void setValueForPref(String var1, String var2);

    public Map<String, String> getPrefsToSave() {
        return null;
    }

    public void savePreferences(Context context) {
        Map<String, String> keyValuePairs = this.getPrefsToSave();
        if (keyValuePairs != null) {
            Editor prefs = context.getSharedPreferences(this.getPrefname(), 0).edit();
            Iterator var5 = keyValuePairs.keySet().iterator();

            while(var5.hasNext()) {
                String key = (String)var5.next();
                String value = (String)keyValuePairs.get(key);
                this.savePref(prefs, key, value);
            }

            prefs.commit();
        }
    }

    private void savePref(Editor prefs, String key, String value) {
        String newkey = this.getStoredKeyForFieldName(key);
        Log.d(tag, "saving:" + newkey + ":" + value);
        prefs.putString(newkey, value);
    }

    private void removePref(Editor prefs, String key) {
        String newkey = this.getStoredKeyForFieldName(key);
        Log.d(tag, "Removing:" + newkey);
        prefs.remove(newkey);
    }

    protected String getStoredKeyForFieldName(String fieldName) {
        return fieldName + "_" + this.iid;
    }

    public static void clearAllPreferences(Context context, String prefname) {
        Log.d(tag, "Clearing all preferences for:" + prefname);
        SharedPreferences prefs = context.getSharedPreferences(prefname, 0);
        Log.d(tag, "Number of preferences:" + prefs.getAll().size());
        Editor prefsEdit = prefs.edit();
        prefsEdit.clear();
        prefsEdit.commit();
    }

    public boolean retrievePrefs(Context ctx) {
        Log.d(tag, "Rerieving preferences for widget id:" + this.iid);
        SharedPreferences prefs = ctx.getSharedPreferences(this.getPrefname(), 0);
        Map<String, ?> keyValuePairs = prefs.getAll();
        Log.d(tag, "Number of keys for all widget ids of this type:" + keyValuePairs.size());
        boolean prefFound = false;
        Iterator var6 = keyValuePairs.keySet().iterator();

        while(var6.hasNext()) {
            String key = (String)var6.next();
            if (this.isItMyPref(key)) {
                String value = (String)keyValuePairs.get(key);
                Log.d(tag, "setting value for:" + key + ":" + value);
                this.setValueForPref(key, value);
                prefFound = true;
            }
        }

        return prefFound;
    }

    public void removePrefs(Context context) {
        Log.d(tag, "Removing preferences for widget id:" + this.iid);
        Map<String, String> keyValuePairs = this.getPrefsToSave();
        if (keyValuePairs != null) {
            Editor prefs = context.getSharedPreferences(this.getPrefname(), 0).edit();
            Iterator var5 = keyValuePairs.keySet().iterator();

            while(var5.hasNext()) {
                String key = (String)var5.next();
                this.removePref(prefs, key);
            }

            prefs.commit();
        }
    }

    private boolean isItMyPref(String keyname) {
        Log.d(tag, "Examinging keyname:" + keyname);
        return keyname.indexOf("_" + this.iid) > 0;
    }
}

