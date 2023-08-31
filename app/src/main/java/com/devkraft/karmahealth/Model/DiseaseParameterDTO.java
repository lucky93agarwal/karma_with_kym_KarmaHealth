package com.devkraft.karmahealth.Model;

public class DiseaseParameterDTO {

    private Long parameterId;
    private String parameterName;
    private String parameterFrequency;
    private String frequencyUnit;
    private Double frequencyNumber;
    private boolean remindFlag;
    private double calculatedFrequency;
    private String parameterDescription;
    private String medicalParameterType;
    private String informationLink;

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterFrequency() {
        return parameterFrequency;
    }

    public void setParameterFrequency(String parameterFrequency) {
        this.parameterFrequency = parameterFrequency;
    }

    public String getFrequencyUnit() {
        return frequencyUnit;
    }

    public void setFrequencyUnit(String frequencyUnit) {
        this.frequencyUnit = frequencyUnit;
    }

    public Double getFrequencyNumber() {
        return frequencyNumber;
    }

    public void setFrequencyNumber(Double frequencyNumber) {
        this.frequencyNumber = frequencyNumber;
    }

    public boolean isRemindFlag() {
        return remindFlag;
    }

    public void setRemindFlag(boolean remindFlag) {
        this.remindFlag = remindFlag;
    }

    public double getCalculatedFrequency() {
        return calculatedFrequency;
    }

    public void setCalculatedFrequency(double calculatedFrequency) {
        this.calculatedFrequency = calculatedFrequency;
    }

    public String getParameterDescription() {
        return parameterDescription;
    }

    public void setParameterDescription(String parameterDescription) {
        this.parameterDescription = parameterDescription;
    }

    public String getMedicalParameterType() {
        return medicalParameterType;
    }

    public void setMedicalParameterType(String medicalParameterType) {
        this.medicalParameterType = medicalParameterType;
    }

    public String getInformationLink() {
        return informationLink;
    }

    public void setInformationLink(String informationLink) {
        this.informationLink = informationLink;
    }

}
