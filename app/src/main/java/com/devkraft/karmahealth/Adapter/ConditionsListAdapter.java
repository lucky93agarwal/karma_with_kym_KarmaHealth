package com.devkraft.karmahealth.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devkraft.karmahealth.Model.Disease;
import com.devkraft.karmahealth.Model.DiseaseDto;
import com.devkraft.karmahealth.Model.DiseaseParameterDTO;
import com.devkraft.karmahealth.Model.DiseaseParameterResponseDTO;
import com.devkraft.karmahealth.Model.SymptomsDTO;
import com.devkraft.karmahealth.R;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.devkraft.karmahealth.Screen.WebViewActivity;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.inter.RvClickListener;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequestWithoutAuth;
import com.google.gson.Gson;

import java.util.List;

public class ConditionsListAdapter extends RecyclerView.Adapter<ConditionsListAdapter.ViewHolder> implements RvClickListener {

    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;

    private Context mContext;
    private DiseaseDto mDiseaseDto;
    private List<Disease> mConditionList;
    private RvClickListener rvClickListener;
    private DiseaseParameterResponseDTO diseaseScheduleDto;

    public ConditionsListAdapter(Context context, List<Disease> conditionList, DiseaseDto diseaseDto) {
        this.mContext = context;
        this.mConditionList = conditionList;
        this.mDiseaseDto = diseaseDto;
    }

    public void setRvClickListener(RvClickListener rvClickListener) {
        this.rvClickListener = rvClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_condition_list_item, parent, false);
        return new ViewHolder(adapterView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final boolean isExpanded = position == mExpandedPosition;
        holder.mLayoutDetails.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded) {
            previousExpandedPosition = position;
            holder.mImageViewArrowUp.setVisibility(View.VISIBLE);
            holder.mImageViewArrowDown.setVisibility(View.GONE);
            if (mDiseaseDto != null && mDiseaseDto.getDiseases().get(position).getId() != null)
                callScheduledDiseaseAPI(holder, mDiseaseDto.getDiseases().get(position).getId());
            else
                showCustomConditionDescription(holder);
        } else {
            holder.mImageViewArrowUp.setVisibility(View.GONE);
            holder.mImageViewArrowDown.setVisibility(View.VISIBLE);
        }

        holder.mRelativeLayoutHeader.setOnClickListener(v -> handleHeaderClick(isExpanded, position));

        if (mConditionList.get(position).getOrganType() != null) {
            if (mConditionList.get(position).getOrganType().equalsIgnoreCase(Constants.CELLS)) {
                holder.mImageViewOrganType.setImageResource(R.drawable.ic_cells_icon);
            } else if (mConditionList.get(position).getOrganType().equalsIgnoreCase(Constants.INTESTINE)) {
                holder.mImageViewOrganType.setImageResource(R.drawable.ic_intestine_icon);
            } else if (mConditionList.get(position).getOrganType().equalsIgnoreCase(Constants.KIDNEY)) {
                holder.mImageViewOrganType.setImageResource(R.drawable.ic_kidney_icon);
            } else if (mConditionList.get(position).getOrganType().equalsIgnoreCase(Constants.OBESITY)) {
                holder.mImageViewOrganType.setImageResource(R.drawable.ic_obesity_icon);
            } else if (mConditionList.get(position).getOrganType().equalsIgnoreCase(Constants.PANCREAS)) {
                holder.mImageViewOrganType.setImageResource(R.drawable.ic_pancreas_icon);
            } else if (mConditionList.get(position).getOrganType().equalsIgnoreCase(Constants.THYROID)) {
                holder.mImageViewOrganType.setImageResource(R.drawable.ic_thyroid_icon);
            } else if (mConditionList.get(position).getOrganType().equalsIgnoreCase(Constants.UTERUS)) {
                holder.mImageViewOrganType.setImageResource(R.drawable.ic_uterus_icon);
            } else {
                holder.mImageViewOrganType.setImageResource(R.drawable.ic_vitruvian_icon_other);
            }
        }

        if (mConditionList.get(position).getInformationLink() != null)
            holder.mLayoutReadMore.setVisibility(View.VISIBLE);
        else
            holder.mLayoutReadMore.setVisibility(View.INVISIBLE);

        holder.mTextViewConditionName.setText("" + mConditionList.get(position).getName());
        holder.mTextViewAddedOn.setText(mContext.getString(R.string.added_on_text) + " " + AppUtils.convertDateInMMDDYYYY(AppUtils.getLongToStringDate(mConditionList.get(position).getCreatedDate())));

