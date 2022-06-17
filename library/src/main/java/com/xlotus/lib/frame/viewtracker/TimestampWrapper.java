package com.xlotus.lib.frame.viewtracker;

import android.os.SystemClock;

import androidx.annotation.NonNull;

public class TimestampWrapper<T> {
    @NonNull
    T mInstance;
    long mCreateTimestamp;

    TimestampWrapper(T instance) {
        mCreateTimestamp = SystemClock.uptimeMillis();
        mInstance = instance;
    }

    boolean hasRequiredTimeElapsed(long minTimeViewed) {
        return SystemClock.uptimeMillis() - mCreateTimestamp >= minTimeViewed;
    }

}
