/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.ui.scan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.cppsystem.cppbus.CppApp;
import com.cppsystem.cppbus.R;
import com.cppsystem.cppbus.Secrets;
import com.cppsystem.cppbus.cppcardlib.nfclib.NfcTagController;
import com.cppsystem.cppbus.cppcardlib.nfclib.targets.Target;
import com.cppsystem.cppbus.cppcardlib.nfclib.util.Util;
import com.cppsystem.cppbus.data.local.CppUserLastParam;
import com.cppsystem.cppbus.data.local.MessageEvent;
import com.cppsystem.cppbus.data.model.parse.CppFidMember;


import com.cppsystem.cppbus.ui.login.LoadingFragment;
import com.cppsystem.cppbus.ui.login.LoginActivity;
import com.cppsystem.cppbus.ui.sync.CppSynActivity;
import com.cppsystem.cppbus.util.CppConstant;
import com.cppsystem.cppbus.util.TimeoutQuery;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.boltsinternal.Continuation;
import com.parse.boltsinternal.Task;
import com.pixplicity.easyprefs.library.Prefs;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_CARD_HACKED;
import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_CARD_NOT_FOUND;
import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_CHECKIN;
import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_CHECKIN_COMMENTS;
import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_CHECKIN_GET_CREDIT;
import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_CPP;
import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_NO_CREDIT_COMMENTS;
import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_TRANSAC_NO_CREDIT;
import static com.cppsystem.cppbus.util.CppConstant.CAISSE_TYPE_TRANSAC_OK;
import static com.cppsystem.cppbus.util.CppConstant.CPP_TRANSPORT_DEFAULT_ARRET;
import static com.cppsystem.cppbus.util.CppConstant.CPP_VEHICLE_DEPART_10_MIN;
import static com.cppsystem.cppbus.util.CppConstant.CPP_VEHICLE_DEPART_15_MIN;
import static com.cppsystem.cppbus.util.CppConstant.CPP_VEHICLE_DEPART_IMM;
import static com.cppsystem.cppbus.util.CppConstant.CPP_VEHICLE_EN_SERVICE;
import static com.cppsystem.cppbus.util.CppConstant.CPP_VEHICLE_STATUS;
import static com.cppsystem.cppbus.util.CppConstant.EXTRA_STATION_ID;
import static com.cppsystem.cppbus.util.CppConstant.PARSE_CPP_CLASS_ARRET;
import static com.cppsystem.cppbus.util.CppConstant.PARSE_CPP_CLASS_FID_MEMBER;
import static com.cppsystem.cppbus.util.CppConstant.PARSE_CPP_CLASS_VEHICLE;
import static com.cppsystem.cppbus.util.CppConstant.QUICK_TIMEOUT_QUERY;
import static com.cppsystem.cppbus.util.CppConstant.STATION_ID_PREFS;
import static com.cppsystem.cppbus.util.CppConstant.TICKET_PRICE_EDTX;
import static com.cppsystem.cppbus.util.CppConstant.generateTicketNumber;
import static com.cppsystem.cppbus.util.CppConstant.getDateTimeIndian;
import static com.cppsystem.cppbus.util.CppConstant.getDateTimeNow;
import static com.cppsystem.cppbus.util.CppConstant.getFidMemberInfo;

