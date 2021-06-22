/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.data.model.parse;


import com.cppsystem.cppbus.Secrets;
import com.cppsystem.cppbus.util.CppConstant;
import com.cppsystem.cppbus.util.CryptLib;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import ir.mirrajabi.searchdialog.core.Searchable;

@ParseClassName(CppConstant.PARSE_CPP_CLASS_FID_MEMBER)
public class CppFidMember extends ParseObject implements Searchable {
    private String gender;
    private String mobile;
    private String  cardStatus;
    private String idAlpha;
    private String src;
    private String login;
    private String dateCreate;
    private String name;
    private String cumulCredit;
    private String uniqueId;
    private String netPrice;
    private String adelyaId;
    private String nbCredit;
    private String actif;
    private String uid;
    private String previousCardnumber;
    private String cumulCA;
    private String fidProgramCode;
    private String dateBill;
    private String dateUpdate;
    private String cardnumber;
    private String type;
    private String tel;
    private String firstname;
    private String image;
    private String nbPoint;
    private String email;
    private String pseudo;
    private String cumulPoint;
    private String dateRenew;
    private String birthday;
    private String job;
    private String lang;



    public CppFidMember() {
    }

    private String decrypt(String data){
        // from local host

       // return data;
        // need for online version
        CryptLib cryptLib = null;
        try {
            cryptLib = new CryptLib();
            String[] secrets = new Secrets().getlZWKFgMj("com.cppsystem.cppbus").split(";");
            return cryptLib.decryptCipherText(data, secrets[0],secrets[1]);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }




    }


    public String getGender() {
        return decrypt(getString("gender"));
        //return gender;
    }

    public void setGender(String gender) {
        put("gender", gender);
    }

    public String getMobile() {
        return decrypt(getString("mobile"));
    }

    public void setMobile(String mobile) {
        //this.mobile = mobile;
        put("mobile",mobile);

    }

    public String getBirthday() {
        return decrypt(getString("birthday"));
    }

    public void setBirthday(String birthday) {
        put("birthday",birthday);
    }

    public String getCardStatus() {
        return decrypt(getString("cardStatus"));
    }

    public void setCardStatus(String cardStatus) {
        //this.cardStatus = cardStatus;
        put("cardStatus",cardStatus);
    }

    public String getIdAlpha() {
        return decrypt(getString("idAlpha"));
    }

    public void setIdAlpha(String idAlpha) {
        //this.idAlpha = idAlpha;
        put("idAlpha",idAlpha);
    }

    public String getSrc() {
        return decrypt(getString("src"));
    }

    public void setSrc(String src) {
        //this.src = src;
        put("src",src);
    }

    public String getLogin() {
        return decrypt(getString("login"));
    }

    public void setLogin(String login) {
        //this.login = login;
        put("login",login);
    }

    public String getDateCreate() {
        return decrypt(getString("dateCreate"));
    }

    public void setDateCreate(String dateCreate) {
        //this.dateCreate = dateCreate;
        put("dateCreate",dateCreate);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name",name);
    }

    public String getCumulCredit() {
        return decrypt(getString("cumulCredit"));
    }

    public void setCumulCredit(String cumulCredit) {
        //this.cumulCredit = cumulCredit;
        put("cumulCredit",cumulCredit);
    }

    public String getUniqueId() {
        return decrypt(getString("uniqueId"));
    }

    public void setUniqueId(String uniqueId) {
        //this.uniqueId = uniqueId;
        put("uniqueId",uniqueId);
    }

    public String getNetPrice() {
        return decrypt(getString("netPrice"));
    }

    public void setNetPrice(String netPrice) {
        put("netPrice",netPrice);
    }

    public String getAdelyaId() {
        return decrypt(getString("aId"));
    }

    public void setAdelyaId(String adelyaId) {
        put("aId",adelyaId);
    }

    public String getNbCredit() {
        return decrypt(getString("nbCredit"));
    }

    public void setNbCredit(String nbCredit) {
       put("nbCredit",nbCredit);
    }

    public String getActif() {
        return decrypt(getString("actif"));
    }

    public void setActif(String actif) {
       put("actif",actif);
    }

    public String getUid() {
        return decrypt(getString("uid"));
    }

    public void setUid(String uid) {
        put("uid",uid);
    }

    public String getPreviousCardnumber() {
        return decrypt(getString("previousCardnumber"));
    }

    public void setPreviousCardnumber(String previousCardnumber) {
        put("previousCardnumber",previousCardnumber);
    }

    public String getCumulCA() {
        return decrypt(getString("cumulCA"));
    }

    public void setCumulCA(String cumulCA) {
       put("cumulCA",cumulCA);
    }

    public String getFidProgramCode() {
        return decrypt(getString("fidProgramCode"));
    }

    public void setFidProgramCode(String fidProgramCode) {
      put("fidProgramCode",fidProgramCode);
    }

    public String getDateBill() {
        return decrypt(getString("dateBill"));
    }

    public void setDateBill(String dateBill) {
     //   this.dateBill = dateBill;
        put("dateBill",dateBill);
    }

    public String getDateUpdate() {
        return decrypt(getString("dateUpdate"));
    }

    public void setDateUpdate(String dateUpdate) {
        put("dateUpdate",dateUpdate);
    }

    public String getCardnumber() {
        return decrypt(getString("cardnumber"));
    }

    public void setCardnumber(String cardnumber) {
        put("cardnumber",cardnumber);
    }

    public String getType() {
        return decrypt(getString("type"));
    }

    public void setType(String type) {
       put("type",type);
    }

    public String getTel() {
        return decrypt(getString("tel"));
    }

    public void setTel(String tel) {
      put("tel",tel);
    }

    public String getFirstname() {
        return getString("firstname");
    }

    public void setFirstname(String firstname) {
        put("firstname",firstname);
    }

    public String getImage() {
        return decrypt(getString("image"));
    }

    public void setImage(String image) {
       put("image",image);
    }

    public String getNbPoint() {
        return decrypt(getString("nbPoint"));
    }

    public void setNbPoint(String nbPoint) {
      put("nbPoint",nbPoint);
    }

    public String getEmail() {
        return decrypt(getString("email"));
    }

    public void setEmail(String email) {
       put("email",email);
    }

    public String getPseudo() {
        return decrypt(getString("pseudo"));
    }

    public void setPseudo(String pseudo) {
       put("pseudo",pseudo);
    }

    public String getCumulPoint() {
        return decrypt(getString("cumulPoint"));
    }

    public void setCumulPoint(String cumulPoint) {
       put("cumulPoint",cumulPoint);
    }

    public String getDateRenew() {
        return decrypt(getString("dateRenew"));
    }

    public void setDateRenew(String dateRenew) {
       put("dateRenew",dateRenew);
    }

    @Override
    public String getTitle() {
        return getFirstname()+" "+getName();
    }

    public String getJob() {
        return decrypt(getString("job"));
    }

    public void setJob(String job) {
       put("job",job);
    }

    public String getLang() {
        return decrypt(getString("lang"));
    }

    public void setLang(String lang) {
        put("lang",lang);
    }
}
