package com.xlotus.lib.frame.fragment.strategy;

public interface ICacheStrategy {
    void updateRefreshTime();

    boolean isNeedRefresh();

    String getRefreshKey();

    boolean supportLocalCache();
}
