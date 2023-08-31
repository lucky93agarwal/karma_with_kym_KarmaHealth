package com.devkraft.karmahealth.Model;

import java.util.Objects;

public class Values {

    private String title;
    private Double value;
    private String unit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Values values = (Values) o;
        return Objects.equals(title, values.title) &&
                Objects.equals(value, values.value) &&
                Objects.equals(unit, values.unit);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title, value, unit);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


}
