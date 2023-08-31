package com.devkraft.karmahealth.db;

public class WeightTable {

    public static final String TABLE_NAME = "weightTable";
    public static final String ID = "_id";
    public static final String HEIGHT = "height";
    public static final String RANGE = "range";

    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            HEIGHT + " INTEGER, " +
            RANGE + " TEXT)";

    public static final String COLUMN_NAME_NULLABLE = null;

}