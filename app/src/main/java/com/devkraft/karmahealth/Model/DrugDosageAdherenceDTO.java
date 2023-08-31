package com.devkraft.karmahealth.Model;

public class DrugDosageAdherenceDTO {

    private Long userDrugId;
    private Long userDrugDosageId;
    private String time;
    private String scheduleDate;
    private boolean drugTaken;
    private String drugTakenDate;
    private String drugName;

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public Long getUserDrugId() {
        return userDrugId;
    }

    public void setUserDrugId(Long userDrugId) {
        this.userDrugId = userDrugId;
    }

    public Long getUserDrugDosageId() {
        return userDrugDosageId;
    }

    public void setUserDrugDosageId(Long userDrugDosageId) {
        this.userDrugDosageId = userDrugDosageId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public boolean isDrugTaken() {
        return drugTaken;
    }

    public void setDrugTaken(boolean drugTaken) {
        this.drugTaken = drugTaken;
    }

    public String getDrugTakenDate() {
        return drugTakenDate;
    }

    public void setDrugTakenDate(String drugTakenDate) {
        this.drugTakenDate = drugTakenDate;
    }
}
