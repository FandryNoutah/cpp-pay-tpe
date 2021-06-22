package com.cppsystem.cppbus.cppcardlib.nfclib.targets;

import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.smartcard.EFTransparent;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Tlv;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class eID extends ContentCard {
    public static final byte[] AID_APPLET = {-96, 0, 0, 0, 48, 41, 5, 112, 0, -83, 19, 16, 1, 1, -1};
    public static final byte[] AID_BELPIC = {-96, 0, 0, 1, 119, 80, 75, 67, 83, 45, 49, 53};
    public static final byte[] AID_ID = {-96, 0, 0, 1, 119, 73, 100, 70, 105, 108, 101, 115};
    public static final String COUNTRY_BELGIUM = "BE";
    public static final byte[] DF_ID = {-33, 1};
    public static final byte[] EF_ADRESS = {64, 51};
    public static final byte[] EF_ID_PHOTO = {64, 53};
    public static final byte[] EF_ID_RN = {64, 49};
    public static final byte TLV_2_FIRST_GIVEN_NAMES = 8;
    public static final byte TLV_BIRTHDATE = 12;
    public static final byte TLV_CARD_NUMBER = 1;
    public static final byte TLV_CHIP_NUMBER = 2;
    public static final byte TLV_FILE_STRUCTURE_VERSION = 0;
    public static final byte TLV_NAME = 7;
    public static final byte TLV_NATIONALITY = 10;
    public static final byte TLV_SEX = 13;
    public static final byte TLV_STREET_NUMBER = 1;
    public static final byte TLV_TOWN = 3;
    public static final byte TLV_ZIP = 2;
    private String birthdate;
    private String chipNumber;
    private String givenNames;
    private String name;
    private String sex;
    private String streetAndNumber;
    private String town;
    private String zip;

    public eID(String str) {
        super(str, null);
        this.chipNumber = null;
        this.name = null;
        this.givenNames = null;
        this.birthdate = null;
        this.sex = null;
        this.streetAndNumber = null;
        this.zip = null;
        this.town = null;
        this.chipType = BadgerConstants.CHIP_SMARTCARD;
        this.systemType = BadgerConstants.CARD_EID;
    }

    public static eID createEIDCard(EFTransparent eFTransparent, EFTransparent eFTransparent2) {
        String str = BadgerConstants.LOG_TAG;
        String str2 = "UTF-8";
        byte[] data = eFTransparent.getData();
        byte[] data2 = eFTransparent2.getData();
        String readStringValue = readStringValue(data, (byte) 2, "Binary", "Chip number");
        String str3 = "US-ASCII";
        String readStringValue2 = readStringValue(data, (byte) 1, str3, "Card number");
        if (readStringValue2 != null) {
            readStringValue2 = readStringValue2.replaceFirst("(0*)(.*)", "$2");
        }
        eID eid = new eID(readStringValue);
        try {
            eid.chipNumber = readStringValue;
            eid.setCardNumber(readStringValue2);
            eid.name = readStringValue(data, (byte) 7, str2, "Name");
            eid.givenNames = readStringValue(data, (byte) 8, str2, "Given Names");
            eid.birthdate = readStringValue(data, TLV_BIRTHDATE, str2, "Birth Date");
            eid.sex = readStringValue(data, TLV_SEX, str2, "Sex");
            StringBuilder sb = new StringBuilder();
            sb.append("EF_RN file version: ");
            sb.append(Util.byteArrayToString(Tlv.GetTLV(data, (byte) 0)));
            Log.d(str, sb.toString());
            eid.streetAndNumber = readStringValue(data2, (byte) 1, str2, "Street and number");
            eid.zip = readStringValue(data2, (byte) 2, str3, "Zip code");
            eid.town = readStringValue(data2, (byte) 3, str2, "Municipality");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("EF_ADDR file version: ");
            sb2.append(Util.byteArrayToString(Tlv.GetTLV(data2, (byte) 0)));
            Log.d(str, sb2.toString());
        } catch (Exception e) {
            eid = null;
            Log.w(str, "Error while creating the eIDCard", e);
        }
        HashMap values = eid.getValues();
        values.put("name", eid.name);
        values.put(ContentCard.FIELD_FIRSTNAME, eid.givenNames);
        values.put(ContentCard.FIELD_ADRESS_STREET, eid.streetAndNumber);
        values.put(ContentCard.FIELD_ZIP, eid.zip);
        values.put(ContentCard.FIELD_TOWN, eid.town);
        values.put(ContentCard.FIELD_COUNTRYCODE, COUNTRY_BELGIUM);
        Date parseDate = parseDate(eid.birthdate);
        if (parseDate != null) {
            values.put(ContentCard.FIELD_BIRTHDAY, Long.toString(parseDate.getTime()));
        }
        values.put(ContentCard.FIELD_GENDER, parseSex(eid.sex));
        eid.setValues(values);
        return eid;
    }

    private static String readStringValue(byte[] bArr, byte b, String str, String str2) {
        try {
            byte[] GetTLV = Tlv.GetTLV(bArr, b);
            if (GetTLV == null) {
                return null;
            }
            if ("Binary".equals(str)) {
                return Util.byteArrayToString(GetTLV);
            }
            return new String(GetTLV, str);
        } catch (UnsupportedEncodingException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append("Error reading value ");
            sb.append(b);
            Log.w(BadgerConstants.LOG_TAG, sb.toString());
            return null;
        }
    }

    public String getChipNumber() {
        return this.chipNumber;
    }

    public String getGivenNames() {
        return this.givenNames;
    }

    public String getName() {
        return this.name;
    }

    public String getSex() {
        return this.sex;
    }

    public String getBirthdate() {
        return this.birthdate;
    }

    public String getStreetAndNumber() {
        return this.streetAndNumber;
    }

    public String getZip() {
        return this.zip;
    }

    public String getTown() {
        return this.town;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [uid=");
        sb.append(this.suid);
        sb.append("]\n");
        sb.append("Card number: ");
        sb.append(getCardNumber());
        String str = "\n";
        sb.append(str);
        sb.append("Sex: ");
        sb.append(this.sex);
        sb.append(str);
        sb.append("Name: ");
        sb.append(this.name);
        sb.append(str);
        sb.append("Given names: ");
        sb.append(this.givenNames);
        sb.append(str);
        sb.append("Date of birth: ");
        sb.append(this.birthdate);
        sb.append(str);
        sb.append("Street and number: ");
        sb.append(this.streetAndNumber);
        sb.append(str);
        sb.append("Zip code: ");
        sb.append(this.zip);
        sb.append(str);
        sb.append("Town: ");
        sb.append(this.town);
        return sb.toString();
    }

    private static Date parseDate(String str) {
        String str2 = str;
        if (str2 == null) {
            return null;
        }
        String str3 = "JAN";
        String str4 = "NOV";
        String[][] strArr = {new String[]{str3, "FEV", "MARS", "AVR", "MAI", "JUIN", "JUIL", "AOUT", "SEPT", "OCT", str4, "DEC"}, new String[]{str3, "FEB", "MAAR", "APR", "MEI", "JUN", "JUL", "AUG", "SEP", "OKT", str4, "DEC"}, new String[]{str3, "FEB", "MÄR", "APR", "MAI", "JUN", "JUL", "AUG", "SEP", "OKT", str4, "DEZ"}};
        Date date = null;
        try {
            String[] split = str2.split("[ \\.]+");
            int i = -1;
            String str5 = BadgerConstants.LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("dateSplit ");
            sb.append(split.length);
            sb.append(" ");
            sb.append(Arrays.toString(split));
            Log.d(str5, sb.toString());
            if (split != null && split.length == 3) {
                int i2 = 0;
                while (true) {
                    if (!(i2 < strArr.length) || !(i < 0)) {
                        break;
                    }
                    int i3 = i;
                    int i4 = 0;
                    while (true) {
                        if (!(i4 < strArr[i2].length) || !(i3 < 0)) {
                            break;
                        }
                        if (split[1].equals(strArr[i2][i4])) {
                            i3 = i4;
                        }
                        i4++;
                    }
                    i2++;
                    i = i3;
                }
                if (i >= 0) {
                    Calendar instance = Calendar.getInstance();
                    instance.set(Integer.parseInt(split[2]), i, Integer.parseInt(split[0]));
                    date = instance.getTime();
                }
            }
        } catch (Exception e) {
            Log.d(BadgerConstants.LOG_TAG, "Parsing error", e);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Date in: ");
        sb2.append(str2);
        sb2.append("date out:  ");
        sb2.append(date);
        Log.d(BadgerConstants.LOG_TAG, sb2.toString());
        return date;
    }

    private static String parseSex(String str) {
        String str2 = "M";
        String str3 = "MME";
        if (!str2.equals(str)) {
            if (!"F".equals(str) && !"V".equals(str) && !"W".equals(str)) {
                str2 = "";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Gender in: ");
            sb.append(str);
            sb.append(" gender out: ");
            sb.append(str3);
            Log.d(BadgerConstants.LOG_TAG, sb.toString());
            return str3;
        }
        str3 = str2;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Gender in: ");
        sb2.append(str);
        sb2.append(" gender out: ");
        sb2.append(str3);
        Log.d(BadgerConstants.LOG_TAG, sb2.toString());
        return str3;
    }
}
