package com.xlotus.lib.frame.dialog.custom;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.xlotus.lib.core.lang.thread.TaskHelper;
import com.xlotus.lib.frame.R;
import com.xlotus.lib.frame.dialog.BaseActionDialogFragment;

public class NetworkOpeningCustomDialog extends BaseActionDialogFragment {
    private boolean mIsFullScreen = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (mIsFullScreen) {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            dialog.getWindow().setAttributes(params);
        }
        if (dialog != null) {
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK)
                        return true;
                    return false;
                }
            });
        }
        return dialog;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.dialog_network_opening;
    }

    @Override
    protected void initView(View parent) {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public static void showDialog(Context context) {
        final NetworkOpeningCustomDialog dialog = new NetworkOpeningCustomDialog();
        if (context instanceof FragmentActivity) {
            FragmentActivity activity = (FragmentActivity) context;
            dialog.show(activity.getSupportFragmentManager(), "network_opening");
            TaskHelper.execZForSDK(new TaskHelper.UITask() {
                @Override
                public void callback(Exception e) {
                    dialog.dismiss();
                }
            }, 5000);
        }
    }

}
