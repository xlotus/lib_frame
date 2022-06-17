package com.xlotus.lib.frame.fragment;

import android.view.View;
import android.view.ViewStub;

import com.xlotus.lib.core.utils.ui.ViewUtils;

public class StateViewController {
    private View mRootView;
    protected View mInflatedView;

    private int mErrorLayoutId;
    private int mViewStubResId;
    protected StateViewCallback mCallback;

    public StateViewController(View rootView, int viewStubResId, int layoutId, StateViewCallback callback) {
        this.mRootView = rootView;
        this.mViewStubResId = viewStubResId;
        this.mErrorLayoutId = layoutId;
        this.mCallback = callback;
    }

    public void showView(boolean show) {
        if (show && mInflatedView == null && mRootView != null && mViewStubResId > 0) {
            final ViewStub viewStub = (ViewStub) mRootView.findViewById(mViewStubResId);
            if (mErrorLayoutId > 0 && viewStub != null) {
                viewStub.setLayoutResource(mErrorLayoutId);
                mInflatedView = viewStub.inflate();
                initView(mInflatedView);
            }
        }

        if (mInflatedView != null) {
            mInflatedView.setVisibility(show ? View.VISIBLE : View.GONE);
            onViewShow(show);
        }
    }

    private int mMarginTop = 0;

    public void setMarginTop(int marginTop) {
        mMarginTop = marginTop;
        if (mInflatedView != null) {
            ViewUtils.setViewTopMargin(mInflatedView, mMarginTop);
        }
    }

    public boolean isVisible() {
        return mInflatedView != null && mInflatedView.getVisibility() == View.VISIBLE;
    }

    public void onViewClick() {
    }

    protected void initView(View root) {
        if (mMarginTop != 0)
            ViewUtils.setViewTopMargin(mInflatedView, mMarginTop);

        if (mCallback != null)
            mCallback.initView(root);
    }

    protected void onViewShow(boolean show) {
    }

    public interface StateViewCallback {
        void initView(View view);
    }
}
