package com.devkraft.karmahealth.Model;

public class MedicenListModel {
    public String id;
    public String mName;
    public String noofpills;
    public String time;
    public String drugForm;
    public String whenToTake;
    public Boolean check;
    public Boolean isDelete;
    public String url;


    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public String getDrugForm() {
        return drugForm;
    }

    public void setDrugForm(String drugForm) {
        this.drugForm = drugForm;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWhenToTake() {
        return whenToTake;
    }

    public void setWhenToTake(String whenToTake) {
        this.whenToTake = whenToTake;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getNoofpills() {
        return noofpills;
    }

    public void setNoofpills(String noofpills) {
        this.noofpills = noofpills;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
