package com.devkraft.karmahealth.Utils;

import com.devkraft.karmahealth.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

public class ProgressDialogSetup extends ProgressDialog {

    private ImageView progressImage;
    private ProgressBar progressBar;
    private Drawable drawable;
    private int color;

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
    }


    public ProgressDialogSetup(Context context, int drawable, int colorAccent) {
        super(context);
        this.drawable = ContextCompat.getDrawable(context, drawable);
        color = ContextCompat.getColor(context,colorAccent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);
        progressImage = (ImageView) findViewById(R.id.progressImage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressImage.setImageDrawable(drawable);
        progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        setCancelable(false);
    }

    @Override
    public void show() {
        super.show();
        setProgressStyle(STYLE_SPINNER);
        Window window = getWindow();
        if(window != null){
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

    }

    public static ProgressDialogSetup getProgressDialog(Context ctxt) {
        return new ProgressDialogSetup(ctxt, R.drawable.kym_logo, R.color.colorPrimary);
    }

}