public class ScannerNfcActivity extends AppCompatActivity {
    @BindView(R.id.closeNfc)
    LottieAnimationView closeNfc;
    @BindView(R.id.nfcAnimation)
    LottieAnimationView nfcAnimation;
    @BindView(R.id.enableNFC)
    AppCompatButton enableNFC;
    @BindView(R.id.textInfoNfc)
    TextView textInfoNfc;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    private MediaPlayer mediaplayer;
    private boolean ishowLoading,isSynchro,isCredit;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_nfc);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        ishowLoading=getIntent().getBooleanExtra("SHOW_LOADING",false);
        dialog = new ProgressDialog(this);
       // dialog.setMessage("Veuillez patienter svp!");
        /*if(ishowLoading){
            dialog.show();
        }*/
        ButterKnife.bind(this);
        try{
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                CppUserLastParam userLastParam = CppUserLastParam.findById(CppUserLastParam.class,1);
                if(userLastParam!=null){
                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                    Log.e("DAty",userLastParam.getDateSync());
                    DateTime dt = formatter.parseDateTime(userLastParam.getDateSync());
                    int hours = Hours.hoursBetween(dt,new DateTime()).getHours();
                    if(hours<20){
                        //Log.e("device", android.os.Build.MODEL);;
                        //checkTournee();

                    }else{
                        //CppUserLastParam.deleteAll(CppUserLastParam.class);
                        ParseUser.logOutInBackground(new LogOutCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toasty.warning(ScannerNfcActivity.this, "Veuillez vous connecter d'abord!", Toast.LENGTH_SHORT, true).show();
                                Log.e("NULL", "ELAPSED TIME LOG OFF");
                                startActivity(new Intent(ScannerNfcActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                    }
                }else{

                    Toasty.warning(ScannerNfcActivity.this, "La synchronisation est requis avant de commencer!", Toast.LENGTH_SHORT, true).show();
                    startActivity(new Intent(this, CppSynActivity.class));
                    finish();
                }
            }else{
                Log.e("NULL", "PARSE USER");
                Toasty.warning(ScannerNfcActivity.this, "Veuillez vous connecter d'abord!", Toast.LENGTH_SHORT, true).show();
                startActivity(new Intent(ScannerNfcActivity.this, LoginActivity.class));
                finish();
            }

        }catch (NullPointerException e){

        }
        //closeNfc.set
    }


    @OnClick(R.id.closeNfc)
    public void close(){
       // startActivity(new Intent(this, CppMultipleScanActivity.class).putExtra(EXTRA_STATION_ID,Prefs.getString(STATION_ID_PREFS, CPP_TRANSPORT_DEFAULT_ARRET)));
        finish();
    }

    @OnClick(R.id.enableNFC)
    public void setEnableNFC(){
        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        startActivity(intent);
    }

    @OnClick(R.id.getCredit)
    public void getCredit(){
        isCredit=true;
        String nfc_please_tap_card = getResources().getString(R.string.nfc_please_tap_card);
        String nfc_keep_card_on_field = getResources().getString(R.string.nfc_keep_card_on_field);
        textInfoNfc.setText(nfc_please_tap_card+"\n"+nfc_keep_card_on_field);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(700); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        textInfoNfc.startAnimation(anim);
    }
    @Override
    public void onResume() {
        super.onResume();
        NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
           // NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this, PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0), null, null);
            setupForegroundDispatch(this,adapter);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                this.setTurnScreenOn(true);
            } else {
                final Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            }
            //Yes NFC available
        }else if(adapter != null && !adapter.isEnabled()){
            textInfoNfc.setText("MERCI D'ACTIVER LE SERVICE NFC!");
            enableNFC.setVisibility(View.VISIBLE);
            nfcAnimation.setAnimation(R.raw.enable_nfc);

            //NFC is not enabled.Need to enable by the user.
        }else{
            //NFC is not supported
            textInfoNfc.setText("CET APPAREIL NE PRENDS PAS EN CHARGE LE SERVICE NFC");
            //enableNFC.setVisibility(View.VISIBLE);
            nfcAnimation.setAnimation(R.raw.no_nfc);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
           // NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
            //Yes NFC available
            stopForegroundDispatch(this,adapter);
        }else if(adapter != null && !adapter.isEnabled()){

            //NFC is not enabled.Need to enable by the user.
        }else{
            //NFC is not supported
        }

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resolveIntent(intent);
    }

    /* access modifiers changed from: 0000 */
    public void resolveIntent(Intent intent) {
        String action = intent.getAction();
       // Log.e("action nfc ",action);
        if ("android.nfc.action.NDEF_DISCOVERED".equals(action) || "android.nfc.action.TAG_DISCOVERED".equals(action) || "android.nfc.action.TECH_DISCOVERED".equals(action)) {
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            new NdefReaderTask().execute(tag);
        }
    }

    /**
     * show dialog
     *
     */
    private void showDialog(String status) {
        CppUserLastParam userLastParam = CppUserLastParam.findById(CppUserLastParam.class,1);
        if(userLastParam!=null){

        }

    }



    private void dissmissDialog(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("advertiseFragment");
        if (prev != null) {
            ft.remove(prev);
            try{
                ft.commitAllowingStateLoss();
            }catch (IllegalStateException e){

            }
        }

    }
    public void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }
        Bundle options = new Bundle();
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 500);

        adapter.enableReaderMode(
                ScannerNfcActivity.this,
                new NfcAdapter.ReaderCallback() {
                    @Override
                    public void onTagDiscovered(final Tag tag) {

                        String[] techList = tag.getTechList();

                            String status = Prefs.getString(CPP_VEHICLE_STATUS,CPP_VEHICLE_EN_SERVICE);


                                    new NdefReaderTask().execute(tag);


                    }
                },
                NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                options
        );

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);

    }

    /**
     * @param activity The corresponding {@link } requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     *
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        public NdefReaderTask() {
        }

        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(Tag... params) {

            Tag tag = params[0];
            String str = null;

            // TODO Need to add library nfc later

            Target readTag = NfcTagController.readTag(tag, null);
            if (readTag == null) {
                str = null;
            } else {
                str = readTag.getSuid();
            }

            if (str == null || "".equals(str)) {

                Log.e("Error on card", "Erreur de lecture de la carte / du mobile. Merci de ré-essayer.");
            } else {
                Log.e("card uid", str.toUpperCase());

                try {
                   // need to crypt the card uid
                    tapCard(CppConstant.getCryptedText(str.toUpperCase()),dialog);
                    return str.toUpperCase();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }


            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toasty.error(ScannerNfcActivity.this, "Impossible de lire le contenu de la carte!.", Toast.LENGTH_SHORT, true).show();
                    }
                });

            }
        }
    }

    private void tapCard(String cardUID, ProgressDialog dialog){

        Log.e("cardUID",cardUID);
        return;

    }

    private void playBeepOk(){
        this.mediaplayer= MediaPlayer.create(this, (int) R.raw.ok_sound);
        this.mediaplayer.start();
    }
    private void playBeepNoCredit(){
        this.mediaplayer= MediaPlayer.create(this, (int) R.raw.beep_failed);
        this.mediaplayer.start();
    }
    private void playBeeNotFound(){
        this.mediaplayer= MediaPlayer.create(this, (int) R.raw.beep_not_found);
        this.mediaplayer.start();
    }
}
