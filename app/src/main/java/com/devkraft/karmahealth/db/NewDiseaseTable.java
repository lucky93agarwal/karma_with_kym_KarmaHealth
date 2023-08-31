package com.devkraft.karmahealth.db;

public class NewDiseaseTable {

    public static final String TABLE_NAME = "newDiseaseTable";

    public static final String ID = "_id";
    public static final String USER_ID = "userId";
    public static final String DISEASE = "disease";
    public static final String VACCINES = "vaccines";
    public static final String TESTS = "tests";
    public static final String VITALS = "vitals";


    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            USER_ID + " LONG, " +
            DISEASE + " LONG, " +
            VACCINES + " TEXT, " +
            TESTS + " TEXT, " +
            VITALS + " TEXT)";

    public static final String COLUMN_NAME_NULLABLE = null;


}
