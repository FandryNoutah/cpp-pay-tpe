package com.cppsystem.cppbus.cppcardlib.nfclib.smartcard;


import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CardChannel;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.eID;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EFTransparent extends EF {
    private static final Logger logger = Logger.getLogger(BadgerConstants.LOG_TAG);
    private byte[] data;

    private EFTransparent(byte[] bArr) {
        this.data = bArr;
    }

    public static EFTransparent getFile(byte[] bArr, CardChannel cardChannel) {
        int selectFile = EF.selectFile(bArr, (byte) 2, eID.TLV_BIRTHDATE, cardChannel);
        if (selectFile >= 0) {
            byte[] readTransparent = EF.readTransparent((byte) (selectFile & 255), cardChannel);
            if (readTransparent == null || readTransparent.length <= 0) {
                return null;
            }
            return new EFTransparent(readTransparent);
        }
        logger.log(Level.INFO, "Unable to select EF file");
        return null;
    }

    public byte[] getData() {
        return this.data;
    }
}
