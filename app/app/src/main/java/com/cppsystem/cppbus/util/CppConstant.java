/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.cppsystem.cppbus.Secrets;

import com.cppsystem.cppbus.data.model.parse.CppFidMember;

import com.parse.ParseUser;
import com.pixplicity.easyprefs.library.Prefs;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Hours;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CppConstant {
    public static final String OFFLINE_CPP_FID_MEMBER = "fidmember";
    public static final String OFFLINE_CPP_LIGNE_VARIANCE="cpplignevariance";
    public static final String OFFLINE_CPP_VEHICLE="cppvehicule";
    public static final String OFFLINE_CPP_LIGNE="cppligne";
    public static final String OFFLINE_CPP_COOPERATIVE="cppcooperative";
    public static final String OFFLINE_CPP_ARRET="cpparret";
    public static final String OFFLINE_CPP_VARIANCE="cppvariance";
    public static final String OFFLINE_CPP_PARAM_STATION="cppparamstation";
    public static final String OFFLINE_CPP_TRANSPORT_EVENT_DAY="cpptransportevent_day";
    public static final String OFFLINE_CPP_TRANSPORT_EVENT_WEEKLY="cpptransportevent_weekly";
    public static final String OFFLINE_CPP_TRANSPORT_EVENT_2H="cpptransportevent_2h";
    public static final String OFFLINE_CPP_CONTROL_EVENT_DAILY="cppcontrolevent_day";
    public static final String OFFLINE_CPP_VEHICLE_USER="cppvehicule";
    public static final String OFFLINE_CPP_COOPERATIVE_USER="cppcooperative";
    public static final String OFFLINE_CPP_TRANSPORT_TYPE="cpptransporttype";
    public static final String OFFLINE_CPP_INCIDENT_TYPE="cppincidenttype";
    public static final String OFFLINE_CPP_INCIDENT="cppincident";
    public static final String OFFLINE_CPP_EVENT_TOURNEE="cppeventtournee";

    public static final String EXTRA_COOP_ID="coop_id";
    public static final String EXTRA_LIGNE_ID="ligne_id";
    public static final String EXTRA_LIGNE_VAR_ID="ligne_var_id";
    public static final String EXTRA_VEHICLE_ID="vehicle_id";
    public static final String EXTRA_TRANS_TYPE_ID="trans_type_id";
    public static final String EXTRA_DEPART_ID="depart_id";
    public static final String EXTRA_ARRIVER_ID="arrivee_id";
    public static final String EXTRA_STATION_ID="station_id";
    public static final String EXTRA_SYNC_USER_ONLY="sync_user_only";
    public static final String EXTRA_MULTIPLE_ENCAISS_ID="multiple_encaisse_id";
    public static final String EXTRA_CPP_TRANSPORT_EVENT_ID="cpp_transport_event_id";
    public static final String EXTRA_GO_TO_OTHER="gotoother";
    public static final String EXTRA_SHOW_INCIDENT_DIALOG = "show_dialog_incident";
    public static final String EXTRA_INCIDENT_TYPE = "incident_type";
    public static final String EXTRA_INCIDENT_CONTROL_COMMENT = "incident_control_comments";

    public static final String PARSE_CPP_CLASS_COOPERATIVE="CppCooperative";
    public static final String PARSE_CPP_CLASS_LIGNE="CppLigneTransport";
    public static final String PARSE_CPP_CLASS_LIGNE_VARIANCE="CppLigneVariance";
    public static final String PARSE_CPP_CLASS_VEHICLE="CppTransportVehicule";
    public static final String PARSE_CPP_CLASS_ARRET="CppBusArret";
    public static final String PARSE_CPP_CLASS_TRANSPORT_TYPE="CppTransportType";
    public static final String PARSE_CPP_CLASS_FID_MEMBER="CppFidMember";
    public static final String PARSE_CPP_CLASS_VARIANCE="CppVariance";
    public static final String PARSE_CPP_CLASS_VEHICLE_USER="CppVehicleUser";
    public static final String PARSE_CPP_CLASS_PARAM_STATION="CppParamStation";
    public static final String PARSE_CPP_CLASS_INCIDENT="CppTransportIncident";
    public static final String PARSE_CPP_CLASS_VEHICLE_STATUS="CppVehicleStatus";
    public static final String PARSE_CPP_CLASS_TRANSPORT_EVENT="CppTransportEvent"; // CppFidMemberSearch
    public static final String PARSE_CPP_CLASS_FID_MEMBER_SEARCH="CppFidMemberSearch";
    public static final String PARSE_CPP_CLASS_EVENT_TOURNEE="CppEventTournee";
    public static final String PARSE_CPP_CLASS_EVENT_CONTROL="CppEventControl";
    public static final String PARSE_CPP_CLASS_FIDMEMBER_COUNT="CppFidMemberCount";
    public static final String PARSE_CPP_CLASS_USER_COOPERATIVE="CppUserCooperative";
    public static final String PARSE_CPP_CLASS_INCIDENT_TYPE="CppIncidentType";
    public static final String PARSE_CPP_CLASS_TPE_LOCATION_TRACK="CppTpeLocationTrack";
    public static final String PARSE_CPP_CLASS_TRANSPORT_VEHICLE="CppTransportVehicule";

    //CppVehicleStatus


    public static final long DEFAULT_TIMEOUT_QUERY=20000;
    public static final long QUICK_TIMEOUT_QUERY=10000;
    public static final long BIG_TIMEOUT_QUERY=120000;
    public static final int DEFAUlT_MAX_QUERY_LIMIT=100;
    public static final int DEFAULT_HOUR_QUERY_PAST=2;

    public static final String CAISSE_TYPE_CASH="CASH";
    public static final String CAISSE_TYPE_CPP="CPP";
    public static final String CAISSE_TYPE_CHECKIN="BADGE";
    public static final String CAISSE_TYPE_CANCEL="CANCELLATION";
    public static final String CAISSE_TYPE_UPDATE="UPDATE_TICKET";
    public static final String CAISSE_TYPE_CANCEL_COMMENT_TICKET="TICKET CANCELLATION";
    public static final String CAISSE_TYPE_CANCEL_COMMENT_CASH="CASH CANCELLATION";
    public static final String CAISSE_TYPE_TRANSAC_NO_CREDIT="NO_CREDIT";
    public static final String CAISSE_TYPE_TRANSAC_OK="OK";
    public static final String CAISSE_TYPE_TRANSAC_CONTROL="CONTROL";
    public static final String CAISSE_TYPE_CHECKIN_COMMENTS="CARD PASSAGE";
    public static final String CAISSE_TYPE_CHECKIN_GET_CREDIT="GET CREDIT";
    public static final String CAISSE_TYPE_NO_CREDIT_COMMENTS="INSUFFICIENT TICKET";
    public static final String CAISSE_TYPE_ETIK="DEBIT TICKET";
    public static final String CAISSE_TYPE_CARD_NOT_FOUND="INVALID CARD";
    public static final String CAISSE_TYPE_CARD_HACKED="CORRUPT CARD";
    //public static final String CAISSE_TYPE_CARD_NOT_FOUND="CARD UNKNOWN";


    public static final String CAISSE_MULT_STATUS_WAIT="caisse_mult_wait";
    public static final String CAISSE_MULT_STATUS_DONE="caisse_mult_done";
    public static final String CAISSE_DEFAULT_PRICE = "caisse_default_price";

    public static final String STATION_ID_PREFS = "station_id_prefs";
    public static final String CPP_BUS_TOURNEE_CONTROL_COUNT = "bus_tournee_control_count";
    public static final String CPP_BUS_TOURNEE_CONTROL_UID = "bus_tournee_control_uid";
    public static final String INCIDENT_TYPE_PREFS = "incident_id_prefs";
    public static final String INCIDENT_TYPE_DEFAULT = "7bkRQnkmYc";
    public static final String INCIDENT_TYPE_CONTROL = "jsSNwwXEo4";

    public static final String CPP_TRANSPORT_DEFAULT_ARRET="LAHYavOEnq";
    public static final String TICKET_PRICE_EDTX = "ticket_price";
    public static final String SCREEN_SAVER_PREFS="screenSaver";

    public static final String PHONE_LASER_TPE="PDA401";

    public static final String SCAN_QRCODE_CN = "cn-";
    public static final String SCAN_QRCODE_UID = "uid-";
    public static final String SCAN_QRCODE_UUID = "uuid-";

    public static final String CPP_BUS_SENS_EXTRA="cpp_sens";
    public static final String CPP_BUS_SENS_ALLER="ALLER";
    public static final String CPP_BUS_SENS_RETOUR="RETOUR";
    public static final String CPP_BUS_TOUR_EXTRA="cpp_bus_tour";
    public static final String CPP_RESET_STATION_EXTRA= "station_reset_to_start";
    public static final String CPP_TOUR_COUNT= "tour_count";
    public static final String CPP_TOUR_UID= "tour_count";

    public static final String CPP_VEHICLE_STATUS="cpp_bus_status";

    public static final String CPP_VEHICLE_EN_SERVICE="EN SERVICE";
    public static final String CPP_VEHICLE_EN_INCIDENT="INCIDENT";
    public static final String CPP_VEHICLE_EN_PAUSE="PAUSE";
    public static final String CPP_VEHICLE_DEPOT="DEPÔT";
    public static final String CPP_VEHICLE_EN_FIN_SERVICE="FIN DE SERVICE";
    public static final String CPP_VEHICLE_HORS_SERVICE="HORS SERVICE";
    public static final String CPP_VEHICLE_DEPART_IMM="DEPART IMMINENT";
    public static final String CPP_VEHICLE_DEPART_15_MIN="DEPART 15 Min";
    public static final String CPP_VEHICLE_DEPART_10_MIN="DEPART 15 Min";

    public static final String CPP_USER_TYPE_CHAUFFEUR="chauffeur";
    public static final String CPP_USER_TYPE_RECEVEUR="receveur";
    public static final String CPP_USER_TYPE_CONTROLLEUR="controlleur";
    public static final String CPP_USER_TYPE_POINTEUR="pointeur";
    public static final String CPP_CONTROL_LOAD_HISTORY="load_history";
    public static final String CAISSE_TYPE_CONTROL_CHECKIN_COMMENTS="CONTROL";
    public static final String CONTROL_ITEM_STATUS_OK="OK";
    public static final String CONTROL_ITEM_STATUS_KO="KO";
    public static final String CONTROL_ITEM_STATUS_WAIT="WAIT";
    public static final String INCIDENT_CONTROL="Contrôle";
    public static final String INCIDENT_CONTROL_POLICE="Contrôle de Police";
    public static final String INCIDENT_PANNE="Panne";
    public static final String INCIDENT_ROUTE_DEVIEE="Route dévié";
    public static final String INCIDENT_ROUTE_BLOQUEE="Route bloqué";
    public static final String INCIDENT_AUTRES="Autres";
    public static final String NO_VEHICLE="no_vehicle";
    public static final String NO_VARIANCE="no_variance";
    public static final String INCIDENT_CONTROL_ID="jsSNwwXEo4";
    public static final String INCIDENT_PANNE_ID="7bkRQnkmYc";
    public static final String INCIDENT_ROUTE_DEVIEE_ID="Se06G9wJYJ";
    public static final String INCIDENT_CONTROL_POLICE_ID="iDUhWzKgD2";
    public static final String INCIDENT_ROUTE_BLOQUEE_ID="CvR3pVo9bz";
    public static final String INCIDENT_AUTRES_ID="62CjQS6nhQ";




    public static  String hidePartPhoneNumber(String fullPhone){
        //Log.e("Full phone",fullPhone);
        if(fullPhone==null)
            return "Non renseigné";
        String threelastPhone="",threeFirstPhone="";
        if (fullPhone.length() == 3) {
            threelastPhone=fullPhone;
            threeFirstPhone=fullPhone;
        } else if (fullPhone.length() > 3) {
            threelastPhone = fullPhone.substring(fullPhone.length() - 3); //+261342991788
            threeFirstPhone= fullPhone.substring(0,3);
        }
        if(threeFirstPhone.isEmpty() && threelastPhone.isEmpty())

            return "Non renseigné";
        else
            return  threeFirstPhone+"*******";

    }
    public static String hidePartCardNumber(String fullCardNumber){
        if(fullCardNumber==null)
            return "Non renseigné";
        String threelastCard="",threeFirstCard="";
        if (fullCardNumber.length() == 3) {
            threelastCard=fullCardNumber;
          //  threeFirstCard=fullCardNumber;
        } else if (fullCardNumber.length() > 3) {
            threelastCard = fullCardNumber.substring(fullCardNumber.length() - 3); //+261342991788
            //threeFirstCard= "0"+fullCardNumber.substring(4,6);
        }
        if(threelastCard.isEmpty())

            return "Non renseigné";
        else
            return "*******"+threelastCard;
    }
    public static String getLastPhoneNumber(String fullPhone){
        if(fullPhone==null)
            return "";
        String threelastPhone="",threeFirstPhone="";
        if (fullPhone.length() == 3) {
            threelastPhone=fullPhone;

        } else if (fullPhone.length() > 3) {
            threelastPhone = fullPhone.substring(fullPhone.length() - 3); //+261342991788

        }
        if(threelastPhone.isEmpty())

            return "";
        else
            return threelastPhone ;
    }
    public static String getLastCardNumber(String fullCardNumber){
        String threelastCard="",threeFirstCard="";
        if (fullCardNumber.length() == 3) {
            threelastCard=fullCardNumber;
            //  threeFirstCard=fullCardNumber;
        } else if (fullCardNumber.length() > 3) {
            threelastCard = fullCardNumber.substring(fullCardNumber.length() - 3); //+261342991788
            //threeFirstCard= "0"+fullCardNumber.substring(4,6);
        }
        if(threelastCard.isEmpty())
            return "";
        else
            return threelastCard;
    }
    public static String getYearOfDate(String date){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dt = formatter.parseDateTime(date);
        int year = dt.getYear();
        return year+"";
    }
    public static boolean lessThan2Hours(String dateSaved){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime dt = formatter.parseDateTime(dateSaved);
        int hours = Hours.hoursBetween(dt,new DateTime()).getHours();
        return hours < 2;
    }


    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
              //  Log.e("value stations foreach","presf "+value +" "+entry.getKey());
                return entry.getKey();
            }
        }
        //Log.e("value stationssdfs ","presf 0 returned");
        return (T)new Integer(0);
    }
    public static String getDateTimeNow(){
        DateTime currentTime = new DateTime();
        DateTime dt = currentTime.withZone(DateTimeZone.forID("Indian/Antananarivo"));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        return fmt.print(dt);
       // Log.e("Indian/Antananarivo", datee);
    }
    public static String getDateTimeNowTourneeFormat(){
        DateTime currentTime = new DateTime();
        DateTime dt = currentTime.withZone(DateTimeZone.forID("Indian/Antananarivo"));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return fmt.print(dt);
        // Log.e("Indian/Antananarivo", datee);
    }
    public static String getDateOnlyNow(){
        DateTime currentTime = new DateTime();
        DateTime dt = currentTime.withZone(DateTimeZone.forID("Indian/Antananarivo"));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        return fmt.print(dt);
    }
    public static String getHourOnlyNow(){
        DateTime currentTime = new DateTime();
        DateTime dt = currentTime.withZone(DateTimeZone.forID("Indian/Antananarivo"));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm:ss");
        return fmt.print(dt);
    }
    public static DateTime getDateTime(){
        DateTime dt = new DateTime(DateTimeZone.UTC);
        return dt;
    }
    public static DateTime getDateTimeIndian(){
        DateTime currentTime = new DateTime();
        DateTime dt = currentTime.withZone(DateTimeZone.forID("Indian/Antananarivo"));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        return fmt.parseDateTime(fmt.print(dt));
    }
    public static Date getDateTimeNow2Date(){
        DateTime currentTime = new DateTime();
        DateTime dt = currentTime.withZone(DateTimeZone.forID("Indian/Antananarivo"));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        return fmt.parseDateTime(fmt.print(dt)).toDate();
    }
    public static String formatDate(DateTime dt){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        return fmt.print(dt);
    }
    public static String getPhoneImei(Context context){
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();

    }



    /**
     * Check if host is reachable.
     * @param host The host to check for availability. Can either be a machine name, such as "google.com",
     *             or a textual representation of its IP address, such as "8.8.8.8".
     * @param port The port number.
     * @param timeout The timeout in milliseconds.
     * @return True if the host is reachable. False otherwise.
     */
    public static boolean isHostAvailable(final String host, final int port, final int timeout) {

        boolean reachable = false;

        try {
            reachable = InetAddress.getByName("www.example.com").isReachable(2000);
        } catch (IOException e) {
            e.printStackTrace();
            reachable = false;
        }


        try (final Socket socket = new Socket()) {
            final InetAddress inetAddress = InetAddress.getByName(host);
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, port);

            socket.connect(inetSocketAddress, timeout);
            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String generateTicketNumber(){
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        String datetime = ft.format(dNow);
        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(datetime.length());
        for (int i = 0; i < datetime.length(); i++) {
            int index
                    = (int)(datetime.length()
                    * Math.random());
            sb.append(datetime
                    .charAt(index));
        }
        String ticket =sb.toString();
        //Log.e("Ticket",ticket);
        return ticket;
    }
    public static  String genOfflineObjectGroupIdByMonth(){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMM");
        String datenow = fmt.print(getDateTimeIndian());
        return datenow;
    }

    public static String getLastMonthOjectGroupId(){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMM");
        String datenow = fmt.print(getDateTimeIndian().minusMonths(1));
        return datenow;
    }
    public static String getLast2MonthOjectGroupId(){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMM");
        String datenow = fmt.print(getDateTimeIndian().minusMonths(2));
        return datenow;
    }

    public static String getCryptedText(String plainText) throws Exception {
        String[] secrets = new Secrets().getlZWKFgMj("com.cppsystem.cppbus").split(";");
        String crypt = new CryptLib().encryptPlainText(plainText,secrets[0],secrets[1]);

        return crypt;
    }

    public static String decrypt(String data, String a, String b) throws Exception {
        CryptLib cryptLib = null;

            cryptLib = new CryptLib();
            return cryptLib.decryptCipherText(data, a,b);



    }



    public static String getFidMemberInfo(CppFidMember cppFidMember){
        // actif;lang;sexe-age;profil;credit
       // 1;M;F27;1;1000
        String actif = cppFidMember.getActif();
        String lang =cppFidMember.getLang();
        String sexe="";
        switch (cppFidMember.getGender()){
            case "MELLE":
                sexe="F";
                break;
            case "MME":
                sexe="F";
                break;
            case "M":
                sexe="H";
                break;
        }
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateBirth = fmt.parseDateTime(cppFidMember.getBirthday());
        int years = Years.yearsBetween(new DateTime(),dateBirth).getYears();
        String age = years+"";
        String profil="";
        if(cppFidMember.getJob()!=null){
            if(cppFidMember.getJob().length()>5){
                profil = cppFidMember.getJob().substring(5);
            }
        }
        Log.e("profil",cppFidMember.getJob()+" test");
        //String profil = cppFidMember.getJob().substring(5);
       // return actif+";"+lang+";"+sexe+age.replaceAll("-","")+";";
       return actif+";"+lang+";"+sexe+age.replaceAll("-","")+";"+profil;


    }



}
