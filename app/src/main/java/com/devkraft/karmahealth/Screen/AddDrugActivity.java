
package com.devkraft.karmahealth.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devkraft.karmahealth.Adapter.FindDrugAdapter;
import com.devkraft.karmahealth.Adapter.FrequencySpinnerAdapter;
import com.devkraft.karmahealth.Adapter.MonthlySpinnerCustomDrugAdapter;
import com.devkraft.karmahealth.Adapter.RefillSpinnerAdapter;
import com.devkraft.karmahealth.Adapter.ReminderDTOAdapter;
import com.devkraft.karmahealth.Adapter.TimeGridAdapter;
import com.devkraft.karmahealth.Adapter.TimeGridMonthlyAdapter;
import com.devkraft.karmahealth.Adapter.TimeGridWeeklyAdapter;
import com.devkraft.karmahealth.Adapter.UserSpinnerAdapter;
import com.devkraft.karmahealth.Model.APIMessageResponse;
import com.devkraft.karmahealth.Model.AddDrugDto;
import com.devkraft.karmahealth.Model.AddDrugRequest;
import com.devkraft.karmahealth.Model.AddDrugResponse;
import com.devkraft.karmahealth.Model.DependentDto;
import com.devkraft.karmahealth.Model.DosageDropDownDto;
import com.devkraft.karmahealth.Model.DosageDto;
import com.devkraft.karmahealth.Model.DrugDosageDto;
import com.devkraft.karmahealth.Model.DrugDto;
import com.devkraft.karmahealth.Model.DrugListAPIResponse;
import com.devkraft.karmahealth.Model.LoginNewResponse;
import com.devkraft.karmahealth.Model.LoginResponse;
import com.devkraft.karmahealth.Model.MonthlyDays;
import com.devkraft.karmahealth.Model.PrescriptionRefillDto;
import com.devkraft.karmahealth.Model.RefreshTokenRequest;
import com.devkraft.karmahealth.Model.RefreshTokenResponse;
import com.devkraft.karmahealth.Model.ReminderDto;
import com.devkraft.karmahealth.Model.RetrofitPatientIDRequest;
import com.devkraft.karmahealth.Model.RetrofitPrescriptionsModel;
import com.devkraft.karmahealth.Model.Time;
import com.devkraft.karmahealth.Model.TimeDto;
import com.devkraft.karmahealth.Model.UserDrugDto;
import com.devkraft.karmahealth.Model.UserDto;
import com.devkraft.karmahealth.Model.WeeklyDays;
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
import com.devkraft.karmahealth.net.GenericRequestWithoutAuth;
import com.devkraft.karmahealth.retrofit.ServiceGenerator;
import com.devkraft.karmahealth.retrofit.ServiceGeneratorTwo;
import com.devkraft.karmahealth.retrofit.UserService;
import com.google.gson.Gson;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.devkraft.karmahealth.Utils.AppUtils.DAILY_LIMIT;
import static com.devkraft.karmahealth.Utils.AppUtils.WEEKLY_DAY_LIMIT;
import static com.devkraft.karmahealth.Utils.AppUtils.WEEKLY_LIMIT;

import retrofit2.Call;
import retrofit2.Callback;


public class AddDrugActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    List<DrugListAPIResponse> adddrug = new ArrayList<>();
    public String drugDesplay, drugForSearch;
    AppCompatButton btnsave;

    public static final String TAG = AddDrugActivity.class.getSimpleName();
    public static final String USER_DRUG_DTO = "USER_DRUG_DTO";
    public static String isFromEditDrug = "isFromEditDrug";
    private ProgressDialogSetup progress;
    private ArrayAdapter<String> drugFreqAdapter;
    private AutoCompleteTextView drugSearchAc;
    private FindDrugAdapter findDrugAdapter;

    private DrugDto drugDto = null;
    private TextView startDateTv, endDateTv, userNameTv, countTv, pickYourTimeTv, remindedMsgTv,
            tabletTv, injectionTv, liquidTv, otherTv, startDateTextView, endDateTextView, prescribeDosageMsg,
            cantFindYourDrug, cantFindYourDosage, txtTryCustomSchedule, txtBackToStandardSchedule, morningtvtv, aftertvtv, eveingtvtv, nighttvtv;
    private ImageView startDateIv, endDateIv, addIv, removeIv, clearIv, dosageArrow;
    private Spinner frequencySpinner, customRouteSpinner, customFrequencySpinner,
            customDayOfWeekSpinner, customDayOfMonthSpinner, customTimeSpinner, userSpinner,
            refillSpinner, dosageSpinner;
    private Button removeDrugBtn, customDosageSaveButton, customDosageClearButton;
    private RelativeLayout tabletRl, injectionRl, liquidRl, otherRl, parentLayout;


    private LinearLayout dosageSpinnerLl, dosageEditTextLl, frequencyLl, customRouteLL, endDateLl,
            customFrequencyLL, customDayOfWeekOrMonthLL, customSelectDaysLL, recyclerLl, startDateLl, endDateLls,
            dosageSummaryContainerLL, dosageArrowLl, timingLl, removeBtnLl, drugTypeLl,
            drugInfoLl, dosageFormLl, standardScheduleLL, customScheduleLL;
    private View startDateHrLine, endDateHrLine;

    private UserDrugDto userDrugDto;
    private DrugDosageDto.DrugDosageDtoList drugFreqList;
    private Menu menu;
    private DrugDto drugDtoFromDetailsScreen;

    private int countForTiming = 0;
    private String[] timingArray = new String[6];
    private String[] timingNameArray = new String[6];

    private List<DosageDto> timeDtoList;
    private CheckBox noEndDateCb;

    private EditText dosageEditText, customDosageEditText, strengthEt;
    private NestedScrollView scrollView;
    private UserDto userDto;
    private String startDateString, endDateString;

    private UserSpinnerAdapter userSpinnerAdapter;
    private ArrayAdapter<String> ethnicityAdapter;
    private RefillSpinnerAdapter refillSpinnerAdapter;
    private PrescriptionRefillDto refillDto;
    private List<Time> timeList;

    private RecyclerView timeCardRecyclerView, weeklyRecyclerView, monthlyRecyclerView;
    private TimeGridAdapter timeGridAdapter;
    private TimeGridWeeklyAdapter timeGridWeeklyAdapter;
    private TimeGridMonthlyAdapter timeGridMonthlyAdapter;
    private FrequencySpinnerAdapter frequencySpinnerAdapter;
    private boolean isTablet, isInjection, isLiquid, isOther, userIsInteracting = false, isEditDrug,
            isCantFindYourDosage, isCustomSchedule, isFromSplashScreen, isProgrammatically = false,
            isFromDetailScreen = false;


    private List<ReminderDto> customDosageReminderDtoList;
    private ListView customReminderListView;
    private ReminderDTOAdapter reminderDTOAdapter;
    private List<String> stringList;


    // Data Fields
    private Map<String, Integer> routeMap, frequencyMap;
    private List<String> hoursOfDayList, dayOfWeekList;
    private int editReminderDtoIndex;
    private MenuItem doneItem;
    private MonthlySpinnerCustomDrugAdapter monthlySpinnerCustomDrugAdapter;
    private LinearLayout llCustomTimeLL;
    private String notficationUserId;
    private boolean isUserTryStandardDrug, isUserTrySmartSchedule;
    private String drugToBeSearch;
    ArrayList<String> medicineListTemp = new ArrayList<>();
    private ArrayList<String> medicineForCleverTap = new ArrayList<>();

    private Boolean morning = true, afternoon = false, evening = false, night = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug);

        setUpToolbar();
        iniIds();
        onClick();
        handleClickEvent();
        updateRefillData();
        getIntentValue(getIntent());

        noEndDateCb.setChecked(true);
        timeDtoList = new ArrayList<>();
        customDosageReminderDtoList = new ArrayList<>();
        updateFrequencyData();


        insertValueForDosages();
        loadRouteDropDownValues();
        loadHoursOfDayValues();
        loadDayOfWeekValues();

        AppUtils.callDosageDropDownAPI(AddDrugActivity.this);

//        AppUtils.callGetUserSubscriptionAPI(this, parentLayout);
        if (!isEditDrug) {
            generateTimeGridView(DAILY_LIMIT, null, Constants.DAILY);
            tabletRl.performClick();
        }
        AppUtils.logEvent(Constants.ADD_DRUG_SCREEN_OPENED);
        AppUtils.logCleverTapEvent(this, Constants.ADD_MEDICINE_SCREEN_OPENED, null);


    }

    private void setUpToolbar() {

        btnsave = (AppCompatButton)findViewById(R.id.savebtn);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFields();
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
        getSupportActionBar().setTitle(getString(R.string.add_drug));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColor));
        showHideMenuOption(false);
    }

    private void onClick() {
        morningtvtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!morning) {
                    morning = true;
                    morningtvtv.setBackgroundResource(R.drawable.border_blue);
                    morningtvtv.setTextColor(getResources().getColor(R.color.white));
                } else {
                    morning = false;
                    morningtvtv.setBackgroundResource(R.drawable.border);
                    morningtvtv.setTextColor(getResources().getColor(R.color.gray));
                }

            }
        });
        aftertvtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!afternoon) {
                    afternoon = true;
                    aftertvtv.setBackgroundResource(R.drawable.border_blue);
                    aftertvtv.setTextColor(getResources().getColor(R.color.white));
                } else {
                    afternoon = false;
                    aftertvtv.setBackgroundResource(R.drawable.border);
                    aftertvtv.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        eveingtvtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!evening) {
                    evening = true;
                    eveingtvtv.setBackgroundResource(R.drawable.border_blue);
                    eveingtvtv.setTextColor(getResources().getColor(R.color.white));
                } else {
                    evening = false;
                    eveingtvtv.setBackgroundResource(R.drawable.border);
                    eveingtvtv.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
        nighttvtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!night) {
                    night = true;
                    nighttvtv.setBackgroundResource(R.drawable.border_blue);
                    nighttvtv.setTextColor(getResources().getColor(R.color.white));
                } else {
                    night = false;
                    nighttvtv.setBackgroundResource(R.drawable.border);
                    nighttvtv.setTextColor(getResources().getColor(R.color.gray));
                }
            }
        });
    }

    private void iniIds() {
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        edit = sharedPreferences.edit();
        progress = ProgressDialogSetup.getProgressDialog(AddDrugActivity.this);
        parentLayout = findViewById(R.id.activity_add_drug);
        drugSearchAc = findViewById(R.id.drugNameAc);
        userNameTv = findViewById(R.id.userNameTv);
        morningtvtv = findViewById(R.id.morningtv);
        aftertvtv = findViewById(R.id.aftertv);
        eveingtvtv = findViewById(R.id.eveingtv);
        nighttvtv = findViewById(R.id.nighttv);
        startDateIv = findViewById(R.id.startDateIv);
        startDateTv = findViewById(R.id.startDateTv);
        endDateIv = findViewById(R.id.endDateIv);
        endDateTv = findViewById(R.id.endDateTv);
        startDateHrLine = findViewById(R.id.startDateHrLine);
        endDateHrLine = findViewById(R.id.endDateHrLine);
        removeBtnLl = findViewById(R.id.removeBtnLl);
        removeDrugBtn = findViewById(R.id.removeDrugBtn);
        customDosageSaveButton = findViewById(R.id.btnCustomDosageSave);
        customDosageClearButton = findViewById(R.id.btnCustomDosageClear);
        timingLl = findViewById(R.id.timingLl);
        addIv = findViewById(R.id.addIv);
        removeIv = findViewById(R.id.removeIv);
        countTv = findViewById(R.id.countTv);
        noEndDateCb = findViewById(R.id.noEndDateCb);
        endDateLl = findViewById(R.id.endDateLl);
        strengthEt = findViewById(R.id.strengthEt);
        scrollView = findViewById(R.id.scrollView);
        userSpinner = findViewById(R.id.userSpinner);
        drugSearchAc.setThreshold(1);
        drugSearchAc.setAdapter(findDrugAdapter);
        prescribeDosageMsg = findViewById(R.id.prescribe_dosage_msg);
        refillSpinner = findViewById(R.id.refillSpinner);
        injectionRl = findViewById(R.id.injectionRl);
        tabletRl = findViewById(R.id.tabletRl);
        liquidRl = findViewById(R.id.liquidRl);
        otherRl = findViewById(R.id.otherRl);
        injectionTv = findViewById(R.id.injectionTv);
        tabletTv = findViewById(R.id.tabletTv);
        liquidTv = findViewById(R.id.liquidTv);
        otherTv = findViewById(R.id.otherTv);
        startDateLl = findViewById(R.id.startDateLl);
        endDateLls = findViewById(R.id.endDateLls);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);
        dosageSpinner = findViewById(R.id.dosageSpinner);
        dosageArrow = findViewById(R.id.dosageArrow);
        dosageArrowLl = findViewById(R.id.dosageArrowLl);
        dosageEditTextLl = findViewById(R.id.dosageEditTextLl);
        dosageSpinnerLl = findViewById(R.id.dosageSpinnerLl);
        frequencySpinner = findViewById(R.id.frequencySpinner);
        customRouteSpinner = findViewById(R.id.spCustomRoute);
        customFrequencySpinner = findViewById(R.id.spCustomFrequency);
        customDayOfWeekSpinner = findViewById(R.id.spCustomDayOfWeek);
        customDayOfMonthSpinner = findViewById(R.id.spCustomDayOfMonth);
        customTimeSpinner = findViewById(R.id.spCustomTime);
        frequencyLl = findViewById(R.id.frequencyLl);
        customRouteLL = findViewById(R.id.llCustomRouteLL);
        customFrequencyLL = findViewById(R.id.llCustomFrequencyLL);
        customDayOfWeekOrMonthLL = findViewById(R.id.llCustomDayOfWeekOrMonthLL);
        customSelectDaysLL = findViewById(R.id.llCustomSelectDaysLL);
        dosageSummaryContainerLL = findViewById(R.id.llDosageSummaryContainer);
        timeCardRecyclerView = findViewById(R.id.time_card_recycler_view);
        weeklyRecyclerView = findViewById(R.id.weekly_recycler_view);
        monthlyRecyclerView = findViewById(R.id.monthly_recycler_view);
        pickYourTimeTv = findViewById(R.id.pickYourTimeTv);
        remindedMsgTv = findViewById(R.id.remindedMsg);
        drugInfoLl = findViewById(R.id.drugInfoLl);
        dosageFormLl = findViewById(R.id.dosageFormLl);
        dosageEditText = findViewById(R.id.dosageEditText);
        customDosageEditText = findViewById(R.id.etCustomDosage);
        clearIv = findViewById(R.id.clearIv);
        cantFindYourDrug = findViewById(R.id.cantFindYourDrug);
        cantFindYourDosage = findViewById(R.id.cantFindYourDosage);
        standardScheduleLL = findViewById(R.id.llStandardContainer);
        customScheduleLL = findViewById(R.id.llCustomContainer);
        txtTryCustomSchedule = findViewById(R.id.txtTryCustomSchedule);
        txtBackToStandardSchedule = findViewById(R.id.txtBackToStandardSchedule);
        customReminderListView = findViewById(R.id.lvCustomReminder);
        llCustomTimeLL = findViewById(R.id.llCustomTimeLL);
        drugSearchAc.setFilters(new InputFilter[]{AppUtils.setInputFilterToRestrictEmoticons()});

        updateTimeCount();
