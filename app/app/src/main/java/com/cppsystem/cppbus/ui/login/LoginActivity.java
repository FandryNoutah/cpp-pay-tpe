/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.ui.login;

import android.Manifest;
import android.annotation.TargetApi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cppsystem.cppbus.CppApp;

import com.cppsystem.cppbus.R;
import com.cppsystem.cppbus.data.local.CppUserLastParam;
import com.cppsystem.cppbus.data.local.LoginCredentials;

import com.cppsystem.cppbus.ui.sync.CppSynActivity;
import com.cppsystem.cppbus.util.CppConstant;
import com.cppsystem.cppbus.util.TimeoutParseUser;
import com.jaredrummler.android.device.DeviceName;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.pixplicity.easyprefs.library.Prefs;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static com.cppsystem.cppbus.CppApp.SERVER_HEALTH_CHECK;
import static com.cppsystem.cppbus.util.CppConstant.CPP_USER_TYPE_CONTROLLEUR;
import static com.cppsystem.cppbus.util.CppConstant.SCREEN_SAVER_PREFS;
import static com.cppsystem.cppbus.util.CppConstant.getDateTimeNowTourneeFormat;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.usernameLoginEdtx) EditText usernameLoginEdtx;
    @BindView(R.id.passwordLoginText) EditText passwordLoginText;
    @BindView(R.id.login)
    AppCompatButton login;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    private OnLoginFinishListener mCallback;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // mCallback = (OnLoginFinishListener) this;

       /* permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }*/
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.NFC,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION,
                        Manifest.permission.VIBRATE,
                        READ_PHONE_STATE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                if(report.areAllPermissionsGranted()){


                    try{
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        if (currentUser != null) {
                            // start activity sync
                            //Log.e("DEVICE NAME", DeviceName.getDeviceName());
                            CppUserLastParam userLastParam = CppUserLastParam.findById(CppUserLastParam.class,1);
                            if(userLastParam!=null){
                                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                DateTime dt = formatter.parseDateTime(userLastParam.getDateSync());
                                int hours = Hours.hoursBetween(dt,new DateTime()).getHours();
                                if(hours<20){


                                }else{
                                    //CppUserLastParam.deleteAll(CppUserLastParam.class);

                                    ParseUser.logOutInBackground(new LogOutCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                        }
                                    });

                                    LoginCredentials credentials = LoginCredentials.findById(LoginCredentials.class,1);
                                    if(credentials!=null){
                                        usernameLoginEdtx.setText(credentials.getLogin());
                                        passwordLoginText.setText(credentials.getPassword());
                                    }
                                }
                            }else{



                                finish();
                            }
                        }
                    }catch (NullPointerException e){

                    }

                }else{
                    BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(LoginActivity.this)
                            .setTitle("Attention!")
                            .setAnimation(R.raw.warning_or_alert)
                            .setMessage("Toutes ces autorisations sont requis par l'application, merci d'accorder svp!")
                            .setCancelable(false)
                            .setPositiveButton("ACCORDER", R.drawable.baseline_sync_white_24dp, new BottomSheetMaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {

                                    dialogInterface.dismiss();
                                }


                            })
                            .setNegativeButton("FERMER", R.drawable.baseline_close_white_24dp, new BottomSheetMaterialDialog.OnClickListener() {
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
               }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }
    public void checkServerHealth() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(SERVER_HEALTH_CHECK)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error server ",e.getMessage(),e);
            }
            @Override
            public void onResponse(Call call, Response response) {
                try {
                    Log.e("Ok server ",response.message()+" "+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }
    @OnClick(R.id.login)
    public void login(){
        if(!usernameLoginEdtx.getText().toString().isEmpty()&&!passwordLoginText.getText().toString().isEmpty()){
            //startActivity(new Intent(LoginActivity.this,MainActivity.class).putExtra(UtilsType.SESSION,"sdfjklh"));

                login.setEnabled(false);
               // layout.startLoading();
                showDialog();
               // checkServerHealth();
                try{
                    new TimeoutParseUser(CppConstant.BIG_TIMEOUT_QUERY)
                            .logInInBackground(usernameLoginEdtx.getText().toString(),
                                    passwordLoginText.getText().toString(), new LogInCallback() {
                                        @Override
                                        public void done(ParseUser user, ParseException e) {
                                            dissmissDialog();
                                            login.setEnabled(true);
                                            if(e==null&& user != null) {
                                        //TODO for TPE LASER
                                               /* if (DeviceName.getDeviceName().equals(CppConstant.PHONE_LASER_TPE)) {
                                                    Intent newIntent = new Intent(LoginActivity.this, ScanService.class);
                                                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startService(newIntent);
                                                }*/
                                                Prefs.putBoolean(SCREEN_SAVER_PREFS,true);

                                                // check if the connected user  is controller or driver
                                                if(user.getString("type").equals(CPP_USER_TYPE_CONTROLLEUR)){
                                                   // startActivity(new Intent(LoginActivity.this, ControlSyncActivity.class));
                                                }else{
                                                    startActivity(new Intent(LoginActivity.this, CppSynActivity.class));
                                                    LoginCredentials credentials = LoginCredentials.findById(LoginCredentials.class,1);
                                                    if(credentials!=null){
                                                        credentials.setLogin(usernameLoginEdtx.getText().toString());
                                                        credentials.setPassword(passwordLoginText.getText().toString());
                                                        credentials.setDate(getDateTimeNowTourneeFormat());
                                                        credentials.update();
                                                    }else{
                                                        credentials = new LoginCredentials();
                                                        credentials.setLogin(usernameLoginEdtx.getText().toString());
                                                        credentials.setPassword(passwordLoginText.getText().toString());
                                                        credentials.setDate(getDateTimeNowTourneeFormat());
                                                        credentials.save();
                                                    }
                                                }

                                                finish();

                                            }else {
                                                e.printStackTrace();
                                                Log.e("ERROR",e.getMessage()+" "+e.getCode());
                                                if(e.getCode()==ParseException.TIMEOUT){
                                                    BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(LoginActivity.this)
                                                            .setTitle("Attention!")
                                                            .setAnimation(R.raw.no_connection)
                                                            .setMessage("Serveur indisponible, veuillez réessayer plutard!")
                                                            .setCancelable(false)
                                                            .setPositiveButton("Réessayer",  new BottomSheetMaterialDialog.OnClickListener() {
                                                                @Override
                                                                public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                                                    login.performClick();
                                                                    dialogInterface.dismiss();
                                                                }

                                                            })
                                                            .setNegativeButton("Cancel", R.drawable.baseline_close_white_24dp, new BottomSheetMaterialDialog.OnClickListener() {
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
                                                else if(e.getCode()==ParseException.OBJECT_NOT_FOUND && e.getMessage().contains("Invalid username/password.")){
                                                    Toasty.error(CppApp.getInstance().getApplicationContext(), "Les informations fournis sont incorrect! ", Toast.LENGTH_LONG, true).show();
                                                }else if(e.getCode()==ParseException.OBJECT_NOT_FOUND && e.getMessage().contains("failed login attempts")){
                                                    Toasty.error(CppApp.getInstance().getApplicationContext(), "Votre compte est temporairement verrouillé en raison de plusieurs tentatives de connexion infructueuses. Veuillez réessayer après 10 minute (s)", Toast.LENGTH_LONG, true).show();
                                                }
                                                else{
                                                    Toasty.error(CppApp.getInstance().getApplicationContext(), "Une erreur inconnu est survenu, veuillez réessayer dans quelques instants! ", Toast.LENGTH_LONG, true).show();
                                                }

                                            }

                                        }
                                    });
                }catch (NullPointerException ex){
                    login.setEnabled(true);
                    /*try {
                        Thread.sleep(5000);

                    } catch (InterruptedException exc) {

                    }*/
                    dissmissDialog();
                    CppApp.getInstance().onCreate();
                    finish();
                    startActivity(getIntent());

                }




        }else{
            Toasty.error(CppApp.getInstance().getApplicationContext(), "Veuillez renseigner les informations svp!", Toast.LENGTH_SHORT, true).show();

        }
        //throw new RuntimeException("This is a crash");
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    /**
     * show dialog
     *
     */
    private void showDialog() {
        LoadingFragment progress= LoadingFragment.newInstance("LOADING");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        progress.show(ft, "dialog");

    }

    /**
     * Dismiss dioalog
     *
     */
    private void dissmissDialog(){

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
            try{
                ft.commitAllowingStateLoss();
            }catch (IllegalStateException e){

            }
        }

    }



    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("Toutes ces autorisations sont requis par l'application, merci d'accorder svp!.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("ACCORDER", okListener)
                .setNegativeButton("Annuler", null)
                .create()
                .show();
    }

    public interface OnLoginFinishListener {
        public void onLoginFinished();
    }

}