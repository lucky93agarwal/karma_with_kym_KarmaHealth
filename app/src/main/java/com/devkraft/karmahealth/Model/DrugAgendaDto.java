package com.devkraft.karmahealth.Model;

public class DrugAgendaDto {

    private Long userDrugDosageId;
    private String drugName;
    private String dosageTiming;
    private String dosage;
    private Long userDrugId;
    private boolean drugTaken;
    private String displayName;

    public boolean isTaken() {
        return drugTaken;
    }

    public void setTaken(boolean drugTaken) {
        this.drugTaken = drugTaken;
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

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDosageTiming() {
        return dosageTiming;
    }

    public void setDosageTiming(String dosageTiming) {
        this.dosageTiming = dosageTiming;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
