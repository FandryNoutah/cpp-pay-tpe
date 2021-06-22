package com.cppsystem.cppbus.cppcardlib.nfclib.usb;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CcidController {
    private static final String LOG_TAG = "AdelyaCCID";
    private static final String TAG = "CcidController";
    protected static CcidController instance;
    protected Map<UsbDevice, CcidDevice[]> devices = new HashMap();

    public static CcidController getInstance() {
        if (instance == null) {
            instance = new CcidController();
        }
        return instance;
    }

    protected CcidController() {
    }

    public final List<CcidDevice> getDevicesList(UsbManager usbManager) {
        ArrayList arrayList = new ArrayList();
        if (usbManager == null) {
            Log.e(TAG, "Attention, mUsbManager est null !! --> return liste vide");
            return arrayList;
        }
        Collection values = usbManager.getDeviceList().values();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        Iterator it = values.iterator();
        while (true) {
            boolean hasNext = it.hasNext();
            String str = LOG_TAG;
            if (hasNext) {
                UsbDevice usbDevice = (UsbDevice) it.next();
                CcidDevice[] ccidDeviceArr = null;
                try {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Permission for device ");
                    sb3.append(usbDevice.getDeviceName());
                    sb3.append(": ");
                    sb3.append(usbManager.hasPermission(usbDevice));
                    Log.d(str, sb3.toString());
                    ccidDeviceArr = getCcidDevice(usbManager, usbDevice);
                } catch (SecurityException unused) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("No permission to access the USB device ");
                    sb4.append(usbDevice.getDeviceName());
                    Log.i(str, sb4.toString());
                }
                String str2 = " ";
                if (ccidDeviceArr != null) {
                    arrayList.addAll(Arrays.asList(ccidDeviceArr));
                    sb.append(usbDevice.getDeviceName());
                    sb.append(str2);
                } else {
                    sb2.append(usbDevice.getDeviceName());
                    sb2.append(str2);
                }
            } else {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Ccid devices:");
                sb5.append(sb);
                sb5.append("\nOther devices:");
                sb5.append(sb2);
                Log.d(str, sb5.toString());
                return arrayList;
            }
        }
    }

    public final CcidDevice[] getCcidDevice(UsbManager usbManager, UsbDevice usbDevice) {
        CcidDevice[] ccidDeviceArr = (CcidDevice[]) this.devices.get(usbDevice);
        if (ccidDeviceArr != null) {
            int length = ccidDeviceArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (!ccidDeviceArr[i].isOpen()) {
                    ccidDeviceArr = null;
                    break;
                } else {
                    i++;
                }
            }
        }
        if (ccidDeviceArr == null) {
            try {
                ccidDeviceArr = createCcidDevices(usbManager, usbDevice);
                this.devices.put(usbDevice, ccidDeviceArr);
            } catch (CcidException e) {
                Log.w(LOG_TAG, "Cannot use the USB device for CCID", e);
                return null;
            }
        }
        return ccidDeviceArr;
    }

    /* access modifiers changed from: protected */
    public CcidDevice createDevice(UsbManager usbManager, UsbDevice usbDevice, UsbInterface usbInterface) throws CcidException {
        return new CcidDevice(usbManager, usbDevice, usbInterface);
    }

    /* access modifiers changed from: protected */
    public CcidDevice[] createCcidDevices(UsbManager usbManager, UsbDevice usbDevice) throws CcidException {
        String str = LOG_TAG;
        Log.d(str, "Create CCID device");
        StringBuilder sb = new StringBuilder();
        sb.append("descriptor: ");
        sb.append(Integer.toHexString(usbDevice.describeContents()));
        Log.v(str, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("device name: ");
        sb2.append(usbDevice.getDeviceName());
        Log.v(str, sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("interface number: ");
        sb3.append(usbDevice.getInterfaceCount());
        Log.v(str, sb3.toString());
        int interfaceCount = usbDevice.getInterfaceCount();
        if (interfaceCount >= 1) {
            List list = null;
            int i = 0;
            while (true) {
                int i2 = i + 1;
                UsbInterface usbInterface = usbDevice.getInterface(i);
                String str2 = "Interface ";
                if (usbInterface.getEndpointCount() < 3) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str2);
                    sb4.append(usbInterface.getId());
                    sb4.append(" has too few endPoints:");
                    sb4.append(usbInterface.getEndpointCount());
                    Log.v(str, sb4.toString());
                } else {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(str2);
                    sb5.append(usbInterface.getId());
                    sb5.append(" is ok, endPoints:");
                    sb5.append(usbInterface.getEndpointCount());
                    Log.v(str, sb5.toString());
                    if (list == null) {
                        list = new ArrayList();
                    }
                    CcidDevice createDevice = createDevice(usbManager, usbDevice, usbInterface);
                    if (createDevice != null) {
                        list.add(createDevice);
                    }
                }
                if (i2 >= interfaceCount) {
                    break;
                }
                i = i2;
            }
            if (list != null) {
                return (CcidDevice[]) list.toArray(new CcidDevice[0]);
            }
            throw new CcidException("No USB interface has enought endpoints.");
        }
        Log.w(str, "could not find an USB interface");
        throw new CcidException("No USB interface available.");
    }
}
