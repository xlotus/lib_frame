package com.xlotus.lib.frame.dialog;

public class IDialog {

    public interface OnCancelListener{
        void onCancel();
    }

    public interface OnOKListener {
        void onOK();
    }

    public interface OnOkDataListener<T> {
        void onOk(T okData);
    }

    public interface OnDismissListener {
        void onDismiss(String dialogName);
    }

    public interface OnCheckListener {
        void notifyCheck(boolean checked, boolean isOkClick);
    }
}
