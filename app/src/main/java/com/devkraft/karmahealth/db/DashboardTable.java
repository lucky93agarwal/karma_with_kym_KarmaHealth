package com.devkraft.karmahealth.db;

public class DashboardTable {

    public static final String TABLE_NAME = "dashboardTable";

    public static final String ID = "_id";
    public static final String USER_ID = "userId";
    public static final String ASNEEDEDDRUGS = "asNeededDrugs";
    public static final String DRUGS = "drugs";
    public static final String VITALS = "vitals";
    public static final String TESTS = "tests";
    public static final String VACCINES = "vaccines";
    public static final String NOTHING_ADDED = "nothingAdded";
    public static final String ALL_DRUG_DONE = "allDrugDone";
    public static final String ALL_PARAMETER_DONE = "allParameterDone";

    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            USER_ID + " LONG, " +
            DRUGS + " TEXT, " +
            ASNEEDEDDRUGS + " TEXT, " +
            VITALS + " TEXT, " +
            TESTS + " TEXT, " +
            VACCINES + " TEXT, " +
            ALL_DRUG_DONE + " INTEGER DEFAULT 0, " +
            ALL_PARAMETER_DONE + " INTEGER DEFAULT 0, " +
            NOTHING_ADDED + " INTEGER DEFAULT 0)";

    public static final String COLUMN_NAME_NULLABLE = null;

}
