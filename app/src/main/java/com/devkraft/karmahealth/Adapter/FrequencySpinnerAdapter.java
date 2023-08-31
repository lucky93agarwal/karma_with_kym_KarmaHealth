package com.devkraft.karmahealth.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Screen.AddDrugActivity;

import java.util.List;

public class FrequencySpinnerAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;
    private LayoutInflater inflater;
    private AddDrugActivity.SearchUserDtoCallback searchUserDtoCallback;

    public FrequencySpinnerAdapter(Context applicationContext, List<String> list) {
        this.context = applicationContext;
        this.list = list;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.users_spinner_layout, null);
        TextView names = view.findViewById(R.id.userName);

        names.setTextSize(14f);

        String str = list.get(i);
        names.setText(str);
        return view;
    }
}
