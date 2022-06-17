package com.xlotus.lib.frame.fragment;

import android.text.Html;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.xlotus.lib.core.lang.ObjectStore;
import com.xlotus.lib.core.net.NetUtils;
import com.xlotus.lib.frame.R;

public class ErrorViewController extends StateViewController {
    private ImageView failedIconImageView;
    private ImageView failedTagImageView;
    private TextView errorMsgText;
    private View retryButton;
    private ImageView retryIcon;
    private TextView retryText;

    protected boolean noNetwork;
    protected ErrorConfig mErrorConfig;

    protected boolean mIsForGame;

    public ErrorViewController(View rootView, int viewStubResId, int layoutId, ErrorViewCallback callback, ErrorConfig errorConfig) {
        super(rootView, viewStubResId, layoutId, callback);
        mErrorConfig = errorConfig;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        if (mIsForGame) {
            return;
        }

        failedIconImageView = (ImageView) view.findViewById(R.id.error_icon);
        failedTagImageView = (ImageView) view.findViewById(R.id.tag_icon);

        errorMsgText = (TextView) view.findViewById(R.id.error_msg);
        retryButton = view.findViewById(R.id.retry_btn);
        if (mErrorConfig != null && mErrorConfig.retryButton > 0)
            retryButton.setBackground(ContextCompat.getDrawable(retryButton.getContext(), mErrorConfig.retryButton));

        retryIcon = (ImageView) view.findViewById(R.id.retry_icon);
        retryText = (TextView) view.findViewById(R.id.retry_text);
        retryText.setOnClickListener(mRetryClickListener);
        retryButton.setOnClickListener(mRetryClickListener);
    }

