package com.cppsystem.cppbus.cppcardlib.nfclib.usb;

public class CcidException extends Exception {
    private static final long serialVersionUID = -330918879701556155L;

    public CcidException(String str) {
        super(str);
    }

    public CcidException(String str, Exception exc) {
        super(str, exc);
    }
}
