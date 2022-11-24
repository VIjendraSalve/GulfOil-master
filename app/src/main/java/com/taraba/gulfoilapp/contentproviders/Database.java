package com.taraba.gulfoilapp.contentproviders;

/**
 * Created by android3 on 12/24/15.
 */
public class Database {

    /// Application Version

    // Database
    public static final String APP_NAMESPACE = "com.taraba.gulfoilapp";
    public static final String LATEST_CIRCULAR_ID = "latest_circular_ID";
    public static final String SCHOOL_DATABSE_NAME = "GULFOILDATABASE.db";

    public static final String LATEST_USER_ID = "latest_homework_ID";

    // Database Version
    public static final int DATABASE_VERSION = 3;


    // ---------------Circular-----------------
    public static final String STATE_TABLE = "tbl_state_district";
    public static final String STATE_TABLE_TRNO = "trno";
    public static final String STATE_TABLE_STATE = "state";
    public static final String STATE_TABLE_DISTRICT = "district";

    //-----------------User table----------------------
    public static final String USER_TABLE = "tbl_user";
    public static final String USER_TABLE_TRNO = "trno";
    public static final String USER_TABLE_PICTURE = "picture";
    public static final String USER_TABLE_FNAME = "fname";
    public static final String USER_TABLE_LNAME = "lname";
    public static final String USER_TABLE_GENDER = "gender";
    public static final String USER_TABLE_NOMININAME = "nomini";
    public static final String USER_TABLE_NOMINIRELATION = "nominirely";
    public static final String USER_TABLE_MOTHERNAME = "mothername";
    public static final String USER_TABLE_WORKSHOPNAME = "workshopname";
    public static final String USER_TABLE_STATE = "state";
    public static final String USER_TABLE_DISTRICT = "district";

    public static final String USER_TABLE_STATE_MAPPED = "state_mapped";
    public static final String USER_TABLE_DISTRICT_MAPPED = "district_mapped";

    public static final String USER_TABLE_PINCODE = "pincode";
    public static final String USER_TABLE_TALUKA = "taluka";
    public static final String USER_TABLE_CITY = "city";
    public static final String USER_TABLE_SHOPADDR = "shopadd";
    public static final String USER_TABLE_LANDMARK = "landmark";
    public static final String USER_TABLE_MOBNO = "mobile_no";
    public static final String USER_TABLE_EMAIL = "email";
    public static final String USER_TABLE_SECTOR = "sector";
    public static final String USER_TABLE_SPECIALIZATION = "specialization";
    public static final String USER_TABLE_DOB = "dob";
    public static final String USER_TABLE_REGDATE = "regdate";
    public static final String USER_TABLE_TOTALSPERCONSPERMONTH = "toatalsperconpermonth";
    public static final String USER_TABLE_SPERPARTCONFORMMVECPERMONTH = "sperpartconformmvehicpermonth";
    public static final String USER_TABLE_MMGENUSPARECONPERMONTH = "mmgenuspareconpermonth";
    public static final String USER_TABLE_TOTALVEHICALPERMONTH = "totalvehicalpermonth";
    public static final String USER_TABLE_MAHINDRAVEHICALPERMONTH = "mahindravehicalpermonth";
    public static final String USER_TABLE_NOOFMECHANICS = "noofmechanics";
    public static final String USER_TABLE_POINT = "point";
    public static final String USER_TABLE_STATUS = "status";
    public static final String USER_TABLE_TYPE = "type";
    public static final String USER_TABLE_PARTICIPANTCODE = "participant_code";
    public static final String USER_TABLE_PARTICIPANt_ID_PK = "participant_id_pk";
    public static final String USER_TABLE_FORMFILLDATE = "form_fillup_date";
    public static final String USER_TABLE_PARTICIPANT_CLAIM_HISTORY = "participantclaimhistory";

    public static final String HISTRY_TABLE = "history";
    public static final String ID_COL = "id";
    public static final String TEXT_COL = "text";
    public static final String FORMAT_COL = "format";
    public static final String DISPLAY_COL = "display";
    public static final String TIMESTAMP_COL = "timestamp";
    public static final String DETAILS_COL = "details";
    public static final String HISTRY_PARTICIPANT_ID_COL = "participant_login_id";
    public static final String HISTRY_PUBLISH_TYPE_COL = "barcode_publish_type";

    public static final String PARTICIPANT_CLAIM_HISTRY_TABLE = "participant_claim_history";
    public static final String PARTICIPANT_CLAIM_HISTRY_ID_COL = "claim_participant_id";
    public static final String PARTICIPANT_CLAIM_HISTRY_UID_COL = "claim_uid";
    public static final String PARTICIPANT_CLAIM_HISTRY_VALIDATION_STATUS_COL = "claim_validation_status";
    public static final String PARTICIPANT_CLAIM_HISTRY_POINT_COL = "claim_point";
    public static final String PARTICIPANT_CLAIM_HISTRY_PARTNO_COL = "claim_part_no";
    public static final String PARTICIPANT_CLAIM_HISTRY_RECORD_DATE_COL = "claim_record_date";
    public static final String PARTICIPANT_CLAIM_HISTRY_UID_STATUS_COL = "claim_uid_status";

