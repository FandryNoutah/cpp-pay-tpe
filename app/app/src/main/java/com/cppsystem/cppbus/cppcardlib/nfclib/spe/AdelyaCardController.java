package com.cppsystem.cppbus.cppcardlib.nfclib.spe;


import com.cppsystem.cppbus.cppcardlib.nfclib.CardController;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.ClessCard;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.eID;
import com.cppsystem.cppbus.cppcardlib.nfclib.usb.Card;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdelyaCardController {
    private static final Logger logger = Logger.getLogger(BadgerConstants.LOG_TAG);

    public static Card getMifareUL(CardController cardController, byte[] bArr, byte[] bArr2, Map<String, Object> map) {
        if (bArr2 == null || bArr2.length != 7) {
            return null;
        }
        ClessCard clessCard = new ClessCard(bArr, Arrays.copyOf(bArr2, 8));
        clessCard.setCardNumber(getMifareULCardNumber(cardController));
        return clessCard;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0052  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00aa  */
    /* JADX WARNING: Removed duplicated region for block: B:48:? A[RETURN, SYNTHETIC] */
    public static String getMifareULCardNumber(CardController cardController) {
        String str = null;
        String str2;
        byte[] readMemoryBlock = cardController.readMemoryBlock((byte) 4, 4);
        byte[] readMemoryBlock2 = cardController.readMemoryBlock((byte) 5, 4);
        byte[] readMemoryBlock3 = cardController.readMemoryBlock((byte) 6, 4);
        byte[] readMemoryBlock4 = cardController.readMemoryBlock((byte) 7, 4);
        boolean z = true;
        String str3 = "Error reading card number: {0}";
        if (((readMemoryBlock != null) & (readMemoryBlock2 != null) & (readMemoryBlock3 != null)) && (readMemoryBlock4 != null)) {
            try {
                str = blockToString(readMemoryBlock4, blockToString(readMemoryBlock3, blockToString(readMemoryBlock2, blockToString(readMemoryBlock, null))));
            } catch (Exception e) {
                logger.log(Level.FINE, str3, e.getMessage());
            }
            if (str == null) {
                byte[] readMemoryBlock5 = cardController.readMemoryBlock(eID.TLV_BIRTHDATE, 4);
                byte[] readMemoryBlock6 = cardController.readMemoryBlock(eID.TLV_SEX, 4);
                byte[] readMemoryBlock7 = cardController.readMemoryBlock((byte) 14, 4);
                byte[] readMemoryBlock8 = cardController.readMemoryBlock((byte) 15, 4);
                boolean z2 = (readMemoryBlock5 != null) & (readMemoryBlock6 != null) & (readMemoryBlock7 != null);
                if (readMemoryBlock8 == null) {
                    z = false;
                }
                if (z2 && z) {
                    try {
                        str2 = blockToString(readMemoryBlock8, blockToString(readMemoryBlock7, blockToString(readMemoryBlock6, blockToString(readMemoryBlock5, null))));
                    } catch (Exception e2) {
                        logger.log(Level.FINE, str3, e2.getMessage());
                        str2 = null;
                    }
                    if ("".equals(str2)) {
                        return null;
                    }
                    return str2;
                }
            }
            str2 = str;
            if ("".equals(str2)) {
            }
        }
        str = null;
        if (str == null) {
        }
        str2 = str;
        if ("".equals(str2)) {
        }
        return null;
    }

    private static String blockToString(byte[] bArr, String str) throws Exception {
        if (str == null) {
            str = "";
        }
        StringBuilder sb = new StringBuilder(str);
        for (byte b : bArr) {
            if ((b >= 48 && b <= 57) || ((b >= 65 && b <= 90) || (b >= 97 && b <= 122))) {
                sb.append((char) b);
            } else if (b != 0 || sb.length() > 0) {
                throw new Exception("Error in block format");
            }
        }
        return sb.toString().toUpperCase(Locale.FRANCE);
    }
}
