package com.taraba.gulfoilapp.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.taraba.gulfoilapp.AppConfig;
import com.taraba.gulfoilapp.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android1 on 12/17/15.
 */
public class UserFunctions {

    private JSONParser jsonParser;
    public static String mString_URL = "http://startdemo.in/POC/";
    public static String webservice_URL = mString_URL + "WS/event_apis.php";
    public static final int PERMISSION_READ_PHONE_STATE = 127;
    public static String mString_URL_M = BuildConfig.BASE_URL;


    public static String webservice_URL_Login = mString_URL_M + "login";
    public static String webservice_URL_PointCalculatorMasters = mString_URL_M + "points_master";
    public static String webservice_URL_TargetMeterCategory = mString_URL_M + "target_header";
    public static String webservice_URL_PointCalculator = mString_URL_M + "points_calculater";
    public static String webservice_URL_GetParticipant = mString_URL_M + "get_participant";
    public static String webservice_URL_GetV2RetailerParticipant = mString_URL_M + "v2_get_participant_profile";
    public static String webservice_URL_v2_Otp_UpdateParticipant_profile = mString_URL_M + "v2_Otp_UpdateParticipant_profile";
    public static String webservice_URL_v2_Otp_Verify = mString_URL_M + "v2_verify_update_profile";
    public static String webservice_URL_v2_Resend_Otp_Update_Profile = mString_URL_M + "v2_Resend_Otp_profile_update";

    public static String webservice_URL_GetMechniceParticipant = mString_URL_M + "transaction_history";

    public static String webservice_URL_CheckParticipantAvailability = mString_URL_M + "check_participant_availability";

    public static String webservice_URL_Registration = mString_URL_M + "registration";
    public static String webservice_URL_GenuieCheck = mString_URL_M + "genuineCheck";
    public static String webservice_URL_SubmitOtp = mString_URL_M + "otpSubmit";
    public static String webservice_URL_ReSubmitOtp = mString_URL_M + "resendOtp";
    public static String webservice_URL_UploadCode = mString_URL_M + "accumulateCode";
    public static String webservice_URL_forgotpass = mString_URL_M + "forgotPassword";
    public static String webservice_URL_changepass = mString_URL_M + "changePassword";

    //---------------------Phase 2 ----------------------------
    public static String webservice_URL_CategoryList = mString_URL_M + "categoryList";
    public static String webservice_URL_ProductList = mString_URL_M + "productList";
    public static String webservice_URL_ProductData = mString_URL_M + "productData";
    public static String webservice_URL_PlaceOrder = mString_URL_M + "placeOrder";
    public static String webservice_URL_RecentOtp = mString_URL_M + "resendOrderOtp";
    public static String webservice_URL_CancelOrder = mString_URL_M + "cancelOrder";
    public static String webservice_URL_OrderHistory = mString_URL_M + "orderHistory";
    public static String webservice_URL_OrderOtpSubmit = mString_URL_M + "orderOtpSubmit";
    public static String webservice_URL_TransactionHistory = mString_URL_M + "transaction_history";
    public static String webservice_URL_ADDRESS = mString_URL_M + "get_retailer_address";
    //http://gulfoiluat.grgrewards.in/app/get_retailer_address

    public static String webservice_URL_UpdateProfile = mString_URL_M + "update_participant_profile";
    public static String webservice_URL_GetPendingParticipant = mString_URL_M + "get_all_pending_participants";
    //http://gulfoiluat.grgrewards.in/app/get_all_pending_participants
    public static String webservice_URL_UpdateReatilerProfile = mString_URL_M + "update_participant_info";
    public static String webservice_URL_Redemption_window = mString_URL_M + "redemption_window";

    public static String webservice_URL_UpdateReatilerProfileGulf = mString_URL_M + "verify_profile_changes";
    public static String webservice_URL_Whats_new = mString_URL_M + "whats_new_list";
    public static String webservice_URL_ReatilerTargetMeter = mString_URL_M + "reatiler_target_meter";

    public static String webservice_URL_SchemeLetterr = mString_URL_M + "scheme_letter_list";
    public static String webservice_URL_SchemeLetterDetail = mString_URL_M + "view_scheme_letter_desc";
    public static String webservice_URL_TargetMeter = mString_URL_M + "target_meter";
    public static String webservice_URL_CurrentBalance = mString_URL_M + "current_balance";
    public static String webservice_URL_DIGITAL_ORDER_HISTORY = mString_URL_M + "digitalOrderHistory";
    public static String webservice_URL_voucher_details = mString_URL_M + "gulf_voucher_detail";

    public static String webservice_URL_CAMPAIGN_REWARDS = mString_URL_M + "magicOrder";
    public static String webservice_URL_CAMPAIGN_REWARDS_DETAILS = mString_URL_M + "ev_details";
    public static String webservice_URL_ROYALTY_BENEFIT = mString_URL_M + "royalOrder";


