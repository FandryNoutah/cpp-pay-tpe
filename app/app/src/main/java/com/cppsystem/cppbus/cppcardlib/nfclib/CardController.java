package com.cppsystem.cppbus.cppcardlib.nfclib;

import android.util.Log;
import android.util.SparseArray;
import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CardChannel;
import com.cppsystem.cppbus.cppcardlib.nfclib.spe.AdelyaCardController;
import com.cppsystem.cppbus.cppcardlib.nfclib.spe.CalypsoUtil;
import com.cppsystem.cppbus.cppcardlib.nfclib.spe.CardletUtil;
import com.cppsystem.cppbus.cppcardlib.nfclib.spe.EidUtil;
import com.cppsystem.cppbus.cppcardlib.nfclib.spe.MWC;
import com.cppsystem.cppbus.cppcardlib.nfclib.spe.OTPassTourismeController;
import com.cppsystem.cppbus.cppcardlib.nfclib.spe.RetailAPIController;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.CalypsoCard;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.ClessCard;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.ContentCard;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.NfcDevice;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.OTCMRTMPass;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.OTPassTourisme;
import com.cppsystem.cppbus.cppcardlib.nfclib.usb.Card;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

public class CardController {
    protected static byte[] APDU_AUTH = {-1, -122, 0, 0, 5, 1, 0, 4, 96, 1};
    protected static byte[] APDU_LOAD_KEY = {-1, -126, 0, 96, 6, -1, 0, 0, 0, 0, 0};
    protected static byte[] APDU_READ = {-1, -80, 0, 0, 0};
    protected final SparseArray<byte[]> blockCache = new SparseArray<>();
    protected CardChannel channel;

    public CardController(CardChannel cardChannel) {
        this.channel = cardChannel;
    }

    public CardChannel getChannel() {
        return this.channel;
    }

