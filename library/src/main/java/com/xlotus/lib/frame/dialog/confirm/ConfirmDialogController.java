package com.xlotus.lib.frame.dialog.confirm;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.xlotus.lib.frame.R;
import com.xlotus.lib.frame.dialog.BaseDialogController;
import com.xlotus.lib.frame.dialog.IDialog;

public class ConfirmDialogController extends BaseDialogController {
    private boolean mCheck = false;
    private TextView mMessageView;

    private IDialog.OnCheckListener mCheckListener;

    public void setCheckListener(IDialog.OnCheckListener checkListener) {
        this.mCheckListener = checkListener;
    }

    public void onCheck(boolean check, boolean isOkClick) {
        if (mCheckListener != null)
            mCheckListener.notifyCheck(check, isOkClick);
    }

    @Override
    public int getDialogLayout() {
        if (mDialogParams != null && mDialogParams.layout != -1) {
            return mDialogParams.layout;
        }
        return R.layout.widget_confirm_dialog_fragment;
    }

    public void updateView(View view) {
        super.updateView(view);
        updateMsgView(view);
    }

    protected void updateMsgView(View view) {
        if (!TextUtils.isEmpty(mDialogParams.richMsg)) {
            updateRichMsgView(view);
        } else {
            mMessageView = view.findViewById(R.id.msg_view);
            super.updateMessageView(view);
        }
    }

    private void updateRichMsgView(View view) {
        View messageView = view.findViewById(R.id.msg_view);
        if (messageView != null)
            messageView.setVisibility(View.GONE);

        ViewStub richViewStub = (ViewStub) view.findViewById(R.id.rich_msg_view_stub);
        View richView = richViewStub.inflate();
        TextView richMsgView = richView.findViewById(R.id.rich_msg_view);
        richMsgView.setText(mDialogParams.richMsg);
    }

    protected void onOKAction() {
        onCheck(mCheck, true);
        super.onOKAction();
    }

    protected void onCancelAction() {
        onCheck(mCheck, false);
        super.onCancelAction();
    }

    public final void updateMessage(String msgStr) {
        if (mMessageView != null)
            mMessageView.setText(msgStr);
    }

}
