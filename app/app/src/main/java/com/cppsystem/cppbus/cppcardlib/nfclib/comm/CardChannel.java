package com.cppsystem.cppbus.cppcardlib.nfclib.comm;

public abstract class CardChannel {
    public abstract byte[] transmit(byte[] bArr);

    public ResponseAPDU transmit(CommandAPDU commandAPDU) {
        byte[] transmit = transmit(commandAPDU.getBytes());
        if (transmit != null) {
            return new ResponseAPDU(transmit);
        }
        return null;
    }
}
