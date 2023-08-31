package com.devkraft.karmahealth.Model;

public class NewDrugTable {
    public static final String TABLE_NAME = "newDrugTable";

    public static final String ID = "_id";

    public static final String SID = "sid";
    public static final String DOSAGE = "dosage";
    public static final String DRUG_NAME = "drugName";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String DRUG_FREQ = "drugFreq";
    public static final String SOURCE = "source";
    public static final String USER_ID = "userId";
    public static final String DRUG_ID = "drugId";
    public static final String COMBINATION_ID = "combinationId";
    public static final String ROUTE = "route";
    public static final String DRUG_DOSAGE_COUNT = "drugDosageCount";
    public static final String STRENGTH = "strength";
    public static final String DRUG_DOSAGES = "drugDosages";
    public static final String USER_NAME = "userName";
    public static final String PRESCRIPTION_REFILL = "prescriptionRefill";

    public static final String QUANTITY = "quantity";
    public static final String UNIT = "unit";
    public static final String DOSAGE_TYPE = "dosageType";
    public static final String REMINDER_LIST = "reminderList";
    public static final String DOSAGE_FORM = "dosageForm";

    public static final String DOSAGE_DISPLAY_NAME = "dosageDisplayName";
    public static final String CUSTOM = "custom";
    public static final String URL = "url";
    public static final String CERNER = "cerner";

    //2.3.34
    public static final String DISPLAY_NAME = "displayName";


    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            SID + " LONG, " +
            DOSAGE + " TEXT, " +
            DRUG_NAME + " TEXT, " +
            START_DATE + " TEXT, " +
            END_DATE + " TEXT, " +
            DRUG_FREQ + " TEXT, " +
            SOURCE + " TEXT, " +
            USER_ID + " LONG, " +
            DRUG_ID + " LONG, " +
            COMBINATION_ID + " LONG, " +
            ROUTE + " TEXT, " +
            DRUG_DOSAGE_COUNT + " INTEGER, " +
            STRENGTH + " TEXT, " +
            DRUG_DOSAGES + " TEXT, " +
            USER_NAME + " TEXT, " +
            CUSTOM + " INTEGER, " +
            DISPLAY_NAME + " TEXT)";

    public static final String COLUMN_NAME_NULLABLE = null;

}

