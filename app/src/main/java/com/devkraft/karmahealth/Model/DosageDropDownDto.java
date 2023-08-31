package com.devkraft.karmahealth.Model;

import java.util.ArrayList;

public class DosageDropDownDto {

    private Long id;
    private String key;
    private String value;
    private Long order;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public static class DosageDropDownDtoList extends ArrayList<DosageDropDownDto> {
    }
}