    // ---------------Category-----------------
    public static final String CATEGORY_TABLE = "tbl_category";
    public static final String CATEGORY_TABLE_TRNO = "trno";
    public static final String CATEGORY_TABLE_NAME = "name";
    public static final String CATEGORY_TABLE_SORT_ORDER = "sort_date";
    public static final String CATEGORY_TABLE_ACTIVE = "active";
    public static final String CATEGORY_TABLE_RECORD_DATE = "record_date";
    public static final String CATEGORY_TABLE_LAST_UPDATED = "last_updated";


    // ---------------Product-----------------
    public static final String PRODUCT_TABLE = "tbl_product";
    public static final String PRODUCT_TABLE_TRNO = "trno";
    public static final String PRODUCT_TABLE_PRODUCT_ID = "product_id";
    public static final String PRODUCT_TABLE_NAME = "name";
    public static final String PRODUCT_TABLE_PRODUCT_CODE = "product_code";
    public static final String PRODUCT_TABLE_SMALL_DESC = "small_description";
    public static final String PRODUCT_TABLE_SMALL_IMAGE_LINK = "small_image_link";
    public static final String PRODUCT_TABLE_SORT_ORDER = "sort_order";
    public static final String PRODUCT_TABLE_POINTS = "points";
    public static final String PRODUCT_TABLE_PRODUCTS_VISIBLE = "product_visible";
    public static final String PRODUCT_TABLE_ACTIVE_STATUS = "active_status";
    public static final String PRODUCT_TABLE_PRODUCT_TYPE = "product_type";
    public static final String PRODUCT_TABLE_RECORD_DATE = "record_date";
    public static final String PRODUCT_TABLE_UPDATE_DATE = "update_date";
    public static final String PRODUCT_TABLE_CATEGORY_ID = "category_id";
    public static final String PRODUCT_TABLE_PRODUCT_DETAILS = "produt_details";
    public static final String PRODUCT_TABLE_BONUS_DETAILS = "bonus_details";
    public static final String PRODUCT_TABLE_DATE = "date";


    // ---------------Notification-----------------
    public static final String NOTIFICATION_TABLE = "tbl_notification";
    public static final String NOTIFICATION_TABLE_TRNO = "trno";
    public static final String NOTIFICATION_TABLE_TYPE = "type";
    public static final String NOTIFICATION_TABLE_USER_ID = "user_id";
    public static final String NOTIFICATION_TABLE_TITLE = "title";
    public static final String NOTIFICATION_TABLE_DESC = "description";
    public static final String NOTIFICATION_TABLE_IMG = "image";
    public static final String NOTIFICATION_TABLE_CHANNEL_TYPE = "channel_type";
    public static final String NOTIFICATION_TABLE_DATE = "mdate";
    public static final String NOTIFICATION_TABLE_STATUS = "status";
    public static final String STATE_TABLE_CREATE_SCRIPT = "create table "
            + STATE_TABLE + " ( "
            + STATE_TABLE_TRNO + " integer primary key, "
            + STATE_TABLE_STATE + " Text , "
            + STATE_TABLE_DISTRICT + " Text )";

  /*  public static final String NOTIFICATION_TABLE_CREATE_SCRIPT = "create table "
            + NOTIFICATION_TABLE + " ( "
            + NOTIFICATION_TABLE_TRNO + " integer primary key AUTOINCREMENT, "
            + NOTIFICATION_TABLE_MOBILE_NO + " Text , "
            + NOTIFICATION_TABLE_TYPE + " Text , "
            + NOTIFICATION_TABLE_DATA + " Text , "
            + NOTIFICATION_TABLE_DATE + " Text , "
            +NOTIFICATION_TABLE_STATUS+" Text )";
*/


    public static final String NOTIFICATION_TABLE_CREATE_SCRIPT = "create table "
            + NOTIFICATION_TABLE + " ( "
            + NOTIFICATION_TABLE_TRNO + " integer primary key AUTOINCREMENT, "
            + NOTIFICATION_TABLE_USER_ID + " Text , "
            + NOTIFICATION_TABLE_TYPE + " Text , "
            + NOTIFICATION_TABLE_TITLE + " Text , "
            + NOTIFICATION_TABLE_DESC + " Text , "
            + NOTIFICATION_TABLE_IMG + " Text , "
            + NOTIFICATION_TABLE_CHANNEL_TYPE + " Text , "
            + NOTIFICATION_TABLE_DATE + " Text , "
            + NOTIFICATION_TABLE_STATUS + " Text )";

