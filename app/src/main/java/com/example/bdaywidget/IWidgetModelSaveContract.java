package com.example.bdaywidget;


import java.util.Map;

public interface IWidgetModelSaveContract {
    void setValueForPref(String var1, String var2);

    String getPrefname();

    Map<String, String> getPrefsToSave();

    void init();
}
