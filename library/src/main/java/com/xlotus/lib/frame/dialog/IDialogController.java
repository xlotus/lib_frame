package com.xlotus.lib.frame.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

public interface IDialogController {
    int getDialogLayout();

    void init(BaseDialog dialogFragment, Context context, Bundle args);

    void updateView(View view);

    void onCancel(DialogInterface dialog);

    void onDismiss(DialogInterface dialog);

    void onPause();

    void onDestroy();

    boolean handleKeyDown();
}
