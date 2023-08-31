package com.devkraft.karmahealth.Model;


public class DependentTable {

    public static final String TABLE_NAME = "dependentTable";

    public static final String ID = "_id";

    public static final String SID = "id";
    public static final String ETHNICITY_ID = "ethnicityId";
    public static final String GENDER = "gender";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String ETHNICITY_NAME = "ethnicityName";
    public static final String AVATAR_NAME = "avatarName";

    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            SID + " LONG, " +
            ETHNICITY_ID + " LONG, " +
            GENDER + " TEXT, " +
            NAME + " TEXT, " +
            EMAIL + " TEXT, " +
            DATE_OF_BIRTH + " LONG, " +
            ETHNICITY_NAME + " TEXT)";

    public static final String COLUMN_NAME_NULLABLE = null;

}
