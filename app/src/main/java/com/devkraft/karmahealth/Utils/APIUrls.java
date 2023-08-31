package com.devkraft.karmahealth.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class APIUrls {
    private static APIUrls instance;
    private String apiPrefix;
    private String apiPrefixV2;
    public String getDiseaseReminder(Long userId, Long parameterId) {
        return apiPrefix + "user/" + userId + "/parameter/" + parameterId;
    }
    public String getupdateNotificationStatus(Long id) {
        return apiPrefix + "notification/markAsRead/" + id;
    }
    public String CustomConditionSearch(Long id, String conditionSearchStr) {
        String searchKeyword = conditionSearchStr;
        try {
            searchKeyword = URLEncoder.encode(conditionSearchStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return apiPrefix + "user/" + id + "/disease-search?name=" + searchKeyword;
    }
    public String getEditParameterTrack(Long uId, long userParameterTrackingId) {
        return apiPrefix + "user/" + uId + "/user-parameter-tracking/" + userParameterTrackingId;
    }
    public String getAddCustomDisease(Long id) {
        return apiPrefix + "user/" + id + "/custom-disease";
    }
    public String getAddDisease(Long id) {
        return apiPrefix + "user/" + id + "/disease";
    }
    public String getDeleteDisease(Long userId, long userDiseaseId) {
        return apiPrefixV2 + "user/" + userId + "/disease/" + userDiseaseId;
    }
    public String getToggleAPIUrl(long userId) {
        return apiPrefix + "user/" + userId + "/parameter";
    }
    public String setToggleState(long userId, long userParameterId) {
        return apiPrefix + "user/" + userId + "/parameter/" + userParameterId + "/update-status";  //     /{userId}/parameter/{userParameterId}/update-status
    }

    public String addSymptomsAPI(long userId) {
        return apiPrefix + "symptoms/user/" + userId;
    }

    public String getSymptomDelete(Long userId, Long symptomId) {
        return apiPrefix + "symptoms/" + symptomId + "/user/" + userId;
    }

    public String getUpdateSymptomTracking(Long userId) {
        return apiPrefix + "symptoms/user/" + userId + "/tracking";
    }
    public String getUserSelectedSymptoms(Long userId, String longFormatDate) {
        return apiPrefix + "symptoms/user/" + userId + "?date=" + longFormatDate;
    }
    public String getDiseaseSchedule(Long id) {
        return apiPrefix + "disease/" + id + "/parameters?newData=true";
    }
    public String getAddParameter(Long id) {
        return apiPrefix + "user/" + id + "/parameter";
    }
    public String updateFitBitMultipleParameter(Long userId) {
        return apiPrefix + "user/" + userId + "/multiple-parameter/track-multiple";
    }
    public String getConfigurationParameterTrack(Long userId, Long paraId) {
        return apiPrefixV2 + "user/" + userId + "/parameter/" + paraId + "/track";
    }
    public String getTodayAgenda(Long userId, String todayDate) {
        return apiPrefix + "user/" + userId + "/agenda?date=" + todayDate;
    }
    public String drugInTakeDetails(Long id) {
        return apiPrefix + "user/" + id + "/" + "drug-intake-by-details";
    }
    public String getAuthRefreshUrl() {
        return apiPrefix + "auth/access-token";
    }
    public String AddDrug(Long id) {
        return apiPrefixV2 + "user/" + id + "/drug";
    }
    public String EditDrug(Long id) {
        return apiPrefixV2 + "user/" + id + "/drug";
    }

    public APIUrls(String prefix, String urlPrefixV2) {
        this.apiPrefix = prefix;
        this.apiPrefixV2 = urlPrefixV2;
    }
    public String getAllDisease(Long id) {
        return apiPrefix + "user/" + id + "/diseases";
    }
    public static APIUrls init(String prefix, String urlPrefixV2) {
        instance = new APIUrls(prefix, urlPrefixV2);
        return instance;
    }
    public String prescriptionRefill() {
        return apiPrefix + "static/drop-down?key=REFILL_REMINDER_DURATION";
    }
    public String getDeleteDrug(Long userId, Long drugId) {
        return apiPrefixV2 + "user/" + userId + "/drug/" + drugId;
    }
    public String getDosage(String key) {
        return apiPrefix + "static/drop-down/?key=" + key;
    }
    public String DrugSearch(String drugSearchStr) {
        String searchKeyword = drugSearchStr;
        /*
        try {
            searchKeyword = URLEncoder.encode(drugSearchStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        return /*apiPrefixV2 + */"https://api.karmaprimaryhealthcare.in/api/auth/drug/druglist" + searchKeyword;
    }
    public static APIUrls get() {
        return instance;
    }

    public String SignIn() {
        return apiPrefix + "auth/signin";
    }
    public String getRequestedDisease(Long id) {
        return apiPrefix + "user/" + id + "/requestedDiseases";
    }
    public String getRequestedDrug(Long id) {
        return apiPrefix + "user/" + id + "/requestedDrug";
    }

    public String emilConfirm(Long id) {
        return apiPrefix + "user/" + id + "/resend-verify-user-link";
    }

    public String getReport(Long userId, Long userParameterId, String startDate, String endDate,
                            String order) {
        return apiPrefixV2 + "user/" + userId + "/parameter/" + userParameterId + "/report?startDate="
                + startDate + "&endDate=" + endDate + "&order=" + order;
    }

    public String getReport(Long userId, Long userParameterId, String startDate, String endDate,
                            String weight, String order) {
        return apiPrefixV2 + "user/" + userId + "/parameter/" + userParameterId + "/report?startDate=" + startDate +
                "&endDate=" + endDate + weight + "&order=" + order;
    }


    public String deleteVital(Long uId, Long userParameterTrackingId) {
        return apiPrefix + "user/" + uId + "/parameter/" + userParameterTrackingId + "/delete-tracking";
    }




    public String getAllParameters() {
        return apiPrefix + "disease/" + "getAllParameters";
    }

    public String parameterSearch(Long id, String strParameterTypeString, String parametersearchString) {
        return apiPrefix + "user/" + id + "/parameter/search?name=" + parametersearchString + "&parameterType=" + strParameterTypeString;
    }


    public String getParameterDetails(Long id) {
        return apiPrefix + "disease/parameter/" + id;
    }

    public String getPostalCodeDetails(Long pincode) {
        return apiPrefix + "pincode/" + pincode;
    }
    public String getSymptomsList(Long userId) {
        return apiPrefix + "symptoms/condition/search?key=&userId=" + userId;
    }



    public String getConfigurationParameter(Long userId, Long paraId) {
        return apiPrefixV2 + "user/" + userId + "/parameter/" + paraId;
    }

    public String getDeleteDiseaseParamaneter(Long userId, Long userParameterId) {
        return apiPrefix + "user/" + userId + "/parameter/" + userParameterId;
    }
    public String getReportDownload(Long userId, Long userParameterId, String startDate, String endDate,
                                    String order) {
        return apiPrefixV2 + "user/" + userId + "/parameter/" + userParameterId + "/report-download?startDate="
                + startDate + "&endDate=" + endDate + "&order=" + order;
    }
    public String getReportSymptomsList(Long userId, String date) {
        return apiPrefix + "symptoms/user/" + userId + "/symptom-report-download?startDate=" + date;
    }


    public String getDetailsDataOfSymptom(Long userSymptomId, String startDate, String endDate) {
        return apiPrefix + "symptoms/" + userSymptomId + "/tracking?startDate=" + startDate + "&endDate=" +
                endDate;
    }

    public String getReportForSymptoms(Long userId, Long userSymptomId, String startDate, String endDate) {
        return apiPrefix + "symptoms/user/" + userId + "/" + userSymptomId + "/report-download?startDate=" +
                startDate + "&endDate=" + endDate;
    }
    public String getDeleteNotification(Long id) {
        return apiPrefix + "notification/delete/" + id;
    }

    public String getMarkAllRead() {
        return apiPrefix + "notification/markAllAsRead";
    }
    public String getAllNotification(int notificationCount) {
        return apiPrefix + "notification?limit=10&start=" + notificationCount;
    }
}
