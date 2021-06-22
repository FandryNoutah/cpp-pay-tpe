package com.cppsystem.cppbus.cppcardlib.nfclib.spe;

import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CardChannel;
import com.cppsystem.cppbus.cppcardlib.nfclib.comm.CommandAPDU;
import com.cppsystem.cppbus.cppcardlib.nfclib.comm.ResponseAPDU;
import com.cppsystem.cppbus.cppcardlib.nfclib.readers.CardChannelCCID;
import com.cppsystem.cppbus.cppcardlib.nfclib.readers.Reader;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.RetailAPIMobile;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.Target;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.eID;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Tlv;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Util;

import java.util.Arrays;
import java.util.Map;

public class RetailAPIController {
    public static final byte[] APDU_CLOSE_SESSION = {0, -37, 0, 0, 4, -97, 35, 1, 22};
    public static final byte[] APDU_GET_CUSTOMERID = {0, -53, 0, 0, 4, 92, 2, -97, 49};
    public static final byte[] APDU_GET_MEHOLDERID = {0, -53, 0, 0, 4, 92, 2, -97, 48};
    public static final byte[] APDU_GET_OPENFIELD = {0, -53, 0, 0, 2, -97, 53};
    public static final byte[] APDU_OPEN_SESSION_ADELYA = {0, -37, 0, 0, 14, 48, 11, -97, 35, 1, 21, -97, 40, 5, 57, 57, 48, 51, 52};
    public static final byte[] APDU_OPEN_SESSION_TEST = {0, -37, 0, 0, eID.TLV_SEX, 48, 11, -97, 35, 1, 21, -97, 40, 4, 79, 82, 73, 49};
    public static final byte[] APDU_PUT_OPENFIELD = {0, -37, 0, 0, -1, 48, -1, -97, 35, 1, 0, -97, 53};
    public static final byte[] APDU_SELECT_RETAILAPI = {0, -92, 4, 0, 11, -96, 0, 0, 5, 69, 79, 82, 81, 80, 73, 73};
    private static final byte[] TLV_CUSTOMERID = {-97, 49};
    private static final byte[] TLV_MEHOLDERID = {-97, 48};

    /* JADX WARNING: Removed duplicated region for block: B:39:0x0137  */
    public static Target getRetailAPIMobile(byte[] bArr, CardChannel cardChannel, Map<String, Object> map, String str) {
        String str2;
        String str3 = BadgerConstants.LOG_TAG;
        Log.v(str3, "getRetailAPIMobile()");
        Reader reader = null;
        if (sendCommand(cardChannel, APDU_SELECT_RETAILAPI, "Select RetailAPI cardlet") == null) {
            return null;
        }
        ResponseAPDU sendCommand = sendCommand(cardChannel, APDU_OPEN_SESSION_ADELYA, "Open RetailAPI (99034) session");
        if (sendCommand == null) {
            sendCommand = sendCommand(cardChannel, APDU_OPEN_SESSION_TEST, "Open RetailAPI (Test) session");
        }
        if (sendCommand == null) {
            return null;
        }
        if (str != null && str.length() < 32) {
            byte[] copyOf = Arrays.copyOf(APDU_PUT_OPENFIELD, APDU_PUT_OPENFIELD.length + 1 + 1 + str.length());
            copyOf[4] = (byte) (str.length() + 10);
            copyOf[6] = (byte) (str.length() + 8);
            copyOf[APDU_PUT_OPENFIELD.length] = (byte) (str.length() + 2);
            copyOf[APDU_PUT_OPENFIELD.length + 1] = (byte) (str.length() + 1);
            System.arraycopy(str.getBytes(), 0, copyOf, APDU_PUT_OPENFIELD.length + 2, str.getBytes().length);
            if (sendCommand(cardChannel, copyOf, "Set OpenField") == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("CodeGroup loaded in OpenField: ");
            sb.append(str);
            Log.i(str3, sb.toString());
        }
        ResponseAPDU sendCommand2 = sendCommand(cardChannel, APDU_GET_MEHOLDERID, "Get meHolderID");
        if (sendCommand2 == null) {
            return null;
        }
        byte[] GetTLV = Tlv.GetTLV(sendCommand2.getBytes(), TLV_MEHOLDERID);
        if (GetTLV == null || GetTLV.length <= 0) {
            return null;
        }
        String byteArrayToString = Util.byteArrayToString(GetTLV);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("meHolderId: ");
        sb2.append(byteArrayToString);
        Log.i(str3, sb2.toString());
        ResponseAPDU sendCommand3 = sendCommand(cardChannel, APDU_GET_CUSTOMERID, "Get CustomerID");
        if (sendCommand3 == null) {
            return null;
        }
        byte[] GetTLV2 = Tlv.GetTLV(sendCommand3.getBytes(), TLV_CUSTOMERID);
        if (GetTLV2 != null && GetTLV2.length > 0) {
            if (GetTLV2[0] == 0 || GetTLV2[0] + 1 > GetTLV2.length) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Wrong customerID field: ");
                sb3.append(Util.byteArrayToString(GetTLV2));
                Log.w(str3, sb3.toString());
            } else {
                str2 = Util.byteArrayToString(Arrays.copyOfRange(GetTLV2, 1, GetTLV2[0]));
                StringBuilder sb4 = new StringBuilder();
                sb4.append("sCustomerId: ");
                sb4.append(str2);
                Log.i(str3, sb4.toString());
                sendCommand(cardChannel, APDU_CLOSE_SESSION, "Close RetailAPI session");
                if (cardChannel instanceof CardChannelCCID) {
                    reader = ((CardChannelCCID) cardChannel).getReader();
                }
                return new RetailAPIMobile(bArr, str2, byteArrayToString, reader);
            }
        }
        str2 = null;
        StringBuilder sb42 = new StringBuilder();
        sb42.append("sCustomerId: ");
        sb42.append(str2);
        Log.i(str3, sb42.toString());
        sendCommand(cardChannel, APDU_CLOSE_SESSION, "Close RetailAPI session");
        if (cardChannel instanceof CardChannelCCID) {
        }
        return new RetailAPIMobile(bArr, str2, byteArrayToString, reader);
    }

    private static ResponseAPDU sendCommand(CardChannel cardChannel, byte[] bArr, String str) {
        ResponseAPDU transmit = cardChannel.transmit(new CommandAPDU(bArr));
        String str2 = "Command ";
        String str3 = BadgerConstants.LOG_TAG;
        if (transmit == null || transmit.getSW1() != 144) {
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(str);
            sb.append(" failed: ");
            sb.append(Util.byteArrayToString(transmit.getBytes()));
            Log.v(str3, sb.toString());
            return null;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str2);
        sb2.append(str);
        sb2.append(" successfull: ");
        sb2.append(Util.byteArrayToString(transmit.getBytes()));
        Log.v(str3, sb2.toString());
        return transmit;
    }
}