        holder.mLayoutReadMore.setOnClickListener(v -> openWebViewActivityForInfo(position));
        holder.mLayoutDelete.setOnClickListener(v -> rvClickListener.rv_click(position, position, Constants.DELETE));
    }

    private void showCustomConditionDescription(ViewHolder holder) {
        holder.mLayoutEmptyHealth.setVisibility(View.VISIBLE);
        holder.mLayoutEmptySymptom.setVisibility(View.VISIBLE);
        holder.getmLayoutEmptyCheckups.setVisibility(View.VISIBLE);
    }

    private void handleHeaderClick(boolean isExpanded, int position) {
        mExpandedPosition = isExpanded ? -1 : position;
        notifyItemChanged(previousExpandedPosition);
        notifyItemChanged(position);
    }

    private void openWebViewActivityForInfo(int position) {
        if (mConditionList.get(position).getInformationLink() != null && mContext != null) {
            Intent intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra(Constants.TITLE, mConditionList.get(position).getName());
            intent.putExtra(Constants.URL, mConditionList.get(position).getInformationLink());
            mContext.startActivity(intent);
        }
    }

    private void callScheduledDiseaseAPI(ViewHolder holder, Long id) {
        GenericRequestWithoutAuth<DiseaseParameterResponseDTO> getScheduleRequest = new GenericRequestWithoutAuth<>
                (Request.Method.GET, APIUrls.get().getDiseaseSchedule(id),
                        DiseaseParameterResponseDTO.class, null,
                        diseaseParameterResponseDTO -> {
                            diseaseScheduleDto = diseaseParameterResponseDTO;
                            Log.e("diseaseScheduleDto_log", " = " + new Gson().toJson(diseaseScheduleDto));
                            saveToPreference(diseaseScheduleDto);
                            setHealtMatricsList(holder, diseaseScheduleDto.getVitals(), mDiseaseDto);
                            setCheckupsList(holder, diseaseScheduleDto.getTests(), mDiseaseDto);
                            setSymptomsList(holder, diseaseScheduleDto.getSymptoms(), mDiseaseDto);
                        },
                        error -> {
                            String res = AppUtils.getVolleyError(mContext, error);
                            AppUtils.openSnackBar(holder.mLayoutMain, res);
                        });
        ApiService.get().addToRequestQueue(getScheduleRequest);
    }

    private void setSymptomsList(ViewHolder holder, List<SymptomsDTO> symptoms, DiseaseDto mDiseaseDto) {
        if (!symptoms.isEmpty()) {
            holder.mLayoutEmptySymptom.setVisibility(View.GONE);
            holder.mRecyclerViewSymptoms.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            holder.mRecyclerViewSymptoms.setLayoutManager(linearLayoutManager);
            SymptomConditionDetailsListAdapter adapter = new SymptomConditionDetailsListAdapter(symptoms, mDiseaseDto);
            holder.mRecyclerViewSymptoms.setAdapter(adapter);
            adapter.setRvClickListener(this);
        } else {
            holder.mRecyclerViewSymptoms.setVisibility(View.GONE);
            holder.mLayoutEmptySymptom.setVisibility(View.VISIBLE);
        }
    }

    private void saveToPreference(DiseaseParameterResponseDTO diseaseParameterResponseDTO) {
        if (diseaseParameterResponseDTO != null) {
            Gson gson = new Gson();
            String diseaseParameterResponse = gson.toJson(diseaseParameterResponseDTO);
            ApplicationPreferences.get().saveStringValue(Constants.DISEASE_PARAMETER_RESPONSE, diseaseParameterResponse);
        }
    }

    private void setCheckupsList(ViewHolder holder, List<DiseaseParameterDTO> mCheckupList, DiseaseDto mDiseaseDto) {
        if (!mCheckupList.isEmpty()) {
            holder.mRecyclerViewCheckUps.setVisibility(View.VISIBLE);
            holder.getmLayoutEmptyCheckups.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            holder.mRecyclerViewCheckUps.setLayoutManager(linearLayoutManager);
            ConditionDetailsCheckupsListAdapter adapter = new ConditionDetailsCheckupsListAdapter(mCheckupList, mDiseaseDto);
            holder.mRecyclerViewCheckUps.setAdapter(adapter);
            adapter.setRvClickListener(this);
        } else {
            holder.mRecyclerViewCheckUps.setVisibility(View.GONE);
            holder.getmLayoutEmptyCheckups.setVisibility(View.VISIBLE);
        }
    }

    private void setHealtMatricsList(ViewHolder holder, List<DiseaseParameterDTO> mHealthMatricsList, DiseaseDto mDiseaseDto) {
        if (!mHealthMatricsList.isEmpty()) {
            holder.mLayoutEmptyHealth.setVisibility(View.GONE);
            holder.mRecyclerViewHealthMartics.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            holder.mRecyclerViewHealthMartics.setLayoutManager(linearLayoutManager);
            ConditionDetailsHealthMatricsListAdapter adapter =
                    new ConditionDetailsHealthMatricsListAdapter(mContext, mHealthMatricsList, mDiseaseDto);
            holder.mRecyclerViewHealthMartics.setAdapter(adapter);
            adapter.setRvClickListener(this);
        } else {
            holder.mLayoutEmptyHealth.setVisibility(View.VISIBLE);
            holder.mRecyclerViewHealthMartics.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mConditionList.size();
    }

    @Override
    public void rv_click(int position, int value, String key) {
        if (key.equals(Constants.DELETE_HEALTH_MATRICS)) {
            rvClickListener.rv_click(position, position, Constants.DELETE_HEALTH_MATRICS);
        } else if (key.equals(Constants.ADD_HEALTH_MATRICS)) {
            rvClickListener.rv_click(position, position, Constants.ADD_HEALTH_MATRICS);
        } else if (key.equals(Constants.ADD_CHECKUPS)) {
            rvClickListener.rv_click(position, position, Constants.ADD_CHECKUPS);
        } else if (key.equals(Constants.DELETE_CHECKUPS)) {
            rvClickListener.rv_click(position, position, Constants.DELETE_CHECKUPS);
        } else if (key.equalsIgnoreCase(Constants.ADD_SYMPTOMS)) {
            rvClickListener.rv_click(position, position, Constants.ADD_SYMPTOMS);
        } else if (key.equalsIgnoreCase(Constants.REMOVE_SYMPTOMS)) {
            rvClickListener.rv_click(position, position, Constants.REMOVE_SYMPTOMS);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageViewArrowUp;
        private ImageView mImageViewArrowDown;
        private ImageView mImageViewOrganType;

        private TextView mTextViewAddedOn;
        private TextView mTextViewConditionName;

        private LinearLayout mLayoutMain;
        private LinearLayout mLayoutDelete;
        private LinearLayout mLayoutDetails;
        private LinearLayout mLayoutReadMore;
        private RelativeLayout mRelativeLayoutHeader;

        private RecyclerView mRecyclerViewCheckUps;
        private RecyclerView mRecyclerViewHealthMartics;
        private RecyclerView mRecyclerViewSymptoms;

        private LinearLayout mLayoutEmptyHealth;
        private LinearLayout mLayoutEmptySymptom;
        private LinearLayout getmLayoutEmptyCheckups;

        public ViewHolder(View view) {
            super(view);

            mLayoutMain = view.findViewById(R.id.layout_main);
            mLayoutDelete = view.findViewById(R.id.layout_delete);
            mLayoutReadMore = view.findViewById(R.id.layout_read_more);
            mTextViewAddedOn = view.findViewById(R.id.textview_added_on);
            mRelativeLayoutHeader = view.findViewById(R.id.layout_header);
            mImageViewArrowUp = view.findViewById(R.id.imageview_arrow_up);
            mLayoutDetails = view.findViewById(R.id.layout_condition_details);
            mImageViewArrowDown = view.findViewById(R.id.imageview_arrow_down);
            mImageViewOrganType = view.findViewById(R.id.imageview_organ_type);
            mLayoutEmptyHealth = view.findViewById(R.id.layout_empty_view_health);
            mLayoutEmptySymptom = view.findViewById(R.id.layout_empty_view_symptoms);
            mTextViewConditionName = view.findViewById(R.id.text_view_condition_name);
            getmLayoutEmptyCheckups = view.findViewById(R.id.layout_empty_view_checkup);
            mRecyclerViewSymptoms = view.findViewById(R.id.recyclerview_condition_symptoms);
            mRecyclerViewCheckUps = view.findViewById(R.id.recyclerview_condition_checkups);
            mRecyclerViewHealthMartics = view.findViewById(R.id.recyclerview_condition_health_matrics);
        }
    }
}
