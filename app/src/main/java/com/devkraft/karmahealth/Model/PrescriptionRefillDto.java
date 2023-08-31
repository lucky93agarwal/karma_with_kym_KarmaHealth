package com.devkraft.karmahealth.Model;

import java.util.ArrayList;
import java.util.Objects;

public class PrescriptionRefillDto {

    private Long id;
    private String key;
    private int order;
    private String value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrescriptionRefillDto that = (PrescriptionRefillDto) o;
        return order == that.order &&
                Objects.equals(id, that.id) &&
                Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, key, order, value);
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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static class PrescriptionRefillDtoList extends ArrayList<PrescriptionRefillDto> {
    }
}
