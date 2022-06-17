package com.xlotus.lib.frame.fragment.strategy;

public class SimpleCacheStrategy implements ICacheStrategy {

    private String mKey;

    public SimpleCacheStrategy(String key) {
        mKey = key;
    }

    @Override
    public void updateRefreshTime() {
    }

    @Override
    public boolean isNeedRefresh() {
        return false;
    }

    @Override
    public String getRefreshKey() {
        return mKey;
    }

    @Override
    public boolean supportLocalCache() {
        return true;
    }
}
