package com.devkraft.karmahealth.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.devkraft.karmahealth.Model.ParamDto;
import com.devkraft.karmahealth.R;
import java.util.List;

public class ParameterSpinnerAdapter extends BaseAdapter {
    private List<ParamDto> list;
    private Context context;
    private LayoutInflater inflater;

    public ParameterSpinnerAdapter(Context context, List<ParamDto> list) {
        this.list = list;
        this.context = context;
        this.inflater = (LayoutInflater.from(context));
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

        ParamDto userDto = list.get(i);
        names.setText(userDto.getName());
        return view;
    }
}
