package com.cppsystem.cppbus.cppcardlib.nfclib.targets;

import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;

;

public class OTPassTourisme extends ClessCard {
    public OTPassTourisme(byte[] bArr, byte[] bArr2, String str) throws Exception {
        super(bArr, bArr2);
        this.chipType = BadgerConstants.CHIP_MIFARE_DESFIRE;
        this.systemType = BadgerConstants.CARD_OT_TOULOUSE;
        setCardNumber(str);
    }
}
