
package com.taraba.gulfoilapp.barcode;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.taraba.gulfoilapp.contentproviders.Database;
import com.taraba.gulfoilapp.contentproviders.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by android3 on 12/31/15.
 */

public final class HistoryManager {

    private static final String TAG = HistoryManager.class.getSimpleName();

    private static final int MAX_ITEMS = 2000;

    private static final String[] COLUMNS = {
            Database.TEXT_COL,
            Database.DISPLAY_COL,
            Database.FORMAT_COL,
            Database.TIMESTAMP_COL,
            Database.DETAILS_COL,
    };

    private static final String[] COUNT_COLUMN = {"COUNT(1)"};

    private static final String[] ID_COL_PROJECTION = {Database.ID_COL};
    private static final String[] ID_DETAIL_COL_PROJECTION = {Database.ID_COL, Database.DETAILS_COL};

    private final Activity activity;
    private final boolean enableHistory;

    public HistoryManager(Activity activity) {
        this.activity = activity;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        enableHistory = prefs.getBoolean(PreferencesActivity.KEY_ENABLE_HISTORY, true);
    }

    public boolean hasHistoryItems() {
        SQLiteOpenHelper helper = new DatabaseHelper(activity);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            cursor = db.query(Database.HISTRY_TABLE, COUNT_COLUMN, null, null, null, null, null);
            cursor.moveToFirst();
            return cursor.getInt(0) > 0;
        } finally {
            close(cursor, db);
        }
    }

    public List<HistoryItem> buildHistoryItems() {
        SQLiteOpenHelper helper = new DatabaseHelper(activity);
        List<HistoryItem> items = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            cursor = db.query(Database.HISTRY_TABLE, COLUMNS, null, null, null, null, Database.TIMESTAMP_COL + " DESC");
            while (cursor.moveToNext()) {
                String text = cursor.getString(0);
                String display = cursor.getString(1);
                String format = cursor.getString(2);
                long timestamp = cursor.getLong(3);
                String details = cursor.getString(4);
                Result result = new Result(text, null, null, BarcodeFormat.valueOf(format), timestamp);
                items.add(new HistoryItem(result, display, details));
            }
        } finally {
            close(cursor, db);
        }
        return items;
    }

    public HistoryItem buildHistoryItem(int number) {
        SQLiteOpenHelper helper = new DatabaseHelper(activity);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            cursor = db.query(Database.HISTRY_TABLE, COLUMNS, null, null, null, null, Database.TIMESTAMP_COL + " DESC");
            cursor.move(number + 1);
            String text = cursor.getString(0);
            String display = cursor.getString(1);
            String format = cursor.getString(2);
            long timestamp = cursor.getLong(3);
            String details = cursor.getString(4);
            Result result = new Result(text, null, null, BarcodeFormat.valueOf(format), timestamp);
            return new HistoryItem(result, display, details);
        } finally {
            close(cursor, db);
        }
    }

    public void deleteHistoryItem(int number) {
        SQLiteOpenHelper helper = new DatabaseHelper(activity);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
            cursor = db.query(Database.HISTRY_TABLE,
                    ID_COL_PROJECTION,
                    null, null, null, null,
                    Database.TIMESTAMP_COL + " DESC");
            cursor.move(number + 1);
            db.delete(Database.HISTRY_TABLE, Database.ID_COL + '=' + cursor.getString(0), null);
        } finally {
            close(cursor, db);
        }
    }

    public void addHistoryItem(Result result, ResultHandler handler, String strParticipant_id) {
        // Do not save this item to the history if the preference is turned off, or the contents are
        // considered secure.
        if (!activity.getIntent().getBooleanExtra(Intents.Scan.SAVE_HISTORY, true) ||
                handler.areContentsSecure() || !enableHistory) {
            return;
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        if (!prefs.getBoolean(PreferencesActivity.KEY_REMEMBER_DUPLICATES, false)) {
            deletePrevious(result.getText());
        }


        Log.e("part id :", "Part id in capture handler : " + strParticipant_id);

        Log.e("barcode :", "Captured barcode : " + result.getText());
        Log.e("part id :", "result text: " + result.getText());

        String res = "";
        //rawResult.toString().indexOf(",");
        if (result.getText().contains(",")) {
            res = result.getText().substring(0, result.getText().indexOf(","));
        } else {
            res = result.getText();
        }

        Log.e("part id :", "result substring text: " + res);

        ContentValues values = new ContentValues();
        values.put(Database.TEXT_COL, res);
        values.put(Database.FORMAT_COL, result.getBarcodeFormat().toString());
        values.put(Database.DISPLAY_COL, handler.getDisplayContents().toString());
        values.put(Database.TIMESTAMP_COL, System.currentTimeMillis());
        values.put(Database.HISTRY_PARTICIPANT_ID_COL, strParticipant_id);
        values.put(Database.HISTRY_PUBLISH_TYPE_COL, "unpublish");

        SQLiteOpenHelper helper = new DatabaseHelper(activity);
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            // Insert the new entry into the DB.
            db.insert(Database.HISTRY_TABLE, Database.TIMESTAMP_COL, values);
        } finally {
            close(null, db);
        }
    }

    public void addHistoryItemDetails(String itemID, String itemDetails) {
        // As we're going to do an update only we don't need need to worry
        // about the preferences; if the item wasn't saved it won't be udpated
        SQLiteOpenHelper helper = new DatabaseHelper(activity);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
            cursor = db.query(Database.HISTRY_TABLE,
                    ID_DETAIL_COL_PROJECTION,
                    Database.TEXT_COL + "=?",
                    new String[]{itemID},
                    null,
                    null,
                    Database.TIMESTAMP_COL + " DESC",
                    "1");
            String oldID = null;
            String oldDetails = null;
            if (cursor.moveToNext()) {
                oldID = cursor.getString(0);
                oldDetails = cursor.getString(1);
            }

            if (oldID != null) {
                String newDetails;
                if (oldDetails == null) {
                    newDetails = itemDetails;
                } else if (oldDetails.contains(itemDetails)) {
                    newDetails = null;
                } else {
                    newDetails = oldDetails + " : " + itemDetails;
                }
                if (newDetails != null) {
                    ContentValues values = new ContentValues();
                    values.put(Database.DETAILS_COL, newDetails);
                    db.update(Database.HISTRY_TABLE, values, Database.ID_COL + "=?", new String[]{oldID});
                }
            }

        } finally {
            close(cursor, db);
        }
    }

    private void deletePrevious(String text) {
        SQLiteOpenHelper helper = new DatabaseHelper(activity);
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.delete(Database.HISTRY_TABLE, Database.TEXT_COL + "=?", new String[]{text});
        } finally {
            close(null, db);
        }
    }

    public void trimHistory() {
        SQLiteOpenHelper helper = new DatabaseHelper(activity);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
            cursor = db.query(Database.HISTRY_TABLE,
                    ID_COL_PROJECTION,
                    null, null, null, null,
                    Database.TIMESTAMP_COL + " DESC");
            cursor.move(MAX_ITEMS);
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                Log.i(TAG, "Deleting scan history ID " + id);
                db.delete(Database.HISTRY_TABLE, Database.ID_COL + '=' + id, null);
            }
        } catch (SQLiteException sqle) {
            // We're seeing an error here when called in CaptureActivity.onCreate() in rare cases
            // and don't understand it. First theory is that it's transient so can be safely ignored.
            Log.w(TAG, sqle);
            // continue
        } finally {
            close(cursor, db);
        }
    }

    CharSequence buildHistory() {
        SQLiteOpenHelper helper = new DatabaseHelper(activity);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
            cursor = db.query(Database.HISTRY_TABLE,
                    COLUMNS,
                    null, null, null, null,
                    Database.TIMESTAMP_COL + " DESC");

            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
            StringBuilder historyText = new StringBuilder(1000);
            while (cursor.moveToNext()) {

                historyText.append('"').append(massageHistoryField(cursor.getString(0))).append("\",");
                historyText.append('"').append(massageHistoryField(cursor.getString(1))).append("\",");
                historyText.append('"').append(massageHistoryField(cursor.getString(2))).append("\",");
                historyText.append('"').append(massageHistoryField(cursor.getString(3))).append("\",");

                // Add timestamp again, formatted
                long timestamp = cursor.getLong(3);
                historyText.append('"').append(massageHistoryField(
                        format.format(new Date(timestamp)))).append("\",");

                // Above we're preserving the old ordering of columns which had formatted data in position 5

                historyText.append('"').append(massageHistoryField(cursor.getString(4))).append("\"\r\n");
            }
            return historyText;
        } finally {
            close(cursor, db);
        }
    }

    void clearHistory() {
        SQLiteOpenHelper helper = new DatabaseHelper(activity);
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.delete(Database.HISTRY_TABLE, null, null);
        } finally {
            close(null, db);
        }
    }

    static Uri saveHistory(String history) {
        File bsRoot = new File(Environment.getExternalStorageDirectory(), "BarcodeScanner");
        File historyRoot = new File(bsRoot, "History");
        if (!historyRoot.exists() && !historyRoot.mkdirs()) {
            Log.w(TAG, "Couldn't make dir " + historyRoot);
            return null;
        }
        File historyFile = new File(historyRoot, "history-" + System.currentTimeMillis() + ".csv");
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(historyFile), Charset.forName("UTF-8"));
            out.write(history);
            return Uri.parse("file://" + historyFile.getAbsolutePath());
        } catch (IOException ioe) {
            Log.w(TAG, "Couldn't access file " + historyFile + " due to " + ioe);
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }
    }

    private static String massageHistoryField(String value) {
        return value == null ? "" : value.replace("\"", "\"\"");
    }

    private static void close(Cursor cursor, SQLiteDatabase database) {
        if (cursor != null) {
            cursor.close();
        }
        if (database != null) {
            database.close();
        }
    }

}

