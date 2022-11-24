package com.taraba.gulfoilapp.royalty_user_view.voucher_details;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.messaging.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.royalty_user_view.voucher_details.adapter.RoyaltyVoucherListAdapter;
import com.taraba.gulfoilapp.royalty_user_view.voucher_details.model.VoucherDetails;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoyaltyDigitalOrderVoucherDetailsFragment extends Fragment {
    /* access modifiers changed from: private */
    public ArrayList<VoucherDetails> voucherDetails;
    ListView lstVoucherDetails;
    int mech_trno = 0;
    String rewardCode;
    View rootView;
    TextView tv_expiry_of_code;
    TextView tv_reward_code_details;
    TextView tv_voucher_amount;
    TextView tv_voucher_no;
    String voucher_id;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        this.voucher_id = arguments.getString("voucher_id", "");
        this.rewardCode = arguments.getString("rewardCode", "");
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.rootView = layoutInflater.inflate(R.layout.fragment_royalty_digital_order_voucher_details, viewGroup, false);
        initView();
        return this.rootView;
    }

    private void initView() {
        this.tv_reward_code_details = (TextView) this.rootView.findViewById(R.id.tv_reward_code_details);
        this.tv_voucher_no = (TextView) this.rootView.findViewById(R.id.tv_voucher_no);
        this.tv_voucher_amount = (TextView) this.rootView.findViewById(R.id.tv_voucher_amount);
        this.tv_expiry_of_code = (TextView) this.rootView.findViewById(R.id.tv_expiry_of_code);
        this.lstVoucherDetails = (ListView) this.rootView.findViewById(R.id.lst_voucher_details);
        TextView textView = this.tv_reward_code_details;
        textView.setText(this.rewardCode + " Details");
        if (this.mech_trno == 0) {
            this.mech_trno = getActivity().getSharedPreferences("userinfo", 0).getInt("Mechanic_trno_to_redeem", 0);
        }
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            new GetVoucherDetails().execute();
        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }
    }

    class GetVoucherDetails extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        private ProgressDialog progressDialog;

        GetVoucherDetails() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            Log.e("insert :", "in pre execute of get order history");
            ProgressDialog progressDialog2 = new ProgressDialog(RoyaltyDigitalOrderVoucherDetailsFragment.this.getActivity());
            this.progressDialog = progressDialog2;
            progressDialog2.setMessage("Please  wait!!!");
            this.progressDialog.setIndeterminate(true);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        public JSONObject doInBackground(Void... voidArr) {
            Log.e("insert :", "in do in background of get order history");
            JSONObject jSONObject = null;
            try {
                UserFunctions userFunctions = new UserFunctions();
                jSONObject = userFunctions.GetVoucherDetails("" + RoyaltyDigitalOrderVoucherDetailsFragment.this.mech_trno, RoyaltyDigitalOrderVoucherDetailsFragment.this.voucher_id);
                Log.e("", "order history json:" + jSONObject);
                return jSONObject;
            } catch (Exception unused) {
                RoyaltyDigitalOrderVoucherDetailsFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RoyaltyDigitalOrderVoucherDetailsFragment.this.getActivity(), "Network Error...", Toast.LENGTH_LONG).show();
                    }
                });
                return jSONObject;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(JSONObject jSONObject) {
            super.onPostExecute(jSONObject);
            Log.e("insert :", "in post execute of proceed order" + jSONObject);
            this.progressDialog.dismiss();
            if (jSONObject != null) {
                try {
                    if (jSONObject.getString("status").equals("success")) {
                        ArrayList unused = RoyaltyDigitalOrderVoucherDetailsFragment.this.voucherDetails = (ArrayList) new Gson().fromJson(jSONObject.getJSONArray(Constants.ScionAnalytics.MessageType.DATA_MESSAGE).toString(), new TypeToken<List<VoucherDetails>>() {
                        }.getType());
                        RoyaltyDigitalOrderVoucherDetailsFragment.this.tv_voucher_no.setText(((VoucherDetails) RoyaltyDigitalOrderVoucherDetailsFragment.this.voucherDetails.get(0)).getVoucher_no());
                        RoyaltyDigitalOrderVoucherDetailsFragment.this.tv_voucher_amount.setText(((VoucherDetails) RoyaltyDigitalOrderVoucherDetailsFragment.this.voucherDetails.get(0)).getVoucher_amount());
                        RoyaltyDigitalOrderVoucherDetailsFragment.this.tv_expiry_of_code.setText(((VoucherDetails) RoyaltyDigitalOrderVoucherDetailsFragment.this.voucherDetails.get(0)).getExpiry_of_the_code());
                        RoyaltyDigitalOrderVoucherDetailsFragment.this.lstVoucherDetails.setAdapter(new RoyaltyVoucherListAdapter(RoyaltyDigitalOrderVoucherDetailsFragment.this.getActivity(), ((VoucherDetails) RoyaltyDigitalOrderVoucherDetailsFragment.this.voucherDetails.get(0)).getInvoiceDetail()));
                        return;
                    }
                    new CustomOKDialog(RoyaltyDigitalOrderVoucherDetailsFragment.this.getActivity(), jSONObject.getString("error"), "Gulf Oil").show();
                    this.progressDialog.dismiss();
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e);
                }
            }
        }
    }
}