    public static String webservice_URL_get_upoaded_invoice_status = mString_URL_M + "get_invoices";
    public static String webservice_URL_upload_tally_invoice = mString_URL_M + "upload_invoice";

    public static String webservice_URL_KC_CATEGORY_ONE = mString_URL_M + "get_kc_category_one";
    public static String webservice_URL_KC_CATEGORY_TWO = mString_URL_M + "get_kc_category_two";
    public static String webservice_URL_KC_VEHICLE_DETAILS = mString_URL_M + "get_kc_vehicle_details";
    public static String webservice_URL_SPLASH_POP_UP = mString_URL_M + "splash_popup";

    public static final String program_broucher_url = "https://gulfoiluat.grgrewards.in/public_files/brochure/Gulf%20Unnati%20brochure.pdf";
//http://dev.grgrewards.in/gulfoil/app/verify_profile_changes

    public UserFunctions() {
        jsonParser = new JSONParser();
    }

    public JSONObject PointsMasterWS() {

        Log.e("WS", "Points Calculator Master web service" + webservice_URL_PointCalculatorMasters);

        JSONObject params = new JSONObject();


        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_PointCalculatorMasters, params);


        //System.out.println("Got result : " + json.toString());

        Log.e("", "Login response" + json);
        // return json
        return json;
    }

    public JSONObject TargetMeterCategoryWS() {

        Log.e("WS", "Points Calculator Master web service" + webservice_URL_TargetMeterCategory);

        JSONObject params = new JSONObject();


        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_TargetMeterCategory, params);


        //System.out.println("Got result : " + json.toString());

        Log.e("", "Login response" + json);
        // return json
        return json;
    }

    public JSONObject calculatePointsWS(String sku_number, String quantity) {

        Log.e("WS", "Points Calculator web service" + webservice_URL_PointCalculator);

        JSONObject params = new JSONObject();
        try {
            params.put("sku_number", sku_number);
            params.put("quality", quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_PointCalculator, params);


        //System.out.println("Got result : " + json.toString());

        Log.e("", "Login response" + json);
        // return json
        return json;
    }

    public JSONObject Login_webservice_call(String mStringUsername, String mStringPassword, String mStringObjectId, String regId, String termsCondition) {

        Log.e("Login web service : ", "" + webservice_URL_Login + " Login web service : Username : " + mStringUsername + " , Password : " + mStringPassword + " , ObjectId : " + mStringObjectId);

        JSONObject params = new JSONObject();

        try {
            //params.put("action", "login_grg");
            params.put("username", "" + mStringUsername);
            params.put("object_id", "" + regId);
            params.put("password", "" + mStringPassword);
            params.put("device", "Android");
            params.put("registrationId", "" + regId);
            params.put("notification_token", "" + regId);
            params.put("terms_condition", "" + termsCondition);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_Login, params);


        //System.out.println("Got result : " + json.toString());

        Log.e("", "Loginresponse" + json);
        // return json
        return json;
    }

    public JSONObject Redimption_Window() {
        // Log.e("Redimption_Window web service : ", ""+webservice_URL_Redemption_window);
        JSONObject params = new JSONObject();
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_Redemption_window, params);
        Log.e("", "Redimption_Window response" + json);
        return json;
    }

    public JSONObject GenuieCheck_webservice_call(String mStringUid, String mStringMobNo) {

        Log.e("Login web service : ", "Login web service : Username : " + mStringUid + " , Password : " + mStringMobNo);

        JSONObject params = new JSONObject();

        try {
            //  params.put("action", "login_grg");
            params.put("uniqueid", "" + mStringUid);
            params.put("mobile_no", "" + mStringMobNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_GenuieCheck, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject ForgotPass_webservice_call_NewPAss(String mStringMobNo) {

        JSONObject params = new JSONObject();

        try {
            params.put("username", "" + mStringMobNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_forgotpass, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    //Added by Chaitali
    public JSONObject Transaction_webservice_call(String mStringLoginId) {

        JSONObject params = new JSONObject();

        try {
            params.put("login_id", "" + mStringLoginId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_TransactionHistory, params);
        System.out.println("Got result :Transaction " + json.toString());
        // return json
        return json;
    }

    public JSONObject signUpUserWebService(String stremailid, String strimeino, String strname,
                                           String strPhoneNo, String strPassword, String regId, String login_reg, String mStringTokenNo) {


        // Building Parameters
        //   List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONObject params = new JSONObject();

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL, params);
        return json;
    }

    public JSONObject ChangePass_webservice_call(String mStringUsername, String mStringoldPassword, String mStringNewPassword, String mStringConfirmPass) {

        JSONObject params = new JSONObject();

        try {
            params.put("user_login_id", "" + mStringUsername);
            params.put("old_password", "" + mStringoldPassword);
            params.put("new_password", "" + mStringNewPassword);
            params.put("confirm_new_password", "" + mStringConfirmPass);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_changepass, params);
        //	System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject GetParticipant(String mStringMobNo, String mStringSearchType, String loginUserType, String userLoginId) {
        Log.e("URL : ", "URL : " + webservice_URL_GetParticipant);
        Log.e("Login web service : ", "Login web service : Username : " + mStringMobNo);
        Log.e("Login web service : ", "Login web service : mStringSearchType : " + mStringSearchType);
        Log.e("Login web service : ", "Login web service : loginUserType : " + loginUserType);
        Log.e("Login web service : ", "Login web service : userLoginId : " + userLoginId);

        JSONObject params = new JSONObject();

        try {
            if (mStringSearchType.equals("Search By Mobile Number")) {
                params.put("mobile_number", "" + mStringMobNo);
                params.put("login_user_type", "" + loginUserType);
                params.put("user_login_id", "" + userLoginId);
                params.put("search_by", "mobile_number");
                Log.e("", "JSONfor getParticipants : " + params.toString());
            } else if (mStringSearchType.equals("Search By Dealer Code")) {
                params.put("retailer_code", "" + mStringMobNo);
                params.put("login_user_type", "" + loginUserType);
                params.put("user_login_id", "" + userLoginId);
                params.put("search_by", "retailer_code");
                Log.e("", "JSONfor getParticipants : " + params.toString());
            } else if (mStringSearchType.equals("Search By Shop Name")) {
                params.put("workshop_name", "" + mStringMobNo);
                params.put("login_user_type", "" + loginUserType);
                params.put("user_login_id", "" + userLoginId);
                params.put("search_by", "workshop_name");
                Log.e("", "JSONfor getParticipants : " + params.toString());
            }
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        Log.e("UserFunctions", "GetParticipant: params: " + params.toString());
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_GetParticipant, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject GetPendingParticipant(String userLoginId) {
        Log.e("URL : ", "URL : " + webservice_URL_GetPendingParticipant);
        Log.e("Login web service : ", "Login web service : userLoginId : " + userLoginId);

        JSONObject params = new JSONObject();

        try {

            params.put("fls_login_id", "" + userLoginId);


        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_GetPendingParticipant, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject GetRetailParticipant(String retailerID) {
        Log.e("URL : ", "URL : " + webservice_URL_GetV2RetailerParticipant);
        Log.e("Login web service : ", "Login web service : userLoginId : " + retailerID);

        JSONObject params = new JSONObject();

        try {

            params.put("participant_login_id", "" + retailerID);


        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_GetV2RetailerParticipant, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject UpdateRetailerProfile(String retailerID, String fls_login_id, String workshop_name, String mobile_no) {
        Log.e("URL : ", "URL : " + webservice_URL_v2_Otp_UpdateParticipant_profile);
        Log.e("Login web service : ", "Login web service : userLoginId : " + retailerID);

        JSONObject params = new JSONObject();

        try {

            params.put("participant_login_id", "" + retailerID);
            params.put("fls_login_id", "" + fls_login_id);
            params.put("workshop_name", "" + workshop_name);
            params.put("mobile_no", "" + mobile_no);


        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_v2_Otp_UpdateParticipant_profile, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject verfiyOTPV2(String retailerID, String fls_login_id, String otp) {
        Log.e("URL : ", "URL : " + webservice_URL_v2_Otp_Verify);
        Log.e("Login web service : ", "Login web service : userLoginId : " + retailerID);

        JSONObject params = new JSONObject();

        try {

            params.put("participant_login_id", "" + retailerID);
            params.put("fls_login_id", "" + fls_login_id);
            params.put("otp", "" + otp);


        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_v2_Otp_Verify, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject resendOTPV2(String retailerID) {
        Log.e("URL : ", "URL : " + webservice_URL_v2_Resend_Otp_Update_Profile);
        Log.e("Login web service : ", "Login web service : participant_login_id : " + retailerID);

        JSONObject params = new JSONObject();

        try {

            params.put("participant_login_id", "" + retailerID);


        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_v2_Resend_Otp_Update_Profile, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

//--------------------------------------------- check participant availability-----------------------------------------------

    public JSONObject CheckParticipantAvailability(String mStringMobNo) {
        Log.e("Login web service : ", "" + webservice_URL_CheckParticipantAvailability + "   Login web service : Username : " + mStringMobNo);

        JSONObject params = new JSONObject();

        try {
            params.put("mobile_number", "" + mStringMobNo);

            Log.e("", "JSONfor CheckParticipantAvailability : " + params.toString());

        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_CheckParticipantAvailability, params);
        System.out.println("Got result of CheckParticipantAvailability : " + json.toString());
        Log.e("", "Got result of CheckParticipantAvailability : " + json.toString());
        return json;
    }

    public JSONObject send_otp(String strLoginid, String strParticipantid, String mStringMobNo, String mStringOTP) {

        Log.e("Login web service : ", "Login web service : Username : " + mStringMobNo);

        JSONObject params = new JSONObject();

        try {
            //  params.put("action", "verify_otp");
            params.put("login_id", "" + strLoginid);
            params.put("participant_login_id", "" + strParticipantid);
            params.put("mobile_no", "" + mStringMobNo);
            params.put("otp", "" + mStringOTP);
            Log.e("CHECKLOG : ", "CHECKLOG : " + strLoginid + "PID= " + strParticipantid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_SubmitOtp, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject Resend_otp(String strParticipantid, String mStringMobNo) {

        Log.e("Login web service : ", "Login web service : Username : " + mStringMobNo);

        JSONObject params = new JSONObject();

        try {
            //  params.put("action", "verify_otp");

            params.put("participant_login_id", "" + strParticipantid);
            params.put("mobile_no", "" + mStringMobNo);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_ReSubmitOtp, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject uploadCodes(String strLoginid, String strParticipantid, String mStringCodes) {

        Log.e("Login web service : ", "Login web service : Username : " + mStringCodes);

        JSONObject params = new JSONObject();

        try {
            //  params.put("action", "verify_otp");
            params.put("login_id", "" + strLoginid);
            params.put("participant_login_id", "" + strParticipantid);
            params.put("codes", "" + mStringCodes);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_UploadCode, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject Mech_Reg_Data(String strRegPhoto, String strFirstName, String strLastName, String strNomineName, String strNomineRelation, String strMotherName, String strWorkShopName,
                                    String strPincode, String strTaluka, String strVillage, String strshopAdress, String strLandmark, String strOwnerMobileNumber, String strEmail,
                                    String strDateOfBirth, String strregFillDate, String strregtoatalSperConsumptioperMonth, String strsperpartconsuptionPErMonth,
                                    String strmmgenuinesparespartsconsuptionPerMoth, String strtotalVehicalsperMonth, String strmahindraVehicalsperMonth,
                                    String strnoofMechanics, String strGender, String strSector, String strSpecialization, String strState, String strDistrict, String strPhoto, String strTrno, String strUserData) {

        Log.e("Image Path in pass: ", "in Befor in userfunction  :" + strPhoto);
        Log.e("District Data: ", "District in mech_reg_data  :" + strDistrict);

        // Building Parameters
        JSONObject params = new JSONObject();

        try {
            //  params.put("action", "login_grg");

            params.put("action", "add_record");

            params.put("photo", strRegPhoto);
            params.put("first_name", strFirstName);
            params.put("last_name", strLastName);
            params.put("nominee_name", strNomineName);
            params.put("nominee_relation", strNomineRelation);
            params.put("mothers_maiden_name", strMotherName);
            params.put("workshop_name", strWorkShopName);
            params.put("pincode", strPincode);
            params.put("taluka", strTaluka);
            params.put("city", strVillage);
            params.put("shop_add", strshopAdress);
            params.put("landmark", strLandmark);
            params.put("mobile_number", strOwnerMobileNumber);
            params.put("email_address", strEmail);
            params.put("dob", strDateOfBirth);
            params.put("registration_date", strregFillDate);
            params.put("totalconsumption", strregtoatalSperConsumptioperMonth);
            params.put("spconsumption", strsperpartconsuptionPErMonth);
            params.put("genuineConsumption", strmmgenuinesparespartsconsuptionPerMoth);
            params.put("totalVehicles", strtotalVehicalsperMonth);
            params.put("mahindraVehicles", strmahindraVehicalsperMonth);
            params.put("mechanics", strnoofMechanics);
            params.put("nominee_gender", strGender);
            params.put("sector", strSector);
            params.put("specialisation", strSpecialization);
            params.put("state", strState);
            params.put("district", strDistrict);
            Log.e("", "Photo:" + strRegPhoto + "FName : " + strFirstName + " , LName : " + strLastName + " , NName : " + strNomineName + " , NRealtion : " + strNomineRelation);
            Log.e("", "MName : " + strMotherName + " , WNAME : " + strWorkShopName + " , PIN : " + strPincode + " , TALUKA : " + strTaluka);
            Log.e("", "city : " + strVillage + " , strshopAdress : " + strshopAdress + " , landmark : " + strLandmark + " , mobile_number : " + strOwnerMobileNumber);
            Log.e("", "email_address : " + strEmail + " , dob : " + strDateOfBirth + " , registration_date : " + strregFillDate + " , totalconsumption : " + strregtoatalSperConsumptioperMonth);
            Log.e("", "spconsumption : " + strsperpartconsuptionPErMonth + " , genuineConsumption : " + strmmgenuinesparespartsconsuptionPerMoth + " , totalVehicles : " + strtotalVehicalsperMonth + " , mahindraVehicles : " + strmahindraVehicalsperMonth);
            Log.e("", "mechanics : " + strnoofMechanics + " , nominee_gender : " + strGender + " , sector : " + strSector + " , strSpecialization : " + strSpecialization);
            Log.e("", "state : " + strState + " , strDistrict : " + strDistrict);


            params.put("user_data", strUserData.replace("\\", ""));
            Log.e("in userfunc ", "in userfunc UserData : " + strUserData.replace("\\", ""));
            //  params.put("status", "Management Verified");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json;
        // getting JSON Object
        Log.e("in userfunc ", "userfunc UserData Final : " + params);

        json = jsonParser.getJSONFromUrl(webservice_URL_Registration, params);
        //	System.out.println("Got result : " + json.toString());
        // return json
        return json;

    }


    public JSONObject upload_profile(int participant_id, String first_name, String last_name, String mobile_number,
                                     String taluka, String shop_add, String mechanics, String registration_date,
                                     String spconsumption, String nominee_gender, String city, String district, String mothers_maiden_name,
                                     String mahindraVehicles, String genuineConsumption, String nominee_name, String pincode, String specialisation,
                                     String landmark, String totalVehicles, String dob, String workshop_name, String nominee_relation,
                                     String totalconsumption, String email_address, String photo) {
        Log.e("Image Path in pass: ", "in Befor in userfunction  :" + photo);

        // Building Parameters
        JSONObject params = new JSONObject();

        try {
            //  params.put("action", "login_grg");

            params.put("action", "add_record");
            params.put("participant_id", participant_id);
            params.put("photo", photo);
            params.put("first_name", first_name);
            params.put("last_name", last_name);
            params.put("nominee_name", nominee_name);
            params.put("nominee_relation", nominee_relation);
            params.put("mothers_maiden_name", mothers_maiden_name);
            params.put("workshop_name", workshop_name);
            params.put("pincode", pincode);
            params.put("taluka", taluka);
            params.put("city", city);
            params.put("shop_add", shop_add);
            params.put("landmark", landmark);
            params.put("mobile_number", mobile_number);
            params.put("email_address", email_address);
            params.put("dob", dob);
            params.put("registration_date", registration_date);
            params.put("totalconsumption", totalconsumption);
            params.put("spconsumption", spconsumption);
            params.put("genuineConsumption", genuineConsumption);
            params.put("totalVehicles", totalVehicles);
            params.put("mahindraVehicles", mahindraVehicles);
            params.put("mechanics", mechanics);
            params.put("nominee_gender", nominee_gender);
            //   params.put("sector", sector);
            params.put("specialisation", specialisation);
            //   params.put("state", state);
            params.put("district", district);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json;
        // getting JSON Object
        Log.e("in userfunc ", "userfunc UserData Final update profile : " + params);


        json = jsonParser.getJSONFromUrl(webservice_URL_UpdateProfile, params);
        //	System.out.println("Got result : " + json.toString());
        // return json

        return json;


    }

    public JSONObject Search_mechanics(String mStringParticepantCode, String mStringMobileNo, String mStringWorkshopName) {

        JSONObject params = new JSONObject();

        try {
            params.put("action", "grg_mech_search");
            params.put("particepent_code", "" + mStringParticepantCode);
            params.put("mech_mobile_no", "" + mStringMobileNo);
            params.put("workshopname", "" + mStringWorkshopName);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL, params);
        //	System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject getProductList(String updateDate, String loginId) {
        JSONObject params = new JSONObject();
        Log.e("", "Update date in get product list user functions : " + updateDate);
        try {

            if (!(updateDate == null))
                params.put("update_date", updateDate);
                params.put("login_id", loginId);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_ProductList, params);
        //System.out.println("Got result : " + json.toString());

        return json;
    }

    public JSONObject getAllCategories(String updateDate, String login_id4) {
        JSONObject params = new JSONObject();
        Log.e("", "Update date in get all categories user functions : " + updateDate);
        try {

            if (!(updateDate == null)) {

                params.put("update_date", updateDate);
                params.put("login_id", login_id4);
            }
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        Log.e("", "Update category json request:" + webservice_URL_CategoryList + "" + params);
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_CategoryList, params);

        Log.e("", "Update category json response0" + json);
        // System.out.println("Got result : " + json.toString());

        return json;

    }

    public JSONObject getProductData(String product_code, String loginId) {
        JSONObject params = new JSONObject();
        //Log.e("", "Update date in get product list user functions : " + updateDate);
        try {

            if (!(product_code == null))
                params.put("product_code", product_code);
                params.put("login_id", loginId);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }
        Log.e("", "product json request:" + webservice_URL_CategoryList + "" + params);
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_ProductData, params);
        //System.out.println("Got result : " + json.toString());

        Log.e("", "json in get product data" + json.toString());

        return json;
    }


    public JSONObject getUserAddress(String retailer_code) {
        JSONObject params = new JSONObject();
        Log.e("retailer_code", "in json function : " + retailer_code);
        try {

            if (!(retailer_code == null))
                params.put("username", retailer_code.toString());
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }
        Log.e("address", "json request:" + webservice_URL_ADDRESS + params);
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_ADDRESS, params);
        //System.out.println("Got result : " + json.toString());

        Log.e("json ", "in get address data " + json.toString());

        return json;
    }


    public JSONObject proceedOrder(String participant_login_id, String product_code, String user_login_id, String addressType, String multi_qty) {
        JSONObject params = new JSONObject();
        Log.e("", "Update date in get product list user functions : " + participant_login_id + " , " + product_code + " , " + user_login_id + " , " + addressType);
        try {
            params.put("participant_login_id", participant_login_id);
            params.put("product_code", product_code);
            params.put("user_login_id", user_login_id);
            params.put("address", addressType);
            params.put("qty", multi_qty);
            //params.put("user_login_id","1");
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_PlaceOrder, params);
        Log.e("", "json in proceedOrder" + json.toString());
        return json;
    }

    public JSONObject resendOtp(String order_request_no) {
        JSONObject params = new JSONObject();
        try {
            params.put("order_request_no", order_request_no);

        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_RecentOtp, params);
        Log.e("", "json in resend otp" + json.toString());
        return json;
    }

    public JSONObject cancelOrder(String order_request_no, String user_login_id) {
        JSONObject params = new JSONObject();
        try {
            params.put("order_request_no", order_request_no);
            params.put("user_login_id", user_login_id);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_CancelOrder, params);
        Log.e("", "json in resend otp" + json.toString());
        return json;
    }

    public JSONObject orderDeatils(String order_request_no, String user_login_id) {
        JSONObject params = new JSONObject();
        try {
            params.put("order_request_no", order_request_no);
            params.put("user_login_id", user_login_id);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_PlaceOrder, params);
        Log.e("", "json in resend otp" + json.toString());
        return json;
    }

    public JSONObject orderHistory(String participant_login_id) {
        JSONObject params = new JSONObject();
        try {
            params.put("participant_login_id", participant_login_id);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_OrderHistory, params);
        Log.e("", "json in order history" + json.toString());
        return json;
    }

    public JSONObject submitOtp(String order_request_no, String user_login_id, String otp) {
        JSONObject params = new JSONObject();
        try {
            params.put("order_request_no", order_request_no);
            params.put("user_login_id", user_login_id);
            // params.put("user_login_id",1);
            params.put("otp", otp);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }
        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_OrderOtpSubmit, params);
        Log.e("", "json in order history" + json.toString());
        return json;
    }


    public JSONObject GetMechaniceParticipant(String userLoginId) {
        Log.e("URL : ", "URL : " + webservice_URL_GetMechniceParticipant);
        Log.e("Login web service : ", "Login web service : userLoginId : " + userLoginId);

        JSONObject params = new JSONObject();

        try {
            params.put("user_login_id", "" + userLoginId);
            Log.e("", "JSONfor getParticipants : " + params.toString());

        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_GetMechniceParticipant, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject updateRetailer(String fls_login_id, String participant_id, String retailer_code,
                                     String login_id_fk, String workshop_name, String workshop_address, String workshop_address2, String first_name,
                                     String email_id, String mobile_no, String alternate_mobile_no, String landline_no, String anniversary_date, String state,
                                     String district, String city, String sub_district, String dob, String pincode, String country, String landmark,
                                     String classification, String category, String type, String spouse_name, String no_of_children, String participant_gender,
                                     String residential_address, String residential_address2, String residential_landmark, String residential_pincode,
                                     String residential_city, String residential_state, String communication_consent, String lucky_draw_consent,
                                     String lucky_draw_a1, String lucky_draw_a2) {
        Log.e("URL : ", "URL : " + webservice_URL_UpdateReatilerProfile);

        JSONObject params = new JSONObject();


        try {
            // params with static values
       /*     params.put("fls_login_id", "1");
            params.put("participant_id", "1");
            params.put("retailer_code", "GRGRC1");
            params.put("login_id_fk", "6");
            params.put("workshop_name", "Grass Roots");
            params.put("workshop_address", "Lower parel");
            params.put("workshop_address2", "East");
            params.put("first_name", "Nirav");
            params.put("email_id", "nirav.shah@grg.com");
            params.put("mobile_no", "9324259919");
            params.put("alternate_mobile_no", "");
            params.put("landline_no", "");
            params.put("anniversary_date", "0000-00-00");
            params.put("state", "Maharshtra");
            params.put("district", "Mumbai");
            params.put("city", "Mumbai");
            params.put("sub_district", "Mumbai");
            params.put("dob", "0000-00-00");
            params.put("pincode", "400001");
            params.put("country", "India");
            params.put("landmark", "");
            params.put("classification", "Lube Oil Shop");
            params.put("category", "DEO");
            params.put("type", "Retail Outlet");
            params.put("spouse_name", "");
            params.put("participant_gender", "male");
            params.put("residential_address", "sdgsdfg");
            params.put("residential_address2", "sdgdfgsd");
            params.put("residential_landmark", "");
            params.put("residential_pincode", "400088");
            params.put("residential_city", "Mumbai");
            params.put("residential_state", "Maharashtra");
            params.put("communication_consent", "yes");
            params.put("lucky_draw_consent", "yes");
            params.put("lucky_draw_a1", "india");
            params.put("lucky_draw_a2", "india");

*/
            params.put("fls_login_id", "" + fls_login_id);
            params.put("participant_id", "" + participant_id);
            params.put("retailer_code", "" + retailer_code);
            params.put("login_id_fk", "" + login_id_fk);
            params.put("workshop_name", "" + workshop_name);
            params.put("workshop_address", "" + workshop_address);
            params.put("workshop_address2", "" + workshop_address2);
            params.put("first_name", "" + first_name);
            params.put("email_id", "" + email_id);
            params.put("mobile_no", "" + mobile_no);
            params.put("alternate_mobile_no", "" + alternate_mobile_no);
            params.put("landline_no", "" + landline_no);
            params.put("anniversary_date", "" + anniversary_date);
            params.put("state", "" + state);
            params.put("district", "" + district);
            params.put("city", "" + city);
            params.put("sub_district", "" + sub_district);
            params.put("dob", "" + dob);
            params.put("pincode", "" + pincode);
            params.put("country", "" + country);
            params.put("landmark", "" + landmark);
            params.put("classification", "" + classification);
            params.put("category", "" + category);
            params.put("type", "" + type);
            params.put("spouse_name", "" + spouse_name);
            params.put("no_of_children", "" + no_of_children);
            params.put("participant_gender", "" + participant_gender);
            params.put("residential_address", "" + residential_address);
            params.put("residential_address2", "" + residential_address2);
            params.put("residential_landmark", "" + residential_landmark);
            params.put("residential_pincode", "" + residential_pincode);
            params.put("residential_city", "" + residential_city);
            params.put("residential_state", "" + residential_state);
            params.put("communication_consent", "" + communication_consent);
            params.put("lucky_draw_consent", "" + lucky_draw_consent);
            params.put("lucky_draw_a1", "" + lucky_draw_a1);
            params.put("lucky_draw_a2", "" + lucky_draw_a2);
            //
            Log.e("", "JSONfor updateRetailer : " + params.toString());

            /* {
            "fls_login_id" : "1",   "participant_id": "1",  "retailer_code": "GRGRC1",  "login_id_fk": "6",
                     "workshop_name": "Grass Roots",     "workshop_address": "Lower parel",  "workshop_address2": "East",
                "first_name": "Nirav",    "email_id": "nirav.shah@grg.com",   "mobile_no": "9324259919",
                "alternate_mobile_no": "",  "landline_no": "", "anniversary_date": "0000-00-00", "state": "Maharshtra",
                "district": "Mumbai",  "city": "Mumbai",  "sub_district": "Mumbai",  "dob": "0000-00-00",
                "pincode": "400001",  "country": "India",      "landmark": "",    "classification": "Lube Oil Shop",
                "category": "DEO",   "type": "Retail Outlet",   "spouse_name": "", "no_of_children": "0",
                "participant_gender": "male",
                "residential_address": "sdgsdfg",
                "residential_address2": "sdgdfgsd",
                "residential_landmark": "",
                "residential_pincode": "400088",
                "residential_city": "Mumbai",
                "residential_state": "Maharashtra",
                "communication_consent": "yes",
                "lucky_draw_consent": "yes",
                "lucky_draw_a1": "india",
                "lucky_draw_a2": "india"
        }*/

        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_UpdateReatilerProfile, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject Whats_new_webservice(String loginID, String limit) {

        Log.e("web service : ", "web service : newOne : " + limit);

        JSONObject params = new JSONObject();

        try {

            params.put("limit", "" + limit);
            params.put("login_id", loginID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_Whats_new, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject Scheme_letter_webservice(String login_id) {

        Log.e("web service : ", " web service : Limit : " + login_id);

        JSONObject params = new JSONObject();

        try {
            params.put("login_id", login_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_SchemeLetterr, params);
        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject Scheme_letter_Detail_webservice(String schemeId) {

        Log.e("web service : ", " web service : scheme ID : " + schemeId);

        JSONObject params = new JSONObject();

        try {
            params.put("scheme_letter_id", "" + schemeId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_SchemeLetterDetail, params);

        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }

    public JSONObject Target_meter_webservice(String loginId, String campaign) {
        Log.e("web service : ", " web service : login ID : " + loginId + "\nCampaing : " + campaign);

        JSONObject params = new JSONObject();

        try {
            params.put("login_id", "" + loginId);
            params.put("campaign", "" + campaign);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_TargetMeter, params);

        System.out.println("Got result : " + json.toString());
        // return json
        return json;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean callReadPhoneStateAndExtStoragePermission(Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permission);
                }
            }
            Log.e("LIst Size permission : ", "" + listPermissionsNeeded.size());
            if (listPermissionsNeeded.size() > 0) {
                ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_READ_PHONE_STATE);
                Log.e("requestPermissions Call", "");
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    public static boolean checkReadPhoneStateAndExtStoragePermission(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    // Added By Almas 19 sep 17
    public JSONObject getcurrentbal() {
        JSONObject params = new JSONObject();

        try {

            SharedPreferences preferences2 = AppConfig.getInstance().getSharedPreferences(
                    "signupdetails", Context.MODE_PRIVATE);
            String loginId = preferences2.getString("usertrno", "");

            params.put("login_id", loginId);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_CurrentBalance, params);
        //System.out.println("Got result : " + json.toString());

        return json;

    }

    // Added By PRAVIN DHARAM 02 april 19
    public JSONObject getDigitOrderHistory(String participant_login_id) {
        JSONObject params = new JSONObject();

        try {
            params.put("participant_login_id", participant_login_id);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_DIGITAL_ORDER_HISTORY, params);
        //System.out.println("Got result : " + json.toString());

        return json;
    }

    // Added By PRAVIN DHARAM 29 MAY 19
    public JSONObject getKcCategoryOne(String vehicle_type) {
        JSONObject params = new JSONObject();

        try {
            params.put("vehicle_type", vehicle_type);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_KC_CATEGORY_ONE, params);
        //System.out.println("Got result : " + json.toString());

        return json;
    }

    // Added By PRAVIN DHARAM 29 MAY 19
    public JSONObject getKcCategoryTwo(String vehicle_type, String category1) {
        JSONObject params = new JSONObject();

        try {
            params.put("vehicle_type", vehicle_type);
            params.put("category1", category1);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_KC_CATEGORY_TWO, params);
        //System.out.println("Got result : " + json.toString());

        return json;
    }

    // Added By PRAVIN DHARAM 29 MAY 19
    public JSONObject getKcVehicleDetils(String vehicle_type, String category1, String category2) {
        JSONObject params = new JSONObject();

        try {
            params.put("vehicle_type", vehicle_type);
            params.put("category1", category1);
            params.put("category2", category2);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_KC_VEHICLE_DETAILS, params);
        //System.out.println("Got result : " + json.toString());

        return json;
    }

    //----------------------------------------------------------------------------------------------------------
    // Added By APARNA BADHAN 17 September 19
    public JSONObject getCampaignRewards(String participant_login_id) {
        JSONObject params = new JSONObject();

        try {
            params.put("participant_login_id", participant_login_id);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_CAMPAIGN_REWARDS, params);
        //System.out.println("Got result : " + json.toString());

        return json;
    }

    public JSONObject GetVoucherDetails(String participant_login_id, String voucher_id) {
        JSONObject params = new JSONObject();

        try {
            params.put("participant_login_id", participant_login_id);
            params.put("voucher_id", voucher_id);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_voucher_details, params);
        //System.out.println("Got result : " + json.toString());

        return json;
    }

    public JSONObject getRoyaltyBenefitHistory(String participant_login_id) {
        JSONObject params = new JSONObject();

        try {
            params.put("participant_login_id", participant_login_id);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_ROYALTY_BENEFIT, params);
        //System.out.println("Got result : " + json.toString());

        return json;
    }

    public JSONObject GetCampaignRewardsDetails(String participant_login_id, String order_detail_id) {
        JSONObject params = new JSONObject();

        try {
            params.put("participant_login_id", participant_login_id);
            params.put("order_detail_id", order_detail_id);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_CAMPAIGN_REWARDS_DETAILS, params);
        //System.out.println("Got result : " + json.toString());

        return json;
    }

    public JSONObject GetUploadedInvoiceStatus(String participant_login_id) {
        JSONObject params = new JSONObject();

        try {
            params.put("participant_login_id", participant_login_id);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_get_upoaded_invoice_status, params);
        //System.out.println("Got result : " + json.toString());

        return json;
    }

    public JSONObject GetSplashPopUpDetails(String login_id) {
        JSONObject params = new JSONObject();

        try {
            params.put("login_id", login_id);
        } catch (JSONException e) {
            Log.e("", "JSON errror : " + e.toString());
        }

        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_SPLASH_POP_UP, params);
        //System.out.println("Got result : " + json.toString());

        return json;
    }

    public JSONObject uploadTallyInvoice() {

        Log.e("WS", "Points Calculator Master web service" + webservice_URL_PointCalculatorMasters);

        JSONObject params = new JSONObject();


        JSONObject json = jsonParser.getJSONFromUrl(webservice_URL_upload_tally_invoice, params);


        //System.out.println("Got result : " + json.toString());

        Log.e("", "Login response" + json);
        // return json
        return json;
    }


}
