package com.xlotus.lib.frame.adapter;

import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.RequestManager;
import com.xlotus.lib.core.Logger;
import com.xlotus.lib.frame.holder.BaseRecyclerViewHolder;
import com.xlotus.lib.frame.holder.OnHolderEventListener;
import com.xlotus.lib.frame.viewtracker.ImpressionTracker;

import java.util.List;

public abstract class HeaderFooterRecyclerAdapter<T, FD> extends BaseRecyclerViewAdapter<T, BaseRecyclerViewHolder<T>> {
    private OnHolderEventListener<T> mHolderItemClickListener;

    protected Object mHeader;
    protected FD mFooter;
    private OnFooterBindItemListener<FD> mFooterBindItemListener;
    private OnBindBasicItemListener<FD> mOnBindBasicItemListener;
    private OnUnbindBasicItemListener<FD> mOnUnbindBasicItemListener;
    private OnUnbindHeaderListener<FD> mOnUnbindHeaderListener;
    private OnHeaderBindItemListener mHeaderBindItemListener;

    private OnHolderEventListener<Object> mHeaderHolderChildEventListener;
    private OnHolderEventListener<FD> mFooterHolderChildEventListener;

    protected BaseRecyclerViewHolder<Object> mHeaderHolder;

    public HeaderFooterRecyclerAdapter() {
        super();
    }

    public HeaderFooterRecyclerAdapter(RequestManager requestManager, ImpressionTracker impressionTracker) {
        super(requestManager, impressionTracker);
    }

    @Override
    public final int getItemViewType(int position) {
        if (position == 0 && showHeader()) {
            return 1000;
        }
        if (position == getItemCount() - 1 && showFooter()) {
            return 1001;
        }
        return getBasicItemViewType(getBasicPosition(position));
    }

