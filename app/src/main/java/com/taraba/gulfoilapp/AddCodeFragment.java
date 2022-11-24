package com.taraba.gulfoilapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.taraba.gulfoilapp.adapter.BarcodeHistoryModel;
import com.taraba.gulfoilapp.adapter.BarcodeListAdapter;
import com.taraba.gulfoilapp.barcode.CaptureActivity;
import com.taraba.gulfoilapp.barcode.HistoryManager;
import com.taraba.gulfoilapp.barcode.PreferencesActivity;
import com.taraba.gulfoilapp.contentproviders.Database;
import com.taraba.gulfoilapp.contentproviders.DatabaseHelper;
import com.taraba.gulfoilapp.contentproviders.UserModel;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by android3 on 12/29/15.
 * Modified by Mansi
 */
public class AddCodeFragment extends Fragment {

    Button btnAddcode, btnScancode, btnSubmitBarcode;
    //private static final String TAG = HistoryActivity.class.getSimpleName();

    //LinearLayout lin_mech_code_accu;
    //  TextView txt_mechanic_code;
    private HistoryManager historyManager;
    private BarcodeListAdapter adapter;
    private CharSequence originalTitle;
    private ArrayList<BarcodeHistoryModel> circularList;
    int strParticipnt_login_id;
    ListView lstHisty;
    ImageView imgDelete;
    //int position;
    int strid;
    String mStringParticipantCode;
    BarcodeHistoryModel barhm;

    TextView txt_paricipantid;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addcode_fragment, container, false);
        lstHisty = (ListView) view.findViewById(R.id.lstBarcodeHistry);
        btnAddcode = (Button) view.findViewById(R.id.btnAddBarcode);
        btnScancode = (Button) view.findViewById(R.id.btnScanBarcode);
        btnSubmitBarcode = (Button) view.findViewById(R.id.btnSubmitBarcode);
        circularList = new ArrayList<BarcodeHistoryModel>();
        txt_paricipantid = (TextView) view.findViewById(R.id.txt_paricipantid);
        // lin_mech_code_accu = (LinearLayout) view.findViewById(R.id.lin_mech_code_accu);
        //   txt_mechanic_code = (TextView) view.findViewById(R.id.txt_mechanic_code);

        Bundle bundle = this.getArguments();
        strParticipnt_login_id = bundle.getInt("participant_login_id");

        mStringParticipantCode = bundle.getString("participant_code");
        txt_paricipantid.setText("Membership ID :" + mStringParticipantCode);
        //--------------------------------
