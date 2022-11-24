package com.taraba.gulfoilapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.taraba.gulfoilapp.databinding.ActivityForgotBinding;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONObject;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


/**
 * Created by android on 12/18/15.
 * Modified by Mansi
 */
public class ForgotActivity extends Activity implements OnClickListener {

    //LinearLayout lin_changePass, lin_forgetPass;
    String mStringUserNo;//,mStringEmailId, mStringOTP, Email, mStringPassword, mStringPasswordNew;
    //EditText edt_new_pass, edt_confirm_pass;
    private ActivityForgotBinding binding;

    public void initComp() {

    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent(this, LoginActivity.class);
        startActivity(mIntent);
        finish();
    }

    /*@Override
    protected void onNewIntent(Intent intent) {
        ForgotActivity.this.finish();
        Log.e("In new Instance", "In new Instance");
        Intent i = new Intent(ForgotActivity.this, ForgotActivity.class);
        startActivity(i);
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initComp();
        setListeners();


    }

    private void setListeners() {
        binding.footerView.tvFooterTollFreeNumber.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private void validateForSendPass() {
        binding.tilUsername.setError(null);
        if (binding.edtUsername.getText().toString().trim().equals("")) {
            binding.tilUsername.setError("Enter username");
        } else {
            if (NetworkUtils.isNetworkAvailable(this)) {

                mStringUserNo = binding.edtUsername.getText().toString();
                new SendNewPass().execute(new String[]{mStringUserNo});

                SharedPreferences preferences1 = getSharedPreferences("forgotinfo", Context.MODE_PRIVATE);
                Editor edit = preferences1.edit();
                edit.putString("register_no", "" + mStringUserNo);
                edit.commit();

            } else {
                Toast.makeText(ForgotActivity.this, "Internet Disconnected", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFooterTollFreeNumber:
                GulfOilUtils.callTollFree(ForgotActivity.this);
                break;
            case R.id.btnSubmit:
                submitForgotPassword();
                break;
        }
    }

    private void submitForgotPassword() {
        hideKeyboard();
        if (NetworkUtils.isNetworkAvailable(this)) {
            validateForSendPass();
        } else {
            Toast.makeText(ForgotActivity.this, "Internet Disconnected",
                    Toast.LENGTH_LONG).show();
        }
    }


    public void alertDialog(String message, final String fromwhere) {

        new GulfUnnatiDialog(this)
                .setTitle(fromwhere)
                .setDescription(message)
                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                    dialog.dismiss();
                    if (fromwhere.equals(getString(R.string.str_success))) {
                        SharedPreferences preferences = getSharedPreferences("forgotinfo", Context.MODE_PRIVATE);

                        String mStringno = preferences.getString("register_no", "");
                        String mStringpass = preferences.getString("password", "");
                        Intent mIntent = new Intent(ForgotActivity.this, LoginActivity.class);
                        mIntent.putExtra("action", "forgot");
                        mIntent.putExtra("number", mStringno);
                        mIntent.putExtra("pass", mStringpass);
                        startActivity(mIntent);
                        finish();
                    }
                })
                .show();

    }

    class SendNewPass extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ForgotActivity.this);
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String[]... params) {
            JSONObject jObj = null;
            Log.e("data in do inback: ", "Data : Username : " + params[0][0]);
            try {
                Log.e("data : ", "Data : Username : " + params[0][0]);
                jObj = new UserFunctions().ForgotPass_webservice_call_NewPAss("" + params[0][0]);
            } catch (Exception e) {
                // TODO: handle exception
                ForgotActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(ForgotActivity.this, "Network Error...", Toast.LENGTH_LONG).show();
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
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("success")) {
                        String mStringPass = jObj.getString("message");
                        Log.e("in sucess :", "in sucess 1");

                        alertDialog(mStringPass,
                                getString(R.string.str_success));

                    } else {
                        String mStringPass = jObj.getString("error");
                        alertDialog(mStringPass,
                                getString(R.string.str_error));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } // end onpostecxe
    }


}
