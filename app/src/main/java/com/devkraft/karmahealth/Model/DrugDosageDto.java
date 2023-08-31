package com.devkraft.karmahealth.Model;

import java.util.ArrayList;

public class DrugDosageDto {

    private String value;
    private Long id;
    private String key;
    private Integer order;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public static class DrugDosageDtoList extends ArrayList<DrugDosageDto> {
    }
}
