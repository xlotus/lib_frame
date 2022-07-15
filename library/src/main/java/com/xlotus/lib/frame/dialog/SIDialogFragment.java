package com.xlotus.lib.frame.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class SIDialogFragment<F extends BaseDialogBuilder<F>, C extends IDialogController> extends BaseDialog {
    private F mBuilder;
    private C mController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mController != null)
            mController.init(this, getContext(), getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mBuilder == null || mController == null) {
            dismiss();
            return null;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutResId() {
        if (mController != null) {
            return mController.getDialogLayout();
        }
        return 0;
    }

    @Override
    protected void initView(View parent) {
        mController.updateView(parent);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mController != null)
            mController.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mController != null)
            mController.onDestroy();
        mBuilder = null;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mController != null)
            mController.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mController != null)
            mController.onDismiss(dialog);
        mBuilder = null;
    }

    @Override
    protected boolean handleOnKeyDown(int keyCode, KeyEvent event) {
        return mController != null ? mController.handleKeyDown() : false;
    }

    private int getDialogLayout() {
        return mController.getDialogLayout();
    }

    @Nullable
    public F getBuilder() {
        return mBuilder;
    }

    public void setBuilder(F builder) {
        mBuilder = builder;
        mController = (C) mBuilder.getController();
    }

    @Nullable
    public C getController() {
        return mController;
    }

}
