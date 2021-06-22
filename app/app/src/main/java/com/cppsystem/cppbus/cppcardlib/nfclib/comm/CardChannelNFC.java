package com.cppsystem.cppbus.cppcardlib.nfclib.comm;

import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareUltralight;
import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;

import java.io.IOException;

public class CardChannelNFC extends CardChannel {
    private IsoDep isoDep = null;
    private MifareUltralight mul = null;

    public CardChannelNFC(IsoDep isoDep2) {
        this.isoDep = isoDep2;
    }

    public CardChannelNFC(MifareUltralight mifareUltralight) {
        this.mul = mifareUltralight;
    }

    public MifareUltralight getMul() {
        return this.mul;
    }

    public IsoDep getIsoDep() {
        return this.isoDep;
    }

    public byte[] transmit(byte[] bArr) {
        if (this.mul != null) {
            return null;
        }
        try {
            return this.isoDep.transceive(bArr);
        } catch (IOException unused) {
            Log.w(BadgerConstants.LOG_TAG, "isoDep transceive failed");
            return null;
        }
    }
}
