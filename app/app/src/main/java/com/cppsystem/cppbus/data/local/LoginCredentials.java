/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.data.local;

import com.orm.SugarRecord;

/**
 * Created by anndrinarivo on 14/08/2017.
 */

public class LoginCredentials extends SugarRecord{
    private String login;
    private String password;
    private boolean rememberme;
    private String date;
    private boolean temporary;

    public LoginCredentials() {
    }

    public LoginCredentials(String login, String password, String date,boolean temporary,boolean rememberme) {
        this.login = login;
        this.password = password;
        this.rememberme = rememberme;

        this.date=date;
        this.temporary= temporary;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberme() {
        return rememberme;
    }

    public void setRememberme(boolean rememberme) {
        this.rememberme = rememberme;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }
}
