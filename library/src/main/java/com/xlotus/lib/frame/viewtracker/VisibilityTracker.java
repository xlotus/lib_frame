package com.xlotus.lib.frame.viewtracker;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View的可见性追踪器，根据View可见区域判断是否真实可见
 * 支持Feed流
 */
public class VisibilityTracker {

    private static final int VISIBILITY_THROTTLE_MILLIS = 100;

    private VisibilityListener mVisibilityTrackerListener;

    private final VisibilityRunnable mVisibilityRunnable;

    private final Handler mVisibilityHandler;

    private boolean mIsVisibilityScheduled = false;

    private HashMap<View, TrackingInfo> mTrackViews;


    public VisibilityTracker() {
        mTrackViews = new HashMap<>();
        mVisibilityRunnable = new VisibilityRunnable();
        mVisibilityHandler = new Handler(Looper.getMainLooper());
    }

    public void setVisibilityTrackerListener(VisibilityListener visibilityTrackerListener) {
        mVisibilityTrackerListener = visibilityTrackerListener;
    }


    interface VisibilityListener {
        void onVisibilityChanged(List<View> visibleViews, List<View> invisibleViews);
    }

    public void addView(@NonNull View view, int minPercentageViewed) {
        addView(view, view, minPercentageViewed, 1.0f);
    }

    public void addView(@NonNull View view, int minPercentageViewed, float minAlphaViewed) {
        addView(view, view, minPercentageViewed, minAlphaViewed);
    }

    public void addView(@NonNull View rootView, View view, int minPercentageViewed, float minAlphaViewed) {
        TrackingInfo trackingInfo = mTrackViews.get(view);
        if (trackingInfo == null) {
            trackingInfo = new TrackingInfo();
        }
        trackingInfo.rootView = rootView;
        trackingInfo.view = view;
        trackingInfo.minPercentageViewed = minPercentageViewed;
        trackingInfo.minAlphaViewed = minAlphaViewed;
        mTrackViews.put(view, trackingInfo);
        // 延迟100ms进行检测是否满足展示要求
        scheduleVisibilityCheck();
    }

    private void scheduleVisibilityCheck() {
        // Tracking this directly instead of calling hasMessages directly because we measured that
        // this led to slightly better performance.
        if (mIsVisibilityScheduled) {
            return;
        }
        mIsVisibilityScheduled = true;
        mVisibilityHandler.postDelayed(mVisibilityRunnable, VISIBILITY_THROTTLE_MILLIS);
    }

    public void removeView(View view) {
        mTrackViews.remove(view);
    }

    public void destroy() {
        mTrackViews.clear();
        mVisibilityHandler.removeMessages(0);
        mIsVisibilityScheduled = false;
        mVisibilityTrackerListener = null;
    }

    /**
     * 手动出发检测，可以在用户滑动停止时再出发一下，以免100ms漏检测的卡片
     */
    public void performCheck() {
        if (!mTrackViews.isEmpty()) {
            scheduleVisibilityCheck();
        }
    }


    static class TrackingInfo {
        int minPercentageViewed;
        float minAlphaViewed;
        View rootView;
        View view;
    }

    class VisibilityRunnable implements Runnable {

        private List<View> visibleViews;
        private List<View> invisibleViews;

        VisibilityRunnable() {
            visibleViews = new ArrayList<>();
            invisibleViews = new ArrayList<>();
        }

        @Override
        public void run() {
            mIsVisibilityScheduled = false;
            for (Map.Entry<View, TrackingInfo> entry : mTrackViews.entrySet()) {
                TrackingInfo trackingInfo = entry.getValue();
                View rootView = trackingInfo.rootView;
                View view = trackingInfo.view;
                int minPercentageViewed = trackingInfo.minPercentageViewed;
                float minAlphaViewed = trackingInfo.minAlphaViewed;
                // 判断View的可见区域是否满足最小百分比
                if (VisibilityUtil.isVisible(rootView, view, minPercentageViewed, minAlphaViewed)) {
                    visibleViews.add(view);
                }
                else {
                    invisibleViews.add(view);
                }
            }
            if (mVisibilityTrackerListener != null) {
                mVisibilityTrackerListener.onVisibilityChanged(visibleViews, invisibleViews);
            }
            visibleViews.clear();
            invisibleViews.clear();
        }
    }

}
