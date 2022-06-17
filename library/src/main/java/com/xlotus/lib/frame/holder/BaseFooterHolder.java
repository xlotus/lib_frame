package com.xlotus.lib.frame.holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

public abstract class BaseFooterHolder extends BaseRecyclerViewHolder<Integer> {
    public BaseFooterHolder(ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(Integer itemData) {
        super.onBindViewHolder(itemData);
        int state = itemData == null ? 0 : itemData;
        setState(state);
    }
    public abstract void setState(int data);
}