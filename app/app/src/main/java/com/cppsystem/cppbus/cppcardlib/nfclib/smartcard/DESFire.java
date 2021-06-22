package com.cppsystem.cppbus.cppcardlib.nfclib.smartcard;


import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CardChannel;
import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CommandAPDU;
import com.cppsystem.cppbus.cppcardlib.nfclib.comm.ResponseAPDU;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Util;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DESFire {
    protected static final byte[] APDU_READ_DESFIRE_FILE = {-112, -67, 0, 0, 7, -1, -1, 0, 0, -1, 0, 0, 0};
    protected static final byte[] APDU_SELECT_APPLI_DESFIRE = {-112, 90, 0, 0, 3, 48, 5, -14, 0};
    private static final Logger logger = Logger.getLogger(BadgerConstants.LOG_TAG);

    public static boolean selectAppli(CardChannel cardChannel) {
        try {
            ResponseAPDU transmit = cardChannel.transmit(new CommandAPDU(APDU_SELECT_APPLI_DESFIRE));
            if (transmit != null) {
                if ((transmit.getSW1() & 240) == 144) {
                    logger.log(Level.FINE, "DESFire AID selected ");
                    logger.log(Level.FINEST, "Response: {0}", Util.byteArrayToString(transmit.getBytes()));
                    return true;
                }
            }
            logger.log(Level.INFO, "Unable to select DESFire application");
            return false;
        } catch (Exception e) {
            logger.log(Level.INFO, "DESFire select AID exception: {0}", e.toString());
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0092, code lost:
        logger.log(java.util.logging.Level.FINE, "Unable to read file: ID={0} result={1}", new java.lang.Object[]{java.lang.Byte.valueOf(r16), com.adelya.badger.util.Util.byteArrayToString(r7.getBytes())});
     */
    public static byte[] readFile(byte b, byte b2, byte b3, CardChannel cardChannel) {
        int i = b2 & 255;
        byte b4 = (byte) (b3 & 255);
        int i2 = b4 + i;
        ByteBuffer allocate = ByteBuffer.allocate(b4);
        int i3 = 0;
        int i4 = 0;
        while (true) {
            if (i >= i2) {
                break;
            }
            try {
                byte min = (byte) Math.min(58, i2 - i);
                APDU_READ_DESFIRE_FILE[5] = b;
                APDU_READ_DESFIRE_FILE[6] = (byte) i;
                APDU_READ_DESFIRE_FILE[9] = min;
                ResponseAPDU transmit = cardChannel.transmit(new CommandAPDU(APDU_READ_DESFIRE_FILE));
                if (transmit == null) {
                    break;
                } else if ((transmit.getSW1() & 240) != 144) {
                    break;
                } else if (transmit.getData().length == 0) {
                    logger.log(Level.FINE, "Data empty: offset={0} file={1}", new Object[]{Integer.valueOf(i), Byte.valueOf(b)});
                    break;
                } else {
                    byte[] data = transmit.getData();
                    i4 += data.length;
                    logger.log(Level.FINE, "Data read: {0} {1} {2}", new Object[]{Byte.valueOf(b), Integer.valueOf(i), Util.byteArrayToString(data)});
                    allocate.put(data);
                    i += min;
                }
            } catch (Exception e) {
                logger.log(Level.INFO, "DESFire readFile exception: {0}", e.toString());
                return null;
            }
        }
        i3 = i4;
        if (i3 <= 0) {
            return null;
        }
        byte[] bArr = new byte[allocate.position()];
        allocate.rewind();
        allocate.get(bArr);
        return bArr;
    }
}
