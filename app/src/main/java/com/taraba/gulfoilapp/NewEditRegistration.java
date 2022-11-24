package com.taraba.gulfoilapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.taraba.gulfoilapp.async.ResendOTPTask;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewEditRegistration extends Fragment implements View.OnClickListener {


    private static final String RETAILERID = "retailerID";
    private EditText edt_m_mobilenumber;
    private EditText edt_m_shopname;
    private String retailerId;
    private Button btn_update;
    private String shopname, mobile;
    private String edit;
    private String msg;
    private TextView tvMsg, tv_retailerId, tv_retailerName;
    private ConnectionDetector cd;
    private String retailer_name;
    private String retailerCode;
    private Button btn_Resendotp;

    public NewEditRegistration() {
        // Required empty public constructor
    }

    public static NewEditRegistration newInstance(String retailerId) {
        NewEditRegistration fragment = new NewEditRegistration();
        Bundle bundle = new Bundle();
        bundle.putString(RETAILERID, retailerId);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.retailerId = bundle.getString(RETAILERID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_edit_registration, container, false);
        init(view);
        getDetails();
        return view;


    }


    private void init(View view) {
        edt_m_shopname = (EditText) view.findViewById(R.id.edt_m_shopname);
        edt_m_mobilenumber = (EditText) view.findViewById(R.id.edt_m_mobilenumber);
        tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        tv_retailerId = (TextView) view.findViewById(R.id.tv_retailerId);
        tv_retailerName = (TextView) view.findViewById(R.id.tv_retailerName);
        btn_update = (Button) view.findViewById(R.id.btn_update);
        btn_Resendotp = (Button) view.findViewById(R.id.btn_Resendotp);
        btn_update.setOnClickListener(this);
        btn_Resendotp.setOnClickListener(this);

        cd = new ConnectionDetector(getActivity());
    }

    private void getDetails() {
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            new RetailerDetails().execute();
        } else {
            Toast.makeText(getActivity(),
                    "Internet Disconnected", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_update) {

            if (NetworkUtils.isNetworkAvailable(getActivity())) {
                if (validateFields()) {
                    new UpdateRetailerDetails().execute();
                }
            } else {
                Toast.makeText(getActivity(),
                        "Internet Disconnected", Toast.LENGTH_LONG).show();
            }
        } else if (v.getId() == R.id.btn_Resendotp) {
            if (NetworkUtils.isNetworkAvailable(getActivity())) {
                ResendOTPTask resendOTPTask = new ResendOTPTask(getActivity());
                resendOTPTask.setCallback(new ResendOTPTask.ResendOTPCallback() {
                    @Override
                    public void success(JSONObject jsonObject) {
                        try {
                            com.taraba.gulfoilapp.customdialogs.CustomOKDialog cdd = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), "" + jsonObject.getString("message"), "Gulf Oil");
                            cdd.show();
                            cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    openOTPScreen();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                resendOTPTask.execute(new String[]{retailerId});
            } else {
                Toast.makeText(getActivity(),
                        "Internet Disconnected", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateFields() {
        boolean flag = true;
        if (TextUtils.isEmpty(edt_m_shopname.getText())) {
            flag = false;
            Toast.makeText(getActivity(), "Please Enter Shop name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edt_m_mobilenumber.getText())) {
            flag = false;
            Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
        } else if (edt_m_mobilenumber.getText().length() != 10) {
            flag = false;
            Toast.makeText(getActivity(), "Please Enter 10 digit Mobile Number", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    private void enableDisableEditField(boolean enable) {
        edt_m_shopname.setEnabled(enable);
        edt_m_mobilenumber.setEnabled(enable);
    }

    private void openOTPScreen() {
        Fragment otpFragment = new Otp_fragment();
        Bundle bundle = new Bundle();
        bundle.putInt("participant_login_id", Integer.parseInt(retailerId));
        bundle.putBoolean("fromUpdateRetailerProfile", true);
        //Integer.parseInt(jsonObject.getString("participant_login_id")));
        bundle.putString("participant_mob_no", "" + edt_m_mobilenumber.getText().toString());//jsonObject.getString("mobile_no"));
        bundle.putString("participant_code", "" + retailerCode);
        otpFragment.setArguments(bundle);

        SharedPreferences preferences = getActivity().getSharedPreferences("status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("status", "");
        editor.commit();
        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
        ft1.replace(R.id.container_body, otpFragment);
        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft1.addToBackStack(null);
        getFragmentManager().popBackStack();
        ft1.commit();
    }

    private class RetailerDetails extends AsyncTask<String[], Void, JSONObject> {
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
            try {
                jObj = new UserFunctions().GetRetailParticipant("" + retailerId);

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

                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jsonObject.getString("message"), "Gulf Oil");
                        cdd.show();


                    } else if (mStringStatus.equals("success")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                        shopname = GulfOilUtils.getStr(jsonObject1, "workshop_name");
                        mobile = GulfOilUtils.getStr(jsonObject1, "mobile_no");
                        edit = GulfOilUtils.getStr(jsonObject1, "edit");
                        msg = GulfOilUtils.getStr(jsonObject1, "msg");
                        retailerCode = GulfOilUtils.getStr(jsonObject1, "retailer_id");
                        retailer_name = GulfOilUtils.getStr(jsonObject1, "retailer_name");
                        tv_retailerName.setText(retailer_name);

                        edt_m_mobilenumber.setText(mobile);
                        edt_m_shopname.setText(shopname);

                        tv_retailerId.setText(retailerCode);


                        if (edit.equalsIgnoreCase("0")) {
                            tvMsg.setVisibility(View.VISIBLE);
                            tvMsg.setText(msg);
                            btn_update.setVisibility(View.GONE);
                            enableDisableEditField(false);
                        } else if (edit.equalsIgnoreCase("2")) {
                            tvMsg.setVisibility(View.VISIBLE);
                            tvMsg.setText(msg);
                            btn_update.setVisibility(View.GONE);
                            btn_Resendotp.setVisibility(View.VISIBLE);
                            enableDisableEditField(false);
                        } else {
                            tvMsg.setVisibility(View.VISIBLE);
                            tvMsg.setText(msg);
                            btn_update.setVisibility(View.VISIBLE);
                            enableDisableEditField(true);
                        }
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            } // end onpostecxe
        }


    }

    private class UpdateRetailerDetails extends AsyncTask<String[], Void, JSONObject> {
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
                jObj = new UserFunctions().UpdateRetailerProfile("" + retailerId, fls_loginId, edt_m_shopname.getText().toString(), edt_m_mobilenumber.getText().toString());

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

                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jsonObject.getString("message"), "Gulf Oil");
                        cdd.show();


                    } else if (mStringStatus.equals("success")) {
                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jsonObject.getString("message"), "Gulf Oil");
                        cdd.show();
                        cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                openOTPScreen();
                            }
                        });

                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            } // end onpostecxe
        }


    }
}
