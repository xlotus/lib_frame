package com.xlotus.lib.frame.dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.xlotus.lib.core.lang.ObjectStore;

import java.util.LinkedHashMap;

public abstract class BaseDialogBuilder<T extends BaseDialogBuilder<T>> {

    final Class<? extends SIDialogFragment> mClazz;

    protected Bundle mArgs = new Bundle();
    protected boolean mCancelable = true;
    public BaseDialogBuilder(Class<? extends SIDialogFragment> clazz) {
        mClazz = clazz;
    }

    public SIDialogFragment build() {
        return createDialog();
    }

    public SIDialogFragment show(FragmentActivity activity, String tag) {
        SIDialogFragment dialogFragment = createDialog();
        if (dialogFragment == null)
            return null;

        dialogFragment.setFragmentActivity(activity);
        dialogFragment.setShowTag(tag);
        dialogFragment.setShowPriority(5);
        dialogFragment.show();
        return dialogFragment;
    }

    private SIDialogFragment createDialog() {
        SIDialogFragment dialogFragment = (SIDialogFragment) Fragment.instantiate(ObjectStore.getContext(), mClazz.getName(), mArgs);
        dialogFragment.setBuilder(this);
        dialogFragment.setCancelable(mCancelable);
        return dialogFragment;
    }

    public Bundle getArgs() {
        return mArgs;
    }

    private T getBuilder() {
        return (T) this;
    }

    public abstract BaseDialogController getController();

    public T setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return getBuilder();
    }

    public T setCanceledOnTouchOutside(boolean cancel) {
        getArgs().putBoolean("params_cancel", cancel);
        return getBuilder();
    }

    public T setCouldCancel(boolean couldCancel) {
        getArgs().putBoolean(DialogParams.EXTRA_DIALOG_COULD_CANCEL, couldCancel);
        return getBuilder();
    }

    public T setOnOkListener(IDialog.OnOKListener listener) {
        getController().setOnOkListener(listener);
        return getBuilder();
    }

    public T setOnOkDataListener(IDialog.OnOkDataListener onOkDataListener) {
        getController().setOnOKDataListener(onOkDataListener);
        return getBuilder();
    }

    public T setOnDismissListener(IDialog.OnDismissListener listener) {
        getController().setOnDismissListener(listener);
        return getBuilder();
    }

    public T setOnCancelListener(IDialog.OnCancelListener listener) {
        getController().setOnCancelListener(listener);
        return getBuilder();
    }

    public T setCustomArgs(Bundle args) {
        getArgs().putAll(args);
        return getBuilder();
    }

    public T setTitle(String title) {
        mArgs.putString(DialogParams.EXTRA_TITLE, title);
        return getBuilder();
    }

    public T setMessage(String message) {
        mArgs.putString(DialogParams.EXTRA_MSG, message);
        return getBuilder();
    }

    public T setOkButton(String okButton) {
        mArgs.putString(DialogParams.EXTRA_BTN_OK_TEXT, okButton);
        return getBuilder();
    }

    public T setCancelButton(String cancelButton) {
        mArgs.putString(DialogParams.EXTRA_BTN_CANCEL_TEXT, cancelButton);
        return getBuilder();
    }

    public T setShowCancel(boolean showCancel) {
        mArgs.putBoolean(DialogParams.EXTRA_SHOW_CANCEL, showCancel);
        return getBuilder();
    }
}
