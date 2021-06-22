/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus;


import android.content.Context;
import android.content.ContextWrapper;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;


import com.cppsystem.cppbus.data.model.parse.CppFidMember;

import com.cppsystem.cppbus.util.CppConstant;
import com.cppsystem.cppbus.util.OkhttpManager;
import com.cppsystem.cppbus.util.PrintKernel;
import com.jaredrummler.android.device.DeviceName;
import com.orm.SugarApp;
import com.orm.SugarContext;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.pixplicity.easyprefs.library.Prefs;

import net.danlew.android.joda.JodaTimeAndroid;
//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteDatabase;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import es.dmoral.toasty.Toasty;
import guy4444.extrnalloggerlibrary.ExtLog;
import guy4444.extrnalloggerlibrary.MyLoggerDB;
import okhttp3.CipherSuite;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.TlsVersion;

import static android.util.Log.DEBUG;
import static com.cppsystem.cppbus.util.CppConstant.getDateTimeIndian;


public class CppApp  extends SugarApp {
    private static CppApp mInstance;
    public boolean appDestroyed=false;
    public boolean allMemberDone=false;
    public boolean userVehicleDone=false;
    public boolean userLigneDone=false;
    public boolean userCooperativeDone=false;
    public boolean userTransportTypeDone=false;
    public boolean listCooperativeDone=false;
    public boolean listLigneDone=false;
    public boolean listLigneVarianceDone=false;
    public boolean listAllVarianceDone=false;
    public String currentStationId;
    private String CA_BUNDLE;
    public static String SERVER_HEALTH_CHECK="http://172.16.1.2/parse/health";
    //public static String SERVER_HEALTH_CHECK="https://qatransport.cpp-system.com:8585/parse/health";

    public static Context sContext;
    public static CppApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        System.loadLibrary("native-lib");
        sContext = getApplicationContext();
        String[] secrets = new Secrets().getlZWKFgMj(getPackageName()).split(";");
        SugarContext.init(this,secrets);
        DeviceName.init(this);
        JodaTimeAndroid.init(this);
        PrintKernel.getInstance().init();
        OkHttpClient.Builder mOkhttpClient = null;

        try {
            OkhttpManager.getInstance().setTrustrCertificates(getAssets().open("cpp_system.cer"));
             mOkhttpClient= OkhttpManager.getInstance().build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        DateTimeFormatter fmt1 = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTimeFormatter fmt2 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String datenow2 = fmt1.print(getDateTimeIndian());
        String date11 = datenow2+" 00:00:00";
        String date22 = datenow2+" 23:59:00";


        // for local use
        /*ParseObject.registerSubclass(CppFidMember.class);
        ParseObject.registerSubclass(CppTransportEvent.class);
        ParseObject.registerSubclass(CppFidMemberSearch.class);
        ParseObject.registerSubclass(CppEventTournee.class);
        ParseObject.registerSubclass(CppVehicleStatus.class);
        Parse.enableLocalDatastore(this);
        Parse.setLogLevel(DEBUG);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .enableLocalDataStore()
                .applicationId("CPP_APP_BACK")
                .clientKey("fnjCAu]n#5MX'}Jg")
                .clientBuilder(builder)
                .server("http://192.168.43.187:1337/parse")
                .build()
        );*/
        try{
           // String[] secrets = new Secrets().getlZWKFgMj(getPackageName()).split(";");
            SugarContext.init(this,secrets);

            DeviceName.init(this);
            JodaTimeAndroid.init(this);

            String xyz = CppConstant.decrypt(new String (Base64.decode(getXYZ(),Base64.DEFAULT)),secrets[0],secrets[1]);
            String opqlm = CppConstant.decrypt(new String (Base64.decode(getOPQLM(),Base64.DEFAULT)),secrets[0],secrets[1]);
            String abcd = CppConstant.decrypt(new String (Base64.decode(getABDCD(),Base64.DEFAULT)),secrets[0],secrets[1]);

            ParseObject.registerSubclass(CppFidMember.class);

            Parse.enableLocalDatastore(this);
            Parse.setLogLevel(DEBUG);
            Parse.initialize(new Parse.Configuration.Builder(this)
                    .enableLocalDataStore()
                    .applicationId(xyz)
                    .clientKey(abcd)
                    .clientBuilder(mOkhttpClient)
                    //.server(opqlm)
                    .server("http://172.16.1.2/parse")
                    //.server("https://qatransport.cpp-system.com:8585/parse")
                    .build()
            );

        }catch (Exception ex){

        }

        mInstance = this;
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

    }

    public static Context getContext() {
        return sContext;
    }

    public String getCurrentStationId() {
        return currentStationId;
    }

    public void setCurrentStationId(String currentStationId) {
        this.currentStationId = currentStationId;
    }

    public native String getXYZ();
    public native String getOPQLM();
    public native String getABDCD();



    public static synchronized CppApp getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);

    }

}
