package com.devkraft.karmahealth.Model;

import java.util.ArrayList;

public class GetSymptomReportResponseDTO {

    private Long id;
    private Long updatedTs;
    private Long createdTs;
    private String recordedDate;
    private Long userSymptomId;
    private String severityLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUpdatedTs() {
        return updatedTs;
    }

    public void setUpdatedTs(Long updatedTs) {
        this.updatedTs = updatedTs;
    }

    public Long getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(Long createdTs) {
        this.createdTs = createdTs;
    }

    public String getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(String recordedDate) {
        this.recordedDate = recordedDate;
    }

    public Long getUserSymptomId() {
        return userSymptomId;
    }

    public void setUserSymptomId(Long userSymptomId) {
        this.userSymptomId = userSymptomId;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public static class GetSymptomReportListResponseDTO extends ArrayList<GetSymptomReportResponseDTO> {

    }
}
