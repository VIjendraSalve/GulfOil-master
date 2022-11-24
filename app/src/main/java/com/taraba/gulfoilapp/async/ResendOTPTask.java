package com.taraba.gulfoilapp.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dell on 02-Oct-18.
 */
public class ResendOTPTask extends AsyncTask<String[], Void, JSONObject> {
    private ProgressDialog progressDialog;
    private Context mContext;
    private ResendOTPCallback callback;

    public ResendOTPTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(mContext);
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
            jObj = new UserFunctions().resendOTPV2(params[0][0]);

        } catch (Exception e) {
            // TODO: handle exception
            try {
                progressDialog.dismiss();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
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
                    com.taraba.gulfoilapp.customdialogs.CustomOKDialog cdd = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(mContext, "" + jsonObject.getString("message"), "Gulf Oil");
                    cdd.show();


                } else if (mStringStatus.equals("success")) {

                    progressDialog.dismiss();
                    if (callback != null) {
                        callback.success(jsonObject);
                    }

                }
            } catch (JSONException e) {
                Log.e("Error : ", "Error : " + e.toString());
            }
        } // end onpostecxe
    }

    public void setCallback(ResendOTPCallback callback) {
        this.callback = callback;
    }

    public interface ResendOTPCallback {
        void success(JSONObject jsonObject);
    }
}
