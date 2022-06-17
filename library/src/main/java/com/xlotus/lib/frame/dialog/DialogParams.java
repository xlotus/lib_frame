package com.xlotus.lib.frame.dialog;

import android.os.Bundle;

public class DialogParams {
    public int layout = -1;
    public String title;
    public String msg;
    public String subMsg;
    public CharSequence richMsg;
    public String okText;
    public String cancelText;
    public String checkboxInfo;
    public int checkboxImgRes = -1;
    public int contentImg;
    public boolean showFlatButton = true;

    public boolean showCancelBtn = true;
    public boolean showCheckBox = false;
    public boolean couldCancel = true;

    public int selectPosition = 0;
    public String[] selectTitles;
    public String[] selectMessages;
    public boolean[] selectChecks;

    private int btnColor = -1;
    public int btnOkColor = -1;
    public int btnCancelColor = -1;

    public static final String EXTRA_DIALOG_LAYOUT = "layout";
    public static final String EXTRA_MSG = "msg";
    public static final String EXTRA_SUB_MSG = "sub_msg";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_RICH_MSG = "rich_msg";
    public static final String EXTRA_BTN_OK_TEXT = "ok_button";
    public static final String EXTRA_BTN_CANCEL_TEXT = "cancel_button";
    public static final String EXTRA_SHOW_CANCEL = "show_cancel";
    public static final String EXTRA_SHOW_CHECKBOX = "show_checkbox";
    public static final String EXTRA_CHECKBOX_TEXT = "checkbox_text";
    public static final String EXTRA_CHECKBOX_IMG_RES = "checkbox_img_res";
    public static final String EXTRA_CONTENT_IMG = "content_img";
    public static final String EXTRA_SHOW_FLAT_BUTTON = "show_flat_button";
    public static final String EXTRA_BTN_COLOR_RES = "btn_color";
    public static final String EXTRA_BTN_OK_COLOR_RES = "btn_ok_color";
    public static final String EXTRA_BTN_CANCEL_COLOR_RES = "btn_cancel_color";

    public static final String EXTRA_DIALOG_COULD_CANCEL = "dialog_could_cancel";

    //select dialog
    public static final String EXTRA_DIALOG_SELECT_POSITION = "dialog_select_position";
    public static final String EXTRA_DIALOG_SELECT_TITLES = "dialog_select_titles";
    public static final String EXTRA_DIALOG_SELECT_MESSAGES = "dialog_select_message";
    public static final String EXTRA_DIALOG_SELECT_CHECKS = "dialog_select_checks";

    public static DialogParams parseArguments(Bundle args) {
        DialogParams dialogParams = new DialogParams();
        if (args == null)
            return dialogParams;

        if (args.containsKey(EXTRA_DIALOG_LAYOUT))
            dialogParams.layout = args.getInt(EXTRA_DIALOG_LAYOUT);

        if (args.containsKey(EXTRA_TITLE))
            dialogParams.title = args.getString(EXTRA_TITLE);

        if (args.containsKey(EXTRA_MSG))
            dialogParams.msg = args.getString(EXTRA_MSG);

        if (args.containsKey(EXTRA_SUB_MSG))
            dialogParams.subMsg = args.getString(EXTRA_SUB_MSG);

        if (args.containsKey(EXTRA_RICH_MSG))
            dialogParams.richMsg = args.getCharSequence(EXTRA_RICH_MSG);

        if (args.containsKey(EXTRA_BTN_OK_TEXT))
            dialogParams.okText = args.getString(EXTRA_BTN_OK_TEXT);

        if (args.containsKey(EXTRA_BTN_CANCEL_TEXT))
            dialogParams.cancelText = args.getString(EXTRA_BTN_CANCEL_TEXT);

        if (args.containsKey(EXTRA_SHOW_CANCEL))
            dialogParams.showCancelBtn = args.getBoolean(EXTRA_SHOW_CANCEL);

        if (args.containsKey(EXTRA_SHOW_CHECKBOX))
            dialogParams.showCheckBox = args.getBoolean(EXTRA_SHOW_CHECKBOX);

        if (args.containsKey(EXTRA_CHECKBOX_TEXT))
            dialogParams.checkboxInfo = args.getString(EXTRA_CHECKBOX_TEXT);

        if (args.containsKey(EXTRA_CHECKBOX_IMG_RES))
            dialogParams.checkboxImgRes = args.getInt(EXTRA_CHECKBOX_IMG_RES);

        if (args.containsKey(EXTRA_DIALOG_COULD_CANCEL))
            dialogParams.couldCancel = args.getBoolean(EXTRA_DIALOG_COULD_CANCEL);

        if (args.containsKey(EXTRA_DIALOG_SELECT_POSITION))
            dialogParams.selectPosition = args.getInt(EXTRA_DIALOG_SELECT_POSITION);

        if (args.containsKey(EXTRA_CONTENT_IMG))
            dialogParams.contentImg = args.getInt(EXTRA_CONTENT_IMG);

        if (args.containsKey(EXTRA_SHOW_FLAT_BUTTON))
            dialogParams.showFlatButton = args.getBoolean(EXTRA_SHOW_FLAT_BUTTON);

        if (args.containsKey(EXTRA_DIALOG_SELECT_TITLES))
            dialogParams.selectTitles = args.getStringArray(EXTRA_DIALOG_SELECT_TITLES);

        if (args.containsKey(EXTRA_DIALOG_SELECT_MESSAGES))
            dialogParams.selectMessages = args.getStringArray(EXTRA_DIALOG_SELECT_MESSAGES);

        if (args.containsKey(EXTRA_DIALOG_SELECT_CHECKS))
            dialogParams.selectChecks = args.getBooleanArray(EXTRA_DIALOG_SELECT_CHECKS);

        if (args.containsKey(EXTRA_BTN_COLOR_RES)) {
            dialogParams.btnColor = args.getInt(EXTRA_BTN_COLOR_RES);
            dialogParams.btnCancelColor = dialogParams.btnColor;
            dialogParams.btnOkColor = dialogParams.btnColor;
        }

        if (args.containsKey(EXTRA_BTN_OK_COLOR_RES))
            dialogParams.btnOkColor = args.getInt(EXTRA_BTN_OK_COLOR_RES);

        if (args.containsKey(EXTRA_BTN_CANCEL_COLOR_RES))
            dialogParams.btnCancelColor = args.getInt(EXTRA_BTN_CANCEL_COLOR_RES);
        return dialogParams;
    }
}
