package com.taraba.gulfoilapp.royalty_user_view.campaign_reward_details;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.messaging.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.model.CampaignRewardsDetails;
import com.taraba.gulfoilapp.model.RewardOrderDetail;
import com.taraba.gulfoilapp.royalty_user_view.how_to_use_rewards.HowToUseRewardsFragment;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CampaignRewardsDetailsFragment extends Fragment implements View.OnClickListener {
    private static final String MECH_TRNO = "mech_trno";
    /* access modifiers changed from: private */
    public ListView lst_order_details;
    /* access modifiers changed from: private */
    public String order_detail_id;
    /* access modifiers changed from: private */
    public TextView tv_order_date;
    /* access modifiers changed from: private */
    public TextView tv_order_id;
    /* access modifiers changed from: private */
    public TextView tv_reward_name;
    List<CampaignRewardsDetails> campaignRewardsList;
    int mech_trno = 0;
    private TextView tv_click_here;

    public static CampaignRewardsDetailsFragment newInstance(int i, boolean z) {
        CampaignRewardsDetailsFragment campaignRewardsDetailsFragment = new CampaignRewardsDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MECH_TRNO, i);
        campaignRewardsDetailsFragment.setArguments(bundle);
        return campaignRewardsDetailsFragment;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_campaign_rewards_details, viewGroup, false);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_click_here);
        this.tv_click_here = textView;
        textView.setOnClickListener(this);
        this.tv_order_date = (TextView) inflate.findViewById(R.id.tv_order_date);
        this.tv_order_id = (TextView) inflate.findViewById(R.id.tv_order_id);
        this.tv_reward_name = (TextView) inflate.findViewById(R.id.tv_reward_name);
        this.lst_order_details = (ListView) inflate.findViewById(R.id.lst_order_details);
        if (this.mech_trno == 0) {
            this.mech_trno = getActivity().getSharedPreferences("userinfo", 0).getInt("Mechanic_trno_to_redeem", 0);
        }
        this.order_detail_id = getArguments().getString("order_detail_id");
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            new GetCampaignRewardsDetails().execute(new Void[0]);
        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }
        return inflate;
    }

    public void alertDialog2(String str, String str2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(str);
        builder.setMessage(str2).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.tv_click_here) {
            HowToUseRewardsFragment howToUseRewardsFragment = new HowToUseRewardsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("product_name", this.campaignRewardsList.get(0).getProduct_name());
            bundle.putString("product_image", this.campaignRewardsList.get(0).getProduct_image());
            bundle.putString("reward_code", this.campaignRewardsList.get(0).getReward_code());
            bundle.putString("reward_value", this.campaignRewardsList.get(0).getReward_value());
            bundle.putString("product_description", this.campaignRewardsList.get(0).getProduct_description());
            howToUseRewardsFragment.setArguments(bundle);
            FragmentTransaction beginTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.container_body, howToUseRewardsFragment);
            beginTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            beginTransaction.addToBackStack((String) null);
            beginTransaction.commit();
        }
    }

    class GetCampaignRewardsDetails extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        private ProgressDialog progressDialog;

        GetCampaignRewardsDetails() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            Log.e("insert :", "in pre execute of get order history");
            ProgressDialog progressDialog2 = new ProgressDialog(CampaignRewardsDetailsFragment.this.getActivity());
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
                jSONObject = userFunctions.GetCampaignRewardsDetails("" + CampaignRewardsDetailsFragment.this.mech_trno, CampaignRewardsDetailsFragment.this.order_detail_id);
                Log.e("", "order history json:" + jSONObject);
                return jSONObject;
            } catch (Exception unused) {
                CampaignRewardsDetailsFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CampaignRewardsDetailsFragment.this.getActivity(), "Network Error...", Toast.LENGTH_LONG).show();
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
                        JSONArray jSONArray = jSONObject.getJSONArray(Constants.ScionAnalytics.MessageType.DATA_MESSAGE);
                        CampaignRewardsDetailsFragment.this.campaignRewardsList = (List) new Gson().fromJson(jSONArray.toString(), new TypeToken<List<CampaignRewardsDetails>>() {
                        }.getType());
                        CampaignRewardsDetailsFragment.this.tv_order_id.setText(CampaignRewardsDetailsFragment.this.campaignRewardsList.get(0).getOrder_detail_id());
                        CampaignRewardsDetailsFragment.this.tv_order_date.setText(CampaignRewardsDetailsFragment.this.campaignRewardsList.get(0).getOrders_record_date());
                        CampaignRewardsDetailsFragment.this.tv_reward_name.setText(CampaignRewardsDetailsFragment.this.campaignRewardsList.get(0).getReward_name());
                        if (CampaignRewardsDetailsFragment.this.campaignRewardsList.get(0).getRewardOrderDetail() == null || CampaignRewardsDetailsFragment.this.campaignRewardsList.get(0).getRewardOrderDetail().size() == 0) {
                            RewardOrderDetail rewardOrderDetail = new RewardOrderDetail();
                            rewardOrderDetail.setEvCode("-");
                            rewardOrderDetail.setEvPin("");
                            rewardOrderDetail.setExpiryDate("-");
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(rewardOrderDetail);
                            CampaignRewardsDetailsFragment.this.campaignRewardsList.get(0).setRewardOrderDetail(arrayList);
                        }
                        CampaignRewardsDetailsFragment.this.lst_order_details.setAdapter(new CampaignRewardsDetailsAdapter(CampaignRewardsDetailsFragment.this.getActivity(), CampaignRewardsDetailsFragment.this.campaignRewardsList.get(0).getRewardOrderDetail(), CampaignRewardsDetailsFragment.this.campaignRewardsList.get(0).getReward_value(), CampaignRewardsDetailsFragment.this.campaignRewardsList.get(0).getReward_code()));
                        return;
                    }
                    new CustomOKDialog(CampaignRewardsDetailsFragment.this.getActivity(), jSONObject.getString("error"), "Gulf Oil").show();
                    this.progressDialog.dismiss();
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }
}
