package com.devkraft.karmahealth.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ApplicationDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 16;
    private static final String TAG = ApplicationDBHelper.class.getSimpleName();

    public ApplicationDBHelper(Context context, String appName) {
        super(context, appName + ".db", null, DATABASE_VERSION);
        Log.i("checkmodeldashboard", "Creating database: ApplicationDBHelper  = ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("checkmodeldashboard", "Creating database: DATABASE_VERSION  = ");
        Log.w(TAG, "onCreate(): Creating database: DATABASE_VERSION: "+DATABASE_VERSION);

//        // 1) Call create of all tables here
//        db.execSQL(DrugTable.CREATE_TABLE_SQL);
        db.execSQL(NotificationTable.CREATE_TABLE_SQL);
//        db.execSQL(NewsTable.CREATE_TABLE_SQL);
//        db.execSQL(ConditionTable.CREATE_TABLE_SQL);
        db.execSQL(AlarmTable.CREATE_TABLE_SQL);
        db.execSQL(SymptomsTable.CREATE_TABLE_SQL);
//
//        // 2) Upgrade schema code here
//        updateTableForVersionTwo(db);
//        updateTableForVersionThree(db);
//        updateTableForVersionFour(db);
//        updateTableForVersionFive(db);
//        updateTableForVersionSix(db);
//        updateTableForVersionSeven(db);
//        updateTableForVersionEight(db);
//
//        /**
//         * No need to update for version 8 because it was not used it has older table for disease.
//         */
//
//        /**
//         * No need to update for version 9 because schema change of NewDrugTable
//         * already added in create table SQL in updateTableForVersionTwo(db).
//         */
//        updateTableForVersionTen(db);
       updateTableForVersionEleven(db);
//        updateTableForVersionTwelve(db);
//        updateTableForVersionThirteen(db);
//        updateTableForVersionFourTeen(db);
////        updateTableForVersionSixTeen(db);
    }
    private void updateTableForVersionFifteen(SQLiteDatabase db) {
        db.execSQL(SymptomsTable.CREATE_TABLE_SQL);
    }

//    private void updateTableForVersionSixTeen(SQLiteDatabase db) {
//        db.execSQL("ALTER TABLE " + NewDrugTable.TABLE_NAME +
//                " ADD COLUMN " + NewDrugTable.DISPLAY_NAME + " TEXT ");
//
//        db.execSQL("ALTER TABLE " + DashboardTable.TABLE_NAME +
//                " ADD COLUMN " + DashboardTable.ASNEEDEDDRUGS + " TEXT ");
//    }
//
//
    private void updateTableForVersionEleven(SQLiteDatabase db) {
//        db.execSQL(WeightTable.CREATE_TABLE_SQL);
        Log.e("Login_response", "Creating database ");
        db.execSQL(NewDiseaseTable.CREATE_TABLE_SQL);
//        db.execSQL(DashboardTable.CREATE_TABLE_SQL);
//        db.execSQL(DashboardUpcomingAgendaTable.CREATE_TABLE_SQL);
    }
//
//    private void updateTableForVersionTen(SQLiteDatabase db) {
//        db.execSQL("ALTER TABLE " + DependentTable.TABLE_NAME +
//                " ADD COLUMN " + DependentTable.AVATAR_NAME + " TEXT ");
//
//    }
//
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("checkmodeldashboard", "Creating database: onUpgrade  = ");
        Log.w(TAG,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        if(oldVersion == 14 && newVersion == DATABASE_VERSION) {
            updateTableForVersionFifteen(db);
//            updateTableForVersionSixTeen(db);
        }
//        if (oldVersion == 1 && newVersion == 2) {
//            updateTableForVersionTwo(db);
//        }
//
//        if (oldVersion == 1 && newVersion == 3) {
//            updateTableForVersionTwo(db);
//            updateTableForVersionThree(db);
//        }
//
//        if (oldVersion == 1 && newVersion == 4) {
//            updateTableForVersionTwo(db);
//            updateTableForVersionThree(db);
//            updateTableForVersionFour(db);
//        }
//
//        if (oldVersion == 1 && newVersion == 5) {
//            updateTableForVersionTwo(db);
//            updateTableForVersionThree(db);
//            updateTableForVersionFour(db);
//            updateTableForVersionFive(db);
//        }
//
//        if (oldVersion == 1 && newVersion == 6) {
//            updateTableForVersionTwo(db);
//            updateTableForVersionThree(db);
//            updateTableForVersionFour(db);
//            updateTableForVersionFive(db);
//            updateTableForVersionSix(db);
//        }
//
//        if (oldVersion == 1 && newVersion == 7) {
//            updateTableForVersionTwo(db);
//            updateTableForVersionThree(db);
//            updateTableForVersionFour(db);
//            updateTableForVersionFive(db);
//            updateTableForVersionSix(db);
//            updateTableForVersionSeven(db);
//        }
//        /**
//         * No need to update for version 8 because it was not used it has older table for disease.
//         */
//
//        if (oldVersion == 1 && newVersion == 8) {
//            updateTableForVersionTwo(db);
//            updateTableForVersionThree(db);
//            updateTableForVersionFour(db);
//            updateTableForVersionFive(db);
//            updateTableForVersionSix(db);
//            updateTableForVersionSeven(db);
//            updateTableForVersionEight(db);
//            /**
//             * No need to update for version 9 because schema change of NewDrugTable
//             * already added in create table SQL in updateTableForVersionTwo(db).
//             */
//        }
//
//        if(oldVersion == 1 && newVersion == 10){
//            updateTableForVersionTwo(db);
//            updateTableForVersionThree(db);
//            updateTableForVersionFour(db);
//            updateTableForVersionFive(db);
//            updateTableForVersionSix(db);
//            updateTableForVersionSeven(db);
//            updateTableForVersionEight(db);
//            /**
//             * No need to update for version 9 because schema change of NewDrugTable
//             * already added in create table SQL in updateTableForVersionTwo(db).
//             */
//            updateTableForVersionTen(db);
//        }
//
//        if(oldVersion == 1 && newVersion == 11){
//            updateTableForVersionTwo(db);
//            updateTableForVersionThree(db);
//            updateTableForVersionFour(db);
//            updateTableForVersionFive(db);
//            updateTableForVersionSix(db);
//            updateTableForVersionSeven(db);
//            updateTableForVersionEight(db);
//            /**
//             * No need to update for version 9 because schema change of NewDrugTable
//             * already added in create table SQL in updateTableForVersionTwo(db).
//             */
//            updateTableForVersionTen(db);
//            updateTableForVersionEleven(db);
//        }
//
//        if(oldVersion == 1 && newVersion == DATABASE_VERSION){
//            updateTableForVersionTwo(db);
//            updateTableForVersionThree(db);
//            updateTableForVersionFour(db);
//            updateTableForVersionFive(db);
//            updateTableForVersionSix(db);
//            updateTableForVersionSeven(db);
//            updateTableForVersionEight(db);
//            /**
//             * No need to update for version 9 because schema change of NewDrugTable
//             * already added in create table SQL in updateTableForVersionTwo(db).
//             */
//            updateTableForVersionTen(db);
//            updateTableForVersionEleven(db);
//            updateTableForVersionTwelve(db);
//            updateTableForVersionThirteen(db);
//            updateTableForVersionFourTeen(db);
////            updateTableForVersionSixTeen(db);
//        }
//
//        if (oldVersion == 2 && newVersion == 3) {
//            updateTableForVersionThree(db);
//        }
//
//        if (oldVersion == 2 && newVersion == 4) {
//            updateTableForVersionThree(db);
//            updateTableForVersionFour(db);
//        }
//
//        if (oldVersion == 2 && newVersion == 5) {
//            updateTableForVersionThree(db);
//            updateTableForVersionFour(db);
//            updateTableForVersionFive(db);
//        }
//
//        if (oldVersion == 2 && newVersion == 6) {
//            updateTableForVersionThree(db);
//            updateTableForVersionFour(db);
//            updateTableForVersionFive(db);
//            updateTableForVersionSix(db);
//        }
//
//        if (oldVersion == 2 && newVersion == 7) {
//            updateTableForVersionThree(db);
//            updateTableForVersionFour(db);
//            updateTableForVersionFive(db);
//            updateTableForVersionSix(db);
//            updateTableForVersionSeven(db);
//        }
//
//        /**
//         * No need to update for version 8 because it was not used it has older table for disease.
//         */
//
//
//
//        if (oldVersion == 2 && newVersion == 10) {
//            updateTableForVersionThree(db);
//            updateTableForVersionFour(db);
//            updateTableForVersionFive(db);
//            updateTableForVersionSix(db);
//            updateTableForVersionSeven(db);
//            updateTableForVersionEight(db);
//            /**
//             * No need to update for version 9 because schema change of NewDrugTable
//             * already added in create table SQL in updateTableForVersionTwo(db).
//             */
//            updateTableForVersionTen(db);
//        }
//
//        if (oldVersion == 2 && newVersion == 11) {

    }


}
