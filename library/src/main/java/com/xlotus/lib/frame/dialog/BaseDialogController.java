package com.xlotus.lib.frame.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xlotus.lib.core.utils.HtmlUtils;
import com.xlotus.lib.frame.R;
import com.xlotus.lib.frame.stats.PVEBuilder;

public abstract class BaseDialogController implements IDialogController {
    private IDialog.OnDismissListener mOnDismissListener;
    protected IDialog.OnCancelListener mOnCancelListener;
    protected IDialog.OnOKListener mOnOkListener;
    protected IDialog.OnOkDataListener mOnOkDataListener;

    protected DialogParams mDialogParams;
    protected Context mContext;
    protected BaseDialog mDialogFragment;

    @Override
    public void init(BaseDialog dialogFragment, Context context, Bundle args) {
        mDialogFragment = dialogFragment;
        mContext = context;
        tryParseDialogParams(args);
    }

    protected void tryParseDialogParams(Bundle args) {
        if (args == null)
            return;

        mDialogParams = DialogParams.parseArguments(args);
    }

    @Override
    public void updateView(View view) {
        updateTitleView(view);
        updateMessageView(view);
        updateButtonView(view);
    }

    private void updateTitleView(View view) {
        View titleView = view.findViewById(R.id.title_text);
        if (titleView == null)
            return;

        if (!TextUtils.isEmpty(mDialogParams.title)) {
            ((TextView) titleView).setText(mDialogParams.title);
        } else {
            titleView.setVisibility(View.GONE);
        }
    }

    protected void updateMessageView(View view) {
        View messageView = view.findViewById(R.id.msg_view);
        if (messageView == null)
            return;

        CharSequence message;
        try {
            message = Html.fromHtml(HtmlUtils.replaceLineBreak(mDialogParams.msg));
        } catch (Exception e) {
            message = "";
        }
        if (TextUtils.isEmpty(message))
            messageView.setVisibility(View.GONE);
        else
            ((TextView) messageView).setText(message);
    }

    protected void updateButtonView(View view) {
        updateOkButton(view);
        updateCancelButton(view);
    }

    private void updateOkButton(View view) {
        View okView = view.findViewById(R.id.quit_ok);
        if (okView == null)
            return;

        TextView okButton = (TextView) okView;
        if (!TextUtils.isEmpty(mDialogParams.okText))
            okButton.setText(mDialogParams.okText);

        if (mDialogParams.btnOkColor > 0)
            okButton.setTextColor(mContext.getResources().getColor(mDialogParams.btnOkColor));

        okView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOKAction();
            }
        });
    }

    private void updateCancelButton(View view) {
        View cancelView = view.findViewById(R.id.quit_cancel);
        if (cancelView == null)
            return;

        if (!mDialogParams.showCancelBtn) {
            cancelView.setVisibility(View.GONE);
            return;
        }

        TextView cancelButton = (TextView) cancelView;
        if (!TextUtils.isEmpty(mDialogParams.cancelText))
            cancelButton.setText(mDialogParams.cancelText);

        if (mDialogParams.btnCancelColor > 0)
            cancelButton.setTextColor(mContext.getResources().getColor(mDialogParams.btnCancelColor));

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelAction();
            }
        });
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        onCancel();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        onDismiss();
    }

    @Override
    public boolean handleKeyDown() {
        return mDialogParams != null && !mDialogParams.couldCancel;
    }

    public void setOnOkListener(IDialog.OnOKListener onOkListener) {
        this.mOnOkListener = onOkListener;
    }

    public void setOnOKDataListener(IDialog.OnOkDataListener onOkDataListener) {
        mOnOkDataListener = onOkDataListener;
    }

    public void setOnCancelListener(IDialog.OnCancelListener onCancelListener) {
        this.mOnCancelListener = onCancelListener;
    }

    public void setOnDismissListener(IDialog.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    public void onDismiss() {
        if (mOnDismissListener != null)
            mOnDismissListener.onDismiss(mDialogFragment.getClass().getSimpleName());
    }

    protected void onOKAction() {
        mDialogFragment.dismiss();
        onOk();
        mDialogFragment.statsPopupClick(PVEBuilder.ELEMENT_OK);
    }

    protected void onCancelAction() {
        mDialogFragment.dismiss();
        onCancel();
        mDialogFragment.statsPopupClick(PVEBuilder.ELEMENT_CANCEL);
    }

    protected void onOk() {
        if (mOnOkListener != null)
            mOnOkListener.onOK();
    }

    protected void onCancel() {
        if (mOnCancelListener != null)
            mOnCancelListener.onCancel();
    }

    public void dismiss() {
        if (mDialogFragment == null || mDialogFragment.isHidden()) {
            return;
        }
        mDialogFragment.dismissAllowingStateLoss();
    }
}
