package com.taraba.gulfoilapp;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * Created by android1 on 12/17/15.
 */
public class MyTrackConstants {

    public static String url = "http://192.168.1.109/track_me/";
    /**
     * _mStringUrlForPhoneVerification => url for sending verification code
     */
    public static String _mStringUrlForPhoneVerification = url + "tfk_project_pages/check_user.php";
    public static String _mStringUrlForValidateVerficationCode = url + "tfk_project_pages/check_valid_activation_code.php";

    public static String _mStringTermsConditions = "Terms and Conditions";
    public static String _mStringFAQ = "FAQ's";
    public static String _mStringAboutUs = "About Us";
    public static String _mStringHelp = "Help";
    public static String _mStringRewards = "Rewards";
    public static String _mStringNotification = "Notifications";
    public static String _mStringTransactions = "Transactions";

    public static String _mStringProductUpdatedate = "product_update_date";
    public static String _mStringCategoryUpdateDate = "Cat_update_date";
    public static String _mStringProductCode = "product_code";
    public static String _mStringContent = "content";
    public static String _mStringOrderRequestNo = "order_request_no";

    public static String _mStringSerchData = "search_data";
    public static String _mStringSerchtype = "search_type";


/*	public static DataBaseHelper mDatabaseHelper;

	public static void createDatabaseObj(Context mContext)
	{
		mDatabaseHelper = new DataBaseHelper(mContext);
	}

	MyTrackConstants.createDatabaseObj(AddItemToDashboardActivity.this);
	MyTrackConstants.mDatabaseHelper.setMenusName(gitm);
	*/

    /**
     * setColors
     * <p>
     * void
     *
     * @param view              - view on which you want to set gradient colors
     * @param mStringStartColor
     * @param mStringEndColor   Created Date : Jul 21, 2015
     */
    public static void setColors(View view, int mStringStartColor, int mStringEndColor) {
        int[] colors = {mStringStartColor, mStringEndColor};

        GradientDrawable drawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, colors);
        drawable.setCornerRadius(10f);
        drawable.setStroke(1, Color.parseColor("#000000"));
        /*GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setColor(Color.parseColor(mStringColor));*/
        view.setBackgroundDrawable(drawable);
    }
}