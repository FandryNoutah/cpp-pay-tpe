/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.ui.scan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.cppsystem.cppbus.CppApp;
import com.cppsystem.cppbus.R;

import com.cppsystem.cppbus.data.local.CppUserLastParam;
import com.cppsystem.cppbus.data.model.parse.CppFidMember;

import com.cppsystem.cppbus.util.CppConstant;
import com.cppsystem.cppbus.util.TimeoutQuery;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.pixplicity.easyprefs.library.Prefs;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_CARD_NOT_FOUND;
import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_CHECKIN;
import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_CHECKIN_COMMENTS;
import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_NO_CREDIT_COMMENTS;
import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_TRANSAC_NO_CREDIT;
import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_TRANSAC_OK;
import static com.cppsystem.cppbus.util.CppConstant.CPP_TRANSPORT_DEFAULT_ARRET;
import static com.cppsystem.cppbus.util.CppConstant.EXTRA_STATION_ID;
import static com.cppsystem.cppbus.util.CppConstant.PARSE_CPP_CLASS_ARRET;
import static com.cppsystem.cppbus.util.CppConstant.PARSE_CPP_CLASS_FID_MEMBER;
import static com.cppsystem.cppbus.util.CppConstant.PARSE_CPP_CLASS_VEHICLE;
import static com.cppsystem.cppbus.util.CppConstant.QUICK_TIMEOUT_QUERY;
import static com.cppsystem.cppbus.util.CppConstant.STATION_ID_PREFS;
import static com.cppsystem.cppbus.util.CppConstant.getDateTimeIndian;
import static com.cppsystem.cppbus.util.CppConstant.getDateTimeNow;

public class Scan1d2dFragment extends Fragment {


    private String toast;
    private MediaPlayer mediaplayer;

    private BeepManager beepManager;
    private String lastText;

    @BindView(R.id.barcode_scanner)
    DecoratedBarcodeView barcodeView;
    @BindView(R.id.flash2dScan)
    ImageView flash2dScan;
    boolean isflashEnabled=false;

    private ProgressDialog dialog;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }
            // TODO  need to call Parse scan
           /* lastText = result.getText();
            barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();*/
            try {
               // tapCard(CppConstant.getCryptedText(result.getText().toUpperCase()),dialog);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };


    @OnClick(R.id.flash2dScan)
    public void enableFlash(){
        if(isflashEnabled){
            Glide.with(getActivity()).load(R.drawable.baseline_flash_off_white_36dp)
                    .into( flash2dScan);
            isflashEnabled=false;
            barcodeView.setTorchOff();
        }else{
            Glide.with(getActivity()).load(R.drawable.baseline_flash_on_white_36dp)
                    .into( flash2dScan);
            isflashEnabled=true;
            barcodeView.setTorchOn();
        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scanner, container, false);
        ButterKnife.bind(this, root);

        barcodeView.initializeFromIntent(getActivity().getIntent());
        barcodeView.setStatusText("SCANNER LE QR CODE SUR LA CARTE DU CLIENT");
        barcodeView.decodeContinuous(callback);
        beepManager = new BeepManager(getActivity());

        dialog = new ProgressDialog(getActivity());
         dialog.setMessage("Veuillez patienter svp!");




        return root;
    }
    private void playBeeOk(){
       this.mediaplayer= MediaPlayer.create(getActivity(), (int) R.raw.ok_sound);
       this.mediaplayer.start();
    }
    private void playBeepNoCredit(){
        this.mediaplayer= MediaPlayer.create(getActivity(), (int) R.raw.beep_failed);
        this.mediaplayer.start();
    }
    private void playBeeNotFound(){
        this.mediaplayer= MediaPlayer.create(getActivity(), (int) R.raw.beep_not_found);
        this.mediaplayer.start();
    }
/*    private void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast = "Cancelled from fragment";
            } else {
                toast = "Scanned from fragment: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }



    private void playBeepOk(){
        this.mediaplayer= MediaPlayer.create(getActivity(), (int) R.raw.ok_sound);
        this.mediaplayer.start();
    }



  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }*/

}