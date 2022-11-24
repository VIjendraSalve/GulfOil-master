package com.taraba.gulfoilapp.royalty_user_view.digital_reward;

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
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.model.DigitalOrderHistory;
import com.taraba.gulfoilapp.model.OrderHistory;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoyaltyDigitalOrderHistoryFragment extends Fragment {

    private static final String MECH_TRNO = "mech_trno";
    private static final String FROM_MECh_SEARCH = "from_mech_search";
    String order_request_no = "";

    ListView lst_orderHistory;
    int participant_login_id = 0;
    int mech_trno = 0;
    boolean fromMechSearch = false;


    public static RoyaltyDigitalOrderHistoryFragment newInstance(int mech_trno, boolean fromSearchMember) {
        RoyaltyDigitalOrderHistoryFragment orderHistoryFragment = new RoyaltyDigitalOrderHistoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_royal_digital_order_history, container, false);

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

            new GetDigitalOrdersHistory().execute();

        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }
//        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GulfOilUtils.callTollFree(getActivity());
//            }
//        });
        return view;
    }

    public void alertDialog2(String title, String message) {
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

    class GetDigitalOrdersHistory extends AsyncTask<Void, Void, JSONObject> {

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
                jObj = new UserFunctions().getDigitOrderHistory("" + mech_trno);
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
                        List<DigitalOrderHistory> digitalOrderHistoryList = new Gson().fromJson(jsonArrayOrders.toString(), new TypeToken<List<DigitalOrderHistory>>() {
                        }.getType());
                        ArrayList<OrderHistory> arrayList_orderHistory = new ArrayList<OrderHistory>();
                        JSONArray dataJArray = jObj.getJSONArray("data");

                        lst_orderHistory.setAdapter(new RoyaltyDigitalOrderHistoryAdapter(getActivity(), digitalOrderHistoryList, getActivity().getSupportFragmentManager(), fromMechSearch));

                    } else {

                       /* alertDialog2(
                                getResources().getString(R.string.app_name),
                                "" + jObj.getString("error"));*/

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