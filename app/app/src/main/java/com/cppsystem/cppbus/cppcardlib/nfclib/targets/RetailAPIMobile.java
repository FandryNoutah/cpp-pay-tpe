package com.cppsystem.cppbus.cppcardlib.nfclib.targets;


import com.cppsystem.cppbus.cppcardlib.nfclib.usb.CcidDevice;

public class RetailAPIMobile extends Target {
    private final String meHolderId;

    public void setDescription(String str) {
    }

    public RetailAPIMobile(byte[] bArr, String str, String str2, CcidDevice ccidDevice) {
        super(bArr, str);
        this.meHolderId = str2;
        this.description = "Mobile with RetailAPI cardlet and Adelya identifiers";
    }

    public String getMeHolderId() {
        return this.meHolderId;
    }
}
