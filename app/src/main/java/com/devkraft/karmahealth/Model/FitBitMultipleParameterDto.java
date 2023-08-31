package com.devkraft.karmahealth.Model;

import java.util.ArrayList;
import java.util.List;

public class FitBitMultipleParameterDto {

    private Long parameterId;
    private List<FitBitMultipleParameter> readings;

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public List<FitBitMultipleParameter> getReadings() {
        return readings;
    }

    public void setReadings(List<FitBitMultipleParameter> readings) {
        this.readings = readings;
    }

    public static class FitBitMultipleParameter {
        private String maxBaselineDisplayName;
        private String source;
        private String recordedDate;
        private Long minBaselineValue;
        private String minBaselineDisplayName;
        private double maxBaselineValue;
        private String measurementUnit;

        public String getMaxBaselineDisplayName() {
            return maxBaselineDisplayName;
        }

        public void setMaxBaselineDisplayName(String maxBaselineDisplayName) {
            this.maxBaselineDisplayName = maxBaselineDisplayName;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getRecordedDate() {
            return recordedDate;
        }

        public void setRecordedDate(String recordedDate) {
            this.recordedDate = recordedDate;
        }

        public Long getMinBaselineValue() {
            return minBaselineValue;
        }

        public void setMinBaselineValue(Long minBaselineValue) {
            this.minBaselineValue = minBaselineValue;
        }

        public String getMinBaselineDisplayName() {
            return minBaselineDisplayName;
        }

        public void setMinBaselineDisplayName(String minBaselineDisplayName) {
            this.minBaselineDisplayName = minBaselineDisplayName;
        }

        public double getMaxBaselineValue() {
            return maxBaselineValue;
        }

        public void setMaxBaselineValue(double maxBaselineValue) {
            this.maxBaselineValue = maxBaselineValue;
        }

        public String getMeasurementUnit() {
            return measurementUnit;
        }

        public void setMeasurementUnit(String measurementUnit) {
            this.measurementUnit = measurementUnit;
        }
    }

    public static class FitBitMultipleParameterDtoList extends ArrayList<FitBitMultipleParameterDto> {
    }

    public static class FitBitMultipleParameterIdsDtoList extends ArrayList<Long> {
    }
}
