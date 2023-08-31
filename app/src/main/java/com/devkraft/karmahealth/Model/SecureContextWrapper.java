package com.devkraft.karmahealth.Model;

import android.content.Context;
import android.content.ContextWrapper;

public class SecureContextWrapper extends ContextWrapper {
    final private boolean wrapAppContext;
    final private boolean wrapLayoutInflater;

    public SecureContextWrapper(Context base, boolean wrapAppContext) {
        this(base, wrapAppContext, false);
    }

    public SecureContextWrapper(Context base, boolean wrapAppContext,
                                boolean wrapLayoutInflater) {
        super(base);

        this.wrapAppContext=wrapAppContext;
        this.wrapLayoutInflater=wrapLayoutInflater;
    }

    @Override
    public Object getSystemService(String name) {
        return(FlagSecureHelper
                .getWrappedSystemService(super.getSystemService(name), name,
                        wrapLayoutInflater));
    }

    @Override
    public Context getApplicationContext() {
        Context result=super.getApplicationContext();

        if (wrapAppContext) {
            result=new SecureContextWrapper(result, true);
        }

        return(result);
    }
}
