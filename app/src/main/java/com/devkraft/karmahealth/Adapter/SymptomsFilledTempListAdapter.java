package com.devkraft.karmahealth.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devkraft.karmahealth.Model.GetUserAddedSymptomsResponseDTO;
import com.devkraft.karmahealth.R;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.devkraft.karmahealth.Screen.MyConditionsNewActivity;
import com.devkraft.karmahealth.Screen.SymptomsDetailActivity;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.inter.RvClickListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;



import static com.devkraft.karmahealth.fragment.SymptomTrackersFragment.userTracking;

public class SymptomsFilledTempListAdapter extends RecyclerView.Adapter<SymptomsFilledTempListAdapter.ViewHolder> {

    private Context mContext;
    private RvClickListener rvClickListener;
    private List<GetUserAddedSymptomsResponseDTO> mList;

    private LinearLayout mLayoutSaveCancel;
    private FrameLayout mFloatingButtonView;
    private List<View> relatedViews = new ArrayList<>();

    public SymptomsFilledTempListAdapter(Context context, List<GetUserAddedSymptomsResponseDTO> checkupList, LinearLayout mLayoutSaveCancelButton, FrameLayout mFloatingButtonLayout) {
        this.mContext = context;
        this.mList = checkupList;
        this.mLayoutSaveCancel = mLayoutSaveCancelButton;
        this.mFloatingButtonView = mFloatingButtonLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_symptoms_filled_list_item, parent, false);
        return new ViewHolder(adapterView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.mTextViewSymptomName.setText(mList.get(position).getName());

        if (mList.get(position).getRecordedDate() == null) {
            holder.mTextViewRecordedTime.setVisibility(View.GONE);
        } else {
            holder.mTextViewRecordedTime.setVisibility(View.VISIBLE);
            holder.mTextViewRecordedTime.setText(mContext.getString(R.string.updated_on_text) + " " + AppUtils.getOnlyTimeFromSymptomDateTwo(mList.get(position).getRecordedDate()));
        }

        if ((mList.get(position).getSeverityLevel() != null && !mList.get(position).getSeverityLevel().equals(""))) {

            int ansPos = 0;
            if (mList.get(position).getSelectedMeasurementValues() == null || mList.get(position).getSelectedMeasurementValues().equals("")) {
                Log.e("severityLeveLPos", ":" + mList.get(position).getSeverityLevel() + "   : severityLeveLvaluePos :  " + mList.get(position).getMeasurementValue().indexOf(mList.get(position).getSeverityLevel()));
                ansPos = mList.get(position).getMeasurementValue().indexOf(mList.get(position).getSeverityLevel());

//                updateSelectedColor(holder, ansPos, position);
            } else {
                Log.e("mesurementLeveLPos", ":" + mList.get(position).getSelectedMeasurementValues() + "   : severityLeveLvaluePos :  " + mList.get(position).getMeasurementValue().indexOf(mList.get(position).getSelectedMeasurementValues()));
                ansPos = mList.get(position).getMeasurementValue().indexOf(mList.get(position).getSelectedMeasurementValues());

            }

            updateSelectedColor(holder, ansPos, position);


          /*  if (mList.get(position).getSeverityLevel().equalsIgnoreCase(Constants.MILD) ||
                    mList.get(position).getSeverityLevel().equalsIgnoreCase("3-5")) {
                setMildTextVisibleWithBackground(holder);
            } else if (mList.get(position).getSeverityLevel().equalsIgnoreCase(Constants.NONE) ||
                    mList.get(position).getSeverityLevel().equalsIgnoreCase("0-2")) {
                setNoneTextVisibleWithBackground(holder);
            } else if (mList.get(position).getSeverityLevel().equalsIgnoreCase(Constants.MODERATE) ||
                    mList.get(position).getSeverityLevel().equalsIgnoreCase("6-8")) {
                setModerateTextVisibleWithBackground(holder);
            } else if (mList.get(position).getSeverityLevel().equalsIgnoreCase(Constants.SEVERE) ||
                    mList.get(position).getSeverityLevel().equalsIgnoreCase("9-10")) {
                setDifficultTextVisibleWithBackground(holder);
            } else if (mList.get(position).getSeverityLevel().equalsIgnoreCase(Constants.VERY_SEVERE) ||
                    mList.get(position).getSeverityLevel().equalsIgnoreCase("10+")) {
                setSevereTextVisibleWithBackground(holder);
            }*/
        } else {

            Log.e("severityLeveLPos", ": Else :");

            setEmptyView(holder);
//            removeSelection(holder);
        }

        if (mList.get(position).getMeasurementValue() != null && !mList.get(position).getMeasurementValue().isEmpty()) {
            setDataToTextViews(holder, position);
        }

        if (mList.get(position).getSelectedMeasurementValues() != null && !mList.get(position).getSelectedMeasurementValues().equals("")) {
            Log.e("ansPosLog", ":" + mList.get(position).getSelectedMeasurementValues() + "   : valuePos :  " + mList.get(position).getMeasurementValue().indexOf(mList.get(position).getSelectedMeasurementValues()));
            int ansPos = mList.get(position).getMeasurementValue().indexOf(mList.get(position).getSelectedMeasurementValues());

            updateSelectedColor(holder, ansPos, position);
            mLayoutSaveCancel.setVisibility(View.VISIBLE);
        }

        /*else {
            Log.e("measureMentValueEmpty", ": Else :");
            setEmptyView(holder);
        }*/
/*
        mButtonCancel.setOnClickListener(v -> {
            clearSelection();
        });*/

    /*    holder.mLayoutNone.setOnClickListener(v -> handleNoneClick(holder, position));
        holder.mLayoutMild.setOnClickListener(v -> handleMildClick(holder, position));
        holder.mLayoutSevere.setOnClickListener(v -> handleSevereClick(holder, position));
        holder.mLayoutModerate.setOnClickListener(v -> handleModerateClick(holder, position));
        holder.mLayoutDifficult.setOnClickListener(v -> handleDifficultClick(holder, position));*/

        holder.itemView.setOnClickListener(v -> {
           // openDetailActivity(position);
        });
    }

