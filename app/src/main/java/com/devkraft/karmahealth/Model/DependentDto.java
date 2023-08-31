package com.devkraft.karmahealth.Model;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

public class DependentDto {

    private Long id;
    private Long ethnicityId;
    private String gender;
    private String name;
    private String email;
    private Long dateOfBirth;
    private String ethnicityName;
    private String avatarName;

    public DependentDto(Long id, Long ethnicityId, String gender, String name, String email,
                        Long dateOfBirth, String ethnicityName, String avatarName) {
        this.id = id;
        this.ethnicityId = ethnicityId;
        this.gender = gender;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.ethnicityName = ethnicityName;
        this.avatarName = avatarName;
    }

    public static DependentDto fromCursor(Cursor cursor) {
        return new DependentDto(cursor.getLong(cursor.getColumnIndex(DependentTable.SID)),
                cursor.getLong(cursor.getColumnIndex(DependentTable.ETHNICITY_ID)),
                cursor.getString(cursor.getColumnIndex(DependentTable.GENDER)),
                cursor.getString(cursor.getColumnIndex(DependentTable.NAME)),
                cursor.getString(cursor.getColumnIndex(DependentTable.EMAIL)),
                cursor.getLong(cursor.getColumnIndex(DependentTable.DATE_OF_BIRTH)),
                cursor.getString(cursor.getColumnIndex(DependentTable.ETHNICITY_NAME)),
                cursor.getString(cursor.getColumnIndex(DependentTable.AVATAR_NAME)));
    }

    public ContentValues toCV() {
        ContentValues cv = new ContentValues();

        cv.put(DependentTable.SID, id);
        cv.put(DependentTable.ETHNICITY_ID, ethnicityId);
        cv.put(DependentTable.GENDER, gender);
        cv.put(DependentTable.NAME, name);
        cv.put(DependentTable.EMAIL, email);
        cv.put(DependentTable.DATE_OF_BIRTH, dateOfBirth);
        cv.put(DependentTable.ETHNICITY_NAME, ethnicityName);
        cv.put(DependentTable.AVATAR_NAME,avatarName);

        return cv;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }



    public String getEthnicityName() {
        return ethnicityName;
    }

    public void setEthnicityName(String ethnicityName) {
        this.ethnicityName = ethnicityName;
    }

    public Long getEthnicityId() {
        return ethnicityId;
    }

    public void setEthnicityId(Long ethnicityId) {
        this.ethnicityId = ethnicityId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public static class DependentDtoList extends ArrayList<DependentDto> {
    }
}
