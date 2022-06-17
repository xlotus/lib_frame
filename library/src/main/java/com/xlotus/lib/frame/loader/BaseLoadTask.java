package com.xlotus.lib.frame.loader;


import com.xlotus.lib.core.lang.thread.TaskHelper;

public abstract class BaseLoadTask<T> extends TaskHelper.Task {

    private BaseLoadListener mListener;
    private T mResult;

    public BaseLoadTask(BaseLoadListener listener) {
        this.mListener = listener;
    }

    protected BaseLoadListener getListener() {
        return mListener;
    }

    @Override
    public final void execute() throws Exception {
        mResult = doExecute();
    }

    protected abstract T doExecute() throws Exception;

    @Override
    public final void callback(Exception e) {
        if (mListener == null || !mListener.isFragmentAdded())
            return;

        if (e != null) {
            e.printStackTrace();
            onError(e);
        } else
            onResult(mResult);
    }

    protected abstract void onResult(T result);

    protected abstract void onError(Throwable t);

    public void release() {
        this.mListener = null;
    }

    public interface BaseLoadListener {
        boolean isFragmentAdded();
    }
}