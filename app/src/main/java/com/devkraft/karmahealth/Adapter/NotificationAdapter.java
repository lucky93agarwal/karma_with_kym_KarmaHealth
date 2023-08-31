package com.devkraft.karmahealth.Adapter;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.Notification;
import com.devkraft.karmahealth.Model.NotificationsDto;
import com.devkraft.karmahealth.Model.OnDeleteNotification;
import com.devkraft.karmahealth.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devkraft.karmahealth.Screen.NotificationActivity;
import com.devkraft.karmahealth.Screen.NotificationDetailActivity;
import com.devkraft.karmahealth.Screen.WebViewActivity;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<Notification> {

    private List<Notification> list;
    private Context context;
    private LayoutInflater inflater;
    private NotificationActivity.UpdateUnreadNotification updateUnreadNotification;
    private OnDeleteNotification mOnDeleteNotification;

    public NotificationAdapter(Context context, int resource, List<Notification> list, NotificationActivity.UpdateUnreadNotification updateUnreadNotification) {
        super(context, resource, list);
        this.list = list;
        this.context = context;
        this.updateUnreadNotification = updateUnreadNotification;
        inflater = LayoutInflater.from(context);
        this.mOnDeleteNotification = (OnDeleteNotification) context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.all_notification_list, null);
            holder = new ViewHolder();

            holder.title = convertView.findViewById(R.id.title);
            holder.subTitle = convertView.findViewById(R.id.subTitle);
            holder.date = convertView.findViewById(R.id.date);
            holder.parentLayout = convertView.findViewById(R.id.parentLayout);
            holder.name = convertView.findViewById(R.id.name);
            holder.deleteIv = convertView.findViewById(R.id.deleteIv);
            holder.headerTv = convertView.findViewById(R.id.headerTv);
            holder.vrLine = convertView.findViewById(R.id.vr_line);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Notification item = getItem(position);

        holder.title.setText(item.getTitle());
        holder.subTitle.setText(AppUtils.getHtmlString(item.getBody()));
        holder.date.setText(AppUtils.getStringDateForNotification(item.getCreatedDate()));
        holder.name.setText(AppUtils.getInitialFromName(item.getUserName()));
        holder.headerTv.setText(item.getHeader());

        String colorCode = item.getNotificationColorCode();

        if (colorCode != null) {
            if (colorCode.equalsIgnoreCase(Constants.BLUE)) {
                holder.vrLine.setBackgroundColor(ContextCompat.getColor(context, R.color.noti_blue));
            } else if (colorCode.equalsIgnoreCase(Constants.PURPLE)) {
                holder.vrLine.setBackgroundColor(ContextCompat.getColor(context, R.color.noti_purple));
            } else if (colorCode.equalsIgnoreCase(Constants.PINK)) {
                holder.vrLine.setBackgroundColor(ContextCompat.getColor(context, R.color.noti_pink));
            } else if (colorCode.equalsIgnoreCase(Constants.RED)) {
                holder.vrLine.setBackgroundColor(ContextCompat.getColor(context, R.color.noti_red));
            } else if (colorCode.equalsIgnoreCase(Constants.YELLOW)) {
                holder.vrLine.setBackgroundColor(ContextCompat.getColor(context, R.color.noti_yellow));
            } else if (colorCode.equalsIgnoreCase(Constants.ORANGE)) {
                holder.vrLine.setBackgroundColor(ContextCompat.getColor(context, R.color.noti_orange));
            } else {
                holder.vrLine.setBackgroundColor(ContextCompat.getColor(context, R.color.hrLine));
            }
        }


        if (item.isRead()) {
            //holder.parentLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.white));

            Typeface lighttypeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Muli-Light.ttf");

            Typeface regulartypeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Muli-Regular.ttf");

            holder.title.setTypeface(regulartypeface);
            holder.subTitle.setTypeface(lighttypeface);

        } else {
            //holder.parentLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.unread_notification));

            Typeface semiboldtypeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Muli-SemiBold.ttf");

            Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Muli-Bold.ttf");

            holder.title.setTypeface(boldtypeface);
            holder.subTitle.setTypeface(semiboldtypeface);
        }

        final ViewHolder finalHolder = holder;
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.logEvent(Constants.NOTIFICATION_SCR_NOTIFICATION_CLK);
                if (!item.isRead()) {
                    callNotificationReadAPI(finalHolder, item);
                }
                Intent notificationIntent = new Intent(getContext(), NotificationDetailActivity.class);
                notificationIntent.putExtra(Constants.TITLE, item.getTitle());
                notificationIntent.putExtra(Constants.SUB_TITLE, item.getBody());
                getContext().startActivity(notificationIntent);
                if (item.getUrl() != null) {
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra(Constants.TITLE, item.getTitle());
                    intent.putExtra(Constants.URL, item.getUrl());
                    getContext().startActivity(intent);
                }
            }
        });

        final ViewHolder finalHolder1 = holder;
        holder.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogBox(item, finalHolder1);
            }
        });

        return convertView;
    }

    private void showDialogBox(final Notification item, final ViewHolder finalHolder1) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.delete_drug_dialog_box, null);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme1);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptView);

        TextView title = promptView.findViewById(R.id.title);
        title.setText(context.getString(R.string.are_you_sure_you_want_remove_notification));

        TextView msg = promptView.findViewById(R.id.msg);
        msg.setVisibility(View.GONE);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(context.getString(R.string.ok), (dialog, id) -> callDeleteNotificationAPI(item, finalHolder1));

        alertDialogBuilder.setCancelable(false)
                .setNegativeButton(context.getString(R.string.cancel), (dialog, id) -> dialog.dismiss());

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void callDeleteNotificationAPI(final Notification item, final ViewHolder finalHolder) {

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(getContext());
        GenericRequest<APIMessageResponse> deleteNotificationRequest = new GenericRequest<APIMessageResponse>
                (Request.Method.DELETE, APIUrls.get().getDeleteNotification(item.getId()),
                        APIMessageResponse.class, null,
                        new Response.Listener<APIMessageResponse>() {
                            @Override
                            public void onResponse(APIMessageResponse apiMessageResponse) {
                                authExpiredCallback.hideProgressBar();
                                ApplicationDB.get().deleteNotification(item.getId());
                                if (!item.isRead()) {
                                    ApplicationPreferences.get().setNotificationCount(ApplicationPreferences.get().getNotificationCount()  - 1);
                                }
                                updateUnreadNotification.updateUnreadNotifications();

                                list.remove(item);
                                mOnDeleteNotification.onDeleteNotification();
                                notifyDataSetChanged();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                authExpiredCallback.hideProgressBar();
                                String res = AppUtils.getVolleyError(getContext(), error, authExpiredCallback);
                                AppUtils.openSnackBar(finalHolder.parentLayout, res);
                            }
                        });
        authExpiredCallback.setRequest(deleteNotificationRequest);
        ApiService.get().addToRequestQueue(deleteNotificationRequest);
    }

    private void callNotificationReadAPI(final ViewHolder finalHolder, final Notification notification) {
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(getContext());
        GenericRequest<NotificationsDto> updateNotificationRequest = new GenericRequest<NotificationsDto>
                (Request.Method.PUT, APIUrls.get().getupdateNotificationStatus(notification.getId()),
                        NotificationsDto.class, null,
                        new Response.Listener<NotificationsDto>() {
                            @Override
                            public void onResponse(NotificationsDto notificationsDto) {
                                authExpiredCallback.hideProgressBar();
                                notification.setRead(true);
                                ApplicationPreferences.get().setNotificationCount(ApplicationPreferences.get().getNotificationCount()  - 1);
                                ApplicationDB.get().changeNotificationReadStatus(notification.getId(),true);
                                updateUnreadNotification.updateUnreadNotifications();
                                notifyDataSetChanged();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                authExpiredCallback.hideProgressBar();
                                String res = AppUtils.getVolleyError(getContext(), error, authExpiredCallback);
                                AppUtils.openSnackBar(finalHolder.parentLayout, res);
                            }
                        });
        authExpiredCallback.setRequest(updateNotificationRequest);
        ApiService.get().addToRequestQueue(updateNotificationRequest);
    }

    @Override
    public int getCount() {
        if (this.list != null && this.list.size() > 0) {
            return this.list.size();
        } else {
            return 0;
        }
    }

    public void updateList(List<Notification> notificationList) {
        if(list != null){
            list.addAll(notificationList);
            notifyDataSetChanged();
        }
    }

    private class ViewHolder {
        private TextView title;
        private TextView subTitle;
        private TextView date;
        private RelativeLayout parentLayout;
        private TextView name;
        private ImageView deleteIv;
        private TextView headerTv;
        private View vrLine;
    }
}
