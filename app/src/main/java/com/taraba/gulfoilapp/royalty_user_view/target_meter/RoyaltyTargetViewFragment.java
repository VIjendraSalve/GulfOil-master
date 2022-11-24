package com.taraba.gulfoilapp.royalty_user_view.target_meter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.customdialogs.CustomDialogForFragment;
import com.taraba.gulfoilapp.model.TargetMeterCategory;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONObject;

import java.util.ArrayList;

/*
 * Created by tarabasoftwareiinc on 12/05/17.
 */
public class RoyaltyTargetViewFragment extends Fragment {
    private static final String TAG = "TargetViewFragment";
    /**
     * COMMENTED BY PRAVIN DHARAM ON 11-Jul-18
     * Remove piechar
     */
    //PieChart pieChart;
    //ENDED
    ArrayList<Entry> entries;
    ArrayList<String> PieEntryLabels;
    PieDataSet pieDataSet;
    PieData pieData;
    private String loginID;
    private TextView tvRetailerActualTarget, tvTargetAchieved, tvPercentageTargetAchieved, tvPercentageTargetBalanceToBeAchieved, tvRetailerID, tvheader;
    private TargetMeterCategory tmc;
    private ImageView iv_target_meter;

    public RoyaltyTargetViewFragment() {

    }

    public static RoyaltyTargetViewFragment newInstance(String loginID, TargetMeterCategory tmc) {
        RoyaltyTargetViewFragment targetViewFragment = new RoyaltyTargetViewFragment();

        Bundle args = new Bundle();
        args.putString("loginID", loginID);
        args.putSerializable("tmc", tmc);
        targetViewFragment.setArguments(args);

        return targetViewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            loginID = bundle.getString("loginID");
            tmc = (TargetMeterCategory) bundle.getSerializable("tmc");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_royalty_target_view, container, false);

        init(view);
//        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GulfOilUtils.callTollFree(getActivity());
//            }
//        });
        return view;
    }

    private void init(View view) {

        tvRetailerActualTarget = (TextView) view.findViewById(R.id.tvRetailerActualTarget);
        tvTargetAchieved = (TextView) view.findViewById(R.id.tvTargetAchieved);
        tvPercentageTargetAchieved = (TextView) view.findViewById(R.id.tvPercentageTargetAchieved);
        tvPercentageTargetBalanceToBeAchieved = (TextView) view.findViewById(R.id.tvPercentageTargetBalanceToBeAchieved);
        tvRetailerID = (TextView) view.findViewById(R.id.tvRetailerID);
        tvheader = (TextView) view.findViewById(R.id.tvheader);
        iv_target_meter = (ImageView) view.findViewById(R.id.iv_target_meter);

/** COMMENTED BY PRAVIN DHARAM ON 11-Jul-18
 *Remove Piechart
 */
        /*pieChart = (PieChart) view.findViewById(R.id.chart1);
        pieChart.setDrawHoleEnabled(false);*/
//ENDED

        entries = new ArrayList<>();
        PieEntryLabels = new ArrayList<String>();


//        Check NW connection and Call Target Meter WS

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            new TargetMeterWS().execute(new String[]{loginID, tmc.getCampaign_name()});
        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }

    }

    private void showTargetMeterImage(String percentage) {
        if (percentage != null && !percentage.isEmpty()) {
            iv_target_meter.setVisibility(View.VISIBLE);
            double numPercentage = Double.parseDouble(percentage);
            if (numPercentage <= 25) {
                iv_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.speed_0_25));
            } else if (numPercentage > 25 && numPercentage <= 50) {
                iv_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.speed_25_50));
            } else if (numPercentage > 50 && numPercentage <= 75) {
                iv_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.speed_50_75));
            } else if (numPercentage > 75 && numPercentage <= 100) {
                iv_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.speed_75_100));
            } else if (numPercentage > 100) {
                iv_target_meter.setImageDrawable(getResources().getDrawable(R.drawable.speed_more_then_100));
            }
        }
    }

    /**
     * COMMENTED BY PRAVIN DHARAM ON 11-Jul-18
     * Remove piechart from activity
     */


