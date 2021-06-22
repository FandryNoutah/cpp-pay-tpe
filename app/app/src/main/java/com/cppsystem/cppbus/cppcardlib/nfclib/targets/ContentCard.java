package com.cppsystem.cppbus.cppcardlib.nfclib.targets;

import java.util.HashMap;
import java.util.Map;

public class ContentCard extends ClessCard {
    public static final String FIELD_ADRESS_STREET = "adressStreet";
    public static final String FIELD_BIRTHDAY = "birthday";
    public static final String FIELD_CHIP_TYPE = "chipType";
    public static final String FIELD_COMPANY = "company";
    public static final String FIELD_COUNTRYCODE = "country";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_FIRSTNAME = "firstname";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_MOBILE = "mobile";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_PHOTO = "photo";
    public static final String FIELD_SYSTEM_TYPE = "systemType";
    public static final String FIELD_TOWN = "town";
    public static final String FIELD_ZIP = "zip";
    private HashMap<String, String> fields = new HashMap<>();

    public ContentCard(byte[] bArr, byte[] bArr2, String[][] strArr) {
        super(bArr, bArr2);
        if (strArr != null) {
            for (String[] strArr2 : strArr) {
                if (strArr2.length == 2) {
                    this.fields.put(strArr2[0], strArr2[1]);
                }
            }
        }
    }

    public ContentCard(byte[] bArr, byte[] bArr2, Map<String, String> map) {
        super(bArr, bArr2);
        this.fields.putAll(map);
    }

    public ContentCard(byte[] bArr, Map<String, String> map) {
        super((byte[]) null, bArr);
        this.fields.putAll(map);
    }

    public ContentCard(String str, Map<String, String> map) {
        super((byte[]) null, str);
        if (map != null) {
            this.fields.putAll(map);
        }
    }

    public String getValue(String str) {
        return (String) this.fields.get(str);
    }

    public HashMap<String, String> getValues() {
        return this.fields;
    }

    /* access modifiers changed from: protected */
    public void setValues(HashMap<String, String> hashMap) {
        this.fields = hashMap;
    }
}
