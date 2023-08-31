package com.devkraft.karmahealth.db;

public class AlarmTable {
    public static final String TABLE_NAME = "alarmTable";

    public static final String ID = "_id";

    public static final String IDENTIFIER = "identifier";
    public static final String TITLE = "title";
    public static final String SUB_TITLE = "subTitle";
    public static final String TIME = "time";
    public static final String DATE = "date";
    public static final String REMIND_FLAG = "remindFlag";
    public static final String WEEK_DAY = "weekDay";
    public static final String MONTH_DAY = "monthDay";
    public static final String DRUG_ADHERENCE_DTO = "drugAdherenceDto";
    public static final String USER_ID = "userId";

    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            IDENTIFIER + " INTEGER, " +
            TITLE + " TEXT, " +
            SUB_TITLE + " TEXT, " +
            TIME + " TEXT, " +
            REMIND_FLAG + " INTEGER DEFAULT 1, "+
            DATE + " TEXT)";

    public static final String CREATE_TABLE_SQL_NEW = "CREATE TABLE " + TABLE_NAME +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            IDENTIFIER + " TEXT, " +
            TITLE + " TEXT, " +
            SUB_TITLE + " TEXT, " +
            TIME + " TEXT, " +
            REMIND_FLAG + " INTEGER DEFAULT 1, "+
            WEEK_DAY + " TEXT, " +
            MONTH_DAY + " TEXT, " +
            DATE + " TEXT, " +
            DRUG_ADHERENCE_DTO + " TEXT)";

    public static final String COLUMN_NAME_NULLABLE = null;
}