    private void clearSelection() {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelectedMeasurementValues("");
        }
        mLayoutSaveCancel.setVisibility(View.GONE);
        mFloatingButtonView.setVisibility(View.VISIBLE);

        userTracking = null;
        notifyDataSetChanged();
    }

   /* private void setSevereTextVisibleWithBackground(ViewHolder holder) {
        holder.mTextViewMild.setVisibility(View.GONE);
        holder.mTextViewNone.setVisibility(View.GONE);
        holder.mTextViewModerate.setVisibility(View.GONE);
        holder.mTextViewSevere.setVisibility(View.VISIBLE);
        holder.mTextViewDifficult.setVisibility(View.GONE);

        holder.mLayoutMild.setBackgroundColor(Color.parseColor("#bff8e9"));
        holder.mLayoutModerate.setBackgroundColor(Color.parseColor("#fdf8b8"));
        holder.mLayoutDifficult.setBackgroundColor(Color.parseColor("#fce9cb"));
        holder.mLayoutSevere.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_selected_severe));
        holder.mLayoutNone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_none_unselected_background));
    }

    private void setDifficultTextVisibleWithBackground(ViewHolder holder) {
        holder.mTextViewMild.setVisibility(View.GONE);
        holder.mTextViewSevere.setVisibility(View.GONE);
        holder.mTextViewNone.setVisibility(View.GONE);
        holder.mTextViewModerate.setVisibility(View.GONE);
        holder.mTextViewDifficult.setVisibility(View.VISIBLE);
        holder.mLayoutMild.setBackgroundColor(Color.parseColor("#bff8e9"));
        holder.mLayoutModerate.setBackgroundColor(Color.parseColor("#fdf8b8"));
        holder.mLayoutDifficult.setBackgroundColor(Color.parseColor("#f1b12b"));
        holder.mLayoutSevere.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_severe_unselected));
        holder.mLayoutNone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_none_unselected_background));
    }

    private void setModerateTextVisibleWithBackground(ViewHolder holder) {
        holder.mTextViewMild.setVisibility(View.GONE);
        holder.mTextViewNone.setVisibility(View.GONE);
        holder.mTextViewSevere.setVisibility(View.GONE);
        holder.mTextViewDifficult.setVisibility(View.GONE);
        holder.mTextViewModerate.setVisibility(View.VISIBLE);
        holder.mLayoutMild.setBackgroundColor(Color.parseColor("#bff8e9"));
        holder.mLayoutModerate.setBackgroundColor(Color.parseColor("#efdb00"));
        holder.mLayoutDifficult.setBackgroundColor(Color.parseColor("#fce9cb"));
        holder.mLayoutSevere.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_severe_unselected));
        holder.mLayoutNone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_none_unselected_background));
    }

    private void setNoneTextVisibleWithBackground(ViewHolder holder) {
        holder.mTextViewMild.setVisibility(View.GONE);
        holder.mTextViewSevere.setVisibility(View.GONE);
        holder.mTextViewNone.setVisibility(View.VISIBLE);
        holder.mTextViewModerate.setVisibility(View.GONE);
        holder.mTextViewDifficult.setVisibility(View.GONE);
        holder.mLayoutMild.setBackgroundColor(Color.parseColor("#bff8e9"));
        holder.mLayoutModerate.setBackgroundColor(Color.parseColor("#fdf8b8"));
        holder.mLayoutDifficult.setBackgroundColor(Color.parseColor("#fce9cb"));
        holder.mLayoutSevere.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_severe_unselected));
        holder.mLayoutNone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_none_selected_background));
    }

    private void setMildTextVisibleWithBackground(ViewHolder holder) {
        holder.mTextViewNone.setVisibility(View.GONE);
        holder.mTextViewSevere.setVisibility(View.GONE);
        holder.mTextViewMild.setVisibility(View.VISIBLE);
        holder.mTextViewModerate.setVisibility(View.GONE);
        holder.mTextViewDifficult.setVisibility(View.GONE);
        holder.mLayoutMild.setBackgroundColor(Color.parseColor("#06c6ad"));
        holder.mLayoutModerate.setBackgroundColor(Color.parseColor("#fdf8b8"));
        holder.mLayoutDifficult.setBackgroundColor(Color.parseColor("#fce9cb"));
        holder.mLayoutSevere.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_severe_unselected));
        holder.mLayoutNone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_none_unselected_background));
    }*/

    private void setEmptyView(ViewHolder holder) {
        holder.mLayoutMild.setBackgroundColor(Color.parseColor("#bff8e9"));
        holder.mLayoutModerate.setBackgroundColor(Color.parseColor("#fdf8b8"));
        holder.mLayoutDifficult.setBackgroundColor(Color.parseColor("#fce9cb"));
        holder.mLayoutSevere.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_severe_unselected));
        holder.mLayoutNone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_none_unselected_background));

        holder.mTextViewNone.setVisibility(View.GONE);
        holder.mTextViewMild.setVisibility(View.GONE);
        holder.mTextViewSevere.setVisibility(View.GONE);
        holder.mTextViewModerate.setVisibility(View.GONE);
        holder.mTextViewDifficult.setVisibility(View.GONE);

