package com.cppsystem.cppbus.cppcardlib.nfclib;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.cppsystem.cppbus.cppcardlib.nfclib.readers.AcsAcr;
import com.cppsystem.cppbus.cppcardlib.nfclib.readers.Reader;
import com.cppsystem.cppbus.cppcardlib.nfclib.readers.Weneo;
import com.cppsystem.cppbus.cppcardlib.nfclib.usb.CcidController;
import com.cppsystem.cppbus.cppcardlib.nfclib.usb.CcidDevice;
import com.cppsystem.cppbus.cppcardlib.nfclib.usb.CcidException;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.BadgerConstants;

public class ReadersController extends CcidController {
    public static final int ACS_ACR1255U_PID = 8767;
    public static final int ACS_VID = 1839;
    public static final int HID_VID = 1899;
    public static final int IDENTIVE_CLOUD4700_PID = 22304;
    public static final int IDENTIVE_SCL010_PID = 21137;
    public static final int IDENTIVE_SCL011_PID = 21139;
    public static final int IDENTIVE_VID = 1254;
    public static final int NEOWAVE_VID = 7693;
    public static final int OMNIKEY_5321CL_PID = 21280;
    public static final int OMNIKEY_5321_PID = 21281;
    public static final int VASCO_DP905_PID = 1;
    public static final int VASCO_VID = 6724;
    public static final int WENEO_PID = 8243;

    protected ReadersController() {
    }

    public static ReadersController getInstance() {
        if (instance == null) {
            instance = new ReadersController();
        }
        return (ReadersController) instance;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00b6 A[Catch:{ CcidException -> 0x00ff }] */
    public CcidDevice createDevice(UsbManager usbManager, UsbDevice usbDevice, UsbInterface usbInterface) {
        Reader reader;
        String str = BadgerConstants.LOG_TAG;
        try {
            int vendorId = usbDevice.getVendorId();
            if (vendorId == 1254) {
                if (usbDevice.getProductId() == 22304) {
                    reader = new Reader(usbManager, usbDevice, usbInterface);
                    if (usbInterface.getId() == 1) {
                        reader.setContacless(true);
                        reader.setName("IDENTIVE CLOUD 4700F Contactless");
                    } else {
                        reader.setContacless(true);
                        reader.setName("IDENTIVE CLOUD 4700F Contact");
                    }
                } else {
                    if (usbDevice.getProductId() != 21137) {
                        if (usbDevice.getProductId() == 21139) {
                        }
                    }
                    reader = new Reader(usbManager, usbDevice, usbInterface);
                    reader.setContacless(true);
                    reader.setName("SCL01x");
                }
                if (reader == null) {
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Reader found: ");
                sb.append(reader.getName());
                Log.i(str, sb.toString());
                return reader;
            } else if (vendorId != 1839) {
                if (vendorId != 1899) {
                    if (vendorId != 6724) {
                        if (vendorId == 7693) {
                            if (usbDevice.getProductId() == 8243) {
                                reader = new Weneo(usbManager, usbDevice, usbInterface);
                                if (reader == null) {
                                    reader = new Reader(usbManager, usbDevice, usbInterface);
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append(reader.getName());
                                    sb2.append(" ");
                                    sb2.append(usbDevice.getVendorId());
                                    sb2.append("/");
                                    sb2.append(usbDevice.getProductId());
                                    reader.setName(sb2.toString());
                                }
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("Reader found: ");
                                sb3.append(reader.getName());
                                Log.i(str, sb3.toString());
                                return reader;
                            }
                        }
                    } else if (usbDevice.getProductId() == 1) {
                        reader = new Reader(usbManager, usbDevice, usbInterface);
                        reader.setContacless(false);
                        reader.setName("Vasco DP905");
                        if (reader == null) {
                        }
                        StringBuilder sb32 = new StringBuilder();
                        sb32.append("Reader found: ");
                        sb32.append(reader.getName());
                        Log.i(str, sb32.toString());
                        return reader;
                    }
                } else if (usbDevice.getProductId() == 21281 || usbDevice.getProductId() == 21280) {
                    reader = new Reader(usbManager, usbDevice, usbInterface);
                    reader.setContacless(true);
                    reader.setName("Omnikey 5321");
                    if (reader == null) {
                    }
                    StringBuilder sb322 = new StringBuilder();
                    sb322.append("Reader found: ");
                    sb322.append(reader.getName());
                    Log.i(str, sb322.toString());
                    return reader;
                }
            } else if (usbDevice.getProductId() == 8767) {
                reader = new AcsAcr(usbManager, usbDevice, usbInterface);
                if (reader == null) {
                }
                StringBuilder sb3222 = new StringBuilder();
                sb3222.append("Reader found: ");
                sb3222.append(reader.getName());
                Log.i(str, sb3222.toString());
                return reader;
            }
            reader = null;
            if (reader == null) {
            }
            StringBuilder sb32222 = new StringBuilder();
            sb32222.append("Reader found: ");
            sb32222.append(reader.getName());
            Log.i(str, sb32222.toString());
            return reader;
        } catch (CcidException unused) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Cannot use USB device VID=");
            sb4.append(usbDevice.getVendorId());
            sb4.append(" PID=");
            sb4.append(usbDevice.getProductId());
            Log.w(str, sb4.toString());
            return null;
        }
    }
}
