package com.taraba.gulfoilapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mayuri on 2/16/16.
 * Modified by Mansi
 */
public class GenuineCheckFragment extends Fragment {

    EditText _mEditTextUniqueId, _mEditTextMobNo;

    Button _mButtonSubmit;
    String mStringUniqueId, mStringMobNo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_genuie_check, container, false);

        initComp(view);

        MyTrackConstants.setColors(_mButtonSubmit, getResources()
                .getColor(R.color.btn_gradient_start_color), getResources()
                .getColor(R.color.btn_gradient_start_color));

        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
        String number = tm.getLine1Number();
        if (number == null || number.equals("")) {

        } else {
            _mEditTextMobNo.setText("" + number);
        }

        _mButtonSubmit
                .setOnClickListener(SubmitClickListener);

        return view;
    }

    public void initComp(View view) {
        _mEditTextUniqueId = (EditText) view.findViewById(R.id.xedt_genuineCheckuniqueid);
        _mEditTextMobNo = (EditText) view.findViewById(R.id.xedt_genuineCheckMobNo);
        _mButtonSubmit = (Button) view.findViewById(R.id.xbtn_genuineChecksubmit);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * PhoneNumberSubmitClickListener =>
     */
    public View.OnClickListener SubmitClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            hideKeyboard();
            if (NetworkUtils.isNetworkAvailable(getActivity())) {
                validate();
            } else {
                Toast.makeText(getActivity(), "Internet Disconnected",
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    private void validate() {

        if (_mEditTextUniqueId.getText().toString().trim()
                .equals("")) {
            Toast.makeText(getActivity(), "Please enter Unique Id",
                    Toast.LENGTH_SHORT).show();
        } else if (_mEditTextMobNo.getText().toString().trim()
                .equals("")) {
            Toast.makeText(getActivity(), "Please enter Mobile Number",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (NetworkUtils.isNetworkAvailable(getActivity())) {
                mStringUniqueId = _mEditTextUniqueId.getText().toString();
                mStringMobNo = _mEditTextMobNo.getText().toString();

                new CheckGenuie().execute(new String[]{
                        _mEditTextUniqueId.getText().toString(),
                        _mEditTextMobNo.getText().toString()});
            } else {
                Toast.makeText(getActivity(), "Internet Disconnected",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    class CheckGenuie extends AsyncTask<String[], Void, JSONObject> {
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
                jObj = new UserFunctions().GenuieCheck_webservice_call(""
                        + params[0][0], "" + params[0][1]);
            } catch (Exception e) {
                // TODO: handle exception
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Network Error...",
                                Toast.LENGTH_LONG).show();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            // showToast("Calling set up views");
            progressDialog.dismiss();
            if (jObj != null) {
                try {
                    Log.e("Json Data : ", "Json data : " + jObj);
                    // String mStringStatus = jObj.getString("error");

                    String mStringResult = jObj.getString("status");
                    if (mStringResult.equals("success")) {
                        /*alertDialog(
                                getResources().getString(R.string.app_name),
                                jObj.getString("message"));*/


                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jObj.getString("message"), "Genuine Verification");
                        cdd.show();

                    } else {
                        // JSONObject result = jObj.getJSONObject("result");
                      /*  alertDialog(
                                getResources().getString(R.string.app_name),
                                jObj.getString("message"));*/


                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jObj.getString("message"), "Genuine Verification");
                        cdd.show();
                    }

                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    } // end onpostecxe

  /*  public void alertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        _mEditTextUniqueId.setText("");
                        _mEditTextMobNo.setText("");

                        dialog.dismiss();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }*/
}