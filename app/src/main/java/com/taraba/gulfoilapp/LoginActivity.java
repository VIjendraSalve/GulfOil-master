package com.taraba.gulfoilapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.databinding.ActivityLoginBinding;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.util.Android_Permission;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.OrientationUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


/**
 * Created by android1 on 12/17/15.
 * Modified by Mansi
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {
    String mStringPhoneNo, mStringPassword, mStringImeino;
    String mStringAction, strPhoneNo, strPassword;
    private ProgressDialog mProgressDialog;
    String regId = "";
    Android_Permission permissions;
    private ActivityLoginBinding binding;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        permissions = new Android_Permission(this);
        getFCMToken();


        initSignUpActivityComponants();
        SharedPreferences preferences1 = getSharedPreferences("dialoStatus", Context.MODE_PRIVATE);
        String DialogStatus = preferences1.getString("statusDialog", "");
        if (DialogStatus.equals("yes"))
            show_dialog_you_are_not_updated();
        Log.e("", "Registration activity oncreate dialog status:" + DialogStatus);

        // set colors to buttons
       /* MyTrackConstants.setColors(_mButtonPhoneNumberSubmit, getResources()
                .getColor(R.color.btn_gradient_start_color), getResources()
                .getColor(R.color.btn_gradient_start_color));
*/
        // set listeners on buttons
        setListeners();
        Log.e("conta no :", "mStringAction no :" + mStringAction);
        Log.e("strPhoneNo no :", "strPhoneNo no :" + strPhoneNo);
        Log.e("strPhoneNo no :", "strPassword no :" + strPassword);

        try {
            if ( mStringAction == null) { //mStringAction.equals("") ||hide
            } else {
                if (mStringAction.equals("forgot")) {
                    Log.e("in forgot regis", "in forgot reg");
                    binding.edtUsername.setText(strPhoneNo);
                    binding.edtPassword.setText(strPassword);
                } else {
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void setListeners() {
        binding.btnLogin.setOnClickListener(this);
        binding.tvForgotPasword.setOnClickListener(this);
        binding.tvTermAndConditionLink.setOnClickListener(this);
        binding.footerView.tvFooterTollFreeNumber.setOnClickListener(this);
    }

    private boolean checkPlayServices() {
        return GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(this) != 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // checkPlayServices();
    }

    private void registerInBackground(final String emailID) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    /*if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(getApplication());
                    }
                    regId = gcmObj
                            .register(Constants.PUSH_APP_NO);*/
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    regId = prefs.getString("FCM_DeviceToken", "");

                    Log.d("FCM_DeviceToken", regId);

                    SharedPreferences preferences = getSharedPreferences("Notification", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("NotificationId", "" + regId);
                    editor.commit();

                    msg = "Registration ID :" + regId;

                } catch (Exception ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                try {
                    if (!TextUtils.isEmpty(regId)) {
                        storeRegIdinSharedPref(getApplication(), regId, emailID);
                       /* Toast.makeText(
                                getApplication(),
                                "Registered with GCM Server successfully.\n\n"
                                        + msg, Toast.LENGTH_SHORT).show();*/
                        // Log.e("Error", msg);
                        //sb = getNumber(getContentResolver());
                        //new DownloadJSON().execute();

                    } else {
                        Toast.makeText(
                                getApplication(),
                                "Either you haven't enabled Internet. Make sure you enabled Internet and try registering again after some time."
                                        + msg, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute(null, null, null);
    }

    private void storeRegIdinSharedPref(Context context, String regId,
                                        String emailID) {
        try {
            SharedPreferences prefs = getSharedPreferences("UserDetails",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("regId", regId);
//        editor.putString(EMAIL_ID, emailID);
            editor.commit();

            mProgressDialog.dismiss();
            //  new PushRegAsyncTask().execute();
            validate(regId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * PhoneNumberSubmitClickListener =>
     */


    public void RegisterUser() {
        try {
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.setMessage("Login Process...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            if (!TextUtils.isEmpty("demo")) {
                // if (checkPlayServices()) {
                registerInBackground("demo");
                // }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    /**
     * initSignUpActivityComponants
     * <p>
     * void
     * <p>
     * Created Date : Jul 16, 2015
     */

    public void initSignUpActivityComponants() {
        //Call to toll free number

//        _mButtonContactUs = (Button) findViewById(R.id.xbtn_contactus);


        //mTextViewLoginFormSignUp = (TextView) findViewById(R.id.txt_signup);
        /*  mTextViewGenuineCheck = (TextView) findViewById(R.id.txt_genuineCheck);*/
        mStringAction = getIntent().getStringExtra("action");
        strPhoneNo = getIntent().getStringExtra("number");
        strPassword = getIntent().getStringExtra("pass");


        /*findViewById(R.id.appDemobtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentappdemo=new Intent(RegistrationActivity.this,AppdemoFragment.class);
                startActivity(intentappdemo);
            }
        });*/

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.e("", "Permission callback called-------");
        switch (requestCode) {
            case UserFunctions.PERMISSION_READ_PHONE_STATE: {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Dexter.withContext(LoginActivity.this)
                            .withPermissions(
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.GET_ACCOUNTS,
                                    Manifest.permission.CALL_PHONE,
                                    Manifest.permission.READ_MEDIA_IMAGES,
                                    Manifest.permission.READ_MEDIA_AUDIO,
                                    Manifest.permission.READ_MEDIA_VIDEO,
                                    Manifest.permission.READ_PHONE_STATE
                            )
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        RegisterUser();
                                    }

                                    if (report.isAnyPermissionPermanentlyDenied()) {
                                        showSettingsDialog();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).check();
                }
                else {
                    Dexter.withContext(LoginActivity.this)
                            .withPermissions(
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_CONTACTS,
                                    Manifest.permission.GET_ACCOUNTS,
                                    Manifest.permission.CALL_PHONE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_PHONE_STATE
                            )
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted()) {
                                        RegisterUser();
                                    }

                                    if (report.isAnyPermissionPermanentlyDenied()) {
                                        showSettingsDialog();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).check();
                }

                /*Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(android.Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("sms & location services", " permission granted");
                        if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
                            //startActivity(new Intent(this,MainActivity.class));
                            // L.pd(getString(R.string.dialog_please_wait), this);
                            RegisterUser();

                        } else {
                            Toast.makeText(LoginActivity.this, "Internet Disconnected",
                                    Toast.LENGTH_LONG).show();

                        }
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.e(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE) || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            showDialogOK("Read Phone State and External Storage Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    UserFunctions.callReadPhoneStateAndExtStoragePermission(LoginActivity.this);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });

                        } else {
                            //permission is denied (and never ask again is  checked)
                            //shouldShowRequestPermissionRationale will return false
                            Log.e("", "Go to settings and enable permissions");
                            //proceed with logic by disabling the related features or quit the app.
                            showDialogOK("Read Phone State and External Storage Permission required for login please enable from setting",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                    intent.setData(uri);
                                                    startActivity(intent);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }

                    }
                } else {
                    Log.e("", "Permission NOT granted");
                }*/
            }
        }
    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                // .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
/*
    public void parseRegistration() {
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.e("Installation ID : ", "Installation ID : " + ParseInstallation.getCurrentInstallation().getInstallationId());
                    Log.e("Object ID : ", "Object ID : " + ParseInstallation.getCurrentInstallation().getObjectId());

                   // validate(ParseInstallation.getCurrentInstallation().getObjectId());
                    validate(regId);
                } else {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }*/

    private void validate(String mStringPaseObjectID) {
        binding.tilUsername.setError(null);
        binding.tilPassword.setError(null);

        if (binding.edtUsername.getText().toString().trim()
                .equals("")) {
            binding.tilUsername.setError("Enter username");
        } else if (binding.edtPassword.getText().toString().trim()
                .equals("")) {
            binding.tilPassword.setError("Enter password");
        } else if (!binding.cbtAcceptTermAndCondition.isChecked()) {
            Toast.makeText(this, "Select terms and condition", Toast.LENGTH_SHORT).show();
        } else {
            if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
                mStringPhoneNo = binding.edtUsername.getText().toString();
                mStringPassword = binding.edtPassword.getText().toString();

                new LogedIn().execute(new String[]{
                        binding.edtUsername.getText().toString(),
                        binding.edtPassword.getText().toString(),
                        mStringPaseObjectID,
                        binding.cbtAcceptTermAndCondition.isChecked() ? "yes" : "no"});
            } else {
                Toast.makeText(LoginActivity.this, "Internet Disconnected",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public void switchActivity(String strType, String retailer_type, String kyc_status) {
        Log.e(TAG, "switchActivity: Type: " + strType + " Retailer Type: " + retailer_type + "kyc_status "+ kyc_status);


            Intent mIntentGoToHome = null;
            if (strType.equals("fls")) {
                mIntentGoToHome = new Intent(LoginActivity.this,
                        FlsDashboardActivity.class);
                Log.e("Type ", "Type : " + strType);

                mIntentGoToHome.putExtra("action", strType);
                mIntentGoToHome.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mIntentGoToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntentGoToHome);
                finish();
            } else if(kyc_status.equals("true")) {
                mIntentGoToHome = new Intent(LoginActivity.this,
                        MainDashboardActivity.class);

                mIntentGoToHome.putExtra("action", strType);
                mIntentGoToHome.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mIntentGoToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mIntentGoToHome);
                finish();
            }else {
                Intent mIntentGoToHome1 = null;
                mIntentGoToHome1 = new Intent(LoginActivity.this,
                        CheckKYCActivity.class);
                startActivity(mIntentGoToHome1);
                finish();
            }



    }

    public void alertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                LoginActivity.this);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        binding.edtPassword.setText("");
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvForgotPasword:
                openForgotPasswordScreen();
                break;
            case R.id.btnLogin:
                //startActivity(new Intent(this, CheckKYCActivity.class));
                processLogin();
                break;
            case R.id.tvTermAndConditionLink:
                openTermsAndConditionsScreen();
                break;
            case R.id.tvFooterTollFreeNumber:
                GulfOilUtils.callTollFree(LoginActivity.this);
                break;
        }
    }

    private void openTermsAndConditionsScreen() {
        hideKeyboard();
        Intent mIntent = new Intent(LoginActivity.this, TermsAndConditionsActivity.class);
        startActivity(mIntent);
        finish();
    }


    private void openForgotPasswordScreen() {
        hideKeyboard();
        Intent mIntent = new Intent(LoginActivity.this, ForgotActivity.class);
        startActivity(mIntent);
        finish();
    }

    private void processLogin() {
        hideKeyboard();
        if (NetworkUtils.isNetworkAvailable(LoginActivity.this)) {
            if (UserFunctions.checkReadPhoneStateAndExtStoragePermission(LoginActivity.this)) {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(LoginActivity.this.TELEPHONY_SERVICE);
                mStringImeino = "123";//telephonyManager.getDeviceId();
                RegisterUser();
            } else {
                UserFunctions.callReadPhoneStateAndExtStoragePermission(LoginActivity.this);
            }

        } else {
            Toast.makeText(LoginActivity.this, "Internet Disconnected",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void show_dialog_you_are_not_updated() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

        alertDialogBuilder.setTitle(getString(R.string.you_are_not_updated_title));
        alertDialogBuilder.setMessage(getString(R.string.you_are_not_updated_message));
        alertDialogBuilder.setIcon(R.drawable.ic_action_collections_cloud_light);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                OrientationUtils.unlockOrientation(LoginActivity.this);
            }
        });
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences preferences = getSharedPreferences("dialoStatus", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("statusDialog", "no");
                editor.commit();
                Log.e("", "Registration activity dialog status:no");
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));

                dialog.cancel();
                OrientationUtils.unlockOrientation(LoginActivity.this);
            }
        });
        alertDialogBuilder.show();
    }

    @Override
    public void onBackPressed() {


        new GulfUnnatiDialog(this)
                .setTitle(getString(R.string.str_confirmation))
                .setDescription(getString(R.string.str_app_exit_msg))
                .setPosButtonText(getString(R.string.str_yes), dialog -> {
                    dialog.dismiss();
                    finishAffinity();
                })
                .setNegButtonText(getString(R.string.str_no), null)
                .show();

    }

    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    String msg = "FCM TOKEN: " + token;
                    Log.d(TAG, msg);

                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    // 0 - for private mode
                    SharedPreferences.Editor editor2 = pref.edit();
                    editor2.putString("FCM_DeviceToken", token);
                    editor2.apply();
                    //Toast.makeText(MainDashboardActivity.this, msg, Toast.LENGTH_SHORT).show();
                });
    }

    class LogedIn extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
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
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                regId = prefs.getString("FCM_DeviceToken", "");

                jObj = new UserFunctions().Login_webservice_call(""
                        + params[0][0], "" + params[0][1], "" + params[0][2], regId, "" + params[0][3]);

                Log.e("", "LoginResponse1:---" + jObj);
            } catch (Exception e) {
                // TODO: handle exception
                LoginActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LoginActivity.this, "Network Error...",
                                Toast.LENGTH_LONG).show();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);

            Log.e("Registration activity", "Registration activity loged in json:--------------------------------" + jObj);

            progressDialog.dismiss();
            if (jObj != null) {
                try {
                    if (jObj.has("error")) {

                        new GulfUnnatiDialog(LoginActivity.this)
                                .setTitle(getString(R.string.str_error))
                                .setDescription("" + jObj.get("error"))
                                .setPosButtonText(getString(R.string.str_ok), null)
                                .show();

                    } else {
                        if (jObj != null) {
                            Log.d("Result_Response1", jObj.getString("type").toString());
                            if (jObj.getString("type").equals("retailer")) {
                                SharedPreferences preferences1 = getSharedPreferences(
                                        "signupdetails", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit1 = preferences1.edit();
                                edit1.putString("password", mStringPassword);
                                edit1.putString("uname", mStringPhoneNo);
                                edit1.putString("imeino", mStringImeino);
                                edit1.putString("usertrno", jObj.getString("login_id"));
                                Log.e("Login Id :", "Login Id : " + jObj.getString("login_id"));
                                edit1.putString("user_type", jObj.getString("type"));
                                edit1.putString("login_reg", "2");

                                edit1.putString("username", jObj.getString("username"));
                                edit1.putString("fullname", jObj.getString("first_name") + " " + jObj.getString("last_name"));

                                edit1.putString("mobile", jObj.getString("mobile_no"));
                                edit1.putString("email", jObj.getString("email_id"));

                                edit1.putString("shopname", jObj.getString("workshop_name"));

                                edit1.putString("shopaddress1", jObj.getString("workshop_address"));
                                edit1.putString("shopaddress2", jObj.getString("workshop_address2"));
                                edit1.putString("shoplandmark", jObj.getString("landmark"));
                                edit1.putString("shoppincode", jObj.getString("pincode"));
                                edit1.putString("shopsubdistrict", jObj.getString("sub_district"));
                                edit1.putString("shopcity", jObj.getString("city"));
                                edit1.putString("shopdistrict", jObj.getString("district"));
                                edit1.putString("shopstate", jObj.getString("state"));
                                edit1.putString("alternatemob", jObj.getString("alternate_mobile_no"));
                                edit1.putString("shoplandline", jObj.getString("landline_no"));
                                edit1.putString("resadd1", jObj.getString("residential_address"));
                                edit1.putString("resadd2", jObj.getString("residential_address2"));
                                edit1.putString("reslandmark", jObj.getString("residential_landmark"));
                                edit1.putString("respincode", jObj.getString("residential_pincode"));
                                edit1.putString("rescity", jObj.getString("residential_city"));
                                edit1.putString("resstate", jObj.getString("residential_state"));
                                edit1.putString("dob", jObj.getString("dob"));
                                edit1.putString("anniversery", jObj.getString("anniversary_date"));
                                edit1.putString("spouse", jObj.getString("spouse_name"));
                                edit1.putString("gender", jObj.getString("participant_gender"));
                                edit1.putString("children", jObj.getString("no_of_children"));
                                edit1.putString("distributorid", jObj.getString("db_code"));
                                edit1.putString("distributorname", jObj.getString("db_name"));
                                if (jObj.has("retailer_type")) {
                                    edit1.putString("retailer_type", jObj.getString("retailer_type"));
                                } else {
                                    edit1.putString("retailer_type", "");
                                }
                                edit1.putString("kyc_status", jObj.getString("kyc_status"));
                                edit1.putString("tds_content", jObj.getString("tds_content"));
                                edit1.putString("alert_content", jObj.getString("alert_content"));
                                Log.d("Vijendra1", "onPostExecute: "+jObj.getString("kyc_status"));
                                edit1.commit();

                                SharedPreferences preferences = getSharedPreferences(
                                        "userinfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = preferences.edit();
                                edit.putString("status", "Loged In");
                                edit.commit();

                                final UserTableDatasource ctdUser = new UserTableDatasource(getApplicationContext());
                                try {
                                    ctdUser.open();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                ctdUser.insertIntoHomework(jObj, "");
                                ctdUser.close();

                                SharedPreferences preference10 = getSharedPreferences(
                                        "signupdetails", Context.MODE_PRIVATE);
                                String type = preference10.getString("user_type", "");
                                String retailer_type = preference10.getString("retailer_type", "");
                                String kyc_status = preference10.getString("kyc_status", "");
                                //switchActivity(jObj.getString("type"));
                                switchActivity(type, retailer_type, kyc_status);
                            } else {
                                Log.d("Result_Response2", jObj.getString("type").toString());
                                SharedPreferences preferences1 = getSharedPreferences(
                                        "signupdetails", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit1 = preferences1.edit();
                                edit1.putString("password", mStringPassword);
                                edit1.putString("uname", mStringPhoneNo);
                                edit1.putString("imeino", mStringImeino);
                                edit1.putString("usertrno", jObj.getString("login_id"));
                                Log.e("Login Id :", "Login Id : " + jObj.getString("login_id"));
                                edit1.putString("user_type", jObj.getString("type"));
                                edit1.putString("mgmt_user_id", jObj.getString("mgmt_user_id_pk"));
                                edit1.putString("login_reg", "2");

                                edit1.putString("username", jObj.getString("username"));

                                edit1.putString("fullname", jObj.getString("first_name") + " " + jObj.getString("last_name"));

                                edit1.putString("mobile", jObj.getString("mobile_no"));
                                edit1.putString("email", jObj.getString("email_id"));
                                if (jObj.has("retailer_type")) {
                                    edit1.putString("retailer_type", jObj.getString("retailer_type"));
                                    //edit1.putString("retailer_type", "Unnati Club");
                                } else {
                                    edit1.putString("retailer_type", "");
                                }

                                if(!jObj.getString("type").equals("fls")) {
                                    edit1.putString("kyc_status", jObj.getString("kyc_status"));
                                    edit1.putString("tds_content", jObj.getString("tds_content"));
                                    edit1.putString("alert_content", jObj.getString("alert_content"));
                                    Log.d("Vijendra1", "onPostExecute2: " + jObj.getString("kyc_status"));
                                }

                                edit1.commit();

                                SharedPreferences preferences = getSharedPreferences(
                                        "userinfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = preferences.edit();

                                edit.putString("status", "Loged In");
                                edit.commit();


                                final UserTableDatasource ctdUser = new UserTableDatasource(getApplicationContext());
                                try {
                                    ctdUser.open();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                ctdUser.insertIntoHomework(jObj, "");
                                ctdUser.close();
                                SharedPreferences preference10 = getSharedPreferences(
                                        "signupdetails", Context.MODE_PRIVATE);
                                String type = preference10.getString("user_type", "");
                                String retailer_type = preference10.getString("retailer_type", "");

                                if(!jObj.getString("type").equals("fls")) {
                                    String kyc_status = preference10.getString("kyc_status", "");
                                    switchActivity(type, retailer_type, kyc_status);
                                }else {
                                    switchActivity(type, retailer_type, "false");
                                }

                                //switchActivity(jObj.getString("type"));

                                // switchActivity(jObj.getString("type"));
                            }
                        }
                    }

                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    } // end onpostecxe

}