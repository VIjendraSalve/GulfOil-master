package com.taraba.gulfoilapp.royalty_user_view.dashboard;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taraba.gulfoilapp.AppConfig;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.contentproviders.UserModel;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.royalty_user_view.app_demo.RoyaltyAppdemoFragment;
import com.taraba.gulfoilapp.royalty_user_view.campaign_rewards.CampaignRewardsFragment;
import com.taraba.gulfoilapp.royalty_user_view.change_password.NewChangePasswordFragment;
import com.taraba.gulfoilapp.royalty_user_view.dashboard.adpter.RoyaltyDashboardAdapter;
import com.taraba.gulfoilapp.royalty_user_view.dashboard.model.RoyaltyDashboardMenu;
import com.taraba.gulfoilapp.royalty_user_view.digital_reward.RoyaltyDigitalOrderHistoryFragment;
import com.taraba.gulfoilapp.royalty_user_view.help.RoyaltyHelpTabFragment;
import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_dashboard.view.KnowledgeCornerFragment;
import com.taraba.gulfoilapp.royalty_user_view.point_calculator.PointCalculatorWebViewFragment;
import com.taraba.gulfoilapp.royalty_user_view.profile.RoyaltyMyProfileFragment;
import com.taraba.gulfoilapp.royalty_user_view.royal_benefit.RoyaltyBenefitFragment;
import com.taraba.gulfoilapp.royalty_user_view.scheme_letter.RoyaltySchemeLetterFragment;
import com.taraba.gulfoilapp.royalty_user_view.tally_invoice.RoyaltyIPLChampaignUploadFragment;
import com.taraba.gulfoilapp.royalty_user_view.tally_invoice.RoyaltyTallyInvoiceUploadFragment;
import com.taraba.gulfoilapp.royalty_user_view.tally_invoice.RoyaltyTallyInvoiceUploadedfragment;
import com.taraba.gulfoilapp.royalty_user_view.target_meter.RoyaltyTargetMeterCategoryFragment;
import com.taraba.gulfoilapp.royalty_user_view.view_rewards.RewardsFragment;
import com.taraba.gulfoilapp.royalty_user_view.view_transaction.MyPointsFragment;
import com.taraba.gulfoilapp.royalty_user_view.whats_new.RoyaltyWhatsNewFragment;
import com.taraba.gulfoilapp.util.DataProvider;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;
import com.taraba.gulfoilapp.view.splash_pop_up.SplashPopUpActivity;
import com.taraba.gulfoilapp.view.splash_pop_up.model.SplashPopUpDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by android3 on 2/12/16.
 * Modified by Mansi
 */
public class RoyaltyDashboardFragment extends Fragment {
    /// FrameLayout dashboardframe;

    private static final String TAG = "RoyaltyDashboardFragmen";
    public android.os.Handler mHandler;
    GridView gv;
    int strid;
    String mStringMobileNo;
    Long time;
    int old_check = 0;
    int new_check = 0;
    String action;
    private List<RoyaltyDashboardMenu> dashboardMenuList;
    private List<UserModel> circularList;
    private Thread r;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_royalty_dashboard, container, false);
        Log.e(TAG, "onCreateView: ");
        initComp(view);
        // VolleyLog.DEBUG = true;
//        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GulfOilUtils.callTollFree(getActivity());
//            }
//        });
        SharedPreferences preferences = (getActivity()).getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);

        action = preferences.getString("action", "");


        Log.e("", "action in Dashboard fragment:" + action);
        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        String mStringtype = preferences1.getString("user_type", "");
        String retailer_type = preferences1.getString("retailer_type", "");

        int check = Check_notification_count();
        DataProvider dataProvider = new DataProvider();
        dashboardMenuList = dataProvider.getRoyaltyDashboardMenuList(getActivity(), retailer_type);
        gv = (GridView) view.findViewById(R.id.gridView1);
        gv.setAdapter(new RoyaltyDashboardAdapter(view.getContext(), dashboardMenuList, check, "Participant"));


        //date
        // LocationManager locMan = (LocationManager)getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        //long time = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();

