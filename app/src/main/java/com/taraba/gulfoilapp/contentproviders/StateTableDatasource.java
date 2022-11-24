package com.taraba.gulfoilapp.contentproviders;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.taraba.gulfoilapp.util.StateDistrict;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android3 on 12/24/15.
 */
public class StateTableDatasource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {Database.STATE_TABLE_TRNO,
            Database.STATE_TABLE_STATE};

    public StateTableDatasource(Context context) {
        dbHelper = DatabaseHelper.sharedInstance(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private void saveLastID(int id, Context context) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(
                Database.APP_NAMESPACE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Database.LATEST_CIRCULAR_ID, id);
        editor.commit();
    }

    // Method to insert the news date and news Description into the news table.
    public void insertIntoCircular(JSONObject news) throws JSONException {
        StateDistrict savedNews = getCircularByID(String.valueOf(news.getString("id")));
        if (savedNews == null) {
            ContentValues values = new ContentValues();

            values.put(Database.STATE_TABLE_DISTRICT, news.getString("postContent"));
            values.put(Database.STATE_TABLE_STATE, news.getString("postTitle"));
            values.put(Database.STATE_TABLE_TRNO, news.getString("id"));
            Long res = database.insert(Database.STATE_TABLE, null, values);
            System.out.println(res + " ");
        } else {
            ContentValues values = new ContentValues();

            values.put(Database.STATE_TABLE_DISTRICT, news.getString("postContent"));
            values.put(Database.STATE_TABLE_STATE, news.getString("postTitle"));
            int res = database.update(Database.STATE_TABLE, values, Database.STATE_TABLE_TRNO + " = ?",
                    new String[]{String.valueOf(news.getString("id"))});
            System.out.println(res + " ");
        }
    }

    public void insertBulkCircluar(JSONArray jsonArray, Context context) throws JSONException {
        int index = 0;
        for (index = 0; index < jsonArray.length(); index++) {
            insertIntoCircular(jsonArray.getJSONObject(index));
        }

        index--;

        if (index > 0) {
            int id = jsonArray.getJSONObject(index).getInt("id");
            saveLastID(id, context);
        }

    }

    public void setTempData() {

        Log.e("in SetTem Data", "Set temp Data");
        ContentValues values = new ContentValues();
        values.put(Database.STATE_TABLE_TRNO, 1);
        values.put(Database.STATE_TABLE_STATE, "MH");
        values.put(Database.STATE_TABLE_DISTRICT, "Nashik");


        database.insert(Database.STATE_TABLE, null, values);

        ContentValues values1 = new ContentValues();
        values1.put(Database.STATE_TABLE_TRNO, 2);
        values1.put(Database.STATE_TABLE_STATE, "MH");
        values1.put(Database.STATE_TABLE_DISTRICT, "Pune");


        database.insert(Database.STATE_TABLE, null, values1);

        ContentValues values2 = new ContentValues();
        values2.put(Database.STATE_TABLE_TRNO, 3);
        values2.put(Database.STATE_TABLE_STATE, "Goa");
        values2.put(Database.STATE_TABLE_DISTRICT, "panji");


        database.insert(Database.STATE_TABLE, null, values2);

        ContentValues values3 = new ContentValues();
        values3.put(Database.STATE_TABLE_TRNO, 4);
        values3.put(Database.STATE_TABLE_STATE, "Goa");
        values3.put(Database.STATE_TABLE_DISTRICT, "Panji1");


        database.insert(Database.STATE_TABLE, null, values3);


    }

    public List<StateDistrict> getAllCirculars() {
        List<StateDistrict> news = new ArrayList<StateDistrict>();
        Cursor cursor = database.rawQuery("select * from "
                + Database.STATE_TABLE + " ORDER BY " + Database.STATE_TABLE_STATE + " DESC", null);
        while (cursor.moveToNext()) {

            StateDistrict _news = new StateDistrict();
            String id = cursor.getString(cursor
                    .getColumnIndex(Database.STATE_TABLE_TRNO));
            String state = cursor.getString(cursor
                    .getColumnIndex(Database.STATE_TABLE_STATE));
            String district = cursor.getString(cursor
                    .getColumnIndex(Database.STATE_TABLE_DISTRICT));


            _news.setId(Integer.parseInt(id));
            _news.setStatename(state);
            _news.setDistrictname(district);

            news.add(_news);

        }
        return news;
    }

    public StateDistrict getCircularByID(String _id) {
        StateDistrict circular = null;
        Cursor cursor = database.rawQuery("select * from "
                + Database.STATE_TABLE + " WHERE " + Database.STATE_TABLE_TRNO
                + " = " + _id, null);
        if (cursor.moveToFirst()) {
            circular = new StateDistrict();
            String id = cursor.getString(cursor
                    .getColumnIndex(Database.STATE_TABLE_TRNO));
            String state = cursor.getString(cursor
                    .getColumnIndex(Database.STATE_TABLE_STATE));
            String district = cursor.getString(cursor
                    .getColumnIndex(Database.STATE_TABLE_DISTRICT));
            //circular.setId(Parse id);
            circular.setStatename(state);
            circular.setDistrictname(district);
            cursor.moveToNext();

        }
        return circular;
    }

    // Method to delete the News Table
    public void deleteUserTable() {
        database.delete(Database.STATE_TABLE, null, null);
    }

    // Method which returns true if the table is empty
    public boolean isCircularTableEmpty() {
        boolean bIsEmpty = false;

        Cursor cursor = database.query(Database.STATE_TABLE, allColumns, null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            bIsEmpty = false;
        } else {
            bIsEmpty = true;
        }
        return bIsEmpty;
    }

    public ArrayList<String> getDataFromTableForSpinner(String table_name, String[] field_name, String condition, String order_by) {
        ArrayList<String> db_arr_nemus = new ArrayList<String>();

        Cursor cursor = database.query(table_name, field_name, condition, null,
                null, null, order_by);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            db_arr_nemus.add(cursor.getString(0));
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return db_arr_nemus;
    }

    public ArrayList<String> getDataFromTableForSpinnerNew(String table_name, String[] field_name, String condition, String order_by) {
        ArrayList<String> db_arr_nemus = new ArrayList<String>();

        Cursor cursor = database.query(table_name, field_name, condition, null,
                null, null, order_by);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            db_arr_nemus.add(cursor.getString(0));
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return db_arr_nemus;
    }
}
