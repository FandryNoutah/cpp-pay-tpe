package com.orm;

import android.app.Application;
import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;

import static com.orm.util.ContextUtil.getContext;

public class SugarApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SQLiteDatabase.loadLibs(this);
       // SugarContext.init(this,new String[]{}); // init only on CppApp
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }




}
