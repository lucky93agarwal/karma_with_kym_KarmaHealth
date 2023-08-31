package com.devkraft.karmahealth.Model;
public class Time {

    private String displayFormat;
    private String _24HrsFormat;
    private Long longTime;
    private boolean isSelected = false;
    private String withSecondFormat;

    public String getWithSecondFormat() {
        return withSecondFormat;
    }

    public void setWithSecondFormat(String withSecondFormat) {
        this.withSecondFormat = withSecondFormat;
    }

    public Long getLongTime() {
        return longTime;
    }

    public void setLongTime(Long longTime) {
        this.longTime = longTime;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public String get_24HrsFormat() {
        return _24HrsFormat;
    }

    public void set_24HrsFormat(String _24HrsFormat) {
        this._24HrsFormat = _24HrsFormat;
    }
}

