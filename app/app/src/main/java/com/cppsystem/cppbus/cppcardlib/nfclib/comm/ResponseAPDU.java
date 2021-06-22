package com.cppsystem.cppbus.cppcardlib.nfclib.comm;

import java.util.Arrays;

public class ResponseAPDU {
    private byte[] bytes;

    public ResponseAPDU(byte[] bArr) {
        this.bytes = bArr;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public byte[] getData() {
        byte[] bArr = this.bytes;
        return (bArr == null || bArr.length < 2) ? new byte[0] : Arrays.copyOf(bArr, bArr.length - 2);
    }

    public int getSW1() {
        byte[] bArr = this.bytes;
        if (bArr == null || bArr.length < 2) {
            return 0;
        }
        return bArr[bArr.length - 2] & 255;
    }

    public int getSW2() {
        byte[] bArr = this.bytes;
        if (bArr == null || bArr.length < 2) {
            return 0;
        }
        return bArr[bArr.length - 1] & 255;
    }
}
