/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.ui.sync;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.cppsystem.cppbus.CppApp;
import com.cppsystem.cppbus.R;

import com.cppsystem.cppbus.Secrets;
import com.cppsystem.cppbus.data.local.CppUserLastParam;
import com.cppsystem.cppbus.data.local.MessageEvent;

import com.cppsystem.cppbus.util.CppConstant;
import com.cppsystem.cppbus.util.TimeoutQuery;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static android.Manifest.permission.CALL_PHONE;
import static com.cppsystem.cppbus.util.CppConstant.*;


public class CppSynActivity extends AppCompatActivity  {


    @BindView(R.id.suivantSync)
    AppCompatButton suivant;
    @BindView(R.id.retrySync)
    AppCompatButton retrySync;
    @BindView(R.id.progressBarSync)
    ProgressBar progresSync;
    @BindView(R.id.infoSync)
    TextView infoSync;

    @BindView(R.id.textViewSyncCoop)
    TextView textViewSyncCoop;
    @BindView(R.id.showChoiceSync)
    LinearLayoutCompat showChoiceSync;
    @BindView(R.id.synCoopDataYes)
    AppCompatButton synCoopDataYes;
    @BindView(R.id.synCoopDataNo)
    AppCompatButton synCoopDataNo;

    @BindView(R.id.animationView)
    LottieAnimationView animationView;

    @BindView(R.id.vehicleInfo)
    TextView vehicleInfo;

    @BindView(R.id.tpeInfo)
    TextView tpeInfo;

    @BindView(R.id.ligneInfo)
    TextView ligneInfo;



