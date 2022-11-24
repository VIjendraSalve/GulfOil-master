package com.taraba.gulfoilapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.taraba.gulfoilapp.async.ResendOTPTask;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by android3 on 1/29/16.
 * Modified by Mansi
 */

public class Otp_fragment extends Fragment {

    Button btnSubmitOtp, btn_back;
    EditText edtOtp;
    String strOtp;
    String mStringtype;
    String mStringStatus;
    TextView txt_desc;
    private boolean fromUpdateRetailerProfile;
    private String strParticipnt_login_idV2;
    private LinearLayout ll_v2_resend_otp;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_proceed_activity, container, false);

        initComp(view);
        Bundle bundle = this.getArguments();
        final int strParticipnt_login_id = bundle.getInt("participant_login_id");
        strParticipnt_login_idV2 = String.valueOf(strParticipnt_login_id);
        final String strMobNo = bundle.getString("participant_mob_no");
        fromUpdateRetailerProfile = bundle.getBoolean("fromUpdateRetailerProfile");
        //  String pcode=bundle.getString("participant_code");
        String pcode = bundle.getString("participant_code");
        //pcode = ""+strParticipnt_login_id;
        SharedPreferences preferences = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);

        final String mStringlogin_id = preferences.getString("usertrno", "");
        mStringtype = preferences.getString("user_type", "");

        SharedPreferences preferences1 = getActivity().getSharedPreferences("status", Context.MODE_PRIVATE);

        mStringStatus = preferences1.getString("status", "");

        if (mStringStatus.equals("") || mStringStatus == null) {
            txt_desc.setVisibility(View.VISIBLE);
            txt_desc.setTextColor(Color.parseColor("#2e7d32"));
            txt_desc.setText("The application is submitted successfully. Please note this " + pcode + " as membership ID. You have to write that id in the physical form in the field: Form number. Please note this number for your record. OTP has been sent to the user.Please contact the user and enter the OTP below to complete the registration");
        } else if (mStringStatus.equals("profile")) {
            txt_desc.setVisibility(View.GONE);
        }

        if (fromUpdateRetailerProfile) {
            txt_desc.setVisibility(View.GONE); //DEFAULT VISIBLE
            ll_v2_resend_otp.setVisibility(View.VISIBLE); //DEFAULT GONE
        }

        btnSubmitOtp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // hideKeyboard();

                if (NetworkUtils.isNetworkAvailable(getActivity())) {

                    validate(mStringlogin_id, String.valueOf(strParticipnt_login_id), strMobNo);
                }
            }
        });

        ll_v2_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkUtils.isNetworkAvailable(getActivity())) {

                    ResendOTPTask resendOTPTask = new ResendOTPTask(getActivity());
                    resendOTPTask.setCallback(new ResendOTPTask.ResendOTPCallback() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            try {
                                com.taraba.gulfoilapp.customdialogs.CustomOKDialog cdd = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "" + jsonObject.getString("message"), "Gulf Oil");
                                cdd.show();
                                /*cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        getFragmentManager().popBackStack();
                                    }
                                });*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    resendOTPTask.execute(new String[]{strParticipnt_login_idV2});
                }
            }
        });


       /* btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }

                *//*Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra("isOtp", "otp");
                Log.e("", "isOTP:");
                i.putExtra("action", "otp");
                startActivity(i);
                getActivity().finish();*//*
            }
        });
*/

       /* btnCancleOtp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
              *//*  Intent mIntentGoToHome = new Intent(getActivity(),
                        MainActivity.class);
                mIntentGoToHome.putExtra("action", "" + mStringtype);
                startActivity(mIntentGoToHome);
*//*
                //getActivity().getFragmentManager().popBackStack();
                getActivity().getFragmentManager().beginTransaction().remove(Otp_fragment.this).commit();
                getActivity().getFragmentManager().popBackStack();
            }
        });*/

        return view;
    }

    private void varifyOTPNewRetailerUpdateProfile() {
        Toast.makeText(getActivity(), "Test Successfully", Toast.LENGTH_SHORT).show();
    }

    public void initComp(View view) {
        btnSubmitOtp = (Button) view.findViewById(R.id.btn_submit);
        // btnCancleOtp = (Button) view.findViewById(R.id.btn_cancle_otp);
        btn_back = (Button) view.findViewById(R.id.btn_back);
        edtOtp = (EditText) view.findViewById(R.id.edt_otp);
        txt_desc = (TextView) view.findViewById(R.id.txt_desc);
        ll_v2_resend_otp = (LinearLayout) view.findViewById(R.id.ll_v2_resend_otp);
        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });
    }

    private void validate(String strLoginid, String strParticipantid, String strMobno) {
        String otp = "";
        otp = edtOtp.getText().toString();
        if (otp.trim().equals("") || otp.trim() == null) {
               /* Toast.makeText(getActivity(),
                        "Please enter OTP", Toast.LENGTH_SHORT).show();*/
            Log.e("", "In if----------------");
            CustomOKDialog cdd = new CustomOKDialog(getActivity(), "Please Enter OTP", "OTP Verification", "error");
            cdd.show();
        } else {
            Log.e("", "In else----------------");

            if (NetworkUtils.isNetworkAvailable(getActivity())) {
                strOtp = edtOtp.getText().toString();
                if (fromUpdateRetailerProfile) {
                    new VerifyOTPV2().execute();
                } else {
                    new LogedIn().execute(new String[]{
                            strLoginid, strParticipantid, strMobno, edtOtp.getText().toString()});
                }
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
                Log.e("data : ", "Data : Username : " + params[0][0] + " : " + params[0][1] + " : " + params[0][2] + " : " + params[0][3]
                );
                jObj = new UserFunctions().send_otp(""
                        + params[0][0], params[0][1], params[0][2], params[0][3]);
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
                    progressDialog.dismiss();
                    Log.e("Json Data : ", "Json data : " + jObj);
                  /*  //String mStringStatus = jObj.getString("success");
                    if (jObj.has("error")) {
                        Toast.makeText(getActivity(), "Id & Mobile Number Required.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (jObj.has("message")) {
                           *//* alertDialog(
                                    getResources().getString(R.string.app_name),
                                    jObj.getString("message"), "message");*//*
                            CustomOKDialog cdd=new CustomOKDialog(getActivity(),jObj.getString("message"),"OTP Verification","message");
                            cdd.show();
                        } else {
                            *//*alertDialog(
                                    getResources().getString(R.string.app_name),
                                    jObj.getString("error_message"), "error");*//*
                            CustomOKDialog cdd=new CustomOKDialog(getActivity(),jObj.getString("error_message"),"OTP Verification","error");
                            cdd.show();
                        }*/

                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {

                       /* alertDialog1(
                                getResources().getString(R.string.app_name),
                                "" + jObj.getString("error"));*/

                        com.taraba.gulfoilapp.customdialogs.CustomOKDialog cdd = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "" + jObj.getString("error_message"), "Gulf Oil");
                        cdd.show();

                        progressDialog.dismiss();
                    } else if (mStringStatus.equals("success")) {
                        Log.e("", "Resopse in send otp fragment" + jObj);
                        String msg = jObj.getString("message");
                        /*alertDialog3(
                                getResources().getString(R.string.app_name),
                                "" + msg);*/

                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jObj.getString("message"), "Gulf Oil", "message");
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

    public void alertDialog3(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        //getActivity().getFragmentManager().popBackStack();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        for (int i = 0; i < 1; ++i) {
                            fm.popBackStack();
                        }
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    class CustomOKDialog extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button ok;
        String msg, heading;
        TextView txtMsg, txtHeading;
        String type;
        FragmentManager fm;

        public CustomOKDialog(Activity a, String msg, String heading, String type) {
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
                    if (type.equals("message")) {
                            /*Intent mIntentGoToHome = new Intent(getActivity(),
                                    MainActivity.class);
                            mIntentGoToHome.putExtra("action", ""+mStringtype);
                            startActivity(mIntentGoToHome);*/
                        //getActivity().getFragmentManager().popBackStack();
                        getActivity().getSupportFragmentManager().beginTransaction().remove(Otp_fragment.this).commit();
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else if (type.equals("error")) {
                        dismiss();
                    }
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    public void alertDialog(String title, String message, final String type) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (type.equals("message")) {
                            /*Intent mIntentGoToHome = new Intent(getActivity(),
                                    MainActivity.class);
                            mIntentGoToHome.putExtra("action", ""+mStringtype);
                            startActivity(mIntentGoToHome);*/
                            //getActivity().getFragmentManager().popBackStack();
                            getActivity().getSupportFragmentManager().beginTransaction().remove(Otp_fragment.this).commit();
                            getActivity().getSupportFragmentManager().popBackStack();
                        } else if (type.equals("error")) {
                            dialog.dismiss();
                        }
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class VerifyOTPV2 extends AsyncTask<String[], Void, JSONObject> {
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
            Log.e("result", "" + params);
            SharedPreferences preferences1 = getActivity().getSharedPreferences(
                    "signupdetails", getActivity().MODE_PRIVATE);
            String fls_loginId = preferences1.getString("usertrno", "");

            try {
                jObj = new UserFunctions().verfiyOTPV2(strParticipnt_login_idV2, fls_loginId, edtOtp.getText().toString());

                Log.e("jobj", "" + jObj);


            } catch (Exception e) {
                // TODO: handle exception
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(),
                                "Network Error...", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if (jsonObject != null) {
                try {
                    String mStringStatus = jsonObject.getString("status");
                    if (mStringStatus.equals("failure")) {
                        progressDialog.dismiss();
                        com.taraba.gulfoilapp.customdialogs.CustomOKDialog cdd = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "" + jsonObject.getString("message"), "Gulf Oil");
                        cdd.show();


                    } else if (mStringStatus.equals("success")) {

                        progressDialog.dismiss();
                        com.taraba.gulfoilapp.customdialogs.CustomOKDialog cdd = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "" + jsonObject.getString("message"), "Gulf Oil");
                        cdd.show();
                        cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                getFragmentManager().popBackStack();
                            }
                        });

                    }
                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            } // end onpostecxe
        }


    }
}
