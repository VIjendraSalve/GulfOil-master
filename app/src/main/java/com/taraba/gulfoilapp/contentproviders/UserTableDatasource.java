package com.taraba.gulfoilapp.contentproviders;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.taraba.gulfoilapp.adapter.BarcodeHistoryModel;
import com.taraba.gulfoilapp.adapter.ClaimHistoryModel;
import com.taraba.gulfoilapp.model.Category;
import com.taraba.gulfoilapp.model.NewNotification;
import com.taraba.gulfoilapp.model.Notification;
import com.taraba.gulfoilapp.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by android3 on 12/25/15.
 * Modified by Mansi
 * Modified by Chaitali on 09/02/2016 changes in insertIntoClaimHistry() and getClaimHistry() functions
 */
public class UserTableDatasource {


    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = {Database.USER_TABLE_TRNO,
            Database.USER_TABLE_FNAME};

    public UserTableDatasource(Context context) {
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
        editor.putInt(Database.LATEST_USER_ID, id);
        editor.commit();
    }

    // Method to insert the news date and news Description into the news table.
    public void insertIntoHomework(JSONObject news, String strClaimHistry) throws JSONException {
        UserModel savedNews = null;

        Log.e("insert :", "insert news : " + news);

        if (news.has("login_id_pk")) {
            savedNews = getHomeworkByID(String.valueOf(news.getString("login_id_pk")));

            if (savedNews == null) {
                ContentValues values = new ContentValues();
                Log.e("insert :", "insert login_id_pk : " + savedNews);


                values.put(Database.USER_TABLE_TRNO, news.getString("login_id_pk"));
                Log.e("Login Id :", "Login Id In Datasource : " + news.getString("login_id_pk"));
                if (news.has("image_path"))
                    values.put(Database.USER_TABLE_PICTURE, news.getString("image_path"));
                else
                    values.put(Database.USER_TABLE_PICTURE, "");
                values.put(Database.USER_TABLE_FNAME, news.getString("first_name"));

                values.put(Database.USER_TABLE_LNAME, news.getString("last_name"));
                values.put(Database.USER_TABLE_GENDER, news.getString("gender"));
                values.put(Database.USER_TABLE_NOMININAME, news.getString("nominee_name"));
                values.put(Database.USER_TABLE_NOMINIRELATION, news.getString("nominee_relation"));
                values.put(Database.USER_TABLE_MOTHERNAME, news.getString("mothers_maiden_name"));
                values.put(Database.USER_TABLE_WORKSHOPNAME, news.getString("workshop_name"));
                values.put(Database.USER_TABLE_STATE, news.getString("state"));
                values.put(Database.USER_TABLE_DISTRICT, news.getString("district"));

                values.put(Database.USER_TABLE_PINCODE, news.getString("pincode"));
                values.put(Database.USER_TABLE_TALUKA, news.getString("taluka"));
                values.put(Database.USER_TABLE_CITY, news.getString("city"));
                values.put(Database.USER_TABLE_SHOPADDR, news.getString("workshop_address"));
                values.put(Database.USER_TABLE_LANDMARK, news.getString("landmark"));
                values.put(Database.USER_TABLE_MOBNO, news.getString("mobile_no"));
                values.put(Database.USER_TABLE_EMAIL, news.getString("email_id"));
                values.put(Database.USER_TABLE_SECTOR, news.getString("sector"));
                values.put(Database.USER_TABLE_SPECIALIZATION, news.getString("specialization"));

                values.put(Database.USER_TABLE_DOB, news.getString("dob"));
                values.put(Database.USER_TABLE_REGDATE, news.getString("record_date"));
                values.put(Database.USER_TABLE_TOTALSPERCONSPERMONTH, news.getString("total_consumption"));
                values.put(Database.USER_TABLE_SPERPARTCONFORMMVECPERMONTH, news.getString("spare_consumption"));
                values.put(Database.USER_TABLE_MMGENUSPARECONPERMONTH, news.getString("genuine_consumption"));
                values.put(Database.USER_TABLE_TOTALVEHICALPERMONTH, news.getString("total_vehicles"));
                values.put(Database.USER_TABLE_MAHINDRAVEHICALPERMONTH, news.getString("client_vehicles"));
                values.put(Database.USER_TABLE_NOOFMECHANICS, news.getString("no_of_mechanics"));
                values.put(Database.USER_TABLE_POINT, news.getString("points"));
                values.put(Database.USER_TABLE_STATUS, news.getString("status"));
                //     values.put(Database.USER_TABLE_TYPE, news.getString(""));
                values.put(Database.USER_TABLE_PARTICIPANTCODE, news.getString("participant_code"));
                values.put(Database.USER_TABLE_PARTICIPANt_ID_PK, news.getString("participant_id_pk"));
                values.put(Database.USER_TABLE_FORMFILLDATE, news.getString("form_fillup_date"));
                values.put(Database.USER_TABLE_PARTICIPANT_CLAIM_HISTORY, strClaimHistry);

                Long res = database.insert(Database.USER_TABLE, null, values);
                System.out.println(res + " ");
            } else {

                Log.e("insert :", "update login_id_pk : " + savedNews);

                ContentValues values = new ContentValues();
                values.put(Database.USER_TABLE_TRNO, news.getString("login_id_pk"));
                Log.e("Login Id :", "Login Id In Datasource : " + news.getString("login_id_pk"));
                if (news.has("image_path"))
                    values.put(Database.USER_TABLE_PICTURE, news.getString("image_path"));
                else
                    values.put(Database.USER_TABLE_PICTURE, "");
                values.put(Database.USER_TABLE_FNAME, news.getString("first_name"));
                values.put(Database.USER_TABLE_LNAME, news.getString("last_name"));
                if (news.has("gender"))
                    values.put(Database.USER_TABLE_GENDER, news.getString("gender"));
                else
                    values.put(Database.USER_TABLE_GENDER, "");
                if (news.has("nominee_name"))
                    values.put(Database.USER_TABLE_NOMININAME, news.getString("nominee_name"));
                else
                    values.put(Database.USER_TABLE_NOMININAME, "");
                values.put(Database.USER_TABLE_NOMINIRELATION, news.getString("nominee_relation"));
                values.put(Database.USER_TABLE_MOTHERNAME, news.getString("mothers_maiden_name"));
                values.put(Database.USER_TABLE_WORKSHOPNAME, news.getString("workshop_name"));
                values.put(Database.USER_TABLE_STATE, news.getString("state"));
                values.put(Database.USER_TABLE_DISTRICT, news.getString("district"));
                values.put(Database.USER_TABLE_PINCODE, news.getString("pincode"));
                values.put(Database.USER_TABLE_TALUKA, news.getString("taluka"));
                values.put(Database.USER_TABLE_CITY, news.getString("city"));
                values.put(Database.USER_TABLE_SHOPADDR, news.getString("workshop_address"));
                values.put(Database.USER_TABLE_LANDMARK, news.getString("landmark"));
                values.put(Database.USER_TABLE_MOBNO, news.getString("mobile_no"));
                values.put(Database.USER_TABLE_EMAIL, news.getString("email_id"));
                values.put(Database.USER_TABLE_SECTOR, news.getString("sector"));
                values.put(Database.USER_TABLE_SPECIALIZATION, news.getString("specialization"));
                values.put(Database.USER_TABLE_DOB, news.getString("dob"));
                values.put(Database.USER_TABLE_REGDATE, news.getString("record_date"));
                values.put(Database.USER_TABLE_TOTALSPERCONSPERMONTH, news.getString("total_consumption"));
                values.put(Database.USER_TABLE_SPERPARTCONFORMMVECPERMONTH, news.getString("spare_consumption"));
                values.put(Database.USER_TABLE_MMGENUSPARECONPERMONTH, news.getString("genuine_consumption"));
                values.put(Database.USER_TABLE_TOTALVEHICALPERMONTH, news.getString("total_vehicles"));
                values.put(Database.USER_TABLE_MAHINDRAVEHICALPERMONTH, news.getString("client_vehicles"));
                values.put(Database.USER_TABLE_NOOFMECHANICS, news.getString("no_of_mechanics"));
                values.put(Database.USER_TABLE_POINT, news.getString("points"));
                values.put(Database.USER_TABLE_STATUS, news.getString("status"));
                // values.put(Database.USER_TABLE_TYPE, news.getString(""));
                values.put(Database.USER_TABLE_PARTICIPANTCODE, news.getString("participant_code"));
                values.put(Database.USER_TABLE_PARTICIPANt_ID_PK, news.getString("participant_id_pk"));
                values.put(Database.USER_TABLE_FORMFILLDATE, news.getString("form_fillup_date"));
                values.put(Database.USER_TABLE_PARTICIPANT_CLAIM_HISTORY, strClaimHistry);

                int res = database.update(Database.USER_TABLE, values, Database.USER_TABLE_TRNO + " = ?",
                        new String[]{String.valueOf(news.getString("login_id_pk"))});
                System.out.println(res + " ");
            }

        } else {
            savedNews = getHomeworkByID(String.valueOf(news.getString("login_id")));

            Log.e("insert :", "insert login_id if : " + savedNews);
            if (savedNews == null) {
                ContentValues values = new ContentValues();
                Log.e("insert :", "insert login_id : " + savedNews);
                values.put(Database.USER_TABLE_TRNO, news.getString("login_id"));
                Log.e("Login Id :", "Login Id In Datasource : " + news.getString("login_id"));

                values.put(Database.USER_TABLE_FNAME, news.getString("first_name"));
                values.put(Database.USER_TABLE_LNAME, news.getString("last_name"));
                values.put(Database.USER_TABLE_GENDER, "");
                values.put(Database.USER_TABLE_NOMININAME, "");
                values.put(Database.USER_TABLE_NOMINIRELATION, "");
                values.put(Database.USER_TABLE_MOTHERNAME, "");
                if (news.has("workshop_name")) {
                    values.put(Database.USER_TABLE_WORKSHOPNAME, news.getString("workshop_name"));
                } else {
                    values.put(Database.USER_TABLE_WORKSHOPNAME, "");
                }
                values.put(Database.USER_TABLE_STATE_MAPPED, news.getString("state_district_mapped"));
                values.put(Database.USER_TABLE_STATE, news.getString("state"));
                values.put(Database.USER_TABLE_DISTRICT, news.getString("district"));

                values.put(Database.USER_TABLE_PINCODE, "");
                values.put(Database.USER_TABLE_TALUKA, "");
                values.put(Database.USER_TABLE_CITY, "");
                values.put(Database.USER_TABLE_SHOPADDR, "");
                values.put(Database.USER_TABLE_LANDMARK, "");
                values.put(Database.USER_TABLE_MOBNO, news.getString("username"));
                values.put(Database.USER_TABLE_EMAIL, "");
                if (news.has("sector")) {
                    values.put(Database.USER_TABLE_SECTOR, news.getString("sector"));
                } else {
                    values.put(Database.USER_TABLE_SECTOR, "");
                }
                values.put(Database.USER_TABLE_SPECIALIZATION, "");

                values.put(Database.USER_TABLE_DOB, "");
                values.put(Database.USER_TABLE_REGDATE, "");
                values.put(Database.USER_TABLE_TOTALSPERCONSPERMONTH, "");
                values.put(Database.USER_TABLE_SPERPARTCONFORMMVECPERMONTH, "");
                values.put(Database.USER_TABLE_MMGENUSPARECONPERMONTH, "");
                values.put(Database.USER_TABLE_TOTALVEHICALPERMONTH, "");
                values.put(Database.USER_TABLE_MAHINDRAVEHICALPERMONTH, "");
                values.put(Database.USER_TABLE_NOOFMECHANICS, "");
                //values.put(Database.USER_TABLE_POINT, news.getString("trno"));
                values.put(Database.USER_TABLE_STATUS, news.getString("isSignedIn"));
                values.put(Database.USER_TABLE_TYPE, news.getString("type"));

                Long res = database.insert(Database.USER_TABLE, null, values);
                System.out.println(res + " ");
            } else {

                Log.e("insert :", "update login_id else : " + savedNews.getId());

                ContentValues values = new ContentValues();
                values.put(Database.USER_TABLE_TRNO, news.getString("login_id"));
                Log.e("Login Id :", "Login Id In Datasource : " + news.getString("login_id"));

                values.put(Database.USER_TABLE_FNAME, news.getString("first_name"));
                values.put(Database.USER_TABLE_LNAME, news.getString("last_name"));
                values.put(Database.USER_TABLE_GENDER, "");
                values.put(Database.USER_TABLE_NOMININAME, "");
                values.put(Database.USER_TABLE_NOMINIRELATION, "");
                values.put(Database.USER_TABLE_MOTHERNAME, "");
                if (news.has("workshop_name")) {
                    values.put(Database.USER_TABLE_WORKSHOPNAME, news.getString("workshop_name"));
                } else {
                    values.put(Database.USER_TABLE_WORKSHOPNAME, "");
                }
                values.put(Database.USER_TABLE_STATE_MAPPED, news.getString("state_district_mapped"));
                values.put(Database.USER_TABLE_STATE, news.getString("state"));
                values.put(Database.USER_TABLE_DISTRICT, news.getString("district"));

                values.put(Database.USER_TABLE_PINCODE, "");
                values.put(Database.USER_TABLE_TALUKA, "");
                values.put(Database.USER_TABLE_CITY, "");
                values.put(Database.USER_TABLE_SHOPADDR, "");
                values.put(Database.USER_TABLE_LANDMARK, "");
                values.put(Database.USER_TABLE_MOBNO, news.getString("username"));
                values.put(Database.USER_TABLE_EMAIL, "");
                if (news.has("sector")) {
                    values.put(Database.USER_TABLE_SECTOR, news.getString("sector"));
                } else {
                    values.put(Database.USER_TABLE_SECTOR, "");
                }
                values.put(Database.USER_TABLE_SPECIALIZATION, "");

                values.put(Database.USER_TABLE_DOB, "");
                values.put(Database.USER_TABLE_REGDATE, "");
                values.put(Database.USER_TABLE_TOTALSPERCONSPERMONTH, "");
                values.put(Database.USER_TABLE_SPERPARTCONFORMMVECPERMONTH, "");
                values.put(Database.USER_TABLE_MMGENUSPARECONPERMONTH, "");
                values.put(Database.USER_TABLE_TOTALVEHICALPERMONTH, "");
                values.put(Database.USER_TABLE_MAHINDRAVEHICALPERMONTH, "");
                values.put(Database.USER_TABLE_NOOFMECHANICS, "");
                values.put(Database.USER_TABLE_POINT, "");
                values.put(Database.USER_TABLE_STATUS, news.getString("isSignedIn"));
                values.put(Database.USER_TABLE_TYPE, news.getString("type"));

                int res = database.update(Database.USER_TABLE, values, Database.USER_TABLE_TRNO + " = ?",
                        new String[]{String.valueOf(news.getString("login_id"))});
                System.out.println(res + " ");
            }
        }
    }

