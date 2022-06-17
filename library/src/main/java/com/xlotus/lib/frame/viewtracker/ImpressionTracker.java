package com.xlotus.lib.frame.viewtracker;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;

import com.xlotus.lib.core.Logger;
import com.xlotus.lib.frame.holder.BindingViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View真实展示追踪器（View的可见性保证最低展示时长为真实展示）
 * 支持Feed流
 */
public class ImpressionTracker {

    private static final String TAG = "ImpressionTracker";
    private static final int PERIOD = 500;

    private VisibilityTracker mVisibilityTracker;

    private Map<View, ImpressionInterface> mTrackedViews;

    // View已经可见了，每隔500ms继续判断一次
    private Map<View, TimestampWrapper<ImpressionInterface>> mPollingViews;

    private Handler mPollingHandler;

    private Runnable mPollingRunnable;

    public ImpressionTracker() {
        mVisibilityTracker = new VisibilityTracker();
        mTrackedViews = new HashMap<>();
        mPollingViews = new HashMap<>();
        mVisibilityTracker.setVisibilityTrackerListener(visibilityTrackerListener);
        mPollingHandler = new Handler(Looper.getMainLooper());
        mPollingRunnable = new PollingRunnable();
    }

    private VisibilityTracker.VisibilityListener visibilityTrackerListener = new VisibilityTracker.VisibilityListener() {
        @Override
        public void onVisibilityChanged(List<View> visibleViews, List<View> invisibleViews) {
            for (View view : visibleViews) {
                ImpressionInterface impInterface = mTrackedViews.get(view);
                if (impInterface == null) {
                    mPollingViews.remove(view);
                    continue;
                }
                TimestampWrapper timestampWrapper = mPollingViews.get(view);
                if (timestampWrapper != null && timestampWrapper.mInstance == impInterface) {
                    continue;
                }
                mPollingViews.put(view, new TimestampWrapper<>(impInterface));
            }

            // 从已经开始展示的View中移除（没有执行展示）
            for (View view : invisibleViews) {
                mPollingViews.remove(view);
            }
            scheduleNextPoll();
        }
    };

    private void scheduleNextPoll() {
        if (mPollingHandler.hasMessages(0)) {
            return;
        }
//        Logger.i(TAG, "scheduleNextPoll PERIOD=" + PERIOD);
        mPollingHandler.postDelayed(mPollingRunnable, PERIOD);
    }

    class PollingRunnable implements Runnable {

        private List<View> mFinishedViews;

        PollingRunnable() {
            mFinishedViews = new ArrayList<>();
        }

        @Override
        public void run() {
            for (Map.Entry<View, TimestampWrapper<ImpressionInterface>> entry : mPollingViews.entrySet()) {
                View view = entry.getKey();
                TimestampWrapper<ImpressionInterface> timestampWrapper = entry.getValue();
                ImpressionInterface impressionInterface = timestampWrapper.mInstance;
                long minTimeViewed = impressionInterface.getMinTimeMillisViewed();
                if (mIsUseOperated && timestampWrapper.hasRequiredTimeElapsed(minTimeViewed)) {
                    impressionInterface.recordImpression(view);
                    impressionInterface.setImpressionRecorded();
                    mFinishedViews.add(view);
                }
            }
            // 把已经完成检测的移除
            for (View view : mFinishedViews) {
                removeView(view);
            }
            mFinishedViews.clear();
            if (!mPollingViews.isEmpty()) {
                scheduleNextPoll();
            }
        }
    }

    public void addView(@NonNull View view, @NonNull ImpressionInterface impressionInterface) {
        // 避免重复添加、不用上报的卡片不进行检测
        if (impressionInterface == null || mTrackedViews.get(view) == impressionInterface ||
                !impressionInterface.isSupportImpTracker()) {
            Logger.i(TAG, "repeat or item don't support");
            return;
        }
        removeView(view);
        // 已经展示记录了不进行记录
        if (impressionInterface.isImpressionRecorded()) {
            Logger.i(TAG, "has impression recorded ");
            return;
        }
        mTrackedViews.put(view, impressionInterface);
        mVisibilityTracker.addView(view, impressionInterface.getMinPercentageViewed(),
                impressionInterface.getMinAlphaViewed());
    }

    public void removeView(View view) {
        mTrackedViews.remove(view);
        mPollingViews.remove(view);
        mVisibilityTracker.removeView(view);
    }

    public void forceRecordImpression(BindingViewHolder<?, ?> holder) {
        if (holder.isSupportImpTracker() && !holder.isImpressionRecorded()) {
            holder.recordImpression(holder.itemView);
            holder.setImpressionRecorded();
            removeView(holder.itemView);
        }
    }

    public void pauseTrack() {
        Logger.i(TAG, "pauseTrack");
        mPollingHandler.removeMessages(0);
    }

    public void resumeTrack() {
        Logger.i(TAG, "resumeTrack");
        if (!mPollingViews.isEmpty()) {
            scheduleNextPoll();
        }
    }


    public void destroy() {
        Logger.i(TAG, "destroy");
        mTrackedViews.clear();
        mPollingViews.clear();
        mPollingHandler.removeMessages(0);
        mVisibilityTracker.destroy();
        visibilityTrackerListener = null;
    }

    // 记录跟卡片无关的用户操作状态，例如列表页发生滑动
    private boolean mIsUseOperated = false;

    public void setUserOperated(boolean userOperated) {
        if (!mIsUseOperated) {
            mIsUseOperated = userOperated;
        }
    }

    public void performCheckOnScrolled() {
        Logger.i(TAG, "performCheckOnScrolled");
        if (mVisibilityTracker != null) {
            mVisibilityTracker.performCheck();
        }
    }
}
