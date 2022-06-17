package com.xlotus.lib.frame.utils;

import android.text.TextUtils;

import java.util.Date;

public class RefreshTimeUtils {

    public static void updateRefreshTime(String key) {
        updateRefreshTime(key, new Date());
    }

    public static void updateRefreshTime(String key, Date date) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        ConfigRefresh.setRefresh(key, date.getTime());
    }

    public static boolean isNeedRefresh(String key) {
        return isNeedRefresh(key, 10);
    }

    public static boolean isNeedRefresh(String key, int min) {
        if (!TextUtils.isEmpty(key)) {
            if (min == 0) {
                return true;
            }
            final long lastTime = ConfigRefresh.getRefreshByKey(key, -1L);
            if (lastTime == -1L || Math.abs(System.currentTimeMillis() - lastTime) >= min * 60000L) {
                return true;
            }
        }
        return false;
    }

    public static void clearRefreshTime(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        final long lastTime = ConfigRefresh.getRefreshByKey(key, -1L);
        if (lastTime == -1 || lastTime == 0)
            return;

        ConfigRefresh.delRefresh(key);
    }

    public static int getRefreshNum(String key) {
        if (TextUtils.isEmpty(key)) {
            return 1;
        }
        return ConfigRefresh.getRefreshNumByKey(key, 1);
    }

    public static void resetRefreshNum(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        ConfigRefresh.setRefreshNum(key, 1);
    }

    public static void addRefreshNum(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        ConfigRefresh.setRefreshNum(key, getRefreshNum(key) + 1);
    }

    public static void clearRefreshNum(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        ConfigRefresh.setRefreshNum(key, getRefreshNum(key) + 1);
    }
}
