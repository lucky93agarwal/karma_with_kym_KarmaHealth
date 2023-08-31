package com.devkraft.karmahealth.Adapter;

import android.content.Context;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.fragment.ConditionNewFragment;
import com.devkraft.karmahealth.fragment.SymptomTrackersFragment;
import com.devkraft.karmahealth.fragment.TrackersNewFragment;

import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    private Context mcontext;

    @Override
    public Parcelable saveState() {
        return null;
    }

    public ViewPagerAdapter(FragmentManager fm, int tabCount, Context context) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
        this.mcontext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
//            case 0:
//                return ConditionNewFragment.newInstance();
            case 0:
                return TrackersNewFragment.newInstance();
            case 1:
                return SymptomTrackersFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
//            case 0:
//                return mcontext.getString(R.string.condition);
            case 0:
                return mcontext.getString(R.string.trackers);
            case 1:
                return mcontext.getString(R.string.symptom_trackers);
        }
        return null;
    }

    @Override
    public int getItemPosition(@NonNull @NotNull Object object) {
        return getItemPosition(object);
    }
}
