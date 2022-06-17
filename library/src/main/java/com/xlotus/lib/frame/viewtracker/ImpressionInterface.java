package com.xlotus.lib.frame.viewtracker;

import android.view.View;

public interface ImpressionInterface {

    // 最小展示百分比
    int getMinPercentageViewed();

    float getMinAlphaViewed();

    // 展示回调
    void recordImpression(View view);

    // 判断是否已经展示
    boolean isImpressionRecorded();

    // 标记已经展示
    void setImpressionRecorded();

    // 获取最小展示时长（毫秒）
    int getMinTimeMillisViewed();

    // 判断卡片是否支持检测，如果不用上报的卡片不加入检测
    boolean isSupportImpTracker();

}
