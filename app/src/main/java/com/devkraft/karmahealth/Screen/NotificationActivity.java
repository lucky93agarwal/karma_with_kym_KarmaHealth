package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.android.volley.Request;
import com.clevertap.android.sdk.CTInboxListener;
import com.clevertap.android.sdk.CTInboxStyleConfig;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.InboxMessageButtonListener;
import com.devkraft.karmahealth.Adapter.NotificationAdapter;
import com.devkraft.karmahealth.Model.Notification;
import com.devkraft.karmahealth.Model.NotificationsDto;
import com.devkraft.karmahealth.Model.OnDeleteNotification;
import com.devkraft.karmahealth.R;
import com.devkraft.karmahealth.Utils.APIUrls;
import com.devkraft.karmahealth.Utils.AppUtils;
import com.devkraft.karmahealth.Utils.ApplicationPreferences;
import com.devkraft.karmahealth.Utils.Constants;
import com.devkraft.karmahealth.Utils.ProgressDialogSetup;
import com.devkraft.karmahealth.api.AuthExpiredCallback;
import com.devkraft.karmahealth.db.ApplicationDB;
import com.devkraft.karmahealth.net.ApiService;
import com.devkraft.karmahealth.net.GenericRequest;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationActivity extends BaseActivity implements OnDeleteNotification, CTInboxListener, InboxMessageButtonListener {
    private NotificationAdapter mNotificationAdapter;
    private ProgressDialogSetup mProgressDialogSetup;
    private TextView showMoreTv, unreadCountTv;
    private final int notificationCount = 0;
    private boolean isDeleteRequired = true, mIsLoading = false, isApiCalled = false, mIsApiCalledFirstTime = true;

    private int mTotalCount = 10;
    private int mCartItemCount;
    private boolean isOpenCleveTap = false;

    private Button readAllBtn;
    private MenuItem menuItem;

    private RelativeLayout parentLayout;
    private ListView mNotificationListView;
    private LinearLayout readAllNotificationRl;
    private List<Notification> mNotificationList;
    private CleverTapAPI cleverTapDefaultInstance;
    private TextView mTextViewCleverTapNotificationCount;

    private Context mContext;
    private TabLayout mTabLayout;

    private UiModeManager uiModeManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);



        mContext = NotificationActivity.this;

        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);

        setUpToolbar();
        intIds();
        handleIntentData(getIntent());
        AppUtils.logEvent(Constants.NOTIFICATION_SCR_OPENED);
        callNotificationAPI(notificationCount, false);
        AppUtils.callGetUserSubscriptionAPI(this, parentLayout);
        handleClickEvents();
        cleverTapAppInboxInitialization();
        setTabLayouController();
     //   Intercom.client().setLauncherVisibility(Intercom.Visibility.GONE);
        AppUtils.logCleverTapEvent(this, Constants.Inbox_Screen_Opened, null);

        setupViewPagerBadge();
    }
    private void setupViewPagerBadge() {
        //set the badge
        BadgeDrawable badgeDrawable = mTabLayout.getTabAt(1).getOrCreateBadge();
        badgeDrawable.setNumber(mCartItemCount);
        if (mCartItemCount > 0) {
            badgeDrawable.setVisible(true);
        } else {
            badgeDrawable.setVisible(false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        mTabLayout.selectTab(mTabLayout.getTabAt(0));
    }

    private void setTabLayouController() {
        mTabLayout.addTab(mTabLayout.newTab().setText("Alerts"));
        mTabLayout.addTab(mTabLayout.newTab().setText("Messages"));

        setTabBG(R.drawable.tab_alert_selected, R.drawable.tab_message_unselected);
    }

    private void handleIntentData(Intent intent) {
        if (intent != null) {
            isOpenCleveTap = intent.getBooleanExtra("cleverTapInbox", false);

            if (isOpenCleveTap) {
                openCleverTapInbox();
            }
        }
    }

    private void handleClickEvents() {
        mNotificationListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mNotificationListView.getAdapter() == null) {
                    return;
                }
                if (mNotificationListView.getAdapter().getCount() == 0) {
                    return;
                }

                if (mNotificationListView.getAdapter().getCount() >= mTotalCount) {
                    int l = visibleItemCount + firstVisibleItem;
                    if (l >= totalItemCount && !mIsLoading) {
                        mIsLoading = true;
                        callNotificationAPI(notificationCount, true);
                        showMoreTv.setVisibility(View.GONE);
                    } else {
                        showMoreTv.setVisibility(View.GONE);
                    }
                }
            }
        });

        readAllBtn.setOnClickListener(view -> {
            AppUtils.logEvent(Constants.NOTIFICATION_SCR_READ_ALL_BTN_CLK);

            if (mProgressDialogSetup != null && !isFinishing()) {
                mProgressDialogSetup.show();
            }

            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(NotificationActivity.this);
            GenericRequest<NotificationsDto> readNotificationRequest = new GenericRequest<>
                    (Request.Method.PUT, APIUrls.get().getMarkAllRead(),
                            NotificationsDto.class, null,
                            notificationsDto -> {
                                AppUtils.hideProgressBar(mProgressDialogSetup);
                             //   ApplicationDB.get().updateNotificationReadCount();
                                ApplicationPreferences.get().setNotificationCount(0);
                                updateAdapter();
                            },
                            error -> {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(mProgressDialogSetup);
                                String res = AppUtils.getVolleyError(NotificationActivity.this, error, authExpiredCallback);
                                AppUtils.openSnackBar(parentLayout, res);
                            });
            authExpiredCallback.setRequest(readNotificationRequest);
            ApiService.get().addToRequestQueue(readNotificationRequest);
        });


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mTabLayout.getSelectedTabPosition() == 0) {
                    setTabBG(R.drawable.tab_alert_selected, R.drawable.tab_message_unselected);
                    AppUtils.logCleverTapEvent(mContext, Constants.Clicked_On_Messages_Tab_On_Inbox_Screen, null);
                } else {
                    setTabBG(R.drawable.tab_alert_unselect, R.drawable.tab_message_selected);
                    openCleverTapInbox();
                    AppUtils.logCleverTapEvent(mContext, Constants.Clicked_On_Notification_Tab_On_Inbox_Screen, null);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void callNotificationAPI(int notificationNumber, boolean isFromUpdate) {
        if (mIsApiCalledFirstTime) {
            mProgressDialogSetup.show();
        }

        if (mNotificationAdapter != null) {
            notificationNumber = mNotificationAdapter.getCount();
        } else {
            notificationNumber = 0;
        }

        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(NotificationActivity.this);
        GenericRequest<NotificationsDto> notificationRequest = new GenericRequest<>
                (Request.Method.GET, APIUrls.get().getAllNotification(notificationNumber),
                        NotificationsDto.class, null,
                        notificationsDto -> {
                            authExpiredCallback.hideProgressBar();
                            AppUtils.hideProgressBar(mProgressDialogSetup);
                            if (notificationsDto != null) {
                                List<Notification> notificationList = notificationsDto.getNotification();
                                if (notificationList != null && notificationList.size() > 0) {
                                 //   ApplicationDB.get().upsertNotifications(notificationsDto.getNotification(), isDeleteRequired);
                                    ApplicationPreferences.get().setNotificationList(notificationsDto.getNotification());
                                    if (mIsApiCalledFirstTime) {
                                        ApplicationPreferences.get().setNotificationCount(notificationsDto.getUnreadCount());
                                    }
                                    isApiCalled = true;
                                    if (isFromUpdate) {
                                        mNotificationAdapter.updateList(notificationList);
                                        updateUnreadMessage();
                                        mIsLoading = false;
                                    } else {
                                        updateAdapter();
                                    }
                                    isDeleteRequired = false;
                                    mIsApiCalledFirstTime = false;
                                }
                            }
                        },
                        error -> {
                            if (!isFinishing())
                                authExpiredCallback.hideProgressBar();
                            AppUtils.hideProgressBar(mProgressDialogSetup);
                            String res = AppUtils.getVolleyError(NotificationActivity.this, error, authExpiredCallback);
                            AppUtils.openSnackBar(parentLayout, res);
                        });
        authExpiredCallback.setRequest(notificationRequest);
        ApiService.get().addToRequestQueue(notificationRequest);
    }

    private void updateAdapter() {
        updateUnreadMessage();
        mNotificationList = ApplicationDB.get().getNotifications();
        mNotificationAdapter = new NotificationAdapter(NotificationActivity.this,
                R.layout.all_notification_list, mNotificationList, () -> updateUnreadMessage());

        mNotificationListView.setAdapter(mNotificationAdapter);
    }

    @Override
    public void onInboxButtonClick(HashMap<String, String> payload) {
        Log.e("Payload = ", "" + payload.get("screenName"));
        if (payload != null) {
            //Read the values

            if (payload.get("screenName").equalsIgnoreCase("Intercom_Messenger")) {
                //Intercom.client().displayMessenger();
            }
        }
    }

    public interface UpdateUnreadNotification {
        void updateUnreadNotifications();
    }

    private void intIds() {
        mProgressDialogSetup = ProgressDialogSetup.getProgressDialog(NotificationActivity.this);
        mNotificationListView = findViewById(R.id.notificationList);
        parentLayout = findViewById(R.id.activity_notification);
        showMoreTv = findViewById(R.id.showMore);
        LinearLayout emptyView = findViewById(R.id.emptyView);
        mNotificationListView.setEmptyView(emptyView);
        readAllNotificationRl = findViewById(R.id.readAllNotificationRl);
        readAllBtn = findViewById(R.id.readAllBtn);
        unreadCountTv = findViewById(R.id.unreadCountTv);

        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);


        mTabLayout = findViewById(R.id.tab_layout);

        updateUnreadMessage();
    }

    private void updateUnreadMessage() {
        int unreadCount = ApplicationPreferences.get().getNotificationCount();
        if (unreadCount != 0 && isApiCalled) {
            readAllNotificationRl.setVisibility(View.VISIBLE);
            if (unreadCount == 1) {
                unreadCountTv.setText(unreadCount + getString(R.string.space) + getString(R.string.unread_notification));
            } else {
                unreadCountTv.setText(unreadCount + getString(R.string.space) + getString(R.string.unread_notifications));
            }

        } else {
            readAllNotificationRl.setVisibility(View.GONE);
        }
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setTitle(getString(R.string.inbox));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColor));
    }

    private void openCleverTapInbox() {
        CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
//      styleConfig.setTabs(tabs);//Do not use this if you don't want to use tabs
        styleConfig.setTabBackgroundColor("#00b398");
        styleConfig.setSelectedTabIndicatorColor("#414141");
        styleConfig.setSelectedTabColor("#FFFFFF");
        styleConfig.setUnselectedTabColor("#414141");
        styleConfig.setBackButtonColor("#00b398");
        styleConfig.setNavBarTitleColor("#414141");
        styleConfig.setNavBarTitle("Messages");
        styleConfig.setNavBarColor("#FFFFFF");
        styleConfig.setInboxBackgroundColor("#FFFFFF");
        if (cleverTapDefaultInstance != null) {
            cleverTapDefaultInstance.showAppInbox(styleConfig); //With Tabs
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clevertap_notification_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = menuItem.getActionView();
        actionView.setVisibility(View.GONE);
        mTextViewCleverTapNotificationCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_cart: {
                // Do something
                openCleverTapInbox();
                return true;
            }

            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if (mTextViewCleverTapNotificationCount != null) {
            if (mCartItemCount == 0) {
                if (mTextViewCleverTapNotificationCount.getVisibility() != View.GONE) {
                    mTextViewCleverTapNotificationCount.setVisibility(View.GONE);
                }
            } else {
                mTextViewCleverTapNotificationCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (mTextViewCleverTapNotificationCount.getVisibility() != View.VISIBLE) {
                    mTextViewCleverTapNotificationCount.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mProgressDialogSetup != null)
            mProgressDialogSetup.dismiss();

        ApplicationPreferences.get().setNotificationList(null);
    }

    @Override
    public void onDeleteNotification() {
        mTotalCount = mNotificationList.size() - 1;
    }


    private void cleverTapAppInboxInitialization() {
        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);
        if (cleverTapDefaultInstance != null) {
            //Set the Notification Inbox Listener
            cleverTapDefaultInstance.setCTNotificationInboxListener(this);
            //Initialize the inbox and wait for callbacks on overridden methods
            cleverTapDefaultInstance.initializeInbox();
            mCartItemCount = cleverTapDefaultInstance.getInboxMessageUnreadCount();
        }
    }

    // CleverTap App Inbox implementation methods
    @Override
    public void inboxDidInitialize() {
        mTextViewCleverTapNotificationCount.setOnClickListener(v -> {
            ArrayList<String> tabs = new ArrayList<>();
            tabs.add("Notification");
            tabs.add("Messages");//We support upto 2 tabs only. Additional tabs will be ignored

            CTInboxStyleConfig styleConfig = new CTInboxStyleConfig();
            styleConfig.setTabs(tabs);//Do not use this if you don't want to use tabs
            styleConfig.setTabBackgroundColor("#00b398");
            styleConfig.setSelectedTabIndicatorColor("#414141");
            styleConfig.setSelectedTabColor("#FFFFFF");
            styleConfig.setUnselectedTabColor("#414141");
            styleConfig.setBackButtonColor("#00b398");
            styleConfig.setNavBarTitleColor("#414141");
            styleConfig.setNavBarTitle("App INBOX");
            styleConfig.setNavBarColor("#FFFFFF");
            styleConfig.setInboxBackgroundColor("#FFFFFF");
            if (cleverTapDefaultInstance != null) {
                cleverTapDefaultInstance.showAppInbox(styleConfig); //With Tabs
            }
            //ct.showAppInbox();//Opens Activity with default style configs
        });

    }

    @Override
    public void inboxMessagesDidUpdate() {
        cleverTapDefaultInstance.getAllInboxMessages();
    }

    @SuppressLint("ObsoleteSdkInt")
    private void setTabBG(int tab1, int tab2) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ViewGroup tabStrip = (ViewGroup) mTabLayout.getChildAt(0);
            View tabView1 = tabStrip.getChildAt(0);
            View tabView2 = tabStrip.getChildAt(1);
            if (tabView1 != null) {
                int paddingStart = tabView1.getPaddingStart();
                int paddingTop = tabView1.getPaddingTop();
                int paddingEnd = tabView1.getPaddingEnd();
                int paddingBottom = tabView1.getPaddingBottom();
                ViewCompat.setBackground(tabView1, AppCompatResources.getDrawable(tabView1.getContext(), tab1));
                ViewCompat.setPaddingRelative(tabView1, paddingStart, paddingTop, paddingEnd, paddingBottom);
            }

            if (tabView2 != null) {
                int paddingStart = tabView2.getPaddingStart();
                int paddingTop = tabView2.getPaddingTop();
                int paddingEnd = tabView2.getPaddingEnd();
                int paddingBottom = tabView2.getPaddingBottom();
                ViewCompat.setBackground(tabView2, AppCompatResources.getDrawable(tabView2.getContext(), tab2));
                ViewCompat.setPaddingRelative(tabView2, paddingStart, paddingTop, paddingEnd, paddingBottom);
            }
        }
    }
}