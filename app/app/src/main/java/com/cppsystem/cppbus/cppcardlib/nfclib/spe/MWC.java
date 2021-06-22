package com.cppsystem.cppbus.cppcardlib.nfclib.spe;

import android.nfc.tech.MifareClassic;
import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.CardController;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.ContentCard;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;

import java.util.HashMap;
import java.util.Map;

public class MWC {
    public static int COMPANY_SECTOR = 6;
    public static int COUNTRYCODE_SECTOR = 4;
    public static int EMAIL_SECTOR = 5;
    public static int FIRSTNAME_SECTOR = 2;
    public static int MOBILE_SECTOR = 9;
    public static int NAME_SECTOR = 3;

    public static Map<String, String> getMwcInfos(MifareClassic mifareClassic) throws Exception {
        mifareClassic.connect();
        String sectorValue = MifareClassicUtil.getSectorValue(mifareClassic, FIRSTNAME_SECTOR);
        String sectorValue2 = MifareClassicUtil.getSectorValue(mifareClassic, NAME_SECTOR);
        String sectorValue3 = MifareClassicUtil.getSectorValue(mifareClassic, COUNTRYCODE_SECTOR);
        String sectorValue4 = MifareClassicUtil.getSectorValue(mifareClassic, EMAIL_SECTOR);
        String sectorValue5 = MifareClassicUtil.getSectorValue(mifareClassic, COMPANY_SECTOR);
        String sectorValue6 = MifareClassicUtil.getSectorValue(mifareClassic, MOBILE_SECTOR);
        HashMap hashMap = new HashMap();
        hashMap.put(ContentCard.FIELD_FIRSTNAME, sectorValue);
        hashMap.put("name", sectorValue2);
        hashMap.put(ContentCard.FIELD_COUNTRYCODE, sectorValue3);
        hashMap.put("email", sectorValue4);
        hashMap.put(ContentCard.FIELD_COMPANY, sectorValue5);
        hashMap.put(ContentCard.FIELD_MOBILE, sectorValue6);
        return hashMap;
    }

    public static Map<String, String> getMWCCard(CardController cardController) {
        boolean mifareAuth = cardController.mifareAuth((byte) 26, MifareClassicUtil.KEY_BYTES_FIRA_A);
        StringBuilder sb = new StringBuilder();
        sb.append("mifareAuth: ");
        sb.append(mifareAuth);
        String sb2 = sb.toString();
        String str = BadgerConstants.LOG_TAG;
        Log.d(str, sb2);
        Map<String, String> map = null;
        if (!mifareAuth) {
            return null;
        }
        try {
            HashMap hashMap = new HashMap();
            try {
                String mifareSectorValue = cardController.getMifareSectorValue(FIRSTNAME_SECTOR);
                hashMap.put(ContentCard.FIELD_FIRSTNAME, mifareSectorValue);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("FIRSTNAME mwc: ");
                sb3.append(mifareSectorValue);
                Log.e(str, sb3.toString());
                String mifareSectorValue2 = cardController.getMifareSectorValue(NAME_SECTOR);
                hashMap.put("name", mifareSectorValue2);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("NAME mwc: ");
                sb4.append(mifareSectorValue2);
                Log.e(str, sb4.toString());
                String mifareSectorValue3 = cardController.getMifareSectorValue(EMAIL_SECTOR);
                hashMap.put("email", mifareSectorValue3);
                StringBuilder sb5 = new StringBuilder();
                sb5.append("EMAIL mwc: ");
                sb5.append(mifareSectorValue3);
                Log.e(str, sb5.toString());
                String mifareSectorValue4 = cardController.getMifareSectorValue(MOBILE_SECTOR);
                hashMap.put(ContentCard.FIELD_MOBILE, mifareSectorValue4);
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Mobile mwc: ");
                sb6.append(mifareSectorValue4);
                Log.e(str, sb6.toString());
                String mifareSectorValue5 = cardController.getMifareSectorValue(COMPANY_SECTOR);
                hashMap.put(ContentCard.FIELD_COMPANY, mifareSectorValue5);
                StringBuilder sb7 = new StringBuilder();
                sb7.append("Company mwc: ");
                sb7.append(mifareSectorValue5);
                Log.e(str, sb7.toString());
                String mifareSectorValue6 = cardController.getMifareSectorValue(COUNTRYCODE_SECTOR);
                hashMap.put(ContentCard.FIELD_COUNTRYCODE, mifareSectorValue6);
                StringBuilder sb8 = new StringBuilder();
                sb8.append("Country mwc: ");
                sb8.append(mifareSectorValue6);
                Log.e(str, sb8.toString());
                return hashMap;
            } catch (Exception e) {
                e = e;
                map = hashMap;
                Log.e(str, "", e);
                return map;
            }
        } catch (Exception e2) {
            Exception e = e2;
            Log.e(str, "", e);
            return map;
        }
    }
}
