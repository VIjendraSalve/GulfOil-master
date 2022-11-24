package com.taraba.gulfoilapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONObject;

/**
 * Created by Mansi on 3/16/16.
 * Modified by Mansi
 */
public class SubmitOtpFragment extends Fragment {

    TextView txt_otp_desc;
    EditText et_otp;
    Button btn_submit, btn_back;
    SharedPreferences PREF_participant_login_id;
    String product_code = "";
    int participant_login_id;
    int mech_trno;
    String order_id, otp;

    String order_request_no = "", user_login_id = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_proceed_activity, container, false);

        Bundle b = getArguments();

        order_request_no = b.getString("order_request_no", "");
        user_login_id = b.getString("user_login_id", "");

        SharedPreferences preferences = getActivity().getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);

        mech_trno = preferences.getInt("Mechanic_trno_to_redeem", 0);
        Log.e("", "Mechanic TRNO : " + mech_trno);

        Log.e("", "participant login id in proceed order is:" + participant_login_id);
        initComp(view);
        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                otp = et_otp.getText().toString();
                if (otp.equals("") || otp == null) {
                    Toast.makeText(getActivity(), "Please enter otp", Toast.LENGTH_LONG).show();
                } else {

                    if (NetworkUtils.isNetworkAvailable(getActivity())) {
                        new submitOrder().execute();
                    } else {
                        Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

/*
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
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
        });*/


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
            try {
                jObj = new UserFunctions().submitOtp(order_request_no, user_login_id, otp);
            } catch (Exception e) {
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
                        /*alertDialog3(
                                getResources().getString(R.string.app_name),
                                "" + msg);*/

                        CustomOKDialog cd = new CustomOKDialog(getActivity(), "" + msg, "Gulf Oil");
                        cd.show();
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
                        FragmentManager fm = getActivity().getFragmentManager();
                        for (int i = 0; i < 2; ++i) {
                            fm.popBackStack();
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

        public CustomOKDialog(Context a, String msg, String heading) {
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
                    /*if (type.equals("dismiss")) {*/
                    dismiss();
                    et_otp.setText("");
                    //getActivity().getFragmentManager().popBackStack();
                    FragmentManager fm = getActivity().getFragmentManager();
                    for (int i = 0; i < 2; ++i) {
                        fm.popBackStack();
                    }
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

}
