package com.xlotus.lib.frame.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xlotus.lib.core.utils.Utils;
import com.xlotus.lib.core.utils.ui.ViewUtils;
import com.xlotus.lib.frame.R;
import com.xlotus.lib.frame.databinding.ActivityBaseTitleBinding;
import com.xlotus.lib.frame.databinding.LayoutTitleBarBinding;

public abstract class BaseTitleActivity extends BaseActivity {
    private FrameLayout mParentView;
    private View mContainerView;

    private ActivityBaseTitleBinding binding;

    protected abstract void onRightButtonClick();

    protected abstract void onLeftButtonClick();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentView = (FrameLayout) (findViewById(android.R.id.content));
        binding = ActivityBaseTitleBinding.inflate(getLayoutInflater(), mParentView);
//        super.setContentView(binding.getRoot());
        setStatusBar();
        measuredTitleBar();
        ViewUtils.setBackgroundResource(binding.getRoot(), getTitleViewBg());

        LayoutTitleBarBinding titleBar = binding.titleBar;
        ViewUtils.setBackgroundResource(titleBar.returnView, getLeftBackIcon());

        titleBar.rightButton.setTextColor(getResources().getColorStateList(getButtonTextColor()));

        titleBar.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightButtonClick();
            }
        });

        titleBar.returnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftButtonClick();
            }
        });
    }

    protected FrameLayout getTitleBarLayout() {
        return binding.titleBar.getRoot();
    }

    protected boolean isUseWhiteTheme() {
        return true;
    }

    protected int getTitleViewBg() {
        if (isUseWhiteTheme()) {
            return R.drawable.title_bg_white;
        } else {
            return R.color.primary_blue;
        }
    }

    protected boolean isShowTitleViewBottomLine() {
        return true;
    }

    protected int getTitleTextColor() {
        return !isUseWhiteTheme() ? R.color.color_ffffff : R.color.common_actionbar_title_color_dark;
    }

    protected int getLeftBackIcon() {
        return !isUseWhiteTheme() ? R.drawable.title_bar_back_selector : R.drawable.title_bar_back_selector_black;
    }

    protected int getButtonTextColor() {
        return !isUseWhiteTheme() ? R.color.title_bar_button_text_color : R.color.title_bar_button_text_color_black;
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = View.inflate(this, layoutResID, null);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.TOP;
        // Activity use android:fitsSystemWindows="true", do not need add StatusBar Height.
        layoutParams.topMargin = (int) getResources().getDimension(R.dimen.common_title_height) + (view.getFitsSystemWindows() ? 0 : Utils.getStatusBarHeihgt(this));
        mParentView.addView(view, mParentView.getChildCount() - 1, layoutParams);
        mContainerView = view;
    }

    public void setTitleText(int res) {
        binding.titleBar.titleText.setText(res);
    }

    protected void setTitleText(String text) {
        binding.titleBar.titleText.setText(text);
    }

    protected TextView getTitleView() {
        return binding.titleBar.titleText;
    }

    protected View getLeftButton() {
        return binding.titleBar.returnView;
    }

    protected Button getRightButton() {
        return binding.titleBar.rightButton;
    }

    protected void setRightButtonEnabled(boolean enable) {
        getRightButton().setEnabled(enable);
    }

    protected void setBackgroundResource(int resource) {
        mParentView.setBackgroundResource(resource);
    }

    protected void hideTitleBar() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mContainerView.getLayoutParams();
        layoutParams.topMargin = 0;
        mContainerView.setLayoutParams(layoutParams);
        binding.titleBar.getRoot().setVisibility(View.GONE);
    }

    protected void showTitleBar() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mContainerView.getLayoutParams();
        layoutParams.topMargin = (int) getResources().getDimension(R.dimen.common_title_height);
        mContainerView.setLayoutParams(layoutParams);
        binding.titleBar.getRoot().setVisibility(View.VISIBLE);
    }

    private void measuredTitleBar() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) binding.titleBar.getRoot().getLayoutParams();
        layoutParams.topMargin = Utils.getStatusBarHeihgt(this);
        binding.titleBar.getRoot().setLayoutParams(layoutParams);
    }

    @Override
    protected void setStatusBar() {
        setStatusBar(getResources().getColor(R.color.primary_blue));
    }
}