    public void updateStattus(String trno, String status) {
        UserModel savedNews = getHomeworkByID(trno);
        if (savedNews == null) {

        } else {
            ContentValues values = new ContentValues();

            values.put(Database.USER_TABLE_STATUS, status);

            int res = database.update(Database.USER_TABLE, values, Database.USER_TABLE_TRNO + " = ?",
                    new String[]{String.valueOf(trno)});
            System.out.println(res + " ");
        }
    }

    public void insertBulkHomework(JSONArray jsonArray, Context context) throws JSONException {
        int index = 0;
        for (index = 0; index < jsonArray.length(); index++) {
            insertIntoHomework(jsonArray.getJSONObject(index), "");
        }

        index--;

        if (index > 0) {
            int id = jsonArray.getJSONObject(index).getInt("trno");
            saveLastID(id, context);
        }
    }

    public void insertBulkClaimHistry(JSONArray jsonArray, Context context, String strpartid) throws JSONException {
        int index = 0;
        for (index = 0; index < jsonArray.length(); index++) {
            Log.e("insertBulkClaimHistry: ", "" + jsonArray.getJSONObject(index).toString());
            insertIntoClaimHistry(jsonArray.getJSONObject(index), strpartid);
        }

        //index--;

       /* if(index > 0){
            int id  = jsonArray.getJSONObject(index).getInt("trno");
            saveLastID(id, context);
        }*/
    }

    public void setTempData() {
        ContentValues values = new ContentValues();

        values.put(Database.USER_TABLE_TRNO, 2);
        values.put(Database.USER_TABLE_PICTURE, "");
        values.put(Database.USER_TABLE_FNAME, "Shyam");
        values.put(Database.USER_TABLE_LNAME, "Borse");
        values.put(Database.USER_TABLE_GENDER, "Male");
        values.put(Database.USER_TABLE_NOMININAME, "aaa");
        values.put(Database.USER_TABLE_NOMINIRELATION, "ass");
        values.put(Database.USER_TABLE_MOTHERNAME, "sss");
        values.put(Database.USER_TABLE_WORKSHOPNAME, "Shyam Auto");
        values.put(Database.USER_TABLE_STATE, "maharashtra");
        values.put(Database.USER_TABLE_DISTRICT, "anjaw");

        values.put(Database.USER_TABLE_PINCODE, "1111");
        values.put(Database.USER_TABLE_TALUKA, "dfdf");
        values.put(Database.USER_TABLE_CITY, "fdf");
        values.put(Database.USER_TABLE_SHOPADDR, "dfdf");
        values.put(Database.USER_TABLE_LANDMARK, "dfdf");
        values.put(Database.USER_TABLE_MOBNO, "2222222222");
        values.put(Database.USER_TABLE_EMAIL, "dfd");
        values.put(Database.USER_TABLE_SECTOR, "dfdf");
        values.put(Database.USER_TABLE_SPECIALIZATION, "Engine");

        values.put(Database.USER_TABLE_DOB, "2016-01-12");
        values.put(Database.USER_TABLE_REGDATE, "2016-12-11");
        values.put(Database.USER_TABLE_TOTALSPERCONSPERMONTH, "dfdf");
        values.put(Database.USER_TABLE_SPERPARTCONFORMMVECPERMONTH, "fdf");
        values.put(Database.USER_TABLE_MMGENUSPARECONPERMONTH, "dfdf");
        values.put(Database.USER_TABLE_TOTALVEHICALPERMONTH, "fdf");
        values.put(Database.USER_TABLE_MAHINDRAVEHICALPERMONTH, "dfd");
        values.put(Database.USER_TABLE_NOOFMECHANICS, "fdfd");
        //values.put(Database.USER_TABLE_POINT, news.getString("trno"));
        values.put(Database.USER_TABLE_STATUS, "Active");
        values.put(Database.USER_TABLE_TYPE, "M");
        values.put(Database.USER_TABLE_PARTICIPANTCODE, "MA002");

        database.insert(Database.USER_TABLE, null, values);

        ContentValues values1 = new ContentValues();

        values1.put(Database.USER_TABLE_TRNO, 3);
        values1.put(Database.USER_TABLE_PICTURE, "");
        values1.put(Database.USER_TABLE_FNAME, "Mayuri");
        values1.put(Database.USER_TABLE_LNAME, "Kele");
        values1.put(Database.USER_TABLE_GENDER, "Female");
        values1.put(Database.USER_TABLE_NOMININAME, "aaa");
        values1.put(Database.USER_TABLE_NOMINIRELATION, "ass");
        values1.put(Database.USER_TABLE_MOTHERNAME, "sss");
        values1.put(Database.USER_TABLE_WORKSHOPNAME, "ddd");
        values1.put(Database.USER_TABLE_STATE, "maharashtra");
        values1.put(Database.USER_TABLE_DISTRICT, "anjaw");

        values1.put(Database.USER_TABLE_PINCODE, "11111");
        values1.put(Database.USER_TABLE_TALUKA, "dfdf");
        values1.put(Database.USER_TABLE_CITY, "fdf");
        values1.put(Database.USER_TABLE_SHOPADDR, "dfdf");
        values1.put(Database.USER_TABLE_LANDMARK, "dfdf");
        values1.put(Database.USER_TABLE_MOBNO, "3333333333");
        values1.put(Database.USER_TABLE_EMAIL, "dfd");
        values1.put(Database.USER_TABLE_SECTOR, "dfdf");
        values1.put(Database.USER_TABLE_SPECIALIZATION, "Engine");

        values1.put(Database.USER_TABLE_DOB, "2016-12-11");
        values1.put(Database.USER_TABLE_REGDATE, "2016-12-11");
        values1.put(Database.USER_TABLE_TOTALSPERCONSPERMONTH, "dfdf");
        values1.put(Database.USER_TABLE_SPERPARTCONFORMMVECPERMONTH, "fdf");
        values1.put(Database.USER_TABLE_MMGENUSPARECONPERMONTH, "dfdf");
        values1.put(Database.USER_TABLE_TOTALVEHICALPERMONTH, "fdf");
        values1.put(Database.USER_TABLE_MAHINDRAVEHICALPERMONTH, "dfd");
        values1.put(Database.USER_TABLE_NOOFMECHANICS, "fdfd");
        //values1.put(Database.USER_TABLE_POINT, news.getString("trno"));
        values1.put(Database.USER_TABLE_STATUS, "Active");
        values1.put(Database.USER_TABLE_TYPE, "M");
        values.put(Database.USER_TABLE_PARTICIPANTCODE, "MA003");

        database.insert(Database.USER_TABLE, null, values1);
    }

