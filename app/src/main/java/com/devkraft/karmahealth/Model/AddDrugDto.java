package com.devkraft.karmahealth.Model;

import java.util.List;

public class AddDrugDto {

    private Long kymSearchId;
    private Long id;
    private Long drugId;
    private String drugFreq;
    private String startDate;
    private String endDate;
    private String dosage;
    private String route;
    private Long userId;
    private String source;
    private int dosageCount;
    private List<DosageDto> userDrugDosages;
    private String strength;
    private Long prescriptionRefillId;
    private String dosageForm;
    private String dosageType;
    private List<ReminderDto> reminderList;
    private PrescriptionRefillDto prescriptionRefill;
    private boolean custom;
    private String userEnteredDrugName;

    public Long getKymSearchId() {
        return kymSearchId;
    }

    public void setKymSearchId(Long kymSearchId) {
        this.kymSearchId = kymSearchId;
    }

    public boolean isCustom() {
        return custom;
    }

    public String getUserEnteredDrugName() {
        return userEnteredDrugName;
    }

    public void setUserEnteredDrugName(String userEnteredDrugName) {
        this.userEnteredDrugName = userEnteredDrugName;
    }

    public PrescriptionRefillDto getPrescriptionRefill() {
        return prescriptionRefill;
    }

    public void setPrescriptionRefill(PrescriptionRefillDto prescriptionRefill) {
        this.prescriptionRefill = prescriptionRefill;
    }

    public List<ReminderDto> getReminderList() {
        return reminderList;
    }

    public void setReminderList(List<ReminderDto> reminderList) {
        this.reminderList = reminderList;
    }

    public String getDosageType() {
        return dosageType;
    }

    public void setDosageType(String dosageType) {
        this.dosageType = dosageType;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public Long getPrescriptionRefillId() {
        return prescriptionRefillId;
    }

    public void setPrescriptionRefillId(Long prescriptionRefillId) {
        this.prescriptionRefillId = prescriptionRefillId;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public int getDosageCount() {
        return dosageCount;
    }

    public void setDosageCount(int dosageCount) {
        this.dosageCount = dosageCount;
    }

    public List<DosageDto> getUserDrugDosages() {
        return userDrugDosages;
    }

    public void setUserDrugDosages(List<DosageDto> userDrugDosages) {
        this.userDrugDosages = userDrugDosages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDrugId() {
        return drugId;
    }

    public void setDrugId(Long drugId) {
        this.drugId = drugId;
    }

    public String getDrugFreq() {
        return drugFreq;
    }

    public void setDrugFreq(String drugFreq) {
        this.drugFreq = drugFreq;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean getCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }
}

