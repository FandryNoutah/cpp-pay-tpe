/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.data.local;

import com.orm.SugarRecord;

import java.util.Date;

public class CppUserLastParam  extends SugarRecord {
    private String ligne;
    private String cooperative;
    private String vehicule;
    private String ligneVariance;
    private String transportType;
    private String dateSync;
    private String departId;
    private String arriveeId;
    private int defaultPrice;
    private int priceMin;
    private int priceMax;
    private String chauffeurId;
    private String receveurId;
    private String userVehicleList;
    public CppUserLastParam() {
    }

    public CppUserLastParam(String ligne, String cooperative, String vehicule, String ligneVariance) {
        this.ligne = ligne;
        this.cooperative = cooperative;
        this.vehicule = vehicule;
        this.ligneVariance = ligneVariance;
    }

    public int getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(int defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public int getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getArriveeId() {
        return arriveeId;
    }

    public void setArriveeId(String arriveeId) {
        this.arriveeId = arriveeId;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getLigneVariance() {
        return ligneVariance;
    }

    public void setLigneVariance(String ligneVariance) {
        this.ligneVariance = ligneVariance;
    }

    public String getLigne() {
        return ligne;
    }

    public void setLigne(String ligne) {
        this.ligne = ligne;
    }

    public String getCooperative() {
        return cooperative;
    }

    public void setCooperative(String cooperative) {
        this.cooperative = cooperative;
    }

    public String getVehicule() {
        return vehicule;
    }

    public void setVehicule(String vehicule) {
        this.vehicule = vehicule;
    }

    public String getDateSync() {
        return dateSync;
    }

    public void setDateSync(String dateSync) {
        this.dateSync = dateSync;
    }

    public String getChauffeurId() {
        return chauffeurId;
    }

    public void setChauffeurId(String chauffeurId) {
        this.chauffeurId = chauffeurId;
    }

    public String getReceveurId() {
        return receveurId;
    }

    public void setReceveurId(String receveurId) {
        this.receveurId = receveurId;
    }

    public int getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(int priceMax) {
        this.priceMax = priceMax;
    }

    public String getUserVehicleList() {
        return userVehicleList;
    }

    public void setUserVehicleList(String userVehicleList) {
        this.userVehicleList = userVehicleList;
    }
}