    private String vehicleId;
    private String ligneVarianceId;
    private ParseObject ligneVariance;
    private String ligneId;
    private String cooperativeId;
    private String transportType;
    private boolean retried,syncUserOnly,contactSupport;
    private TimeoutQuery<ParseObject> mTimeoutQuery;
    CppUserLastParam userLastParam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_sync_user_data);
        ButterKnife.bind(this);
        syncUserOnly = getIntent().getBooleanExtra(EXTRA_SYNC_USER_ONLY,false);
      //  getAllMemberList();
        progresSync.setVisibility(View.INVISIBLE);
        showChoiceSync.setVisibility(View.VISIBLE);
        textViewSyncCoop.setVisibility(View.VISIBLE);

    }


    @OnClick(R.id.suivantSync)
    public void nextSync() {
        userLastParam = CppUserLastParam.findById(CppUserLastParam.class,1);
        if(userLastParam!=null){
            userLastParam.setCooperative(cooperativeId);
            userLastParam.setLigne(ligneId);
            userLastParam.setLigneVariance(ligneVarianceId);
            userLastParam.setVehicule(vehicleId);
            userLastParam.setUserVehicleList(vehicleId);
            userLastParam.setTransportType(transportType);
            userLastParam.setDateSync(CppConstant.getDateTimeNowTourneeFormat());
            ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CPP_CLASS_LIGNE_VARIANCE);
            query.fromLocalDatastore();
            ParseQuery<ParseObject> queryVehicle = ParseQuery.getQuery(PARSE_CPP_CLASS_VEHICLE);
            queryVehicle.fromLocalDatastore();
            try {
                ParseObject vehicle = queryVehicle.get(vehicleId);
                String type  = vehicle.getString("type");
                // need to get vehicle type
                String tarifs = query.get(ligneVarianceId).getString("tarifs");
                Log.e("tarifs",tarifs);
                JSONArray jarray = new JSONArray(tarifs);
                JSONObject obj = jarray.getJSONObject(0);
                Log.e("tarifs2",obj.toString());
                JSONObject currentTarif=  obj.getJSONObject("tarifs").getJSONObject(type);
                userLastParam.setDefaultPrice(Integer.valueOf(currentTarif.getString("normal")));
                userLastParam.setPriceMin(Integer.valueOf(currentTarif.getString("min")));
                userLastParam.setPriceMax(Integer.valueOf(currentTarif.getString("max")));

                //
                //userLastParam.setPriceMin((Integer) query.get(ligneVarianceId).getNumber("priceMin"));
            } catch (ParseException | JSONException e) {
                e.printStackTrace();
            }
            //userLastParam.setDefaultPrice();
            Log.e("VehicleId 1",vehicleId+" "+userLastParam.getVehicule());
            // Associate the device with a user
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("tpe",CppConstant.getPhoneImei(this));
            installation.saveInBackground();
            userLastParam.update();

        }else{
            userLastParam = new CppUserLastParam();
            userLastParam.setCooperative(cooperativeId);
            userLastParam.setLigne(ligneId);
            userLastParam.setLigneVariance(ligneVarianceId);
            userLastParam.setVehicule(vehicleId);
            userLastParam.setUserVehicleList(vehicleId);
            userLastParam.setTransportType(transportType);
            userLastParam.setDateSync(CppConstant.getDateTimeNowTourneeFormat());
            ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CPP_CLASS_LIGNE_VARIANCE);
            query.fromLocalDatastore();
            ParseQuery<ParseObject> queryVehicle = ParseQuery.getQuery(PARSE_CPP_CLASS_VEHICLE);
            queryVehicle.fromLocalDatastore();
            try {
                ParseObject vehicle = queryVehicle.get(vehicleId);
                String type  = vehicle.getString("type");
                // need to get vehicle type
                String tarifs = query.get(ligneVarianceId).getString("tarifs");
                Log.e("tarifs",tarifs);
                JSONArray jarray = new JSONArray(tarifs);
                JSONObject obj = jarray.getJSONObject(0);
                Log.e("tarifs2",obj.toString());
                JSONObject currentTarif=  obj.getJSONObject("tarifs").getJSONObject(type);
                userLastParam.setDefaultPrice(Integer.valueOf(currentTarif.getString("normal")));
                userLastParam.setPriceMin(Integer.valueOf(currentTarif.getString("min")));
                userLastParam.setPriceMax(Integer.valueOf(currentTarif.getString("max")));
            } catch (ParseException | JSONException e) {
                e.printStackTrace();
            }
            Log.e("VehicleId ",vehicleId+" "+userLastParam.getVehicule());
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("tpe",CppConstant.getPhoneImei(this));
            installation.saveInBackground();
            userLastParam.save();

        }
        if(!syncUserOnly){
         /*   startActivity(new Intent(this, CppChooseCoopActivity.class)
                    .putExtra(EXTRA_COOP_ID, cooperativeId)
                    .putExtra(EXTRA_LIGNE_ID, ligneId)
                    .putExtra(EXTRA_LIGNE_VAR_ID, ligneVarianceId)
                    .putExtra(EXTRA_VEHICLE_ID, vehicleId)
                    .putExtra(EXTRA_TRANS_TYPE_ID, transportType)

            );*/
        }
        finish();
    }

    @OnClick(R.id.retrySync)
    public void retrySync() {
        progresSync.setVisibility(View.VISIBLE);
        infoSync.setText("En cours ...");
        infoSync.setTextSize(14);
        infoSync.setTextColor(getResources().getColor(R.color.black));
        retrySync.setVisibility(View.INVISIBLE);
        //getAllMemberList();
        syncCoopDataYes();

    }


    @OnClick(R.id.synCoopDataYes)
    public void syncCoopDataYes(){
        progresSync.setVisibility(View.VISIBLE);
        infoSync.setText("En cours ...");
        infoSync.setTextSize(14);
        infoSync.setTextColor(getResources().getColor(R.color.black));
        retrySync.setVisibility(View.INVISIBLE);

        showChoiceSync.setVisibility(View.INVISIBLE);
        textViewSyncCoop.setVisibility(View.INVISIBLE);

        //if( CppApp.getInstance().allMemberDone){
            if(!CppApp.getInstance().userVehicleDone)
                getTpeVehicle();
            else if(!CppApp.getInstance().userLigneDone)
                getUserLigne(ligneVarianceId);
            else if(!CppApp.getInstance().userCooperativeDone)
                getUserCooperative(ligneId);
            else  if(!CppApp.getInstance().userTransportTypeDone)
                getUserTransportType(cooperativeId);
            else if(!CppApp.getInstance().listAllVarianceDone)
                getListAllVariance();
       // }
    }
    @OnClick(R.id.synCoopDataNo)
    public void syncCoopDataNo(){
        if(contactSupport){
            BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this)
                    .setTitle("Support!")
                    .setAnimation(R.raw.info)
                    .setMessage("Appeler le support CPP sur 0329973482, en précisant l'erreur et l'IMEI :"+CppConstant.getPhoneImei(this))//TODO need to change support cpp number
                    .setCancelable(false)
                    .setPositiveButton("APPELER",  new BottomSheetMaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:0329973482"));
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                startActivity(intent);
                            } else {
                                ActivityCompat.requestPermissions(CppSynActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        1);
                            }
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("fermer", R.drawable.baseline_close_white_24dp, new BottomSheetMaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                            dialogInterface.dismiss();
                        }


                    })
                    .build();

            // Show Dialog
            LottieAnimationView animationView = mBottomSheetDialog.getAnimationView();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            animationView.setLayoutParams(params );
            mBottomSheetDialog.show();
        }else{
            userLastParam = CppUserLastParam.findById(CppUserLastParam.class,1);
            if(userLastParam!=null){
                userLastParam.setDateSync(CppConstant.getDateTimeNowTourneeFormat());
                userLastParam.save();
                /*startActivity(new Intent(this, CppChooseCoopActivity.class)
                        .putExtra(EXTRA_COOP_ID, userLastParam.getCooperative())
                        .putExtra(EXTRA_LIGNE_ID, userLastParam.getLigne())
                        .putExtra(EXTRA_LIGNE_VAR_ID, userLastParam.getLigneVariance())
                        .putExtra(EXTRA_VEHICLE_ID, userLastParam.getVehicule())
                        .putExtra(EXTRA_TRANS_TYPE_ID, userLastParam.getTransportType())
                );*/
                finish();
            }else{
                BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this)
                        .setTitle("Attention!")
                        .setAnimation(R.raw.warning_or_alert)
                        .setMessage("Vous n'avez pas les données des coopératives disponible sur l'application,\n vous devez synchroniser ces données avant utilisation!")
                        .setCancelable(false)
                        .setPositiveButton("Synchroniser",  new BottomSheetMaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                synCoopDataYes.performClick();
                                dialogInterface.dismiss();
                            }


                        })
                        .setNegativeButton("fermer", R.drawable.baseline_close_white_24dp, new BottomSheetMaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }


                        })
                        .build();

                // Show Dialog
                LottieAnimationView animationView = mBottomSheetDialog.getAnimationView();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                animationView.setLayoutParams(params );
                mBottomSheetDialog.show();
        }



            //Toasty.error(CppApp.getInstance().getApplicationContext(), "Veuillez Synchroniser les données de votre coopérative d'abord! ", Toast.LENGTH_SHORT, true).show();
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(MessageEvent event) {
        if(event.timeout){

                textViewSyncCoop.setVisibility(View.VISIBLE);
                progresSync.setVisibility(View.INVISIBLE);
                infoSync.setText("Serveur innacessible, verifier votre connection internet!");
                infoSync.setTextSize(14);
                infoSync.setTextColor(getResources().getColor(R.color.cpp_red));
                retrySync.setVisibility(View.INVISIBLE);
                showChoiceSync.setVisibility(View.VISIBLE);
                synCoopDataYes.setText("REESSAYER");
                contactSupport =true;
                synCoopDataNo.setText("Contacter CPP");



        }

        if (event.vehiculeId != null && event.varianceId != null) {
            //  getLigne(event.varianceId);
            if(event.vehiculeId.equals(NO_VEHICLE)||event.varianceId.equals(NO_VARIANCE)){
                // no vehicle map to user
                // display TPE IMEI and DISPLAY VEHICLE ON TPE
                String imei = CppConstant.getPhoneImei(this);
                tpeInfo.setText("IMEI "+imei);
                tpeInfo.setVisibility(View.VISIBLE);
                textViewSyncCoop.setVisibility(View.VISIBLE);
                progresSync.setVisibility(View.INVISIBLE);
                infoSync.setText("Attention, vous n'avez pas accèss a ce TPE, veuillez contacter le support CPP et réessayer!");
                infoSync.setTextSize(14);
                infoSync.setTextColor(getResources().getColor(R.color.cpp_red));
                retrySync.setVisibility(View.INVISIBLE);
                showChoiceSync.setVisibility(View.VISIBLE);
               // animationView.setImageAssetsFolder("images");
                animationView.setAnimation(R.raw.sad_face);
                animationView.playAnimation();
                animationView.setVisibility(View.VISIBLE);
                synCoopDataYes.setText("REESSAYER");
                contactSupport =true;
                synCoopDataNo.setText("Contacter CPP");
                textViewSyncCoop.setVisibility(View.INVISIBLE);
            }else{
                tpeInfo.setVisibility(View.INVISIBLE);
                CppApp.getInstance().userVehicleDone=true;
                vehicleId = event.vehiculeId;
                ligneVarianceId = event.varianceId;
                getUserLigne(ligneVarianceId);
                animationView.setAnimation(R.raw.done);
                animationView.playAnimation();
                Log.e("Vehicle","done");

            }

        }
        if (event.ligneId != null) {
            ligneId = event.ligneId;
            CppApp.getInstance().userLigneDone=true;
            Log.e("Ligne","done");
            // get all variance in ligne
            getUserLigneVariance(ligneId);


        }
        if (event.cooperativeId != null) {
            CppApp.getInstance().userCooperativeDone=true;
            cooperativeId = event.cooperativeId;
            getUserTransportType(cooperativeId);
            Log.e("Coope","done");

        }
        if (event.transportTypeId != null) {
            CppApp.getInstance().userTransportTypeDone=true;
            transportType = event.transportTypeId;
            // get List arret
            getListArret(ligneVariance,false);
            Log.e("TransportId","done");

        }
        if (event.fullDone == 3){
           // get user coopérative
            getUserCooperative(ligneId);

        }
        if (event.fullDone == 4){
           // get param per arret
            getParamStation(ligneVariance,false);
            Log.e("Arret","done");
        }

        if (event.fullDone == 5){
            Log.e("Param station","done");
            CppApp.getInstance().listAllVarianceDone=true;
            getListAllVariance();

        }
        if (event.fullDone == 6) {
            animationView.setVisibility(View.VISIBLE);
            progresSync.setVisibility(View.INVISIBLE);
            infoSync.setVisibility(View.VISIBLE);
            infoSync.setText("Terminé!");
            infoSync.setTextColor(getResources().getColor(R.color.call));
            infoSync.setTextSize(13);
            suivant.setVisibility(View.VISIBLE);
            retrySync.setVisibility(View.INVISIBLE);
            showChoiceSync.setVisibility(View.INVISIBLE);
            textViewSyncCoop.setVisibility(View.INVISIBLE);
            try {
                ParseObject vehicle= ParseQuery.getQuery(PARSE_CPP_CLASS_VEHICLE).fromLocalDatastore().get(vehicleId);
                ParseObject coope= ParseQuery.getQuery(PARSE_CPP_CLASS_COOPERATIVE).fromLocalDatastore().get(cooperativeId);
                ParseObject ligne= ParseQuery.getQuery(PARSE_CPP_CLASS_LIGNE).fromLocalDatastore().get(ligneId);
                vehicleInfo.setText(vehicle.getString("numero"));
                vehicleInfo.setVisibility(View.VISIBLE);
                ligneInfo.setText(coope.getString("name")+" "+ligne.getString("ligne"));
                ligneInfo.setVisibility(View.VISIBLE);
                Log.e("FUll","done");
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void onStart() {
        //Log.e(TAG, "onStart");
        EventBus.getDefault().register(this);
        super.onStart();
    }
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Override
    public void onResume() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onResume();
    }
    @Override
    public void onPause() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onPause();
    }

/*
    public static ParseQuery<ParseObject> generatePaginationQuery(int page, int displayLimit, int offset) throws Exception {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CppFidMember");
       *//* String crypto = getCryptedText("1");
        query.whereEqualTo("actif",crypto);*//*
        query.selectKeys(Arrays.asList("mobile", "cardStatus","name","firstname","cumulCredit","nbCredit","actif","uid","cardnumber","image","nbPoint","birthday"));
        query.addDescendingOrder("createdAt");
        query.setLimit(offset);
        if (page > 1) {
            query.setSkip((offset * (page - 1)));
        }
        return query;
    }*/


/*
    private void getAllMemberList() {
        if (isConnected()) {
            ParseQuery<ParseObject> countQuery = ParseQuery.getQuery(PARSE_CPP_CLASS_FIDMEMBER_COUNT);
            //countQuery.whereEqualTo("actif", "1");
            ArrayList<ParseObject> cppFidMemberList = new ArrayList<>();
            // TODO add object to count member in dashboard and query on the last object in this class
            mTimeoutQuery = new TimeoutQuery<>(countQuery, DEFAULT_TIMEOUT_QUERY);
            mTimeoutQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if(e==null){
                        int count = object.getInt("count");
                        // The count request succeeded. Log the count
                        int page = 1;
                        int offset = DEFAUlT_MAX_QUERY_LIMIT;
                        int limit = page * offset;
                        int loops = (count - (count % limit)) / offset;
                        for (; page <= loops + 1; page++) {
                            limit = page * offset;
                            ParseQuery<ParseObject> query = null;
                            try {
                                query = generatePaginationQuery(page, limit, offset);
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));
                                return;

                            }
                            int finalPage = page;
                            int finalLimit = limit;
                            int finalOffset = offset;
                            new TimeoutQuery<>(query, BIG_TIMEOUT_QUERY).findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {

                                        cppFidMemberList.addAll(objects);
                                        ParseObject.unpinAllInBackground(OFFLINE_CPP_FID_MEMBER, new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e != null) {
                                                    // There was some error.
                                                    if(e.getCode()==2021)
                                                        ParseObject.pinAllInBackground(OFFLINE_CPP_FID_MEMBER, cppFidMemberList);
                                                    return;
                                                }
                                                // Add the latest results for this query to the cache.
                                                ParseObject.pinAllInBackground(OFFLINE_CPP_FID_MEMBER, cppFidMemberList);
                                            }
                                        });
                                         Log.e("SIZE FULL ARRAY", cppFidMemberList.size() +" COUNT  " + count);
                                        if (cppFidMemberList.size() == count) {
                                            EventBus.getDefault().postSticky(new MessageEvent(true, null, null, null, null, null, 0,false));
                                        }
                                    } else {
                                        // The request failed
                                        if (e.getCode() == ParseException.TIMEOUT) {
                                            EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));
                                        }else{
                                            Log.e("Error",e.getMessage(),e);
                                        }
                                    }

                                }
                            });
                        }

                    }else {
                        if (e.getCode() == ParseException.TIMEOUT) {
                            EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));
                        }else{
                            Log.e("Error",e.getMessage(),e);

                        }


                    }
                }
            });
        } else {
            Toasty.error(CppApp.getInstance().getApplicationContext(), "La synchronisation journalière des données utilisateurs nécessite une connection internet!", Toast.LENGTH_SHORT, true).show();
            progresSync.setVisibility(View.INVISIBLE);
            infoSync.setText("Echoué!\nLa synchronisation journalière des données utilisateurs nécessite une connection internet!");
            infoSync.setTextSize(11);
            infoSync.setTextColor(getResources().getColor(R.color.cpp_red));
            retrySync.setVisibility(View.INVISIBLE);
            //suivant.setText("REESSAYER");

        }
    }
*/

    private void getListArret(ParseObject ligneVariance,boolean backgroundOnly) {
    /*    ParseQuery<ParseObject> queryTrans = ParseQuery.getQuery(PARSE_CPP_CLASS_LIGNE_VARIANCE);
        queryTrans.whereEqualTo("objectId", ligneVarianceId);*/
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CPP_CLASS_ARRET);
        query.whereEqualTo("ligne", ligneVariance);
       // query.include(PARSE_CPP_CLASS_LIGNE_VARIANCE);
        new TimeoutQuery<ParseObject>(query, DEFAULT_TIMEOUT_QUERY).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    //EventBus.getDefault().post(new MessageEvent(false,null,objects.get(0).getObjectId(),null,null));
                    ParseObject.unpinAllInBackground(OFFLINE_CPP_ARRET, objects, new DeleteCallback() {
                        public void done(ParseException e) {
                            if (e != null) {
                                // There was some error.
                                if(e.getCode()==2021)
                                    ParseObject.pinAllInBackground(OFFLINE_CPP_ARRET, objects);
                                return;
                            }
                            // Add the latest results for this query to the cache.
                            ParseObject.pinAllInBackground(OFFLINE_CPP_ARRET, objects);
                        }
                    });
                    if(!backgroundOnly)EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 4,false));

                }else{
                    if (e.getCode() == ParseException.TIMEOUT) {

                        if(!backgroundOnly) EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));
                    }else{
                        if(!backgroundOnly) EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));

                    }
                }

            }
        });

    }
    private void getParamStation(ParseObject ligneVariance,boolean backgroundOnly){
        /*ParseQuery<ParseObject> queryTrans = ParseQuery.getQuery(PARSE_CPP_CLASS_LIGNE_VARIANCE);
        queryTrans.whereEqualTo("objectId", ligneVarianceId);*/
        List<ParseObject> dataParam = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CPP_CLASS_PARAM_STATION);
        query.include("station");
        query.whereEqualTo("ligne", ligneVariance);
            new TimeoutQuery<ParseObject>(query, DEFAULT_TIMEOUT_QUERY).findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        //EventBus.getDefault().post(new MessageEvent(false,null,objects.get(0).getObjectId(),null,null));
                        ParseObject.unpinAllInBackground(OFFLINE_CPP_PARAM_STATION, objects, new DeleteCallback() {
                            public void done(ParseException e) {
                                if (e != null) {
                                    // There was some error.
                                    if(e.getCode()==2021)
                                        ParseObject.pinAllInBackground(OFFLINE_CPP_PARAM_STATION, objects);
                                    return;
                                }
                                // Add the latest results for this query to the cache.
                                ParseObject.pinAllInBackground(OFFLINE_CPP_PARAM_STATION, objects);
                            }
                        });
                       // EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 5,false));
                        if(!backgroundOnly)EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 5,false));

                    }else{
                        if (e.getCode() == ParseException.TIMEOUT) {
                            if(!backgroundOnly) EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));
                        }else{
                            if(!backgroundOnly) EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));

                        }
                    }

                }
            });

    }

    private void getListAllVariance() {
        ParseQuery<ParseObject> queryTrans = ParseQuery.getQuery(PARSE_CPP_CLASS_VARIANCE);
        new TimeoutQuery<ParseObject>(queryTrans, DEFAULT_TIMEOUT_QUERY).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    //EventBus.getDefault().post(new MessageEvent(false,null,objects.get(0).getObjectId(),null,null));
                    ParseObject.unpinAllInBackground(OFFLINE_CPP_VARIANCE, objects, new DeleteCallback() {
                        public void done(ParseException e) {
                            if (e != null) {
                                // There was some error.
                                if(e.getCode()==2021)
                                    ParseObject.pinAllInBackground(OFFLINE_CPP_VARIANCE, objects, new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null)
                                                EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 6,false));
                                        }
                                    });
                                return;
                            }
                            // Add the latest results for this query to the cache.
                            ParseObject.pinAllInBackground(OFFLINE_CPP_VARIANCE, objects, new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null)
                                        EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 6,false));
                                }
                            });
                        }
                    });

                }else{
                    if (e.getCode() == ParseException.TIMEOUT) {

                        EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));
                    }
                }

            }
        });
    }
    private void getTpeVehicle(){
        String imei = CppConstant.getPhoneImei(this);
        ParseQuery<ParseObject> queryTpe = ParseQuery.getQuery(PARSE_CPP_CLASS_TRANSPORT_VEHICLE);
        queryTpe.whereEqualTo("IMEI",imei);
        queryTpe.include("ligneVariance");
        queryTpe.setLimit(1);
        new TimeoutQuery<ParseObject>(queryTpe, DEFAULT_TIMEOUT_QUERY).getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject vehicle, ParseException e) {
                if (e == null) {
                    vehicle.pinInBackground();
                    // get user vehicle
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CPP_CLASS_VEHICLE_USER);
                    query.whereEqualTo("vehicleId",vehicle);
                    query.include("userId");
                    new TimeoutQuery<ParseObject>(query,DEFAULT_TIMEOUT_QUERY).findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> vechileUsers, ParseException e) {
                            // Add the latest results for this query to the cache.
                            //Log.e("Vehicle user ","list "+vechileUsers.size());
                            if(e==null){
                                if(vechileUsers.size()>0)
                                    ParseObject.unpinAllInBackground(OFFLINE_CPP_VEHICLE_USER, vechileUsers, new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if(e!=null){
                                                if(e.getCode()==2021)
                                                    ParseObject.pinAllInBackground(OFFLINE_CPP_VEHICLE_USER, vechileUsers, new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e == null){
                                                                //EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 5, false));
                                                                EventBus.getDefault().postSticky(new MessageEvent(false,
                                                                        vehicle.getParseObject("ligneVariance").getObjectId(),
                                                                        vehicle.getObjectId(),
                                                                        null, null, null, 0,false));
                                                                ligneVariance=vechileUsers.get(0).getParseObject("ligneVariance"); // normally the vehicle belong to single variance
                                                            }


                                                        }
                                                    });
                                                return;
                                            }
                                            ParseObject.pinAllInBackground(OFFLINE_CPP_VEHICLE_USER, vechileUsers, new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null){
                                                        EventBus.getDefault().postSticky(new MessageEvent(false,
                                                                vehicle.getParseObject("ligneVariance").getObjectId(),
                                                                vehicle.getObjectId(),
                                                                null, null, null, 0,false));
                                                        ligneVariance=vechileUsers.get(0).getParseObject("ligneVariance"); // normally the vehicle belong to single variance
                                                    }


                                                }
                                            });
                                        }
                                    });
                            }
                            else{
                                if (e.getCode() == ParseException.TIMEOUT) {
                                    EventBus.getDefault().postSticky(new MessageEvent(false,
                                            NO_VARIANCE,
                                            NO_VEHICLE,
                                            null, null, null, 0,false));
                                }else{
                                    EventBus.getDefault().postSticky(new MessageEvent(false,
                                            NO_VARIANCE,
                                            NO_VEHICLE,
                                            null, null, null, 0,false));
                                }
                            }

                        }
                    });

                }else{
                    if (e.getCode() == ParseException.TIMEOUT) {

                        EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));
                    }else if(e.getCode() == ParseException.OBJECT_NOT_FOUND){
                        EventBus.getDefault().postSticky(new MessageEvent(false, NO_VARIANCE, NO_VEHICLE, null, null, null, 0,false));

                    }
                }
            }
        });
    }
    private void getUserCooperative(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CPP_CLASS_LIGNE);
        query.whereEqualTo("objectId", objectId);
        query.include("cooperative");
        query.setLimit(1);
        new TimeoutQuery<ParseObject>(query,DEFAULT_TIMEOUT_QUERY).getFirstInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.pinInBackground();
                    object.getParseObject("cooperative").pinInBackground();
                    EventBus.getDefault().postSticky(new MessageEvent(false, null, null, object.getParseObject("cooperative").getObjectId(), null, null, 0,false));

                }else{
                    if (e.getCode() == ParseException.TIMEOUT) {
                        EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));
                    }
                }
            }
        });


        ParseQuery<ParseObject> queryIncidenType = ParseQuery.getQuery(PARSE_CPP_CLASS_INCIDENT_TYPE);
        new TimeoutQuery<ParseObject>(queryIncidenType, DEFAULT_TIMEOUT_QUERY).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    //EventBus.getDefault().post(new MessageEvent(false,null,objects.get(0).getObjectId(),null,null));
                    ParseObject.unpinAllInBackground(OFFLINE_CPP_INCIDENT_TYPE, objects, new DeleteCallback() {
                        public void done(ParseException e) {
                            if (e != null) {
                                // There was some error.
                                if(e.getCode()==2021)
                                    ParseObject.pinAllInBackground(OFFLINE_CPP_INCIDENT_TYPE, objects, new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                        }
                                    });
                                return;
                            }
                            // Add the latest results for this query to the cache.
                            ParseObject.pinAllInBackground(OFFLINE_CPP_INCIDENT_TYPE, objects, new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                }
                            });
                        }
                    });


                }

            }
        });
    }

    private void getUserLigne(String objectId) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CPP_CLASS_LIGNE_VARIANCE);
       // query.whereEqualTo("objectId", objectId);
        query.include("ligne");
        query.setLimit(1);
        //query.getI
        new TimeoutQuery<ParseObject>(query, DEFAULT_TIMEOUT_QUERY).getFirstInBackground(objectId,new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.pinInBackground();
                    EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, object.getParseObject("ligne").getObjectId(), null, 0,false));


                }else{
                    if (e.getCode() == ParseException.TIMEOUT) {
                        EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));
                    }else{
                        EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));
                    }
                }
            }
        });


    }
    private void getUserLigneVariance(String objectId) {
        ParseQuery<ParseObject> queryTrans = ParseQuery.getQuery(PARSE_CPP_CLASS_LIGNE);
        queryTrans.whereEqualTo("objectId", objectId);
        ParseQuery<ParseObject> queryVariance = ParseQuery.getQuery(PARSE_CPP_CLASS_LIGNE_VARIANCE);
        queryVariance.whereMatchesQuery("ligne", queryTrans);
        queryVariance.include("variance");
        queryVariance.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    //EventBus.getDefault().post(new MessageEvent(false,null,objects.get(0).getObjectId(),null,null));
                    ParseObject.unpinAllInBackground(OFFLINE_CPP_LIGNE_VARIANCE, objects, new DeleteCallback() {
                        public void done(ParseException e) {
                            if (e != null) {
                                // There was some error.
                                if(e.getCode()==2021)
                                    ParseObject.pinAllInBackground(OFFLINE_CPP_LIGNE_VARIANCE, objects, new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                        }
                                    });
                            }
                            // Add the latest results for this query to the cache.
                            ParseObject.pinAllInBackground(OFFLINE_CPP_LIGNE_VARIANCE, objects, new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                }
                            });
                        }
                    });
                    for(int i=0;i<objects.size();i++){
                        Log.e("User ligne variance",objects.get(i).getObjectId());
                        getListArret(objects.get(i),true);
                        getParamStation(objects.get(i),true);
                    }
                    EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 3,false));

                }else{
                    if (e.getCode() == ParseException.TIMEOUT) {
                        EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));
                    }else{
                        EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));
                    }
                    e.printStackTrace();
                }
            }
        });

    }

    private void getUserTransportType(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(PARSE_CPP_CLASS_COOPERATIVE);
        query.whereEqualTo("objectId", objectId);
        query.include("type");
        query.setLimit(1);
        new TimeoutQuery<ParseObject>(query, DEFAULT_TIMEOUT_QUERY).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    objects.get(0).pinInBackground();
                    EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, objects.get(0).getParseObject("type").getObjectId(), 1,false));


                }else{
                    if (e.getCode() == ParseException.TIMEOUT) {

                        EventBus.getDefault().postSticky(new MessageEvent(false, null, null, null, null, null, 0,true));
                    }
                }

            }
        });
    }
}
