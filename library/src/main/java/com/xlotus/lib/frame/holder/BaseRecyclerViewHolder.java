package com.xlotus.lib.frame.holder;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.xlotus.lib.core.Logger;
import com.xlotus.lib.frame.viewtracker.ImpressionInterface;
import com.xlotus.lib.imageloader.GlideUtils;

public class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder implements ImpressionInterface {
    private T mItemData;
    private SparseArray<View> mViewIdCache = new SparseArray();

    private OnHolderEventListener<T> mItemClickListener;
    protected int mOrientation;
    protected String mPageType;
    private RequestManager mRequestManager;
    // fixed Holder reuse
    private SparseArray<Boolean> mImpRecordedCache = new SparseArray<>();

    protected int mPosition;
    public BaseRecyclerViewHolder(View v) {
        this(v, GlideUtils.getRequestManager(v.getContext()));
    }

    public BaseRecyclerViewHolder(View v, RequestManager requestManager) {
        super(v);
        this.itemView.setOnClickListener(mRootOnClickListener);
        mRequestManager = requestManager;
        if (mRequestManager == null) {
            mRequestManager = GlideUtils.getRequestManager(v.getContext());
        }
    }

    private final View.OnClickListener mRootOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(BaseRecyclerViewHolder.this);
        }
    };

    public void onViewAttachedToWindow() {
        // Do nothing in base class
    }

    public void setOrientation(int orientation) {
        mOrientation = orientation;
    }

    public void setPageType(String pageType) {
        mPageType = pageType;
    }

    public void onBindViewHolder(T itemData) {
        mItemData = itemData;
    }

    public void onBindViewHolder(T itemData,int position) {
        mItemData = itemData;
        mPosition = position;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getBasePosition() {
        return mPosition;
    }

    public void onUnbindViewHolder() {
        itemView.setTag(null);
    }

    public T getData() {
        return mItemData;
    }

    protected final Context getContext() {
        if (itemView != null)
            return itemView.getContext();

        return null;
    }

    protected final View getConvertView() {
        return itemView;
    }

    protected final View getView(int viewId) {
        View view = mViewIdCache.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViewIdCache.append(viewId, view);
        }
        return view;
    }

    public RequestManager getRequestManager() {
        return mRequestManager;
    }

    public void setOnChildClickListener(int id, View.OnClickListener listener) {
        View view = getView(id);
        if (view != null)
            view.setOnClickListener(listener);
    }

    public OnHolderEventListener<T> getOnHolderItemClickListener() {
        return mItemClickListener;
    }

    public void setOnHolderItemClickListener(OnHolderEventListener<T> listener) {
        mItemClickListener = listener;
    }

    protected void clearImageViewTagAndBitmap(ImageView imageView) {
        imageView.setImageBitmap(null);
        imageView.setTag(null);
    }

    @Override
    public void recordImpression(View view) {
        Logger.i("ImpressionTracker", "record imp holder=" +
                this.getClass().getSimpleName() +
                ",position=" + getAdapterPosition());

        if (mItemClickListener != null) {
            mItemClickListener.onItemBind(BaseRecyclerViewHolder.this);
        }
        onRecordImpressionEx();
    }

    protected void onRecordImpressionEx() {
    }

    public boolean isSupportImpTracker() {
        return true;
    }

    @Override
    public boolean isImpressionRecorded() {
        int position = getAdapterPosition();
        return mImpRecordedCache.get(position) != null ? mImpRecordedCache.get(position) : false;
    }

    @Override
    public void setImpressionRecorded() {
        mImpRecordedCache.append(getAdapterPosition(), true);
    }

    @Override
    public int getMinTimeMillisViewed() {
        return 0;
    }


    @Override
    public int getMinPercentageViewed() {
        return 0;
    }

    @Override
    public float getMinAlphaViewed() {
        return 0;
    }
}
