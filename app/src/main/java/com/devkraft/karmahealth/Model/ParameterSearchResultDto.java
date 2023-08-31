package com.devkraft.karmahealth.Model;

import java.util.ArrayList;
import java.util.Objects;

public class ParameterSearchResultDto {
    private long id;
    private String name;
    private String medicalParameterType;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicalParameterType() {
        return medicalParameterType;
    }

    public void setMedicalParameterType(String medicalParameterType) {
        this.medicalParameterType = medicalParameterType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterSearchResultDto)) return false;
        ParameterSearchResultDto that = (ParameterSearchResultDto) o;
        return getId() == that.getId() &&
                getName().equals(that.getName()) &&
                getMedicalParameterType().equals(that.getMedicalParameterType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getMedicalParameterType());
    }

    public static class ParameterSearchResultList extends ArrayList<ParameterSearchResultDto> {
    }
}
