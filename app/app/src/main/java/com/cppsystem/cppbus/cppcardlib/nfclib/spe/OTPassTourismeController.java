package com.cppsystem.cppbus.cppcardlib.nfclib.spe;


import com.cppsystem.cppbus.cppcardlib.nfclib.CardController;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BitField;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class OTPassTourismeController {
    private static final Logger logger = Logger.getLogger(BadgerConstants.LOG_TAG);

    public static String getOTTTourismID(CardController cardController) {
        return null;
    }

    public static String getRTMProductCode(CardController cardController) {
        byte[] readMemoryBlock = cardController.readMemoryBlock((byte) 6, 4);
        String str = null;
        if (readMemoryBlock == null) {
            return null;
        }
        byte[] byteArray = new BitField(reverse(readMemoryBlock)).getSubField(1, 16).toByteArray();
        if (byteArray != null && byteArray.length == 2) {
            short s = ByteBuffer.wrap(byteArray).getShort();
            if (s >= 15401 && s <= 15409) {
                str = Short.toString(s);
            }
        }
        return str;
    }

    private static byte[] reverse(byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i] = bArr[(bArr.length - i) - 1];
        }
        return bArr2;
    }
}
