/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;

public class CustomTextSwitcher extends TextSwitcher {
    public CustomTextSwitcher(Context context) {
        super(context);
    }

    public CustomTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    @Override
    public void setText(CharSequence text) {

          /*  if(text.length()>12){
                text= text.toString().substring(0,12);
                TextView t = (TextView) getNextView();
                t.setMaxLines(1);
                t.setTextSize(14);
                t.setText(text);
            }else
                super.setText(text);*/

       super.setText(text);
    }

    @Override
    public void setCurrentText(CharSequence text) {
        /*if(text.length()>12){
            text= text.toString().substring(0,12);
            TextView t = (TextView) getCurrentView();
            t.setMaxLines(1);
            t.setTextSize(14);
            t.setText(text);
        }else*/
            super.setCurrentText(text);

       //
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return super.getAccessibilityClassName();
    }
}
