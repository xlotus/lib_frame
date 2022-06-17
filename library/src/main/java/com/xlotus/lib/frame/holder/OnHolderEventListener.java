package com.xlotus.lib.frame.holder;

public interface OnHolderEventListener<T> {

    void onItemClick(BaseRecyclerViewHolder<T> holder);

    void onItemBind(BaseRecyclerViewHolder<T> holder);

    void onHolderChildItemEvent(BaseRecyclerViewHolder<T> holder, int childPos, Object childData, int eventType);
}