//        updateRefillData(); removed for issue
        startDateTextView.setText(AppUtils.getTodayDate());
        endDateTextView.setText(AppUtils.getTodayDate());
        startDateString = AppUtils.getTodayDateForSchedule();
        endDateString = AppUtils.getTodayDateForSchedule();
        recyclerLl = findViewById(R.id.recyclerLl);
    }


    private void loadDayOfWeekValues() {
        String[] dayOfWeek = getResources().getStringArray(R.array.days_of_week_adapter);
        dayOfWeekList = Arrays.asList(dayOfWeek);
    }

    private void loadHoursOfDayValues() {
        String[] hoursOfDay = getResources().getStringArray(R.array.hours_of_day_adapter);
        hoursOfDayList = Arrays.asList(hoursOfDay);
    }

    private void loadRouteDropDownValues() {
        final String[] routeValues = getResources().getStringArray(R.array.route_adapter);
        routeMap = new HashMap<>();
        for (int i = 0; i < routeValues.length; i++) {
            routeMap.put(routeValues[i], i);
        }
    }

    private void insertValueForDosages() {
        timingArray[0] = "09:00";
        timingArray[1] = "13:00";
        timingArray[2] = "18:00";
        timingArray[3] = "21:00";
        timingArray[4] = "22:00";
        timingArray[5] = "23:00";


        timingNameArray[0] = "Dose 1";
        timingNameArray[1] = "Dose 2";
        timingNameArray[2] = "Dose 3";
        timingNameArray[3] = "Dose 4";
        timingNameArray[4] = "Dose 5";
        timingNameArray[5] = "Dose 6";
    }

    private void getIntentValue(Intent intent) {
        Log.i("checkSearchText", "step 1");
        if (intent != null) {
            notficationUserId = intent.getStringExtra(Constants.NOTIFICATION_USER_ID);
            isEditDrug = intent.getBooleanExtra(isFromEditDrug, false);
            if (isEditDrug) {
                AppUtils.logEvent(Constants.EDIT_DRUG_SCREEN_OPENED);
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                getSupportActionBar().setTitle(getString(R.string.edit_drug));
                String userDrugDtoStr = intent.getStringExtra(USER_DRUG_DTO);
                isFromSplashScreen = intent.getBooleanExtra(Constants.IS_FROM_SPLASH_SCREEN, false);
                if (userDrugDtoStr != null) {
                    Gson gson = new Gson();
                    userDrugDto = gson.fromJson(userDrugDtoStr, UserDrugDto.class);
                    if (userDrugDto.getCustom()) {
                        updateValuesForCustomDosage();
                    } else {
                        updateValues();
                    }
                }
            }

            String userDtoStr = intent.getStringExtra(Constants.USER_DTO);
            if (userDtoStr != null) {
                Gson gson = new Gson();
                userDto = gson.fromJson(userDtoStr, UserDto.class);

                if (userDto != null) {
                    userNameTv.setText(userDto.getName());
                }
            }

            String showUserDropDown = intent.getStringExtra(Constants.SHOW_USER_DROP_DOWN);
            if (showUserDropDown != null) {
                generateUserList();
            }

            String drugDtoStr = intent.getStringExtra(Constants.DRUGDTO);
            Log.i("checkdruglist", "drug request model = 789 " + drugDtoStr);
            if (drugDtoStr != null) {
                isFromDetailScreen = true;
                drugDtoFromDetailsScreen = new Gson().fromJson(drugDtoStr, DrugDto.class);
                isProgrammatically = true;

                if (drugDtoFromDetailsScreen.getDisplayName() != null) {
                    String drug = AppUtils.getEncodedStringForDrugAdapter(drugDtoFromDetailsScreen.getDisplayName());
                    String[] separated = drug.split("(");
                    drugSearchAc.setText(drug);
                } else {
                    String drugName = AppUtils.getEncodedStringForDrugAdapter(drugDtoFromDetailsScreen.getName());

                    if (drugName.length() > 1) {
                        String capitalDrugName = drugName.substring(0, 1).toUpperCase() + drugName.substring(1);
                        drugSearchAc.setText(capitalDrugName);
                    } else {
                        drugSearchAc.setText(drugName);
                    }
                }

                drugSearchAc.setEnabled(false);
                isProgrammatically = false;

                generateUserList();

                drugInfoLl.setVisibility(View.VISIBLE);
                showHideMenuOption(false);
            }
        }
    }

    private void updateValues() {
        drugInfoLl.setVisibility(View.VISIBLE);
        showHideMenuOption(false);
        isProgrammatically = true;
//        drugSearchAc.setText(AppUtils.getEncodedStringForDrugAdapter(userDrugDto.getDrugName()));
        if (userDrugDto.getDisplayName() != null) {
            drugSearchAc.setText(AppUtils.getEncodedStringForDrugAdapter(userDrugDto.getDisplayName()));
            drugSearchAc.setEnabled(false);
        } else if (userDrugDto.getDrugName() != null) {
            String drugName = AppUtils.getEncodedStringForDrugAdapter(userDrugDto.getDrugName());
            String capitalDrugName = drugName.substring(0, 1).toUpperCase() + drugName.substring(1);
            drugSearchAc.setText(AppUtils.getEncodedStringForDrugAdapter(capitalDrugName));
            drugSearchAc.setEnabled(false);
        }

        String dosageForm = userDrugDto.getDosageForm();
        if (getString(R.string.tablet).equalsIgnoreCase(dosageForm)) {
            performTabletClick(userDrugDto.getQuantity(), userDrugDto.getUnit(), userDrugDto.getDosage());
        } else if (getString(R.string.injection).equalsIgnoreCase(dosageForm)) {
            performInjectionClick(userDrugDto.getQuantity(), userDrugDto.getUnit(), userDrugDto.getDosage());
        } else if (getString(R.string.liquid).equalsIgnoreCase(dosageForm)) {
            performLiquidClick(userDrugDto.getQuantity(), userDrugDto.getUnit(), userDrugDto.getDosage());
        } else if (getString(R.string.other).equalsIgnoreCase(dosageForm)) {
            performOtherClick(userDrugDto.getQuantity(), userDrugDto.getUnit(), userDrugDto.getDosage());
        }

        startDateString = userDrugDto.getStartDate();
        String formattedStartDate = AppUtils.getFormattedDate(userDrugDto.getStartDate());
        startDateTv.setText(formattedStartDate);
        startDateTextView.setText(formattedStartDate);


        String dosageType = userDrugDto.getDosageType();

        if (getString(R.string.daily).equalsIgnoreCase(dosageType)) {
            frequencySpinner.setSelection(0);

            List<ReminderDto> reminderList = userDrugDto.getReminderList();

            stringList = getSelectedTimingList(reminderList);

            generateTimeGridView(DAILY_LIMIT, stringList, Constants.DAILY);

        } else if (getString(R.string.weekly).equalsIgnoreCase(dosageType)) {
            weeklyRecyclerView.setVisibility(View.VISIBLE);
            pickYourTimeTv.setVisibility(View.VISIBLE);

            monthlyRecyclerView.setVisibility(View.GONE);
            timeCardRecyclerView.setVisibility(View.GONE);

            frequencySpinner.setSelection(1);

            List<ReminderDto> reminderList = userDrugDto.getReminderList();
            stringList = getSelectedTimingList(reminderList);

            generateTimeGridView(WEEKLY_LIMIT, stringList, Constants.WEEKLY);

            List<String> selectedDays = new ArrayList<>();
            for (int i = 0; i < reminderList.size(); i++) {
                ReminderDto reminderDto = reminderList.get(i);
                selectedDays.add(reminderDto.getValue());
            }

            generateWeeklyGridView(selectedDays);

        } else if (getString(R.string.monthly).equalsIgnoreCase(dosageType)) {

            monthlyRecyclerView.setVisibility(View.VISIBLE);
            pickYourTimeTv.setVisibility(View.VISIBLE);

            weeklyRecyclerView.setVisibility(View.GONE);
            timeCardRecyclerView.setVisibility(View.GONE);

            frequencySpinner.setSelection(2);


            List<ReminderDto> reminderList = userDrugDto.getReminderList();
            stringList = getSelectedTimingList(reminderList);

            generateTimeGridView(WEEKLY_LIMIT, stringList, Constants.MONTHLY);

            List<String> selectedDays = new ArrayList<>();
            for (int i = 0; i < reminderList.size(); i++) {
                ReminderDto reminderDto = reminderList.get(i);
                selectedDays.add(reminderDto.getValue());
            }

            generateMonthlyGridView(selectedDays);
        } else {
            showHideTimeOption(View.GONE);
            frequencySpinner.setSelection(3);
        }

        PrescriptionRefillDto prescriptionRefillDto = userDrugDto.getPrescriptionRefill();
        if (prescriptionRefillDto != null) {
            String displayName = prescriptionRefillDto.getValue();
            if (displayName.equalsIgnoreCase(Constants.one_week)) {
                refillSpinner.setSelection(1);
            } else if (displayName.equalsIgnoreCase(Constants.two_week)) {
                refillSpinner.setSelection(2);
            } else if (displayName.equalsIgnoreCase(Constants.three_week)) {
                refillSpinner.setSelection(3);
            } else if (displayName.equalsIgnoreCase(Constants.one_month)) {
                refillSpinner.setSelection(4);
            } else if (displayName.equalsIgnoreCase(Constants.two_months)) {
                refillSpinner.setSelection(5);
            } else if (displayName.equalsIgnoreCase(Constants.three_months)) {
                refillSpinner.setSelection(6);
            } else if (displayName.equalsIgnoreCase(Constants.no_reminder)) {
                refillSpinner.setSelection(0);
            }
        }


        if (userDrugDto.getEndDate() != null) {
            noEndDateCb.setChecked(false);

            endDateString = userDrugDto.getEndDate();
            String formattedEndDate = AppUtils.getFormattedDate(userDrugDto.getEndDate());

            endDateTv.setText(formattedEndDate);

        } else {
            noEndDateCb.setChecked(true);
        }

        String strength = userDrugDto.getStrength();
        if (strength != null) {
            strengthEt.setText(strength);
        }

        List<DosageDto> dosageDtoList = userDrugDto.getUserDrugDosages();
        if (dosageDtoList != null && dosageDtoList.size() > 0) {
            timeDtoList.addAll(dosageDtoList);
            countForTiming = timeDtoList.size();
            generateView();
            updateTimeCount();
        }
    }

    private List<String> getSelectedTimingList(List<ReminderDto> reminderList) {
        List<String> stringList = new ArrayList<>();

        if (reminderList != null && reminderList.size() > 0) {
            for (int i = 0; i < reminderList.size(); i++) {
                ReminderDto reminderDto = reminderList.get(i);

                stringList.add(reminderDto.getReminderTime());
            }
        }
        return stringList;
    }

    private void updateValuesForCustomDosage() {
        // 1) Make customFrequencySpinner to default_user value(0 = Daily)
        customFrequencySpinner.setSelection(0);

        // 1) Hide standard schedule layout, show custom schedule layout and update flag.
        customScheduleLL.setVisibility(View.VISIBLE);
        standardScheduleLL.setVisibility(View.GONE);
        isCustomSchedule = true;

        // 2) Hide can't find dosage text button.
        cantFindYourDrug.setVisibility(View.GONE);

        // 3) Show drug detail layout and fill drug name autocomplete.
        drugInfoLl.setVisibility(View.VISIBLE);
        showHideMenuOption(false);
        isProgrammatically = true;
//        drugSearchAc.setText(AppUtils.getEncodedStringForDrugAdapter(userDrugDto.getDrugName())); removed in 2.3.34

        if (userDrugDto.getDisplayName() != null) {
            drugSearchAc.setText(AppUtils.getEncodedStringForDrugAdapter(userDrugDto.getDisplayName()));
            drugSearchAc.setEnabled(false);
        } else if (userDrugDto.getDrugName() != null) {
            String drugName = AppUtils.getEncodedStringForDrugAdapter(userDrugDto.getDrugName());
            String capitalDrugName = drugName.substring(0, 1).toUpperCase() + drugName.substring(1);
            drugSearchAc.setText(AppUtils.getEncodedStringForDrugAdapter(capitalDrugName));
            drugSearchAc.setEnabled(false);
        }

        // 3) Set startDate value to it's TextView.
        startDateString = userDrugDto.getStartDate();
        String formattedStartDate = AppUtils.getFormattedDate(userDrugDto.getStartDate());
        startDateTv.setText(formattedStartDate);
        startDateTextView.setText(formattedStartDate);

        // 4) Update refill reminder spinner.
        PrescriptionRefillDto prescriptionRefillDto = userDrugDto.getPrescriptionRefill();
        if (prescriptionRefillDto != null) {
            String displayName = prescriptionRefillDto.getValue();
            if (displayName.equalsIgnoreCase(Constants.one_week)) {
                refillSpinner.setSelection(1);
            } else if (displayName.equalsIgnoreCase(Constants.two_week)) {
                refillSpinner.setSelection(2);
            } else if (displayName.equalsIgnoreCase(Constants.three_week)) {
                refillSpinner.setSelection(3);
            } else if (displayName.equalsIgnoreCase(Constants.one_month)) {
                refillSpinner.setSelection(4);
            } else if (displayName.equalsIgnoreCase(Constants.two_months)) {
                refillSpinner.setSelection(5);
            } else if (displayName.equalsIgnoreCase(Constants.three_months)) {
                refillSpinner.setSelection(6);
            } else if (displayName.equalsIgnoreCase(Constants.no_reminder)) {
                refillSpinner.setSelection(0);
            }
        }

        // 5) Show dosage summary layout
        customDosageReminderDtoList = userDrugDto.getReminderList();
        createDosageSummaryList(customDosageReminderDtoList);
        resetCustomDosageForm();
    }

    private void generateUserList() {
        userNameTv.setVisibility(View.GONE);
        userSpinner.setVisibility(View.GONE);
        final List<UserDto> userDtoList = new ArrayList<>();

        LoginResponse loginResponse = ApplicationPreferences.get().getUserDetails();
        if (loginResponse != null) {
            UserDto uDto = loginResponse.getUserDTO();
            if (uDto != null) {
                userDtoList.add(uDto);
                userDto = uDto;
            }
        }
        UserDto uDto;
        List<DependentDto> dependentDtoList = ApplicationDB.get().getDependents();
        if (dependentDtoList != null && dependentDtoList.size() > 0) {
            for (int i = 0; i < dependentDtoList.size(); i++) {
                DependentDto dependentDto = dependentDtoList.get(i);
                uDto = new UserDto();

                uDto.setName(dependentDto.getName());
                uDto.setId(dependentDto.getId());

                userDtoList.add(uDto);
            }
        }

        userSpinnerAdapter = new UserSpinnerAdapter(AddDrugActivity.this, userDtoList,sharedPreferences.getString("Pname",""));
        userSpinner.setAdapter(userSpinnerAdapter);

        if (notficationUserId != null) {
            for (UserDto userDto : userDtoList) {
                if (userDto.getId().toString().equalsIgnoreCase(notficationUserId)) {
                    int index = userDtoList.indexOf(userDto);
                    if (index != -1) {
                        userSpinner.setSelection(index);
                    }
                }
            }
        }

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppUtils.logEvent(Constants.ADD_DRUG_SCREEN_PROFILE_SELECTED);
                UserDto uDto = userDtoList.get(position);
                userDto = uDto;
                callIsDrugExistAPI();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateRefillData() {
        final List<PrescriptionRefillDto> remindRefillDtoList = ApplicationPreferences.get().getRemindRefillData();
        if (remindRefillDtoList != null && remindRefillDtoList.size() > 0) {
            refillSpinnerAdapter = new RefillSpinnerAdapter(AddDrugActivity.this, remindRefillDtoList);
            refillSpinner.setAdapter(refillSpinnerAdapter);
            refillSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    PrescriptionRefillDto rDto = remindRefillDtoList.get(position);
                    refillDto = rDto;
                }

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            refillSpinner.setSelection(4);
        } else {
            AppUtils.callDosageDropDownAPI(AddDrugActivity.this);
            AppUtils.callRefillReminderAPI(AddDrugActivity.this);
            //  updateRefillData();
        }
    }

    private void updateFrequencyData() {
        List<String> stringList = new ArrayList<>();
        stringList.add(getString(R.string.drug_daily));
        stringList.add(getString(R.string.drug_weekly));
        stringList.add(getString(R.string.drug_monthly));
        stringList.add(getString(R.string.as_needed));
        frequencyMap = new HashMap<>();
        frequencyMap.put(getString(R.string.select), 0);
        frequencyMap.put(getString(R.string.drug_daily), 1);
        frequencyMap.put(getString(R.string.drug_weekly), 2);
        frequencyMap.put(getString(R.string.drug_monthly), 3);


        /*for (int i = 1; i < stringList.size(); i++) {
            frequencyMap.put(stringList.get(i), i);
        }*/

        /* frequencySpinnerAdapter = new FrequencySpinnerAdapter(this,stringList);
        frequencySpinner.setAdapter(frequencySpinnerAdapter);

        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                frequencyPos = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_text_view, stringList);
        frequencySpinner.setAdapter(adapter);


        //TODO : IF needed plese remove this code Code Edit by Omkar
       /* List<String> stringListFreq = new ArrayList<>();
        stringListFreq.add(getString(R.string.daily));
        stringListFreq.add(getString(R.string.weekly));
        stringListFreq.add(getString(R.string.monthly));
        ArrayAdapter<String> adapterCustomFreq = new ArrayAdapter<>(this, R.layout.spinner_text_view, stringListFreq);
        customFrequencySpinner.setAdapter(adapterCustomFreq);*/

        // Set day of month adapter to its spinner.
        List<String> dayOfMonthList = new ArrayList<>();
        for (int i = 1; i < 32; i++) {
            dayOfMonthList.add(String.valueOf(i));
        }
        ArrayAdapter<String> dayOfMonthArrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_text_view, dayOfMonthList);
        //customDayOfMonthSpinner.setAdapter(dayOfMonthArrayAdapter);


        monthlySpinnerCustomDrugAdapter = new
                MonthlySpinnerCustomDrugAdapter(AddDrugActivity.this, AppUtils.getMonthlyList(null));

        customDayOfMonthSpinner.setAdapter(monthlySpinnerCustomDrugAdapter);
    }

    private void updateTimeCount() {
        if (timeDtoList != null) {
            countForTiming = timeDtoList.size();
        }
        String countStr = "" + countForTiming;
        countTv.setText(countStr);
    }


    private void openDialogBox() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        AppUtils.openDialogBox(AddDrugActivity.this, userDto, ft, prev, true);
    }

    private void generateMessage() {
        if (frequencySpinner != null) {

            int pos = frequencySpinner.getSelectedItemPosition();
            if (pos == 0) {
                updateMessageForDaily();
            } else if (pos == 1) {
                updateMessageForWeekly();
            } else if (pos == 2) {
                updateMessageForMonthly();
            }
        }
    }

    private void updateMessageForMonthly() {
        if (timeGridMonthlyAdapter != null) {
            List<MonthlyDays> monthlyDaysList = timeGridMonthlyAdapter.getList();

            if (monthlyDaysList != null && monthlyDaysList.size() > 0) {
                List<String> selectedStringList = new ArrayList<>();

                String remindedMsgStr = getString(R.string.you_are_scheduled_to_take) + getString(R.string.space);
                String otherMsg = drugSearchAc.getText().toString();
                if (isOther) {
                    remindedMsgStr = remindedMsgStr.concat(otherMsg);
                } else {
                    if (isCantFindYourDosage) {
                        remindedMsgStr = remindedMsgStr.concat(otherMsg);
                    } else {
                        if (dosageSpinner.getSelectedItem() != null) {
                            remindedMsgStr = remindedMsgStr.concat(dosageSpinner.getSelectedItem().toString().toLowerCase());
                        }
                    }
                }

                remindedMsgStr = remindedMsgStr + getString(R.string.space) +
                        getString(R.string.of) + getString(R.string.space);


                if (drugDto != null) {
//                    String drugName = AppUtils.getEncodedStringForDrugAdapter(drugDto.getName());
                    String drugName = AppUtils.getEncodedStringForDrugAdapter(drugDto.getDisplayName());
                    remindedMsgStr = remindedMsgStr.concat(drugName) + getString(R.string.space)
                            + getString(R.string.on) + getString(R.string.space);
                } else if (userDrugDto != null) {
//                    String drugName = AppUtils.getEncodedStringForDrugAdapter(userDrugDto.getDrugName());
                    if (userDrugDto.getDisplayName() != null) {
                        String drugName = AppUtils.getEncodedStringForDrugAdapter(userDrugDto.getDisplayName());
                        remindedMsgStr = remindedMsgStr.concat(drugName) + getString(R.string.space)
                                + getString(R.string.on) + getString(R.string.space);
                    }
                } else if (drugDtoFromDetailsScreen != null) {
                    String drugName = AppUtils.getEncodedStringForDrugAdapter(drugDtoFromDetailsScreen.getDisplayName());
                    remindedMsgStr = remindedMsgStr.concat(drugName) + getString(R.string.space)
                            + getString(R.string.on) + getString(R.string.space);
                } else {
                    String drugName = AppUtils.getEncodedStringForDrugAdapter(drugSearchAc.getText().toString());
                    remindedMsgStr = remindedMsgStr.concat(drugName) + getString(R.string.space)
                            + getString(R.string.at) + getString(R.string.space);
                }

                for (int i = 0; i < monthlyDaysList.size(); i++) {
                    MonthlyDays monthlyDays = monthlyDaysList.get(i);
                    if (monthlyDays.isSelected()) {
                        selectedStringList.add(monthlyDays.getDisplayName());
                    }
                }

                if (selectedStringList.size() > 0) {


                    boolean isFirstTime = true;
                    for (int i = 0; i < selectedStringList.size(); i++) {
                        if (selectedStringList.size() == 1) {
                            remindedMsgStr = remindedMsgStr.concat(selectedStringList.get(i)).concat(getString(R.string.space));
                            break;
                        } else {
                            if (i == selectedStringList.size() - 1) {
                                remindedMsgStr = remindedMsgStr.concat(getString(R.string.space)).concat(getString(R.string.and_symbol)).concat(getString(R.string.space)).concat(selectedStringList.get(i)).concat(getString(R.string.space));
                            } else {
                                if (isFirstTime) {
                                    isFirstTime = false;
                                    remindedMsgStr = remindedMsgStr.concat(selectedStringList.get(i));
                                } else {
                                    remindedMsgStr = remindedMsgStr.concat(getString(R.string.comma)).concat(getString(R.string.space)).concat(selectedStringList.get(i));
                                }

                            }
                        }
                    }

                    remindedMsgStr = remindedMsgStr.concat(getString(R.string.of_every_month)).
                            concat(getString(R.string.space)).concat(getString(R.string.at)).concat(getString(R.string.space));

                    if (timeGridAdapter != null) {
                        List<Time> timeList = timeGridAdapter.getList();
                        if (timeList != null && timeList.size() > 0) {
                            for (int i = 0; i < timeList.size(); i++) {
                                Time time = timeList.get(i);
                                if (time.isSelected()) {
                                    remindedMsgStr = remindedMsgStr.concat(time.getDisplayFormat());
                                    break;
                                }
                            }
                        }
                    }


                    remindedMsgTv.setText(remindedMsgStr);
                }
            }
        }
    }

    private void updateMessageForWeekly() {
        if (timeGridWeeklyAdapter != null) {
            List<WeeklyDays> weeklyDaysList = timeGridWeeklyAdapter.getList();

            if (weeklyDaysList != null && weeklyDaysList.size() > 0) {
                List<String> selectedStringList = new ArrayList<>();

                String remindedMsgStr = getString(R.string.you_are_scheduled_to_take) + getString(R.string.space);
                String otherMsg = drugSearchAc.getText().toString();
                if (isOther) {
                    remindedMsgStr = remindedMsgStr.concat(otherMsg);
                } else {
                    if (isCantFindYourDosage) {
                        remindedMsgStr = remindedMsgStr.concat(otherMsg);
                    } else {
                        if (dosageSpinner.getSelectedItem() != null) {
                            remindedMsgStr = remindedMsgStr.concat(dosageSpinner.getSelectedItem().toString().toLowerCase());
                        }
                    }
                }

                remindedMsgStr = remindedMsgStr + getString(R.string.space) +
                        getString(R.string.of) + getString(R.string.space);


                if (drugDto != null) {
//                    String drugName = AppUtils.getEncodedStringForDrugAdapter(drugDto.getName());
                    String drugName = AppUtils.getEncodedStringForDrugAdapter(drugDto.getDisplayName());
                    remindedMsgStr = remindedMsgStr.concat(drugName) + getString(R.string.space)
                            + getString(R.string.on) + getString(R.string.space);
                } else if (userDrugDto != null) {
                    if (userDrugDto.getDisplayName() != null) {
//                        String drugName = AppUtils.getEncodedStringForDrugAdapter(userDrugDto.getDrugName());
                        String drugName = AppUtils.getEncodedStringForDrugAdapter(userDrugDto.getDisplayName());
                        remindedMsgStr = remindedMsgStr.concat(drugName) + getString(R.string.space)
                                + getString(R.string.on) + getString(R.string.space);
                    }
                } else if (drugDtoFromDetailsScreen != null) {
                    String drugName = AppUtils.getEncodedStringForDrugAdapter(drugDtoFromDetailsScreen.getDisplayName());
                    remindedMsgStr = remindedMsgStr.concat(drugName) + getString(R.string.space)
                            + getString(R.string.on) + getString(R.string.space);
                } else {
                    String drugName = AppUtils.getEncodedStringForDrugAdapter(drugSearchAc.getText().toString());
                    remindedMsgStr = remindedMsgStr.concat(drugName) + getString(R.string.space)
                            + getString(R.string.at) + getString(R.string.space);
                }

                for (int i = 0; i < weeklyDaysList.size(); i++) {
                    WeeklyDays weeklyDays = weeklyDaysList.get(i);
                    if (weeklyDays.isSelected()) {
                        selectedStringList.add(weeklyDays.getFullName());
                    }
                }

                if (selectedStringList.size() > 0) {
                    boolean isFirstTime = true;
                    for (int i = 0; i < selectedStringList.size(); i++) {
                        if (selectedStringList.size() == 1) {
                            remindedMsgStr = remindedMsgStr.concat(selectedStringList.get(i)).concat(getString(R.string.space));
                            break;
                        } else {
                            if (i == selectedStringList.size() - 1) {
                                remindedMsgStr = remindedMsgStr.concat(getString(R.string.space)).concat(getString(R.string.and_symbol)).concat(getString(R.string.space)).concat(selectedStringList.get(i)).concat(getString(R.string.space));
                            } else {
                                if (isFirstTime) {
                                    isFirstTime = false;
                                    remindedMsgStr = remindedMsgStr.concat(selectedStringList.get(i));
                                } else {
                                    remindedMsgStr = remindedMsgStr.concat(getString(R.string.comma)).concat(getString(R.string.space)).concat(selectedStringList.get(i));
                                }

                            }
                        }
                    }

                    if (timeGridAdapter != null) {
                        List<Time> timeList = timeGridAdapter.getList();
                        if (timeList != null && timeList.size() > 0) {
                            for (int i = 0; i < timeList.size(); i++) {
                                Time time = timeList.get(i);
                                if (time.isSelected()) {
                                    remindedMsgStr = remindedMsgStr.concat(getString(R.string.at)).
                                            concat(getString(R.string.space)).concat(time.getDisplayFormat());
                                    break;
                                }
                            }
                        }
                    }
                    remindedMsgTv.setText(remindedMsgStr);

                }
            }
        }


    }

    private void updateMessageForDaily() {
        List<String> selectedStringList = new ArrayList<>();
        if (timeGridAdapter != null) {
            List<Time> timeList = timeGridAdapter.getList();

            String remindedMsgStr = getString(R.string.you_are_scheduled_to_take) + getString(R.string.space);
            String otherMsg = dosageEditText.getText().toString().trim();
            if (isOther) {
                if (!otherMsg.equalsIgnoreCase("")) {
                    remindedMsgStr = remindedMsgStr.concat(otherMsg);
                }
            } else {
                if (isCantFindYourDosage) {
                    remindedMsgStr = remindedMsgStr.concat(otherMsg);
                } else {
                    if (dosageSpinner.getSelectedItem() != null) {
                        remindedMsgStr = remindedMsgStr.concat(dosageSpinner.getSelectedItem().toString().toLowerCase());
                    }
                }
            }

            remindedMsgStr = remindedMsgStr + getString(R.string.space) +
                    getString(R.string.of) + getString(R.string.space);


            if (drugDto != null) {
//                String drugName = AppUtils.getEncodedStringForDrugAdapter(drugDto.getName());
                String drugName = AppUtils.getEncodedStringForDrugAdapter(drugDto.getDisplayName());
                remindedMsgStr = remindedMsgStr.concat(drugName) + getString(R.string.space)
                        + getString(R.string.at) + getString(R.string.space);
            } else if (userDrugDto != null) {
                if (userDrugDto.getDisplayName() != null) {
//                    String drugName = AppUtils.getEncodedStringForDrugAdapter(userDrugDto.getDrugName());
                    String drugName = AppUtils.getEncodedStringForDrugAdapter(userDrugDto.getDisplayName());
                    remindedMsgStr = remindedMsgStr.concat(drugName) + getString(R.string.space)
                            + getString(R.string.at) + getString(R.string.space);
                }
            } else if (drugDtoFromDetailsScreen != null) {
                String drugName = AppUtils.getEncodedStringForDrugAdapter(drugDtoFromDetailsScreen.getDisplayName());
                remindedMsgStr = remindedMsgStr.concat(drugName) + getString(R.string.space)
                        + getString(R.string.on) + getString(R.string.space);
            } else {
                String drugName = AppUtils.getEncodedStringForDrugAdapter(drugSearchAc.getText().toString());
                remindedMsgStr = remindedMsgStr.concat(drugName) + getString(R.string.space)
                        + getString(R.string.at) + getString(R.string.space);
            }


            for (int i = 0; i < timeList.size(); i++) {
                Time time = timeList.get(i);
                if (time.isSelected()) {
                    selectedStringList.add(time.getDisplayFormat().toLowerCase());
                }
            }

            if (selectedStringList.size() > 0) {
                boolean isFirstTime = true;
                for (int i = 0; i < selectedStringList.size(); i++) {

                    if (selectedStringList.size() == 1) {
                        remindedMsgStr = remindedMsgStr.concat(selectedStringList.get(i));
                        break;
                    } else {
                        if (i == selectedStringList.size() - 1) {
                            remindedMsgStr = remindedMsgStr.concat(getString(R.string.space)).concat(getString(R.string.and_symbol)).concat(getString(R.string.space)).concat(selectedStringList.get(i));
                        } else {
                            if (isFirstTime) {
                                isFirstTime = false;
                                remindedMsgStr = remindedMsgStr.concat(selectedStringList.get(i));
                            } else {
                                remindedMsgStr = remindedMsgStr.concat(getString(R.string.comma)).concat(getString(R.string.space)).concat(selectedStringList.get(i));
                            }


                        }
                    }
                }

                if (isOther && otherMsg.equalsIgnoreCase("")) {
                    remindedMsgStr = "";
                }
                remindedMsgTv.setText(remindedMsgStr);
            }
        }


    }

    private void showHideMenuOption(boolean isVisible) {
        if (menu != null) {
            MenuItem item = menu.findItem(R.id.done);
            if (item != null) {
                item.setVisible(isVisible);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_menu, menu);
        this.menu = menu;
        if (!isFromDetailScreen) {
            doneItem = menu.findItem(R.id.done);
            SpannableString s = new SpannableString(getString(R.string.done));
            if (isCustomSchedule) {
                if ((customDosageReminderDtoList != null && customDosageReminderDtoList.size() < 1)) {
                    doneItem.setEnabled(false);
                    s.setSpan(new ForegroundColorSpan(Color.rgb(236, 236, 236)), 0, s.length(), 0);
                    doneItem.setTitle(s);
                } else {
                    doneItem.setEnabled(true);
                    s.setSpan(new ForegroundColorSpan(Color.rgb(74, 188, 147)), 0, s.length(), 0);
                    doneItem.setTitle(s);
                }
            } else {
                if (drugDto != null) {
                    doneItem.setEnabled(true);
                    s.setSpan(new ForegroundColorSpan(Color.rgb(74, 188, 147)), 0, s.length(), 0);
                    doneItem.setTitle(s);
                } else {
                    String userInput = drugSearchAc.getText().toString();
                    if (userInput.length() > 0) {
                        doneItem.setEnabled(true);
                        s.setSpan(new ForegroundColorSpan(Color.rgb(74, 188, 147)), 0, s.length(), 0);
                        doneItem.setTitle(s);
                    } else {
                        doneItem.setEnabled(false);
                        s.setSpan(new ForegroundColorSpan(Color.rgb(236, 236, 236)), 0, s.length(), 0);
                        doneItem.setTitle(s);
                    }
                }
            }
        }
        // return true so that the menu pop up is opened
        return true;
    }

    private void performTabletClick(int qty, String unit, String dosage) {
//        tabletRl.setBackgroundColor(ContextCompat.getColor(AddDrugActivity.this, R.color.colorAccent));
        tabletRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.left_border_with_left_corner));
        tabletTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.white));

        dosageArrowLl.setVisibility(View.VISIBLE);
        dosageSpinnerLl.setVisibility(View.VISIBLE);
        dosageEditTextLl.setVisibility(View.GONE);
        cantFindYourDosage.setVisibility(View.VISIBLE);

        isTablet = true;
        isLiquid = false;
        isInjection = false;
        isOther = false;

        List<String> stringList = new ArrayList<>();
        List<DosageDropDownDto> dosageList = ApplicationPreferences.get().getDosageList(Constants.TABLET);
        if (dosageList != null && dosageList.size() > 0) {
            for (int i = 0; i < dosageList.size(); i++) {
                DosageDropDownDto dosageDropDownDto = dosageList.get(i);
                stringList.add(dosageDropDownDto.getValue());
            }
        }

        updateDosageAdapter(stringList, qty, unit, dosage);


        injectionRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.gray_border_without_corner));
        injectionTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.gray));
        liquidRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.gray_border_without_corner));
        liquidTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.gray));
        otherRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.right_border_with_right_color));
        otherTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.gray));
    }

    private void updateDosageAdapter(List<String> stringList, int qty, String unit, String dosage) {

        ArrayAdapter<String> dosageAdapter = new ArrayAdapter<String>(this, R.layout.dosage_spinner_dropdown);
        /*int posToBeSelected = -1;
        if (stringList != null && stringList.size() > 0) {
            for (int i = 0; i < stringList.size(); i++) {
                dosageAdapter.add(stringList.get(i));

                String listStr = stringList.get(i);
                if (listStr.equals(dosage)) {
                    posToBeSelected = i;
                }
            }
        }*/
        dosageAdapter.add("भोजन से पहले");
        dosageAdapter.add("भोजन के बाद");
        dosageAdapter.add("जरूरत पड़ने पर");
        dosageAdapter.add("रात को सोने से पहले");
        dosageAdapter.setDropDownViewResource(R.layout.dosage_spinner_dropdown);
        dosageSpinner.setAdapter(dosageAdapter);

        /*if (posToBeSelected == -1) {
            if (isTablet)*/
        dosageSpinner.setSelection(1);
        /*}*/

/*        if (isEditDrug) {
            if (posToBeSelected != -1) {
                dosageSpinner.setSelection(posToBeSelected);
                isCantFindYourDosage = false;
                dosageArrowLl.setVisibility(View.VISIBLE);
                dosageSpinnerLl.setVisibility(View.VISIBLE);
                dosageEditText.setText("");
                dosageEditTextLl.setVisibility(View.GONE);
                cantFindYourDosage.setText(getText(R.string.dont_see_dosage));
            } else {
                if (dosage == null) {
                    isCantFindYourDosage = false;
                    dosageArrowLl.setVisibility(View.VISIBLE);
                    dosageSpinnerLl.setVisibility(View.VISIBLE);
                    dosageEditText.setText("");
                    dosageEditTextLl.setVisibility(View.GONE);
                    cantFindYourDosage.setText(getText(R.string.dont_see_dosage));
                } else {
                    isCantFindYourDosage = true;
                    dosageArrowLl.setVisibility(View.GONE);
                    dosageSpinnerLl.setVisibility(View.GONE);
                    dosageEditTextLl.setVisibility(View.VISIBLE);
                    cantFindYourDosage.setText(getText(R.string.choose_from_standard_dosage));
                    dosageEditText.setText(dosage);
                }
            }
        } else {
            cantFindYourDosage.setText(getText(R.string.dont_see_dosage));
        }*/
    }

    private void generateTimeGridView(int limit, List<String> selectedTimeList, String whichType) {
        timeList = AppUtils.getTimeList(limit, selectedTimeList, whichType);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 6);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 50);
        layoutManager.canScrollVertically();
        timeCardRecyclerView.setLayoutManager(layoutManager);


        timeGridAdapter = new TimeGridAdapter(AddDrugActivity.this, timeList, limit, new TimeCallback() {
            @Override
            public void timeDto(List<Time> timeList) {
                if (timeList != null && timeList.size() > 0) {
                    if (frequencySpinner.getSelectedItem().toString().equalsIgnoreCase(Constants.WEEKLY)) {
                        updateMessageForWeekly();
                    } else if (frequencySpinner.getSelectedItem().toString().equalsIgnoreCase(Constants.MONTHLY)) {
                        updateMessageForMonthly();
                    } else {
                        updateMessageForDaily();
                    }
                }
            }
        });

        timeCardRecyclerView.setAdapter(timeGridAdapter);
    }

    private void performMonthlyClickEvent() {
        monthlyRecyclerView.setVisibility(View.VISIBLE);
        pickYourTimeTv.setVisibility(View.VISIBLE);

        weeklyRecyclerView.setVisibility(View.GONE);
        timeCardRecyclerView.setVisibility(View.GONE);

        generateMonthlyGridView(null);
    }

    private void generateMonthlyGridView(List<String> selectedList) {
        List<MonthlyDays> monthlyDaysList = AppUtils.getMonthlyList(selectedList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 6);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 50);
        layoutManager.canScrollVertically();
        monthlyRecyclerView.setLayoutManager(layoutManager);


        timeGridMonthlyAdapter = new TimeGridMonthlyAdapter(AddDrugActivity.this, monthlyDaysList, 4, new MonthlyTimeCallback() {
            @Override
            public void timeDto(List<MonthlyDays> monthlyDaysList) {
                updateMessageForMonthly();
            }
        });

        monthlyRecyclerView.setAdapter(timeGridMonthlyAdapter);
    }


    private void openEndsDateDialogPicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        int month = monthOfYear + 1;
                        String endd = endDateTextView.getText().toString();

                        if (month >=10){
                            if(dayOfMonth>=10){
                                String startDateStr =  "0"+(monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                                // startDateTv.setText(startDateStr);
                                endDateTextView.setText(startDateStr);
                                endDateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }
                            else {

                                String startDateStr =  "0"+(monthOfYear + 1) + "/0" + dayOfMonth + "/" + year;
                                // startDateTv.setText(startDateStr);
                                endDateTextView.setText(startDateStr);
                                endDateString = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                            }

                        }else {
                            if(dayOfMonth>=10){
                                String startDateStr =  "0"+(monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                                // startDateTv.setText(startDateStr);
                                endDateTextView.setText(startDateStr);
                                endDateString = year + "-" + "0"+(monthOfYear + 1) + "-" + dayOfMonth;
                            }
                            else {
                                String startDateStr =  "0"+(monthOfYear + 1) + "/0" + dayOfMonth + "/" + year;
                                // startDateTv.setText(startDateStr);
                                endDateTextView.setText(startDateStr);
                                endDateString = year + "-" + "0"+(monthOfYear + 1) + "-0" + dayOfMonth;
                            }

                        }


                        if(startDateTextView.getText().toString().compareToIgnoreCase(endDateTextView.getText().toString()) <=0){

                        }else{
                            endDateTextView.setText(endd);
                            AppUtils.openSnackBar(parentLayout, getString(R.string.select_different_date_one));
                        }
                    }
                }, mYear, mMonth, mDay);

        AppUtils.setDialogBoxButton(AddDrugActivity.this, datePickerDialog);

        //AppUtils.setSelectedDate(datePickerDialog, startDateTv.getText().toString().trim());

        datePickerDialog.show();
    }

    private void openStartDateDialogPicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        int month = monthOfYear + 1;
                        String endd = startDateTextView.getText().toString();
                        if (month >=10){
                            if(dayOfMonth>=10){
                                String startDateStr = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                                startDateTv.setText(startDateStr);
                                startDateTextView.setText(startDateStr);
                                startDateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            }else {
                                String startDateStr = (monthOfYear + 1) + "/0" + dayOfMonth + "/" + year;
                                startDateTv.setText(startDateStr);
                                startDateTextView.setText(startDateStr);
                                startDateString = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                            }

                        }else {
                            if(dayOfMonth>=10){
                                String startDateStr = "0"+(monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                                startDateTv.setText(startDateStr);
                                startDateTextView.setText(startDateStr);
                                startDateString = year + "-" + "0"+(monthOfYear + 1) + "-" + dayOfMonth;
                            }else {
                                String startDateStr = "0"+(monthOfYear + 1) + "/0" + dayOfMonth + "/" + year;
                                startDateTv.setText(startDateStr);
                                startDateTextView.setText(startDateStr);
                                startDateString = year + "-" + "0"+(monthOfYear + 1) + "-0" + dayOfMonth;
                            }

                        }
                        if(startDateTextView.getText().toString().compareToIgnoreCase(endDateTextView.getText().toString()) <=0){

                        }else{
                            startDateTextView.setText(endd);
                            AppUtils.openSnackBar(parentLayout, getString(R.string.select_different_date_one));
                        }
                    }
                }, mYear, mMonth, mDay);

        AppUtils.setDialogBoxButton(AddDrugActivity.this, datePickerDialog);

        AppUtils.setSelectedDate(datePickerDialog, startDateTv.getText().toString().trim());

        datePickerDialog.show();
    }

    private void performWeeklyClickEvent() {
        weeklyRecyclerView.setVisibility(View.VISIBLE);
        pickYourTimeTv.setVisibility(View.VISIBLE);

        monthlyRecyclerView.setVisibility(View.GONE);
        timeCardRecyclerView.setVisibility(View.GONE);
        generateWeeklyGridView(null);
    }

    private void generateWeeklyGridView(List<String> selectedList) {
        List<WeeklyDays> weeklyDaysList = AppUtils.getWeeklyList(selectedList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),
                7, RecyclerView.VERTICAL, false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        layoutManager.canScrollHorizontally();
        weeklyRecyclerView.setLayoutManager(layoutManager);


        timeGridWeeklyAdapter = new TimeGridWeeklyAdapter(AddDrugActivity.this, weeklyDaysList, WEEKLY_DAY_LIMIT, new WeekTimeCallback() {
            @Override
            public void timeDto(List<WeeklyDays> weeklyDaysList) {
                updateMessageForWeekly();
            }
        });
        weeklyRecyclerView.setAdapter(timeGridWeeklyAdapter);
    }

    private void performDailyClickEvent() {
        weeklyRecyclerView.setVisibility(View.GONE);
        monthlyRecyclerView.setVisibility(View.GONE);
        pickYourTimeTv.setVisibility(View.GONE);

        timeCardRecyclerView.setVisibility(View.VISIBLE);
        generateTimeGridView(DAILY_LIMIT, null, Constants.DAILY);
    }

    private void showHideTimeOption(int isNeedToVisible) {
        recyclerLl.setVisibility(isNeedToVisible);
    }

    public class SelectedView {
        LinearLayout parentView;
        Context context;
        LayoutInflater inflater;

        EditText drugNameEt;
        TextView time;
        ImageView delete;

        private SelectedView(Context ctxt, final DosageDto dosageDto, final int pos) {
            inflater = LayoutInflater.from(ctxt);
            parentView = (LinearLayout) inflater.inflate(R.layout.add_timing_layout, null);

            this.context = ctxt;

            this.drugNameEt = parentView.findViewById(R.id.dosageName);
            this.time = parentView.findViewById(R.id.time);
            this.delete = parentView.findViewById(R.id.clear);

            if (dosageDto.getReminderDesc() != null) {
                drugNameEt.setText(dosageDto.getReminderDesc());
                drugNameEt.setSelection(dosageDto.getReminderDesc().length());
            }

            time.setText(AppUtils.get12HrsDate(dosageDto.getReminderTime()));

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (timeDtoList != null) {
                        timeDtoList.remove(pos);
                        generateView();
                        updateTimeCount();
                        moveScrollViewToBottom();
                    }
                }
            });


            time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(AddDrugActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            String dosageName = drugNameEt.getText().toString().trim();
                            if (dosageName.length() > 0) {

                                String selectedHrs = selectedHour + "";
                                String selectedMinutes = selectedMinute + "";

                                if (selectedHour < 9) {
                                    selectedHrs = 0 + "" + selectedHour;
                                }

                                if (selectedMinute < 9) {
                                    selectedMinutes = 0 + "" + selectedMinute;
                                }

                                updateTimeList(pos, selectedHrs, selectedMinutes, dosageName);
                            } else {
                                AppUtils.openSnackBar(parentView, getString(R.string.dosage_name_is_required));
                            }
                        }
                    }, hour, minute, true);// Yes 24 hour time
                    String timeStr = dosageDto.getReminderTime();
                    if (timeStr != null) {
                        String time[] = timeStr.split(":");
                        if (time != null) {
                            String hours = time[0];
                            String mins = time[1];
                            mTimePicker.updateTime(Integer.parseInt(hours), Integer.parseInt(mins));
                        }
                    }


                    mTimePicker.show();
                }
            });
        }

        public LinearLayout getParentView() {
            return parentView;
        }
    }

    private void updateTimeList(int pos, String selectedHour, String selectedMinute, String dosageName) {
        if (timeDtoList != null) {
            DosageDto dosageDto = timeDtoList.get(pos);
            if (dosageDto != null) {
                dosageDto.setReminderDesc(dosageName);

                if (selectedHour.length() < 2) {
                    selectedHour = "0" + selectedHour;
                }
                if (selectedMinute.length() < 2) {
                    selectedMinute = "0" + selectedMinute;
                }
                dosageDto.setReminderTime(selectedHour + ":" + selectedMinute);
            }
            generateView();
        }
    }

    private void generateView() {
        SelectedView selectedView;
        timingLl.removeAllViews();
        for (int i = 0; i < timeDtoList.size(); i++) {
            selectedView = new SelectedView(AddDrugActivity.this, timeDtoList.get(i), i);
            timingLl.addView(selectedView.getParentView());
        }
    }

    private void performOtherClick(int qty, String unit, String dosage) {

//        otherRl.setBackgroundColor(ContextCompat.getColor(AddDrugActivity.this, R.color.colorAccent));
        otherRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.right_border_with_right_corner));
        otherTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.white));

        tabletRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.left_border_with_gray_color));
        tabletTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.gray));
        injectionRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.gray_border_without_corner));
        injectionTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.gray));
        liquidRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.gray_border_without_corner));
        liquidTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.gray));

        dosageArrowLl.setVisibility(View.GONE);
        dosageSpinnerLl.setVisibility(View.GONE);
        dosageEditTextLl.setVisibility(View.VISIBLE);
        cantFindYourDosage.setVisibility(View.GONE);
        dosageEditText.setText(dosage);

        isTablet = false;
        isLiquid = false;
        isInjection = false;
        isOther = true;
    }

    private void moveScrollViewToBottom() {
        drugSearchAc.clearFocus();
        scrollView.post(new Runnable() {
            @Override

            public void run() {
                scrollView.scrollTo(0, scrollView.getBottom());
            }
        });
    }

    private void performInjectionClick(int qty, String unit, String dosage) {
        injectionRl.setBackgroundColor(ContextCompat.getColor(AddDrugActivity.this, R.color.colorAccent));
        injectionTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.white));

        dosageArrowLl.setVisibility(View.VISIBLE);
        dosageSpinnerLl.setVisibility(View.VISIBLE);
        dosageEditTextLl.setVisibility(View.GONE);
        cantFindYourDosage.setVisibility(View.VISIBLE);

        isTablet = false;
        isLiquid = false;
        isInjection = true;
        isOther = false;

        List<String> stringList = new ArrayList<>();
        List<DosageDropDownDto> dosageList = ApplicationPreferences.get().getDosageList(Constants.INJECTION);
        if (dosageList != null && dosageList.size() > 0) {
            for (int i = 0; i < dosageList.size(); i++) {
                DosageDropDownDto dosageDropDownDto = dosageList.get(i);
                stringList.add(dosageDropDownDto.getValue());
            }
        }
        updateDosageAdapter(stringList, qty, unit, dosage);

        tabletRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.left_border_with_gray_color));
        tabletTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.gray));
        liquidRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.gray_border_without_corner));
        liquidTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.gray));
        otherRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.right_border_with_right_color));
        otherTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.gray));
    }

    private void performLiquidClick(int qty, String unit, String dosage) {
        liquidRl.setBackgroundColor(ContextCompat.getColor(AddDrugActivity.this, R.color.colorAccent));
        liquidTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.white));


        dosageArrowLl.setVisibility(View.VISIBLE);
        dosageSpinnerLl.setVisibility(View.VISIBLE);
        dosageEditTextLl.setVisibility(View.GONE);
        cantFindYourDosage.setVisibility(View.VISIBLE);

        isTablet = false;
        isLiquid = true;
        isInjection = false;
        isOther = false;

        List<String> stringList = new ArrayList<>();
        List<DosageDropDownDto> dosageList = ApplicationPreferences.get().getDosageList(Constants.LIQUID);
        if (dosageList != null && dosageList.size() > 0) {
            for (int i = 0; i < dosageList.size(); i++) {
                DosageDropDownDto dosageDropDownDto = dosageList.get(i);
                stringList.add(dosageDropDownDto.getValue());
            }
        }

        updateDosageAdapter(stringList, qty, unit, dosage);

        tabletRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.left_border_with_gray_color));
        tabletTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.gray));
        injectionRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.gray_border_without_corner));
        injectionTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.gray));
        otherRl.setBackground(ContextCompat.getDrawable(AddDrugActivity.this, R.drawable.right_border_with_right_color));
        otherTv.setTextColor(ContextCompat.getColor(AddDrugActivity.this, R.color.gray));
    }

    private void handleClickEvent() {

       /* cantFindYourDosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dosageEditTextLl.getVisibility() == View.VISIBLE) {
                    dosageArrowLl.setVisibility(View.VISIBLE);
                    dosageSpinnerLl.setVisibility(View.VISIBLE);
                    dosageEditTextLl.setVisibility(View.GONE);
                    isCantFindYourDosage = false;
                    cantFindYourDosage.setText(getString(R.string.dont_see_dosage));
                } else {
                    dosageArrowLl.setVisibility(View.GONE);
                    dosageSpinnerLl.setVisibility(View.GONE);
                    dosageEditTextLl.setVisibility(View.VISIBLE);
                    isCantFindYourDosage = true;
                    cantFindYourDosage.setText(getString(R.string.choose_from_standard_dosage));
                }
                generateMessage();
            }
        });*/

        cantFindYourDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.logEvent(Constants.REQUEST_DRUG_BUTTON_CLICKED);
                openDialogBox();
            }
        });

        clearIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drugSearchAc.setText("");
                drugDto = null;
                drugInfoLl.setVisibility(View.GONE);
                showHideMenuOption(false);
                performTabletClick(-1, null, null);
                generateMessage();
                invalidateOptionsMenu();
            }
        });

        pickYourTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (timeCardRecyclerView.getVisibility() == View.VISIBLE) {
                    timeCardRecyclerView.setVisibility(View.GONE);
                } else {
                    timeCardRecyclerView.setVisibility(View.VISIBLE);
                    String freqStr = frequencySpinner.getSelectedItem().toString();
                    if (freqStr.equalsIgnoreCase(Constants.WEEKLY)) {
                        if (userDrugDto != null) {
                            String dosageType = userDrugDto.getDosageType();
                            if (dosageType != null) {
                                if (dosageType.equalsIgnoreCase(getString(R.string.weekly))) {
                                    generateTimeGridView(WEEKLY_LIMIT, stringList, Constants.WEEKLY);
                                } else {
                                    generateTimeGridView(WEEKLY_LIMIT, null, Constants.WEEKLY);
                                }
                            }
                        } else {
                            generateTimeGridView(WEEKLY_LIMIT, null, Constants.WEEKLY);
                        }
                    } else if (freqStr.equalsIgnoreCase(Constants.MONTHLY)) {
                        if (userDrugDto != null) {
                            String dosageType = userDrugDto.getDosageType();
                            if (dosageType != null) {
                                if (dosageType.equalsIgnoreCase(getString(R.string.monthly))) {
                                    generateTimeGridView(WEEKLY_LIMIT, stringList, Constants.MONTHLY);
                                } else {
                                    generateTimeGridView(WEEKLY_LIMIT, null, Constants.MONTHLY);
                                }
                            }
                        } else {
                            generateTimeGridView(WEEKLY_LIMIT, stringList, Constants.MONTHLY);
                        }
                    }
                }
            }
        });

        frequencyLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frequencySpinner.performClick();
            }
        });

        customRouteLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customRouteSpinner.performClick();
            }
        });

        customFrequencyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customFrequencySpinner.performClick();
            }
        });

        customDayOfWeekOrMonthLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customFrequencySpinner.getSelectedItem().toString().equalsIgnoreCase(Constants.WEEKLY)) {
                    customDayOfWeekSpinner.performClick();
                } else if (customFrequencySpinner.getSelectedItem().toString().equalsIgnoreCase(Constants.MONTHLY)) {
                    customDayOfMonthSpinner.performClick();
                }
            }
        });

        dosageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                generateMessage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dosageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
                generateMessage();
            }
        });


        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (userIsInteracting) {

                    if (position == 0) {
                        showHideTimeOption(View.VISIBLE);
                        performDailyClickEvent();
                    } else if (position == 1) {
                        showHideTimeOption(View.VISIBLE);
                        performWeeklyClickEvent();
                    } else if (position == 2) {
                        showHideTimeOption(View.VISIBLE);
                        performMonthlyClickEvent();
                    } else if (position == 3) {
                        showHideTimeOption(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        customFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        customSelectDaysLL.setVisibility(View.GONE);
                        customDayOfWeekSpinner.setVisibility(View.GONE);
                        customDayOfMonthSpinner.setVisibility(View.GONE);
                        break;
                    case 1:
                        customSelectDaysLL.setVisibility(View.GONE);
                        customDayOfWeekSpinner.setVisibility(View.GONE);
                        customDayOfMonthSpinner.setVisibility(View.GONE);
                        break;
                    case 2:
                        customSelectDaysLL.setVisibility(View.VISIBLE);
                        customDayOfWeekSpinner.setVisibility(View.VISIBLE);
                        customDayOfMonthSpinner.setVisibility(View.GONE);
                        break;
                    case 3:
                        customSelectDaysLL.setVisibility(View.VISIBLE);
                        customDayOfMonthSpinner.setVisibility(View.VISIBLE);
                        customDayOfWeekSpinner.setVisibility(View.GONE);
                        break;
                    case 4:
                        customSelectDaysLL.setVisibility(View.GONE);
                        customDayOfWeekSpinner.setVisibility(View.GONE);
                        customDayOfMonthSpinner.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dosageArrowLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dosageSpinner.performClick();
            }
        });

        dosageSpinnerLl.setOnClickListener(view -> dosageSpinner.performClick());

        endDateLls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEndsDateDialogPicker();
            }
        });
        startDateLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartDateDialogPicker();
            }
        });


        tabletRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performTabletClick(-1, null, null);
                generateMessage();
            }
        });

        injectionRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performInjectionClick(-1, null, null);
                generateMessage();
            }
        });

        liquidRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLiquidClick(-1, null, null);
                generateMessage();
            }
        });

        otherRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performOtherClick(-1, null, null);
                generateMessage();
            }
        });


        noEndDateCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    endDateLl.setVisibility(View.GONE);
                } else {
                    endDateLl.setVisibility(View.VISIBLE);
                }
            }
        });


        addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countForTiming != 6 && !(countForTiming > 6)) {

                    TimeDto timeDto = new TimeDto();
                    timeDto.setName(timingNameArray[countForTiming]);
                    timeDto.setTime(timingArray[countForTiming]);

                    DosageDto dosageDto = new DosageDto();
                    dosageDto.setReminderDesc(timingNameArray[countForTiming]);
                    dosageDto.setReminderTime(timingArray[countForTiming]);


                    timeDtoList.add(dosageDto);

                    countForTiming = countForTiming + 1;

                    updateTimeCount();
                    generateView();

                    moveScrollViewToBottom();
                }
            }
        });

        removeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countForTiming != 0 && !(countForTiming < 0)) {
                    countForTiming = countForTiming - 1;

                    if (timeDtoList != null) {
                        timeDtoList.remove(countForTiming);
                        generateView();
                    }
                    updateTimeCount();
                    generateView();
                    moveScrollViewToBottom();
                }
            }
        });

        removeDrugBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditDrug && userDrugDto != null) {
//                    showDialogBoxForDeleteDrug(userDrugDto.getId(), userDrugDto.getDrugName());

                    if (userDrugDto.getDisplayName() != null)
                        showDialogBoxForDeleteDrug(userDrugDto.getId(), userDrugDto.getDisplayName());
                }
            }
        });

        customDosageSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1) Validate add custom dosage form
                if (validateCustomDosageForm()) {
                    ReminderDto reminderDto = new ReminderDto();
                    String selectedRoute = customRouteSpinner.getSelectedItem().toString();
                    String selectedFrequency = customFrequencySpinner.getSelectedItem().toString();
                    reminderDto.setDosageForm(selectedRoute);
                    reminderDto.setDosageType(selectedFrequency.toLowerCase());
                    reminderDto.setRemindFlag(true);
                    String selectedTime = AppUtils.get24HrsTime(customTimeSpinner.getSelectedItem().toString());
                    reminderDto.setReminderTime(selectedTime);
                    reminderDto.setDosage(customDosageEditText.getText().toString());
                    if (selectedFrequency.equalsIgnoreCase(Constants.WEEKLY)) {
                        reminderDto.setValue(customDayOfWeekSpinner.getSelectedItem().toString());
                    } else if (selectedFrequency.equalsIgnoreCase(Constants.MONTHLY)) {
                        int pos = customDayOfMonthSpinner.getSelectedItemPosition();
                        MonthlyDays monthlyDays = (MonthlyDays) monthlySpinnerCustomDrugAdapter.getItem(pos);
                        reminderDto.setValue(monthlyDays.getDay() + "");
                        //reminderDto.setValue(customDayOfMonthSpinner.getSelectedItem().toString());
                    } else {
                        reminderDto.setValue("");
                    }

                    if (String.valueOf(customDosageSaveButton.getText())
                            .equalsIgnoreCase(AddDrugActivity.this.getString(R.string.save_label))) {
                        if (!customDosageReminderDtoList.contains(reminderDto)) {
                            customDosageReminderDtoList.add(reminderDto);
                            invalidateOptionsMenu();
                        } else {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.two_dosages_cant_be_same));
                        }
                    } else {
                        if (!customDosageReminderDtoList.contains(reminderDto)) {
                            customDosageReminderDtoList.set(editReminderDtoIndex, reminderDto);
                            invalidateOptionsMenu();
                        } else {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.two_dosages_cant_be_same));
                        }

                        //customDosageReminderDtoList.set(editReminderDtoIndex, reminderDto);
                        customDosageSaveButton.setText(R.string.save_label);
                    }

                    createDosageSummaryList(customDosageReminderDtoList);
                    resetCustomDosageForm();
                }
            }
        });

        customDosageClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCustomDosageForm();
            }
        });

        drugSearchAc.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int
                    before, int count) {
                AppUtils.cancelAPICalls();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*Log.i("checkSearchText","afterTextSchanged");*/
                if (!isProgrammatically) {
                    Log.i("checkSearchText", "true");
                    if (!drugSearchAc.getText().toString().matches("")) {

                        drugToBeSearch = drugSearchAc.getText().toString().trim();
                        /*Log.i("checkSearchText","drugToBeSearch = "+drugToBeSearch);*/
                        if (drugToBeSearch.length() > 0) {
                            clearIv.setVisibility(View.VISIBLE);
                          /*  Log.i("checkSearchText","drugToBeSearch 2  = "+drugToBeSearch);
                            Log.i("checkSearchText","drugToBeSearch 2 url  = "+APIUrls.get().DrugSearch(drugToBeSearch));*/

                            checkdruglist(drugToBeSearch);

                        } else {
                            AppUtils.openSnackBar(parentLayout, getString(R.string.please_enter_valid_drug));
                        }

                    } else {
                        clearIv.setVisibility(View.GONE);
                    }
                }
            }

        });

        startDateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartDateDialogPicker();
            }
        });

        startDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartDateDialogPicker();
            }
        });

        startDateHrLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStartDateDialogPicker();
            }
        });

        endDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEndDateDialogPicker();
            }
        });

        endDateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEndDateDialogPicker();
            }
        });

        endDateHrLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEndDateDialogPicker();
            }
        });

        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.hideKeyboard(AddDrugActivity.this);
            }
        });

        strengthEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    prescribeDosageMsg.setVisibility(View.VISIBLE);
                } else {
                    prescribeDosageMsg.setVisibility(View.GONE);
                }
            }
        });

        txtTryCustomSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCustomSchedule) {
                    showCreateCustomDosageAlert();
                }
            }
        });

        txtBackToStandardSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCustomSchedule) {
                    showStandardDosageAlert();
                }
            }
        });

        customDosageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    AppUtils.hideKeyboard(AddDrugActivity.this);
                }
            }
        });

        llCustomTimeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customTimeSpinner.performClick();
            }
        });
    }


    private void checkdruglist(String searchstr) {
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        Log.i("checkmodrugdruglistSea", "prescription api request = " + searchstr);
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null, false);
        service.druglist(searchstr,token).enqueue(new Callback<List<DrugListAPIResponse>>() {
            @Override
            public void onResponse(Call<List<DrugListAPIResponse>> call, retrofit2.Response<List<DrugListAPIResponse>> response) {
                Log.i("checkmodrugdruglistSea", "api login response 0 code = " + response.code());
                Log.i("checkmodrugdruglistSea", "api login response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Log.i("checkmodrugdruglistSea", "api login LoginNewResponse response = " + new Gson().toJson(response.body()));

                    adddrug.addAll(response.body());
                    Log.i("checkmodrugdruglistSea", "api login LoginNewResponse size = " + String.valueOf(adddrug.size()));
                    drugDto = null;
                    drugInfoLl.setVisibility(View.GONE);
                    showHideMenuOption(false);
                    if (response.body() != null){
                        updateDrugAdapter(response.body());

                    }

                }else if(response.code() == 401){
                    refreshToken();
                } else {
                    AppUtils.openSnackBar(parentLayout, "Drug not found");
//                    Toast.makeText(LoginActivity.this, "गलत प्रत्यक्ष पत्र", Toast.LENGTH_SHORT).show();
                    Log.i("checkmodeldata", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<List<DrugListAPIResponse>> call, Throwable t) {
                AppUtils.openSnackBar(parentLayout, t.getMessage());
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });
    }

    private void callDrugSearchAPI(String drugSearchStr) {
        Log.i("checkdruglist", "drug request model = url " + APIUrls.get().DrugSearch(drugSearchStr));
        GenericRequestWithoutAuth<DrugDto.DrugDtoList> searchDrugRequest = new GenericRequestWithoutAuth<DrugDto.DrugDtoList>
                (Request.Method.GET, APIUrls.get().DrugSearch(drugSearchStr),
                        DrugDto.DrugDtoList.class, null,
                        new Response.Listener<DrugDto.DrugDtoList>() {
                            @Override
                            public void onResponse(DrugDto.DrugDtoList drugDtoList) {
                                Log.i("checkdruglist", "drug request model = response " + new Gson().toJson(drugDtoList));
                                //  Log.i("checkSearchText","api response 22  = "+drugDtoList.toString());
                                drugDto = null;
                                drugInfoLl.setVisibility(View.GONE);
                                showHideMenuOption(false);
                                if (drugDtoList != null) {
                                }
                                // updateDrugAdapter(drugDtoList);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //    Log.i("checkSearchText","error 22  = "+error.getMessage().toString());
                            }
                        });
        ApiService.get().addToRequestQueue(searchDrugRequest);
    }

    private void updateDrugAdapter(List<DrugListAPIResponse> drugDtoList) {
        try {
            DrugListAPIResponse drugDto1 = new DrugListAPIResponse();
            drugDto1.medicineName = getString(R.string.add_drug_to_your_profile_msg);
            //drugDtoList.add(drugDto1);
           /* if (drugDtoList == null) {
                drugDtoList = new ArrayList<>();
                drugDtoList.add(0, drugDto1);
            } else {
                drugDtoList.add(0, drugDto1);
            }*/
            Log.i("checkmodrugdruglist", "api login drugDtoList size = " + String.valueOf(drugDtoList.size()));

            findDrugAdapter = new FindDrugAdapter(this,
                    R.layout.view_find_drug,
                    drugDtoList, new HomeActivity.SearchDrugCallback() {
                @Override
                public void callDrugDto(DrugListAPIResponse dDto) {
                    HashMap<String, Object> medicineMap = new HashMap<>();

                    if (dDto.medicineName.equalsIgnoreCase(getString(R.string.add_drug_to_your_profile_msg))) {
                        //drugSearchAc.setText("");
                        drugDto = null;
                        drugInfoLl.setVisibility(View.VISIBLE);
                        showHideMenuOption(false);
                        generateMessage();
                        callIsDrugExistAPI();
                        drugSearchAc.dismissDropDown();
                    } else {
                        AppUtils.hideKeyboard(AddDrugActivity.this);
                       /* drugDto = dDto;
                        if (drugDto != null) {*/
                        drugInfoLl.setVisibility(View.VISIBLE);
                        showHideMenuOption(false);
                        isProgrammatically = true;
//                            drugSearchAc.setText(AppUtils.getEncodedString(drugDto.getName()));
                        String drug = AppUtils.getEncodedString(dDto.medicineName);
                        drugForSearch = drug;
                        String[] separated = drug.split("\\(");
                        if(separated.length >=2){
                            Log.i("checkdataspit","data = "+String.valueOf(separated[0]));
                            drugSearchAc.setText(String.valueOf(separated[0]));
                        }else {
                            drugDesplay=drugForSearch;
                            drugSearchAc.setText(drugDesplay/*separated[0]*/);
                        }
                        Log.i("checkdataspit","size = "+String.valueOf(separated.length));

                        isProgrammatically = false;
                        drugSearchAc.setSelection(drugSearchAc.getText().length());
                        generateMessage();
                        callIsDrugExistAPI();
                        drugSearchAc.dismissDropDown();

                        medicineMap.put("Medicine Name", drugSearchAc.getText().toString());
                        AppUtils.logCleverTapEvent(AddDrugActivity.this,
                                Constants.MEDICINE_SEARCHED, medicineMap);
                        /* }*/
                    }
                    isUserTryStandardDrug = true;
                    invalidateOptionsMenu();
                }
            });
            AppUtils.logEvent(Constants.DRUG_SEARCH);
            Log.i("checkmodrugdruglist", "api login drugDtoList two size = " + String.valueOf(drugDtoList.size()));
            drugSearchAc.setAdapter(findDrugAdapter);
            findDrugAdapter.notifyDataSetChanged();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callIsDrugExistAPI() {
        /*if (userDto != null) {

            String source;
            Long mappedId = null;
            String drugName = null;
            if (drugDto == null) {
                source = Constants.User_Entered_Drug;
                drugName = drugSearchAc.getText().toString();
            } else {
                source = drugDto.getSource();
                mappedId = drugDto.getId();
            }


            final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(AddDrugActivity.this);
            GenericRequest<IsExistDto> isDrugExistRequest = new GenericRequest<IsExistDto>
                    (Request.Method.GET, APIUrls.get().drugExist(userDto.getId(), mappedId, source, drugName),
                            IsExistDto.class, null,
                            new Response.Listener<IsExistDto>() {
                                @Override
                                public void onResponse(IsExistDto isExistDto) {
                                    authExpiredCallback.hideProgressBar();
                                    if (isExistDto.isExist()) {
                                        drugDto = null;
                                        drugSearchAc.setText("");
                                        drugInfoLl.setVisibility(View.GONE);
                                        showHideMenuOption(false);

                                        //AppUtils.openSnackBar(parentLayout, getString(R.string.drug_already_exist));
                                        showAlert(getString(R.string.drug_already_exist));
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    authExpiredCallback.hideProgressBar();
                                    String res = AppUtils.getVolleyError(AddDrugActivity.this, error, authExpiredCallback);
                                    AppUtils.openSnackBar(parentLayout, res);
                                }
                            });
            authExpiredCallback.setRequest(isDrugExistRequest);
            ApiService.get().addToRequestQueue(isDrugExistRequest);
        }*/
    }

    private void showCreateCustomDosageAlert() {


        AlertDialog.Builder builder = new AlertDialog.Builder(AddDrugActivity.this, R.style.AlertDialogTheme);
        String drugName = drugSearchAc.getText().toString();
        String message = getString(R.string.smart_Schedule_msg1) + getString(R.string.space) + drugName + getString(R.string.space) +
                getString(R.string.smart_Schedule_msg2);
        builder.setMessage(message)
                .setTitle(R.string.smart_schedule);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Make customFrequencySpinner to default_user value(0 = Daily)
                customFrequencySpinner.setSelection(0);
                customScheduleLL.setVisibility(View.VISIBLE);
                standardScheduleLL.setVisibility(View.GONE);
                isCustomSchedule = true;
                customDosageReminderDtoList.clear();
                createDosageSummaryList(customDosageReminderDtoList);
                resetCustomDosageForm();
                invalidateOptionsMenu();
                isUserTrySmartSchedule = true;
                isUserTryStandardDrug = false;
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openEndDateDialogPicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String endDateStr = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                        endDateTv.setText(endDateStr);
                        endDateString = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                }, mYear, mMonth, mDay);

        AppUtils.setSelectedDate(datePickerDialog, endDateTv.getText().toString().trim());
        datePickerDialog.show();
    }

    private void showStandardDosageAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddDrugActivity.this, R.style.AlertDialogTheme);
        String drugName = drugSearchAc.getText().toString();
        String message = getString(R.string.standard_schedule_msg) + getString(R.string.space) + AppUtils.getEncodedStringForDrugAdapter(drugName) + getString(R.string.space) + getString(R.string.smart_Schedule_msg2);
        builder.setMessage(message)
                .setTitle(R.string.create_standard_schedule);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                standardScheduleLL.setVisibility(View.VISIBLE);
                customScheduleLL.setVisibility(View.GONE);
                isCustomSchedule = false;

                generateTimeGridView(DAILY_LIMIT, null, Constants.DAILY);
                tabletRl.performClick();
                frequencySpinner.setSelection(0);

                invalidateOptionsMenu();
                isUserTrySmartSchedule = false;
                isUserTryStandardDrug = true;
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetCustomDosageForm() {
        customRouteSpinner.setSelection(0);
        customFrequencySpinner.setSelection(0);
        customDayOfWeekSpinner.setSelection(0);
        customDayOfMonthSpinner.setSelection(0);
        customDosageEditText.setText("");
        customTimeSpinner.setSelection(0);
        customDosageSaveButton.setText(R.string.save_label);
    }

    private void createDosageSummaryList(final List<ReminderDto> reminderDtoList) {

        if (reminderDtoList.size() > 0) {
            dosageSummaryContainerLL.setVisibility(View.VISIBLE);

            if (reminderDTOAdapter == null) {
                reminderDTOAdapter = new ReminderDTOAdapter(reminderDtoList, AddDrugActivity.this);
                reminderDTOAdapter.setEditButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customDosageSaveButton.setText(R.string.update);
                        int position = (Integer) view.getTag();
                        editReminderDtoIndex = position;
                        ReminderDto reminderDto = reminderDtoList.get(position);

                        Integer routeValueIndex = routeMap.get(reminderDto.getDosageForm());
                        customRouteSpinner.setSelection(routeValueIndex);

                        Integer frequencyValueIndex = frequencyMap.get(AppUtils.capitalize(reminderDto.getDosageType()));
                        customFrequencySpinner.setSelection(frequencyValueIndex);

                        //reminderDto.setRemindFlag(true);
                        String selectedTime = AppUtils.get12HrsDate(reminderDto.getReminderTime());
                        selectedTime = selectedTime.replace("AM", "am")
                                .replace("PM", "pm");
                        customTimeSpinner.setSelection(hoursOfDayList.indexOf(selectedTime));

                        customDosageEditText.setText(reminderDto.getDosage());

                        if (Constants.WEEKLY.equalsIgnoreCase(AppUtils.capitalize(reminderDto.getDosageType()))) {
                            customDayOfWeekSpinner.setSelection(dayOfWeekList.indexOf(reminderDto.getValue()));
                        } else if (Constants.MONTHLY.equalsIgnoreCase(AppUtils.capitalize(reminderDto.getDosageType()))) {
                            Integer index = Integer.parseInt(reminderDto.getValue()) - 1;
                            customDayOfMonthSpinner.setSelection(index);
                        } else {
                            customDayOfMonthSpinner.setSelection(0);
                        }
                    }
                });
                reminderDTOAdapter.setDeleteButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (Integer) view.getTag();
                        reminderDtoList.remove(position);
                        reminderDTOAdapter.refresh();
                        AppUtils.setListViewHeightBasedOnChildren(customReminderListView);
                        customDosageClearButton.performClick();
                        if (reminderDtoList != null && reminderDtoList.size() == 0) {
                            showHideMenuOption(false);
                            invalidateOptionsMenu();
                        }
                    }
                });
                customReminderListView.setAdapter(reminderDTOAdapter);
            } else {
                reminderDTOAdapter.refresh();
            }

            AppUtils.setListViewHeightBasedOnChildren(customReminderListView);
        } else {
            dosageSummaryContainerLL.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (progress != null) {
            progress.dismiss();
        }
    }

    private boolean validateCustomDosageForm() {
        String selectedRoute = customRouteSpinner.getSelectedItem().toString();
        int routePos = customRouteSpinner.getSelectedItemPosition();
        if (TextUtils.isEmpty(selectedRoute) || routePos == 0) {
            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_route_value));
            //showCustomDosageValidationAlert("Please select route value.");
            return false;
        }
        String selectedFrequency = customFrequencySpinner.getSelectedItem().toString();
        int frePos = customFrequencySpinner.getSelectedItemPosition();
        if (TextUtils.isEmpty(selectedFrequency) || frePos == 0) {
            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_frequency_value));
            //showCustomDosageValidationAlert("Please select frequency value.");
            return false;
        }
        if (selectedFrequency.equalsIgnoreCase(Constants.WEEKLY)) {
            String dayOfWeek = customDayOfWeekSpinner.getSelectedItem().toString();
            if (TextUtils.isEmpty(dayOfWeek)) {
                AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_day_value));
                //showCustomDosageValidationAlert("Please select day value.");
                return false;
            }
        } else if (selectedFrequency.equalsIgnoreCase(Constants.MONTHLY)) {
            int pos = customDayOfMonthSpinner.getSelectedItemPosition();
            MonthlyDays monthlyDays = (MonthlyDays) monthlySpinnerCustomDrugAdapter.getItem(pos);
            String dayOfMonth = monthlyDays.getDisplayName();

            if (TextUtils.isEmpty(dayOfMonth)) {
                AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_day_value));
                //showCustomDosageValidationAlert("Please select day value.");
                return false;
            }
        }
        String dosageValue = customDosageEditText.getText().toString();
        if (TextUtils.isEmpty(dosageValue)) {
            AppUtils.openSnackBar(parentLayout, getString(R.string.please_enter_dosage_value));
            //showCustomDosageValidationAlert("Please enter dosage value.");
            return false;
        }
        String selectedTime = customTimeSpinner.getSelectedItem().toString();
        int timePos = customTimeSpinner.getSelectedItemPosition();
        if (TextUtils.isEmpty(selectedTime) || timePos == 0) {
            AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_time_for_reminder));
            //showCustomDosageValidationAlert("Please select time for reminder.");
            return false;
        }

        return true;
    }

    private void showDialogBoxForDeleteDrug(final Long dId, String drugName) {
        LayoutInflater layoutInflater = LayoutInflater.from(AddDrugActivity.this);
        View promptView = layoutInflater.inflate(R.layout.delete_drug_dialog_box, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddDrugActivity.this, R.style.AlertDialogTheme);
        alertDialogBuilder.setView(promptView);

        TextView msg = promptView.findViewById(R.id.msg);
        String msgStr = getString(R.string.this_action_will_remove) + getString(R.string.space) + drugName + getString(R.string.space) + getString(R.string.from) + getString(R.string.space) +
                userDto.getName() + getString(R.string.space) + getString(R.string.from_drug_profile);
        msg.setText(msgStr);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        callDeleteDrug(dId);
                    }
                });

        alertDialogBuilder.setCancelable(false)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void callDeleteDrug(final long id) {
        progress.show();
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(AddDrugActivity.this);
        GenericRequest<APIMessageResponse> deleteDrugRequest = new GenericRequest<APIMessageResponse>
                (Request.Method.DELETE, APIUrls.get().getDeleteDrug(userDto.getId(), id),
                        APIMessageResponse.class, null,
                        new Response.Listener<APIMessageResponse>() {
                            @Override
                            public void onResponse(APIMessageResponse userDrugDtoList) {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                AppUtils.isDataChanged = true;
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(AddDrugActivity.this, error, authExpiredCallback);
                                AppUtils.openSnackBar(parentLayout, res);
                            }
                        });
        authExpiredCallback.setRequest(deleteDrugRequest);
        ApiService.get().addToRequestQueue(deleteDrugRequest);
    }

    public interface SearchUserDtoCallback {
        public void selectUserDto(UserDto userDto);
    }

    public interface MonthlyTimeCallback {
        void timeDto(List<MonthlyDays> monthlyDaysList);
    }

    public interface WeekTimeCallback {
        void timeDto(List<WeeklyDays> weeklyDaysList);
    }

    public interface TimeCallback {
        void timeDto(List<Time> timeList);
    }

    // new code


    private void performBackButtonClickEvent() {
        if (isEditDrug) {
            AppUtils.logEvent(Constants.EDIT_DRUG_BACK_BUTTON_CLICKED);
            finish();
        } else {
            AppUtils.logEvent(Constants.ADD_DRUG_BACK_BUTTON_CLICKED);
            //  checkIfNeedToShowDialogBoxForVideo();
            finish();
        }
    }

    private void checkIfNeedToShowDialogBoxForVideo() {
        AppUtils.logEvent(Constants.ADD_DRUG_SHOW_ADD_DRUG_VIDEO);
        if (isUserTryStandardDrug && userDto != null) {
            boolean standardDrugAdded = ApplicationDB.get().isStandardDrugAdded(userDto.getId());
            if (!standardDrugAdded) {
                showDialogBoxForVideo(getString(R.string.need_help_adding_standard_drug), getString(R.string.add_drug_video));
            } else {
                finish();
            }
        } else if (isUserTrySmartSchedule && userDto != null) {
            boolean customDrugAdded = ApplicationDB.get().isCustomDrugAdded(userDto.getId());
            if (!customDrugAdded) {
                showDialogBoxForVideo(getString(R.string.need_help_adding_custom_drug), getString(R.string.add_smart_drug_video));
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void showDialogBoxForVideo(String msgStr, String videoUrl) {
        LayoutInflater layoutInflater = LayoutInflater.from(AddDrugActivity.this);
        View promptView = layoutInflater.inflate(R.layout.video_alert_dialog_box, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddDrugActivity.this);
        alertDialogBuilder.setView(promptView);

        TextView msgTv = promptView.findViewById(R.id.msg);
        msgTv.setText(msgStr);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(getString(R.string.watch_video), (dialog, id) -> {
                    AppUtils.logEvent(Constants.VIDEO_POPUP_WATCHVIDEO_BTN_CLK);
                   /* Intent intent = new Intent(AddDrugActivity.this, FullScreenActivity.class);
                    intent.putExtra(Constants.VIDEO_URL, videoUrl);
                    startActivity(intent);
                    finish();*/
                });

        alertDialogBuilder.setCancelable(false)
                .setNegativeButton(getString(R.string.cancel), (dialog, id) -> {
                    dialog.dismiss();
                    finish();
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            performBackButtonClickEvent();
        }

        if (item.getItemId() == R.id.done) {
            if (isEditDrug) {
                AppUtils.logEvent(Constants.EDIT_DRUG_DONE_BUTTON_CLICKED);
            } else {
                AppUtils.logEvent(Constants.ADD_DRUG_DONE_BUTTON_CLICKED);
            }

            checkFields();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkFields() {
        AddDrugRequest addDrugRequest = new AddDrugRequest();
        AddDrugDto addDrugDto = new AddDrugDto();
        addDrugDto.setCustom(isCustomSchedule);

        if (isEditDrug) {
            addDrugDto.setDrugId(userDrugDto.getDrugId());
            addDrugDto.setId(userDrugDto.getId());
            addDrugDto.setUserId(userDto.getId());
            addDrugDto.setKymSearchId(userDrugDto.getDrugId());

            Log.i("checkdruglist", "drug request model = 1 " + new Gson().toJson(addDrugDto));
            proceedToAddDrug(addDrugDto, addDrugRequest);

        } else {
            if (isFromDetailScreen) {
                if (drugDtoFromDetailsScreen != null) {
                    addDrugDto.setDrugId(drugDtoFromDetailsScreen.getId());
                    addDrugDto.setId(drugDtoFromDetailsScreen.getId());
                    addDrugDto.setUserId(userDto.getId());
                    addDrugDto.setKymSearchId(drugDtoFromDetailsScreen.getId());
                    Log.i("checkdruglist", "drug request model = 2 " + new Gson().toJson(addDrugDto));
                    proceedToAddDrug(addDrugDto, addDrugRequest);

                } else {
                    AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_valid_drug));
                }
            } else {
                if (drugDto == null) {
                    String userInput = drugSearchAc.getText().toString().trim();
                    if (userInput.length() > 0) {
                        addDrugDto.setUserId(userDto.getId());
                        addDrugDto.setUserEnteredDrugName(userInput);
                        Log.i("checkdruglist", "drug request model = 3 " + new Gson().toJson(addDrugDto));
                        proceedToAddDrug(addDrugDto, addDrugRequest);
                    } else {
                        AppUtils.openSnackBar(parentLayout, getString(R.string.please_enter_valid_drug));
                    }
                } else {
                    if (drugDto != null) {
                        addDrugDto.setDrugId(drugDto.getMappedId());
                        addDrugDto.setId(drugDto.getId());
                        addDrugDto.setKymSearchId(drugDto.getId());
                        addDrugDto.setUserId(userDto.getId());
                        Log.i("checkdruglist", "drug request model = 4 " + new Gson().toJson(addDrugDto));
                        proceedToAddDrug(addDrugDto, addDrugRequest);

                    } else {
                        AppUtils.openSnackBar(parentLayout, getString(R.string.please_select_valid_drug));
                    }
                }
            }
        }
    }

    private void proceedToAddDrug(AddDrugDto addDrugDto, AddDrugRequest addDrugRequest) {
        String dName = drugForSearch;

        ArrayList<String> dd = new ArrayList<>();
        if (!morning  && !afternoon && !evening && !night) {
            AppUtils.openSnackBar(parentLayout, "Please select timing");

        } else {
            if (morning) {
                dd.add("MD");
            }
            if (afternoon) {
                dd.add("AD");
            }
            if (evening) {
                dd.add("ED");
            }
            if (night) {
                dd.add("ND");
            }

            /*if(endDateTextView.getText().toString().equalsIgnoreCase(startDateTextView.getText().toString())){
                AppUtils.openSnackBar(parentLayout, getString(R.string.select_different_date));
            }else  */if(startDateTextView.getText().toString().compareToIgnoreCase(endDateTextView.getText().toString()) <=0){
                if (startDateTextView != null) {
                    String endDate = endDateTextView.getText().toString();
                    addDrugRequest.timing.addAll(dd);


                    if (endDate != null){
                        addDrugRequest.endDate = endDateString;


                        Log.i("checkdruglist", "drug request model = 623 " + dName);
                        if (dosageSpinner.getSelectedItem().toString().equalsIgnoreCase("भोजन के बाद")) {
                            addDrugRequest.whenToTake = "PC";

                        } else if (dosageSpinner.getSelectedItem().toString().equalsIgnoreCase("भोजन से पहले")) {
                            addDrugRequest.whenToTake = "AC";

                        } else  if (dosageSpinner.getSelectedItem().toString().equalsIgnoreCase("जरूरत पड़ने पर")) {
                            addDrugRequest.whenToTake = "SOS";

                        } else {
                            addDrugRequest.whenToTake = "HS";

                        }

                    } else {
                        AppUtils.openSnackBar(parentLayout, getString(R.string.select_end_date));
                    }

                    String startDate = startDateTextView.getText().toString();
                    String dosageEditTextStr = dosageEditText.getText().toString().trim();
                    Log.i("checkmodrug", "drug request size =  " + String.valueOf(adddrug.size()));
                    Log.i("checkmodrug", "drug request dName =  " + String.valueOf(dName));

                    if(adddrug.size()>0){
                        for (int i = 0; i < adddrug.size(); i++) {
                            if (adddrug.get(i).medicineName.equalsIgnoreCase(dName)) {
                                Log.i("checkmodrug", "drug request id =  " + String.valueOf(adddrug.get(i).id));
                                addDrugRequest.drugId = adddrug.get(i).id;
                                addDrugRequest.dosageType = adddrug.get(i).type;
                            }
                        }

                    }


                    //  addDrugRequest.drugId = dName;
                    if (startDate.length() > 0) {
                        addDrugDto.setStartDate(startDateString);
                        addDrugRequest.startDate = startDateString;
                        // 1) Set dosageForm and dosage.
                        if (isTablet) {
                            addDrugRequest.dosageForm="Tablet";
                        } else if (isInjection) {
                            addDrugRequest.dosageForm="Injection";
                        } else if (isLiquid) {
                            addDrugRequest.dosageForm="Liquid";
                        } else if (isOther) {
                            addDrugRequest.dosageForm="Other";

//                            if (dosageEditTextStr.length() > 0) {
//                                addDrugDto.setDosage(dosageEditTextStr);
//                            }
                        }
                    /*    if (isTablet) {
                            addDrugDto.setDosageForm("Tablet");
                        } else if (isInjection) {
                            addDrugDto.setDosageForm("Injection");
                        } else if (isLiquid) {
                            addDrugDto.setDosageForm("Liquid");
                        } else if (isOther) {
                            addDrugDto.setDosageForm("Other");

                            if (dosageEditTextStr.length() > 0) {
                                addDrugDto.setDosage(dosageEditTextStr);
                            }
                        }

                        if (!isOther) {
                            if (isCantFindYourDosage) {
                                if (dosageEditTextStr.length() > 0) {
                                    addDrugDto.setDosage(dosageEditTextStr);
                                }
                            } else {
                                Object dosageString = dosageSpinner.getSelectedItem();
                                if (dosageString != null) {
                                    addDrugDto.setDosage(dosageString.toString());
                                }
                            }
                        }*/

                        // 2) Set reminder list based on selected frequency and standard or custom schedule.
                        String dosageTypeStr = frequencySpinner.getSelectedItem().toString();
                        if (dosageTypeStr != null) {
                            Log.i("Login_response", "dosageTypeStr 2 = " + dosageTypeStr);
                            if (dosageTypeStr.equalsIgnoreCase("रोज")) {
                                addDrugDto.setDosageType("daily");
                            } else if (dosageTypeStr.equalsIgnoreCase("साप्ताहिक")) {
                                addDrugDto.setDosageType("weekly");
                            } else if (dosageTypeStr.equalsIgnoreCase("महीने के")) {
                                addDrugDto.setDosageType("monthly");
                            } else if (dosageTypeStr.equalsIgnoreCase("जैसी जरूरत थी")) {
                                addDrugDto.setDosageType("as needed");
                            }


                            if (isCustomSchedule) {
                                addDrugDto.setReminderList(customDosageReminderDtoList);
                            } else {
                                if (dosageTypeStr.equalsIgnoreCase(Constants.DAILY)) {

                                    if (timeGridAdapter != null) {
                                        List<Time> timeList = timeGridAdapter.getList();
                                        List<ReminderDto> reminderDtoList = new ArrayList<>();
                                        ReminderDto reminderDto;
                                        if (timeList != null && timeList.size() > 0) {

                                            for (int i = 0; i < timeList.size(); i++) {
                                                Time time = timeList.get(i);
                                                if (time.isSelected()) {
                                                    reminderDto = new ReminderDto();

                                                    reminderDto.setValue(null);
                                                    reminderDto.setReminderTime(time.get_24HrsFormat());
                                                    reminderDtoList.add(reminderDto);
                                                }
                                            }
                                            addDrugDto.setReminderList(reminderDtoList);
                                        }
                                    }

                                } else if (dosageTypeStr.equalsIgnoreCase(Constants.WEEKLY)) {

                                    if (timeGridAdapter != null && timeGridWeeklyAdapter != null) {
                                        String selectedTime = "";

                                        List<Time> timeList = timeGridAdapter.getList();
                                        if (timeList != null && timeList.size() > 0) {
                                            for (int i = 0; i < timeList.size(); i++) {
                                                Time time = timeList.get(i);
                                                if (time.isSelected()) {
                                                    selectedTime = time.get_24HrsFormat();
                                                    break;
                                                }
                                            }
                                        }

                                        List<ReminderDto> reminderDtoList = new ArrayList<>();
                                        ReminderDto reminderDto;

                                        List<WeeklyDays> weeklyDaysList = timeGridWeeklyAdapter.getList();
                                        if (weeklyDaysList != null && weeklyDaysList.size() > 0) {
                                            for (int i = 0; i < weeklyDaysList.size(); i++) {
                                                WeeklyDays weeklyDays = weeklyDaysList.get(i);
                                                if (weeklyDays.isSelected()) {

                                                    reminderDto = new ReminderDto();
                                                    reminderDto.setValue(weeklyDays.getFullName());
                                                    reminderDto.setReminderTime(selectedTime);

                                                    reminderDtoList.add(reminderDto);
                                                }
                                            }
                                        }

                                        addDrugDto.setReminderList(reminderDtoList);
                                    }


                                } else if (dosageTypeStr.equalsIgnoreCase(Constants.MONTHLY)) {

                                    if (timeGridAdapter != null && timeGridMonthlyAdapter != null) {


                                        String selectedTime = "";

                                        List<Time> timeList = timeGridAdapter.getList();
                                        if (timeList != null && timeList.size() > 0) {
                                            for (int i = 0; i < timeList.size(); i++) {
                                                Time time = timeList.get(i);
                                                if (time.isSelected()) {
                                                    selectedTime = time.get_24HrsFormat();
                                                    break;
                                                }
                                            }
                                        }


                                        List<MonthlyDays> monthlyDaysList = timeGridMonthlyAdapter.getList();
                                        List<ReminderDto> reminderDtoList = new ArrayList<>();
                                        ReminderDto reminderDto;

                                        if (monthlyDaysList != null && monthlyDaysList.size() > 0) {
                                            for (int i = 0; i < monthlyDaysList.size(); i++) {
                                                MonthlyDays monthlyDays = monthlyDaysList.get(i);

                                                if (monthlyDays.isSelected()) {
                                                    reminderDto = new ReminderDto();

                                                    reminderDto.setValue(monthlyDays.getDay() + "");
                                                    reminderDto.setReminderTime(selectedTime);

                                                    reminderDtoList.add(reminderDto);
                                                }
                                            }
                                        }

                                        addDrugDto.setReminderList(reminderDtoList);
                                    }
                                } else if (dosageTypeStr.equalsIgnoreCase(Constants.AS_NEEDED)) {
                                    Log.i("Login_response", "dosageTypeStr 3 = " + dosageTypeStr);

                                    addDrugDto.setDosageType("as needed");

                                    /*addDrugDto.setDosageType(dosageTypeStr.toLowerCase());*/

                                    addDrugDto.setReminderList(new ArrayList<ReminderDto>());
                                }
                            }

                            if (refillDto != null) {
                                addDrugDto.setPrescriptionRefillId(refillDto.getId());
                            }


                            if (isOther) {
                                checkDosageField(dosageEditTextStr, addDrugDto, addDrugRequest);
                            } else {
                                if (isCantFindYourDosage) {
                                    checkDosageField(dosageEditTextStr, addDrugDto, addDrugRequest);
                                } else {
                                    Log.i("checkdruglist", "drug request model = 6 " + new Gson().toJson(addDrugDto) + ", new drug = " + new Gson().toJson(addDrugRequest));
                                    callDrugAPI(addDrugDto, addDrugRequest);
                                }
                            }
                        }
                    } else {
                        AppUtils.openSnackBar(parentLayout, getString(R.string.select_start_date));
                    }
                }
            }else {
                AppUtils.openSnackBar(parentLayout, getString(R.string.select_different_date_one));
            }

            //Toast.makeText(AddDrugActivity.this, "", Toast.LENGTH_SHORT).show();
        }

    }

    private void checkDosageField(String dosageEditTextStr, AddDrugDto addDrugDto, AddDrugRequest request) {
        if (dosageEditTextStr.length() > 0) {
            Log.i("checkdruglist", "drug request model = 5 " + new Gson().toJson(addDrugDto));
            callDrugAPI(addDrugDto, request);
        } else {
            AppUtils.openSnackBar(parentLayout, getString(R.string.please_enter_valid_dosage));
        }
    }
    public void refreshToken(){
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken(sharedPreferences.getString("refreshToken",""));
        Log.i("refreshToken", "refreshToken api request 278 = " + new Gson().toJson(request));

        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null,false);
        service.refreshToken(request).enqueue(new Callback<RefreshTokenResponse>() {
            @Override
            public void onResponse(Call<RefreshTokenResponse> call, retrofit2.Response<RefreshTokenResponse> response) {
                Log.i("refreshToken", "prescription api response 0121 code = " + response.code());
                if (response.isSuccessful()) {
                    Log.i("refreshToken", "prescription api response = " + new Gson().toJson(response.body()));

                    edit.putString("Ptoken", response.body().accessToken);
                    edit.apply();
                    AppUtils.openSnackBar(parentLayout, "Try Again");
                } else {
                    edit.clear();
                    edit.apply();
                    Intent intent = new Intent(AddDrugActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                Log.i("checkmodeldata", "api error message response  = " + t.getMessage());
            }
        });

    }
    private void callDrugAPI(AddDrugDto addDrugDto, AddDrugRequest request) {
        Log.i("checkmodrug", "drug request model = 589 " + new Gson().toJson(request));
        Log.i("checkmodrug", "drug request user id = 589 " + sharedPreferences.getString("kymPid", "134388"));
        String token = "Bearer " + sharedPreferences.getString("Ptoken", "134388");
        UserService service = ServiceGeneratorTwo.createService(UserService.class, null, null, false);
        service.addDrug(sharedPreferences.getString("kymPid", "134388"), request,token).enqueue(new Callback<AddDrugResponse>() {
            @Override
            public void onResponse(Call<AddDrugResponse> call, retrofit2.Response<AddDrugResponse> response) {
                Log.i("checkmodrug", "api login response 0 code = " + response.code());
                Log.i("checkmodrug", "api login response  = " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Log.i("checkmodrug", "api login LoginNewResponse response = " + response.body().message);

                    AppUtils.openSnackBar(parentLayout, response.body().message);
                    onBackPressed();
                } else if(response.code() ==401){
                    refreshToken();
                }else {
                    AppUtils.openSnackBar(parentLayout, "Please try after some time.");
                    Log.i("checkmodrug", "api response 1 code = " + response.code());

                }
            }

            @Override
            public void onFailure(Call<AddDrugResponse> call, Throwable t) {
                AppUtils.openSnackBar(parentLayout, t.getMessage());
                Log.i("checkmodrug", "api error message response  = " + t.getMessage());
            }
        });
    }


    private void callUpdateDrugAPI(AddDrugDto addDrugDto) {
        AppUtils.logEvent(Constants.EDIT_DRUG_FORM_SUBMITTED);
        progress.show();
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(AddDrugActivity.this);
        GenericRequest<UserDrugDto> addDrugRequest = new GenericRequest<UserDrugDto>
                (Request.Method.PUT, APIUrls.get().EditDrug(userDto.getId()),
                        UserDrugDto.class, addDrugDto,
                        new Response.Listener<UserDrugDto>() {
                            @Override
                            public void onResponse(UserDrugDto uDDto) {
                                authExpiredCallback.hideProgressBar();

                                uDDto.setUserName(userDto.getName());

                                ApplicationDB.get().updateDrug(uDDto, userDto.getId());
                                AppUtils.hideProgressBar(progress);

                                // Solve error for dosage not found
                                AppUtils.setAlarmForDrug(AddDrugActivity.this, uDDto, userDrugDto, uDDto.getPrescriptionRefill());

                                //   setResult(DrugFragment.EDIT_DRUG);
                                AppUtils.isDataChanged = true;
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                authExpiredCallback.hideProgressBar();
                                AppUtils.hideProgressBar(progress);
                                String res = AppUtils.getVolleyError(AddDrugActivity.this, error, authExpiredCallback);
                                AppUtils.openSnackBar(parentLayout, res);
                            }
                        });
        authExpiredCallback.setRequest(addDrugRequest);
        ApiService.get().addToRequestQueue(addDrugRequest);
    }

    private void callAddDrugAPI(final AddDrugDto addDrugDto) {
        Log.i("Login_response", "addDrugDto 2 = " + new Gson().toJson(addDrugDto));
        Log.i("Login_response", "Id 2 = " + userDto.getId());
        AppUtils.logCleverTapEvent(AddDrugActivity.this,
                Constants.STANDARD_MEDICINE_FORM_SUBMITTED, null);
        progress.show();
        final AuthExpiredCallback authExpiredCallback = new AuthExpiredCallback(AddDrugActivity.this);
        GenericRequest<UserDrugDto> addDrugRequest = new GenericRequest<UserDrugDto>
                (Request.Method.POST, APIUrls.get().AddDrug(userDto.getId()),
                        UserDrugDto.class, addDrugDto,
                        userDrugDto -> {
                            // 1) Hide progress bar from screen.
                            authExpiredCallback.hideProgressBar();
                            AppUtils.hideProgressBar(progress);

                            userDrugDto.setUserName(userDto.getName());

                            // 2) Add or update all values got from serer.
                            ApplicationDB.get().upsertDrug(AddDrugActivity.this, userDrugDto, userDto.getId(), userDto.getName(), refillDto);

                            // 3) Log events.
//                            AppUtils.logDrugAddedRequestEvent(userDrugDto.getDrugName());
//                            AppUtils.logReminderAdded(userDrugDto.getDrugName(), addDrugDto.getUserDrugDosages());
                            if (userDrugDto.getDisplayName() != null) {
                                AppUtils.logDrugAddedRequestEvent(userDrugDto.getDisplayName());
                                AppUtils.logReminderAdded(userDrugDto.getDisplayName(), addDrugDto.getUserDrugDosages());
                            }

                            // 4) Call check diseases API.
                            //callCheckDiseasesAPI(userDto.getId(), userDrugDto.getDrugId(), userDrugDto.getSource(), userDrugDto.getDrugName());

                           /* //added this line for checking drug notification code
                            AppUtils.setAlarmForDrug(AddDrugActivity.this, userDrugDto, userDrugDto, userDrugDto.getPrescriptionRefill());*/


                            /**
                             * When you enable callCheckDiseasesAPI() please remove finishTask()
                             * because it is already covered in callCheckDiseasesAPI() flow.
                             */

                            AppUtils.isDataChanged = true;
                            finishTask();
                        },
                        error -> {
                            authExpiredCallback.hideProgressBar();
                            AppUtils.hideProgressBar(progress);
                            String res = AppUtils.getVolleyError(AddDrugActivity.this, error, authExpiredCallback);
                            Log.i("Login_response", "error message  = " + res);
                            AppUtils.openSnackBar(parentLayout, res + " Lucky");
                        });
        authExpiredCallback.setRequest(addDrugRequest);
        ApiService.get().addToRequestQueue(addDrugRequest);
    }

    private void finishTask() {
        /*    if (isFromDetailScreen) {*/
        Intent intent = new Intent(AddDrugActivity.this, HomeActivity.class);
            /*intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(Constants.OPEN_DRUG_FRAGMENT, Constants.OPEN_DRUG_FRAGMENT);*/
        startActivity(intent);
     /*   } else {
            setResult(DrugFragment.ADD_DRUG);
            finish();
        }*/
    }
}