//        String timeSettings = android.provider.Settings.System.getString(
//                getActivity().getContentResolver(),
//                android.provider.Settings.System.AUTO_TIME);
//        if (timeSettings.contentEquals("0")) {
//            android.provider.Settings.System.putString(
//                    getActivity().getContentResolver(),
//                    android.provider.Settings.System.AUTO_TIME, "1");
//        }
//        Date now = new Date(System.currentTimeMillis());

        // String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        //time=date_get();
        //Toast.makeText(getActivity(), "Current Date" + "Date:"+time, Toast.LENGTH_LONG).show();
        // Log.d("Date", now.toString());

        mHandler = new android.os.Handler();
        r = new Thread(new Runnable() {
            public void run() {
                try {

                    int check = Check_notification_count();

                    new_check = check;

/*
                    SharedPreferences preferen = getSharedPreferences("Count",MODE_PRIVATE);
                    SharedPreferences.Editor e=preferen.edit();
                    e.putInt("new_count",new_check);
                    e.commit();
*/
                    /*Toast.makeText(MainActivity.this,
                            "check"+String.valueOf(new_check), Toast.LENGTH_LONG)
                            .show();*/

                    //(toolbar.findViewById(R.id.txtUnreadCount)).setText(""+old_check);

                    //txtUnreadCount.setText("" + String.valueOf(old_check));


                    if (new_check > old_check) {


                        if (NetworkUtils.isNetworkAvailable(getActivity())) {
                            old_check = new_check;

                            /*Toast.makeText(getActivity(),
                                    String.valueOf(new_check), Toast.LENGTH_LONG)
                                    .show();*/

                            //      txtUnreadCount.setText("" + String.valueOf(new_check));

                            gv.setAdapter(new RoyaltyDashboardAdapter(view.getContext(), dashboardMenuList, new_check, "Participant"));

                            //     mNavigationNavDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), toolbar, mStringAction,new_check);

                        } else {
                            /*Toast.makeText(getActivity(),
                                    "No Internet Connection", Toast.LENGTH_LONG)
                                    .show();*/
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.postDelayed(this, 4000);
            }
        });

        mHandler.postDelayed(r, 4000);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("Position", "" + position);
                String menuName = dashboardMenuList.get(position).getLebal();
                switch (menuName) {
                    case "View Profile":

                        replaceFragment(new RoyaltyMyProfileFragment());
                        break;

                    case "View Transactions":
                        SharedPreferences preferences11 = getActivity().getSharedPreferences(
                                "signupdetails", getActivity().MODE_PRIVATE);
                        String loginId = preferences11.getString("usertrno", "");
                        Fragment notiFrag1 = new MyPointsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("participant_login_id", Integer.parseInt(loginId));
                        /*bundle.putString("total_points", values.get(position).getTotal_point());
                        bundle.putString("balanced_points", values.get(position).getBalance_points());
                        bundle.putString("redeem_points", values.get(position).getRedeem_point());
                        bundle.putString("participant_code", values.get(position).getParticipant_code());
                        Log.e("part id :", "Part id in adapter : " + values.get(position).getId());*/
                        notiFrag1.setArguments(bundle);
                        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                        ft1.replace(R.id.container_body, notiFrag1);
                        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft1.addToBackStack(null);
                        ft1.commit();
                        break;
                    case "View Rewards":
                        Fragment mech2 = new RewardsFragment();

                        Bundle bundleRewards = new Bundle();
                        bundleRewards.putString("isDisable", "false");
                        bundleRewards.putString("path", "d");
                        mech2.setArguments(bundleRewards);

                        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        String login_id = preferences1.getString("usertrno", "");
                        Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id);

                        SharedPreferences pref = getActivity().getSharedPreferences(
                                "userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit1 = pref.edit();
                        int lid = Integer.parseInt("" + login_id);
                        edit1.putInt("Mechanic_trno_to_redeem", lid);
                        edit1.putString("Mechanic_status", "rewards");
                        edit1.commit();
                        replaceFragment(mech2);
                        break;
                    case "Your Digital Rewards":
                        SharedPreferences preferences2 = getActivity().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        String login_id2 = preferences2.getString("usertrno", "");
                        Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id2);

                        SharedPreferences pref2 = getActivity().getSharedPreferences(
                                "userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit2 = pref2.edit();
                        int lid2 = Integer.parseInt("" + login_id2);
                        edit2.putInt("Mechanic_trno_to_redeem", lid2);

//                        edit2.putString("Mechanic_status", "rewards");

                        edit2.commit();


                        Fragment detailsFragment = new RoyaltyDigitalOrderHistoryFragment();
                        FragmentTransaction ftmech = getActivity().getSupportFragmentManager().beginTransaction();
                        ftmech.replace(R.id.container_body, detailsFragment);
                        ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftmech.addToBackStack(null);
                        ftmech.commit();
                        break;
                    case "What's New":
                        replaceFragment(new RoyaltyWhatsNewFragment());
                        break;
                    case "Scheme Letter":
                        //replaceFragment(new RoyaltySchemeLetterFragment());
                        SharedPreferences preferences4 = getActivity().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        String login_id4 = preferences4.getString("usertrno", "");
                        SharedPreferences pref4 = getActivity().getSharedPreferences(
                                "userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit4 = pref4.edit();
                        int lid4 = Integer.parseInt("" + login_id4);
                        edit4.putInt("Mechanic_trno_to_redeem", lid4);
                        edit4.commit();
                        Fragment shemeLetterFragment = new RoyaltySchemeLetterFragment();
                        FragmentTransaction ftscheme = getActivity().getSupportFragmentManager().beginTransaction();
                        ftscheme.replace(R.id.container_body, shemeLetterFragment);
                        ftscheme.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftscheme.addToBackStack(null);
                        ftscheme.commit();
                        break;
                    case "Target Meter":
                        SharedPreferences pref1 = getActivity().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        String loginId2 = pref1.getString("usertrno", "");
                        replaceFragment(RoyaltyTargetMeterCategoryFragment.newInstance(loginId2));
                        break;
                    case "Change Password":
                        replaceFragment(new NewChangePasswordFragment());
                        break;
                    case "Help":
                        Log.e("Tag ", " Tag : Help");
                        replaceFragment(new RoyaltyHelpTabFragment());
                        break;

                    case "App Demo":
                        replaceFragment(new RoyaltyAppdemoFragment());
                        break;
                    case "Points Calculator":
                        replaceFragment(new PointCalculatorWebViewFragment());
                        break;
                    case "Campaign Rewards":
                        SharedPreferences preferences3 = getActivity().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        String login_id3 = preferences3.getString("usertrno", "");
                        Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id3);

                        SharedPreferences pref3 = getActivity().getSharedPreferences(
                                "userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit3 = pref3.edit();
                        int lid3 = Integer.parseInt("" + login_id3);
                        edit3.putInt("Mechanic_trno_to_redeem", lid3);
                        edit3.commit();

                        Fragment campaignRewardsFragment = new CampaignRewardsFragment();
                        FragmentTransaction ftmechcampaign = getActivity().getSupportFragmentManager().beginTransaction();
                        ftmechcampaign.replace(R.id.container_body, campaignRewardsFragment);
                        ftmechcampaign.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftmechcampaign.addToBackStack(null);
                        ftmechcampaign.commit();
                        break;
                    case "Royale Benefit":
                        SharedPreferences preferences5 = getActivity().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        String login_id5 = preferences5.getString("usertrno", "");
                        Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id5);

                        SharedPreferences pref5 = getActivity().getSharedPreferences(
                                "userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit5 = pref5.edit();
                        int lid5 = Integer.parseInt("" + login_id5);
                        edit5.putInt("Mechanic_trno_to_redeem", lid5);
                        edit5.commit();

                        Fragment royaltyBenefit = new RoyaltyBenefitFragment();
                        FragmentTransaction ftRoyaltyBenefit = getActivity().getSupportFragmentManager().beginTransaction();
                        ftRoyaltyBenefit.replace(R.id.container_body, royaltyBenefit);
                        ftRoyaltyBenefit.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftRoyaltyBenefit.addToBackStack(null);
                        ftRoyaltyBenefit.commit();
                        break;
                    case "Tally Invoice Upload":
                        SharedPreferences preferences6 = getActivity().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        String login_id6 = preferences6.getString("usertrno", "");
                        Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id6);

                        SharedPreferences pref6 = getActivity().getSharedPreferences(
                                "userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit6 = pref6.edit();
                        int lid6 = Integer.parseInt("" + login_id6);
                        edit6.putInt("Mechanic_trno_to_redeem", lid6);
                        edit6.commit();

                        if (NetworkUtils.isNetworkAvailable(getActivity())) {
                            new GetUploadedInvoiceStatus().execute(login_id6);
                        } else {
                            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                        }

                       /* Fragment royaltyTallyInvoiceUploadFragment = new RoyaltyTallyInvoiceUploadFragment();
                        FragmentTransaction ftRoyaltyTallyInvoiceUploadFragment = getActivity().getSupportFragmentManager().beginTransaction();
                        ftRoyaltyTallyInvoiceUploadFragment.replace(R.id.container_body, royaltyTallyInvoiceUploadFragment);
                        ftRoyaltyTallyInvoiceUploadFragment.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftRoyaltyTallyInvoiceUploadFragment.addToBackStack(null);
                        ftRoyaltyTallyInvoiceUploadFragment.commit();*/
                        break;
                    case "IPL Offer": //there is _ intended behaviour - < IPL CHAMPAIGN SCREEN >
                        SharedPreferences preferences7 = getActivity().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        String login_id7 = preferences7.getString("usertrno", "");
                        Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id7);

                        SharedPreferences pref7 = getActivity().getSharedPreferences(
                                "userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit7 = pref7.edit();
                        int lid7 = Integer.parseInt("" + login_id7);
                        edit7.putInt("Mechanic_trno_to_redeem", lid7);
                        edit7.commit();

                        if (NetworkUtils.isNetworkAvailable(getActivity())) {
                            new GetUploadedIPLChampaignStatus().execute(login_id7);
//                            Fragment royaltyTallyInvoiceUploadFragment = new RoyaltyIPLChampaignUploadFragment();
//                            FragmentTransaction ftRoyaltyTallyInvoiceUploadFragment = getActivity().getSupportFragmentManager().beginTransaction();
//                            ftRoyaltyTallyInvoiceUploadFragment.replace(R.id.container_body, royaltyTallyInvoiceUploadFragment);
//                            ftRoyaltyTallyInvoiceUploadFragment.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                            ftRoyaltyTallyInvoiceUploadFragment.addToBackStack(null);
//                            ftRoyaltyTallyInvoiceUploadFragment.commit();
                        } else {
                            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                        }

                       /* Fragment royaltyTallyInvoiceUploadFragment = new RoyaltyTallyInvoiceUploadFragment();
                        FragmentTransaction ftRoyaltyTallyInvoiceUploadFragment = getActivity().getSupportFragmentManager().beginTransaction();
                        ftRoyaltyTallyInvoiceUploadFragment.replace(R.id.container_body, royaltyTallyInvoiceUploadFragment);
                        ftRoyaltyTallyInvoiceUploadFragment.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftRoyaltyTallyInvoiceUploadFragment.addToBackStack(null);
                        ftRoyaltyTallyInvoiceUploadFragment.commit();*/
                        break;
                    case "Knowledge Corner":
                        replaceFragment(new KnowledgeCornerFragment());
                        break;
                }
            }
        });
        if (AppConfig.isSplashPopUpSessionActive)
            callSplashPopUpAPI();
        return view;
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

    public void initComp(View view) {
        // dashboardframe = (FrameLayout) view.findViewById(R.id.container_bodyindh);
        /*imgvmyprofile= (ImageView) view.findViewById(R.id.btn_myprofile);
        imgvabout = (ImageView) view.findViewById(R.id.btn_aboutus);
        imgvnew_registration = (ImageView) view.findViewById(R.id.btn_newregistration);
        imgvmember_search = (ImageView) view.findViewById(R.id.btn_membersearch);
        imgvgenuine_verification = (ImageView) view.findViewById(R.id.btn_genuineverification);
        imgvhelp = (ImageView) view.findViewById(R.id.btn_help);

        imgvtransactions= (ImageView) view.findViewById(R.id.btn_transaction);
        imgvrewards= (ImageView) view.findViewById(R.id.btn_rewards);
        imgvnotifications= (ImageView) view.findViewById(R.id.btn_notification);*/
    }

    public int Check_notification_count() {
        int count = 0;
        try {

            final UserTableDatasource ctdUser1 = new UserTableDatasource(getActivity());
            try {

                //if(ctdUser.)

                ctdUser1.open();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            SharedPreferences preferences1 = AppConfig.getInstance().getSharedPreferences(
                    "signupdetails", Context.MODE_PRIVATE);

            mStringMobileNo = preferences1.getString("uname", "");


            count = ctdUser1.getStatusCount("" + mStringMobileNo);

           /* SharedPreferences preferences1 = getActivity().getSharedPreferences(
                    "signupdetails", Context.MODE_PRIVATE);

            mStringMobileNo=preferences1.getString("uname", "");


            count=ctdUser.getStatusCount(""+mStringMobileNo);
*/

            Log.e("notification dashdcount", String.valueOf(count));
            ctdUser1.close();

//Toast.makeText(getApplicationContext(),MyConstants.eventID,Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }//Check_notification_count

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft4 = getFragmentManager().beginTransaction();
        ft4.replace(R.id.container_body, fragment);
        ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft4.addToBackStack(null);
        ft4.commit();
    }

    private void callSplashPopUpAPI() {

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            SharedPreferences preferences1 = getActivity().getSharedPreferences(
                    "signupdetails", Context.MODE_PRIVATE);
            String login_id = preferences1.getString("usertrno", "");
            new CallSplashPopUpAPI().execute(login_id);
        }

    }

    private void showSplashPopUpDialogs(List<SplashPopUpDetails> splashPopUpDetailsList) {

        startActivity(
                new Intent(getActivity(), SplashPopUpActivity.class)
                        .putParcelableArrayListExtra("splash_pop_up_list", (ArrayList<? extends Parcelable>) splashPopUpDetailsList)
                        .putExtra("isRoyalUser", true)
        );
        AppConfig.isSplashPopUpSessionActive = false;
    }


    /*class SearchMechanic extends AsyncTask<String[], Void, JSONObject> {
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
                SharedPreferences preferences1 = getActivity().getSharedPreferences(
                        "signupdetails", getActivity().MODE_PRIVATE);
                String loginId = preferences1.getString("usertrno", "");
                String user_type = preferences1.getString("user_type", "");
                Log.d("Result_Response3", user_type);
                Log.e("data : ", " Mobile No : " + params[0][0]);
                jObj = new UserFunctions().GetParticipant(""
                        + params[0][0], "Search By Mobile Number", user_type, loginId);
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
            // showToast("Calling set up views");

            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {

                       *//* alertDialog2(
                                getResources().getString(R.string.app_name),
                                "Number Not Registered");*//*
                        progressDialog.dismiss();
                    } else if (mStringStatus.equals("success")) {

                        JSONArray resultJArray = jObj.getJSONArray("particpant_data");
                        Log.e("particpant_data : ", "particpant_data : " + resultJArray);

                        JSONObject result = resultJArray.getJSONObject(0);
                        Log.e("result : ", "result : " + result);
                        circularList = new ArrayList<UserModel>();
                        UserModel _news = new UserModel();
                        strid = Integer.parseInt(result.getString("login_id_pk"));
                        _news.setId(Integer.parseInt(result.getString("login_id_pk")));
                        _news.setPicture(result.getString("image_path"));
                        _news.setUserfname(result.getString("first_name"));
                        _news.setUserlname(result.getString("last_name"));
                        _news.setGender(result.getString("gender"));
                        _news.setNomini(result.getString("nominee_name"));
                        _news.setNominirely(result.getString("nominee_relation"));
                        // _news.setMothername(mothername);
                        _news.setWorkshopname(result.getString("workshop_name"));

                        _news.setState(result.getString("state"));
                        _news.setDistrict(result.getString("district"));
                        _news.setPincode(result.getString("pincode"));
                        _news.setTaluka(result.getString("taluka"));
                        _news.setCity(result.getString("city"));
                        _news.setShopadd(result.getString("workshop_address"));
                        _news.setLandmark(result.getString("landmark"));
                        _news.setMobile_no(result.getString("mobile_no"));

                        _news.setEmail(result.getString("email_id"));
                        _news.setSector(result.getString("sector"));
                        _news.setSpecialization(result.getString("specialization"));
                        _news.setDob(result.getString("dob"));
                        _news.setRegdate(result.getString("record_date"));
                        _news.setToatalsperconpermonth(result.getString("total_consumption"));
                        _news.setSperpartconformmvehicpermonth(result.getString("spare_consumption"));
                        _news.setMmgenuspareconpermonth(result.getString("genuine_consumption"));
                        _news.setTotalvehicalpermonth(result.getString("total_vehicles"));

                        _news.setMahindravehicalpermonth(result.getString("client_vehicles"));
                        _news.setNoofmechanics(result.getString("no_of_mechanics"));
                        _news.setBalance_points(result.getString("points"));
                        _news.setStatus(result.getString("status"));

                        _news.setParticipant_code(result.getString("participant_code"));
                        _news.setParticipant_id_pk(result.getString("participant_id_pk"));
                        _news.setForm_fillup_date(result.getString("form_fillup_date"));

                        circularList.add(_news);


                        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
                        try {
                            ctdUser.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (jObj.has("participant_claim_history")) {
                            ctdUser.deleteFromClaimHistory(result.getString("login_id_pk"));
                            ctdUser.insertIntoHomework(result, jObj.getJSONArray("participant_claim_history").toString());
                            ctdUser.insertBulkClaimHistry(jObj.getJSONArray("participant_claim_history"), getActivity(), result.getString("login_id_pk"));

                            ctdUser.close();
                        } else {
                            ctdUser.insertIntoHomework(result, "");
                            ctdUser.close();
                        }
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            } // end onpostecxe
        }
    }*/

    class CustomYesNoDialog extends Dialog implements
            View.OnClickListener {

        public Activity c;
        public Dialog d;
        public Button yes, no;
        TextView txtMsg;

        public CustomYesNoDialog(Activity a) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_logout_dialog);
            txtMsg = (TextView) findViewById(R.id.txt_msg);
            txtMsg.setText("Do you really want to call?");
            yes = (Button) findViewById(R.id.btn_Yes);
            no = (Button) findViewById(R.id.btn_No);
            yes.setOnClickListener(this);
            no.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_Yes:
                    dismiss();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:18002094470"));
                    startActivity(callIntent);
                    break;
                case R.id.btn_No:
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    class GetUploadedInvoiceStatus extends AsyncTask<String, Void, JSONObject> {

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
        protected JSONObject doInBackground(String... params) {
            Log.e("insert :", "in do in background of get order history");
            JSONObject jObj = null;
            try {
                jObj = new UserFunctions().GetUploadedInvoiceStatus(params[0]);
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
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (jObj.has("invoice")) {
                            if (jObj.getBoolean("invoice")) {
                                JSONObject jObjInvoiceImg = jObj.getJSONArray("invoice_details").getJSONObject(0);
                                Bundle b = new Bundle();

                                b.putString("invoice_1", jObjInvoiceImg.getString("invoice_1"));
                                //invoice_2 & invoice_3 both images are optional
                                if (jObjInvoiceImg.has("invoice_2"))
                                    b.putString("invoice_2", jObjInvoiceImg.getString("invoice_2"));
                                if (jObjInvoiceImg.has("invoice_3"))
                                    b.putString("invoice_3", jObjInvoiceImg.getString("invoice_3"));

                                Fragment royaltyTallyInvoiceUploadedfragment = new RoyaltyTallyInvoiceUploadedfragment();

                                royaltyTallyInvoiceUploadedfragment.setArguments(b);

                                FragmentTransaction ftmech = getActivity().getSupportFragmentManager().beginTransaction();
                                ftmech.replace(R.id.container_body, royaltyTallyInvoiceUploadedfragment);
                                ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                ftmech.addToBackStack(null);
                                ftmech.commit();
                            } else {
                                CustomOKDialog cd = new CustomOKDialog(getActivity(),
                                        "" + jObj.getString("message"),
                                        getActivity().getResources().getString(R.string.app_name));
                                cd.show();
                            }
                        }


                    } else {
                        Fragment royaltyTallyInvoiceUploadFragment = new RoyaltyTallyInvoiceUploadFragment();
                        FragmentTransaction ftRoyaltyTallyInvoiceUploadFragment = getActivity().getSupportFragmentManager().beginTransaction();
                        ftRoyaltyTallyInvoiceUploadFragment.replace(R.id.container_body, royaltyTallyInvoiceUploadFragment);
                        ftRoyaltyTallyInvoiceUploadFragment.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftRoyaltyTallyInvoiceUploadFragment.addToBackStack(null);
                        ftRoyaltyTallyInvoiceUploadFragment.commit();
                        //progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e);
                }
            }
        }
    }

    class GetUploadedIPLChampaignStatus extends AsyncTask<String, Void, JSONObject> {

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
        protected JSONObject doInBackground(String... params) {
            Log.e("insert :", "in do in background of get order history");
            JSONObject jObj = null;
            try {
                jObj = new UserFunctions().GetUploadedInvoiceStatus(params[0]);
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
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("success")) {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        if (jObj.getBoolean("invoice_flag")) {

                            Fragment royaltyTallyInvoiceUploadFragment = new RoyaltyIPLChampaignUploadFragment();
                            FragmentTransaction ftRoyaltyTallyInvoiceUploadFragment = getActivity().getSupportFragmentManager().beginTransaction();
                            ftRoyaltyTallyInvoiceUploadFragment.replace(R.id.container_body, royaltyTallyInvoiceUploadFragment);
                            ftRoyaltyTallyInvoiceUploadFragment.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            ftRoyaltyTallyInvoiceUploadFragment.addToBackStack(null);
                            ftRoyaltyTallyInvoiceUploadFragment.commit();
                        } else {
                            CustomOKDialog cd = new CustomOKDialog(getActivity(),
                                    "" + jObj.getString("message"),
                                    getActivity().getResources().getString(R.string.app_name));
                            cd.show();
                        }


                    } else {
                        CustomOKDialog cd = new CustomOKDialog(getActivity(),
                                "" + jObj.getString("error"),
                                getActivity().getResources().getString(R.string.app_name));
                        cd.show();
                    }
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e);
                }
            }
        }
    }

    class CallSplashPopUpAPI extends AsyncTask<String, Void, JSONObject> {

        private Context mContext;

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "CallSplashPopUpAPI - onPreExecute() called");
            // super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            Log.d(TAG, "CallSplashPopUpAPI - doInBackground() called with: params = [" + params + "]");
            JSONObject jObj = null;
            try {
                jObj = new UserFunctions().GetSplashPopUpDetails(params[0]);
                Log.e("", "CallSplashPopUpAPI - json:" + jObj);

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
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("success")) {
                        if (jObj.getBoolean("display_popup")) {
                            JSONArray dataList = jObj.getJSONArray("data");
                            List<SplashPopUpDetails> splashPopUpDetailsList = new Gson().fromJson(dataList.toString(), new TypeToken<List<SplashPopUpDetails>>() {
                            }.getType());
                            if (splashPopUpDetailsList != null && splashPopUpDetailsList.size() > 0)
                                showSplashPopUpDialogs(splashPopUpDetailsList);
                        }

                    } else {
                    }
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e);
                }
            }
        }
    }
}
