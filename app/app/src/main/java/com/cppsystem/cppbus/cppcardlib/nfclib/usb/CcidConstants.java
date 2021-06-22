package com.cppsystem.cppbus.cppcardlib.nfclib.usb;

public class CcidConstants {
    public static final byte[] APDU_GET_UID = {-1, -54, 0, 0, 0};
    public static final int HANDLER_WHAT_CARD = 0;
    public static final int HANDLER_WHAT_ERROR = -1;
    public static final int HANDLER_WHAT_READER_DISCONNECTED = -10;
    protected static final String LOG_TAG = "AdelyaCCID";
}
