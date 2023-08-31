package com.devkraft.karmahealth.Model;

import java.util.List;

public class ReportDto {

    private List<TrackConfigurationDto> dates;

    public List<TrackConfigurationDto> getDates() {
        return dates;
    }

    public void setDates(List<TrackConfigurationDto> dates) {
        this.dates = dates;
    }
}
