package com.cppsystem.cppbus.cppcardlib.nfclib.usb;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
//import kotlin.jvm.internal.ByteCompanionObject;

public class CcidDevice implements Runnable {
    private static final String LOG_TAG = "AdelyaCCID";
    private static final byte OFFSET_ERROR = 8;
    private static final byte OFFSET_PROTOCOL_NUM = 9;
    private static final byte OFFSET_STATUS = 7;
    public static final byte PROTOCOL_T0 = 0;
    public static final byte PROTOCOL_T1 = 1;
    public static final byte PROTOCOL_UNKNOWN = -1;
    private static final int RDR_TO_PC_DATABLOCK_DATA_MAX_LENGTH = 65536;
    private static final int RDR_TO_PC_DATABLOCK_HEADER_LENGTH = 10;
    private static final byte SLOT_NUMBER = 0;
    private static final byte VOLTAGE_3V = 2;
    private static final byte VOLTAGE_AUTO = 0;
    private byte[] bufferDataIn;
    private UsbEndpoint endpointBulkIn;
    private UsbEndpoint endpointBulkOut;
    private UsbEndpoint endpointIntr;
    private List<Handler> handlers = new ArrayList();
    private Thread interruptThread = null;
    private boolean opened;
    private byte sequenceNumber;
    private UsbDeviceConnection usbConnection;
    private UsbDevice usbDevice;
    private UsbInterface usbInterface;

