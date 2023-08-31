package com.devkraft.karmahealth.db;

public class SymptomsTable {

    public static final String TABLE_NAME = "symptomsTable";

    public static final String ID = "_id";
    public static final String USER_ID = "userId";
    public static final String NAME = "name";
    public static final String SYMPTOM_ID = "symptomId";
    public static final String CONDITION = "condition";
    public static final String RECORDEDDATE = "recordedDate";
    public static final String USERSYMPTOMID = "userSymptomId";
    public static final String SYMPTOMSTYPE = "symptomsType";
    public static final String SEVERITYLEVEL = "severityLevel";
    public static final String MEASUREMENTTYPE = "measurementType";
    public static final String MEASUREMENTVALUE = "measurementValue";
    public static final String SELECTEDMEASUREMENTVALUES = "selectedMeasurementValues";


    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            USER_ID + " LONG," +
            NAME + " TEXT," +
            SYMPTOM_ID + " LONG, " +
            CONDITION + " TEXT, " +
            RECORDEDDATE + " TEXT, " +
            USERSYMPTOMID + " LONG, " +
            SYMPTOMSTYPE + " TEXT, " +
            SEVERITYLEVEL + " TEXT, " +
            MEASUREMENTTYPE + " TEXT, " +
            MEASUREMENTVALUE + " TEXT, " +
            SELECTEDMEASUREMENTVALUES + " TEXT )";

    public static final String COLUMN_NAME_NULLABLE = null;

}
