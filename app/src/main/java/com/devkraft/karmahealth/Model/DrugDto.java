package com.devkraft.karmahealth.Model;

import java.util.ArrayList;
import java.util.Objects;

public class DrugDto {

    private Long id;
    private String name;
    private Long combinationId;
    private String source;
    private Long mappedId;
    private boolean removed;
    private String url;
    private String cerner;
    private String displayName;
    private String countryCode;
    private String ingredients;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCerner() {
        return cerner;
    }

    public void setCerner(String cerner) {
        this.cerner = cerner;
    }

    public Long getMappedId() {
        return mappedId;
    }

    public void setMappedId(Long mappedId) {
        this.mappedId = mappedId;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(Long combinationId) {
        this.combinationId = combinationId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public static class DrugDtoList extends ArrayList<DrugDto> {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrugDto drugDto = (DrugDto) o;
        return Objects.equals(id, drugDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.name;
    }
}

