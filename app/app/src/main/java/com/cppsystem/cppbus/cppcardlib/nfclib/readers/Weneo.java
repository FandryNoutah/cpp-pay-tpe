package com.cppsystem.cppbus.cppcardlib.nfclib.readers;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.CardController;
import com.cppsystem.cppbus.cppcardlib.nfclib.usb.Card;
import com.cppsystem.cppbus.cppcardlib.nfclib.usb.CcidException;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;

import java.util.Map;

public class Weneo extends Reader {
    public Weneo(UsbManager usbManager, UsbDevice usbDevice, UsbInterface usbInterface) throws CcidException {
        super(usbManager, usbDevice, usbInterface);
        Log.d(BadgerConstants.LOG_TAG, "Lecteur Weneo");
        setContacless(true);
        setName("Weneo");
    }

    /* access modifiers changed from: protected */
    public CardController getNewCardController() {
        return new CardController(new CardChannelCCID(this)) {
            /* access modifiers changed from: protected */
            public Card getCardlets(byte[] bArr, byte[] bArr2, Map<String, Object> map, String str) {
                return null;
            }
        };
    }
}
