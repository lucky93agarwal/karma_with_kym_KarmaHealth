package com.devkraft.karmahealth.Model;

import java.util.ArrayList;

public class UserSymptomTrackingRequest {

    private ArrayList<UserSymptomTrackingRequest.UserTracking> userSymptomTracking;

    public ArrayList<UserTracking> getUserSymptomTracking() {
        return userSymptomTracking;
    }

    public void setUserSymptomTracking(ArrayList<UserTracking> userSymptomTracking) {
        this.userSymptomTracking = userSymptomTracking;
    }

    public static class UserTracking {

        private String recordedDate;
        private Long userSymptomId;
        private String severityLevel;

        public String getSeverityLevel() {
            return severityLevel;
        }

        public void setSeverityLevel(String severityLevel) {
            this.severityLevel = severityLevel;
        }

        public String getRecordedDate() {
            return recordedDate;
        }

        public void setRecordedDate(String recordedDate) {
            this.recordedDate = recordedDate;
        }

        public Long getUserSymptomId() {
            return userSymptomId;
        }

        public void setUserSymptomId(Long userSymptomId) {
            this.userSymptomId = userSymptomId;
        }
    }

}
