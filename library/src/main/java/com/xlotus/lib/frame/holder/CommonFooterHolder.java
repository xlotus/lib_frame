package com.xlotus.lib.frame.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xlotus.lib.frame.R;

public class CommonFooterHolder extends BaseFooterHolder {
    private TextView mTvFooter;
    private View mWheelProgress;
    private String mEmptyString;
    private String mFailedString;


    public CommonFooterHolder(ViewGroup parent) {
        this(parent, parent.getContext().getString(R.string.common_no_more_data), parent.getContext().getString(R.string.common_loading_failed));
    }

    public CommonFooterHolder(ViewGroup parent, String emptyRes, String failedRes) {
        super(parent, R.layout.common_list_footer_view);
        mWheelProgress = getView(R.id.progressbar);
        mTvFooter = (TextView) getView(R.id.footer_text);
        mEmptyString = emptyRes;
        mFailedString = failedRes;

    }

    @Override
    public void setState(int data) {
        switch (data) {
            case 1:
                mTvFooter.setText(mFailedString);
                mTvFooter.setVisibility(View.VISIBLE);
                mWheelProgress.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mTvFooter.setText(mEmptyString);
                mWheelProgress.setVisibility(View.INVISIBLE);
                mTvFooter.setVisibility(View.VISIBLE);
                break;
            case 0:
                mWheelProgress.setVisibility(View.VISIBLE);
                mTvFooter.setVisibility(View.INVISIBLE);
                break;
            default:
                mTvFooter.setText(mFailedString + "(" + data + ")");
                getView(R.id.footer_text).setVisibility(View.VISIBLE);
                mWheelProgress.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
