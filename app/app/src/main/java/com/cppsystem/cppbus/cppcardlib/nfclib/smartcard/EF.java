package com.cppsystem.cppbus.cppcardlib.nfclib.smartcard;

import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CardChannel;
import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CommandAPDU;
import com.cppsystem.cppbus.cppcardlib.nfclib.comm.ResponseAPDU;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Tlv;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Util;

import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EF {
    private static byte[] APDU_GET_RESPONSE = {0, -64, 0, 0, 0};
    private static byte[] APDU_READ_BINARY = {0, -80, 0, 0, 9};
    private static byte[] APDU_SELECT = {0, -92, 2, 4, 2, 0, 0};
    private static final Logger logger = Logger.getLogger(BadgerConstants.LOG_TAG);

    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        logger.log(java.util.logging.Level.INFO, "EF selectFile no length info");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x012c, code lost:
        r0 = e;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x0122 */
    public static int selectFile(byte[] bArr, byte b, byte b2, CardChannel cardChannel) {
        ResponseAPDU responseAPDU;
        byte[] bArr2 = bArr;
        byte b3 = b2;
        CardChannel cardChannel2 = cardChannel;
        byte[] bArr3 = APDU_SELECT;
        bArr3[2] = b;
        bArr3[3] = b3;
        if (bArr2 == null || bArr2.length == 0) {
            return -1;
        }
        int i = 0;
        if ((b & 8) == 8 || (b & 4) == 4) {
            byte[] bArr4 = new byte[((APDU_SELECT.length - 2) + bArr2.length)];
            System.arraycopy(APDU_SELECT, 0, bArr4, 0, APDU_SELECT.length);
            System.arraycopy(bArr2, 0, bArr4, 5, bArr2.length);
            bArr4[4] = (byte) bArr2.length;
            responseAPDU = cardChannel2.transmit(new CommandAPDU(bArr4));
            if (responseAPDU != null) {
                if (responseAPDU.getSW1() == 144) {
                    logger.log(Level.FINE, "Path selected: {0}", Util.byteArrayToString(bArr));
                    logger.log(Level.FINEST, "Response: {0}", Util.byteArrayToString(responseAPDU.getBytes()));
                }
            }
            String str = BadgerConstants.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to select path: ");
            sb.append(Util.byteArrayToString(bArr));
            sb.append(", result: ");
            sb.append(Util.byteArrayToString(responseAPDU.getBytes()));
            Log.i(str, sb.toString());
            return -1;
        }
        responseAPDU = null;
        int i2 = 0;
        while (i2 < bArr2.length) {
            try {
                System.arraycopy(bArr2, i2, APDU_SELECT, 5, 2);
                responseAPDU = cardChannel2.transmit(new CommandAPDU(APDU_SELECT));
                if (responseAPDU != null) {
                    if (responseAPDU.getSW1() == 144) {
                        logger.log(Level.FINE, "File selected: {0}", Util.byteArrayToString(new byte[]{bArr2[i2], bArr2[i2 + 1]}));
                        i2 += 2;
                    }
                }
                logger.log(Level.INFO, "Unable to select: {0}, result: {1}", new Object[]{Util.byteArrayToString(new byte[]{bArr2[i2], bArr2[i2 + 1]}), Util.byteArrayToString(responseAPDU.getBytes())});
                return -1;
            } catch (Exception e) {
                e = e;
                i = -1;
                logger.log(Level.INFO, "EF selectFile exception: {0}", e.toString());
                return i;
            }
        }
        if (b3 != 12) {
            logger.log(Level.FINEST, "r.getData(): {0}", Util.byteArrayToString(responseAPDU.getData()));
            byte[] GetTLV = Tlv.GetTLV(responseAPDU.getData(), (byte) 98);
            byte[] GetTLV2 = Tlv.GetTLV(GetTLV, (byte) java.lang.Byte.MIN_VALUE);
            logger.log(Level.FINEST, "fileInfo {0}", Util.byteArrayToString(GetTLV));
            logger.log(Level.FINEST, "tlvLength {0}", Util.byteArrayToString(GetTLV2));
            if (GetTLV2 != null && GetTLV2.length == 2) {
                i = (GetTLV2[1] & 255) << ((GetTLV2[0] & 255) + 8);
            }
        }
        return i;
    }

    public static byte[] getResponse(byte b, CardChannel cardChannel) {
        try {
            APDU_GET_RESPONSE[4] = b;
            return cardChannel.transmit(new CommandAPDU(APDU_GET_RESPONSE)).getBytes();
        } catch (Exception e) {
            logger.log(Level.INFO, "getResponse exception: {0}", e.toString());
            return null;
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=byte, code=int, for r10v0, types: [byte, int] */
    public static byte[] getFullResponse(int i, CardChannel cardChannel) {
        byte[] bArr;
        ResponseAPDU transmit;
        ByteBuffer allocate = ByteBuffer.allocate(65536);
        loop0:
        while (true) {
            int i2 = i & 255;
            while (true) {
                bArr = null;
                if (i2 < 0) {
                    break loop0;
                }
                try {
                    APDU_GET_RESPONSE[4] = (byte) i2;
                    transmit = cardChannel.transmit(new CommandAPDU(APDU_GET_RESPONSE));
                    if (transmit != null && transmit.getSW1() == 108) {
                        break;
                    }
                    if (transmit != null) {
                        if (transmit.getSW1() == 144) {
                            byte[] bytes = transmit.getBytes();
                            logger.log(Level.FINEST, "Data from get response: {0} {1}", new Object[]{Byte.valueOf(APDU_READ_BINARY[4]), Util.byteArrayToString(bytes)});
                            allocate.put(bytes);
                            i2 = -1;
                        }
                    }
                    logger.log(Level.INFO, "Unable to get response: {0}", Util.byteArrayToString(transmit.getBytes()));
                    allocate = null;
                    i2 = -1;
                } catch (Exception e) {
                    logger.log(Level.INFO, "EF getFullResponse exception: {0}", e.toString());
                }
            }
            return bArr;
           // i = transmit.getSW2();
        }
        if (allocate != null) {
            bArr = new byte[allocate.position()];
            allocate.rewind();
            allocate.get(bArr);
        }
        logger.log(Level.FINEST, "Data in response: {0}", Util.byteArrayToString(bArr));
        return bArr;
    }

    public static byte[] readTransparent(byte b, CardChannel cardChannel) {
        byte[] bArr;
        ByteBuffer allocate = ByteBuffer.allocate(65536);
        byte b2 = (byte) (b & 255);
        byte b3 = 0;
        while (true) {
            bArr = null;
            if (b2 < 0) {
                break;
            }
            try {
                byte b4 = (byte) (b3 + 1);
                APDU_READ_BINARY[2] = b3;
                APDU_READ_BINARY[4] = (byte) b2;
                logger.log(Level.FINEST, "Command: {0}", Util.byteArrayToString(APDU_READ_BINARY));
                ResponseAPDU transmit = cardChannel.transmit(new CommandAPDU(APDU_READ_BINARY));
                if (transmit == null || transmit.getSW1() != 108) {
                    if (transmit != null) {
                        if (transmit.getSW1() == 144) {
                            byte[] data = transmit.getData();
                            boolean z = true;
                            logger.log(Level.FINEST, "Data read: {0} {1} {2}", new Object[]{Integer.valueOf(b4 - 1), Byte.valueOf(APDU_READ_BINARY[4]), Util.byteArrayToString(data)});
                            allocate.put(data);
                            boolean z2 = b2 > 0;
                            boolean z3 = b2 == 0;
                            if (data.length >= 256) {
                                z = false;
                            }
                            if (z2 || (z3 & z)) {
                                logger.log(Level.FINEST, "Length last packet read: {0}", Integer.valueOf(data.length));
                                b3 = b4;
                                b2 = -1;
                            } else {
                                b3 = b4;
                            }
                        }
                    }
                    logger.log(Level.INFO, "Unable to read file: {0}", Util.byteArrayToString(transmit.getBytes()));
                    allocate = null;
                    b3 = b4;
                    b2 = -1;
                } else {
                    byte sw2 = (byte) (transmit.getSW2() & 255);
                    b3 = (byte) (b4 - 1);
                    b2 = sw2;
                }
            } catch (Exception e) {
                logger.log(Level.INFO, "EF readTransparent exception: {0}", e.toString());
            }
        }
        if (allocate != null) {
            bArr = new byte[allocate.position()];
            allocate.rewind();
            allocate.get(bArr);
        }
        logger.log(Level.FINEST, "Data read: {0}", Util.byteArrayToString(bArr));
        return bArr;
    }
}
