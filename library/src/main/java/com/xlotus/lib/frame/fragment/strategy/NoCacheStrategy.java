package com.xlotus.lib.frame.fragment.strategy;

public class NoCacheStrategy implements ICacheStrategy {

    private static NoCacheStrategy sInstance = new NoCacheStrategy();

    public static NoCacheStrategy getInstance() {
        return sInstance;
    }

    @Override
    public void updateRefreshTime() {
    }

    @Override
    public boolean isNeedRefresh() {
        return true;
    }

    @Override
    public String getRefreshKey() {
        return "";
    }

    @Override
    public boolean supportLocalCache() {
        return false;
    }
}
