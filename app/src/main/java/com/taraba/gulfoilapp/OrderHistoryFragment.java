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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.adapter.OrderHistoryAdapter;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.OrderHistory;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by android3 on 3/16/16.
 * Modified by Mansi
 */
public class OrderHistoryFragment extends Fragment {

    private static final String MECH_TRNO = "mech_trno";
    private static final String FROM_MECh_SEARCH = "from_mech_search";
    String order_request_no = "";

    ListView lst_orderHistory;
    SharedPreferences PREF_participant_login_id;
    int participant_login_id = 0;
    int mech_trno = 0;
    boolean fromMechSearch = false;

    // added by Pravin Dharam 24-5-2017
    public static OrderHistoryFragment newInstance(int mech_trno, boolean fromSearchMember) {
        OrderHistoryFragment orderHistoryFragment = new OrderHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MECH_TRNO, mech_trno);
        bundle.putBoolean(FROM_MECh_SEARCH, fromSearchMember);
        orderHistoryFragment.setArguments(bundle);
        return orderHistoryFragment;
    }

    // added by Pravin Dharam 24-5-2017
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mech_trno = bundle.getInt(MECH_TRNO, 0);
            fromMechSearch = bundle.getBoolean(FROM_MECh_SEARCH, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        // added by Pravin Dharam 24-5-2017
        if (mech_trno == 0) {
            SharedPreferences preferences = getActivity().getSharedPreferences(
                    "userinfo", Context.MODE_PRIVATE);

            mech_trno = preferences.getInt("Mechanic_trno_to_redeem", 0);
        }
        //End


        Log.e("", "Mechanic trno to RedeemMainFragment : " + mech_trno);

        lst_orderHistory = (ListView) view.findViewById(R.id.lst_order_history);



        if (NetworkUtils.isNetworkAvailable(getActivity())) {

            new getOrderHistory().execute();

        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }
        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });
        return view;
    }

    class getOrderHistory extends AsyncTask<Void, Void, JSONObject> {

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
                jObj = new UserFunctions().orderHistory("" + mech_trno);
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

                        ArrayList<OrderHistory> arrayList_orderHistory = new ArrayList<OrderHistory>();
                        JSONArray dataJArray = jObj.getJSONArray("data");

                        Log.e("", "dataArray size:-----------------------" + dataJArray.length());
                        for (int i = 0; i < dataJArray.length(); i++) {

                            JSONObject dataObject = dataJArray.getJSONObject(i);
                            Log.e("", "dataObject json------------" + dataObject);

                            String order_id = dataObject.optString("order_request_no");
                            String name = dataObject.optString("name");
                            String points = dataObject.optString("points");
                            String orders_record_date = dataObject.optString("orders_record_date");

                            order_request_no = dataObject.optString("order_request_no");

                            String orderDetails = dataObject.getJSONArray("order_detail").toString();

                            /*ArrayList<String> orders=new ArrayList<String>();
                            for(int k=0;k<orderDetails.length();k++) {
                                JSONObject orderObject = orderDetails.getJSONObject(k);
                                orders.add("" + orderObject.toString());
                            }*/
                            String order_status = dataObject.optString("orders_status");

                            arrayList_orderHistory.add(new OrderHistory("" + order_id, "" + name, "" + points, "" + order_status, "" + orders_record_date, "" + order_request_no, "" + orderDetails));

                            Log.e("", "order id in order history fragment" + order_id);
                            Log.e("", "name in order history fragment" + name);
                            Log.e("", "points in order history fragment" + points);
                            Log.e("", "date in order history fragment" + orders_record_date);
                            Log.e("", "status in order history fragment" + order_status);
                        }
                        lst_orderHistory.setAdapter(new OrderHistoryAdapter(getActivity(), arrayList_orderHistory, getActivity().getSupportFragmentManager(), fromMechSearch));

                    } else {
                        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle(getString(R.string.str_error))
                                .setDescription("" + jObj.getString("error"))
                                .setPosButtonText(getString(R.string.str_ok), null)
                                .show();

                        progressDialog.dismiss();
                    }

                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }

}