package com.cppsystem.cppbus.cppcardlib.nfclib.readers;


import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CardChannel;

public class CardChannelCCID extends CardChannel {
    private Reader reader = null;

    public CardChannelCCID(Reader reader2) {
        this.reader = reader2;
    }

    public byte[] transmit(byte[] bArr) {
        return this.reader.sendApdu(bArr);
    }

    public Reader getReader() {
        return this.reader;
    }
}
