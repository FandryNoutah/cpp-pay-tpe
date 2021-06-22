/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.data.local;

public class MessageEvent {



    public final boolean memberDone;
    public final String varianceId;
    public final String vehiculeId;
    public final String cooperativeId;
    public final String ligneId;
    public final String transportTypeId;
    public final int fullDone;
    public boolean timeout;
    public String carduid;



    public MessageEvent(boolean done,String varianceId,String vehiculeId,String cooperativeId,String ligneId,String transportTypeId,int fullDone,boolean timeout) {
        this.memberDone =done;
        this.varianceId=varianceId;
        this.vehiculeId=vehiculeId;
        this.cooperativeId=cooperativeId;
        this.ligneId=ligneId;
        this.transportTypeId=transportTypeId;
        this.fullDone=fullDone;
        this.timeout=timeout;
    }

    public MessageEvent(String carduid) {
        this.carduid = carduid;
        memberDone = false;
        varianceId=null;
        cooperativeId=null;
        vehiculeId=null;
        ligneId=null;
        transportTypeId=null;
        fullDone=0;
    }
}
