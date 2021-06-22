package com.cppsystem.cppbus.cppcardlib.nfclib.targets;


import com.cppsystem.cppbus.cppcardlib.nfclib.util.Util;

public class CalypsoCard extends ClessCard {
    public static final String CALYPSO_UID_HEADER = "43414C59";

    public CalypsoCard(byte[] bArr, byte[] bArr2) throws Exception {
        super(bArr, createUid(bArr2));
        setCardNumber(createCardNumber(bArr2));
    }

    private static String createUid(byte[] bArr) throws Exception {
        if (bArr == null || !(bArr.length == 4 || bArr.length == 8)) {
            throw new Exception("Calypso serial number not valid");
        }
        String byteArrayToString = Util.byteArrayToString(bArr);
        String str = CALYPSO_UID_HEADER;
        String replace = byteArrayToString.replace("00000000", str);
        if (replace.length() != 8) {
            return replace;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(replace);
        return sb.toString();
    }

    private static String createCardNumber(byte[] bArr) {
        long j = 0;
        for (byte b : bArr) {
            j = (j << 8) + ((long) (b & 255));
        }
        return Long.toString(j);
    }
}
