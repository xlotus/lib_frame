package com.xlotus.lib.frame.fragment.strategy;

import com.xlotus.lib.frame.utils.RefreshTimeUtils;

public class CacheStrategy implements ICacheStrategy {

    private String key;
    private int interval; //更新间隔

    public CacheStrategy(String key) {
        this(key, 5);
    }

    public CacheStrategy(String key, int interval) {
        this.key = key;
        this.interval = interval;
    }

    @Override
    public void updateRefreshTime() {
        RefreshTimeUtils.updateRefreshTime(key);
    }

    @Override
    public boolean isNeedRefresh() {
        return RefreshTimeUtils.isNeedRefresh(key, interval);
    }

    @Override
    public String getRefreshKey() {
        return key;
    }

    @Override
    public boolean supportLocalCache() {
        return true;
    }
}
