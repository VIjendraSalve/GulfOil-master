package com.taraba.gulfoilapp.royalty_user_view.change_password;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.taraba.gulfoilapp.AppConfig;
import com.taraba.gulfoilapp.FlsDashboardActivity;
import com.taraba.gulfoilapp.LoginActivity;
import com.taraba.gulfoilapp.MainDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONObject;

/**
 * Created by android on 12/18/15.
 * Modified by Mansi
 */
public class NewChangePasswordFragment extends Fragment {


    String mStringUserID, mStringPassword, mStringPasswordNew,
            mStringPasswordNewConform;
    private TextInputLayout tilOldPassword;
    private TextInputLayout tilNewPassword;
    private TextInputLayout tilConfirmNewPassword;
    private TextInputEditText edtOldPassword;
    private TextInputEditText edtNewPassword;
    private TextInputEditText edtConfirmNewPassword;
    private AppCompatButton btnSubmit;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutUserWise(), container, false);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_change_password));
        } else if (getActivity() instanceof FlsDashboardActivity) {
            ((FlsDashboardActivity) getActivity()).hideShowView(true);
            ((FlsDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_change_password));
        }
        tilOldPassword = view.findViewById(R.id.tilOldPassword);
        tilNewPassword = view.findViewById(R.id.tilNewPassword);
        tilConfirmNewPassword = view.findViewById(R.id.tilConfirmNewPassword);
        edtOldPassword = view.findViewById(R.id.edtOldPassword);
        edtNewPassword = view.findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = view.findViewById(R.id.edtConfirmNewPassword);
        btnSubmit = view.findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // hideKeyboard();
                validate();
            }
        });
       /* view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });*/
        return view;
    }

    private void validate() {
        tilOldPassword.setError(null);
        tilNewPassword.setError(null);
        tilConfirmNewPassword.setError(null);

        if (edtOldPassword.getText().toString().trim().equals("")) {
            tilOldPassword.setError("Please enter Old Password");
        } else if (edtNewPassword.getText().toString().trim().equals("")) {
            tilNewPassword.setError("Please enter New Password");
        } else if (edtConfirmNewPassword.getText().toString().trim()
                .equals("")) {
            tilConfirmNewPassword.setError("Please enter New Confirm Password");

        } else if (!edtNewPassword
                .getText()
                .toString()
                .trim()
                .equals(edtConfirmNewPassword.getText().toString().trim())) {
            tilConfirmNewPassword.setError("New Password & Confirm Password must be same..");

        } else {

            if (NetworkUtils.isNetworkAvailable(getActivity())) {

                SharedPreferences preferences = AppConfig.getInstance().getSharedPreferences(
                        "signupdetails", Context.MODE_PRIVATE);
                mStringUserID = preferences.getString("usertrno", "");

                mStringPassword = edtOldPassword.getText().toString();
                mStringPasswordNew = edtNewPassword.getText().toString();
                mStringPasswordNewConform = edtConfirmNewPassword.getText().toString();

                new LogedIn().execute(new String[]{mStringUserID,
                        mStringPassword,
                        mStringPasswordNew, mStringPasswordNewConform});
            } else {
                Toast.makeText(getActivity(),
                        "Internet Disconnected", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void alertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        getFragmentManager().popBackStack();

                        Intent mIntent = new Intent(getActivity(),
                                LoginActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mIntent.putExtra("action", "splash");
                        mIntent.putExtra("number", "");
                        mIntent.putExtra("pass", "");
                        startActivity(mIntent);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void alertDialog1(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void logout() {
        SharedPreferences preferences = getContext().getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("old_usertrno", "" + preferences.getString("usertrno", ""));
        //	Log.e("user id ", "User Id : "+""+preferences.getString("usertrno", ""));
        edit.putString("username", "");
        edit.putString("userimage", "");
        edit.putString("usertrno", "");
        edit.putString("status", "");
        //	edit.putString("logout", "Logout");
        edit.commit();
        //c.finish();
        getActivity().finish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_change_password_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_change_password_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_change_password_club;
        else
            return R.layout.fragment_change_password_fls;
    }

    class LogedIn extends AsyncTask<String[], Void, JSONObject> {
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
                Log.e("data : ", "Data : Username : " + params[0][0]
                        + "  ,  Password : " + params[0][1]
                        + "  ,  new Password : " + params[0][2]);
                jObj = new UserFunctions().ChangePass_webservice_call(""
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
                    Log.e("Json Data : ", "Json data : " + jObj);
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("success")) {
                        Log.e("In sucess  ", "In sucess  : " + " In sucess 1");

                        SharedPreferences preferences = AppConfig.getInstance().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("old_usertrno", "" + preferences.getString("usertrno", ""));
                        Log.e("user id ", "User Id : " + "" + preferences.getString("usertrno", ""));
                        edit.putString("uname", "");
                        edit.putString("userimage", "");
                        edit.putString("usertrno", "");
                        edit.putString("status", "");
                        edit.commit();

                        SharedPreferences preferences1 = AppConfig.getInstance().getSharedPreferences(
                                "userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit1 = preferences1.edit();
                        edit1.putString("old_usertrno", "" + preferences1.getString("usertrno", ""));
                        //	Log.e("user id ", "User Id : "+""+preferences.getString("usertrno", ""));
                        edit1.putString("username", "");
                        edit1.putString("userimage", "");
                        edit1.putString("usertrno", "");
                        edit1.putString("status", "");
                        //	edit.putString("logout", "Logout");
                        edit1.commit();

                        progressDialog.dismiss();

                        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle(getString(R.string.str_success))
                                .setDescription("" + jObj.getString("meaasge"))
                                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                                    dialog.dismiss();
                                    logout();
                                })
                                .show();

                    } else {
                        progressDialog.dismiss();

                        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle(getString(R.string.str_error))
                                .setDescription("" + jObj.getString("error"))
                                .setPosButtonText(getString(R.string.str_ok), null)
                                .show();

                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.e("Error : ", "Error : " + e.toString());
                }
            } else {
                progressDialog.dismiss();
            }
        } // end onpostecxe
    }
}
