package com.xlotus.lib.frame.stats;

public class PVEBuilder {

    private StringBuilder mStringBuilder;

    private PVEBuilder() {
        mStringBuilder = new StringBuilder();
    }

    private PVEBuilder(String page) {
        mStringBuilder = new StringBuilder(page);
    }

    public PVEBuilder append(String s) {
        mStringBuilder.append(s);
        return this;
    }

    public String build() {
        return mStringBuilder.toString();
    }

    public static PVEBuilder create() {
        return new PVEBuilder();
    }

    public static PVEBuilder create(String page) {
        return new PVEBuilder(page);
    }

    public PVEBuilder clone() {
        return new PVEBuilder(mStringBuilder.toString());
    }

    public static final String PAGE_ME = "/Me";

    public static final String PAGE_FLASH = "/Flash";
    public static final String PAGE_AGREEMENT = "/Agreement";

    public static final String PAGE_USER_PROFILE = "/UerProfile";
    public static final String PAGE_PERSON_INFO = "/PersonInfo";
    public static final String PAGE_MAIN_ACTIVITY = "/MainActivity";

    public static final String PAGE_LOGIN = "/Login";
    public static final String PAGE_INVITE = "/Invite";

    public static final String AREA_TITLEBAR = "/Titlebar";

    public static final String AREA_FOOTER = "/Footer";


    //element or action
    public static final String SEPARATOR = "/";
    public static final String ELEMENT_NONE = "/0";
    public static final String ELEMENT_OK = "/ok";
    public static final String ELEMENT_CANCEL = "/cancel";
    public static final String ELEMENT_CLICK = "/click";
    public static final String ELEMENT_BACK_KEY = "/back_key";
    public static final String ELEMENT_CLOSE = "/close";
    public static final String ELEMENT_HIDE = "/hide";
    public static final String ELEMENT_SHARE = "/share";

    public static final String ELEMENT_LOGIN = "/login";
    public static final String ELEMENT_NOT_YET = "/not_yet";
    public static final String ELEMENT_YES = "/yes";
    public static final String ELEMENT_NO = "/no";
    public static final String ELEMENT_ABANDON = "/abandon";

    public static final String ELEMENT_RETRY = "/retry";
    public static final String ELEMENT_ASK_FOR_HELP = "/ask_for_help";
    public static final String ELEMENT_GOT_IT = "/got_it";

    @Override
    public String toString() {
        return build();
    }
}
