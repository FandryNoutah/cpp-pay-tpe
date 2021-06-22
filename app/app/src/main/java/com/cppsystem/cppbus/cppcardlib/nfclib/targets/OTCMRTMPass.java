package com.cppsystem.cppbus.cppcardlib.nfclib.targets;


import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;

public class OTCMRTMPass extends ClessCard {
    public OTCMRTMPass(byte[] bArr, byte[] bArr2, String str) throws Exception {
        super(bArr, bArr2);
        this.description = "OTCM RTM tourism pass";
        this.chipType = BadgerConstants.CHIP_MIFARE_UL;
        this.systemType = BadgerConstants.CARD_OTCM_RTM;
        setCardNumber(str);
    }
}
