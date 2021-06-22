package com.cppsystem.cppbus.cppcardlib.nfclib.spe;

import android.nfc.tech.MifareUltralight;
import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;

import java.io.IOException;
import java.util.Arrays;

public class MifareUltralightUtil {
    public static byte[] readMemoryBlock(MifareUltralight mifareUltralight, byte b, int i) {
        try {
            byte[] readPages = mifareUltralight.readPages(b);
            if (readPages != null) {
                return Arrays.copyOfRange(readPages, 0, i);
            }
            return null;
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to read MifareUltralight:");
            sb.append(e.getMessage());
            Log.w(BadgerConstants.LOG_TAG, sb.toString());
            return null;
        }
    }
}
