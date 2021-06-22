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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Collections;
import java.util.List;

/**
 * Created by Andrinarivo on 10/05/2017.
 * used on CPP November 2020
 */

public class TimeoutQuery<T extends ParseObject> {
    private ParseQuery<T> mQuery;
    private final long mTimeout;
    private FindCallback<T> mCallback;
    private CountCallback cCallback;
    private GetCallback<T> gCallback;
    private final Object mLock = new Object();
    private final Thread mThread;


    public TimeoutQuery(ParseQuery<T> query, long timeout) {
        mQuery = query;
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
                cancelQuery();
            }
        };
    }

    public void cancelQuery() {
        synchronized (mLock) {
            if (null == mQuery) return; // it's already canceled
            mQuery.cancel();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if(cCallback!=null)
                        cCallback.done(0, new ParseException(ParseException.TIMEOUT, ""));
                    if(mCallback!=null)
                        mCallback.done(Collections.<T>emptyList(), new ParseException(ParseException.TIMEOUT, "TIMEOUT"));
                    if(gCallback!=null)
                        gCallback.done(null, new ParseException(ParseException.TIMEOUT, "TIMEOUT"));
                }
            });
        }
    }

    public void findInBackground(final FindCallback<T> callback) {
        synchronized (mLock) {
            mCallback = callback;
            mQuery.findInBackground(new FindCallback<T>() {
                @Override
                public void done(List<T> ts, ParseException e) {
                    synchronized (mLock) {
                        mThread.interrupt();
                        mQuery = null;
                        mCallback.done(ts, e);
                    }
                }
            });
            mThread.start();
        }
    }
    public void getFirstInBackground(final GetCallback<T> gcallback){
        synchronized (mLock) {
            gCallback = gcallback;
            mQuery.getFirstInBackground(new GetCallback<T>() {
                @Override
                public void done(T object, ParseException e) {
                    synchronized (mLock) {
                        mThread.interrupt();
                        mQuery = null;
                        gCallback.done(object, e);
                    }
                }
            });

            mThread.start();
        }

    }
    public void getFirstInBackground(String objectId, final GetCallback<T> gcallback){
        synchronized (mLock) {
            gCallback = gcallback;
            mQuery.getInBackground(objectId, new GetCallback<T>() {
                @Override
                public void done(T object, ParseException e) {
                    synchronized (mLock) {
                        mThread.interrupt();
                        mQuery = null;
                        gCallback.done(object, e);
                    }
                }
            });

            mThread.start();
        }

    }


    public void countInBackground(final CountCallback callback){
        synchronized (mLock) {
            cCallback = callback;
            mQuery.countInBackground(new CountCallback() {
                @Override
                public void done(int count, ParseException e) {
                    synchronized (mLock) {
                        mThread.interrupt();
                        mQuery = null;
                        cCallback.done(count, e);
                    }
                }
            });
            mThread.start();
        }
    }
}