    public static final String USER_TABLE_CREATE_SCRIPT = "create table "
            + USER_TABLE + " ( "
            + USER_TABLE_TRNO + " integer primary key, "
            + USER_TABLE_PICTURE + " Text , "
            + USER_TABLE_FNAME + " Text , "
            + USER_TABLE_LNAME + " Text , "
            + USER_TABLE_GENDER + " Text , "
            + USER_TABLE_NOMININAME + " Text , "
            + USER_TABLE_NOMINIRELATION + " Text , "
            + USER_TABLE_MOTHERNAME + " Text , "
            + USER_TABLE_WORKSHOPNAME + " Text , "
            + USER_TABLE_STATE + " Text , "
            + USER_TABLE_STATE_MAPPED + " Text , "
            + USER_TABLE_DISTRICT + " Text , "
            + USER_TABLE_PINCODE + " Text , "
            + USER_TABLE_TALUKA + " Text , "
            + USER_TABLE_CITY + " Text , "
            + USER_TABLE_SHOPADDR + " Text , "
            + USER_TABLE_LANDMARK + " Text , "
            + USER_TABLE_MOBNO + " Text , "
            + USER_TABLE_EMAIL + " Text , "
            + USER_TABLE_SECTOR + " Text , "
            + USER_TABLE_SPECIALIZATION + " Text , "
            + USER_TABLE_DOB + " Text , "
            + USER_TABLE_REGDATE + " Text , "
            + USER_TABLE_TOTALSPERCONSPERMONTH + " Text , "
            + USER_TABLE_SPERPARTCONFORMMVECPERMONTH + " Text , "
            + USER_TABLE_MMGENUSPARECONPERMONTH + " Text , "
            + USER_TABLE_TOTALVEHICALPERMONTH + " Text , "
            + USER_TABLE_MAHINDRAVEHICALPERMONTH + " Text , "
            + USER_TABLE_NOOFMECHANICS + " Text , "
            + USER_TABLE_POINT + " Text , "
            + USER_TABLE_STATUS + " Text , "
            + USER_TABLE_PARTICIPANTCODE + " Text , "
            + USER_TABLE_PARTICIPANt_ID_PK + " Text , "
            + USER_TABLE_FORMFILLDATE + " Text , "
            + USER_TABLE_TYPE + " Text , "
            + USER_TABLE_PARTICIPANT_CLAIM_HISTORY + " Text )";

    public static final String HISTRY_TABLE_CREATE_SCRIPT = "create table "
            + HISTRY_TABLE + " ( "
            + ID_COL + " integer primary key, "
            + TEXT_COL + " Text , "
            + FORMAT_COL + " Text , "
            + DISPLAY_COL + " Text , "
            + TIMESTAMP_COL + " Text , "
            + DETAILS_COL + " Text , "
            + HISTRY_PARTICIPANT_ID_COL + " Text , "
            + HISTRY_PUBLISH_TYPE_COL + " Text )";

    public static final String PARTICIPANT_CLAIM_HISTRY_TABLE_CREATE_SCRIPT = "create table "
            + PARTICIPANT_CLAIM_HISTRY_TABLE + " ( "
            + PARTICIPANT_CLAIM_HISTRY_ID_COL + " integer , "
            + PARTICIPANT_CLAIM_HISTRY_UID_COL + " Text , "
            + PARTICIPANT_CLAIM_HISTRY_VALIDATION_STATUS_COL + " Text , "
            + PARTICIPANT_CLAIM_HISTRY_POINT_COL + " Text , "
            + PARTICIPANT_CLAIM_HISTRY_PARTNO_COL + " Text , "
            + PARTICIPANT_CLAIM_HISTRY_RECORD_DATE_COL + " Text , "
            + PARTICIPANT_CLAIM_HISTRY_UID_STATUS_COL + " Text )";

    public static final String CATEGORY_TABLE_CREATE_SCRIPT = "create table "
            + CATEGORY_TABLE + " ( "
            + CATEGORY_TABLE_TRNO + " integer primary key, "
            + CATEGORY_TABLE_NAME + " Text , "
            + CATEGORY_TABLE_SORT_ORDER + " integer , " + CATEGORY_TABLE_ACTIVE + " integer , "
            + CATEGORY_TABLE_RECORD_DATE + " text , " + CATEGORY_TABLE_LAST_UPDATED + " text )";


    public static final String PRODUCT_TABLE_CREATE_SCRIPT = "create table "
            + PRODUCT_TABLE + " ( "
            + PRODUCT_TABLE_TRNO + " integer primary key AUTOINCREMENT, "
            + PRODUCT_TABLE_PRODUCT_ID + " integer, "
            + PRODUCT_TABLE_NAME + " text , "
            + PRODUCT_TABLE_PRODUCT_CODE + " Text , "
            + PRODUCT_TABLE_SMALL_DESC + " text , " + PRODUCT_TABLE_SMALL_IMAGE_LINK + " text, "
            + PRODUCT_TABLE_SORT_ORDER + " integer, " + PRODUCT_TABLE_POINTS + " integer , " + PRODUCT_TABLE_PRODUCTS_VISIBLE + " integer , "
            + PRODUCT_TABLE_ACTIVE_STATUS + " integer , " + PRODUCT_TABLE_PRODUCT_TYPE + " text , " + PRODUCT_TABLE_RECORD_DATE + " text , "
            + PRODUCT_TABLE_UPDATE_DATE + " text , " + PRODUCT_TABLE_CATEGORY_ID + " text , " + PRODUCT_TABLE_PRODUCT_DETAILS + " text , "
            + PRODUCT_TABLE_BONUS_DETAILS + " text , " + PRODUCT_TABLE_DATE + ")";
}
