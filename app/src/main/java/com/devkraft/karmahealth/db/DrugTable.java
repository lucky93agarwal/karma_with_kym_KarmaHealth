package com.devkraft.karmahealth.db;

public class DrugTable {
    public static final String TABLE_NAME = "drugTable";

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

    public static final String START_DATE_STR = "startDateStr";
    public static final String END_DATE_STR = "endDateStr";

    /*private int dosageCount;
    private List<DosageDto> userDrugDosages;
    private String strength;
*/
    public static final String DRUG_DOSAGE_COUNT = "drugDosageCount";

    public static final String STRENGTH = "strength";
    public static final String DRUG_DOSAGES = "drugDosages";


    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            SID + " LONG, " +
            DOSAGE + " TEXT, " +
            DRUG_NAME + " TEXT, " +
            START_DATE + " LONG, " +
            END_DATE + " LONG, " +
            DRUG_FREQ + " TEXT, " +
            SOURCE + " TEXT, " +
            USER_ID + " LONG, " +
            DRUG_ID + " LONG, " +
            COMBINATION_ID + " LONG, " +
            ROUTE + " TEXT)";

    public static final String COLUMN_NAME_NULLABLE = null;
}
