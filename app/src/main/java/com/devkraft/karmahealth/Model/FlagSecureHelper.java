package com.devkraft.karmahealth.Model;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.WindowManager;

public class FlagSecureHelper {
    public static Object getWrappedSystemService(Object service,
                                                 String name) {
        return(getWrappedSystemService(service, name, false));
    }

    public static Object getWrappedSystemService(Object service,
                                                 String name,
                                                 boolean wrapLayoutInflater) {
        if (Context.WINDOW_SERVICE.equals(name)) {
            boolean goAhead=true;

            for (StackTraceElement entry : Thread.currentThread().getStackTrace()) {
                try {
                    Class cls=Class.forName(entry.getClassName());

                    if (Dialog.class.isAssignableFrom(cls)) {
                        goAhead=false;
//                        goAhead=true; // added for testing purpose
                        break;
                    }
                }
                catch (ClassNotFoundException e) {
                    // ???
                }
            }

            if (goAhead) {
                service=new SecureWindowManagerWrapper((WindowManager)service);
            }
        }
        else if (Context.LAYOUT_INFLATER_SERVICE.equals(name) && wrapLayoutInflater) {
            LayoutInflater original=(LayoutInflater)service;
            Context securified=
                    new SecureContextWrapper(original.getContext(), true, true);

            service=original.cloneInContext(securified);
        }

        return(service);
    }
}
