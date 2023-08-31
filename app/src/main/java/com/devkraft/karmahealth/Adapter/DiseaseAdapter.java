package com.devkraft.karmahealth.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.DiseaseParameterDTO;
import com.devkraft.karmahealth.Model.DiseaseToggleDto;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.R;
import androidx.appcompat.widget.SwitchCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;

import java.util.List;

public class DiseaseAdapter extends ArrayAdapter<DiseaseParameterDTO> {

    private List<DiseaseParameterDTO> list;
    private Context context;
    private LayoutInflater inflater;
    private boolean isProgrammatically;
    private UserDto userDto;
    private boolean isAddDisease;

    public DiseaseAdapter(Context context, int resource, List<DiseaseParameterDTO> list,
                          UserDto userDto, boolean isAddDisease) {
        super(context, resource, list);
        this.list = list;
        this.context = context;
        this.userDto = userDto;
        this.isAddDisease = isAddDisease;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.disease_list, null);
            holder = new ViewHolder();

            holder.name = convertView.findViewById(R.id.name);
            holder.frequency = convertView.findViewById(R.id.frequency);
            holder.notificationSc = convertView.findViewById(R.id.notification_switch_compat);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DiseaseParameterDTO item = getItem(position);

        holder.name.setText(item.getParameterName());
        holder.frequency.setText(item.getParameterFrequency());

        isProgrammatically = true;
        //holder.notificationSc.setChecked(item.isShortTermFlag());
        isProgrammatically = false;

        final ViewHolder finalHolder = holder;
        holder.notificationSc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isSelected) {
                if (!isProgrammatically) {
                    callChangedFlagAPI(finalHolder,isSelected,item.getParameterId());
                }
            }
        });

        if (isAddDisease) {
            holder.notificationSc.setVisibility(View.GONE);
        }else {
            holder.notificationSc.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private void callChangedFlagAPI(final ViewHolder finalHolder, final boolean isSelected, Long parameterId) {
        DiseaseToggleDto diseaseToggleDto = new DiseaseToggleDto();
        diseaseToggleDto.setStatus(isSelected);

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(getContext());
        GenericRequest<APIMessageResponse> diseaseRemindRequest = new GenericRequest<APIMessageResponse>
                (Request.Method.PUT, APIUrls.get().getDiseaseReminder(userDto.getId(),parameterId),
                        APIMessageResponse.class, diseaseToggleDto,
                        new Response.Listener<APIMessageResponse>() {
                            @Override
                            public void onResponse(APIMessageResponse apiMessageResponse) {
                                authExpiredCallback.hideProgressBar();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                authExpiredCallback.hideProgressBar();
                                String res = AppUtils.getVolleyError(getContext(), error, authExpiredCallback);
                                isProgrammatically = true;
                                finalHolder.notificationSc.setChecked(!isSelected);
                                isProgrammatically = false;
                                Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
                            }
                        });

        authExpiredCallback.setRequest(diseaseRemindRequest);
        ApiService.get().addToRequestQueue(diseaseRemindRequest);


    }

    @Override
    public int getCount() {
        if (this.list != null && this.list.size() > 0) {
            return this.list.size();
        } else {
            return 0;
        }
    }

    private class ViewHolder {
        private TextView name;
        private TextView frequency;
        private SwitchCompat notificationSc;
    }
}