    @Override
    public final BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1000) {
            if (mHeaderHolder == null) {
                mHeaderHolder = onCreateHeaderViewHolder(parent, viewType);
                mHeaderHolder.setOnHolderItemClickListener(mHeaderHolderChildEventListener);
            }
            return mHeaderHolder;
        }
        if (viewType == 1001) {
            BaseRecyclerViewHolder<FD> footerHolder = onCreateFooterViewHolder(parent, viewType);
            footerHolder.setOnHolderItemClickListener(mFooterHolderChildEventListener);
            return footerHolder;
        }

        BaseRecyclerViewHolder viewHolder = onCreateBasicItemViewHolder(parent, viewType);
        viewHolder.setOnHolderItemClickListener(mHolderItemClickListener);
        return viewHolder;
    }

    @Override
    public final void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (holder.getItemViewType() == 1000) {
            holder.onBindViewHolder(mHeader);
            if (mHeaderBindItemListener != null)
                mHeaderBindItemListener.onBindHeader(holder);
        } else if (holder.getItemViewType() == 1001) {
            holder.onBindViewHolder(mFooter);
            if (mFooterBindItemListener != null)
                mFooterBindItemListener.onBindFooter(holder, mFooter);
        } else {
            onBindBasicItemView(holder, getBasicPosition(position));
            if (mOnBindBasicItemListener != null)
                mOnBindBasicItemListener.onBindBasicItem(holder, position);
        }
    }

    @Override
    public final void onBindViewHolder(BaseRecyclerViewHolder holder, int position, List payload) {
        if (payload == null || payload.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }

        onViewStatusChanged(holder, getBasicPosition(position), payload);
    }

    protected void onBindBasicItemView(BaseRecyclerViewHolder<T> holder, int position) {
        holder.onBindViewHolder(getItem(position));
    }

    protected void onViewStatusChanged(BaseRecyclerViewHolder<T> holder, int position, List payload) {
        holder.onBindViewHolder(getItem(position));
    }

    @Override
    public <D extends T> void updateDataAndNotify(List<D> list, boolean refresh) {
        final int itemCount = getBasicItemCount();
        updateData(list, refresh);

        if (refresh) {
            notifyDataSetChanged();
        } else if (list != null && !list.isEmpty()) {
            notifyItemRangeChanged(getActualPosition(itemCount), list.size());
        }
    }

    @Override
    public <D extends T> void updateDataAndNotify(List<D> list, int keepCnt, boolean refresh) {
        final int itemCount = getBasicItemCount();
        updateData(list, keepCnt, refresh);

        if (refresh) {
            notifyDataSetChanged();
        } else if (list != null && !list.isEmpty()) {
            notifyItemRangeChanged(getActualPosition(itemCount), list.size());
        }
    }

    public T getBasicItem(int basicPos) {
        return getItem(basicPos);
    }

    public int getActualPosition(int basicPos) {
        int pos = basicPos;
        if (showHeader())
            pos++;

        return pos;
    }

    public int getBasicPosition(int adapterPos) {
        int pos = adapterPos;
        if (showHeader()) {
            pos--;
        }
        return pos;
    }

    public int getBasicItemCount() {
        return getData().size();
    }

    @Override
    public int getItemCount() {
        int count = getBasicItemCount();
        if (mHeader != null)
            count++;
        if (mFooter != null)
            count++;
        return count;
    }

    public abstract int getBasicItemViewType(int position);

    public abstract BaseRecyclerViewHolder<T> onCreateBasicItemViewHolder(ViewGroup parent, int viewType);

    public abstract BaseRecyclerViewHolder<Object> onCreateHeaderViewHolder(ViewGroup parent, int viewType);

    public abstract BaseRecyclerViewHolder<FD> onCreateFooterViewHolder(ViewGroup parent, int viewType);

    public Object getHeaderData() {
        return mHeader;
    }

    public void setHeaderData(Object hd) {
        final boolean lastShow = showHeader();
        mHeader = hd;
        if (mHeaderHolder != null)
            mHeaderHolder.onBindViewHolder(hd);

        if (showHeader() != lastShow)
            notifyDataSetChanged();
    }

    public void setHeaderDataOnly(Object hd) {
        mHeader = hd;
    }

    public void updateHeaderView() {
        if (mHeaderHolder == null)
            return;

        mHeaderHolder.onBindViewHolder(mHeader);
    }

    public void removeHeaderData() {
        mHeader = null;
    }

    public FD getFooterData() {
        return mFooter;
    }

    public void setFooterData(FD fd) {
        boolean lastShow = showFooter();
        int lastItemCount = getItemCount();
        mFooter = fd;
        if (!lastShow) {
            if (showFooter())
                notifyItemInserted(lastItemCount);
            return;
        }

        if (showFooter()) {
            notifyItemChanged(lastItemCount - 1);
            return;
        }

        notifyItemRemoved(lastItemCount - 1);
    }

    public boolean showHeader() {
        return mHeader != null;
    }

    public boolean showFooter() {
        return mFooter != null;
    }

    public BaseRecyclerViewHolder getHeaderViewHolder() {
        return mHeaderHolder;
    }

    public void setOnUnbindBasicItemListener(OnUnbindBasicItemListener listener) {
        mOnUnbindBasicItemListener = listener;
    }

    public void setOnUnbindHeaderListener(OnUnbindHeaderListener onUnbindHeaderListener) {
        this.mOnUnbindHeaderListener = onUnbindHeaderListener;
    }

    public void setOnFooterBindItemListener(OnFooterBindItemListener listener) {
        mFooterBindItemListener = listener;
    }

    public void setOnBindBasicItemListener(OnBindBasicItemListener listener) {
        mOnBindBasicItemListener = listener;
    }

    public void setOnHeaderBindItemListener(OnHeaderBindItemListener listener) {
        mHeaderBindItemListener = listener;
    }

    public void setHeaderClickListener(OnHolderEventListener listener) {
        this.mHeaderHolderChildEventListener = listener;
    }

    public void setFooterClickListener(OnHolderEventListener listener) {
        this.mFooterHolderChildEventListener = listener;
    }

    public void setItemClickListener(OnHolderEventListener listener) {
        this.mHolderItemClickListener = listener;
    }

    @Override
    public boolean isEmpty() {
        return getBasicItemCount() == 0;
    }

    public interface OnHeaderBindItemListener {
        void onBindHeader(BaseRecyclerViewHolder headerHolder);
    }

    public interface OnFooterBindItemListener<FD> {
        void onBindFooter(BaseRecyclerViewHolder<FD> footerHolder, FD footer);
    }

    public interface OnBindBasicItemListener<FD> {
        void onBindBasicItem(BaseRecyclerViewHolder<FD> holder, int position);
    }

    public interface OnUnbindBasicItemListener<FD> {
        void onUnbindBasicItem(BaseRecyclerViewHolder<FD> holder);
    }

    public interface OnUnbindHeaderListener<FD> {
        void onUnbindHeader(BaseRecyclerViewHolder<FD> holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == 1000 || getItemViewType(position) == 1001) {
                        return gridManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
        recyclerView.addOnScrollListener(mScrollListener);
    }

    RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            handleScrollStateChanged(newState);
        }
    };

    public void handleScrollStateChanged(int newState) {
        ImpressionTracker impressionTracker = getImpressionTracker();
        if (impressionTracker != null) {
            impressionTracker.setUserOperated(true);
            // 当用户滑动停止时进行检测一下，以防止滑动到底部漏检
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                impressionTracker.performCheckOnScrolled();
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        recyclerView.removeOnScrollListener(mScrollListener);
    }

    @Override
    public void onViewAttachedToWindow(BaseRecyclerViewHolder<T> holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) lp;
            if (holder.getItemViewType() == 1001) {
                params.setFullSpan(true);
            } else {
                params.setFullSpan(false);
            }
        }
        ImpressionTracker impressionTracker = getImpressionTracker();
        if (impressionTracker != null) {
            Logger.d("ImpressionTracker", "track view holder = " + holder.getClass().getSimpleName());
            impressionTracker.addView(holder.itemView, holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(BaseRecyclerViewHolder<T> holder) {
        super.onViewDetachedFromWindow(holder);
        ImpressionTracker impressionTracker = getImpressionTracker();
        if (impressionTracker != null) {
            Logger.d("ImpressionTracker", "remove view from tracker holder = " + holder.getClass().getSimpleName());
            impressionTracker.removeView(holder.itemView);
        }
    }

    @Override
    public void onViewRecycled(BaseRecyclerViewHolder<T> holder) {
        super.onViewRecycled(holder);
        holder.onUnbindViewHolder();
        if (holder.getItemViewType() == 1000) {
            if (mOnUnbindHeaderListener != null) {
                mOnUnbindBasicItemListener.onUnbindBasicItem((BaseRecyclerViewHolder) holder);
            }
        }
        if (holder.getItemViewType() != 1000 && holder.getItemViewType() != 1001) {
            if (mOnUnbindBasicItemListener != null) {
                mOnUnbindBasicItemListener.onUnbindBasicItem((BaseRecyclerViewHolder) holder);
            }
        }
    }
}

