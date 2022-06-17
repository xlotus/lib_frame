package com.xlotus.lib.frame.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.xlotus.lib.core.Logger;
import com.xlotus.lib.core.change.ChangeListenerManager;
import com.xlotus.lib.core.utils.Utils;
import com.xlotus.lib.core.utils.device.DevBrandUtils;
import com.xlotus.lib.frame.R;

import java.lang.ref.WeakReference;

public abstract class BaseDialog extends DialogFragment {

    private static final String TAG = "BaseDialog";

    private WeakReference<FragmentActivity> activityReference;
    protected View parent;
    private DialogInterface.OnDismissListener dismissListener;

    private String showTag = "";
    private int showPriority = 5;

    public BaseDialog() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = inflater.inflate(getLayoutResId(), container, false);
        initView(parent);
        initData();
        return parent;
    }

    protected abstract @LayoutRes int getLayoutResId();

    protected abstract void initView(View parent);

    protected abstract void initData();

    public void setFragmentActivity(FragmentActivity activity) {
        activityReference = new WeakReference<>(activity);
    }

    public void queueToShow() {
        DialogManager.getInstance().addToQueue(this);
    }

    /**
     * 显示弹框
     **/
    BaseDialog show() {
        FragmentActivity activity = activityReference != null ? activityReference.get() : null;
        Logger.d(TAG, "showDialog activity=" + activity);
        if (activity != null && !activity.isFinishing()) {
            try {
                show(activity.getSupportFragmentManager(), showTag);
                Logger.d(TAG, "showDialog success");
                return this;
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }
        Logger.d(TAG, "showDialog failed");
        return null;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Logger.d(TAG, "dialog onDismiss");
        if (mIsOnBack) {
            collectDialogOnBackPressed();
        }
        if (isAutoRemoveFromQueue()) {
            ChangeListenerManager.getInstance().notifyChange(DialogManager.TAG, this);
        }
        if (dismissListener != null) {
            dismissListener.onDismiss(dialog);
        }
    }


    /** 点击 back 上报 */
    protected void collectDialogOnBackPressed() {

    }


    private void updateWindowAttributes(Dialog dialog) {
        try {
            if (dialog == null || !DevBrandUtils.MIUI.isMIUI() || DevBrandUtils.MIUI.isFullScreen())
                return;
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            WindowManager wm = (WindowManager) window.getContext().getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            lp.width = dm.widthPixels;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                lp.height = dm.heightPixels + Utils.getStatusBarHeihgt(window.getContext());
                wm.getDefaultDisplay().getRealMetrics(dm);
                lp.height = Math.min(lp.height, dm.heightPixels);
            } else {
                lp.height = dm.heightPixels;
            }
            window.setAttributes(lp);
        } catch (Exception e) {
        }
    }
    private void updateWidowAnimations(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null)
            window.setWindowAnimations(getDialogAnimations());
    }

    protected int getDialogAnimations() {
        return R.style.common_dialog_animstyle;
    }

    /**
     * 是否自动从弹框队列移除
     **/
    protected boolean isAutoRemoveFromQueue() {
        return true;
    }

    public void setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        if (dialog != null) {
            updateWidowAnimations(dialog);
        }

        if (dialog != null) {
            dialog.setOnKeyListener(mKeyListener);
        }

        updateWindowAttributes(dialog);
        return dialog;
    }

    private boolean mIsOnBack;
    private DialogInterface.OnKeyListener mKeyListener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            mIsOnBack = (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0);
            if (mIsOnBack) {
                return handleOnKeyDown(keyCode, event);
            }

            return false;
        }
    };

    protected boolean handleOnKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void setShowTag(String showTag) {
        this.showTag = showTag;
    }

    public void setShowPriority(int priority) {
        showPriority = priority;
    }

    public final int getShowPriority() {
        return showPriority;
    }

    public final void statsPopupClick(String action) {

    }
}
