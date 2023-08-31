package com.devkraft.karmahealth.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devkraft.karmahealth.Model.ReminderDto;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.AppUtils;

import androidx.core.content.ContextCompat;

import java.util.List;

public class ReminderDTOAdapter extends ArrayAdapter<ReminderDto> {// implements View.OnClickListener {
    public static final String TAG = ReminderDTOAdapter.class.getSimpleName();

    List<ReminderDto> reminderDtoList;
    Context context;

    View.OnClickListener editButtonOnClickListener, deleteButtonOnClickListener;

    public ReminderDTOAdapter(List<ReminderDto> reminderDtoList, Context context) {
        super(context, R.layout.dosage_summary_row, reminderDtoList);
        this.reminderDtoList = reminderDtoList;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ReminderDto reminderDto = reminderDtoList.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.dosage_summary_row, parent, false);
            viewHolder.txtFrequency = (TextView) convertView.findViewById(R.id.txtFrequency);
            viewHolder.txtDosageValue = (TextView) convertView.findViewById(R.id.txtDosageValue);
            viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            viewHolder.ivCustomDosageEdit = (ImageView) convertView.findViewById(R.id.ivCustomDosageEdit);
            viewHolder.ivCustomDosageDelete = (ImageView) convertView.findViewById(R.id.ivCustomDosageDelete);
            viewHolder.ivCustomDosageEdit.setTag(position);
            viewHolder.ivCustomDosageEdit.setOnClickListener(editButtonOnClickListener);
            viewHolder.ivCustomDosageDelete.setTag(position);
            viewHolder.ivCustomDosageDelete.setOnClickListener(deleteButtonOnClickListener);
            viewHolder.dosageIv = convertView.findViewById(R.id.dosageIv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (reminderDto.getDosageType().equalsIgnoreCase(context.getString(R.string.drug_daily))) {
            viewHolder.txtFrequency.setText(AppUtils.capitalize(reminderDto.getDosageType()));
        } else if (reminderDto.getDosageType().equalsIgnoreCase(context.getString(R.string.drug_monthly))) {
            String value = "";
            int i = Integer.parseInt(reminderDto.getValue());
            if (i == 1) {
                value = value.concat("1st");
            } else if (i == 2) {
                value = value.concat("2nd");
            } else if (i == 3) {
                value = value.concat("3rd");
            } else if (i == 21) {
                value = value.concat("21st");
            } else if (i == 22) {
                value = value.concat("22nd");
            } else if (i == 23) {
                value = value.concat("23rd");
            } else if (i == 31) {
                value = value.concat("31st");
            } else {
                value = value.concat(i + "th");
            }
            viewHolder.txtFrequency.setText(value + " of every month");
        } else {
            viewHolder.txtFrequency.setText(reminderDto.getValue());
        }

        String dosageForm = reminderDto.getDosageForm();
        if (context.getString(R.string.tablet).equalsIgnoreCase(dosageForm)) {
            viewHolder.dosageIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.tablet));
        } else if (context.getString(R.string.injection).equalsIgnoreCase(dosageForm)) {
            viewHolder.dosageIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.injection));
        } else if (context.getString(R.string.liquid).equalsIgnoreCase(dosageForm)) {
            viewHolder.dosageIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.liquid));
        } else if(context.getString(R.string.other).equalsIgnoreCase(dosageForm)){
            viewHolder.dosageIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.drop));
        }

        viewHolder.txtDosageValue.setText(reminderDto.getDosage());
        viewHolder.txtTime.setText(AppUtils.get12HrsDate(reminderDto.getReminderTime()));

        // Return the completed view to render on screen
        return convertView;
    }

    public void setEditButtonOnClickListener(View.OnClickListener editButtonOnClickListener) {
        this.editButtonOnClickListener = editButtonOnClickListener;
    }

    public void setDeleteButtonOnClickListener(View.OnClickListener deleteButtonOnClickListener) {
        this.deleteButtonOnClickListener = deleteButtonOnClickListener;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtFrequency;
        TextView txtDosageValue;
        TextView txtTime;
        ImageView ivCustomDosageEdit, ivCustomDosageDelete, dosageIv;
    }
}

