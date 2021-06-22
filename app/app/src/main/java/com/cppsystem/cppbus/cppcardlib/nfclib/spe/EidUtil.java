package com.cppsystem.cppbus.cppcardlib.nfclib.spe;

import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CardChannel;
import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CommandAPDU;
import com.cppsystem.cppbus.cppcardlib.nfclib.comm.ResponseAPDU;
import com.cppsystem.cppbus.cppcardlib.nfclib.smartcard.EFTransparent;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.eID;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Util;

//import kotlin.jvm.internal.ByteCompanionObject;

public class EidUtil {
    /* JADX WARNING: Removed duplicated region for block: B:11:0x008c A[Catch:{ Exception -> 0x00e1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x008e A[Catch:{ Exception -> 0x00e1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x00a3 A[Catch:{ Exception -> 0x00e1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    public static eID readEIDCard(CardChannel cardChannel) {
        boolean z = false;
        String str = "eID applet selected: ";
        String str2 = BadgerConstants.LOG_TAG;
        Log.v(str2, "readEIDCard");
        try {
            ResponseAPDU transmit = cardChannel.transmit(new CommandAPDU(new byte[]{java.lang.Byte.MIN_VALUE, -28, 0, 0, 28}));
            if (transmit != null) {
                if (transmit.getSW1() == 144) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Card data: ");
                    sb.append(Util.byteArrayToString(transmit.getBytes()));
                    Log.i(str2, sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("eID version: ");
                    sb2.append(String.format("%02X", new Object[]{Byte.valueOf(transmit.getBytes()[21])}));
                    Log.i(str2, sb2.toString());
                    z = EFTransparent.selectFile(eID.AID_APPLET, (byte) 4, eID.TLV_BIRTHDATE, cardChannel) < 0;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(z);
                    Log.i(str2, sb3.toString());
                    if (z) {
                        return null;
                    }
                    EFTransparent file = EFTransparent.getFile(new byte[]{-33, 1, 64, 49}, cardChannel);
                    EFTransparent file2 = EFTransparent.getFile(new byte[]{64, 51}, cardChannel);
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str);
                    sb4.append(z);
                    Log.d(str2, sb4.toString());
                    if (file == null || file2 == null) {
                        return null;
                    }
                    return eID.createEIDCard(file, file2);
                }
            }
            Log.i(str2, "Unable to get info about card data");
            if (EFTransparent.selectFile(eID.AID_APPLET, (byte) 4, eID.TLV_BIRTHDATE, cardChannel) < 0) {
            }
            StringBuilder sb32 = new StringBuilder();
            sb32.append(str);
            sb32.append(z);
            Log.i(str2, sb32.toString());
            if (z) {
            }
        } catch (Exception e) {
            Log.e(str2, "Error in readEIDCard", e);
            return null;
        }
        return null;
    }
}
