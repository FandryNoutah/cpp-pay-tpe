package com.cppsystem.cppbus.cppcardlib.nfclib.usb;

public class Card {
    private byte[] atr;
    private byte[] uid;

    public Card(byte[] bArr, byte[] bArr2) {
        this.atr = bArr;
        this.uid = bArr2;
    }

    public byte[] getATR() {
        return this.atr;
    }

    public byte[] getUid() {
        return this.uid;
    }
}
