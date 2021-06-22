/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.ui.login;


import android.os.Bundle;
import android.os.Handler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.cppsystem.cppbus.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * Created by anndrinarivo on 23/08/2016.
 */

public  class LoadingFragment extends DialogFragment implements LoginActivity.OnLoginFinishListener{
    private static final String ARG_PARAM1="textNotice";
    private Handler progressBarHandler = new Handler();
    private long start=0;
    private String textnotice;
    private TextView Notice;


    @BindView(R.id.parentLoadingFgt)
    FrameLayout parent;

    public static LoadingFragment newInstance(String citationText) {
        LoadingFragment fragment = new LoadingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, citationText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        if (getArguments() != null) {
            textnotice = getArguments().getString(ARG_PARAM1);

        }

    }
    private void getListCppIncident(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_loading, container, false);
        ButterKnife.bind(this,v);

        return v;
    }
/*
    @OnClick(R.id.closeLoading)*/
    public void close(){
        /*if(getActivity()!=null)getActivity().onBackPressed();
        else{
            onDestroyView();
        }*/
        onDetach();
        onDestroyView();
    }


    @Override
    public void onLoginFinished() {
        close();
    }
}
