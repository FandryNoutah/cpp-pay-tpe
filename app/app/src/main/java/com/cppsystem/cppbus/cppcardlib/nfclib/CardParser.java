package com.cppsystem.cppbus.cppcardlib.nfclib;


import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CardChannel;
import com.cppsystem.cppbus.cppcardlib.nfclib.usb.Card;

import java.util.Map;

public interface CardParser {
    Card getCalypso(CardChannel cardChannel, byte[] bArr, byte[] bArr2, Map<String, Object> map);

    Card getCardlets(CardChannel cardChannel, byte[] bArr, byte[] bArr2, Map<String, Object> map);

    Card getEID(CardChannel cardChannel, byte[] bArr, byte[] bArr2, Map<String, Object> map);

    Card getMWC(CardChannel cardChannel, byte[] bArr, byte[] bArr2, Map<String, Object> map);

    Card getOTCMRTM(CardChannel cardChannel, byte[] bArr, byte[] bArr2, Map<String, Object> map);

    Card getOTToulouse(CardChannel cardChannel, byte[] bArr, byte[] bArr2, Map<String, Object> map);

    Card getRetailAPIMobile(CardChannel cardChannel, byte[] bArr, byte[] bArr2, Map<String, Object> map);
}
