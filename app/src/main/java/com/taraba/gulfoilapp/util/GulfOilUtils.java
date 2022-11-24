package com.taraba.gulfoilapp.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.taraba.gulfoilapp.AppConfig;
import com.taraba.gulfoilapp.constant.UserType;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Pravin Dharam on 12-04-2017.
 */

public class GulfOilUtils {
    private SharedPreferences userPref = AppConfig.getInstance().getApplicationContext().getSharedPreferences(
            "signupdetails", Context.MODE_PRIVATE);
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy HH:mm:ss";
    public static final String DD_MMMM_YYYYY_HH_MM_AMPM = "dd MMMM yyyy HH:mm a";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String dd_MMM_yyyy = "dd MMM yyyy";

    //Added by Pravin Dharam
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat1.format(calendar.getTime());
        return currentDate;
    }

    public static String formatdate(String fdate, String currentFormat, String requiredFormat) {
        String datetime = null;
        DateFormat inputFormat = new SimpleDateFormat(currentFormat);
        SimpleDateFormat d = new SimpleDateFormat(requiredFormat);
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        } catch (ParseException e) {

        }
        return datetime;


    }


    public static Spanned fromHtml(String html) {
        Spanned result = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
            } else {
                result = Html.fromHtml(html);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getStr(int name, Context context) {
        String str = "";
        try {
            str = context.getResources().getString(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void callTollFree(Context context) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:18002094470"));
        context.startActivity(callIntent);
    }

    public static String getStr(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            try {
                return jsonObject.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    public static Spanned getHtmlText(String htmlText) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Html.fromHtml(htmlText);
        } else {
            return Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY);
        }
    }

    public static String chkNullDefaultValue(String value, String defaultValue) {
        return TextUtils.isEmpty(value) ? defaultValue : value;
    }

    public UserType getUserType() {
        String strUserYype = userPref.getString("retailer_type", "");
        if (strUserYype.equals(UserType.ROYAL.getValue())) {
            return UserType.ROYAL;
        } else if (strUserYype.equals(UserType.ELITE.getValue())) {
            return UserType.ELITE;
        } else if (strUserYype.equals(UserType.CLUB.getValue())) {
            return UserType.CLUB;
        } else {
            return UserType.UNNATI;
        }
    }
}