//        mLayoutSaveCancel.setVisibility(View.GONE);
        mFloatingButtonView.setVisibility(View.VISIBLE);

        userTracking = null;
    }

    private void openDetailActivity(int position) {
        Intent intent = new Intent(mContext, SymptomsDetailActivity.class);
        intent.putExtra(Constants.SYMPTOM_NAME, mList.get(position).getName());
        intent.putExtra(Constants.SYMPTOMS_DTO, new Gson().toJson(mList.get(position)));
        intent.putExtra(Constants.USER_DTO, new Gson().toJson(MyConditionsNewActivity.userDto));
        mContext.startActivity(intent);
    }

    private void updateSelectedColor(ViewHolder holder, int answerPosition, int position) {

        switch (answerPosition) {
            case 0:
                holder.mTextViewNone.setText(mList.get(position).getMeasurementValue().get(0));
                holder.mTextViewNone.setVisibility(View.VISIBLE);

                holder.mLayoutMild.setBackgroundColor(Color.parseColor("#bff8e9"));
                holder.mLayoutModerate.setBackgroundColor(Color.parseColor("#fdf8b8"));
                holder.mLayoutDifficult.setBackgroundColor(Color.parseColor("#fce9cb"));
                holder.mLayoutSevere.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_severe_unselected));
                holder.mLayoutNone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_none_selected_background));
                break;
            case 1:
                holder.mTextViewMild.setText(mList.get(position).getMeasurementValue().get(1));
                holder.mTextViewMild.setVisibility(View.VISIBLE);

                holder.mLayoutMild.setBackgroundColor(Color.parseColor("#06c6ad"));
                holder.mLayoutModerate.setBackgroundColor(Color.parseColor("#fdf8b8"));
                holder.mLayoutDifficult.setBackgroundColor(Color.parseColor("#fce9cb"));
                holder.mLayoutSevere.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_severe_unselected));
                holder.mLayoutNone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_none_unselected_background));
                break;
            case 2:
                holder.mTextViewModerate.setText(mList.get(position).getMeasurementValue().get(2));
                holder.mTextViewModerate.setVisibility(View.VISIBLE);

                holder.mLayoutMild.setBackgroundColor(Color.parseColor("#bff8e9"));
                holder.mLayoutModerate.setBackgroundColor(Color.parseColor("#efdb00"));
                holder.mLayoutDifficult.setBackgroundColor(Color.parseColor("#fce9cb"));
                holder.mLayoutSevere.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_severe_unselected));
                holder.mLayoutNone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_none_unselected_background));
                break;

            case 3:
                holder.mTextViewDifficult.setText(mList.get(position).getMeasurementValue().get(3));
                holder.mTextViewDifficult.setVisibility(View.VISIBLE);

                holder.mLayoutMild.setBackgroundColor(Color.parseColor("#bff8e9"));
                holder.mLayoutModerate.setBackgroundColor(Color.parseColor("#fdf8b8"));
                holder.mLayoutDifficult.setBackgroundColor(Color.parseColor("#f1b12b"));
                holder.mLayoutSevere.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_severe_unselected));
                holder.mLayoutNone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_none_unselected_background));
                break;
            case 4:
                holder.mTextViewSevere.setText(mList.get(position).getMeasurementValue().get(4));
                holder.mTextViewSevere.setVisibility(View.VISIBLE);

                holder.mLayoutMild.setBackgroundColor(Color.parseColor("#bff8e9"));
                holder.mLayoutModerate.setBackgroundColor(Color.parseColor("#fdf8b8"));
                holder.mLayoutDifficult.setBackgroundColor(Color.parseColor("#fce9cb"));
                holder.mLayoutSevere.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_selected_severe));
                holder.mLayoutNone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.layout_none_unselected_background));
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void handleDifficultClick(ViewHolder holder, int position) {
        Log.e("handleDifficultClick_log", " = " + position);

        if (mList.get(position).getSelectedMeasurementValues() == null || mList.get(position).getSelectedMeasurementValues().equals("")) {
            mList.get(position).setSelectedMeasurementValues(holder.mTextViewDifficult.getText().toString());

//            mLayoutSaveCancel.setVisibility(View.VISIBLE);
//            holder.mTextViewDifficult.setVisibility(View.VISIBLE);
            updateSelectedColor(holder, 3, position);
            rvClickListener.rv_click(position, 4, Constants.UPDATE_SYMPTOM_ADD);

        } else {

            if (mList.get(position).getSeverityLevel() != null && mList.get(position).getSeverityLevel().equals(holder.mTextViewDifficult.getText().toString())) {
                mList.get(position).setSeverityLevel("");
                setEmptyView(holder);
                notifyDataSetChanged();
            }

            if (mList.get(position).getSelectedMeasurementValues().equals(holder.mTextViewDifficult.getText().toString())) {
                mList.get(position).setSelectedMeasurementValues("");

                rvClickListener.rv_click(position, position, Constants.UPDATE_SYMPTOM_REMOVE);
            } else {
                setEmptyView(holder);
                mList.get(position).setSelectedMeasurementValues(holder.mTextViewDifficult.getText().toString());
                mList.get(position).setSeverityLevel("");
                rvClickListener.rv_click(position, 4, Constants.UPDATE_SYMPTOM_ADD);
            }

//            notifyDataSetChanged();
        }

        Log.e("updatedOnboarding_symptom_object", " " + new Gson().toJson(mList));
    }

    private void handleModerateClick(ViewHolder holder, int position) {
        Log.e("handleModerateClick_log", " = " + position);

        if (mList.get(position).getSelectedMeasurementValues() == null || mList.get(position).getSelectedMeasurementValues().equals("")) {
            mList.get(position).setSelectedMeasurementValues(holder.mTextViewModerate.getText().toString());


//            mLayoutSaveCancel.setVisibility(View.VISIBLE);
//            holder.mTextViewModerate.setVisibility(View.VISIBLE);
            updateSelectedColor(holder, 2, position);
            rvClickListener.rv_click(position, 3, Constants.UPDATE_SYMPTOM_ADD);


        } else {

            if (mList.get(position).getSeverityLevel() != null && mList.get(position).getSeverityLevel().equals(holder.mTextViewModerate.getText().toString())) {
                mList.get(position).setSeverityLevel("");
                setEmptyView(holder);
                notifyDataSetChanged();
            }

            if (mList.get(position).getSelectedMeasurementValues().equals(holder.mTextViewModerate.getText().toString())) {
                mList.get(position).setSelectedMeasurementValues("");

                rvClickListener.rv_click(position, position, Constants.UPDATE_SYMPTOM_REMOVE);

            } else {
                setEmptyView(holder);
                mList.get(position).setSelectedMeasurementValues(holder.mTextViewModerate.getText().toString());
                mList.get(position).setSeverityLevel("");
                rvClickListener.rv_click(position, 3, Constants.UPDATE_SYMPTOM_ADD);
            }

//            notifyDataSetChanged();
        }

        Log.e("updatedOnboarding_symptom_object", " " + new Gson().toJson(mList));
    }

    private void handleSevereClick(ViewHolder holder, int position) {
        Log.e("handleSevereClick_log", " = " + position);

        if (mList.get(position).getSelectedMeasurementValues() == null || mList.get(position).getSelectedMeasurementValues().equals("")) {
            mList.get(position).setSelectedMeasurementValues(holder.mTextViewSevere.getText().toString());
            updateSelectedColor(holder, 4, position);

//            mLayoutSaveCancel.setVisibility(View.VISIBLE);
//            holder.mTextViewSevere.setVisibility(View.VISIBLE);
            rvClickListener.rv_click(position, 5, Constants.UPDATE_SYMPTOM_ADD);
        } else {

            if (mList.get(position).getSeverityLevel() != null && mList.get(position).getSeverityLevel().equals(holder.mTextViewSevere.getText().toString())) {
                mList.get(position).setSeverityLevel("");
                setEmptyView(holder);
                notifyDataSetChanged();
            }

            if (mList.get(position).getSelectedMeasurementValues().equals(holder.mTextViewSevere.getText().toString())) {
                mList.get(position).setSelectedMeasurementValues("");


                rvClickListener.rv_click(position, position, Constants.UPDATE_SYMPTOM_REMOVE);

            } else {
                setEmptyView(holder);
                mList.get(position).setSelectedMeasurementValues(holder.mTextViewSevere.getText().toString());
                mList.get(position).setSeverityLevel("");
                rvClickListener.rv_click(position, 5, Constants.UPDATE_SYMPTOM_ADD);
            }
//            notifyDataSetChanged();
        }

        Log.e("updatedOnboarding_symptom_object", " " + new Gson().toJson(mList));
    }

    private void handleMildClick(ViewHolder holder, int position) {
        Log.e("handleMildClick_log", " = " + position);

        if (mList.get(position).getSelectedMeasurementValues() == null || mList.get(position).getSelectedMeasurementValues().equals("")) {
            mList.get(position).setSelectedMeasurementValues(holder.mTextViewMild.getText().toString());

            updateSelectedColor(holder, 1, position);

//            mLayoutSaveCancel.setVisibility(View.VISIBLE);
//            holder.mTextViewMild.setVisibility(View.VISIBLE);
            rvClickListener.rv_click(position, 2, Constants.UPDATE_SYMPTOM_ADD);

        } else {
            if (mList.get(position).getSeverityLevel() != null && mList.get(position).getSeverityLevel().equals(holder.mTextViewMild.getText().toString())) {
                mList.get(position).setSeverityLevel("");
                setEmptyView(holder);
                notifyDataSetChanged();
            }

            if (mList.get(position).getSelectedMeasurementValues().equals(holder.mTextViewMild.getText().toString())) {
                mList.get(position).setSelectedMeasurementValues("");


                rvClickListener.rv_click(position, position, Constants.UPDATE_SYMPTOM_REMOVE);

            } else {
                setEmptyView(holder);
                mList.get(position).setSelectedMeasurementValues(holder.mTextViewMild.getText().toString());
                mList.get(position).setSeverityLevel("");
                rvClickListener.rv_click(position, 2, Constants.UPDATE_SYMPTOM_ADD);
            }
//            notifyDataSetChanged();
        }

        Log.e("updatedOnboarding_symptom_object", " " + new Gson().toJson(mList));
    }

    private void handleNoneClick(ViewHolder holder, int position) {
        Log.e("handleNoneClick_log", " = " + position);
        if (mList.get(position).getSelectedMeasurementValues() == null || mList.get(position).getSelectedMeasurementValues().equals("")) {
            Log.e("handleNoneClick_log", " = inside if");
            mList.get(position).setSelectedMeasurementValues(holder.mTextViewNone.getText().toString());
            updateSelectedColor(holder, 0, position);

//            mLayoutSaveCancel.setVisibility(View.VISIBLE);
//            holder.mTextViewNone.setVisibility(View.VISIBLE);
            rvClickListener.rv_click(position, 1, Constants.UPDATE_SYMPTOM_ADD);

        } else {
            Log.e("handleNoneClick_log", " = inside else");
            if (mList.get(position).getSeverityLevel() != null && mList.get(position).getSeverityLevel().equals(holder.mTextViewNone.getText().toString())) {
                mList.get(position).setSeverityLevel("");
                setEmptyView(holder);
                Log.e("handleNoneClick_log", " = inside severity condition");
                notifyDataSetChanged();
            }

            if (mList.get(position).getSelectedMeasurementValues().equals(holder.mTextViewNone.getText().toString())) {
                mList.get(position).setSelectedMeasurementValues("");
                Log.e("handleNoneClick_log", " = inside measurement condition");
                rvClickListener.rv_click(position, position, Constants.UPDATE_SYMPTOM_REMOVE);

            } else {
                setEmptyView(holder);
                mList.get(position).setSelectedMeasurementValues(holder.mTextViewNone.getText().toString());
                mList.get(position).setSeverityLevel("");
                rvClickListener.rv_click(position, 1, Constants.UPDATE_SYMPTOM_ADD);

            }
//            notifyDataSetChanged();
        }

        Log.e("updatedOnboarding_symptom_object", " " + new Gson().toJson(mList));

    }


    private void setDataToTextViews(ViewHolder holder, int position) {

        Log.e("setDataToTextViews_log"," = ");

        holder.mTextViewNone.setText(mList.get(position).getMeasurementValue().get(0));
        holder.mTextViewMild.setText(mList.get(position).getMeasurementValue().get(1));
        holder.mTextViewModerate.setText(mList.get(position).getMeasurementValue().get(2));
        holder.mTextViewDifficult.setText(mList.get(position).getMeasurementValue().get(3));
        holder.mTextViewSevere.setText(mList.get(position).getMeasurementValue().get(4));
    }

    public void setRvClickListener(RvClickListener rvClickListener) {
        this.rvClickListener = rvClickListener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewMild;
        private TextView mTextViewNone;
        private TextView mTextViewSevere;
        private TextView mTextViewModerate;
        private TextView mTextViewDifficult;

        private TextView mTextViewSymptomName;
        private TextView mTextViewRecordedTime;

        private LinearLayout mLayoutNone;
        private LinearLayout mLayoutMild;
        private LinearLayout mLayoutSevere;
        private LinearLayout mLayoutModerate;
        private LinearLayout mLayoutDifficult;

        private LinearLayout mParentMain;

        public ViewHolder(View view) {
            super(view);

            mTextViewSymptomName = view.findViewById(R.id.textview_symptom_name);
            mTextViewRecordedTime = view.findViewById(R.id.textview_recorded_time);

            mTextViewMild = view.findViewById(R.id.textview_mild);
            mTextViewNone = view.findViewById(R.id.textview_none);
            mTextViewSevere = view.findViewById(R.id.textview_severe);
            mTextViewModerate = view.findViewById(R.id.textview_moderate);
            mTextViewDifficult = view.findViewById(R.id.textview_difficult);

            mLayoutNone = view.findViewById(R.id.layout_none);
            mLayoutMild = view.findViewById(R.id.layout_mild);
            mLayoutSevere = view.findViewById(R.id.layout_severe);
            mLayoutModerate = view.findViewById(R.id.layout_moderate);
            mLayoutDifficult = view.findViewById(R.id.layout_difficult);

            mParentMain = view.findViewById(R.id.layout_main);
        }
    }
}
