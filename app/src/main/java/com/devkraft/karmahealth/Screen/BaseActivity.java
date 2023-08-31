package com.devkraft.karmahealth.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devkraft.karmahealth.Model.FlagSecureHelper;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
    @Override
    public Object getSystemService(@NonNull String name) {
        Object result=super.getSystemService(name);

        return(FlagSecureHelper.getWrappedSystemService(result, name));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
//        String selectedLanguage = ApplicationPreferences.get().getSelectedLanguage();
//        Context context;
//        if (selectedLanguage != null) {
//            Locale newLocale = new Locale(selectedLanguage);
//        //    context = ContextWrapper.wrap(newBase, newLocale);
//        } else {
//            String systemLanguage = Locale.getDefault().getLanguage();
////            if(systemLanguage.equalsIgnoreCase(Constants.HINDI)){
////                ApplicationPreferences.get().setSelectedLanguage(Constants.HINDI);
////            } else if (systemLanguage.equalsIgnoreCase(Constants.SPANISH)) {
////                ApplicationPreferences.get().setSelectedLanguage(Constants.SPANISH);
////            } else {
////                ApplicationPreferences.get().setSelectedLanguage(Constants.ENGLISH);
////            }
////            Locale newLocale = new Locale(systemLanguage);
////            context = ContextWrapper.wrap(newBase, newLocale);
//        }
        super.attachBaseContext(newBase);
    }
}