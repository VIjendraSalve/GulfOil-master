package com.taraba.gulfoilapp.contentproviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by android3 on 12/24/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper holder;
    Context context;

    static DatabaseHelper sharedInstance(Context context) {
        if (holder == null) {
            holder = new DatabaseHelper(context);
        }
        return holder;
    }

    public DatabaseHelper(Context context) {
        super(context, Database.SCHOOL_DATABSE_NAME, null,
                Database.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
           /* db.execSQL(Database.NEWS_TABLE_CREATE_SCRIPT);
            db.execSQL(Database.EVENT_TABLE_CREATE_SCRIPT);
            db.execSQL(Database.SCHOOL_CATAGORY_TABLE_CREATE_SCRIPT);
            db.execSQL(Database.SCHOOL_TABLE_CREATE_SCRIPT);
            db.execSQL(Database.CHARCHA_NOTIFICATION_TABLE_CREATE);
            db.execSQL(Database.TIMETABLE_TABLE_CREATE_SCRIPT);
            db.execSQL(Database.EXAMTIME_TABLE_CREATE_SCRIPT);*/
            db.execSQL(Database.STATE_TABLE_CREATE_SCRIPT);
            db.execSQL(Database.USER_TABLE_CREATE_SCRIPT);
            db.execSQL(Database.HISTRY_TABLE_CREATE_SCRIPT);
            db.execSQL(Database.PARTICIPANT_CLAIM_HISTRY_TABLE_CREATE_SCRIPT);
            db.execSQL(Database.CATEGORY_TABLE_CREATE_SCRIPT);
            db.execSQL(Database.PRODUCT_TABLE_CREATE_SCRIPT);
            db.execSQL(Database.NOTIFICATION_TABLE_CREATE_SCRIPT);


//		db.execSQL(Database.EVENT_TIME_LINE_TABLE_CREATE_SCRIPT);
//		db.execSQL(Database.EVENT_CATEGORY_TABLE_CREATE_SCRIPT);
//		db.execSQL(Database.EVENT_CATEGORY_COLOR_TABLE_CREATE_SCRIPT);
//		db.execSQL(Database.EVENT_CATEGORY_MAPPING_TABLE_CREATE_SCRIPT);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*db.execSQL("DROP TABLE IF EXISTS " + Database.NEWS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Database.EVENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Database.EVENT_CATEGORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Database.TABLE_SCHOOL);
        db.execSQL("DROP TABLE IF EXISTS " + Database.CHARCHA_NOTIFICATION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Database.TIMETABLE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Database.EXAMTIME_TABLE);*/
        db.execSQL("DROP TABLE IF EXISTS " + Database.STATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Database.USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Database.HISTRY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Database.PARTICIPANT_CLAIM_HISTRY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Database.CATEGORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Database.PRODUCT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Database.NOTIFICATION_TABLE);
        SharedPreferences pref = context.getSharedPreferences(Database.APP_NAMESPACE,
                Context.MODE_PRIVATE);
        pref.edit().clear().commit();
//		db.execSQL("DROP TABLE IF EXISTS " + Database.EVENT_TIME_LINE_TABLE);
//		db.execSQL("DROP TABLE IF EXISTS " + Database.EVENT_CATEGORY_TABLE);
//		db.execSQL("DROP TABLE IF EXISTS " + Database.EVENT_CATEGORY_COLOR_TABLE);
//		db.execSQL("DROP TABLE IF EXISTS " + Database.EVENT_CATEGORY_MAPPING_TABLE);
        onCreate(db);
    }
}
