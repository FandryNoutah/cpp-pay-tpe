/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by evrencoskun on 11/06/2017.
 */

public class ColumnHeader extends Cell {
    public ColumnHeader(@NonNull String id, @Nullable String data) {
        super(id, data);
    }
}
