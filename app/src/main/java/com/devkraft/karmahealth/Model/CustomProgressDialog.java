package com.devkraft.karmahealth.Model;


import com.devkraft.karmahealth.R;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

public class CustomProgressDialog extends Dialog {

    private Dialog dialog;
    public CustomProgressDialog(Context context) {
        super(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View dialogView = layoutInflater.inflate(R.layout.custom_progress_dialog, null);
        ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar_custom_dialog);
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
        progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void show() {
        super.show();
        if (dialog!=null)
            dialog.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (dialog!=null)
            dialog.dismiss();
    }
}
