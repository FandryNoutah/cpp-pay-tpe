package com.cppsystem.cppbus.cppcardlib.nfclib.spe;

import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CardChannel;
import com.cppsystem.cppbus.cppcardlib.nfclib.smartcard.EF;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.eID;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Tlv;

import com.cppsystem.cppbus.cppcardlib.nfclib.util.Util;

public class CalypsoUtil {
    protected static final byte[] APDU_SELECT_CALYPSO_APPLICATION = {0, -92, 4, 0, 8, 48, 69, 84, 80, 46, 73, 67, 65};
    public final static byte UTF8_BOM_3 = (byte) 0xBF;

    public static byte[] getCalypsoSerialNumber(CardChannel cardChannel) {
        String str = BadgerConstants.LOG_TAG;
        byte[] bArr = null;

        Exception e;
        try {
            byte[] transmit = cardChannel.transmit(APDU_SELECT_CALYPSO_APPLICATION);
            StringBuilder sb = new StringBuilder();
            sb.append("Calypso app selection:");
            sb.append(Util.byteArrayToString(transmit));
            Log.i(str, sb.toString());
            if (transmit != null) {
                if (transmit.length == 2) {
                    String str2 = "Calypso answer:";
                    if (transmit[0] != -112) {
                        if (transmit[0] != 98 || transmit[1] != -125) {
                            if (transmit[0] == 97) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(str2);
                                sb2.append(Util.byteArrayToString(transmit));
                                Log.d(str, sb2.toString());
                                transmit = EF.getResponse(transmit[1], cardChannel);
                            }
                        }
                    }
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str2);
                    sb3.append(Util.byteArrayToString(transmit));
                    Log.d(str, sb3.toString());
                    transmit = EF.getFullResponse(0, cardChannel);
                }
            }
            if (transmit != null && transmit.length > 2) {
                byte[] GetTLV = Tlv.GetTLV(Tlv.GetTLV(Tlv.GetTLV(Tlv.GetTLV(transmit, (byte) 111), (byte) -91), new byte[]{UTF8_BOM_3, eID.TLV_BIRTHDATE}), (byte) -57);
                if (GetTLV == null || GetTLV.length != 8) {
                    Log.i(str, "Calypso identified, but could not read Calypso serial number");
                } else {
                    try {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Calypso serial number in memory:");
                        sb4.append(Util.byteArrayToString(GetTLV));
                        Log.i(str, sb4.toString());
                        return GetTLV;
                    } catch (Exception ex) {
                        Exception exc = ex;
                        bArr = GetTLV;
                        e = exc;
                    }
                }
            }
        } catch (Exception e2) {
            e = e2;
        }
        return bArr;
        /*StringBuilder sb5 = new StringBuilder();
        sb5.append("Failed to read Calypso application:");
        sb5.append(e.getMessage());
        Log.d(str, sb5.toString());
        return bArr;*/
    }
}
