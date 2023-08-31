package com.devkraft.karmahealth.Model;

import java.util.ArrayList;
import java.util.Objects;

public class ReminderDto {

    private long id; // userDrugDosageId
    private String value;
    private boolean remindFlag;
    private String reminderTime;

    // These fields are for custom dosage.
    private String dosage;
    private String dosageForm;
    private String dosageType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isRemindFlag() {
        return remindFlag;
    }

    public void setRemindFlag(boolean remindFlag) {
        this.remindFlag = remindFlag;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getDosageType() {
        return dosageType;
    }

    public void setDosageType(String dosageType) {
        this.dosageType = dosageType;
    }

    public static class ReminderDtoList extends ArrayList<ReminderDto> {
    }

   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReminderDto that = (ReminderDto) o;
        return id == that.id &&
                remindFlag == that.remindFlag &&
                Objects.equals(value, that.value) &&
                Objects.equals(reminderTime, that.reminderTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, remindFlag, reminderTime);
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReminderDto that = (ReminderDto) o;
        return
                remindFlag == that.remindFlag &&
                        Objects.equals(value, that.value) &&
                        Objects.equals(reminderTime, that.reminderTime) &&
                        Objects.equals(dosage, that.dosage) &&
                        Objects.equals(dosageForm, that.dosageForm) &&
                        Objects.equals(dosageType, that.dosageType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(value, remindFlag, reminderTime, dosage, dosageForm, dosageType);
    }
}

