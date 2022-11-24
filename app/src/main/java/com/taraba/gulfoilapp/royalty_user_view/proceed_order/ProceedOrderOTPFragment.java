package com.taraba.gulfoilapp.royalty_user_view.proceed_order;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.chaos.view.PinView;
import com.taraba.gulfoilapp.MyTrackConstants;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONObject;

/**
 * Created by Mansi on 3/15/16.
 * Modified by Mansi
 */
public class ProceedOrderOTPFragment extends Fragment {

    TextView txt_otp_desc, tv_resend_otp;
    Button btn_submit;
    //    EditText et_otp;
    private PinView pinViewOTP;
    SharedPreferences PREF_participant_login_id;
    String product_code = "", user_login_id = "";
    int participant_login_id;
    int mech_trno;
    String order_id, otp, address_type;
    private String multi_qty;
    private boolean fromOrderHistoryScreen;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutUserWise(), container, false);

        Bundle b = getArguments();
        ProceedOrderOTPFragmentArgs args = ProceedOrderOTPFragmentArgs.fromBundle(getArguments());
        product_code = args.getProductCode();
        multi_qty = args.getMultiQty();
        fromOrderHistoryScreen = args.getFromOrderHistoryScreen();
        order_id = args.getOrderId();


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
            if (!fromOrderHistoryScreen)
                new ProceedOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                otp = et_otp.getText().toString();
                otp = pinViewOTP.getText().toString();

                if (otp.equals("") || otp == null) {

                    new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                            .setTitle(getString(R.string.str_error))
                            .setDescription("Please Enter OTP")
                            .setPosButtonText(getString(R.string.str_ok), null)
                            .show();

                } else {


                    if (NetworkUtils.isNetworkAvailable(getActivity())) {

                        new submitOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    } else {
                        Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        return view;
    }

    public void initComp(View view) {
        txt_otp_desc = view.findViewById(R.id.txt_desc);
        tv_resend_otp = view.findViewById(R.id.tv_resend_otp);

        tv_resend_otp.setOnClickListener(v -> new ResendOtp().execute());

//        et_otp = view.findViewById(R.id.edt_otp);
        pinViewOTP = view.findViewById(R.id.pinViewOTP);

        btn_submit = view.findViewById(R.id.btn_submit);
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
//                        et_otp.setText("");
                        //    getActivity().getFragmentManager().popBackStack();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void gotoDashboard() {
        Navigation.findNavController(getView()).popBackStack();
        Navigation.findNavController(getView()).popBackStack();
//        Intent i = new Intent(getActivity(), MainDashboardActivity.class);
//        startActivity(i);
//        getActivity().finish();
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_proceed_order_otp_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_proceed_order_otp_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_proceed_order_otp_club;
        else
            return R.layout.fragment_proceed_order_otp_royal;
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

                        String tag = "";
                        if (jObj.has("tag"))
                            tag = jObj.getString("tag");
                        String tag_lable = getString(R.string.str_ok);
                        if (jObj.has("tag_label"))
                            tag_lable = jObj.getString("tag_label");
                        progressDialog.dismiss();
                        String finalTag = tag;
                        GulfUnnatiDialog dialog = new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle("")
                                .hideDialogCloseButton(true)
                                .setDescription(jObj.getString("error"))
                                .setPosButtonText(tag_lable, dialog12 -> {

                                    dialog12.dismiss();
                                    if (finalTag.equals("my_profile")) {
                                        Navigation.findNavController(getView()).popBackStack();
                                        Navigation.findNavController(getView()).navigate(R.id.participantProfileFragment);
                                    } else
                                        Navigation.findNavController(getView()).popBackStack();
                                });
                        if (tag.equals("my_profile")) {
                            dialog.setNegButtonText(getString(android.R.string.cancel), dialog1 -> {
                                dialog1.dismiss();
                                Navigation.findNavController(getView()).popBackStack();
                            });
                        }

                        dialog.show();
                    } else if (mStringStatus.equals("success")) {
                        Log.e("", "Resopse in proceed order fragment" + jObj);
                        String msg = jObj.getString("message");

                        txt_otp_desc.setText("" + msg);
                        order_id = jObj.getJSONObject(MyTrackConstants._mStringContent).getString(MyTrackConstants._mStringOrderRequestNo);
                    }
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
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

                        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle(getString(R.string.str_error))
                                .setDescription("" + jObj.getString("error"))
                                .setPosButtonText(getString(R.string.str_ok), null)
                                .show();

                        progressDialog.dismiss();
                    } else if (mStringStatus.equals("success")) {
                        Log.e("", "Resopse in proceed order fragment" + jObj);
                        String msg = jObj.getString("message");
                        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle(getString(R.string.str_success))
                                .setDescription(msg)
                                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                                    dialog.dismiss();
                                    pinViewOTP.setText("");
                                    gotoDashboard();
                                })
                                .show();
                    }
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }

    class ResendOtp extends AsyncTask<Void, Void, JSONObject> {
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
            Log.e("insert :", "in do in background of resend otp");
            JSONObject jObj = null;
            try {
                //jObj = new UserFunctions().resendOtp(""+order_request_no);
                jObj = new UserFunctions().resendOtp("" + order_id);
                Log.e("", "resend otp json:" + jObj);
            } catch (Exception e) {
                // TODO: handle exception
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),
                        "Network Error...", Toast.LENGTH_LONG).show());
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            Log.e("insert :", "in post execute of get order details");
            // showToast("Calling set up views");
            progressDialog.dismiss();
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {
                        progressDialog.dismiss();

                        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle(getString(R.string.str_error))
                                .setDescription("" + jObj.getString("error"))
                                .setPosButtonText(getString(R.string.str_ok), null)
                                .show();

                    } else if (mStringStatus.equals("Success")) {

                        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle(getString(R.string.str_error))
                                .setDescription("" + jObj.getString("error"))
                                .setPosButtonText(getString(R.string.str_ok), null)
                                .show();


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