    public List<UserModel> getAllUserData() {
        ArrayList<UserModel> news = new ArrayList<UserModel>();
        Cursor cursor = database.rawQuery("select * from "
                + Database.USER_TABLE + " ORDER BY " + Database.USER_TABLE_TRNO + " DESC", null);
        while (cursor.moveToNext()) {

            UserModel _news = new UserModel();

            String id = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TRNO));
            String picture = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PICTURE));
            String fname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_FNAME));
            String lname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_LNAME));
            String gender = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_GENDER));
            String nomininame = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOMININAME));
            String nominirely = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOMINIRELATION));
            String mothername = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MOTHERNAME));
            String workshopname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_WORKSHOPNAME));
            String state = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATE));
            String state_mapped = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATE_MAPPED));
            String district = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_DISTRICT));
            String pincode = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PINCODE));


            String taluka = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TALUKA));
            String city = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_CITY));
            String shopadd = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SHOPADDR));
            String landmark = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_LANDMARK));
            String mobile_no = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MOBNO));
            String email = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_EMAIL));
            String sector = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SECTOR));
            String specialization = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SPECIALIZATION));

            String dob = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_DOB));
            String regdate = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_REGDATE));
            String toatalsperconpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TOTALSPERCONSPERMONTH));
            String sperpartconformmvehicpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SPERPARTCONFORMMVECPERMONTH));
            String mmgenuspareconpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MMGENUSPARECONPERMONTH));
            String totalvehicalpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TOTALVEHICALPERMONTH));
            String mahindravehicalpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MAHINDRAVEHICALPERMONTH));
            String noofmechanics = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOOFMECHANICS));

            String point = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_POINT));
            String status = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATUS));
            String type = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TYPE));

            String participant_code = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANTCODE));
            String participant_id_pk = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANt_ID_PK));
            String form_fillup_date = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_FORMFILLDATE));
            String participant_claim_history = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANT_CLAIM_HISTORY));


            _news.setId(Integer.parseInt(id));
            _news.setPicture(picture);
            _news.setUserfname(fname);
            _news.setUserlname(lname);
            _news.setGender(gender);
            _news.setNomini(nomininame);
            _news.setNominirely(nominirely);
            _news.setMothername(mothername);
            _news.setWorkshopname(workshopname);

            _news.setState(state);
            _news.setState_mapped(state_mapped);

            _news.setDistrict(district);
            _news.setPincode(pincode);
            _news.setTaluka(taluka);
            _news.setCity(city);
            _news.setShopadd(shopadd);
            _news.setLandmark(landmark);
            _news.setMobile_no(mobile_no);

            _news.setEmail(email);
            _news.setSector(sector);
            _news.setSpecialization(specialization);
            _news.setDob(dob);
            _news.setRegdate(regdate);
            _news.setToatalsperconpermonth(toatalsperconpermonth);
            _news.setSperpartconformmvehicpermonth(sperpartconformmvehicpermonth);
            _news.setMmgenuspareconpermonth(mmgenuspareconpermonth);
            _news.setTotalvehicalpermonth(totalvehicalpermonth);

            _news.setMahindravehicalpermonth(mahindravehicalpermonth);
            _news.setNoofmechanics(noofmechanics);
            _news.setBalance_points(point);
            _news.setStatus(status);
            _news.setType(type);

            _news.setParticipant_code(participant_code);
            _news.setParticipant_id_pk(participant_id_pk);
            _news.setForm_fillup_date(form_fillup_date);
            _news.setParticipant_claim_history(participant_claim_history);

            news.add(_news);

        }
        return news;
    }

    public List<UserModel> getAllUserDataAddress(int user_id) {
        ArrayList<UserModel> news = new ArrayList<UserModel>();
        Cursor cursor = database.rawQuery("select * from "
                + Database.USER_TABLE + " where " + Database.USER_TABLE_TRNO + "=" + user_id + " ORDER BY " + Database.USER_TABLE_TRNO + " DESC", null);
        while (cursor.moveToNext()) {

            UserModel _news = new UserModel();

            String id = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TRNO));
            String picture = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PICTURE));
            String fname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_FNAME));
            String lname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_LNAME));
            String gender = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_GENDER));
            String nomininame = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOMININAME));
            String nominirely = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOMINIRELATION));
            String mothername = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MOTHERNAME));
            String workshopname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_WORKSHOPNAME));
            String state = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATE));
            String state_mapped = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATE_MAPPED));
            String district = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_DISTRICT));
            String pincode = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PINCODE));


            String taluka = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TALUKA));
            String city = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_CITY));
            String shopadd = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SHOPADDR));
            String landmark = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_LANDMARK));
            String mobile_no = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MOBNO));
            String email = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_EMAIL));
            String sector = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SECTOR));
            String specialization = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SPECIALIZATION));

            String dob = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_DOB));
            String regdate = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_REGDATE));
            String toatalsperconpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TOTALSPERCONSPERMONTH));
            String sperpartconformmvehicpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SPERPARTCONFORMMVECPERMONTH));
            String mmgenuspareconpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MMGENUSPARECONPERMONTH));
            String totalvehicalpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TOTALVEHICALPERMONTH));
            String mahindravehicalpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MAHINDRAVEHICALPERMONTH));
            String noofmechanics = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOOFMECHANICS));

            String point = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_POINT));
            String status = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATUS));
            String type = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TYPE));

            String participant_code = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANTCODE));
            String participant_id_pk = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANt_ID_PK));
            String form_fillup_date = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_FORMFILLDATE));
            String participant_claim_history = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANT_CLAIM_HISTORY));


            _news.setId(Integer.parseInt(id));
            _news.setPicture(picture);
            _news.setUserfname(fname);
            _news.setUserlname(lname);
            _news.setGender(gender);
            _news.setNomini(nomininame);
            _news.setNominirely(nominirely);
            _news.setMothername(mothername);
            _news.setWorkshopname(workshopname);

            _news.setState(state);
            _news.setState_mapped(state_mapped);

            _news.setDistrict(district);
            _news.setPincode(pincode);
            _news.setTaluka(taluka);
            _news.setCity(city);
            _news.setShopadd(shopadd);
            _news.setLandmark(landmark);
            _news.setMobile_no(mobile_no);

            _news.setEmail(email);
            _news.setSector(sector);
            _news.setSpecialization(specialization);
            _news.setDob(dob);
            _news.setRegdate(regdate);
            _news.setToatalsperconpermonth(toatalsperconpermonth);
            _news.setSperpartconformmvehicpermonth(sperpartconformmvehicpermonth);
            _news.setMmgenuspareconpermonth(mmgenuspareconpermonth);
            _news.setTotalvehicalpermonth(totalvehicalpermonth);

            _news.setMahindravehicalpermonth(mahindravehicalpermonth);
            _news.setNoofmechanics(noofmechanics);
            _news.setBalance_points(point);
            _news.setStatus(status);
            _news.setType(type);

            _news.setParticipant_code(participant_code);
            _news.setParticipant_id_pk(participant_id_pk);
            _news.setForm_fillup_date(form_fillup_date);
            _news.setParticipant_claim_history(participant_claim_history);

            news.add(_news);

        }
        return news;
    }

    public ArrayList<BarcodeHistoryModel> getAllBarcode(String strParticipantid) {
        ArrayList<BarcodeHistoryModel> news = new ArrayList<BarcodeHistoryModel>();

        Cursor cursor = database.rawQuery("select * from "
                + Database.HISTRY_TABLE + " WHERE " + Database.HISTRY_PARTICIPANT_ID_COL
                + " = '" + strParticipantid + "'" + " and " + Database.HISTRY_PUBLISH_TYPE_COL
                + " = '" + "unpublish" + "'" + " ORDER BY " + Database.ID_COL + " DESC", null);

        while (cursor.moveToNext()) {
            BarcodeHistoryModel _news = new BarcodeHistoryModel();

            String id = cursor.getString(cursor
                    .getColumnIndex(Database.ID_COL));
            String text = cursor.getString(cursor
                    .getColumnIndex(Database.TEXT_COL));
            String format = cursor.getString(cursor
                    .getColumnIndex(Database.FORMAT_COL));
            String display = cursor.getString(cursor
                    .getColumnIndex(Database.DISPLAY_COL));
            String timestamp = cursor.getString(cursor
                    .getColumnIndex(Database.TIMESTAMP_COL));
            String details = cursor.getString(cursor
                    .getColumnIndex(Database.DETAILS_COL));
            String histry_participant_id = cursor.getString(cursor
                    .getColumnIndex(Database.HISTRY_PARTICIPANT_ID_COL));
            String barcode_publish_type = cursor.getString(cursor
                    .getColumnIndex(Database.HISTRY_PUBLISH_TYPE_COL));


            _news.setId(Integer.parseInt(id));
            _news.setText(text);
            _news.setFormat(format);
            _news.setDisplay(display);
            _news.setTimestamp(timestamp);
            _news.setDetails(details);
            _news.setHistryPartcipanID(histry_participant_id);
            _news.setHistryPublishType(barcode_publish_type);

            news.add(_news);

        }
        return news;
    }

    public ArrayList<String> getAllParticipantIds() {
        ArrayList<String> news = new ArrayList<String>();
        Cursor cursor = database.rawQuery("select " + Database.HISTRY_PARTICIPANT_ID_COL + " from "
                + Database.HISTRY_TABLE + " WHERE " + Database.HISTRY_PUBLISH_TYPE_COL
                + " = '" + "unpublish" + "'" + " ORDER BY " + Database.ID_COL + " DESC", null);

        while (cursor.moveToNext()) {
            String histry_participant_id = cursor.getString(cursor
                    .getColumnIndex(Database.HISTRY_PARTICIPANT_ID_COL));

            news.add(histry_participant_id);
        }
        return news;
    }

    public void removeBarCodeFromList(int id) {

        database.execSQL("delete from " + Database.HISTRY_TABLE + " where " + Database.ID_COL + "=" + id);
    }

    public void deleteAllProduct() {
        //FIRST TRUNCATE TABLE
        database.delete(Database.PRODUCT_TABLE, null, null);
    }

    public UserModel getHomeworkByID(String _id) {
        UserModel circular = null;
        Cursor cursor = database.rawQuery("select * from "
                + Database.USER_TABLE + " WHERE " + Database.USER_TABLE_TRNO
                + " = " + _id, null);
        if (cursor.moveToFirst()) {
            circular = new UserModel();

            String id = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TRNO));
            String picture = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PICTURE));
            String fname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_FNAME));
            String lname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_LNAME));
            String gender = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_GENDER));
            String nomininame = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOMININAME));
            String nominirely = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOMINIRELATION));
            String mothername = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MOTHERNAME));
            String workshopname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_WORKSHOPNAME));
            String state = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATE));
            String district = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_DISTRICT));
            String pincode = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PINCODE));


            String taluka = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TALUKA));
            String city = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_CITY));
            String shopadd = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SHOPADDR));
            String landmark = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_LANDMARK));
            String mobile_no = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MOBNO));
            String email = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_EMAIL));
            String sector = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SECTOR));
            String specialization = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SPECIALIZATION));

            String dob = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_DOB));
            String regdate = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_REGDATE));
            String toatalsperconpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TOTALSPERCONSPERMONTH));
            String sperpartconformmvehicpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SPERPARTCONFORMMVECPERMONTH));
            String mmgenuspareconpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MMGENUSPARECONPERMONTH));
            String totalvehicalpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TOTALVEHICALPERMONTH));
            String mahindravehicalpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MAHINDRAVEHICALPERMONTH));
            String noofmechanics = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOOFMECHANICS));

            String point = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_POINT));
            String status = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATUS));
            String type = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TYPE));

            String participant_code = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANTCODE));
            String participant_id_pk = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANt_ID_PK));
            String form_fillup_date = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_FORMFILLDATE));
            String participant_claim_history = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANT_CLAIM_HISTORY));

            circular.setId(Integer.parseInt(id));
            circular.setPicture(picture);
            circular.setUserfname(fname);
            circular.setUserlname(lname);
            circular.setGender(gender);
            circular.setNomini(nomininame);
            circular.setNominirely(nominirely);
            circular.setMothername(mothername);
            circular.setWorkshopname(workshopname);

            circular.setState(state);
            circular.setDistrict(district);
            circular.setPincode(pincode);
            circular.setTaluka(taluka);
            circular.setCity(city);
            circular.setShopadd(shopadd);
            circular.setLandmark(landmark);
            circular.setMobile_no(mobile_no);

            circular.setEmail(email);
            circular.setSector(sector);
            circular.setSpecialization(specialization);
            circular.setDob(dob);
            circular.setRegdate(regdate);
            circular.setToatalsperconpermonth(toatalsperconpermonth);
            circular.setSperpartconformmvehicpermonth(sperpartconformmvehicpermonth);
            circular.setMmgenuspareconpermonth(mmgenuspareconpermonth);
            circular.setTotalvehicalpermonth(totalvehicalpermonth);

            circular.setMahindravehicalpermonth(mahindravehicalpermonth);
            circular.setNoofmechanics(noofmechanics);
            circular.setBalance_points(point);
            circular.setStatus(status);
            circular.setType(type);

            circular.setParticipant_code(participant_code);
            circular.setParticipant_id_pk(participant_id_pk);
            circular.setForm_fillup_date(form_fillup_date);
            circular.setParticipant_claim_history(participant_claim_history);

            cursor.moveToNext();

        }
        return circular;
    }

    public List<UserModel> getSelectedUserData(String strState, String strDistric) {
        ArrayList<UserModel> news = new ArrayList<UserModel>();

        Cursor cursor = database.rawQuery("select * from "
                + Database.USER_TABLE + " WHERE " + Database.USER_TABLE_PARTICIPANTCODE
                + " = '" + strState + "'" + " and " + Database.USER_TABLE_MOBNO
                + " = '" + strDistric + "'", null);

        while (cursor.moveToNext()) {

            UserModel _news = new UserModel();


            String id = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TRNO));
            String picture = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PICTURE));
            String fname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_FNAME));
            String lname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_LNAME));
            String gender = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_GENDER));
            String nomininame = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOMININAME));
            String nominirely = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOMINIRELATION));
            String mothername = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MOTHERNAME));
            String workshopname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_WORKSHOPNAME));
            String state = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATE));
            String district = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_DISTRICT));
            String pincode = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PINCODE));


            String taluka = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TALUKA));
            String city = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_CITY));
            String shopadd = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SHOPADDR));
            String landmark = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_LANDMARK));
            String mobile_no = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MOBNO));
            String email = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_EMAIL));
            String sector = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SECTOR));
            String specialization = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SPECIALIZATION));

            String dob = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_DOB));
            String regdate = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_REGDATE));
            String toatalsperconpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TOTALSPERCONSPERMONTH));
            String sperpartconformmvehicpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SPERPARTCONFORMMVECPERMONTH));
            String mmgenuspareconpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MMGENUSPARECONPERMONTH));
            String totalvehicalpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TOTALVEHICALPERMONTH));
            String mahindravehicalpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MAHINDRAVEHICALPERMONTH));
            String noofmechanics = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOOFMECHANICS));

            String point = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_POINT));
            String status = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATUS));
            String type = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TYPE));

            String participant_code = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANTCODE));
            String participant_id_pk = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANt_ID_PK));
            String form_fillup_date = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_FORMFILLDATE));
            String participant_claim_history = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANT_CLAIM_HISTORY));


            _news.setId(Integer.parseInt(id));
            _news.setPicture(picture);
            _news.setUserfname(fname);
            _news.setUserlname(lname);
            _news.setGender(gender);
            _news.setNomini(nomininame);
            _news.setNominirely(nominirely);
            _news.setMothername(mothername);
            _news.setWorkshopname(workshopname);

            _news.setState(state);
            _news.setDistrict(district);
            _news.setPincode(pincode);
            _news.setTaluka(taluka);
            _news.setCity(city);
            _news.setShopadd(shopadd);
            _news.setLandmark(landmark);
            _news.setMobile_no(mobile_no);

            _news.setEmail(email);
            _news.setSector(sector);
            _news.setSpecialization(specialization);
            _news.setDob(dob);
            _news.setRegdate(regdate);
            _news.setToatalsperconpermonth(toatalsperconpermonth);
            _news.setSperpartconformmvehicpermonth(sperpartconformmvehicpermonth);
            _news.setMmgenuspareconpermonth(mmgenuspareconpermonth);
            _news.setTotalvehicalpermonth(totalvehicalpermonth);

            _news.setMahindravehicalpermonth(mahindravehicalpermonth);
            _news.setNoofmechanics(noofmechanics);
            _news.setBalance_points(point);
            _news.setStatus(status);
            _news.setType(type);

            _news.setParticipant_code(participant_code);
            _news.setParticipant_id_pk(participant_id_pk);
            _news.setForm_fillup_date(form_fillup_date);
            _news.setParticipant_claim_history(participant_claim_history);

            news.add(_news);

        }
        return news;
    }

    public List<UserModel> getSelectedStateDistrict(String strState, String strDistric) {
        ArrayList<UserModel> news = new ArrayList<UserModel>();

        Cursor cursor = database.rawQuery("select * from "
                + Database.USER_TABLE + " WHERE " + Database.USER_TABLE_STATE
                + " = '" + strState + "'" + " and " + Database.USER_TABLE_DISTRICT
                + " = '" + strDistric + "'", null);

        while (cursor.moveToNext()) {

            UserModel _news = new UserModel();

            String id = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TRNO));
            String picture = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PICTURE));
            String fname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_FNAME));
            String lname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_LNAME));
            String gender = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_GENDER));
            String nomininame = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOMININAME));
            String nominirely = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOMINIRELATION));
            String mothername = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MOTHERNAME));
            String workshopname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_WORKSHOPNAME));
            String state = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATE));
            String district = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_DISTRICT));
            String pincode = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PINCODE));


            String taluka = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TALUKA));
            String city = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_CITY));
            String shopadd = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SHOPADDR));
            String landmark = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_LANDMARK));
            String mobile_no = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MOBNO));
            String email = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_EMAIL));
            String sector = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SECTOR));
            String specialization = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SPECIALIZATION));

            String dob = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_DOB));
            String regdate = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_REGDATE));
            String toatalsperconpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TOTALSPERCONSPERMONTH));
            String sperpartconformmvehicpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SPERPARTCONFORMMVECPERMONTH));
            String mmgenuspareconpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MMGENUSPARECONPERMONTH));
            String totalvehicalpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TOTALVEHICALPERMONTH));
            String mahindravehicalpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MAHINDRAVEHICALPERMONTH));
            String noofmechanics = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOOFMECHANICS));

            String point = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_POINT));
            String status = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATUS));
            String type = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TYPE));

            String participant_code = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANTCODE));
            String participant_id_pk = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANt_ID_PK));
            String form_fillup_date = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_FORMFILLDATE));
            String participant_claim_history = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANT_CLAIM_HISTORY));

            _news.setId(Integer.parseInt(id));
            _news.setPicture(picture);
            _news.setUserfname(fname);
            _news.setUserlname(lname);
            _news.setGender(gender);
            _news.setNomini(nomininame);
            _news.setNominirely(nominirely);
            _news.setMothername(mothername);
            _news.setWorkshopname(workshopname);

            _news.setState(state);
            _news.setDistrict(district);
            _news.setPincode(pincode);
            _news.setTaluka(taluka);
            _news.setCity(city);
            _news.setShopadd(shopadd);
            _news.setLandmark(landmark);
            _news.setMobile_no(mobile_no);

            _news.setEmail(email);
            _news.setSector(sector);
            _news.setSpecialization(specialization);
            _news.setDob(dob);
            _news.setRegdate(regdate);
            _news.setToatalsperconpermonth(toatalsperconpermonth);
            _news.setSperpartconformmvehicpermonth(sperpartconformmvehicpermonth);
            _news.setMmgenuspareconpermonth(mmgenuspareconpermonth);
            _news.setTotalvehicalpermonth(totalvehicalpermonth);

            _news.setMahindravehicalpermonth(mahindravehicalpermonth);
            _news.setNoofmechanics(noofmechanics);
            _news.setBalance_points(point);
            _news.setStatus(status);
            _news.setType(type);

            _news.setParticipant_code(participant_code);
            _news.setParticipant_id_pk(participant_id_pk);
            _news.setForm_fillup_date(form_fillup_date);
            _news.setParticipant_claim_history(participant_claim_history);

            news.add(_news);

        }
        return news;
    }

    public List<UserModel> getSearchData(int strid) {
        ArrayList<UserModel> news = new ArrayList<UserModel>();
        Cursor cursor = database.rawQuery("select * from "
                + Database.USER_TABLE + " WHERE " + Database.USER_TABLE_TRNO
                + " = " + strid, null);

        while (cursor.moveToNext()) {

            UserModel _news = new UserModel();


            String id = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TRNO));
            String picture = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PICTURE));
            String fname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_FNAME));
            String lname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_LNAME));
            String gender = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_GENDER));
            String nomininame = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOMININAME));
            String nominirely = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOMINIRELATION));
            String mothername = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MOTHERNAME));
            String workshopname = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_WORKSHOPNAME));
            String state = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATE));
            String district = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_DISTRICT));
            String pincode = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PINCODE));


            String taluka = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TALUKA));
            String city = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_CITY));
            String shopadd = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SHOPADDR));
            String landmark = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_LANDMARK));
            String mobile_no = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MOBNO));
            String email = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_EMAIL));
            String sector = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SECTOR));
            String specialization = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SPECIALIZATION));

            String dob = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_DOB));
            String regdate = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_REGDATE));
            String toatalsperconpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TOTALSPERCONSPERMONTH));
            String sperpartconformmvehicpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_SPERPARTCONFORMMVECPERMONTH));
            String mmgenuspareconpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MMGENUSPARECONPERMONTH));
            String totalvehicalpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TOTALVEHICALPERMONTH));
            String mahindravehicalpermonth = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_MAHINDRAVEHICALPERMONTH));
            String noofmechanics = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_NOOFMECHANICS));

            String point = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_POINT));
            String status = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_STATUS));
            String type = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_TYPE));

            String participant_code = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANTCODE));
            String participant_id_pk = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANt_ID_PK));
            String form_fillup_date = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_FORMFILLDATE));
            String participant_claim_history = cursor.getString(cursor
                    .getColumnIndex(Database.USER_TABLE_PARTICIPANT_CLAIM_HISTORY));

            _news.setId(Integer.parseInt(id));
            _news.setPicture(picture);
            _news.setUserfname(fname);
            _news.setUserlname(lname);
            _news.setGender(gender);
            _news.setNomini(nomininame);
            _news.setNominirely(nominirely);
            _news.setMothername(mothername);
            _news.setWorkshopname(workshopname);

            _news.setState(state);
            _news.setDistrict(district);
            _news.setPincode(pincode);
            _news.setTaluka(taluka);
            _news.setCity(city);
            _news.setShopadd(shopadd);
            _news.setLandmark(landmark);
            _news.setMobile_no(mobile_no);

            _news.setEmail(email);
            _news.setSector(sector);
            _news.setSpecialization(specialization);
            _news.setDob(dob);
            _news.setRegdate(regdate);
            _news.setToatalsperconpermonth(toatalsperconpermonth);
            _news.setSperpartconformmvehicpermonth(sperpartconformmvehicpermonth);
            _news.setMmgenuspareconpermonth(mmgenuspareconpermonth);
            _news.setTotalvehicalpermonth(totalvehicalpermonth);

            _news.setMahindravehicalpermonth(mahindravehicalpermonth);
            _news.setNoofmechanics(noofmechanics);
            _news.setBalance_points(point);
            _news.setStatus(status);
            _news.setType(type);

            _news.setParticipant_code(participant_code);
            _news.setParticipant_id_pk(participant_id_pk);
            _news.setForm_fillup_date(form_fillup_date);
            _news.setParticipant_claim_history(participant_claim_history);

            news.add(_news);

        }
        return news;
    }

    public String getParticipantId(int strid) {
        ArrayList<UserModel> news = new ArrayList<UserModel>();
        Cursor cursor = database.rawQuery("select " + Database.USER_TABLE_PARTICIPANTCODE + " from "
                + Database.USER_TABLE + " WHERE " + Database.USER_TABLE_TRNO
                + " = " + strid, null);

        cursor.moveToFirst();
        String participant_code = cursor.getString(cursor.getColumnIndex(Database.USER_TABLE_PARTICIPANTCODE));
        cursor.close();
        return participant_code;
    }


    // Method to delete the News Table
    public void deleteUserTable() {
        database.delete(Database.USER_TABLE, null, null);
    }

    // Method which returns true if the table is empty
    public boolean isHomeworkTableEmpty() {
        boolean bIsEmpty = false;

        Cursor cursor = database.query(Database.USER_TABLE, allColumns, null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            bIsEmpty = false;
        } else {
            bIsEmpty = true;
        }
        return bIsEmpty;
    }

    // Method to insert the news date and news Description into the news table.
    public void insertIntoClaimHistry(JSONObject news, String strclamepartid) throws JSONException {
        ClaimHistoryModel savedNews = null;

        Log.e("insert :", "insert news : " + news.toString());
        Log.e("date month :", "" + news.get("month").toString());

        ContentValues values = new ContentValues();
        Log.e("insert :", "insert login_id_pk : " + savedNews);

        /*values.put(Database.PARTICIPANT_CLAIM_HISTRY_ID_COL, strclamepartid);
        values.put(Database.PARTICIPANT_CLAIM_HISTRY_UID_COL, news.getString("uid"));
        values.put(Database.PARTICIPANT_CLAIM_HISTRY_VALIDATION_STATUS_COL, news.getString("validation_status"));
        values.put(Database.PARTICIPANT_CLAIM_HISTRY_POINT_COL, news.getString("point"));
        values.put(Database.PARTICIPANT_CLAIM_HISTRY_PARTNO_COL, news.getString("part_no"));
        values.put(Database.PARTICIPANT_CLAIM_HISTRY_RECORD_DATE_COL, news.getString("record_date"));
        values.put(Database.PARTICIPANT_CLAIM_HISTRY_UID_STATUS_COL, news.getString("uid_status"));*/

        values.put(Database.PARTICIPANT_CLAIM_HISTRY_ID_COL, strclamepartid);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String formated_mnth = "";
        String stringMonth = "";
        try {
            Date date = sdf.parse(news.getString("month"));
            //   Date date = sdf.parse("2016-07-01");
            Log.e("date : ", "" + date.toString());
            formated_mnth = new SimpleDateFormat("MMM").format(date);
            stringMonth = (String) android.text.format.DateFormat.format("MMM - yyyy", date);

            // Log.e("","");
            //  Log.v("test_date : ","test"+test_date);


            // Log.e("format","mnth : "+formated_mnth);
            Log.e("format", "mnth : " + stringMonth);
            values.put(Database.PARTICIPANT_CLAIM_HISTRY_UID_COL, stringMonth);
            //int month1 =  Integer.parseInt(formated);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (news.getString("type").equals("base")) {
            Log.e("type", "base : ");
            values.put(Database.PARTICIPANT_CLAIM_HISTRY_UID_STATUS_COL, news.getString("total_points"));
        } else if (news.getString("type").equals("bonus")) {
            Log.e("type", "bonus : ");
            values.put(Database.PARTICIPANT_CLAIM_HISTRY_POINT_COL, news.getString("total_points"));
        }
        values.put(Database.PARTICIPANT_CLAIM_HISTRY_VALIDATION_STATUS_COL, "");
        values.put(Database.PARTICIPANT_CLAIM_HISTRY_PARTNO_COL, "");
        values.put(Database.PARTICIPANT_CLAIM_HISTRY_RECORD_DATE_COL, "");
        try {
            // Log.e("format","mnth : "+formated_mnth);
            Log.e("format", "mnth : " + stringMonth);
            String WHERE = Database.PARTICIPANT_CLAIM_HISTRY_ID_COL + "=" + strclamepartid + " and " + Database.PARTICIPANT_CLAIM_HISTRY_UID_COL + "=" + stringMonth;
            //  String WHERE =Database.PARTICIPANT_CLAIM_HISTRY_UID_COL+"="+stringMonvaluesth;
            Log.e("WHERE", "WHERE : " + WHERE);
            // Cursor cursor = database.rawQuery("select * from "+Database.PARTICIPANT_CLAIM_HISTRY_TABLE+" "+WHERE, null);

            Cursor cursor = database.query(Database.PARTICIPANT_CLAIM_HISTRY_TABLE, null, Database.PARTICIPANT_CLAIM_HISTRY_ID_COL + "=?" + " and " + Database.PARTICIPANT_CLAIM_HISTRY_UID_COL + "=?", new String[]{strclamepartid, stringMonth}, null, null, null);
            //   Cursor cursor = database.query(Database.PARTICIPANT_CLAIM_HISTRY_TABLE, null,  Database.PARTICIPANT_CLAIM_HISTRY_UID_COL+"=?", new String[] { /*strclamepartid,*/ stringMonth }, null, null, null);
            Log.e("cursor", "count: " + cursor.getCount());
            if (cursor.getCount() > 0) {

                int res = database.update(Database.PARTICIPANT_CLAIM_HISTRY_TABLE, values, Database.PARTICIPANT_CLAIM_HISTRY_ID_COL + " = ?" + "  and " + Database.PARTICIPANT_CLAIM_HISTRY_UID_COL + "=?", new String[]{strclamepartid, stringMonth});

                Log.e("update", "query: " + res);
            } else {
                Long res = database.insert(Database.PARTICIPANT_CLAIM_HISTRY_TABLE, null, values);
                Log.e("insert", "query: " + res);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("inserted dta ", "" + getClaimHistry(strclamepartid).toString());
    }

    private static String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMMM").format(cal.getTime());
        return monthName;
    }

    public String getTotalPoints(int strid) {
        String total_point = null;
        Cursor cursor = database.rawQuery("select sum(" + Database.PARTICIPANT_CLAIM_HISTRY_POINT_COL + ") from "
                + Database.PARTICIPANT_CLAIM_HISTRY_TABLE + " WHERE " + Database.PARTICIPANT_CLAIM_HISTRY_ID_COL
                + " = " + strid, null);
        cursor.moveToFirst();

        total_point = cursor.getString(0);
        cursor.close();
        return total_point;
    }

    public List<ClaimHistoryModel> getClaimHistry(String strid) {
        ArrayList<ClaimHistoryModel> news = new ArrayList<ClaimHistoryModel>();
       /* Cursor cursor = database.rawQuery("select * from "
                + Database.PARTICIPANT_CLAIM_HISTRY_TABLE + " WHERE " + Database.PARTICIPANT_CLAIM_HISTRY_ID_COL
                + " = " + strid + " order by " + Database.PARTICIPANT_CLAIM_HISTRY_RECORD_DATE_COL + " desc", null);*/

        /*Cursor cursor = database.rawQuery("select * from "
                + Database.PARTICIPANT_CLAIM_HISTRY_TABLE + " WHERE " + Database.PARTICIPANT_CLAIM_HISTRY_ID_COL
                + " = " + strid, null);*/
        Cursor cursor = database.query(Database.PARTICIPANT_CLAIM_HISTRY_TABLE, null, Database.PARTICIPANT_CLAIM_HISTRY_ID_COL + "=?", new String[]{String.valueOf(strid)}, null, null, null);
        while (cursor.moveToNext()) {
            Log.e("found", "record");
            ClaimHistoryModel _news = new ClaimHistoryModel();

            String id = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_ID_COL));
            String uid = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_UID_COL));
            String status = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_VALIDATION_STATUS_COL));
            String point = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_POINT_COL));
            String partno = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_PARTNO_COL));
            String recorddate = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_RECORD_DATE_COL));
            String uidstatus = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_UID_STATUS_COL));


            _news.setClaim_participant_id(Integer.parseInt(id));
            _news.setClaim_uid(uid);
            _news.setClaim_validation_status(status);
            _news.setClaim_point(point);
            _news.setClaim_part_no(partno);
            _news.setClaim_record_date(recorddate);
            _news.setClaim_uid_status(uidstatus);


            news.add(_news);

        }
        Log.e("database", "list size: " + news.size());
        return news;
    }

    public List<ClaimHistoryModel> getSearchClaimHistory(int strid, String searchChar) {
        ArrayList<ClaimHistoryModel> news = new ArrayList<ClaimHistoryModel>();
        Cursor cursor = database.rawQuery("select * from "
                + Database.PARTICIPANT_CLAIM_HISTRY_TABLE + " WHERE " + Database.PARTICIPANT_CLAIM_HISTRY_ID_COL
                + " = " + strid + " and " + Database.PARTICIPANT_CLAIM_HISTRY_UID_COL + " like '%" + searchChar + "%'", null);

        while (cursor.moveToNext()) {

            ClaimHistoryModel _news = new ClaimHistoryModel();

            String id = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_ID_COL));
            String uid = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_UID_COL));
            String status = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_VALIDATION_STATUS_COL));
            String point = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_POINT_COL));
            String partno = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_PARTNO_COL));
            String recorddate = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_RECORD_DATE_COL));
            String uidstatus = cursor.getString(cursor
                    .getColumnIndex(Database.PARTICIPANT_CLAIM_HISTRY_UID_STATUS_COL));

            _news.setClaim_participant_id(Integer.parseInt(id));
            _news.setClaim_uid(uid);
            _news.setClaim_validation_status(status);
            _news.setClaim_point(point);
            _news.setClaim_part_no(partno);
            _news.setClaim_record_date(recorddate);
            _news.setClaim_uid_status(uidstatus);

            news.add(_news);
        }
        return news;
    }

    public void deleteFromClaimHistory(String trno) {

        Cursor c = database.query(Database.PARTICIPANT_CLAIM_HISTRY_TABLE, null, Database.PARTICIPANT_CLAIM_HISTRY_ID_COL + " = " + trno, null, null,
                null, null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            database.execSQL("delete from " + Database.PARTICIPANT_CLAIM_HISTRY_TABLE + " where "
                    + Database.PARTICIPANT_CLAIM_HISTRY_ID_COL + " = " + trno);
        }

        // Log.e("Table creation", "Table created successfully");
    }

    public void deleteClaimHistory() {

        database.execSQL("delete from " + Database.PARTICIPANT_CLAIM_HISTRY_TABLE);
        //}

        // Log.e("Table creation", "Table created successfully");
    }

    public void updatePublishStattus(int trno) {
        BarcodeHistoryModel savedNews = getBarcodeByID(String.valueOf(trno));
        if (savedNews == null) {

        } else {
            ContentValues values = new ContentValues();

            values.put(Database.HISTRY_PUBLISH_TYPE_COL, "publish");

            int res = database.update(Database.HISTRY_TABLE, values, Database.ID_COL + " = ?",
                    new String[]{String.valueOf(trno)});
            System.out.println(res + " ");
        }
    }

    public BarcodeHistoryModel getBarcodeByID(String _id) {
        BarcodeHistoryModel circular = null;
        Cursor cursor = database.rawQuery("select * from "
                + Database.HISTRY_TABLE + " WHERE " + Database.ID_COL
                + " = " + _id, null);


        if (cursor.moveToFirst()) {


            circular = new BarcodeHistoryModel();


            String id = cursor.getString(cursor
                    .getColumnIndex(Database.ID_COL));
            String text = cursor.getString(cursor
                    .getColumnIndex(Database.TEXT_COL));
            String format = cursor.getString(cursor
                    .getColumnIndex(Database.FORMAT_COL));
            String display = cursor.getString(cursor
                    .getColumnIndex(Database.DISPLAY_COL));
            String timestamp = cursor.getString(cursor
                    .getColumnIndex(Database.TIMESTAMP_COL));
            String details = cursor.getString(cursor
                    .getColumnIndex(Database.DETAILS_COL));
            String histry_participant_id = cursor.getString(cursor
                    .getColumnIndex(Database.HISTRY_PARTICIPANT_ID_COL));
            String barcode_publish_type = cursor.getString(cursor
                    .getColumnIndex(Database.HISTRY_PUBLISH_TYPE_COL));


            circular.setId(Integer.parseInt(id));
            circular.setText(text);
            circular.setFormat(format);
            circular.setDisplay(display);
            circular.setTimestamp(timestamp);
            circular.setDetails(details);
            circular.setHistryPartcipanID(histry_participant_id);
            circular.setHistryPublishType(barcode_publish_type);

            cursor.moveToNext();

        }
        return circular;
    }

    public Category getCategoryByID(String _id) {
        Category product = null;
        Cursor cursor = database.rawQuery("select * from "
                + Database.CATEGORY_TABLE + " WHERE " + Database.CATEGORY_TABLE_TRNO
                + " = " + _id, null);
        if (cursor.moveToFirst()) {
            product = new Category();
            int id = cursor.getInt(cursor
                    .getColumnIndex(Database.PRODUCT_TABLE_TRNO));

            product.setCategory_id_pk(id);

            cursor.moveToNext();
        }
        return product;
    }

    public ContentValues createContentValueCategory(ContentValues values, JSONObject category) {
        try {
            values.put(Database.CATEGORY_TABLE_NAME, category.getString("category_name"));
            values.put(Database.CATEGORY_TABLE_SORT_ORDER, category.getString("category_sort_order"));
            values.put(Database.CATEGORY_TABLE_ACTIVE, category.getString("category_active"));
            values.put(Database.CATEGORY_TABLE_RECORD_DATE, category.getString("category_record_date"));
            values.put(Database.CATEGORY_TABLE_LAST_UPDATED, category.getString("category_last_updated"));
        } catch (Exception e) {

        }
        return values;
    }

    // Method to insert the category details into the category table.
    public void insertIntoCategory(JSONObject category) throws JSONException {
        Category savedCategory = getCategoryByID(String.valueOf(category.getString("category_id_pk")));

        Log.e("insert :", "insert category ");
        if (savedCategory == null) {


            ContentValues values = new ContentValues();
            Log.e("insert :", "insert login_id_pk : " + savedCategory);

            values.put(Database.CATEGORY_TABLE_TRNO, category.getString("category_id_pk"));
            values = createContentValueCategory(values, category);

            Long res = database.insert(Database.CATEGORY_TABLE, null, values);
            System.out.println(res + " ");
        } else {
            ContentValues values = new ContentValues();
            Log.e("insert :", "insert login_id_pk : " + savedCategory);
            values = createContentValueCategory(values, category);

            int res = database.update(Database.CATEGORY_TABLE, values, Database.CATEGORY_TABLE_TRNO + " = ?",
                    new String[]{String.valueOf(category.getString("category_id_pk"))});
        }

    }

    public ArrayList<String> getCategoryName() {
        ArrayList<String> cnames = new ArrayList<String>();
        Cursor cursor = database.rawQuery("select " + Database.CATEGORY_TABLE_TRNO + "," + Database.CATEGORY_TABLE_NAME + " from "
                + Database.CATEGORY_TABLE, null);
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                cnames.add(cursor.getString(cursor.getColumnIndex(Database.CATEGORY_TABLE_NAME)));
                Log.e("", "Category name:" + cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cnames.add(0, "All Categories");

        cursor.close();
        return cnames;
    }

    public String getCategoryIdByName(String cat_name) {
        String id = "";
        Cursor cursor = database.rawQuery("select " + Database.CATEGORY_TABLE_TRNO + " from "
                + Database.CATEGORY_TABLE + " where " + Database.CATEGORY_TABLE_NAME + "='" + cat_name + "'", null);
        cursor.moveToFirst();

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getString(cursor.getColumnIndex(Database.CATEGORY_TABLE_TRNO));
                // Log.e("","Category name:"+cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return id;
    }


    public Product getProductByID(String _id) {
        Product product = null;
        Cursor cursor = database.rawQuery("select * from "
                + Database.PRODUCT_TABLE + " WHERE " + Database.PRODUCT_TABLE_PRODUCT_ID
                + " = " + _id, null);
        if (cursor.moveToFirst()) {


            product = new Product();
            int id = cursor.getInt(cursor
                    .getColumnIndex(Database.PRODUCT_TABLE_TRNO));

            product.setTrno(id);

            cursor.moveToNext();

        }
        return product;
    }

    // Method to insert the product details into the product table.
    public void insertIntoProduct(JSONObject product) throws JSONException {
        Product savedProduct = getProductByID(product.getString("product_id_pk"));
        // Product savedProduct = null;
        if (savedProduct == null) {
            Log.e("insert :", "insert product ");

            ContentValues values = new ContentValues();
            Log.e("insert :", "insert login_id_pk : " + savedProduct);

            values = createContentValueProduct(values, product);


            Long res = database.insert(Database.PRODUCT_TABLE, null, values);
        } else {
            ContentValues values = new ContentValues();
            Log.e("insert :", "insert login_id_pk : " + savedProduct);
            values = createContentValueProduct(values, product);
            int res = database.update(Database.PRODUCT_TABLE, values, Database.PRODUCT_TABLE_TRNO + " = ?",
                    new String[]{String.valueOf(savedProduct.getTrno())});
        }
    }

    public ContentValues createContentValueProduct(ContentValues values, JSONObject product) {
        try {
            values.put(Database.PRODUCT_TABLE_PRODUCT_ID, product.getString("product_id_pk"));
            values.put(Database.PRODUCT_TABLE_NAME, product.getString("name"));
            values.put(Database.PRODUCT_TABLE_PRODUCT_CODE, product.getString("product_code"));
            values.put(Database.PRODUCT_TABLE_SMALL_DESC, product.getString("small_description"));
            values.put(Database.PRODUCT_TABLE_SMALL_IMAGE_LINK, product.getString("small_image_link"));
            values.put(Database.PRODUCT_TABLE_SORT_ORDER, product.getString("sort_order"));
            values.put(Database.PRODUCT_TABLE_POINTS, product.getString("points"));
            values.put(Database.PRODUCT_TABLE_PRODUCTS_VISIBLE, product.getString("product_visible"));
            values.put(Database.PRODUCT_TABLE_ACTIVE_STATUS, product.getString("active_status"));
            values.put(Database.PRODUCT_TABLE_PRODUCT_TYPE, product.getString("product_type"));
            values.put(Database.PRODUCT_TABLE_RECORD_DATE, product.getString("record_date"));
            values.put(Database.PRODUCT_TABLE_UPDATE_DATE, product.getString("update_date"));

            String cat_id = "," + product.getString("category_id") + ",";
            values.put(Database.PRODUCT_TABLE_CATEGORY_ID, "" + cat_id);
        } catch (Exception e) {
        }
        return values;
    }

    public ArrayList<Product> getProductDetails(String orderby, String category, String search) {
        ArrayList<Product> prods = new ArrayList<Product>();

        String condition_sel = "";


        if (category.equals("All Categories")) {

        } else {
            String cat_id = getCategoryIdByName(category);
            condition_sel = condition_sel + "and  " + Database.PRODUCT_TABLE_CATEGORY_ID + " like '%," + cat_id + ",%'";
        }

        if (search.equals("")) {

        } else {
            condition_sel = condition_sel + " and " + Database.PRODUCT_TABLE_NAME + " like '%" + search + "%'";
        }

        Cursor cursor;

        if (TextUtils.isEmpty(orderby)) {
            cursor = database.rawQuery("select * from "
                    + Database.PRODUCT_TABLE + " where 1 " + condition_sel, null);
        } else {
            cursor = database.rawQuery("select * from "
                    + Database.PRODUCT_TABLE + " where 1 " + condition_sel + " order by " + Database.PRODUCT_TABLE_POINTS + " " + orderby, null);
        }


        Log.e("query 0", "Query : " + "select * from "
                + Database.PRODUCT_TABLE + " where 1 " + condition_sel + " order by " + Database.PRODUCT_TABLE_POINTS + " " + orderby);

        if (cursor.moveToFirst()) {
            do {
                Product p = new Product();

                p.setTrno(cursor.getInt(cursor.getColumnIndex((Database.PRODUCT_TABLE_TRNO))));
                p.setProduct_id_pk(cursor.getInt(cursor.getColumnIndex((Database.PRODUCT_TABLE_PRODUCT_ID))));
                p.setName(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_NAME))));
                p.setProduct_code(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_PRODUCT_CODE))));
                p.setSmall_description(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_SMALL_DESC))));
                p.setSmall_image_link(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_SMALL_IMAGE_LINK))));
                p.setSort_order(cursor.getInt(cursor.getColumnIndex((Database.PRODUCT_TABLE_SORT_ORDER))));
                p.setPoints(cursor.getInt(cursor.getColumnIndex((Database.PRODUCT_TABLE_POINTS))));
                p.setProduct_visible(cursor.getInt(cursor.getColumnIndex((Database.PRODUCT_TABLE_PRODUCTS_VISIBLE))));
                p.setActive_status(cursor.getInt(cursor.getColumnIndex((Database.PRODUCT_TABLE_ACTIVE_STATUS))));
                p.setProduct_type(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_PRODUCT_TYPE))));
                p.setRecord_date(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_RECORD_DATE))));
                p.setUpdate_date(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_UPDATE_DATE))));
                p.setCategory_id(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_CATEGORY_ID))));
                prods.add(p);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return prods;
    }

    public ArrayList<Product> getAllProductDetails() {
        ArrayList<Product> prods = new ArrayList<Product>();


        Cursor cursor;


        cursor = database.rawQuery("select * from "
                + Database.PRODUCT_TABLE, null);


        if (cursor.moveToFirst()) {
            do {
                Product p = new Product();

                p.setTrno(cursor.getInt(cursor.getColumnIndex((Database.PRODUCT_TABLE_TRNO))));
                p.setProduct_id_pk(cursor.getInt(cursor.getColumnIndex((Database.PRODUCT_TABLE_PRODUCT_ID))));
                p.setName(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_NAME))));
                p.setProduct_code(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_PRODUCT_CODE))));
                p.setSmall_description(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_SMALL_DESC))));
                p.setSmall_image_link(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_SMALL_IMAGE_LINK))));
                p.setSort_order(cursor.getInt(cursor.getColumnIndex((Database.PRODUCT_TABLE_SORT_ORDER))));
                p.setPoints(cursor.getInt(cursor.getColumnIndex((Database.PRODUCT_TABLE_POINTS))));
                p.setProduct_visible(cursor.getInt(cursor.getColumnIndex((Database.PRODUCT_TABLE_PRODUCTS_VISIBLE))));
                p.setActive_status(cursor.getInt(cursor.getColumnIndex((Database.PRODUCT_TABLE_ACTIVE_STATUS))));
                p.setProduct_type(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_PRODUCT_TYPE))));
                p.setRecord_date(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_RECORD_DATE))));
                p.setUpdate_date(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_UPDATE_DATE))));
                p.setCategory_id(cursor.getString(cursor.getColumnIndex((Database.PRODUCT_TABLE_CATEGORY_ID))));
                prods.add(p);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return prods;
    }

    public String getSelectedProductDetails(String code) {
        /*Cursor cursor = database.rawQuery("select "+Database.PRODUCT_TABLE_PRODUCT_DETAILS+" from "
                + Database.PRODUCT_TABLE+" where "+Database.PRODUCT_TABLE_PRODUCT_CODE+" = "+code, null);*/

        Cursor cursor = database.rawQuery("select " + Database.PRODUCT_TABLE_PRODUCT_DETAILS + " from "
                + Database.PRODUCT_TABLE + " where " + Database.PRODUCT_TABLE_PRODUCT_CODE + " = '" + code + "'", null);
        Log.e("", "Query : " + "select " + Database.PRODUCT_TABLE_PRODUCT_DETAILS + " from "
                + Database.PRODUCT_TABLE + " where " + Database.PRODUCT_TABLE_PRODUCT_CODE + " = '" + code + "'");

        String prod_details = "";

        Log.e("", "Cursor is:" + cursor.toString());

        if (cursor.moveToFirst()) {

            prod_details = cursor.getString((cursor.getColumnIndex(Database.PRODUCT_TABLE_PRODUCT_DETAILS)));
        }

        cursor.close();
        return prod_details;
    }

    public String getBonus_details(String code) {
        /*Cursor cursor = database.rawQuery("select "+Database.PRODUCT_TABLE_PRODUCT_DETAILS+" from "
                + Database.PRODUCT_TABLE+" where "+Database.PRODUCT_TABLE_PRODUCT_CODE+" = "+code, null);*/

        Cursor cursor = database.rawQuery("select " + Database.PRODUCT_TABLE_BONUS_DETAILS + " from "
                + Database.PRODUCT_TABLE + " where " + Database.PRODUCT_TABLE_PRODUCT_CODE + " = '" + code + "'", null);
        Log.e("", "Query : " + "select " + Database.PRODUCT_TABLE_BONUS_DETAILS + " from "
                + Database.PRODUCT_TABLE + " where " + Database.PRODUCT_TABLE_PRODUCT_CODE + " = '" + code + "'");

        String prod_details = "";

        Log.e("", "Cursor is:" + cursor.toString());

        if (cursor.moveToFirst()) {

            prod_details = cursor.getString((cursor.getColumnIndex(Database.PRODUCT_TABLE_BONUS_DETAILS)));
        }

        cursor.close();
        return prod_details;
    }

    public int updateProduct(String product_code, String product_details, String bonus_details) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.PRODUCT_TABLE_PRODUCT_DETAILS, product_details);
        contentValues.put(Database.PRODUCT_TABLE_BONUS_DETAILS, bonus_details);

        int res = database.update(Database.PRODUCT_TABLE, contentValues, Database.PRODUCT_TABLE_PRODUCT_CODE + " = ? ",
                new String[]{product_code});

        Log.e("", "Update product code:-----------------------------------------------" + product_code);
        Log.e("", "Update product product details:-----------------------------------------------" + product_details);
        Log.e("", "Update product bonus details:-----------------------------------------------" + res);
        Log.e("", "Update product result:-----------------------------------------------" + res);
        return res;
    }

    //---------------------------- Notification---------------------------------------------------

    // Method to insert the notifications into the notification table.
    public void insertIntoNotification(String type, String userId, String title, String desc, String img, String channelType) {

        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
        Log.e("insert :", "insert notifications : " + date);

        ContentValues values = new ContentValues();

        values.put(Database.NOTIFICATION_TABLE_USER_ID, userId);
        values.put(Database.NOTIFICATION_TABLE_TYPE, type);
        values.put(Database.NOTIFICATION_TABLE_TITLE, title);
        values.put(Database.NOTIFICATION_TABLE_DESC, desc);
        values.put(Database.NOTIFICATION_TABLE_IMG, img);
        values.put(Database.NOTIFICATION_TABLE_CHANNEL_TYPE, channelType);
        values.put(Database.NOTIFICATION_TABLE_DATE, date);
        values.put(Database.NOTIFICATION_TABLE_STATUS, "0");
        Long res = database.insert(Database.NOTIFICATION_TABLE, null, values);

        Log.e("", "insert notification result:" + res);
    }

    public List<NewNotification> getAllNotifications() {
        List<NewNotification> notificationArr = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from "
                + Database.NOTIFICATION_TABLE, null);
        while (cursor.moveToNext()) {

            NewNotification n = new NewNotification();

            n.setTrno(cursor.getInt(cursor
                    .getColumnIndex(Database.NOTIFICATION_TABLE_TRNO)));
            n.setTitle(cursor.getString(cursor
                    .getColumnIndex(Database.NOTIFICATION_TABLE_TITLE)));
            n.setDescription(cursor.getString(cursor
                    .getColumnIndex(Database.NOTIFICATION_TABLE_DESC)));
            n.setImage(cursor.getString(cursor
                    .getColumnIndex(Database.NOTIFICATION_TABLE_IMG)));
            n.setType(cursor.getString(cursor
                    .getColumnIndex(Database.NOTIFICATION_TABLE_TYPE)));
            n.setChannel_type(cursor.getString(cursor
                    .getColumnIndex(Database.NOTIFICATION_TABLE_CHANNEL_TYPE)));
            n.setMdate(cursor.getString(cursor
                    .getColumnIndex(Database.NOTIFICATION_TABLE_DATE)));
            n.setStatus(cursor.getString(cursor
                    .getColumnIndex(Database.NOTIFICATION_TABLE_STATUS)));

            notificationArr.add(n);
        }
        return notificationArr;
    }

    public ArrayList<String> getNotificationTitle() {
        ArrayList<String> notificationArr = new ArrayList<String>();
        Cursor cursor = database.rawQuery("select * from "
                + Database.NOTIFICATION_TABLE, null);
        while (cursor.moveToNext()) {

            Notification n = new Notification();

            String data = cursor.getString(cursor
                    .getColumnIndex(Database.NOTIFICATION_TABLE_TITLE));

            JSONObject jobj = null;
            try {
                jobj = new JSONObject(data);

                String title = jobj.getString("title");
                notificationArr.add(title);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return notificationArr;
    }


    public ArrayList<Notification> getNotification(String userId) {
        ArrayList<Notification> notification = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from "
                + Database.NOTIFICATION_TABLE + " WHERE " + Database.NOTIFICATION_TABLE_USER_ID + " = '" + userId + "' order by " + Database.NOTIFICATION_TABLE_TRNO + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                String data = cursor.getString(cursor
                        .getColumnIndex(Database.NOTIFICATION_TABLE_TITLE));

                String date = cursor.getString(cursor
                        .getColumnIndex(Database.NOTIFICATION_TABLE_DATE));
                String status = cursor.getString(cursor
                        .getColumnIndex(Database.NOTIFICATION_TABLE_STATUS));

                Log.e("", "Daa : " + data);
                Log.e("", "Daa : " + date);
                Log.e("", "Daa : " + status);
                JSONObject jobj = null;
                JSONObject json_data = null;
                try {
                    jobj = new JSONObject(data);
                    json_data = jobj.getJSONObject("data");
                    ;

                    // String title = job_data.getString("title");
                    String title = json_data.getString("title");
                    String body = json_data.getString("body");

                    String img_url = "";
                    if (json_data.has("img_url"))
                        img_url = json_data.getString("img_url");
                    else
                        img_url = "";

                    int trno = cursor.getInt(cursor
                            .getColumnIndex(Database.NOTIFICATION_TABLE_TRNO));
                    Notification n = new Notification(title, body, img_url, date, trno, status);

                    notification.add(n);
                    Log.d("Daa_n", "  " + n);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Daa", "Exception: " + Log.getStackTraceString(e));
                }

            } while (cursor.moveToNext());
        }
        Log.d("Daa_notification", " " + notification);
        return notification;

    }

    public boolean isUnReadNotifications(String userId) {
        Cursor cursor = database.rawQuery("select * from "
                + Database.NOTIFICATION_TABLE + " WHERE " + Database.NOTIFICATION_TABLE_USER_ID + " = '" + userId + "' AND " + Database.NOTIFICATION_TABLE_STATUS + "='0' order by " + Database.NOTIFICATION_TABLE_TRNO + " DESC", null);

        return cursor.getCount() > 0;
    }

    public ArrayList<NewNotification> getNewNotificationByUserId(String userId) {
        ArrayList<NewNotification> notificationList = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from "
                + Database.NOTIFICATION_TABLE + " WHERE " + Database.NOTIFICATION_TABLE_USER_ID + " = '" + userId + "' order by " + Database.NOTIFICATION_TABLE_TRNO + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                NewNotification n = new NewNotification();
                n.setTrno(cursor.getInt(cursor
                        .getColumnIndex(Database.NOTIFICATION_TABLE_TRNO)));
                n.setTitle(cursor.getString(cursor
                        .getColumnIndex(Database.NOTIFICATION_TABLE_TITLE)));
                n.setDescription(cursor.getString(cursor
                        .getColumnIndex(Database.NOTIFICATION_TABLE_DESC)));
                n.setType(cursor.getString(cursor
                        .getColumnIndex(Database.NOTIFICATION_TABLE_TYPE)));
                n.setImage(cursor.getString(cursor
                        .getColumnIndex(Database.NOTIFICATION_TABLE_IMG)));
                n.setChannel_type(cursor.getString(cursor
                        .getColumnIndex(Database.NOTIFICATION_TABLE_CHANNEL_TYPE)));
                n.setMdate(cursor.getString(cursor
                        .getColumnIndex(Database.NOTIFICATION_TABLE_DATE)));
                n.setStatus(cursor.getString(cursor
                        .getColumnIndex(Database.NOTIFICATION_TABLE_STATUS)));

                notificationList.add(n);


            } while (cursor.moveToNext());
        }
        Log.d("Daa_notification", " " + notificationList);
        return notificationList;

    }


    public int updateNotification(String trno) {
        ArrayList<Notification> notification = new ArrayList<>();
       /* Cursor cursor = database.rawQuery("update "+ Database.NOTIFICATION_TABLE+" set "+Database.NOTIFICATION_TABLE_STATUS+
                "= '1' WHERE "+Database.NOTIFICATION_TABLE_TRNO+" = '"+trno+"'" , null);
*/
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.NOTIFICATION_TABLE_STATUS, "1");


        int res = database.update(Database.NOTIFICATION_TABLE, contentValues, Database.NOTIFICATION_TABLE_USER_ID + " = ? ",
                new String[]{trno});

        Log.e("", "Update notification trno:-----------------------------------------------" + trno);

        return res;
    }

    public void insertClaimHistory(JSONObject news, String strClaimHistry, String strid) throws JSONException {
        UserModel savedNews = null;

        Log.e("insert :", "insert news : " + news);

        savedNews = getHomeworkByID(String.valueOf(strid));

        if (savedNews == null) {
            ContentValues values = new ContentValues();
            Log.e("insert :", "insert login_id_pk : " + savedNews);


            values.put(Database.USER_TABLE_TRNO, strid);
            Log.e("Login Id :", "Login Id In Datasource : " + strid);

            values.put(Database.USER_TABLE_PARTICIPANT_CLAIM_HISTORY, strClaimHistry);

            Long res = database.insert(Database.USER_TABLE, null, values);
            System.out.println(res + " ");
        } else {
            Log.e("insert :", "update login_id_pk : " + savedNews);

            ContentValues values = new ContentValues();
            values.put(Database.USER_TABLE_TRNO, strid);
            Log.e("Login Id :", "Login Id In Datasource : " + strid);

            values.put(Database.USER_TABLE_PARTICIPANT_CLAIM_HISTORY, strClaimHistry);

            int res = database.update(Database.USER_TABLE, values, Database.USER_TABLE_TRNO + " = ?",
                    new String[]{strid});
            System.out.println(res + " ");
        }
    }

    public int getStatusCount(String mobno) {
        int count = 0;
/*
        Cursor cursor = database.rawQuery("select count (*) from "+ Database.NOTIFICATION_TABLE+" where "+Database.NOTIFICATION_TABLE_STATUS+
                "= 0", null);
        count=cursor.getCount();
*/
        final String SQL_STATEMENT = "SELECT COUNT(*) FROM " + Database.NOTIFICATION_TABLE + " WHERE " + Database.NOTIFICATION_TABLE_USER_ID + " = '" + mobno + "'";
        // final String SQL_STATEMENT = "SELECT COUNT(*) FROM "+ Database.NOTIFICATION_TABLE+" WHERE "+Database.NOTIFICATION_TABLE_STATUS+" == '1' and "+Database.NOTIFICATION_TABLE_MOBILE_NO+" = '"+mobno+"'";
        // final String SQL_STATEMENT = "SELECT COUNT(*) FROM "+ Database.NOTIFICATION_TABLE+" WHERE "+Database.NOTIFICATION_TABLE_STATUS+" != '1' and "+Database.NOTIFICATION_TABLE_MOBILE_NO+" = "+mobno;

        Log.e("", "get ccount " + "SELECT COUNT(*) FROM " + Database.NOTIFICATION_TABLE + " WHERE " + Database.NOTIFICATION_TABLE_STATUS + " !=1");
        Log.e("", "get ccount " + "SELECT COUNT(*) FROM " + Database.NOTIFICATION_TABLE + " WHERE " + Database.NOTIFICATION_TABLE_STATUS + " !=1");

        Cursor c = database.rawQuery(SQL_STATEMENT, null);
        // count=c.getCount();
        c.moveToFirst();
        count = c.getInt(0);

        return count;
    }

    public int getStatustotalCount(String mobno) {
        int count = 0;
/*
        Cursor cursor = database.rawQuery("select count (*) from "+ Database.NOTIFICATION_TABLE+" where "+Database.NOTIFICATION_TABLE_STATUS+
                "= 0", null);
        count=cursor.getCount();
*/
        final String SQL_STATEMENT = "SELECT COUNT(*) FROM " + Database.NOTIFICATION_TABLE + " WHERE " + Database.NOTIFICATION_TABLE_USER_ID + " = " + mobno;
        //  final String SQL_STATEMENT = "SELECT COUNT(*) FROM "+ Database.NOTIFICATION_TABLE+" WHERE "+Database.NOTIFICATION_TABLE_STATUS+" != '1' and "+Database.NOTIFICATION_TABLE_MOBILE_NO+" = "+mobno;

        Log.e("", "get ccount " + "SELECT COUNT(*) FROM " + Database.NOTIFICATION_TABLE + " WHERE " + Database.NOTIFICATION_TABLE_STATUS + " !=1");

        Cursor c = database.rawQuery(SQL_STATEMENT, null);
        // count=c.getCount();
        c.moveToFirst();
        count = c.getInt(0);

        return count;
    }

}
