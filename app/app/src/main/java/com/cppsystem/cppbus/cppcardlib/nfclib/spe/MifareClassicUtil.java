package com.cppsystem.cppbus.cppcardlib.nfclib.spe;

import android.nfc.tech.MifareClassic;
import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.targets.ContentCard;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Util;

import java.util.HashMap;
import java.util.Map;

public class MifareClassicUtil {
    public static int COMPANY_SECTOR = 6;
    public static int COUNTRYCODE_SECTOR = 4;
    public static int EMAIL_SECTOR = 5;
    public static int FIRSTNAME_SECTOR = 2;
    public static final byte[] KEY_BYTES_FIRA_A = {82, -13, -38, -79, -121, 122};
    public static final byte[] KEY_BYTES_FIRA_B = {37, 63, -83, 27, 120, -89};
    public static int MOBILE_SECTOR = 9;
    public static int NAME_SECTOR = 3;

    public static Map<String, String> getMwcInfos(MifareClassic mifareClassic) throws Exception {
        try {
            mifareClassic.connect();
            HashMap hashMap = new HashMap();
            try {
                String sectorValue = getSectorValue(mifareClassic, FIRSTNAME_SECTOR);
                String sectorValue2 = getSectorValue(mifareClassic, NAME_SECTOR);
                String sectorValue3 = getSectorValue(mifareClassic, COUNTRYCODE_SECTOR);
                String sectorValue4 = getSectorValue(mifareClassic, EMAIL_SECTOR);
                String sectorValue5 = getSectorValue(mifareClassic, COMPANY_SECTOR);
                String sectorValue6 = getSectorValue(mifareClassic, MOBILE_SECTOR);
                hashMap.put(ContentCard.FIELD_FIRSTNAME, sectorValue);
                hashMap.put("name", sectorValue2);
                hashMap.put(ContentCard.FIELD_COUNTRYCODE, sectorValue3);
                hashMap.put("email", sectorValue4);
                hashMap.put(ContentCard.FIELD_COMPANY, sectorValue5);
                hashMap.put(ContentCard.FIELD_MOBILE, sectorValue6);
                return hashMap;
            } catch (Exception unused) {
                return hashMap;
            }
        } catch (Exception unused2) {
            return null;
        }
    }

    public static String getSectorValue(MifareClassic mifareClassic, int i) throws Exception {
        boolean authenticateSectorWithKeyA = mifareClassic.authenticateSectorWithKeyA(i, KEY_BYTES_FIRA_A);
        StringBuilder sb = new StringBuilder();
        sb.append("A: ");
        sb.append(Util.byteArrayToString(KEY_BYTES_FIRA_A));
        String sb2 = sb.toString();
        String str = BadgerConstants.LOG_TAG;
        Log.w(str, sb2);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("B: ");
        sb3.append(Util.byteArrayToString(KEY_BYTES_FIRA_B));
        Log.w(str, sb3.toString());
        if (authenticateSectorWithKeyA) {
            int blockCountInSector = mifareClassic.getBlockCountInSector(i);
            String str2 = "";
            int sectorToBlock = mifareClassic.sectorToBlock(i);
            for (int i2 = 1; i2 < blockCountInSector; i2++) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str2);
                sb4.append(new String(mifareClassic.readBlock(sectorToBlock), 0, 15).trim());
                str2 = sb4.toString();
                sectorToBlock++;
            }
            return str2.trim();
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Unable to auth sector ");
        sb5.append(i);
        sb5.append(" key: ");
        sb5.append(Util.byteArrayToString(KEY_BYTES_FIRA_A));
        Log.w(str, sb5.toString());
        return null;
    }
}
