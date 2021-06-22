package com.cppsystem.cppbus.cppcardlib.nfclib.readers;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.CardController;
import com.cppsystem.cppbus.cppcardlib.nfclib.usb.CcidException;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;

import java.util.Arrays;

public class AcsAcr extends Reader {
    public AcsAcr(UsbManager usbManager, UsbDevice usbDevice, UsbInterface usbInterface) throws CcidException {
        super(usbManager, usbDevice, usbInterface);
        Log.d(BadgerConstants.LOG_TAG, "Lecteur ACS ACRXXXX");
        setContacless(true);
        setName("ACSACR");
    }

    /* access modifiers changed from: protected */
    public CardController getNewCardController() {
        return new CardController(new CardChannelCCID(this)) {
            /* access modifiers changed from: protected */
            public byte[] doReadMemoryBlock(byte b, int i) {
                byte b2 = (i <= 0 || i >= 3595) ? 4 : (byte) i;
                try {
                    byte[] copyOf = Arrays.copyOf(APDU_READ, APDU_READ.length);
                    copyOf[3] = b;
                    copyOf[4] = b2;
                    byte[] transmit = this.channel.transmit(copyOf);
                    if (transmit == null || transmit.length <= 2) {
                        return null;
                    }
                    return Arrays.copyOfRange(transmit, 0, Math.min(transmit.length - 2, i));
                } catch (Exception e) {
                    Log.w(BadgerConstants.LOG_TAG, "Error in readMemoryBlock", e);
                    return null;
                }
            }
        };
    }
}
