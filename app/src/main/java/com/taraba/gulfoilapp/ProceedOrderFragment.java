package com.taraba.gulfoilapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONObject;

/**
 * Created by Mansi on 3/15/16.
 * Modified by Mansi
 */
public class ProceedOrderFragment extends Fragment {

    TextView txt_otp_desc;
    EditText et_otp;
    Button btn_submit, btn_back;
    SharedPreferences PREF_participant_login_id;
    String product_code = "", user_login_id = "";
    int participant_login_id;
    int mech_trno;
    String order_id, otp, address_type;
    private String multi_qty;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_proceed_activity, container, false);

        Bundle b = getArguments();
        product_code = b.getString(MyTrackConstants._mStringProductCode);
        multi_qty = b.getString("multi_qty");

        SharedPreferences preferences = getActivity().getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);

        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                "address_key", Context.MODE_PRIVATE);

        mech_trno = preferences.getInt("Mechanic_trno_to_redeem", 0);
        Log.e("", "Mechanic TRNO : " + mech_trno);

        Log.e("", "participant login id in proceed order is:" + participant_login_id);
        Log.e("", "product id in proceed order is:" + product_code);

        address_type = preferences1.getString("address_type", "");
        Log.e("proceed", "prefs addressType:: " + address_type);

        initComp(view);


        if (NetworkUtils.isNetworkAvailable(getActivity())) {

            new ProceedOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otp = et_otp.getText().toString();

                if (otp.equals("") || otp == null) {
                   /* alertDialog1(
                            getResources().getString(R.string.app_name),
                            "Please Enter OTP");*/

                    CustomOKDialog cdd = new CustomOKDialog(getActivity(), "Please Enter OTP", "Gulf Oil");
                    cdd.show();

                } else {


                    if (NetworkUtils.isNetworkAvailable(getActivity())) {

                        new submitOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    } else {
                        Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }

                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra("isOtp", "otp");
                Log.e("", "isOTP:");
                i.putExtra("action", "otp");
                startActivity(i);
                getActivity().finish();
            }
        });

        return view;
    }

    class submitOrder extends AsyncTask<Void, Void, JSONObject> {

        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            Log.e("insert :", "in pre execute of progees order");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            Log.e("insert :", "in do in background of submit otp");
            JSONObject jObj = null;
           /* "order_request_no": 14577815663,
                    "user_login_id":"12",
                    "otp"           : 78277*/

            try {
                SharedPreferences preferences1 = getActivity().getSharedPreferences(
                        "signupdetails", getActivity().MODE_PRIVATE);
                String loginId = preferences1.getString("usertrno", "");

                jObj = new UserFunctions().submitOtp(order_id, loginId, otp);
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
            Log.e("insert :", "in post execute of submit otp");
            // showToast("Calling set up views");
            progressDialog.dismiss();


            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {

                       /* alertDialog1(
                                getResources().getString(R.string.app_name),
                                "" + jObj.getString("error"));*/

                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jObj.getString("error"), "Gulf Oil");
                        cdd.show();

                        progressDialog.dismiss();
                    } else if (mStringStatus.equals("success")) {
                        Log.e("", "Resopse in proceed order fragment" + jObj);
                        String msg = jObj.getString("message");
                        alertDialog3(
                                getResources().getString(R.string.app_name),
                                "" + msg);
                    }
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }

    public void initComp(View view) {
        txt_otp_desc = (TextView) view.findViewById(R.id.txt_desc);
        et_otp = (EditText) view.findViewById(R.id.edt_otp);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        btn_back = (Button) view.findViewById(R.id.btn_back);
    }

    public void alertDialog3(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        et_otp.setText("");
                        //getActivity().getFragmentManager().popBackStack();
//                        FragmentManager fm = getActivity().getSupportFragmentManager();
//                        for (int i = 0; i < 3; ++i) {
//                            fm.popBackStack();
//                        }
                        gotoDashboard();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void alertDialog2(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        //getActivity().getSupportFragmentManager().popBackStack();
                        getActivity().finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void alertDialog(String title, String message) {
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


    public void alertDialog1(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        et_otp.setText("");
                        //    getActivity().getFragmentManager().popBackStack();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void gotoDashboard() {
        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        String mStringtype = preferences1.getString("user_type", "");

        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putExtra("isOtp", "otp");
        Log.e("", "isOTP:");
        i.putExtra("action", "otp");
        startActivity(i);
        getActivity().finish();
    }

    class ProceedOrder extends AsyncTask<Void, Void, JSONObject> {

        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            Log.e("insert :", "in pre execute of progees order");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            Log.e("insert :", "in do in background of proceed order");
            JSONObject jObj = null;
            try {
                SharedPreferences preferences1 = getActivity().getSharedPreferences(
                        "signupdetails", getActivity().MODE_PRIVATE);
                String loginId = preferences1.getString("usertrno", "");
                jObj = new UserFunctions().proceedOrder("" + mech_trno, product_code, loginId, address_type, multi_qty);

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
            Log.e("insert :", "in post execute of proceed order");
            // showToast("Calling set up views");
            progressDialog.dismiss();

            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {
                        alertDialog2(
                                getResources().getString(R.string.app_name),
                                "" + jObj.getString("error"));
                        progressDialog.dismiss();
                    } else if (mStringStatus.equals("success")) {
                        Log.e("", "Resopse in proceed order fragment" + jObj);
                        String msg = jObj.getString("message");

                        txt_otp_desc.setTextColor(Color.parseColor("#2e7d32"));
                        //#388e3c
                        txt_otp_desc.setText("" + msg);
                        order_id = jObj.getJSONObject(MyTrackConstants._mStringContent).getString(MyTrackConstants._mStringOrderRequestNo);
                    }
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }
}
