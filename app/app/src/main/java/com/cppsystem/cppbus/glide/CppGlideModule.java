/*
 * Copyright (c) 2021.
 * *******************************************************************************
 * This software is full property of CPP-SYSTEM MADAGASCAR SARL
 * This project was initially developped by Andrinarivo Rakotozafinirina on 2020
 * ************************************************************************************
 */

package com.cppsystem.cppbus.glide;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class CppGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}

