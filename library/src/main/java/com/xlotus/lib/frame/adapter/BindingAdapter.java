package com.xlotus.lib.frame.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.RequestManager;
import com.xlotus.lib.frame.holder.BaseRecyclerViewHolder;
import com.xlotus.lib.frame.holder.BindingViewHolder;
import com.xlotus.lib.frame.holder.EventType;
import com.xlotus.lib.frame.holder.OnHolderEventListener;
import com.xlotus.lib.frame.viewtracker.ImpressionTracker;

/**
 * 数据类型统一，单样式列表的适配器
 * @param <T> 数据类型
 * @param <VB> 每行的ViewBinding
 */
public abstract class BindingAdapter<T, VB extends ViewBinding> extends BaseRecyclerViewAdapter<T, BindingViewHolder<T, VB>> {

    public BindingAdapter() {
        super();
    }

    public BindingAdapter(RequestManager requestManager) {
        super(requestManager);
    }

    public BindingAdapter(RequestManager requestManager, ImpressionTracker impressionTracker) {
        super(requestManager, impressionTracker);
    }

    @NonNull
    @Override
    public BindingViewHolder<T, VB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BindingViewHolder<T, VB> holder = onCreateViewHolder2(parent, viewType);
        holder.setOnHolderItemClickListener(new OnHolderEventListener<T>() {
            @Override
            public void onItemClick(BaseRecyclerViewHolder<T> holder) {
                onItemCLick(holder.getBasePosition());
            }

            @Override
            public void onItemBind(BaseRecyclerViewHolder<T> holder) {

            }

            @Override
            public void onHolderChildItemEvent(BaseRecyclerViewHolder<T> holder, int childPos, Object childData, int eventType) {

            }

        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<T, VB> holder, int position) {
        holder.setPosition(position);
        onBindViewHolder2(holder, position);
    }

    public void onItemCLick(int position) {

    }

    public abstract BindingViewHolder<T, VB> onCreateViewHolder2(ViewGroup parent, int viewType);
    public abstract void onBindViewHolder2(@NonNull BindingViewHolder<T, VB> holder, int position);
}
