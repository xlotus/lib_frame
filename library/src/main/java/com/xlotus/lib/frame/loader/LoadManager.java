package com.xlotus.lib.frame.loader;

import com.xlotus.lib.core.lang.thread.TaskHelper;

public class LoadManager<T> {
    private LocalLoadTask.LocalLoadListener<T> mLocalLoadListener;
    private NetLoadTask.NetLoadListener<T> mNetLoadListener;

    private LocalLoadTask<T> mLocalTask;
    private NetLoadTask<T> mNetTask;

    public LoadManager(LocalLoadTask.LocalLoadListener<T> localLoadListener, NetLoadTask.NetLoadListener<T> netLoadListener) {
        this.mLocalLoadListener = localLoadListener;
        this.mNetLoadListener = netLoadListener;
    }

    public void loadNet(NetLoadTask.IDataHandler<T> handler, String lastId) {
        mNetTask = new NetLoadTask(mNetLoadListener, handler, lastId);
        TaskHelper.execZForSDK(mNetTask);
    }

    public void loadLocal(NetLoadTask.IDataHandler<T> handler, LocalLoadTask.LoadedListener<T> listener) {
        cancelLoadLocalTask();
        mLocalTask = new LocalLoadTask(mLocalLoadListener, handler, listener);
        TaskHelper.execZForSDK(mLocalTask);
    }

    public void cancelLoadLocalTask() {
        if (mLocalTask != null) {
            mLocalTask.release();
            mLocalTask = null;
        }
    }

    public void cancelLoadNetTask() {
        if (mNetTask != null) {
            mNetTask.release();
            mNetTask = null;
        }
    }

    public void release() {
        cancelLoadLocalTask();
        cancelLoadNetTask();
    }
}
