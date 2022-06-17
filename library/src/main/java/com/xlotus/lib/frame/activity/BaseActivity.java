package com.xlotus.lib.frame.activity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.xlotus.lib.core.stats.Stats;
import com.xlotus.lib.core.utils.Timing;
import com.xlotus.lib.core.utils.Utils;
import com.xlotus.lib.core.utils.permission.PermissionsUtils;
import com.xlotus.lib.frame.loadingdialog.LoadingManager;
import com.xlotus.lib.frame.utils.StatusBarUtil;

public abstract class BaseActivity extends FragmentActivity implements ILoading, PermissionsUtils.IPermissionRequestListener {

    private LoadingManager loadingManager;
    protected boolean mHasSetStatusColor = false;

    protected PermissionsUtils.PermissionRequestCallback mPermissionCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Timing timing = new Timing("Timing.AC").start("BaseActivity.onCreate");
        Utils.setAdaptationRequestedOrientation(this, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        timing.split("done super.onCreate");
        loadingManager = new LoadingManager(this);
        timing.end();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Stats.onResume(this, "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Stats.onPause(this, "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoading();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        if (!mHasSetStatusColor) {
            setStatusBar();
            mHasSetStatusColor = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.notifyPermissionsChange(permissions, grantResults, mPermissionCallback);
    }

    @Override
    public void setPermissionRequestListener(PermissionsUtils.PermissionRequestCallback callback) {
        mPermissionCallback = callback;
    }

    protected void setStatusBar() {
        setStatusBar(Color.WHITE);
    }

    protected final void setStatusBar(@ColorInt int color) {
        StatusBarUtil.setColorNoTranslucent(this, color);
        StatusBarUtil.setLightMode(this);
    }

    @Override
    public void showLoading() {
        if (loadingManager != null) {
            loadingManager.show();
        }
    }

    @Override
    public void hideLoading() {
        if (loadingManager != null) {
            loadingManager.hide(null);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // super.onSaveInstanceState(outState);
    }

}