    protected View.OnClickListener mRetryClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (noNetwork) {
                ((ErrorViewCallback) mCallback).onNetworkSettingClick();
            } else {
                ((ErrorViewCallback) mCallback).onRetryClick();
            }
        }
    };

    public boolean isShowNetworkError() {
        return mInflatedView != null && mInflatedView.getVisibility() == View.VISIBLE && noNetwork;
    }

    @Override
    public void onViewClick() {
        super.onViewClick();
        if (mCallback != null) {
            ((ErrorViewCallback) mCallback).onRetryClick();
        }
    }

    @Override
    protected void onViewShow(boolean show) {
        super.onViewShow(show);
        if (mIsForGame) {
            return;
        }

        if (show) {
            Pair<Boolean, Boolean> netInfo = NetUtils.checkConnected(ObjectStore.getContext());
            if (!netInfo.first && !netInfo.second) {
                noNetwork = true;
                String errorText = getErrorTextNet();
                if (TextUtils.isEmpty(errorText)) {
                    errorMsgText.setVisibility(View.GONE);
                } else {
                    errorMsgText.setText(Html.fromHtml(errorText));
                }

                retryText.setText(getNetErrorBtn());

                if (retryIcon != null)
                    retryIcon.setVisibility(View.GONE);

                if (failedIconImageView != null) {
                    if (getErrorImg() > 0) {
                        failedIconImageView.setImageResource(getErrorImg());
                        failedIconImageView.setVisibility(View.VISIBLE);
                    } else
                        failedIconImageView.setVisibility(View.GONE);
                }

                if (failedTagImageView != null) {
                    if (getErrorTagIconNet() > 0)
                        failedTagImageView.setImageResource(getErrorTagIconNet());
                    else
                        failedTagImageView.setVisibility(View.GONE);
                }

                ((ErrorViewCallback) mCallback).onNetworkErrorShow();
            } else {
                noNetwork = false;
                String errorText = getErrorTextCommon();
                if (TextUtils.isEmpty(errorText)) {
                    errorMsgText.setVisibility(View.GONE);
                } else {
                    errorMsgText.setText(Html.fromHtml(errorText));
                }
                retryText.setText(getCommonErrorBtn());

                if (retryIcon != null && showCommonErrorRetryIcon())
                    retryIcon.setVisibility(View.VISIBLE);

                if (failedIconImageView != null) {
                    if (getErrorImg() > 0) {
                        failedIconImageView.setImageResource(getErrorImg());
                        failedIconImageView.setVisibility(View.VISIBLE);
                    } else
                        failedIconImageView.setVisibility(View.GONE);
                }

                if (failedTagImageView != null) {
                    if (getErrorTagIconCommon() > 0)
                        failedTagImageView.setImageResource(getErrorTagIconCommon());
                    else
                        failedTagImageView.setVisibility(View.GONE);
                }
            }
        }
    }

    private int getErrorImg() {
        if (mErrorConfig != null && mErrorConfig.errorImg > 0) {
            return mErrorConfig.errorImg;
        }

        return 0;
    }

    private boolean showCommonErrorRetryIcon() {
        if (mErrorConfig != null) {
            return mErrorConfig.showRetryIcon;
        }
        return true;
    }

    private String getCommonErrorBtn() {
        if (mErrorConfig != null && !TextUtils.isEmpty(mErrorConfig.commonErrorBtn))
            return mErrorConfig.commonErrorBtn;
        return ObjectStore.getContext().getString(R.string.common_load_error_reload);
    }

    private String getNetErrorBtn() {
        if (mErrorConfig != null && !TextUtils.isEmpty(mErrorConfig.netErrorBtn))
            return mErrorConfig.netErrorBtn;
        return ObjectStore.getContext().getString(R.string.common_load_error_set_network);
    }

    private String getErrorTextCommon() {
        if (mErrorConfig != null && !TextUtils.isEmpty(mErrorConfig.commonErrorText)) {
            return mErrorConfig.commonErrorText;
        }

        return ObjectStore.getContext().getString(R.string.video_list_item_error_msg);
    }

    private String getErrorTextNet() {
        if (mErrorConfig != null && !TextUtils.isEmpty(mErrorConfig.netErrorText)) {
            return mErrorConfig.netErrorText;
        }
        return ObjectStore.getContext().getString(R.string.request_failed_network_msg);
    }

    private int getErrorTagIconCommon() {
        if (mErrorConfig != null && mErrorConfig.commonErrorIcon > 0) {
            return mErrorConfig.commonErrorIcon;
        }

        return R.drawable.request_failed_common;
    }

    private int getErrorTagIconNet() {
        if (mErrorConfig != null && mErrorConfig.netErrorIcon > 0) {
            return mErrorConfig.netErrorIcon;
        }

        return R.drawable.request_failed_wireless;
    }

    public interface ErrorViewCallback extends StateViewCallback {
        void onRetryClick();

        void onNetworkSettingClick();

        void onNetworkErrorShow();
    }

    public static class ErrorConfig {
        protected int retryButton;
        protected int errorImg;

        protected String netErrorText;
        protected String commonErrorText;
        protected String netErrorBtn;
        protected String commonErrorBtn;
        private boolean showRetryIcon;
        protected int netErrorIcon; //R.drawable.request_failed_icon_wireless
        protected int commonErrorIcon; //R.drawable.request_failed_icon_common

        protected @DrawableRes int buttonBg;
        protected String titleString;
        protected String msgString;

        public ErrorConfig setRetryButton(int retryButton) {
            this.retryButton = retryButton;
            return this;
        }

        public ErrorConfig setNetErrorText(String netErrorText) {
            this.netErrorText = netErrorText;
            return this;
        }

        public ErrorConfig setErrorImg(int errorImg) {
            this.errorImg = errorImg;
            return this;
        }

        public ErrorConfig setCommonErrorText(String commonErrorText) {
            this.commonErrorText = commonErrorText;
            return this;
        }

        public ErrorConfig setNetErrorIcon(int netErrorIcon) {
            this.netErrorIcon = netErrorIcon;
            return this;
        }

        public ErrorConfig setCommonErrorIcon(int commonErrorIcon) {
            this.commonErrorIcon = commonErrorIcon;
            return this;
        }

        public ErrorConfig setCommonErrorBtn(String commonErrorBtn) {
            this.commonErrorBtn = commonErrorBtn;
            return this;
        }

        public ErrorConfig setNetErrorBtn(String netErrorBtn) {
            this.netErrorBtn = netErrorBtn;
            return this;
        }

        public ErrorConfig setShowCommonErrorRetryIcon(boolean showRetryIcon) {
            this.showRetryIcon = showRetryIcon;
            return this;
        }

        public void setButtonBg(int buttonBg) {
            this.buttonBg = buttonBg;
        }

        public void setTitleString(String titleString) {
            this.titleString = titleString;
        }

        public void setMsgString(String msgString) {
            this.msgString = msgString;
        }
    }
}