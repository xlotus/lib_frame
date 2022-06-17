package com.xlotus.lib.frame.holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xlotus.lib.frame.R;


public class EmptyViewHolder<T> extends BaseRecyclerViewHolder<T> {

    public EmptyViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_empty_card, parent, false));
    }
}
