package com.cppsystem.cppbus.cppcardlib.nfclib.targets;

import java.util.Locale;

public class NfcDevice extends Target {
    private Boolean optEmail;
    private Boolean optMail;
    private Boolean optMobile;

    public void setDescription(String str) {
    }

    public NfcDevice(byte[] bArr, byte[] bArr2) {
        super(bArr, bArr2);
        this.optEmail = null;
        this.optMobile = null;
        this.optMail = null;
        this.suid = uidCorrection(this.suid);
        this.description = "NFC mobile with cardlet from Adelya";
    }

    public NfcDevice(byte[] bArr, String str) {
        super(bArr, str);
        this.optEmail = null;
        this.optMobile = null;
        this.optMail = null;
    }

    private static String uidCorrection(String str) {
        String upperCase = str.toUpperCase(Locale.getDefault());
        while (upperCase.endsWith("F")) {
            upperCase = upperCase.substring(0, upperCase.length() - 1);
        }
        while (upperCase.length() < 26) {
            StringBuilder sb = new StringBuilder();
            sb.append(upperCase);
            sb.append("0");
            upperCase = sb.toString();
        }
        return upperCase;
    }

    public Boolean getOptEmail() {
        return this.optEmail;
    }

    public void setOptEmail(Boolean bool) {
        this.optEmail = bool;
    }

    public Boolean getOptMobile() {
        return this.optMobile;
    }

    public void setOptMobile(Boolean bool) {
        this.optMobile = bool;
    }

    public Boolean getOptMail() {
        return this.optMail;
    }

    public void setOptMail(Boolean bool) {
        this.optMail = bool;
    }
}
