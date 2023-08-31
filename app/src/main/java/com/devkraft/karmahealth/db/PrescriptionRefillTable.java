package com.devkraft.karmahealth.db;

public class PrescriptionRefillTable {

    public static final String TABLE_NAME = "prescriptionRefillTable";

    public static final String ID = "_id";

    public static final String IDENTIFIER = "identifier";
    public static final String TITLE = "title";
    public static final String SUB_TITLE = "subTitle";
    public static final String DISPLAY_NAME = "displayName";
    public static final String TIME = "time";
    public static final String DATE = "date";

    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            IDENTIFIER + " TEXT, " +
            TITLE + " TEXT, " +
            SUB_TITLE + " TEXT, " +
            DISPLAY_NAME + " TEXT, " +
            TIME + " LONG, " +
            DATE + " TEXT)";

    public static final String COLUMN_NAME_NULLABLE = null;
}
