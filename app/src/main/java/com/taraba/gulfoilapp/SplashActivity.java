package com.taraba.gulfoilapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.AppVersionRequest;
import com.taraba.gulfoilapp.model.AppVersionResponse;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGHT = 2000;
    private String currentVersion;
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppConfig.isSplashPopUpSessionActive = true;
        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.e("", "Current version:" + currentVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // exportDB();

        //loadLogIn();
        if (!Utility.checkAllPermission(this)) {
            Utility.callAllPermission(this);
        } else {
            getAppVersionDetails();
        }

    }


    void loadLogIn() {

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    SharedPreferences preferences = getSharedPreferences(
                            "userinfo", Context.MODE_PRIVATE);

                    String mStringUsername = preferences.getString("username", "");
                    String mStringStatus = preferences.getString("status", "");

                    if (mStringStatus.equals("")) {
                        Intent mIntent = new Intent(SplashActivity.this,
                                LoginActivity.class);
                        mIntent.putExtra("action", "splash");
                        mIntent.putExtra("number", "");
                        mIntent.putExtra("pass", "");
                        startActivity(mIntent);
                        finish();
                    } else {
                        SharedPreferences preferences1 = getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        String mStringtype = preferences1.getString("user_type", "");
                        String retailer_type = preferences1.getString("retailer_type", "");

                        String kyc_status = preferences1.getString("kyc_status", "");
                        Log.d(TAG, "kyc_status23: "+kyc_status);

                        Intent mIntent = null;
                        if (mStringtype.equals("fls")) {
                            mIntent = new Intent(SplashActivity.this,
                                    FlsDashboardActivity.class);
                            mIntent.putExtra("action", mStringtype);
                            startActivity(mIntent);
                            finish();
                        } else  if(kyc_status.equals("true")) {
                            mIntent = new Intent(SplashActivity.this,
                                    MainDashboardActivity.class);
                            mIntent.putExtra("action", mStringtype);
                            startActivity(mIntent);
                            finish();
                        } else {
                            Intent mIntentGoToHome;
                            mIntentGoToHome = new Intent(SplashActivity.this,
                                    LoginActivity.class);
                            startActivity(mIntentGoToHome);
                            finish();
                        }


                    }
                }
            }, SPLASH_DISPLAY_LENGHT);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: Permission callback called");
        /*switch (requestCode) {
            case Utility.ALL_PERMISSION_REQUEST_CODE: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Toast.makeText(this, "Android 13", Toast.LENGTH_SHORT).show();
                    Map<String, Integer> perms = new HashMap<>();
                    // Initialize the map with both permissions
                    perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.READ_MEDIA_IMAGES, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.READ_MEDIA_AUDIO, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.READ_MEDIA_VIDEO, PackageManager.PERMISSION_GRANTED);
                    // Fill with actual results from user
                    if (grantResults.length > 0) {
                        for (int i = 0; i < permissions.length; i++)
                            perms.put(permissions[i], grantResults[i]);
                        // Check for both permissions

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.GET_ACCOUNTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_AUDIO)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_VIDEO)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                        ) {
                            Log.d(TAG, "onRequestPermissionsResult: all permission granted");
                            getAppVersionDetails();
                            // process the normal flow
                            //else any one or both the permissions are not granted
                        } else {
                            Log.d(TAG, "onRequestPermissionsResult: Some permissions are not granted ask again");
                            //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                            //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.

                            if (perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                    perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                    perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                                    perms.get(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED &&
                                    perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                                    perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                                showDialogOK("Permissions are required for this app",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        Utility.callAllPermission(SplashActivity.this);
                                                        break;
                                                    case DialogInterface.BUTTON_NEGATIVE:
                                                        // proceed with logic by disabling the related features or quit the app.
                                                        break;
                                                }
                                            }
                                        });
                                Toast.makeText(this, "Android 13 End", Toast.LENGTH_SHORT).show();
                            } else {
                                //permission is denied (and never ask again is  checked)
                                //shouldShowRequestPermissionRationale will return false
                                Log.d(TAG, "onRequestPermissionsResult: Go to settings and enable permissions");
                                //proceed with logic by disabling the related features or quit the app.
                                showDialogOK("Permission required, please enable from setting",
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
                        Log.d(TAG, "onRequestPermissionsResult: Permission NOT granted");
                    }
                }else {
                    //else lower version
                    Toast.makeText(this, "Android 11", Toast.LENGTH_SHORT).show();
                    Map<String, Integer> perms = new HashMap<>();
                    // Initialize the map with both permissions
                    perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                    perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                    // Fill with actual results from user
                    if (grantResults.length > 0) {
                        for (int i = 0; i < permissions.length; i++)
                            perms.put(permissions[i], grantResults[i]);
                        // Check for both permissions

                        if (perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                                perms.get(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED &&
                                perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
                                perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                                perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                                perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                            Log.d(TAG, "onRequestPermissionsResult: all permission granted");
                            getAppVersionDetails();
                            // process the normal flow
                            //else any one or both the permissions are not granted
                        } else {
                            Log.d(TAG, "onRequestPermissionsResult: Some permissions are not granted ask again");
                            //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                            //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.

                            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.GET_ACCOUNTS)
                                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
                                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                            ) {

                                showDialogOK("Permissions are required for this app",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which) {
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        Utility.callAllPermission(SplashActivity.this);
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
                                Log.d(TAG, "onRequestPermissionsResult: Go to settings and enable permissions");
                                //proceed with logic by disabling the related features or quit the app.
                                showDialogOK("Permission required, please enable from setting",
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
                        Log.d(TAG, "onRequestPermissionsResult: Permission NOT granted");
                    }
                }
            }
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(SplashActivity.this)
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
                                getAppVersionDetails();
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
            Dexter.withContext(SplashActivity.this)
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
                                getAppVersionDetails();
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

    }

    private void showSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(SplashActivity.this);
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
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                // .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private void getAppVersionDetails() {
        if (ConnectionDetector.isNetworkAvailable(this)) {

            ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getAppVersionDetails(new AppVersionRequest())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::appVersionAPIResponse, this::appVersionAPIError);

        } else {
            Toast.makeText(this, getString(R.string.str_internet_disconnected), Toast.LENGTH_LONG).show();
        }
    }

    private void appVersionAPIError(Throwable throwable) {
        loadLogIn();
    }

    private void appVersionAPIResponse(AppVersionResponse response) {
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            try {
                int appVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
                int playStoreVersion = Integer.parseInt(response.getData().getVersion_code());
                if (playStoreVersion > appVersionCode) {
                    showAppUpdateDialog(response.getData().getLink(), response.getData().isIs_major_update());
                } else {
                    loadLogIn();
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                loadLogIn();
            }
        } else {
            loadLogIn();
        }
    }

    private void showAppUpdateDialog(String url, boolean is_major_update) {
        GulfUnnatiDialog dialog = new GulfUnnatiDialog(this)
                .hideDialogCloseButton(true)
                .setTitle("App Update")
                .setDescription("New version is available on play store.")
                .setPosButtonText(getString(R.string.str_update), new GulfUnnatiDialog.OnPositiveClickListener() {
                    @Override
                    public void onClick(GulfUnnatiDialog dialog) {
                        finish();
                        openURL(url);
                    }
                });
        if (!is_major_update) {
            dialog.setNegButtonText(getString(R.string.str_no_thanks), new GulfUnnatiDialog.OnNegetiveClickListener() {
                @Override
                public void onClick(GulfUnnatiDialog dialog) {
                    dialog.dismiss();
                    loadLogIn();
                }
            });
        }

        dialog.show();
    }

    public void openURL(String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request. Please install a web browser or check your URL.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


}