/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.cppsystem.cppbus.CppApp;
import recieptservice.com.recieptservice.PrinterInterface;

public class PrintKernel {
    private static PrintKernel sInstance = new PrintKernel();
    private Context mContext = CppApp.getContext();
    public PrinterInterface mPrinterService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        /* class com.sr.hardware.detection.ui.print.PrintKernel.AnonymousClass1 */

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            PrintKernel.this.mPrinterService = PrinterInterface.Stub.asInterface(iBinder);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            PrintKernel.this.mPrinterService = null;

        }
    };

    private PrintKernel() {
    }

    public static PrintKernel getInstance() {
        return sInstance;
    }

    public void init() {
        if (this.mPrinterService == null) {
            String lowerCase = "recIEptService.com.recIEptService".toLowerCase();
            Intent intent = new Intent();
            intent.setClassName(lowerCase, lowerCase + ".service.PrinterService");
            this.mContext.bindService(intent, this.mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }
}
