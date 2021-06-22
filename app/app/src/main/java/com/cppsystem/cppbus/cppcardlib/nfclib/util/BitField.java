package com.cppsystem.cppbus.cppcardlib.nfclib.util;

public class BitField {
    private String data;

    public BitField(String str) {
        this.data = str.replace(" ", "");
    }

    public BitField(byte[] bArr) {
        this.data = "";
        addBits(bArr);
    }

    public final void addBits(byte[] bArr) {
        for (byte b : bArr) {
            for (int i = 7; i >= 0; i--) {
                StringBuilder sb = new StringBuilder();
                sb.append(this.data);
                sb.append((((byte) (1 << i)) & b) == 0 ? "0" : "1");
                this.data = sb.toString();
            }
        }
    }

    public int findFirst(int i, BitField bitField) {
        while (i < this.data.length() - bitField.getLength()) {
            if (this.data.substring(i).startsWith(bitField.data)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public byte getByte(int i) {
        return (byte) Integer.parseInt(this.data.substring(i, i + 8), 2);
    }

    public byte[] getBytes(int i, int i2) {
        byte[] bArr = new byte[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            bArr[i3] = getByte((i3 * 8) + i);
        }
        return bArr;
    }

    public BitField getSubField(int i, int i2) {
        return new BitField(this.data.substring(i, i2 + i));
    }

    public int getLength() {
        return this.data.length();
    }

    public byte[] toByteArray() {
        byte[] bArr = new byte[((this.data.length() / 8) + (this.data.length() % 8 == 0 ? 0 : 1))];
        int length = bArr.length - 1;
        int i = 0;
        for (int length2 = this.data.length() - 1; length2 >= 0; length2--) {
            bArr[length] = (byte) (bArr[length] + (this.data.charAt(length2) == '1' ? 1 << i : 0));
            i = (i + 1) % 8;
            if (i == 0) {
                length--;
            }
        }
        return bArr;
    }

    public String toString() {
        return this.data;
    }
}
