package com.cppsystem.cppbus.cppcardlib.nfclib.spe;

import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CardChannel;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.eID;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Tlv;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Util;

import java.security.MessageDigest;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
//import kotlin.jvm.internal.ByteCompanionObject;

public class CardletUtil {
    private static final byte[] APDU_GET_AUTH_TOKEN = {java.lang.Byte.MIN_VALUE, -62, 3, 0, 16};
    private static final byte[] APDU_GET_IDENTIFIER = {java.lang.Byte.MIN_VALUE, -62, 0, 0, 80};
    private static final byte[] APDU_GET_IDENTIFIER_V2 = {java.lang.Byte.MIN_VALUE, -62, 0, 1};
    private static final byte[] APDU_SELECT_CARDLET = {0, -92, 4, eID.TLV_BIRTHDATE, 16, -46, 80, 0, 0, 9, 0, 0, 0, 0, 0, 0, 0, 65, 68, 69, 65};
    private static final byte TAG_ID = 1;
    private static final byte[] deskey2 = {56, 76, 55, 104, -38, -43, -12, -15, -123, 44, 88, -48, 122, -80, -9, 62, 49, 73, 52, -80, -110, -111, -95, 26};

    public static String getCardletID(CardChannel cardChannel, String str) {
        byte[] bArr;
        String str2 = BadgerConstants.LOG_TAG;
        Log.v(str2, "getCardletID()");
        byte[] transmit = cardChannel.transmit(APDU_SELECT_CARDLET);
        String str3 = null;
        if (transmit == null || transmit.length < 2 || transmit[transmit.length - 2] != -112 || transmit[transmit.length - 1] != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to select cardlet LA. Result=");
            sb.append(Arrays.toString(transmit));
            Log.d(str2, sb.toString());
            return null;
        }
        Log.v(str2, "getIdentifier");
        byte[] transmit2 = cardChannel.transmit(APDU_GET_IDENTIFIER);
        if (transmit2 == null || transmit2.length < 2 || transmit2[transmit2.length - 2] != -112) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to get cardlet ID, try cardlet V2. Result=");
            sb2.append(Arrays.toString(transmit2));
            Log.d(str2, sb2.toString());
            transmit2 = getCardletIDV2(cardChannel, str, null);
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Get identifier command result: ");
        sb3.append(Arrays.toString(transmit2));
        Log.d(str2, sb3.toString());
        if (transmit2 == null) {
            Log.d(str2, "Get CardletID failed");
            bArr = new byte[0];
        } else if (transmit2.length <= 2) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Unable to get CardletID. Response=");
            sb4.append(Arrays.toString(transmit2));
            Log.d(str2, sb4.toString());
            bArr = null;
        } else {
            bArr = Tlv.GetTLV(transmit2, (byte) 1);
        }
        if (bArr != null) {
            String byteArrayToString = Util.byteArrayToString(bArr);
            while (byteArrayToString.endsWith("F")) {
                byteArrayToString = byteArrayToString.substring(0, byteArrayToString.length() - 1);
            }
            str3 = byteArrayToString;
            while (str3.length() < 26) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str3);
                sb5.append("0");
                str3 = sb5.toString();
            }
        }
        return str3;
    }

    private static byte[] getCardletIDV2(CardChannel cardChannel, String str, String str2) {
        int i;
        StringBuilder sb = new StringBuilder();
        sb.append("getIdentifierV2 codeGroup=");
        sb.append(str);
        sb.append(" tagId=");
        sb.append(str2);
        String sb2 = sb.toString();
        String str3 = BadgerConstants.LOG_TAG;
        Log.d(str3, sb2);
        if (str == null) {
            str = "LO_TABLET";
            i = 0;
        } else {
            i = 1;
        }
        byte[] transmit = cardChannel.transmit(APDU_GET_AUTH_TOKEN);
        if (transmit == null || transmit[transmit.length - 2] != -112 || transmit.length != 18) {
            return null;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Token ");
        sb3.append(Arrays.toString(transmit));
        Log.d(str3, sb3.toString());
        byte[] bArr = new byte[48];
        Arrays.fill(bArr, (byte) 0);
        System.arraycopy(str.getBytes(), 0, bArr, 0, str.getBytes().length);
        System.arraycopy(transmit, 0, bArr, 32, 16);
        byte[] codegroupSign = getCodegroupSign(bArr);
        byte[] copyOf = Arrays.copyOf(APDU_GET_IDENTIFIER_V2, str.getBytes().length + 10 + 2 + 16 + 1);
        copyOf[4] = (byte) (copyOf.length - 6);
        copyOf[5] = 16;
        copyOf[6] = 1;
        copyOf[7] = (byte) i;
        copyOf[8] = 17;
        copyOf[9] = (byte) str.getBytes().length;
        System.arraycopy(bArr, 0, copyOf, 10, str.getBytes().length);
        int length = 10 + str.getBytes().length;
        if (codegroupSign != null) {
            int i2 = length + 1;
            copyOf[length] = 18;
            int i3 = i2 + 1;
            copyOf[i2] = (byte) codegroupSign.length;
            System.arraycopy(codegroupSign, 0, copyOf, i3, codegroupSign.length);
            length = i3 + codegroupSign.length;
        }
        copyOf[length] = 80;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Command ");
        sb4.append(Arrays.toString(copyOf));
        Log.d(str3, sb4.toString());
        return cardChannel.transmit(copyOf);
    }

    public static byte[] getCodegroupSign(byte[] bArr) {
        String str = BadgerConstants.LOG_TAG;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Tokentosigne ");
            sb.append(Arrays.toString(bArr));
            Log.d(str, sb.toString());
            SecretKeySpec secretKeySpec = new SecretKeySpec(deskey2, "DESede");
            MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.reset();
            byte[] digest = instance.digest(Arrays.copyOf(bArr, 48));
            Cipher instance2 = Cipher.getInstance("DESede/ECB/NoPadding");
            instance2.init(1, secretKeySpec);
            return instance2.doFinal(Arrays.copyOf(digest, 16));
        } catch (Exception e) {
            Log.e(str, "getCodegroupSign error", e);
            return null;
        }
    }
}