/*
        txt_mechanic_code.setVisibility(View.VISIBLE);
        txt_mechanic_code.setText("Participants Code : " + bundle.getString("participant_code"));
*/
        //------------------------

        SharedPreferences preferences = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);

        final String mStringlogin_id = preferences.getString("usertrno", "");

        Log.e("part id :", "Part id in AddCode------------------------------- : " + strParticipnt_login_id);

        btnAddcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (circularList.size() < 10) {
                    final Dialog mDialogShowImage = new Dialog(getActivity());
                    mDialogShowImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialogShowImage.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    mDialogShowImage.setContentView(R.layout.addcodelayout);

                    final EditText mBarcodeText;

                    Button btnAddBarcode, btnCancle;
                    mBarcodeText = (EditText) mDialogShowImage
                            .findViewById(R.id.comment_text);
                    btnAddBarcode = (Button) mDialogShowImage
                            .findViewById(R.id.Comment_send);
                    btnCancle = (Button) mDialogShowImage
                            .findViewById(R.id.comment_cancel);

                    btnAddBarcode.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub

                            if (mBarcodeText.getText().toString().equals("")) {
                                Toast.makeText(getActivity(), "Please Enter UID code", Toast.LENGTH_LONG).show();
                            } else {
                                // strComment = mBarcodeText.getText().toString();
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                if (!prefs.getBoolean(PreferencesActivity.KEY_REMEMBER_DUPLICATES, false)) {
                                    deletePrevious(mBarcodeText.getText().toString());
                                }
                                Log.e("part id :", "Part id in enter barcode handler : " + strParticipnt_login_id);
                                ContentValues values = new ContentValues();
                                values.put(Database.TEXT_COL, mBarcodeText.getText().toString());
                                values.put(Database.FORMAT_COL, "");
                                values.put(Database.DISPLAY_COL, mBarcodeText.getText().toString());
                                values.put(Database.TIMESTAMP_COL, System.currentTimeMillis());
                                values.put(Database.HISTRY_PARTICIPANT_ID_COL, strParticipnt_login_id);
                                values.put(Database.HISTRY_PUBLISH_TYPE_COL, "unpublish");

                                SQLiteOpenHelper helper = new DatabaseHelper(getActivity());
                                SQLiteDatabase db = null;
                                try {
                                    db = helper.getWritableDatabase();
                                    // Insert the new entry into the DB.
                                    db.insert(Database.HISTRY_TABLE, Database.TIMESTAMP_COL, values);
                                } finally {
                                    close(null, db);
                                }
                                mDialogShowImage.cancel();
                                reloadHistoryItems();
                            }
                        }
                    });
                    btnCancle.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            mDialogShowImage.cancel();
                        }
                    });

                    mDialogShowImage.show();
                } else {
                    /*alertDialog(getResources().getString(R.string.app_name), "Limit Exceed");*/

                    CustomOKDialog cod = new CustomOKDialog(getActivity(), "Limit Exceed", "Gulf Oil");
                    cod.show();
                }
            }


        });

        btnScancode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularList.size() < 10) {
                    Intent mIntentGoToHome = new Intent(getActivity(),
                            CaptureActivity.class);
                    mIntentGoToHome.putExtra("strparticipant_id", strParticipnt_login_id);
                    startActivity(mIntentGoToHome);
                } else {
                    //
                    // alertDialog(getResources().getString(R.string.app_name), "Limit Exceed");
                    CustomOKDialog cod = new CustomOKDialog(getActivity(), "Limit Exceed", "Gulf Oil");
                    cod.show();
                }
            }
        });

        btnSubmitBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkUtils.isNetworkAvailable(getActivity())) {

                    JSONObject mStringcodes = new JSONObject();
                    for (int j = 0; j < circularList.size(); j++) {
                        try {

                            mStringcodes.put(String.valueOf(j), circularList.get(j).getText());
                            final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
                            try {
                                ctdUser.open();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            ctdUser.updatePublishStattus(circularList.get(j).getId());
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    if (mStringcodes.length() <= 0) {
                       /* alertDialog(
                                getResources().getString(R.string.app_name),
                                "Please Scan Code");*/
                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "Please Scan Code", "Gulf Oil");
                        cdd.show();

                    } else {
                        Log.e(" BarcodeList", " Size : " + circularList.size() + " Codes :" + mStringcodes);
                        Log.e(" BarcodeList", " mStringlogin_id : " + mStringlogin_id);
                        Log.e(" BarcodeList", " strParticipnt_login_id : " + strParticipnt_login_id);

                        new UploadCodes().execute(new String[]{mStringlogin_id, String.valueOf(strParticipnt_login_id), mStringcodes.toString()});
                    }
                } else {
                    // alertDialog(getResources().getString(R.string.app_name), getResources().getString(R.string.internet_connection_error));
                    CustomOKDialog cod = new CustomOKDialog(getActivity(), getResources().getString(R.string.internet_connection_error), "Gulf Oil");
                    cod.show();
                }
            }
        });

        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
        try {
            ctdUser.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        circularList = ctdUser.getAllBarcode(String.valueOf(strParticipnt_login_id));
        Log.e("adapter circularList", "circularList :" + circularList + " Size : " + circularList.size());
        adapter = new BarcodeListAdapter(getActivity(), circularList);
        lstHisty.setAdapter(adapter);

        lstHisty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

               /* new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.app_name))
                        .setMessage(getString(R.string.delete_msg))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                BarcodeHistoryModel barhm = circularList.get(position);
                                ctdUser.removeBarCodeFromList(barhm.getId());
                                dialog.dismiss();
                                reloadHistoryItems();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();*/
                CustomYesNoDialog cd = new CustomYesNoDialog(getActivity(), position);
                cd.show();
                //ctdUser.close();

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadHistoryItems();
    }

    private void reloadHistoryItems() {
        adapter.clear();
        circularList = new ArrayList<BarcodeHistoryModel>();
        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
        try {
            ctdUser.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        circularList = ctdUser.getAllBarcode(String.valueOf(strParticipnt_login_id));
        Log.e("adapter circularList", "circularList :" + circularList);
        adapter = new BarcodeListAdapter(getActivity(), circularList);
        lstHisty.setAdapter(adapter);
    }

    class CustomYesNoDialog extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        int position;
        TextView tv_heading;

        public CustomYesNoDialog(Activity a, int position) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.position = position;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_logout_dialog);
            tv_heading = (TextView) findViewById(R.id.txt_msg);
            tv_heading.setText("Are you sure, You want to delete?");
            yes = (Button) findViewById(R.id.btn_Yes);
            no = (Button) findViewById(R.id.btn_No);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_Yes:
                    final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
                    try {
                        ctdUser.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    BarcodeHistoryModel barhm = circularList.get(position);
                    ctdUser.removeBarCodeFromList(barhm.getId());

                    ctdUser.close();
                    dismiss();
                    reloadHistoryItems();
                    break;
                case R.id.btn_No:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    class UploadCodes extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String[]... params) {
            JSONObject jObj = null;
            try {
                jObj = new UserFunctions().uploadCodes(params[0][0], params[0][1], params[0][2]);
            } catch (Exception e) {
                // TODO: handle exception
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Network Error...",
                                Toast.LENGTH_LONG).show();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            // showToast("Calling set up views");
            progressDialog.dismiss();
            if (jObj != null) {

                Log.e("Json Data : ", "Json data : " + jObj);
                try {
                    ArrayList<String> CatSuccess = new ArrayList<String>();
                    ArrayList<String> CatFailed = new ArrayList<String>();
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("success")) {

                        String strSuccessCode = "", strFailedCode = "";
                        Object json_successtoken = new JSONTokener(jObj.getString("succ_codes")).nextValue();//jObj.get("succ_codes");
                        // JSONArray jObjSuccessCode = jObj.getJSONArray("succ_codes");
                        if (json_successtoken instanceof JSONArray) {

                            JSONArray jarrSuccessCode = jObj.getJSONArray("succ_codes");
                            if (jarrSuccessCode.length() <= 0) {
                                //   strSuccessCode = "Valid Codes not available";
                            } else {
                                for (int p = 0; p < jarrSuccessCode.length(); p++) {
                                    strSuccessCode = strSuccessCode + "" + jarrSuccessCode.get(p) + "<br/>";
                                }
                            }
                        } else {
                            JSONObject jObjSuccessCode1 = jObj.getJSONObject("succ_codes");
                            Iterator<String> iterator = jObjSuccessCode1.keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                Log.e("TAG", "key : " + key + "--Value :: " + jObjSuccessCode1.optString(key));
                                CatSuccess.add(jObjSuccessCode1.optString(key));
                            }
                            // Converting ArrayList to String in Java using advanced for-each loop
                            StringBuilder sb = new StringBuilder();
                            for (String str : CatSuccess) {
                                sb.append(str).append("<br/>"); //separating contents using semi colon
                            }
                            strSuccessCode = sb.toString();
                            //strSuccessCode = "Valid Codes not available";
                        }
                        //-------------------------------------------------------------------------------------------------------------
                        Object json_failtoken = new JSONTokener(jObj.getString("failed_codes")).nextValue();
                        //Object json_failtoken = jObj.getString("failed_codes");
                        // JSONObject jObjFailedCode = jObj.getJSONObject("failed_codes");
                        if (json_failtoken instanceof JSONObject) {
                            JSONObject jObjFailedCode = jObj.getJSONObject("failed_codes");
                            Iterator<String> iterator = jObjFailedCode.keys();
                            while (iterator.hasNext()) {

                                String key = iterator.next();
                                Log.e("TAG", "key : " + key + "--Value :: " + jObjFailedCode.optString(key));

                                CatFailed.add(jObjFailedCode.optString(key));
                            }
                            // Converting ArrayList to String in Java using advanced for-each loop
                            StringBuilder sb = new StringBuilder();
                            for (String str : CatFailed) {
                                sb.append(str).append("<br/>"); //separating contents using semi colon
                            }
                            strFailedCode = sb.toString();
                        } else {
                            JSONArray jarrFailCode = jObj.getJSONArray("failed_codes");
                            if (jarrFailCode.length() <= 0) {
                                //  strFailedCode = "Valid Codes not available";
                            } else {
                                for (int p = 0; p < jarrFailCode.length(); p++) {
                                    strFailedCode = strFailedCode + "" + jarrFailCode.get(p) + "<br/>";
                                }
                            }
                        }
//-------------------------------------------------------------------------------------------------------------
                        String dialog_data = "";
                        if (strSuccessCode.equals("")) {

                        } else {
                            dialog_data = "<b>Success Code :</b> <br/>" + strSuccessCode + " <br/> ";
                        }
                        if (strFailedCode.equals("")) {

                        } else {
                            dialog_data = dialog_data + "<b>Failed Code :</b> <br/>" + strFailedCode;
                        }
                       /* alertDialog(
                                getResources().getString(R.string.app_name),
                                dialog_data);*/

                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + Html.fromHtml(dialog_data), "Gulf Oil");
                        cdd.show();
                    } else {

                    }
                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }

    class SearchMechanic extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String[]... params) {
            JSONObject jObj = null;
            try {
                Log.e("data : ", " Mobile No : " + params[0][0]);
                jObj = new UserFunctions().GetParticipant(""
                        + params[0][0], "" + params[0][1], "" + params[0][2], "" + params[0][3]);
            } catch (Exception e) {
                // TODO: handle exception
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(),
                                "Network Error...", Toast.LENGTH_LONG).show();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            // showToast("Calling set up views");

            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {

                      /*  alertDialog(
                                getResources().getString(R.string.app_name),
                                "" + jObj.getString("error"));*/

                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), jObj.getString("error"), "Gulf Oil");
                        cdd.show();


                        progressDialog.dismiss();
                    } else if (mStringStatus.equals("success")) {

                        JSONArray resultJArray = jObj.getJSONArray("particpant_data");
                        Log.e("particpant_data : ", "particpant_data : " + resultJArray);

                        JSONObject result = resultJArray.getJSONObject(0);
                        Log.e("result : ", "result : " + result);

                        UserModel _news = new UserModel();
                        strid = Integer.parseInt(result.getString("login_id_pk"));
                        _news.setId(Integer.parseInt(result.getString("login_id_pk")));

                        _news.setUserfname(result.getString("first_name"));
                        _news.setUserlname(result.getString("last_name"));
                        _news.setGender(result.getString("gender"));
                        _news.setNomini(result.getString("nominee_name"));
                        _news.setNominirely(result.getString("nominee_relation"));
                        _news.setMothername(result.getString("mothers_maiden_name"));
                        if (result.has("image_path")) {
                            _news.setPicture(result.getString("image_path"));
                        } else {
                            _news.setPicture("");
                        }
                        _news.setWorkshopname(result.getString("workshop_name"));

                        _news.setState(result.getString("state"));
                        _news.setDistrict(result.getString("district"));
                        _news.setPincode(result.getString("pincode"));
                        _news.setTaluka(result.getString("taluka"));
                        _news.setCity(result.getString("city"));
                        _news.setShopadd(result.getString("workshop_address"));
                        _news.setLandmark(result.getString("landmark"));
                        _news.setMobile_no(result.getString("mobile_no"));

                        _news.setEmail(result.getString("email_id"));
                        _news.setSector(result.getString("sector"));
                        _news.setSpecialization(result.getString("specialization"));
                        _news.setDob(result.getString("dob"));
                        _news.setRegdate(result.getString("record_date"));
                        _news.setToatalsperconpermonth(result.getString("total_consumption"));
                        _news.setSperpartconformmvehicpermonth(result.getString("spare_consumption"));
                        _news.setMmgenuspareconpermonth(result.getString("genuine_consumption"));
                        _news.setTotalvehicalpermonth(result.getString("total_vehicles"));

                        _news.setMahindravehicalpermonth(result.getString("client_vehicles"));
                        _news.setNoofmechanics(result.getString("no_of_mechanics"));
                        _news.setBalance_points(result.getString("points"));
                        _news.setStatus(result.getString("status"));

                        _news.setParticipant_code(result.getString("participant_code"));
                        _news.setParticipant_id_pk(result.getString("participant_id_pk"));
                        _news.setForm_fillup_date(result.getString("form_fillup_date"));

                        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
                        try {
                            ctdUser.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (jObj.has("participant_claim_history")) {
                            ctdUser.deleteFromClaimHistory(result.getString("login_id_pk"));
                            ctdUser.insertIntoHomework(result, jObj.getJSONArray("participant_claim_history").toString());
                            ctdUser.insertBulkClaimHistry(jObj.getJSONArray("participant_claim_history"), getActivity(), result.getString("login_id_pk"));

                            ctdUser.close();
                        } else {
                            ctdUser.insertIntoHomework(result, "");
                            ctdUser.close();
                        }
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            } // end onpostecxe
        }
    }

    class CustomOKDialog extends Dialog implements
            android.view.View.OnClickListener {

        public Context c;
        public Dialog d;
        public Button ok;
        String msg, heading;
        TextView txtMsg, txtHeading;
        String type;
        FragmentManager fm;

        public CustomOKDialog(Context a, String msg, String heading/*,String type,FragmentManager fm*/) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.msg = msg;
            this.heading = heading;
            this.type = type;
            this.fm = fm;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog_login_invalid);
            ok = (Button) findViewById(R.id.btn_Ok);
            txtMsg = (TextView) findViewById(R.id.txtMsg);
            txtHeading = (TextView) findViewById(R.id.txtHeading);
            txtMsg.setText("" + msg);
            txtHeading.setText("" + heading);
            ok.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_Ok:
                    reloadHistoryItems();
                    dismiss();


                    if (NetworkUtils.isNetworkAvailable(getActivity())) {
                        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                                "signupdetails", getActivity().MODE_PRIVATE);
                        String loginId = preferences1.getString("usertrno", "");
                        String user_type = preferences1.getString("user_type", "");

                        new SearchMechanic().execute(new String[]{mStringParticipantCode, "Search By Membership ID", user_type, loginId});
                    } else {
                        Toast.makeText(getActivity(),
                                "Internet Disconnected", Toast.LENGTH_LONG).show();
                    }
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }


    public void alertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(Html.fromHtml("" + message)).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        reloadHistoryItems();
                        dialog.dismiss();


                        if (NetworkUtils.isNetworkAvailable(getActivity())) {
                            SharedPreferences preferences1 = getActivity().getSharedPreferences(
                                    "signupdetails", getActivity().MODE_PRIVATE);
                            String loginId = preferences1.getString("usertrno", "");
                            String user_type = preferences1.getString("user_type", "");

                            new SearchMechanic().execute(new String[]{mStringParticipantCode, "Search By Membership ID", user_type, loginId});
                        } else {
                            Toast.makeText(getActivity(),
                                    "Internet Disconnected", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deletePrevious(String text) {
        SQLiteOpenHelper helper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.delete(Database.HISTRY_TABLE, Database.TEXT_COL + "=?", new String[]{text});
        } finally {
            close(null, db);
        }
    }

    private static void close(Cursor cursor, SQLiteDatabase database) {
        if (cursor != null) {
            cursor.close();
        }
        if (database != null) {
            database.close();
        }
    }

    public static AddCodeFragment newInstance() {

        return (new AddCodeFragment());

    }

}