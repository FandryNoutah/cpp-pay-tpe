/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.util;

import android.os.Handler;
import android.os.Looper;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Collections;
import java.util.List;

/**
 * Created by Andrinarivo on 10/05/2017.
 * used on CPP November 2020
 */

public class TimeoutParseUser {
    private final long mTimeout;
    private LogInCallback mLogInCallback;
    private final Object mLock = new Object();
    private final Thread mThread;


    public TimeoutParseUser( long timeout) {
        mTimeout = timeout;
        mThread = new Thread() {
            @Override
            public void run() {
                if (isInterrupted()) return;
                try {
                    Thread.sleep(mTimeout);
                } catch (InterruptedException e) {
                    return;
                }
                cancel();
            }
        };
    }

    public void cancel() {
        synchronized (mLock) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    if(mLogInCallback!=null)
                        mLogInCallback.done(null, new ParseException(ParseException.TIMEOUT, "Timeout due to thread timeout expired"));
                }
            });
        }
    }

    public void logInInBackground(String username,String password, final LogInCallback callback) {
        synchronized (mLock) {
            mLogInCallback = callback;
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    synchronized (mLock) {
                        mThread.interrupt();
                        mLogInCallback.done(user, e);
                    }
                }
            });
            mThread.start();
        }
    }



}
