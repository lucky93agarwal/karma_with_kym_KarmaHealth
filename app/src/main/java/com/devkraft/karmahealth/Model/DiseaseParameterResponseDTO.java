package com.devkraft.karmahealth.Model;

import java.util.List;

public class DiseaseParameterResponseDTO {

    private Long diseaseId;
    private List<DiseaseParameterDTO> vitals;
    private List<DiseaseParameterDTO> tests;
    private List<DiseaseParameterDTO> vaccinations;
    private List<SymptomsDTO> symptoms;

    public Long getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Long diseaseId) {
        this.diseaseId = diseaseId;
    }

    public List<DiseaseParameterDTO> getVitals() {
        return vitals;
    }

    public void setVitals(List<DiseaseParameterDTO> vitals) {
        this.vitals = vitals;
    }

    public List<DiseaseParameterDTO> getTests() {
        return tests;
    }

    public void setTests(List<DiseaseParameterDTO> tests) {
        this.tests = tests;
    }

    public List<DiseaseParameterDTO> getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(List<DiseaseParameterDTO> vaccinations) {
        this.vaccinations = vaccinations;
    }

    public List<SymptomsDTO> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<SymptomsDTO> symptoms) {
        this.symptoms = symptoms;
    }
}
