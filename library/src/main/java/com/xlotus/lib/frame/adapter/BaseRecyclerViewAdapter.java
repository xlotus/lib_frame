package com.xlotus.lib.frame.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.xlotus.lib.frame.viewtracker.ImpressionTracker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private RequestManager mRequestManager;
    private ImpressionTracker mImpressionTracker;

    public BaseRecyclerViewAdapter() {
        super();
    }

    public BaseRecyclerViewAdapter(RequestManager requestManager) {
        mRequestManager = requestManager;

    }

    public BaseRecyclerViewAdapter(RequestManager requestManager, ImpressionTracker impressionTracker) {
        this(requestManager);
        mImpressionTracker = impressionTracker;
    }

    public RequestManager getRequestManager() {
        return mRequestManager;
    }

    public ImpressionTracker getImpressionTracker() {
        return mImpressionTracker;
    }

    private List<T> mList = new ArrayList();

    public List<T> getData() {
        return Collections.unmodifiableList(mList);
    }

    public void clearData() {
        mList.clear();
    }

    public void clearData(int keepCnt) {
        if (mList.isEmpty() || keepCnt <= 0 || mList.size() <= keepCnt) {
            mList.clear();
            return;
        }

        mList.removeAll(new ArrayList<>(mList.subList(keepCnt, mList.size())));
    }

    public void clearDataAndNotify() {
        clearData();
        notifyDataSetChanged();
    }

    public <D extends T> void addData(List<D> data) {
        mList.addAll(data);
    }

    public <D extends T> void updateData(List<D> data, boolean refresh) {
        if (refresh)
            clearData();

        if (data != null && !data.isEmpty()) {
            mList.addAll(data);
        }
    }

    public <D extends T> void updateData(List<D> data, int keepCnt, boolean refresh) {
        if (refresh)
            clearData(keepCnt);

        if (data != null && !data.isEmpty()) {
            mList.addAll(data);
        }
    }

    public <D extends T> void insertData(int basicPos, D item) {
        if (item != null) {
            mList.add(basicPos, item);
        }
    }

    public <D extends T> void insertDataAndNotify(int basicPos, D item) {
        insertData(basicPos, item);
        notifyItemInserted(getActualPosition(basicPos));
    }

    public <D extends T> void insertData(int basicPos, List<D> item) {
        mList.addAll(basicPos, item);
    }

    public <D extends T> void insertDataAndNotify(int basicPos, List<D> item) {
        insertData(basicPos, item);
        notifyItemRangeInserted(getActualPosition(basicPos), item.size());
    }

    public <D extends T> int itemIndex(D item) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).equals(item))
                return i;
        }
        return -1;
    }

    public <D extends T> void updateDataAndNotify(List<D> data, boolean refresh) {
        final int oldSize = mList.size();
        updateData(data, refresh);
        if (refresh) {
            notifyDataSetChanged();
            return;
        }
        int newSize = data == null ? 0 : mList.size();
        notifyItemRangeChanged(getActualPosition(oldSize), newSize);
    }

    public <D extends T> void updateDataAndNotify(List<D> data, int keepCnt, boolean refresh) {
        final int oldSize = mList.size();
        updateData(data, refresh);
        if (refresh) {
            notifyDataSetChanged();
            return;
        }
        int newSize = data == null ? 0 : mList.size();
        notifyItemRangeChanged(getActualPosition(oldSize), newSize);
    }

    public void removeDataAndNotify(int basicPos) {
        int size = mList.size();
        if (basicPos < 0 || basicPos >= size)
            return;

        mList.remove(basicPos);
        notifyItemRemoved(getActualPosition(basicPos));
    }

    public void removeDataAndNotify(int basicPos, int itemCount) {
        final int size = mList.size();
        if (basicPos < 0 || basicPos >= size || (basicPos + itemCount) > size)
            return;

        removeData(basicPos, itemCount);
        notifyItemRangeRemoved(getActualPosition(basicPos), itemCount);
    }

    public void removeData(int basicPosStart, int itemCount) {
        for (int i = basicPosStart, size = basicPosStart + itemCount; i < size; i++) {
            mList.remove(i);
            --size;
            --i;
        }
    }

    public <D extends T> void updateItem(D item, int basicPos) {
        mList.set(basicPos, item);
    }

    public <D extends T> void updateItemAndNotify(D item) {
        int position = getDataPosition(item);
        if (position > -1) {
            mList.set(position, item);
            notifyItemChanged(getActualPosition(position));
        }
    }

    public <D extends T> void updateItemAndNotify(D item, int basicPos) {
        mList.set(basicPos, item);
        notifyItemChanged(getActualPosition(basicPos));
    }

    public int getActualPosition(int basicPos) {
        return basicPos;
    }

    public T getItem(int basicPos) {
        if (basicPos < 0 || basicPos >= mList.size())
            return null;

        return mList.get(basicPos);
    }

    public int getDataPosition(T data) {
        return mList.indexOf(data);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public T getLast() {
        int size = mList.size();
        return getLast(size - 1);
    }

    protected T getLast(int limit) {
        int size = mList.size();
        if (size == 0 || limit >= size)
            return null;

        for (int i = limit; i >= 0; i--) {
            T item = mList.get(i);
            if (isNormalItem(item))
                return item;
        }
        return null;
    }

    protected boolean isNormalItem(T t) {
        return true;
    }

    public void onResume() {
    }

    public void onPause() {
    }

}
