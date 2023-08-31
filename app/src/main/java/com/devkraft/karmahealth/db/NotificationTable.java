package com.devkraft.karmahealth.db;

public class NotificationTable {
    public static final String TABLE_NAME = "notificationTable";


    public static final String ID = "_id";
    public static final String SID = "sid";
    public static final String ICON = "icon";
    public static final String BODY = "body";
    public static final String TITLE = "title";
    public static final String NOTIFICATION_TYPE= "notificationType";
    public static final String READ = "read";
    public static final String GENERIC_NAME_ID = "genericNameId";
    public static final String SEVERITY = "severity";
    public static final String CREATION_DATE = "createdDate";
    public static final String  URL = "url";
    public static final String DOCUMENT_ID = "documentId";
    public static final String COMBINATION_ID = "combinationId";
    public static final String PRODUCT_ID = "productId";
    public static final String NOTIFICATION_ID = "notificationId";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";

    public static final String HEADER = "header";
    public static final String NOTIFICATION_COLOR_CODE = "notificationColorCode";


    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
            "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            SID + " LONG, " +
            ICON + " TEXT, " +
            BODY + " TEXT, " +
            TITLE + " TEXT, " +
            NOTIFICATION_TYPE + " TEXT, " +
            READ + " INTEGER DEFAULT 0, "+
            GENERIC_NAME_ID + " LONG, " +
            SEVERITY + " LONG, " +
            CREATION_DATE + " LONG, " +
            URL + " TEXT, " +
            DOCUMENT_ID + " LONG, " +
            COMBINATION_ID + " LONG, " +
            PRODUCT_ID + " LONG)";

    public static final String COLUMN_NAME_NULLABLE = null;
}
