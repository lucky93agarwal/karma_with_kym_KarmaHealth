package com.devkraft.karmahealth.Model;

import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class SecureWindowManagerWrapper implements WindowManager {
    private final WindowManager wm;

    public SecureWindowManagerWrapper(WindowManager wm) {
        this.wm=wm;
    }

    @Override
    public Display getDefaultDisplay() {
        return(wm.getDefaultDisplay());
    }

    @Override
    public void removeViewImmediate(View view) {
        wm.removeViewImmediate(view);
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {
        wm.addView(view, makeSecure(params));
    }

    @Override
    public void updateViewLayout(View view,
                                 ViewGroup.LayoutParams params) {
        wm.updateViewLayout(view, makeSecure(params));
    }

    @Override
    public void removeView(View view) {
        wm.removeView(view);
    }

    private ViewGroup.LayoutParams makeSecure(ViewGroup.LayoutParams params) {
        if (params instanceof LayoutParams) {
            LayoutParams lp=(LayoutParams)params;

            lp.flags |= LayoutParams.FLAG_SECURE;
        }

        return(params);
    }
}
