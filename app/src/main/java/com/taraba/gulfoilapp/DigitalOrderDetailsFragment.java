package com.taraba.gulfoilapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taraba.gulfoilapp.adapter.DigitalOrderDetailsAdapter;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.model.CampaignRewardsDetails;
import com.taraba.gulfoilapp.model.RewardOrderDetail;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android3 on 3/16/16.
 * Modified by Mansi
 */
public class DigitalOrderDetailsFragment extends Fragment implements View.OnClickListener {

    String order_details_id;
    private ListView lst_order_details;
    private String orderDetailsJson = "", order_request_no = "";
    int mech_trno = 0;
    private TextView tv_order_date, tv_order_id, tv_reward_name, tv_click_here;
    private ArrayList<CampaignRewardsDetails> digitalOrderHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_digital_order_details, container, false);

        tv_order_date = (TextView) view.findViewById(R.id.tv_order_date);
        tv_order_id = (TextView) view.findViewById(R.id.tv_order_id);
        tv_reward_name = (TextView) view.findViewById(R.id.tv_reward_name);
        lst_order_details = (ListView) view.findViewById(R.id.lst_order_details);
        tv_click_here = (TextView) view.findViewById(R.id.tv_click_here);
        tv_click_here.setOnClickListener(this);

        Bundle b = getArguments();
        //digitalOrderHistory = (DigitalOrderHistory) b.getSerializable("Order_Details");
        order_details_id = b.getString("order_details_id");

        if (mech_trno == 0) {
            SharedPreferences preferences = getActivity().getSharedPreferences(
                    "userinfo", Context.MODE_PRIVATE);

            mech_trno = preferences.getInt("Mechanic_trno_to_redeem", 0);
        }

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            new GetRewardsDetails().execute();
        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(getActivity(),"Order request no in order details fragment is:"+order_request_no,Toast.LENGTH_LONG).show();

       /* String product_name = b.getString("product_name", "");
        Log.e("OrderDetailsFragment", "******************Order details frament json is:*********************" + orderDetailsJson);
        tv_order_id.setText(digitalOrderHistory.getOrdersIdPk());
        tv_order_date.setText(digitalOrderHistory.getOrdersRecordDate());
        tv_reward_name.setText(digitalOrderHistory.getRewardName());

        if (digitalOrderHistory.getRewardOrderDetail() == null || digitalOrderHistory.getRewardOrderDetail().size() == 0) {
            RewardOrderDetail rod = new RewardOrderDetail();
            rod.setEvCode("-");
            rod.setEvPin("");
            rod.setExpiryDate("-");

            List<RewardOrderDetail> rodList = new ArrayList<>();
            rodList.add(rod);
            digitalOrderHistory.setRewardOrderDetail(rodList);
        }

        lst_order_details.setAdapter(new DigitalOrderDetailsAdapter(getActivity(), digitalOrderHistory.getRewardOrderDetail(), digitalOrderHistory.getRewardValue(), digitalOrderHistory.getRewardCode()));
        */
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_click_here:
                Fragment howToUseFragment = new MechHowToUseRewardsFragment();
                Bundle b = new Bundle();
                b.putString("product_name", digitalOrderHistory.get(0).getProduct_name());
                b.putString("product_image", digitalOrderHistory.get(0).getProduct_image());
                b.putString("reward_code", digitalOrderHistory.get(0).getReward_code());
                b.putString("reward_value", digitalOrderHistory.get(0).getReward_value());
                b.putString("product_description", digitalOrderHistory.get(0).getProduct_description());
                howToUseFragment.setArguments(b);

                FragmentTransaction ftmech = getActivity().getSupportFragmentManager().beginTransaction();
                //FragmentTransaction ftmech = ((OrderHistoryFragment) context).getSu()
                ftmech.replace(R.id.container_body, howToUseFragment);
                ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftmech.addToBackStack(null);
                ftmech.commit();
                break;
        }
    }

    class GetRewardsDetails extends AsyncTask<Void, Void, JSONObject> {

        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            Log.e("insert :", "in pre execute of get order history");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            Log.e("insert :", "in do in background of get order history");
            JSONObject jObj = null;
            try {
                jObj = new UserFunctions().GetCampaignRewardsDetails("" + mech_trno, order_details_id);
                Log.e("", "order history json:" + jObj);

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
            Log.e("insert :", "in post execute of proceed order" + jObj);
            progressDialog.dismiss();

            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("success")) {
                        JSONArray jsonArrayOrders = jObj.getJSONArray("data");
                        digitalOrderHistory = new Gson().fromJson(jsonArrayOrders.toString(), new TypeToken<List<CampaignRewardsDetails>>() {
                        }.getType());
                        tv_order_id.setText(digitalOrderHistory.get(0).getOrder_id());
                        tv_order_date.setText(digitalOrderHistory.get(0).getOrders_record_date());
                        tv_reward_name.setText(digitalOrderHistory.get(0).getReward_name());

                        if (digitalOrderHistory.get(0).getRewardOrderDetail() == null || digitalOrderHistory.get(0).getRewardOrderDetail().size() == 0) {
                            RewardOrderDetail rod = new RewardOrderDetail();
                            rod.setEvCode("-");
                            rod.setEvPin("");
                            rod.setExpiryDate("-");
                            rod.setGulfVoucher("false");

                            List<RewardOrderDetail> rodList = new ArrayList<>();
                            rodList.add(rod);
                            digitalOrderHistory.get(0).setRewardOrderDetail(rodList);
                        }
                        lst_order_details.setAdapter(new DigitalOrderDetailsAdapter(getActivity(), digitalOrderHistory.get(0).getRewardOrderDetail(), digitalOrderHistory.get(0).getReward_value(), digitalOrderHistory.get(0).getReward_code()));
                        //lst_order_details.setAdapter(new RoyaleOrderDetailsAdapter(getActivity(), digitalOrderHistory.get(0).getRewardOrderDetail(), digitalOrderHistory.get(0).getReward_value(), digitalOrderHistory.get(0).getReward_code()));

                    } else {
                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), jObj.getString("error"), "Gulf Oil");
                        cdd.show();
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }

}