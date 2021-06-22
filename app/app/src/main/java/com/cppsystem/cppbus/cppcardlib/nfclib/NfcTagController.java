package com.cppsystem.cppbus.cppcardlib.nfclib;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcV;
import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CardChannelNFC;
import com.cppsystem.cppbus.cppcardlib.nfclib.spe.AdelyaCardController;
import com.cppsystem.cppbus.cppcardlib.nfclib.spe.CalypsoUtil;
import com.cppsystem.cppbus.cppcardlib.nfclib.spe.CardletUtil;
import com.cppsystem.cppbus.cppcardlib.nfclib.spe.MifareClassicUtil;
import com.cppsystem.cppbus.cppcardlib.nfclib.spe.MifareUltralightUtil;
import com.cppsystem.cppbus.cppcardlib.nfclib.spe.OTPassTourismeController;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.CalypsoCard;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.ClessCard;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.ContentCard;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.NfcDevice;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.OTCMRTMPass;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.OTPassTourisme;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.Target;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class NfcTagController {
    public static Target readTag(Tag tag, String str) {
        String str2 = BadgerConstants.LOG_TAG;
        Log.v(str2, "NEW readTag");
        String[] techList = tag.getTechList();
        Target target = null;
        if (Util.arrayIndexOf(techList, MifareUltralight.class.getName()) >= 0) {
            MifareUltralight mifareUltralight = MifareUltralight.get(tag);
            try {
                mifareUltralight.connect();
              //  mifareUltralight.
                target = getOTCMRTM(mifareUltralight);
                if (target == null) {
                    target = getMifareUL(mifareUltralight);
                }
                mifareUltralight.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(str2, "MifareUltralight error", e);
            }
        } else if (Util.arrayIndexOf( techList, IsoDep.class.getName()) >= 0) {
            IsoDep isoDep = IsoDep.get(tag);
            if (isoDep != null) {
                try {
                    connect(isoDep);
                    Target calypso = getCalypso(isoDep);
                    if (calypso == null) {
                        calypso = getOTToulouse(isoDep);
                    }
                    if (calypso == null) {
                        calypso = getCardlets(isoDep, str);
                    }
                    disconnect(isoDep);
                } catch (IOException e2) {
                    Log.e(str2, "IsoDep error", e2);
                }
            }
        } else if (Util.arrayIndexOf( techList, MifareClassic.class.getName()) >= 0) {
            target = getMWC(tag);
        } else if (Util.arrayIndexOf( techList, NfcV.class.getName()) >= 0) {
            target = get15693(NfcV.get(tag));
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Unsuported tag type: ");
            sb.append(tag.toString());
            Log.i(str2, sb.toString());
        }
        if (target != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Card read: ");
            sb2.append(target.toString());
            Log.i(str2, sb2.toString());
        }
        return target;
    }

    private static ClessCard getMifareUL(MifareUltralight mifareUltralight) {
        byte[] id = mifareUltralight.getTag().getId();
        if (id == null || id.length != 7) {
            return null;
        }
        ClessCard clessCard = new ClessCard((byte[]) null, Arrays.copyOf(id, 8));
        clessCard.setCardNumber(AdelyaCardController.getMifareULCardNumber(getNewCardController(new CardChannelNFC(mifareUltralight))));
        return clessCard;
    }

    private static Target getCardlets(IsoDep isoDep, String str) {
        String cardletID = CardletUtil.getCardletID(new CardChannelNFC(isoDep), str);
        StringBuilder sb = new StringBuilder();
        sb.append("CardletId=");
        sb.append(cardletID);
        Log.d(BadgerConstants.LOG_TAG, sb.toString());
        if (cardletID != null) {
            return new NfcDevice((byte[]) null, cardletID);
        }
        return null;
    }

    protected static Target getOTToulouse(IsoDep isoDep) {
        String oTTTourismID = OTPassTourismeController.getOTTTourismID(getNewCardController(new CardChannelNFC(isoDep)));
        StringBuilder sb = new StringBuilder();
        sb.append("tourismID=");
        sb.append(oTTTourismID);
        String sb2 = sb.toString();
        String str = BadgerConstants.LOG_TAG;
        Log.d(str, sb2);
        if (oTTTourismID == null || "".equals(oTTTourismID)) {
            return null;
        }
        byte[] id = isoDep.getTag().getId();
        if (id != null && id.length == 7) {
            id = Arrays.copyOf(id, 8);
        }
        try {
            return new OTPassTourisme(null, id, oTTTourismID);
        } catch (Exception e) {
            Log.e(str, "OT Toulouse error", e);
            return null;
        }
    }

    protected static OTCMRTMPass getOTCMRTM(MifareUltralight mifareUltralight) {
        String rTMProductCode = OTPassTourismeController.getRTMProductCode(getNewCardController(new CardChannelNFC(mifareUltralight)));
        StringBuilder sb = new StringBuilder();
        sb.append("RTMProductCode=");
        sb.append(rTMProductCode);
        String sb2 = sb.toString();
        String str = BadgerConstants.LOG_TAG;
      //  Log.e(str, sb2);
        if (rTMProductCode == null || "".equals(rTMProductCode)) {
            return null;
        }
        byte[] id = mifareUltralight.getTag().getId();
        if (id != null && id.length == 7) {
            id = Arrays.copyOf(id, 8);
        }
        try {
            return new OTCMRTMPass(null, id, rTMProductCode);
        } catch (Exception e) {
            Log.e(str, "OTCM RTM error", e);
            return null;
        }
    }

    private static Target getCalypso(IsoDep isoDep) {
        String str = BadgerConstants.LOG_TAG;
        byte[] calypsoSerialNumber = CalypsoUtil.getCalypsoSerialNumber(new CardChannelNFC(isoDep));
        Target target = null;
        if (calypsoSerialNumber == null) {
            return null;
        }
        try {
            CalypsoCard calypsoCard = new CalypsoCard(null, calypsoSerialNumber);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("Calypso card, uid=");
                sb.append(Util.byteArrayToString(isoDep.getTag().getId()));
                sb.append(" SN=");
                sb.append(Util.byteArrayToString(calypsoSerialNumber));
                Log.i(str, sb.toString());
                return calypsoCard;
            } catch (Exception e) {
                e = e;
                target = calypsoCard;
                Log.w(str, "Calypso error",  e);
                return target;
            }
        } catch (Exception e2) {
            Exception e = e2;
            Log.w(str, "Calypso error", e);
            return target;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0027, code lost:
        if (((r8[4] == 0) & (r8[7] == 0)) != false) goto L_0x0029;
     */
    private static Target getMWC(Tag tag) {
        try {
            Map mwcInfos = MifareClassicUtil.getMwcInfos(MifareClassic.get(tag));
            if (mwcInfos == null) {
                return null;
            }
            byte[] id = tag.getId();
            if (id != null) {
                if (id.length != 4) {
                }
                byte[] bArr = {77, 67, 32, 19, 0, 0, 0, 0};
                System.arraycopy(id, 0, bArr, 4, 4);
                id = bArr;
            }
            return new ContentCard(id, mwcInfos);
        } catch (Exception e) {
            Log.e(BadgerConstants.LOG_TAG, "Error in getMWC", e);
            return null;
        }
    }

    private static Target get15693(NfcV nfcV) {
        byte[] id = nfcV.getTag().getId();
        if (id == null || id.length != 8) {
            return null;
        }
        ClessCard clessCard = new ClessCard((byte[]) null, Arrays.copyOf(id, 8));
        clessCard.setDescription("15693 Card");
        return clessCard;
    }

    private static void connect(IsoDep isoDep) throws IOException {
        if (!isoDep.isConnected()) {
            isoDep.connect();
        }
    }

    private static void disconnect(IsoDep isoDep) throws IOException {
        if (isoDep.isConnected()) {
            isoDep.close();
            Log.d(BadgerConstants.LOG_TAG, "IsoDep disconnected");
        }
    }

    private static CardController getNewCardController(CardChannelNFC cardChannelNFC) {
        return new CardController(cardChannelNFC) {
            /* access modifiers changed from: protected */
            public byte[] doReadMemoryBlock(byte b, int i) {
                CardChannelNFC cardChannelNFC = (CardChannelNFC) getChannel();
                if (cardChannelNFC.getMul() != null) {
                    return MifareUltralightUtil.readMemoryBlock(cardChannelNFC.getMul(), b, i);
                }
                return null;
            }
        };
    }

    private static class T {
    }
}