    protected CcidDevice(UsbManager usbManager, UsbDevice usbDevice2, UsbInterface usbInterface2) throws CcidException {
        this.sequenceNumber = 0;
        this.bufferDataIn = new byte[65546];
        this.opened = true;
        String str = LOG_TAG;
        Log.d(str, "Create CCID device");
        StringBuilder sb = new StringBuilder();
        sb.append("descriptor: ");
        sb.append(Integer.toHexString(usbDevice2.describeContents()));
        Log.v(str, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("device name: ");
        sb2.append(usbDevice2.getDeviceName());
        Log.v(str, sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("interface number: ");
        sb3.append(usbDevice2.getInterfaceCount());
        Log.v(str, sb3.toString());
        this.usbDevice = usbDevice2;
        this.usbInterface = usbInterface2;
        try {
            int endpointCount = usbInterface2.getEndpointCount();
            for (int i = 0; i < endpointCount; i++) {
                UsbEndpoint endpoint = usbInterface2.getEndpoint(i);
                if (this.endpointIntr == null && endpoint.getType() == 3) {
                    this.endpointIntr = endpoint;
                } else if (this.endpointBulkOut == null && (endpoint.getType() & 3) == 2 && (endpoint.getDirection() & 128) == 0) {
                    this.endpointBulkOut = endpoint;
                } else if (this.endpointBulkIn == null && (endpoint.getType() & 3) == 2 && (endpoint.getDirection() & 128) == 128) {
                    this.endpointBulkIn = endpoint;
                }
            }
        } catch (Exception e) {
            Log.w(str, e.getMessage(), e);
        }
        if (this.endpointIntr == null) {
            throw new CcidException("No Interrupt endpoint on this USB device.");
        } else if (this.endpointBulkOut == null) {
            throw new CcidException("No bulk-out endpoint on this USB device.");
        } else if (this.endpointBulkIn != null) {
            Log.d(str, "Endpoints created successfully.");
            this.usbConnection = usbManager.openDevice(usbDevice2);
            UsbDeviceConnection usbDeviceConnection = this.usbConnection;
            if (usbDeviceConnection == null) {
                Log.i(str, "Connection to USB interface FAILED");
                throw new CcidException("Unable to open connection with the interface.");
            } else if (usbDeviceConnection.claimInterface(usbInterface2, true)) {
                Log.i(str, "Connection to USB interface OPENED");
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Descriptor: ");
                sb4.append(byteArrayToString(this.usbConnection.getRawDescriptors()));
                Log.i(str, sb4.toString());
            } else {
                Log.w(str, "Unable to claim access to USB device");
                throw new CcidException("Unable to claim access to USB device.");
            }
        } else {
            throw new CcidException("No bulk-in endpoint on this USB device.");
        }
    }

    /* access modifiers changed from: protected */
    public final byte[] sendCommand(byte[] bArr) throws CcidException {
        int bulkTransfer;
        String str = ") : ";
        String str2 = LOG_TAG;
        try {
            Log.v(str2, "sendCommand");
            byte b = bArr[6];
            int bulkTransfer2 = this.usbConnection.bulkTransfer(this.endpointBulkOut, bArr, bArr.length, 0);
            StringBuilder sb = new StringBuilder();
            sb.append("  Data sent OUT (");
            sb.append(bulkTransfer2);
            sb.append(str);
            sb.append(byteArrayToString(bArr));
            Log.v(str2, sb.toString());
            int i = 0;
            while (true) {
                bulkTransfer = this.usbConnection.bulkTransfer(this.endpointBulkIn, this.bufferDataIn, this.bufferDataIn.length, 0);
                if (this.bufferDataIn[6] == b && (this.bufferDataIn[7] & -128) == 0) {
                    break;
                }
                int i2 = i + 1;
                if (i >= 5) {
                    break;
                }
                i = i2;
            }
            byte[] bArr2 = new byte[bulkTransfer];
            System.arraycopy(this.bufferDataIn, 0, bArr2, 0, bulkTransfer);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("  Data received (");
            sb2.append(bulkTransfer);
            sb2.append(str);
            sb2.append(byteArrayToString(bArr2));
            Log.v(str2, sb2.toString());
            if ((this.bufferDataIn[0] & -16) != Byte.MIN_VALUE) {
                Log.v(str2, "  (Unexpected data)");
            }
            if ((this.bufferDataIn[7] & 3) == 2) {
                Log.v(str2, "  (No ICC is present)");
            }
            if ((this.bufferDataIn[7] & -64) == 64) {
                Log.v(str2, "  (Command failed)");
            }
            return bArr2;
        } catch (Exception e) {
            throw new CcidException("  Send command failed.", e);
        }
    }

    private Card waitForCard() {
        String str = LOG_TAG;
        Log.v(str, "waitForCard()");
        ByteBuffer allocate = ByteBuffer.allocate(10);
        UsbRequest usbRequest = new UsbRequest();
        usbRequest.initialize(this.usbConnection, this.endpointIntr);
        Card card = null;
        boolean z = true;
        while (true) {
            if (!z) {
                break;
            }
            try {
                usbRequest.queue(allocate, 10);
                if (this.usbConnection.requestWait() != usbRequest) {
                    Log.e(str, "requestWait failed, exiting");
                    break;
                } else if (allocate.get(0) == 80 && (allocate.get(1) & 3) == 3) {
                    byte[] atr = getAtr();
                    if (atr == null) {
                        Log.w(str, "Unresponsive card");
                    } else {
                        byte[] uid = getUid();
                        if (uid == null) {
                            getAtr();
                        }
                        card = createCard(atr, uid);
                        z = false;
                    }
                }
            } catch (Exception e) {
                Log.e(str, "waitForCard() Exception", e);
            } catch (Throwable th) {
                usbRequest.close();
                throw th;
            }
        }
        usbRequest.close();
        return card;
    }

    /* access modifiers changed from: protected */
    public Card createCard(byte[] bArr, byte[] bArr2) {
        return new Card(bArr, bArr2);
    }

    /* access modifiers changed from: protected */
    public byte[] getAtr() {
        String str = LOG_TAG;
        Log.v(str, "getAtr()");
        byte b = this.sequenceNumber;
        this.sequenceNumber = (byte) (b + 1);
        byte[] bArr = {98, 0, 0, 0, 0, 0, b, 0, 0, 0};
        byte[] bArr2 = null;
        try {
            byte[] sendCommand = sendCommand(bArr);
            if (!(sendCommand[7] == 0 && sendCommand[8] == 0)) {
                byte b2 = this.sequenceNumber;
                this.sequenceNumber = (byte) (b2 + 1);
                bArr[6] = b2;
                bArr[7] = 2;
                sendCommand = sendCommand(bArr);
            }
            if (sendCommand != null && (sendCommand[1] & 255) > 0) {
                bArr2 = Arrays.copyOfRange(sendCommand, 10, sendCommand[1] + 10);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("ATR: ");
            sb.append(byteArrayToString(bArr2));
            Log.v(str, sb.toString());
        } catch (CcidException e) {
            Log.e(str, "Unable to get ATR", e);
        } catch (Exception e2) {
            Log.e(str, "Unexpected exception", e2);
        }
        return bArr2;
    }

    /* access modifiers changed from: protected */
    public byte getProtocol() {
        String str = LOG_TAG;
        Log.v(str, "getProtocol()");
        try {
            byte b = this.sequenceNumber;
            this.sequenceNumber = (byte) (b + 1);
            byte[] sendCommand = sendCommand(new byte[]{108, 0, 0, 0, 0, 0, b, 0, 0, 0});
            if (sendCommand[7] != 0 || sendCommand[8] != 0) {
                return -1;
            }
            byte b2 = sendCommand[9];
            StringBuilder sb = new StringBuilder();
            sb.append("Protocol: ");
            sb.append(b2);
            Log.v(str, sb.toString());
            return b2;
        } catch (CcidException e) {
            Log.e(str, "Unable to get Protocol", e);
            return -1;
        } catch (Exception e2) {
            Log.e(str, "Unexpected exception", e2);
            return -1;
        }
    }

    /* access modifiers changed from: protected */
    public byte[] getUid() {
        String str = LOG_TAG;
        Log.v(str, "getUID()");
        byte[] sendApdu = sendApdu(CcidConstants.APDU_GET_UID);
        StringBuilder sb = new StringBuilder();
        sb.append("UID command result: ");
        sb.append(Arrays.toString(sendApdu));
        Log.v(str, sb.toString());
        if (sendApdu == null) {
            Log.v(str, "Get UID failed");
        } else if (sendApdu.length > 2) {
            return Arrays.copyOfRange(sendApdu, 0, sendApdu.length - 2);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to get UID. Response=");
            sb2.append(Arrays.toString(sendApdu));
            Log.v(str, sb2.toString());
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public byte[] sendApdu(byte[] bArr) {
        byte length = (byte) bArr.length;
        byte b = this.sequenceNumber;
        this.sequenceNumber = (byte) (b + 1);
        byte[] bArr2 = {111, length, 0, 0, 0, 0, b, 0, 0, 0};
        StringBuilder sb = new StringBuilder();
        sb.append("Send APDU command: ");
        sb.append(byteArrayToString(bArr));
        String sb2 = sb.toString();
        String str = LOG_TAG;
        Log.v(str, sb2);
        byte[] bArr3 = new byte[(bArr2.length + bArr.length)];
        byte[] bArr4 = null;
        try {
            System.arraycopy(bArr2, 0, bArr3, 0, bArr2.length);
            System.arraycopy(bArr, 0, bArr3, bArr2.length, bArr.length);
            byte[] sendCommand = sendCommand(bArr3);
            if (sendCommand[0] == Byte.MIN_VALUE) {
                int i = (sendCommand[1] & 255) + ((sendCommand[2] & 255) << 8) + ((sendCommand[3] & 255) << 16) + ((sendCommand[4] & 255) << 24);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Apdu response length=");
                sb3.append(i);
                Log.v(str, sb3.toString());
                if (i > 0) {
                    bArr4 = new byte[i];
                    System.arraycopy(sendCommand, 10, bArr4, 0, i);
                }
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Send APDU command result: ");
            sb4.append(byteArrayToString(bArr4));
            Log.v(str, sb4.toString());
        } catch (CcidException e) {
            Log.e(str, "Failed to send APDU command", e);
        } catch (Exception e2) {
            Log.e(str, "Failed to send APDU command, unknown error.", e2);
        }
        return bArr4;
    }

    public void run() {
        boolean z = true;
        while (!this.handlers.isEmpty() && z) {
            Card waitForCard = waitForCard();
            Message obtain = Message.obtain();
            String str = LOG_TAG;
            if (waitForCard == null) {
                UsbDeviceConnection usbDeviceConnection = this.usbConnection;
                if (usbDeviceConnection == null || !usbDeviceConnection.claimInterface(this.usbInterface, false)) {
                    obtain.what = -10;
                    obtain.obj = this;
                    Log.d(str, "UsbDevice Connection lost");
                    z = false;
                } else {
                    obtain.what = -1;
                    obtain.obj = this;
                    Log.d(str, "UsbDevice Connection error");
                }
            } else {
                obtain.what = 0;
                obtain.obj = waitForCard;
            }
            List<Handler> list = this.handlers;
            ListIterator listIterator = list.listIterator(list.size());
            while (listIterator.hasPrevious()) {
                try {
                    ((Handler) listIterator.previous()).sendMessage(obtain);
                    obtain = Message.obtain(obtain);
                } catch (Exception e) {
                    listIterator.remove();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error on handler => handler removed. Cause: ");
                    sb.append(e.getClass().getName());
                    Log.w(str, sb.toString(), e);
                }
            }
        }
        close();
    }

    public boolean isCardPresent() {
        String str = LOG_TAG;
        boolean z = false;
        byte b = this.sequenceNumber;
        this.sequenceNumber = (byte) (b + 1);
        try {
            byte[] sendCommand = sendCommand(new byte[]{101, 0, 0, 0, 0, 0, b, 0, 0, 0});
            if (sendCommand[0] == -127 && sendCommand[7] == 0) {
                z = true;
            }
        } catch (CcidException e) {
            Log.e(str, "Unable to send slot status command", e);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Card present: ");
        sb.append(z);
        Log.i(str, sb.toString());
        return z;
    }

    public void registerCardInserted(Handler handler) {
        if (!this.handlers.contains(handler)) {
            this.handlers.add(handler);
            Thread thread = this.interruptThread;
            if (thread == null || !thread.isAlive()) {
                this.interruptThread = new Thread(this);
                this.interruptThread.start();
            }
        }
    }

    public boolean isOpen() {
        return this.opened;
    }

    public void close() {
        String str = LOG_TAG;
        try {
            this.opened = false;
            Log.d(str, "close()");
            if (this.usbConnection != null) {
                this.usbConnection.releaseInterface(this.usbInterface);
                this.usbConnection = null;
            }
        } catch (Exception e) {
            Log.e(str, "Error in close()", e);
        }
    }

    public void unregisterCardInserted(Handler handler) {
        if (this.handlers.contains(handler)) {
            this.handlers.remove(handler);
        }
    }

    public UsbDevice getUsbDevice() {
        return this.usbDevice;
    }

    public boolean equals(Object obj) {
        if (obj instanceof CcidDevice) {
            UsbDevice usbDevice2 = this.usbDevice;
            if (usbDevice2 != null) {
                return usbDevice2.equals(((CcidDevice) obj).usbDevice);
            }
        }
        return false;
    }

    private static String byteArrayToString(byte[] bArr) {
        StringBuilder sb;
        StringBuilder sb2 = new StringBuilder();
        if (bArr == null) {
            sb = null;
        } else {
            for (byte valueOf : bArr) {
                sb2.append(String.format("%02x ", new Object[]{Byte.valueOf(valueOf)}));
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("[");
            sb3.append(sb2.toString().toUpperCase(Locale.getDefault()));
            sb3.append("]");
            sb = new StringBuilder(sb3.toString());
        }
        return sb != null ? sb.toString() : "";
    }
}
