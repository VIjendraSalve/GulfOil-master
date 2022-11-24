package com.taraba.gulfoilapp;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONObject;

/**
 * Created by android on 12/18/15.
 * Modified by Mansi
 */
public class ChangePasswordFragment extends Fragment {

    EditText mEditTextPassword, mEditTextNewPassword,
            mEditTextNewConfirmPassword;
    Button mButtonLogin;
    String mStringUserID, mStringPassword, mStringPasswordNew,
            mStringPasswordNewConform;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.changepassword_fragment, container, false);
        mEditTextPassword = (EditText) view.findViewById(R.id.edt_password_changepass);
        mEditTextNewPassword = (EditText) view.findViewById(R.id.edt_passwordnew_changepass);
        mEditTextNewConfirmPassword = (EditText) view.findViewById(R.id.edt_passwordnewconform_changepass);
        mButtonLogin = (Button) view.findViewById(R.id.btn_submit);


        mButtonLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // hideKeyboard();
                validate();
            }
        });
        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });
        return view;
    }

    private void validate() {
        if (mEditTextPassword.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(),
                    "Please enter Old Password", Toast.LENGTH_SHORT).show();
        } else if (mEditTextNewPassword.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(),
                    "Please enter New Password", Toast.LENGTH_SHORT).show();
        } else if (mEditTextNewConfirmPassword.getText().toString().trim()
                .equals("")) {
            Toast.makeText(getActivity(),
                    "Please enter New Confirm Password", Toast.LENGTH_SHORT)
                    .show();
        } else if (!mEditTextNewPassword
                .getText()
                .toString()
                .trim()
                .equals(mEditTextNewConfirmPassword.getText().toString().trim())) {
            Toast.makeText(getActivity(),
                    "New Password & Confirm Password must be same..",
                    Toast.LENGTH_SHORT).show();
        } else {

            if (NetworkUtils.isNetworkAvailable(getActivity())) {

                SharedPreferences preferences = AppConfig.getInstance().getSharedPreferences(
                        "signupdetails", Context.MODE_PRIVATE);
                mStringUserID = preferences.getString("usertrno", "");

                mStringPassword = mEditTextPassword.getText().toString();
                mStringPasswordNew = mEditTextNewPassword.getText().toString();
                mStringPasswordNewConform = mEditTextNewConfirmPassword.getText().toString();

                new LogedIn().execute(new String[]{mStringUserID,
                        mStringPassword,
                        mStringPasswordNew, mStringPasswordNewConform});
            } else {
                Toast.makeText(getActivity(),
                        "Internet Disconnected", Toast.LENGTH_LONG).show();
            }
        }
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
                       /*alertDialog(
                               getResources().getString(R.string.app_name),
                               jObj.getString("meaasge"));*/

                        CustomOkDialog cdd = new CustomOkDialog(getActivity(), jObj.getString("meaasge"), "Gulf Oil");
                        cdd.show();

                    } else {
                        progressDialog.dismiss();
                        /*alertDialog1(
                                getResources().getString(R.string.app_name),
                                jObj.getString("error"));*/
                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), jObj.getString("error"), "Gulf Oil");
                        cdd.show();

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

    class CustomOkDialog extends Dialog implements
            android.view.View.OnClickListener {

        public Context c;
        public Dialog d;
        public Button ok;
        String msg, heading;
        TextView txtMsg, txtHeading;
        String type;
        FragmentManager fm;

        public CustomOkDialog(Context a, String msg, String heading) {
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
            dismiss();
        }
    }
}
