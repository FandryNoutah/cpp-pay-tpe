package com.cppsystem.cppbus.cppcardlib.nfclib.util;

import java.util.Arrays;

public class Tlv {
    public static byte[] GetTLV(byte[] bArr, byte b) {
        return GetTLV(bArr, new byte[]{b});
    }

    public static byte[] GetTLV(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = null;
        if (bArr == null) {
            return null;
        }
        int i = 0;
        while (true) {
            try {
                if (i >= bArr.length - bArr2.length) {
                    break;
                }
                int i2 = ((bArr[i] & 31) == 31 ? 2 : 1) + i;
                byte b = bArr[i2];
                if (Arrays.equals(bArr2, Arrays.copyOfRange(bArr, i, i2))) {
                    int i3 = i2 + 1;
                    int i4 = i3 + b;
                    if (i4 <= bArr.length) {
                        bArr3 = Arrays.copyOfRange(bArr, i3, i4);
                        break;
                    }
                }
                i = i2 + 1 + b;
            } catch (Exception unused) {
            }
        }
        return bArr3;
    }
}
