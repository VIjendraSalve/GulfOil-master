package com.taraba.gulfoilapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by android1 on 12/17/15.
 */
public class SignUpActivity extends Activity {

    Spinner mSpinnerEmailid;
    EditText mEditTextName, mEditTextPhoneNumber, mEditTextPassword,
            mEditTextconfPassword, mEditTextTokenNo;
    String mStringEmailid, mStringName, mStringPhoneNo, mStringImeino,
            mStringPassword, mStringconfPassword, mStringTokenNo;
    Button btnSubmit;
    TextView termscondtion;
    CheckBox checkboxforview;
    Typeface face;
    String regId;

    int noOfAttemptsAllowed = 5; // Number of Retries allowed
    int noOfAttempts = 0; // Number of tries done
    boolean stopFetching = false;

    AsyncTask<Void, Void, Void> mRegisterTask;

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent mIntent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(mIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initComp();
        showemailslist();
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(SignUpActivity.this.TELEPHONY_SERVICE);
        mStringImeino = telephonyManager.getDeviceId();

        termscondtion.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

               /* Intent dilogopener = new Intent(SignUpActivity.this,
                        TermsConditionActivity.class);
                startActivity(dilogopener);*/

                Toast.makeText(SignUpActivity.this,
                        "click on Accept our Terms & Conditions.",
                        Toast.LENGTH_LONG).show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                mStringName = mEditTextName.getText().toString();
                mStringPhoneNo = mEditTextPhoneNumber.getText().toString();
                mStringPassword = mEditTextPassword.getText().toString();
                mStringconfPassword = mEditTextconfPassword.getText()
                        .toString();
                mStringTokenNo = mEditTextTokenNo.getText().toString();


                if (NetworkUtils.isNetworkAvailable(SignUpActivity.this)) {

                    if (mStringEmailid.equals("") || mStringName.equals("")
                            || mStringPhoneNo.equals("")
                            || mStringImeino.equals("")
                            || mStringPassword.equals("")
                            || mStringconfPassword.equals("")
                            || mStringTokenNo.equals("")) {
                  /*      alertDialog1("Mahindra Event",
                                "All fields are mandatory...!!!", "validate");*/

                        CustomOKDialog cd1 = new CustomOKDialog(SignUpActivity.this, "All fields are mandatory...!!!", "Mahindra Event", "validate");
                        cd1.show();
                    } else {
                        if (checkboxforview.isChecked()) {
                            if (mStringPassword.equals(mStringconfPassword)) {
                                /*
                                 * Log.e("values in on click submit: ",
                                 * "in on click submit Passed Values : " +
                                 * mStringName + " , " + mStringEmailid + "," +
                                 * mStringPhoneNo + " , " + mStringImeino + ","
                                 * + mStringPassword + " , " +
                                 * mStringconfPassword);
                                 */

                                SharedPreferences preferences = getSharedPreferences(
                                        "signupdetails", Context.MODE_PRIVATE);
                                Editor edit = preferences.edit();
                                edit.putString("name", mStringName);
                                edit.putString("uname", mStringEmailid);
                                edit.putString("phoneno", mStringPhoneNo);
                                edit.putString("password", mStringPassword);
                                edit.putString("imeino", mStringImeino);
                                edit.putString("token_no", mStringTokenNo);
                                edit.putString("login_reg", "1");
                                edit.commit();

                                parseRegistration();
                                //ValidateAndRegisterParse();
                                //ValidateAndRegisterGCM();
                            } else {
                              /*  alertDialog1("GrassRoot App",
                                        "Enter same password...!!!","validate");*/

                                CustomOKDialog cd1 = new CustomOKDialog(SignUpActivity.this, "Enter same password...!!!\",\"validate", "GrassRoot App", "validate");
                                cd1.show();
                            }
                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    "Accept our Terms & Conditions.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    public void ValidateAndRegisterParse(String mStringPaseObjectID) {

        if (NetworkUtils.isNetworkAvailable(SignUpActivity.this)) {
           /* edit1.putString("usertrno", jObj.getString("login_id"));
            Log.e("Login Id :", "Login Id : "+jObj.getString("login_id"));
            edit1.putString("user_type", jObj.getString("type"));*/

            //(stremailid, strimeino, strname, strPhoneNo, strPassword, regId, login_reg, mStringTokenNo)
            new SignUpUser().execute(new String[]{mStringEmailid, mStringImeino, mStringName, mStringPhoneNo, mStringPassword, mStringPaseObjectID, "1", mStringTokenNo});
        } else {
            Toast.makeText(SignUpActivity.this, "Internet Not Connected!!!", Toast.LENGTH_LONG).show();
        }
    }

    public void parseRegistration() {
        //Parse.initialize(this, "LnKlAdbGj06DCPTXHIlXOVvPmx93BYLY5Nyu655r", "eSYeBq8b3IgZkeavl5AWzOM5GlCHFN7CUFnEZQjv");

        //ParseInstallation.getCurrentInstallation().put("emailid", ""+mStringEmailid);
        //ParseInstallation.getCurrentInstallation().put("imeino", ""+mStringImeino);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSpinnerEmailid.getWindowToken(), 0);

        // ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
        //   public void done(ParseException e) {
        //     if (e == null) {
        Toast.makeText(getApplicationContext(), "Registred Successfully", Toast.LENGTH_SHORT).show();
        //Toast.makeText(SignUpActivity.this, "Installation ID : "+ParseInstallation.getCurrentInstallation().getInstallationId(), Toast.LENGTH_LONG).show();


        //    ValidateAndRegisterParse(ParseInstallation.getCurrentInstallation().getObjectId());
/*
                } else {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT);
                    toast.show();
                }
*/
    }
//        });


    class SignUpUser extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SignUpActivity.this);
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
                jObj = new UserFunctions().signUpUserWebService(params[0][0], params[0][1], params[0][2], params[0][3], params[0][4], params[0][5], params[0][6], params[0][7]);
            } catch (Exception e) {
                // TODO: handle exception
                SignUpActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SignUpActivity.this, "Network Error...",
                                Toast.LENGTH_LONG).show();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(final JSONObject jObj) {
            super.onPostExecute(jObj);
            // showToast("Calling set up views");
            progressDialog.dismiss();
            if (jObj != null) {
                try {
                    Log.e("Json Data : ", "Json data : " + jObj);

                    String mStringStatus = jObj.getString("success");
                    if (mStringStatus.equals("1")) {
                        String mStringResult = jObj.getString("result");

                        if (mStringResult.equals("duplicate")) {
                            SignUpActivity.this.runOnUiThread(new Runnable() {

                                public void run() {
                                    // TODO Auto-generated method stub
                                    //alertDialog1(getResources().getString(R.string.app_name), "You are already registered!!!" ,"message");
                                    CustomOKDialog cd = new CustomOKDialog(SignUpActivity.this, "You are already registered!!!", "Gulf Oil", "message");
                                    cd.show();
                                }
                            });
                        } else if (mStringResult.equals("duplicate_mobile")) {

                            SignUpActivity.this.runOnUiThread(new Runnable() {

                                public void run() {
                                    // TODO Auto-generated method stub
                                    try {
                                        // alertDialog1(getResources().getString(R.string.app_name), "You are already registered with "+jObj.getString("emailid"),"message");

                                        CustomOKDialog cd = new CustomOKDialog(SignUpActivity.this, "You are already registered with " + jObj.getString("emailid"), "Gulf Oil", "message");
                                        cd.show();
                                    } catch (NotFoundException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } else {
                            if (jObj.has("events")) /*{
                                DataBaseHelper mDatabaseHelperSigup;
                                mDatabaseHelperSigup = new DataBaseHelper(SignUpActivity.this);
                                ArrayList<String> mArrayListSyncTableData = new ArrayList<String>();
                                JSONArray result = jObj.getJSONArray("events");
                                if (result != null) {
                                    for (int j = 0; j < result.length(); j++) {

                                        JSONObject jEvent = result.getJSONObject(j);

                                        ArrayList<String> arr_string_keys = new ArrayList<String>();
                                        ArrayList<String> arr_string_values = new ArrayList<String>();

                                        String EventID ="";

                                        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                                        Iterator<?> it = jEvent.keys();
                                        while (it.hasNext()) {
                                            String key = (String) it.next();
                                            String value = (String) jEvent
                                                    .getString(key);
                                            arr_string_values.add(value);
                                            arr_string_keys.add(key);
                                            if(key.equals("trno"))
                                            {
                                                EventID = value;
                                            }
                                        }

                                        Log.e("values are : ", " Key : " + EventID);
                                        mDatabaseHelperSigup.setDynamicTableData(
                                                arr_string_values, arr_string_keys,
                                                DataBaseHelper.TABLE_EVENT_MASTER);

                                        ArrayList<String> mkey = new ArrayList<String>();
                                        ArrayList<String> mvalues = new ArrayList<String>();
                                        mkey.add(DataBaseHelper.COLUMN_EVENT_DOWNLOAD_STATUS); mvalues.add("1");
                                        mDatabaseHelperSigup.updateDynamicTableData(mvalues, mkey, DataBaseHelper.TABLE_EVENT_MASTER, DataBaseHelper.COLUMN_EVENT_TRNO + "='"+ EventID + "'");
                                    }
                                }
                            }*/
                                callActivity();
                        }

                    } else {

                    }
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        } // end onpostecxe
    }

    public void callActivity() {
        Intent mIntent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(mIntent);
        finish();
    }

    public void initComp() {

        mSpinnerEmailid = (Spinner) findViewById(R.id.edt_emailid);

        mEditTextName = (EditText) findViewById(R.id.edt_name);
        mEditTextPhoneNumber = (EditText) findViewById(R.id.edt_mobile);
        mEditTextPassword = (EditText) findViewById(R.id.edt_signup_password);
        mEditTextconfPassword = (EditText) findViewById(R.id.edt_signup_confirm_password);

        btnSubmit = (Button) findViewById(R.id.btn_signup);
        termscondtion = (TextView) findViewById(R.id.termscondtion);
        checkboxforview = (CheckBox) findViewById(R.id.checkboxforview);
    }

    public void showemailslist() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(SignUpActivity.this)
                .getAccounts();
        ArrayList<String> possibleEmails = new ArrayList<String>();

        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                if (account.name.contains("@mahindra.com")) {
                    possibleEmails.add(0, account.name);
                } else {
                    possibleEmails.add(account.name);
                }
            }
        }

        if (possibleEmails.size() == 0) {
            /*
             * mSpinnerEmailid.setVisibility(View.GONE);
             * mEditTextEmailid.setVisibility(View.VISIBLE);
             * if(mEditTextEmailid.getText().toString().equals("")) {
             * Toast.makeText(getApplicationContext(),
             * "Please Enter your email address", Toast.LENGTH_SHORT).show(); }
             * else { mStringEmailid = mEditTextEmailid.getText().toString(); }
             */
        } else {
            ArrayAdapter<String> emaillst = new ArrayAdapter<String>(this,
                    R.layout.email_layout, R.id.text_name, possibleEmails);

            mSpinnerEmailid.setAdapter(emaillst);

            mSpinnerEmailid
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                            // TODO Auto-generated method stub
                            mStringEmailid = arg0.getItemAtPosition(arg2)
                                    .toString();
                        }

                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    });
        }
    }

    public void alertDialog1(String title, String message, final String type) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                SignUpActivity.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if (type.equals("message")) {
                            callActivity();
                        } else if (type.equals("validate")) {

                        }
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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

        public CustomOKDialog(Context a, String msg, String heading, String type) {
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
                    dismiss();
                    if (type.equals("message")) {
                        callActivity();
                    } else if (type.equals("validate")) {

                    }
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }
}

