package com.devkraft.karmahealth.Model;

import java.util.List;

public class DrugDosageAdherence {

    private String scheduleDate;

    private List<DrugDosageAdherenceDTO> drugDosageAdherenceDTO;

    public List<DrugDosageAdherenceDTO> getDrugDosageAdherenceDTO() {
        return drugDosageAdherenceDTO;
    }

    public void setDrugDosageAdherenceDTO(List<DrugDosageAdherenceDTO> drugDosageAdherenceDTO) {
        this.drugDosageAdherenceDTO = drugDosageAdherenceDTO;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
}
