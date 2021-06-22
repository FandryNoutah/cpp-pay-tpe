package com.cppsystem.cppbus.cppcardlib.nfclib.spe;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.CardController;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

public class NdefController {
    public static NdefMessage getNdefMessage(CardController cardController, byte[] bArr, byte[] bArr2, Map<String, Object> map) {
        return getNdefMessage(cardController);
    }

    public static NdefMessage getNdefMessage(CardController cardController) {
        ByteBuffer allocate = ByteBuffer.allocate(64);
        for (byte b = 4; b <= 15; b = (byte) (b + 1)) {
            byte[] readMemoryBlock = cardController.readMemoryBlock(b, 4);
            if (readMemoryBlock == null) {
                break;
            }
            allocate.put(readMemoryBlock);
        }
        try {
            return new NdefMessage(Arrays.copyOfRange(allocate.array(), 2, allocate.get(1) + 2));
        } catch (FormatException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot parse NdefMessage: ");
            sb.append(e.toString());
            Log.d(BadgerConstants.LOG_TAG, sb.toString());
            return null;
        }
    }
}
