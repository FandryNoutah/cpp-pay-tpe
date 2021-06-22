package com.cppsystem.cppbus.cppcardlib.nfclib.targets;


import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.usb.Card;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Util;

public abstract class Target extends Card {
    String chipType = "UNKNOWN";
    String description = "";
    String suid;
    String systemType = null;

    public Target(byte[] bArr, byte[] bArr2) {
        super(bArr, bArr2);
        this.suid = Util.byteArrayToString(bArr2);
        //Log.e("Card uid",bArr2.toString());
    }

    public Target(byte[] bArr, String str) {
        super(bArr, Util.hexStringToArray(str));
        this.suid = str;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        if (str == null) {
            str = "Unknown card";
        }
        this.description = str;
    }

    public String getSuid() {
        return this.suid;
    }

    public String getChipType() {
        return this.chipType;
    }

    public String getSystemType() {
        return this.systemType;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [uid=");
        sb.append(this.suid);
        sb.append("]");
        return sb.toString();
    }
}
