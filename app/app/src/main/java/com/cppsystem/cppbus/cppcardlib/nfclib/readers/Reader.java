package com.cppsystem.cppbus.cppcardlib.nfclib.readers;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.CardController;
import com.cppsystem.cppbus.cppcardlib.nfclib.usb.Card;
import com.cppsystem.cppbus.cppcardlib.nfclib.usb.CcidDevice;
import com.cppsystem.cppbus.cppcardlib.nfclib.usb.CcidException;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;

import java.util.HashMap;
import java.util.Map;

public class Reader extends CcidDevice {
    private String codeGroup = null;
    private boolean contactless = false;
    private String name = "Unknown CCID Reader";

    public Reader(UsbManager usbManager, UsbDevice usbDevice, UsbInterface usbInterface) throws CcidException {
        super(usbManager, usbDevice, usbInterface);
    }

    /* access modifiers changed from: protected */
    public Card createCard(byte[] bArr, byte[] bArr2) {
        Log.v(BadgerConstants.LOG_TAG, "NEW createCard");
        return getNewCardController().getCard(bArr, bArr2, initReading(), this.codeGroup);
    }

    /* access modifiers changed from: protected */
    public CardController getNewCardController() {
        return new CardController(new CardChannelCCID(this));
    }

    /* access modifiers changed from: protected */
    public Map<String, Object> initReading() {
        return new HashMap();
    }

    /* access modifiers changed from: protected */
    public byte[] sendApdu(byte[] bArr) {
        return super.sendApdu(bArr);
    }

    public String getCodeGroup() {
        return this.codeGroup;
    }

    public void setCodeGroup(String str) {
        this.codeGroup = str;
    }

    public boolean isContacless() {
        return this.contactless;
    }

    public void setContacless(boolean z) {
        this.contactless = z;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }
}
