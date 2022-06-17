package com.xlotus.lib.frame.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.viewbinding.ViewBinding;

import com.xlotus.lib.core.utils.ui.ViewUtils;
import com.xlotus.lib.frame.R;
import com.xlotus.lib.frame.databinding.BaseRequestFragmentLayoutBinding;
import com.xlotus.lib.frame.dialog.custom.NetworkOpeningCustomDialog;
import com.xlotus.lib.frame.fragment.strategy.CacheStrategy;
import com.xlotus.lib.frame.fragment.strategy.ICacheStrategy;
import com.xlotus.lib.frame.fragment.strategy.NoCacheStrategy;
import com.xlotus.lib.frame.loader.LoadManager;
import com.xlotus.lib.frame.loader.LocalLoadTask;
import com.xlotus.lib.frame.loader.NetLoadTask;
import com.xlotus.lib.frame.utils.NetworkUtils;

public abstract class BaseRequestFragment<T> extends BaseFragment implements
        LocalLoadTask.LocalLoadListener<T>,
        NetLoadTask.NetLoadListener<T>,
        NetLoadTask.IDataHandler<T> {

    private LoadManager<T> mLoadManager;
    private View mContentView;
    private View mLoadingView;

    private StateViewController mEmptyViewController;
    private ErrorViewController mErrorViewController;

    private ICacheStrategy mCacheStrategy = NoCacheStrategy.getInstance();

    protected boolean mIsRefreshRetry;

    protected boolean mIsClickNetworkSet = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadManager = new LoadManager(this, this);
        mCacheStrategy = createCacheStrategy(getRefreshKey());
        if (mCacheStrategy == null)
            throw new IllegalArgumentException("CacheStrategy must not be null");
    }

    protected String getRefreshKey() {
        return this.getClass().getSimpleName();
    }

    protected ICacheStrategy getCacheStrategy() {
        return mCacheStrategy;
    }

    protected ICacheStrategy createCacheStrategy(String refreshKey) {
        return new CacheStrategy(refreshKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int requestLayout = getRequestLayout();
        if (requestLayout > 0) {
            View view = inflater.inflate(getRequestLayout(), container, false);
            ViewGroup rootView = view.findViewById(R.id.root);
            if (rootView == null)
                rootView = (ViewGroup) view;

            mContentView = super.onCreateView(inflater, container, savedInstanceState);
            if (mContentView != null) {
                if (rootView.findViewById(R.id.web_container_layout) != null) {
                    rootView.addView(mContentView, 1);
                } else {
                    rootView.addView(mContentView, 0);
                }
            }
            return view;
        } else {
            mContentView = super.onCreateView(inflater, container, savedInstanceState);
            return mContentView;
        }
    }

    protected View getContentView() {
        return mContentView;
    }

    protected @LayoutRes int getRequestLayout() {
        return R.layout.base_request_fragment_layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        afterViewCreated();
    }

    @Override
    public void onDestroy() {
        mLoadManager.release();
        super.onDestroy();
    }

    protected void initView(View view) {
        mEmptyViewController = createEmptyViewController(view);
        if (mEmptyViewController != null)
            mEmptyViewController.setMarginTop(getLoadingMarginTop());
        mErrorViewController = createErrorViewController(view);
        if (mErrorViewController != null)
            mErrorViewController.setMarginTop(getLoadingMarginTop());
    }

    protected boolean shouldShowProgressBar() {
        return true;
    }

    protected int getLoadingMarginTop() {
        return 0;
    }

    protected StateViewController getEmptyViewController() {
        return mEmptyViewController;
    }

    protected ErrorViewController getErrorViewController() {
        return mErrorViewController;
    }

    protected StateViewController createEmptyViewController(View rootView) {
        return new StateViewController(rootView, R.id.base_empty_layout, emptyLayoutId(),
                new StateViewController.StateViewCallback() {
                    @Override
                    public void initView(View view) {
                        initEmptyView(view);
                    }
                });
    }

    protected void initEmptyView(View view) {
    }

    protected abstract String getLoadingText();

    protected ErrorViewController createErrorViewController(View rootView) {
        return new ErrorViewController(rootView, R.id.base_error_layout, getErrorLayoutId(),
                new ErrorViewController.ErrorViewCallback() {
                    @Override
                    public void initView(View view) {
                        BaseRequestFragment.this.initErrorView(view);
                    }

                    @Override
                    public void onRetryClick() {
                        loadNetDataForRetryClick();
                    }

                    @Override
                    public void onNetworkSettingClick() {
                        BaseRequestFragment.this.onNetworkSettingClick();
                    }

                    @Override
                    public void onNetworkErrorShow() {
                        BaseRequestFragment.this.onNetworkErrorShow();
                    }
                }, getErrorConfig());
    }

    protected ErrorViewController createGameErrorViewController(View rootView) {
        return new ErrorViewController(rootView, R.id.base_error_layout, getErrorLayoutId(),
                new ErrorViewController.ErrorViewCallback() {
                    @Override
                    public void initView(View view) {
                        BaseRequestFragment.this.initErrorView(view);
                    }

                    @Override
                    public void onRetryClick() {
                        loadNetDataForRetryClick();
                    }

                    @Override
                    public void onNetworkSettingClick() {
                        BaseRequestFragment.this.onNetworkSettingClick();
                    }

                    @Override
                    public void onNetworkErrorShow() {
                        BaseRequestFragment.this.onNetworkErrorShow();
                    }
                }, getErrorConfig());
    }

    protected ErrorViewController.ErrorConfig getErrorConfig() {
        return null;
    }

    protected void onNetworkSettingClick() {
        NetworkUtils.gotoAuthNetworkSetting(mContext, new NetworkUtils.NetworkStatusListener() {
            @Override
            public void networkReadyOnLow() {
                NetworkOpeningCustomDialog.showDialog(mContext);
            }
        });
        mIsClickNetworkSet = true;
    }

    protected void onNetworkErrorShow() {
    }

    protected void initErrorView(View view) {
    }

    protected int getErrorLayoutId() {
        return R.layout.base_error_layout;
    }

    protected int emptyLayoutId() {
        return R.layout.base_empty_layout;
    }

    protected void afterViewCreated() {
        if (shouldLoadDataForFirstEnter()) {
            if (mCacheStrategy.supportLocalCache()) {
                loadLocalData(new LocalLoadTask.LoadedListener<T>() {
                    @Override
                    public void afterLoadFinished(T o) {
                        if (isLocalDataInvalid(o) || mCacheStrategy.isNeedRefresh()) {
                            loadNetData(null);
                        }
                    }
                });
            } else {
                loadNetData(null);
            }
        }
    }

    protected boolean isLocalDataInvalid(T o) {
        return o == null;
    }

    protected boolean shouldLoadDataForFirstEnter() {
        return true;
    }

    protected void beforeLoadData(boolean isNetLoad, boolean isRefresh) {
        showProgressBar(isRefresh && shouldShowProgressBeforeLoad());
        showEmptyView(false);
        showErrorView(false);
    }

    public boolean loadLocalData(LocalLoadTask.LoadedListener listener) {
        beforeLoadData(false, true);
        mLoadManager.loadLocal(this, listener);
        return true;
    }

    public boolean loadNetData(String lastId) {
        beforeLoadData(true, lastId == null);
        mLoadManager.loadNet(this, lastId);
        return true;
    }

    protected void loadNetDataForRetryClick() {
        mIsRefreshRetry = true;
        loadNetData(null);
    }

    protected boolean shouldShowProgressBeforeLoad() {
        return true;
    }

    private boolean isProgressBarInited = false;

    protected void showProgressBar(boolean show) {
        if (!isProgressBarInited) {
            mLoadingView = getView().findViewById(R.id.base_loadingbar_layout);

            if (mLoadingView != null) {
                int topMargin = getLoadingMarginTop();
                if (topMargin != 0)
                    ViewUtils.setViewTopMargin(mLoadingView, topMargin);

                mLoadingView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return onProgressViewTouch();
                    }
                });
                TextView loadingTipView = mLoadingView.findViewById(R.id.loading_tip);
                String loadingTip = getLoadingText();
                if (loadingTipView != null && !TextUtils.isEmpty(loadingTip))
                    loadingTipView.setText(loadingTip);
            }
            isProgressBarInited = true;
        }

        if (mLoadingView != null)
            mLoadingView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    protected void showErrorView(boolean show) {
        if (mErrorViewController != null)
            mErrorViewController.showView(show);
    }

    protected void showEmptyView(boolean show) {
        if (mEmptyViewController != null)
            mEmptyViewController.showView(show);
    }

    @Override
    public boolean isFragmentAdded() {
        return isAdded();
    }

    @Override
    public void onNetError(boolean isRefresh, Throwable t) {
        showProgressBar(false);
        if (mIsRefreshRetry)
            mIsRefreshRetry = false;
    }

    @Override
    public void onLocalResponse(T t) {
        onResponse(false, true, t);
        if (t != null) {
            showProgressBar(false);
        }
    }

    @Override
    public void onNetResponse(boolean isRefresh, T response) {
        onResponse(true, true, response);
        showProgressBar(false);
        if (mIsRefreshRetry)
            mIsRefreshRetry = false;
    }

    protected void onResponse(boolean isNetResponse, boolean isRefresh, T response) {
        if (isAdded() && isNetResponse) {
            mCacheStrategy.updateRefreshTime();
        }
    }

    protected boolean onProgressViewTouch() {
        return true;
    }

    protected void clearAllRequestTask() {
        mLoadManager.cancelLoadLocalTask();
        mLoadManager.cancelLoadNetTask();
    }
}