//    public void AddValuesToPIEENTRY(float perTargetAchieved) {
//        Log.d(TAG, "AddValuesToPIEENTRY: TARGET ACHIEVED: " + perTargetAchieved);
//
//        entries.add(new BarEntry(perTargetAchieved, 0));
//        entries.add(new BarEntry((100.0f - perTargetAchieved), 1));
//
//        PieEntryLabels.add("Target achieved");
//        PieEntryLabels.add("Target to be achieved");
//
//
//        pieDataSet = new PieDataSet(entries, "");
//
//        pieData = new PieData(PieEntryLabels, pieDataSet);
//
//        //add percentage sign % after value of pia chart
//        pieData.setValueFormatter(new PercentFormatter());
//
//        pieDataSet.setColors(new int[]{R.color.piaChartGreen, R.color.piaChartRed}, getActivity());
//
//        pieChart.setData(pieData);
//
//        pieChart.animateY(3000);
//
//        //remove default description text
//        pieChart.setDescription("");
//
//        //remove piachart text only show values
//        pieChart.setDrawSliceText(false);
//
//        Legend legend = pieChart.getLegend();
//        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
//        legend.setXEntrySpace(10f);
//        legend.setYEntrySpace(15f);
//
//
//    }
    //ENDED

    class TargetMeterWS extends AsyncTask<String[], Void, JSONObject> implements CustomDialogForFragment.MyCallback {
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
                jObj = new UserFunctions().Target_meter_webservice("" + params[0][0], "" + params[0][1]);

                Log.e("", "TargetMeter Response:---" + jObj);
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
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            progressDialog.dismiss();
            if (jsonObject != null) {
                try {
                    progressDialog.dismiss();
                    Log.e("Json Data : ", "Json data : " + jsonObject);

                    // Added By Almas 9 Oct 17
                    String mStringStatus = jsonObject.getString("status");
                    if (mStringStatus.equals("success")) {
                        JSONObject jobj = jsonObject.getJSONObject("data");
                        String header = jobj.getString("header");

                        if (jobj.has("0")) {
                            JSONObject jsonObj = jobj.getJSONObject("0");
                            String login_id_fk = jsonObj.getString("login_id_fk");
                            String retailer_code = jsonObj.getString("retailer_code");
                            String yearly_sales_target = jsonObj.getString("yearly_sales_target");
                            String sales_achievement = jsonObj.getString("sales_achievement");
                            String achivedPercentage = jsonObj.getString("percentage");
                       /* JSONArray jsonArray = jsonObject.getJSONArray("data");
                        String login_id_fk = jsonArray.getJSONObject(0).getString("login_id_fk");
                        String retailer_code = jsonArray.getJSONObject(0).getString("retailer_code");
                        String target_period = jsonArray.getJSONObject(0).getString("target_period");
                        String yearly_sales_target = jsonArray.getJSONObject(0).getString("yearly_sales_target");
                        String sales_achievement = jsonArray.getJSONObject(0).getString("sales_achievement");
                        String percentage = jsonArray.getJSONObject(0).getString("percentage");*/
                            tvheader.setVisibility(View.VISIBLE);
                            tvheader.setText(header);

                            tvRetailerActualTarget.setVisibility(View.VISIBLE);
                            tvRetailerActualTarget.setText("Your Target : " + yearly_sales_target);

                            tvTargetAchieved.setVisibility(View.VISIBLE);
                            tvTargetAchieved.setText("Your Achievement : " + sales_achievement);

                            tvPercentageTargetAchieved.setVisibility(View.VISIBLE);
                            tvPercentageTargetAchieved.setText("You have achieved " + achivedPercentage + "% of your Target");

//                            String balancetobeachived = (Double.parseDouble(achivedPercentage) >= 100) ? "0" : "" + (100 - Double.parseDouble(achivedPercentage));

                            String balancetobeachived = "";
                            balancetobeachived = (Integer.parseInt(yearly_sales_target) - Integer.parseInt(sales_achievement)) + "";
                            tvPercentageTargetBalanceToBeAchieved.setVisibility(View.VISIBLE);
                            tvPercentageTargetBalanceToBeAchieved.setText("Balance to be achieved is " + balancetobeachived + " LTR/KGS");

                            tvRetailerID.setVisibility(View.VISIBLE);
                            tvRetailerID.setText("Retailer ID : " + retailer_code);
                            /*ADDED BY PRAVIN DHARAM ON 11-Jul-18 */
                            showTargetMeterImage(achivedPercentage);
                            //ENDED

                            /** COMMENTED BY PRAVIN DHARAM ON 11-Jul-18
                             *
                             */
                            // AddValuesToPIEENTRY(Float.parseFloat(percentage));
                            //ENDED
                        } else {
                            CustomDialogForFragment dialogForFragment = new CustomDialogForFragment(getActivity(), "Target Meter not found!", "Gulf Oil");
                            dialogForFragment.setMyCallback(this);
                            dialogForFragment.show();
                        }
                    } else {
                        CustomDialogForFragment dialogForFragment = new CustomDialogForFragment(getActivity(), "" + jsonObject.getString("error"), "Gulf Oil");
                        dialogForFragment.setMyCallback(this);
                        dialogForFragment.show();
                    }

                } catch (Exception e) {
                    Log.e("DO THIS", "WHEN SAVE FAILS");
                }
            } else {
                progressDialog.dismiss();
            }

        }

        @Override
        public void onMyBackStack() {
            getActivity().onBackPressed();
        }
    }

}
