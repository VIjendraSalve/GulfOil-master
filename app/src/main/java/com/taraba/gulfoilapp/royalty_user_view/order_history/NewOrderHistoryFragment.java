package com.taraba.gulfoilapp.royalty_user_view.order_history;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.MainDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.constant.UserType;
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
public class NewOrderHistoryFragment extends Fragment {

    private static final String MECH_TRNO = "mech_trno";
    private static final String FROM_MECh_SEARCH = "from_mech_search";
    String order_request_no = "";

    private RecyclerView rvOrderHistory;
    private ArrayList<OrderHistory> orderHistoryList;
    SharedPreferences PREF_participant_login_id;
    int participant_login_id = 0;
    int mech_trno = 0;
    boolean fromMechSearch = false;

    // added by Pravin Dharam 24-5-2017
    public static NewOrderHistoryFragment newInstance(int mech_trno, boolean fromSearchMember) {
        NewOrderHistoryFragment orderHistoryFragment = new NewOrderHistoryFragment();
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
        View view = inflater.inflate(getLayoutUserWise(), container, false);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_order_history));
        }
        // added by Pravin Dharam 24-5-2017
        if (mech_trno == 0) {
            SharedPreferences preferences = getActivity().getSharedPreferences(
                    "userinfo", Context.MODE_PRIVATE);

            mech_trno = preferences.getInt("Mechanic_trno_to_redeem", 0);
        }
        //End


        Log.e("", "Mechanic trno to RedeemMainFragment : " + mech_trno);

        rvOrderHistory = view.findViewById(R.id.rvOrderHistory);



        if (NetworkUtils.isNetworkAvailable(getActivity())) {

            new getOrderHistory().execute();

        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_order_history_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_order_history_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_order_history_club;
        else
            return R.layout.fragment_order_history_royal;
    }

    class getOrderHistory extends AsyncTask<Void, Void, JSONObject> implements RecyclerViewOnClickListener {

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
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),
                        "Network Error...", Toast.LENGTH_LONG).show());

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


                        orderHistoryList = new ArrayList<>();
                        JSONArray dataJArray = jObj.getJSONArray("data");

                        Log.e("", "dataArray size:-----------------------" + dataJArray.length());
                        for (int i = 0; i < dataJArray.length(); i++) {

                            JSONObject dataObject = dataJArray.getJSONObject(i);
                            Log.e("", "dataObject json------------" + dataObject);

                            String order_id = dataObject.optString("order_request_no");
                            String name = dataObject.optString("name");
                            String points = dataObject.optString("points");
                            String orders_record_date = dataObject.optString("orders_record_date");
                            String product_image = dataObject.optString("product_image");
                            String qty = dataObject.optString("qty");

                            order_request_no = dataObject.optString("order_request_no");

                            String orderDetails = dataObject.getJSONArray("order_detail").toString();

                            String order_status = dataObject.optString("orders_status");

                            orderHistoryList.add(new OrderHistory("" + order_id, "" + name, "" + points, "" + order_status, "" + orders_record_date, "" + order_request_no, "" + orderDetails, product_image, qty));

                            Log.e("", "order id in order history fragment" + order_id);
                            Log.e("", "name in order history fragment" + name);
                            Log.e("", "points in order history fragment" + points);
                            Log.e("", "date in order history fragment" + orders_record_date);
                            Log.e("", "status in order history fragment" + order_status);
                        }
                        OrderHistoryAdapterNew orderHistoryAdapterNew = new OrderHistoryAdapterNew(getActivity(), orderHistoryList);
                        orderHistoryAdapterNew.setOnClickListener(this);
                        rvOrderHistory.setAdapter(orderHistoryAdapterNew);

                    } else {


                        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle(getString(R.string.str_error))
                                .setDescription(jObj.getString("error"))
                                .setPosButtonText(getString(R.string.str_ok), null)
                                .show();

                        progressDialog.dismiss();
                    }

                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }

        @Override
        public void onRecyclerViewItemClick(View v, int position) {
            NewOrderHistoryFragmentDirections.ActionOrderHistoryFragmentToOrderHistoryDetailsFragment action = NewOrderHistoryFragmentDirections.actionOrderHistoryFragmentToOrderHistoryDetailsFragment(orderHistoryList.get(position));
            Navigation.findNavController(v).navigate(action);
        }
    }
}