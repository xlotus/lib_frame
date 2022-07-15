package com.xlotus.lib.frame.dialog.confirm;


import com.xlotus.lib.frame.dialog.BaseDialogBuilder;
import com.xlotus.lib.frame.dialog.BaseDialogController;
import com.xlotus.lib.frame.dialog.DialogParams;
import com.xlotus.lib.frame.dialog.IDialog;
import com.xlotus.lib.frame.dialog.IDialogController;
import com.xlotus.lib.frame.dialog.SIDialogFragment;

public class ConfirmDialogFragment extends SIDialogFragment<ConfirmDialogFragment.Builder, ConfirmDialogController> {

    public static Builder builder() {
        return new Builder(ConfirmDialogFragment.class);
    }


    public static class Builder extends BaseDialogBuilder<Builder> {
        private final ConfirmDialogController mDialogController = new ConfirmDialogController();

        public Builder(Class<? extends SIDialogFragment<ConfirmDialogFragment.Builder, ConfirmDialogController>> clazz) {
            super(clazz);
        }

        @Override
        public BaseDialogController getController() {
            return mDialogController;
        }

        public Builder setCheckListener(IDialog.OnCheckListener listener) {
            mDialogController.setCheckListener(listener);
            return this;
        }

        public Builder setMsg(CharSequence richMsg) {
            mArgs.putCharSequence(DialogParams.EXTRA_MSG, richMsg);
            return this;
        }

        public Builder setSubMessage(String subMessage) {
            mArgs.putString(DialogParams.EXTRA_SUB_MSG, subMessage);
            return this;
        }

        public Builder setRichMsg(CharSequence richMsg) {
            mArgs.putCharSequence(DialogParams.EXTRA_RICH_MSG, richMsg);
            return this;
        }
    }

    public void updateMessage(String msg) {
        IDialogController controller = getController();
        if (controller == null || !(controller instanceof ConfirmDialogController))
            return;
        ConfirmDialogController dialogController = (ConfirmDialogController) controller;
        dialogController.updateMessage(msg);
    }
}
