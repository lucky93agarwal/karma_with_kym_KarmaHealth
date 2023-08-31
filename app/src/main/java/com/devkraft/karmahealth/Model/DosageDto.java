package com.devkraft.karmahealth.Model;

public class DosageDto {

    private Long userDrugId;
    private String reminderDesc;
    private boolean remindFlag;
    private String reminderTime;

    public Long getUserDrugId() {
        return userDrugId;
    }

    public void setUserDrugId(Long userDrugId) {
        this.userDrugId = userDrugId;
    }

    public String getReminderDesc() {
        return reminderDesc;
    }

    public void setReminderDesc(String reminderDesc) {
        this.reminderDesc = reminderDesc;
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
}
