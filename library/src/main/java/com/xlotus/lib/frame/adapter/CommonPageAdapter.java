package com.xlotus.lib.frame.adapter;

import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.xlotus.lib.frame.holder.BaseFooterHolder;
import com.xlotus.lib.frame.holder.BaseRecyclerViewHolder;
import com.xlotus.lib.frame.holder.CommonFooterHolder;
import com.xlotus.lib.frame.viewtracker.ImpressionTracker;

public abstract class CommonPageAdapter<T> extends HeaderFooterRecyclerAdapter<T, Integer> {
    public CommonPageAdapter() {
        super();
    }

    public CommonPageAdapter(RequestManager requestManager, ImpressionTracker impressionTracker) {
        super(requestManager, impressionTracker);
    }

    @Override
    public BaseRecyclerViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public BaseFooterHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return new CommonFooterHolder(parent);
    }

    public void setFooterNoMoreState() {
        setFooterData(Integer.valueOf(2));
    }

    public void setFooterLoadingState() {
        setFooterData(Integer.valueOf(0));
    }

    public void setFooterRetryState() {
        setFooterRetryState(Integer.valueOf(1));
    }

    public void setFooterRetryState(int errorCode) {
        setFooterData(errorCode);
    }

    public void setNoFooter() {
        setFooterData(null);
    }
}
