package com.cppsystem.cppbus.cppcardlib.nfclib.util;

import java.util.Locale;

public class Util {
    public static String byteArrayToString(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        String str = "";
        for (byte b : bArr) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(String.format("%02x", new Object[]{Byte.valueOf(b)}));
            str = sb.toString();
        }
        return str.toUpperCase(Locale.FRENCH);
    }

    public static byte[] hexStringToArray(String str) {
        if (str == null || str.length() % 2 != 0) {
            return null;
        }
        byte[] bArr = new byte[(str.length() / 2)];
        int i = 0;
        while (i < bArr.length) {
            try {
                int i2 = i * 2;
                bArr[i] = (byte) (Short.parseShort(str.substring(i2, i2 + 2), 16) & 255);
                i++;
            } catch (NumberFormatException unused) {
                return null;
            }
        }
        return bArr;
    }

    public static <T> int arrayIndexOf(T[] tArr, T t) {
        for (int i = 0; i < tArr.length; i++) {
            T t2 = tArr[i];
            if (t2 == t) {
                return i;
            }
            if (t != null && t.equals(t2)) {
                return i;
            }
        }
        return -1;
    }

    public static int arrayIndexOf(byte[] bArr, byte b) {
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] == b) {
                return i;
            }
        }
        return -1;
    }

    public static int arrayCountElement(byte[] bArr, byte b) {
        int i = 0;
        for (byte b2 : bArr) {
            if (b2 == b) {
                i++;
            }
        }
        return i;
    }
}
