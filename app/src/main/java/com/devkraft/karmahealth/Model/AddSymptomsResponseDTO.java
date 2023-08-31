package com.devkraft.karmahealth.Model;

import java.util.ArrayList;

public class AddSymptomsResponseDTO {

    private Long id;
    private Long userId;
    private Boolean delete;
    private Long updatedTs;
    private Long symptomId;
    private Long createdTs;
    private String measurementType;
    private String customSymptomsName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Long getUpdatedTs() {
        return updatedTs;
    }

    public void setUpdatedTs(Long updatedTs) {
        this.updatedTs = updatedTs;
    }

    public Long getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(Long symptomId) {
        this.symptomId = symptomId;
    }

    public Long getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(Long createdTs) {
        this.createdTs = createdTs;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    public String getCustomSymptomsName() {
        return customSymptomsName;
    }

    public void setCustomSymptomsName(String customSymptomsName) {
        this.customSymptomsName = customSymptomsName;
    }

    public static class AddSymptomsListResponseDTO extends ArrayList<AddSymptomsResponseDTO> {
    }
}