    public Card getCard(byte[] bArr, byte[] bArr2, Map<String, Object> map, String str) {
        byte[] bArr3;
        Card adelyaMULCard = null;
        int i = 0;
        String str2 = BadgerConstants.LOG_TAG;
        Card card = null;
        if (bArr2 == null || bArr2.length < 2 || bArr2[0] != 0 || bArr2[1] != 0) {
            bArr3 = bArr2;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Wrong UID ");
            sb.append(Util.byteArrayToString(bArr2));
            Log.v(str2, sb.toString());
            bArr3 = null;
        }
        if (bArr3 != null) {
            if (!(Util.arrayCountElement(bArr3, (byte) 0) == bArr3.length || Util.arrayCountElement(bArr3, (byte) -1) == bArr3.length)) {
                if (bArr3.length == 4 || (bArr3.length == 8 && bArr3[4] == 0 && bArr3[5] == 0 && bArr3[6] == 0 && bArr3[7] == 0)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("UID too short ");
                    sb2.append(Util.byteArrayToString(bArr3));
                    Log.w(str2, sb2.toString());
                }
            }
            bArr3 = null;
        }
        int[] iArr = {1, 8, 9, 2, 6, 3, 4, 5, 7, 999};
        StringBuilder sb3 = new StringBuilder();
        sb3.append("UID avant create card: ");
        sb3.append(Util.byteArrayToString(bArr3));
        Log.v(str2, sb3.toString());
        while (card == null && i < iArr.length) {
            int i2 = i + 1;
            switch (iArr[i]) {
                case 0:
                case 1:
                case 5:
                    break;
                case 2:
                    adelyaMULCard = getAdelyaMULCard(bArr, bArr2, map);
                    break;
                case 3:
                    adelyaMULCard = RetailAPIController.getRetailAPIMobile(bArr, this.channel, map, str);
                    break;
                case 4:
                    adelyaMULCard = getCardlets(bArr, bArr3, map, str);
                    break;
                case 6:
                    adelyaMULCard = getEID(bArr, bArr3, map);
                    break;
                case 7:
                    adelyaMULCard = getMWC(bArr, bArr2, map);
                    break;
                case 8:
                    adelyaMULCard = getOTToulouse(bArr, bArr2, map);
                    break;
                case 9:
                    adelyaMULCard = getOTCMRTM(bArr, bArr2, map);
                    break;
                default:
                    adelyaMULCard = getDefaultCard(bArr, bArr3, map);
                    break;
            }
            card = adelyaMULCard;
            i = i2;
        }
        if (card != null) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Card read: ");
            sb4.append(card.toString());
            Log.i(str2, sb4.toString());
        }
        return card;
    }

    public final byte[] readMemoryBlock(byte b, int i) {
        byte[] bArr = (byte[]) this.blockCache.get(b);
        if (bArr != null && bArr.length == i) {
            return bArr;
        }
        byte[] doReadMemoryBlock = doReadMemoryBlock(b, i);
        this.blockCache.put(b, doReadMemoryBlock);
        return doReadMemoryBlock;
    }

    /* access modifiers changed from: protected */
    public byte[] doReadMemoryBlock(byte b, int i) {
        try {
            APDU_READ[3] = b;
            byte[] transmit = this.channel.transmit(APDU_READ);
            if (transmit == null || transmit.length <= 2) {
                return null;
            }
            return Arrays.copyOfRange(transmit, 0, Math.min(transmit.length - 2, i));
        } catch (Exception e) {
            Log.w(BadgerConstants.LOG_TAG, "Error in readMemoryBlock", e);
            return null;
        }
    }

    public boolean mifareAuth(byte b, byte[] bArr) {
        String str = BadgerConstants.LOG_TAG;
        Log.v(str, "mifareAuth()");
        boolean z = false;
        if (bArr != null && bArr.length == 6) {
            System.arraycopy(bArr, 0, APDU_LOAD_KEY, 5, 6);
        }
        byte[] transmit = this.channel.transmit(APDU_LOAD_KEY);
        if (transmit != null && transmit.length == 2 && transmit[0] == -112) {
            byte[] bArr2 = APDU_AUTH;
            bArr2[7] = b;
            byte[] transmit2 = this.channel.transmit(bArr2);
            if (transmit2 != null && transmit2.length == 2 && transmit2[0] == -112) {
                z = true;
            }
            return z;
        }
        Log.v(str, "Unable to load key");
        return false;
    }

    public byte[] readMifare1KBlock(byte b, byte b2) {
        String str = BadgerConstants.LOG_TAG;
        Log.v(str, "readMifare1KBlock()");
        byte[] bArr = APDU_READ;
        bArr[3] = b;
        bArr[4] = b2;
        byte[] transmit = this.channel.transmit(bArr);
        if (transmit != null && transmit.length >= 2 && transmit[transmit.length - 2] == -112 && transmit[transmit.length - 1] == 0) {
            return Arrays.copyOf(transmit, Math.min(b2, transmit.length - 2));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to read memory block. Result=");
        sb.append(Arrays.toString(transmit));
        Log.d(str, sb.toString());
        return null;
    }

    public String getMifareSectorValue(int i) {
        String str = "UTF-8";
        byte b = (byte) ((i * 4) & 255);
        try {
            StringBuilder sb = new StringBuilder();
            byte b2 = (byte) (b + 1);
            sb.append(new String(readMifare1KBlock(b, (byte) 15), str));
            byte b3 = (byte) (b2 + 1);
            sb.append(new String(readMifare1KBlock(b2, (byte) 15), str));
            sb.append(new String(readMifare1KBlock(b3, (byte) 15), str));
            return sb.toString().trim();
        } catch (UnsupportedEncodingException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("getMifareSectorValue: ");
            sb2.append(e.getMessage());
            Log.w(BadgerConstants.LOG_TAG, sb2.toString());
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public Card getAdelyaMULCard(byte[] bArr, byte[] bArr2, Map<String, Object> map) {
        return AdelyaCardController.getMifareUL(this, bArr, bArr2, map);
    }

    /* access modifiers changed from: protected */
    public Card getDefaultCard(byte[] bArr, byte[] bArr2, Map<String, Object> map) {
        return new ClessCard(bArr, bArr2);
    }

    /* access modifiers changed from: protected */
    public Card getCardlets(byte[] bArr, byte[] bArr2, Map<String, Object> map, String str) {
        String cardletID = CardletUtil.getCardletID(this.channel, str);
        if (cardletID != null) {
            return new NfcDevice(bArr, cardletID);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Card getCalypso(CardChannel cardChannel, byte[] bArr, byte[] bArr2, Map<String, Object> map) {
        String str = BadgerConstants.LOG_TAG;
        byte[] calypsoSerialNumber = CalypsoUtil.getCalypsoSerialNumber(cardChannel);
        Card card = null;
        if (calypsoSerialNumber == null) {
            return null;
        }
        try {
            CalypsoCard calypsoCard = new CalypsoCard(bArr, calypsoSerialNumber);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("Calypso card, uid=");
                sb.append(Util.byteArrayToString(bArr2));
                sb.append(" SN=");
                sb.append(Util.byteArrayToString(calypsoSerialNumber));
                Log.i(str, sb.toString());
                return calypsoCard;
            } catch (Exception e) {
                e = e;
                card = calypsoCard;
                Log.w(str, "Calypso error", e);
                return card;
            }
        } catch (Exception e2) {
            Exception e = e2;
            Log.w(str, "Calypso error", e);
            return card;
        }
    }

    /* access modifiers changed from: protected */
    public Card getEID(byte[] bArr, byte[] bArr2, Map<String, Object> map) {
        return EidUtil.readEIDCard(this.channel);
    }

    /* access modifiers changed from: protected */
    public Card getOTToulouse(byte[] bArr, byte[] bArr2, Map<String, Object> map) {
        if (bArr2 != null && bArr2.length == 7) {
            byte[] copyOf = Arrays.copyOf(bArr2, 8);
            String oTTTourismID = OTPassTourismeController.getOTTTourismID(this);
            if (oTTTourismID != null) {
                try {
                    return new OTPassTourisme(bArr, copyOf, oTTTourismID);
                } catch (Exception e) {
                    Log.e(BadgerConstants.LOG_TAG, "OT Toulouse error", e);
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public OTCMRTMPass getOTCMRTM(byte[] bArr, byte[] bArr2, Map<String, Object> map) {
        if (bArr2 != null && bArr2.length == 7) {
            byte[] copyOf = Arrays.copyOf(bArr2, 8);
            String rTMProductCode = OTPassTourismeController.getRTMProductCode(this);
            if (rTMProductCode != null) {
                try {
                    return new OTCMRTMPass(bArr, copyOf, rTMProductCode);
                } catch (Exception e) {
                    Log.e(BadgerConstants.LOG_TAG, "OTCM RTM error", e);
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0034, code lost:
        if ((r0 & r4) != false) goto L_0x0036;
     */
    public Card getMWC(byte[] bArr, byte[] bArr2, Map<String, Object> map) {
        Map mWCCard = MWC.getMWCCard(this);
        if (mWCCard == null) {
            return null;
        }
        if (bArr2 != null) {
            if (bArr2.length != 4) {
                if (bArr2.length >= 8) {
                    boolean z = true;
                    boolean z2 = (bArr2[4] == 0) & (bArr2[5] == 0) & (bArr2[6] == 0);
                    if (bArr2[7] != 0) {
                        z = false;
                    }
                }
            }
            byte[] bArr3 = {77, 67, 32, 19, 0, 0, 0, 0};
            System.arraycopy(bArr2, 0, bArr3, 4, 4);
            bArr2 = bArr3;
        }
        return new ContentCard(bArr, bArr2, mWCCard);
    }
}
