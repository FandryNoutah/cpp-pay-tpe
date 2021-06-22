package com.cppsystem.cppbus.cppcardlib.nfclib.comm;

public class CommandAPDU {
    private byte[] bytes;

    public CommandAPDU(byte[] bArr) {
        this.bytes = bArr;
    }

    public byte[] getBytes() {
        return this.bytes;
    }
}
