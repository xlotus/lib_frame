package com.xlotus.lib.frame.loader;

public class NetLoadTask<T> extends BaseLoadTask<T> {

    private final String mLastId;
    private final IDataHandler<T> mProcessDataHandler;

    public NetLoadTask(NetLoadListener<T> loadListener, IDataHandler handler, String lastId) {
        super(loadListener);
        mLastId = lastId;
        mProcessDataHandler = handler;
    }

    @Override
    protected NetLoadListener<T> getListener() {
        return (NetLoadListener<T>) super.getListener();
    }

    @Override
    protected T doExecute() throws Exception {
        T result = getListener().loadNet(mLastId);
        if (mProcessDataHandler != null)
            result = mProcessDataHandler.processData(mLastId == null, true, result);
        return result;
    }

    @Override
    protected void onResult(T result) {
        if (getListener() != null)
            getListener().onNetResponse(mLastId == null, result);
    }

    @Override
    protected void onError(Throwable t) {
        if (getListener() != null)
            getListener().onNetError(mLastId == null, t);
    }

    public interface NetLoadListener<T> extends BaseLoadListener {
        void onNetError(boolean isRefresh, Throwable t);

        void onNetResponse(boolean isRefresh, T response);

        T loadNet(String lastId) throws Exception;
    }

    public interface IDataHandler<T> {
        T processData(boolean isRefresh, boolean isNetResponse, T t);
    }
}
