package com.xlotus.lib.frame.utils;

import com.xlotus.lib.core.Settings;
import com.xlotus.lib.core.lang.ObjectStore;

public class ConfigRefresh {
    private static Settings sRefreshPrefs = new Settings(ObjectStore.getContext(), "refresh_time_sp"); //PreferenceManager.getInstance(App.getContext()).getSharedPreferences("refresh_time_sp");

    public static void delRefresh(String key) {
        sRefreshPrefs.remove(key);
    }

    public static void delRefreshNum(String key) {
        sRefreshPrefs.remove(key + "_fn");
    }

    public static long getRefreshByKey(String key, long defaultValue) {
        return sRefreshPrefs.getLong(key, defaultValue);
    }

    public static int getRefreshNumByKey(String key, int defaultValue) {
        return sRefreshPrefs.getInt(key + "_fn", defaultValue);
    }

    public static void setRefresh(String key, long time) {
        sRefreshPrefs.setLong(key, time);
    }

    public static void setRefreshNum(String key, int num) {
        sRefreshPrefs.setInt(key + "_fn", num);
    }
}
