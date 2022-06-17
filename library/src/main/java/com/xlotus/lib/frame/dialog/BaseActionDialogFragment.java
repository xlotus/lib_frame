package com.xlotus.lib.frame.dialog;

import android.content.DialogInterface;
import android.view.KeyEvent;

public abstract class BaseActionDialogFragment extends BaseDialog {
    private static final String TAG = "BaseActionDialogFragment";
    private boolean mCouldCancel = true;

    private IDialog.OnDismissListener mDismissListener;
    private IDialog.OnOKListener mOkListener;
    private IDialog.OnCancelListener mOnCancelListener;

    public void setDialogDismissListener(IDialog.OnDismissListener dialogDismissListener) {
        mDismissListener = dialogDismissListener;
    }

    public void setOnOkListener(IDialog.OnOKListener onOkListener) {
        mOkListener = onOkListener;
    }

    public void setOnCancelListener(IDialog.OnCancelListener onCancelListener) {
        mOnCancelListener = onCancelListener;
    }

    public void onOk() {
        if (mOkListener != null)
            mOkListener.onOK();
    }

    public void onCancel() {
        if (mOnCancelListener != null)
            mOnCancelListener.onCancel();
    }

    protected boolean handleOnKeyDown(int keyCode, KeyEvent event) {
        return !mCouldCancel && (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        try {
            super.onDismiss(dialog);
        } catch (Exception e) {
        }
        onDialogDismiss();
    }

    private void onDialogDismiss() {
        if (mDismissListener != null)
            mDismissListener.onDismiss(this.getClass().getSimpleName());
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        onCancel();
    }

    public final void setCouldCancel(boolean could) {
        mCouldCancel = could;
    }
}
