package com.devkraft.karmahealth.Model;

import java.util.ArrayList;
import java.util.Objects;

public class DiseasesDropDownDto {
    private Long id;
    private String name;

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


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiseasesDropDownDto diseasesDropDownDto = (DiseasesDropDownDto) o;
        return Objects.equals(id, diseasesDropDownDto.id);
    }

    public static class DiseasesDropDownDtoList extends ArrayList<DiseasesDropDownDto> {
    }
}
