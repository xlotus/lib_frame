package com.xlotus.lib.frame.loader;

public class LocalLoadTask<T> extends BaseLoadTask<T> {

    private LoadedListener<T> mLoadedListener;
    private final NetLoadTask.IDataHandler<T> mProcessDataHandler;

    public LocalLoadTask(LocalLoadListener<T> loadListener, NetLoadTask.IDataHandler handler, LoadedListener<T> loadedListener) {
        super(loadListener);
        mLoadedListener = loadedListener;
        mProcessDataHandler = handler;
    }

    @Override
    protected LocalLoadListener<T> getListener() {
        return (LocalLoadListener<T>) super.getListener();
    }

    @Override
    protected T doExecute() throws Exception {
        T result = null;
        if(getListener() != null)
            result = getListener().loadLocal();
        if (result != null && mProcessDataHandler != null)
            result = mProcessDataHandler.processData(true, false, result);
        return result;
    }

    @Override
    protected void onResult(T result) {
        if (getListener() != null) {
            if (result != null)
                getListener().onLocalResponse(result);
            if (mLoadedListener != null)
                mLoadedListener.afterLoadFinished(result);
        }
    }

    @Override
    protected void onError(Throwable t) {
        if (getListener() != null) {
            if (mLoadedListener != null)
                mLoadedListener.afterLoadFinished(null);
        }
    }

    @Override
    public void release() {
        super.release();
        mLoadedListener = null;
    }

    public interface LoadedListener<T> {
        void afterLoadFinished(T t);
    }

    public interface LocalLoadListener<T> extends BaseLoadListener {
        void onLocalResponse(T t);

        T loadLocal() throws Exception;
    }
}
