package com.taraba.gulfoilapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taraba.gulfoilapp.adapter.DashboardAdapter;
import com.taraba.gulfoilapp.contentproviders.UserModel;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.fragment.HelpTabFragment;
import com.taraba.gulfoilapp.fragment.SchemeLetterFragment;
import com.taraba.gulfoilapp.fragment.WhatsNewFragment;
import com.taraba.gulfoilapp.model.DashboardMenu;
import com.taraba.gulfoilapp.util.DataProvider;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.MyProfileFragment;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;
import com.taraba.gulfoilapp.view.splash_pop_up.SplashPopUpActivity;
import com.taraba.gulfoilapp.view.splash_pop_up.model.SplashPopUpDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayuri on 2/13/16.
 * Modified by Mansi
 */
public class MechanicDashboardFragment extends Fragment {
    private static final String TAG = "MechanicDashboardFragme";
    String mStringAction;
    ArrayList prgmName;
    private ListView listView;
    private List<UserModel> circularList;
    int cnt = 0;
    TextView txtMsg;
    int strid;
    String mStringMobileNo;
    ImageView btn_myprofile, btn_notification, btn_transaction, btn_claim_history, btn_genuineverification,
            btn_rewards, btn_help, btn_aboutus;
    UserTableDatasource ctdUser;
    TextView tvDashContact;
    GridView gv;
    private List<DashboardMenu> dashboardMenuList;
    /* public static String[] prgmNameList = {"View Profile", "View Transactions", "View Reward", "Whats New", "Scheme Letter", "Target Meter", "   Change Password" ,"  Help"};

     public static int[] prgmImages = {R.mipmap.h, R.mipmap.k,
             R.mipmap.b, R.mipmap.e
             , R.mipmap.d, R.mipmap.j, R.mipmap.c,R.mipmap.i
     };*/
    /*String[] gridColor ={

            "#008B8B",
            "#00FF00",
            "#48D1CC",
            "#556B2F",
            "#696969",

    };*/
   /* R.mipmap.veiw_profile,
    R.mipmap.view_reward, R.mipmap.target_meter
    ,R.mipmap.whats_new,
    R.mipmap.scheme_letter,R.mipmap.edita,
    R.mipmap.help,R.mipmap.change_password*/
    String action;
    private Thread r;
    int old_check = 0;
    int new_check = 0;
    public android.os.Handler mHandler;

   /* @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

    }*/

    String loginId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_dashboard, container, false);
        Log.e(TAG, "onCreateView: ");
        int check = Check_notification_count();
        gv = (GridView) view.findViewById(R.id.gridView1);
        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });
        DataProvider dataProvider = new DataProvider();
        dashboardMenuList = dataProvider.getMechDashboardMenuList(getActivity());

        gv.setAdapter(new DashboardAdapter(view.getContext(), dashboardMenuList, check, "Mechanic"));
        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                "signupdetails", getActivity().MODE_PRIVATE);
        loginId = preferences1.getString("usertrno", "");

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

                         /*   Toast.makeText(getActivity(),
                                    String.valueOf(new_check), Toast.LENGTH_LONG)
                                    .show();*/

                            //      txtUnreadCount.setText("" + String.valueOf(new_check));

                            gv.setAdapter(new DashboardAdapter(view.getContext(), dashboardMenuList, new_check, "Participant"));

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

        SharedPreferences preferences = (getActivity()).getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);

        action = preferences.getString("action", "");

        Log.e("", "action in Mechanic Dashboard fragment:" + action);

     /*   Toast.makeText(getActivity(),"action in Mechanic Dashboard fragment:"+action,Toast.LENGTH_SHORT).show();

        Toast.makeText(getActivity(),"action in Mechanic Dashboard fragment:"+action,Toast.LENGTH_SHORT).show();*/


        view.findViewById(R.id.btnappdemo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new AppdemoFragment());
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String manuTxt = dashboardMenuList.get(position).getLebal();
                Log.e("Position", "" + position + " menu+ " + manuTxt);

                switch (manuTxt) {
                    case "View Profile":
                        Fragment mechSearch = new MyProfileFragment();
                        replaceFragment(mechSearch);
                        break;


                    case "View Transactions":
                        Log.e("Tag ", " Tag : Transactions");
                      /*  Context context = view.getContext();

                        ConnectionDetector cd = new ConnectionDetector(
                                getActivity());
                        if (cd.isConnectingToInternet()) {

                            SharedPreferences preferences1 = context.getSharedPreferences(
                                    "signupdetails", Context.MODE_PRIVATE);

                            mStringMobileNo = preferences1.getString("uname", "");

                            //mStringMobileNo = mEditTextMobNo.getText().toString();
                            new SearchMechanic().execute(new String[]{mStringMobileNo});
                        } else {
                            Toast.makeText(getActivity(),
                                    "Internet Disconnected", Toast.LENGTH_LONG).show();
                        }

*/
                        Fragment notiFrag1 = new ClaimHistoryFragment();
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

                    case "temp":

                        Log.e("Tag ", " Tag : Notificsatioon");
                        /*Fragment mechhelp = new ContactUsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("action", MyTrackConstants._mStringNotification);
                        mechhelp.setArguments(bundle);
                        FragmentTransaction fthelp = getFragmentManager().beginTransaction();
                        fthelp.replace(R.id.container_body, mechhelp);
                        fthelp.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fthelp.addToBackStack(null);
                        fthelp.commit();*/

                        Fragment notiFrag = new ComingSoonFragment();
                        replaceFragment(notiFrag);
                        break;

                    case "View Rewards":
                        int n = 1;
//                        if(n==1){
//                            com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), getString(R.string.window_cannot_open), "Gulf Oil");
//                            cod.show();
//                        }else {
                              /* Fragment genuineverify = new GenuineCheckFragment();
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        ft2.replace(R.id.container_body, genuineverify);
                        ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft2.addToBackStack(null);
                        ft2.commit();*/
                        Fragment mech2 = new RedeemMainFragment();

                        Bundle bundleRewards = new Bundle();
                        bundleRewards.putString("isDisable", "false");
                        bundleRewards.putString("path", "dashboard");
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

                        //  }

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


                        Fragment detailsFragment = new DigitalOrderHistoryFragment();
                        FragmentTransaction ftmech = getActivity().getSupportFragmentManager().beginTransaction();
                        ftmech.replace(R.id.container_body, detailsFragment);
                        ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftmech.addToBackStack(null);
                        ftmech.commit();
                        break;
                    case "What's New":
                        replaceFragment(new WhatsNewFragment());
                        break;
                    case "Scheme Letter":
                        SharedPreferences preferences3 = getActivity().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        String login_id3 = preferences3.getString("usertrno", "");
                        Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id3);

                        SharedPreferences pref3 = getActivity().getSharedPreferences(
                                "userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit3 = pref3.edit();
                        int lid3 = Integer.parseInt("" + login_id3);
                        edit3.putInt("Mechanic_trno_to_redeem", lid3);

//                        edit2.putString("Mechanic_status", "rewards");

                        edit3.commit();


                        Fragment schemeLetterFragment = new SchemeLetterFragment();
                        FragmentTransaction ftscheme = getActivity().getSupportFragmentManager().beginTransaction();
                        ftscheme.replace(R.id.container_body, schemeLetterFragment);
                        ftscheme.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftscheme.addToBackStack(null);
                        ftscheme.commit();
                        //replaceFragment(new SchemeLetterFragment());
                        break;
                    case "Target Meter":
                        replaceFragment(TargetMeterCategoryFragment.newInstance(loginId));
                        break;
                    case "Change Password":
                        replaceFragment(new ChangePasswordFragment());
                        break;
                    case "Help":
                        replaceFragment(new HelpTabFragment());

                        break;
                    case "Points Calculator":
                        Log.e(TAG, "onItemClick: " + manuTxt);
                        replaceFragment(new WebViewPointCalculator());
                        break;
                    case "Campaign Rewards":
                        SharedPreferences preferences4 = getActivity().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        String login_id4 = preferences4.getString("usertrno", "");
                        Log.e("Login Id :", "Login Id in mechanic dashboard : " + login_id4);

                        SharedPreferences pref4 = getActivity().getSharedPreferences(
                                "userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit4 = pref4.edit();
                        int lid4 = Integer.parseInt("" + login_id4);
                        edit4.putInt("Mechanic_trno_to_redeem", lid4);

//                        edit2.putString("Mechanic_status", "rewards");

                        edit4.commit();


                        Fragment mechCampaignRewardsFragment = new MechCampaignRewardsFragment();
                        FragmentTransaction ftcampaign = getActivity().getSupportFragmentManager().beginTransaction();
                        ftcampaign.replace(R.id.container_body, mechCampaignRewardsFragment);
                        ftcampaign.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftcampaign.addToBackStack(null);
                        ftcampaign.commit();
                        // replaceFragment(new MechCampaignRewardsFragment());
                        break;
                    case "Tally Invoice Upload":
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

                        if (NetworkUtils.isNetworkAvailable(getActivity())) {
                            new GetUploadedIPLChampionStatus().execute(login_id5);
                        } else {
                            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                        }


                        // replaceFragment(new MechCampaignRewardsFragment());
                        break;
                    case "IPL Offer": //there is _ intended behaviour
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
                            new GetUploadedIPLChampionStatus().execute(login_id6);
//                            Fragment mechTallyInvoiceUploadFragment = new MechIPLCampaignUploadFragment();
//                            FragmentTransaction ftTallyInvoiceUpload = getActivity().getSupportFragmentManager().beginTransaction();
//                            ftTallyInvoiceUpload.replace(R.id.container_body, mechTallyInvoiceUploadFragment);
//                            ftTallyInvoiceUpload.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                            ftTallyInvoiceUpload.addToBackStack(null);
//                            ftTallyInvoiceUpload.commit();
                        } else {
                            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }
        });
        if (AppConfig.isSplashPopUpSessionActive)
            callSplashPopUpAPI();
        return view;
    }

    private void callSplashPopUpAPI() {

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            SharedPreferences preferences1 = getActivity().getSharedPreferences(
                    "signupdetails", Context.MODE_PRIVATE);
            String login_id = preferences1.getString("usertrno", "");
            new CallSplashPopUpAPI().execute(login_id);
        }

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
/*        // dashboardframe = (FrameLayout) view.findViewById(R.id.container_bodyindh);
        btn_myprofile = (ImageView) view.findViewById(R.id.btn_myprofile);
        btn_notification = (ImageView) view.findViewById(R.id.btn_notification);
        btn_transaction = (ImageView) view.findViewById(R.id.btn_transaction);
        // btn_claim_history = (ImageView) view.findViewById(R.id.btn_claim_history);
        btn_genuineverification = (ImageView) view.findViewById(R.id.btn_genuineverification);
        btn_rewards = (ImageView) view.findViewById(R.id.btn_rewards);
        btn_help = (ImageView) view.findViewById(R.id.btn_help);
        btn_aboutus = (ImageView) view.findViewById(R.id.btn_aboutus);*/
    }


    class SearchMechanic extends AsyncTask<String[], Void, JSONObject> {
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

                Log.e("data : ", " Mobile No : " + params[0][0]);
                jObj = new UserFunctions().GetMechaniceParticipant(loginId);
            } catch (Exception e) {
                // TODO: handle exception
                progressDialog.dismiss();
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

            Log.e("", "JSON : " + jObj);
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {

                       /* alertDialog2(
                                getResources().getString(R.string.app_name),
                                "Number Not Registered");*/
                        progressDialog.dismiss();
                    } else if (mStringStatus.equals("success")) {

                        JSONArray resultJArray = jObj.getJSONArray("particpant_data");
                        Log.e("particpant_data : ", "particpant_data : " + resultJArray);

                        JSONObject result = resultJArray.getJSONObject(0);
                        Log.e("result : ", "result : " + result);
                        circularList = new ArrayList<UserModel>();
                        UserModel _news = new UserModel();
                        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        strid = Integer.parseInt(preferences1.getString("usertrno", ""));
                        //     strid = Integer.parseInt(result.getString("login_id_pk"));
                        final String total_points = result.getString("earned_points");
                        _news.setTotal_point(result.getString("earned_points"));
                        final String points = result.getString("points");
                        _news.setBalance_points(result.getString("points"));
                        final String redeemed_points = result.getString("redeemed_points");
                        _news.setRedeem_point(result.getString("redeemed_points"));

                        circularList.add(_news);

                        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
                        try {
                            ctdUser.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (jObj.has("participant_claim_history")) {
                            ctdUser.deleteFromClaimHistory("" + strid);
                            ctdUser.insertClaimHistory(result, jObj.getJSONArray("participant_claim_history").toString(), "" + strid);
                            ctdUser.insertBulkClaimHistry(jObj.getJSONArray("participant_claim_history"), getActivity(), "" + strid);

                            ctdUser.close();
                        } else {
                            ctdUser.insertIntoHomework(result, "");
                            ctdUser.close();
                        }
                        progressDialog.dismiss();

                        new Handler().post(new Runnable() {
                            public void run() {

                                Fragment claimhitoryFragment = new ClaimHistoryFragment();
                                Bundle bundleclaim = new Bundle();

                                bundleclaim.putInt("participant_login_id", strid);
                                try {
                                    ctdUser.open();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                String part_code = ctdUser.getParticipantId(strid);
                                ctdUser.close();
                                bundleclaim.putString("participant_code", part_code);

                                bundleclaim.putString("action", action);
                                bundleclaim.putString("total_points", total_points);
                                bundleclaim.putString("redeem_points", redeemed_points);
                                bundleclaim.putString("balanced_points", points);
                                Log.e("part id :", "Part id in adapter : " + strid + " , " + part_code);
                                claimhitoryFragment.setArguments(bundleclaim);

                                FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                                ft1.replace(R.id.container_body, claimhitoryFragment);
                                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                ft1.addToBackStack(null);
                                ft1.commit();
                            }
                        });
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Log.e("Error : ", "Error : " + e.toString());
                }
            } // end onpostecxe
        }
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

            //  count=ctdUser1.getStatusCount();
            SharedPreferences preferences1 = AppConfig.getInstance().getSharedPreferences(
                    "signupdetails", Context.MODE_PRIVATE);

            mStringMobileNo = preferences1.getString("uname", "");


            count = ctdUser1.getStatusCount("" + mStringMobileNo);


            /*SharedPreferences preferences1 = getActivity().getSharedPreferences(
                    "signupdetails", Context.MODE_PRIVATE);

            mStringMobileNo=preferences1.getString("uname", "");


            count=ctdUser.getStatusCount(""+mStringMobileNo);

*/

            Log.e("notification mechcount", String.valueOf(count));
            ctdUser1.close();

//Toast.makeText(getApplicationContext(),MyConstants.eventID,Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }//Check_notification_count

    class CustomYesNoDialog extends Dialog implements
            android.view.View.OnClickListener {

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

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft4 = getFragmentManager().beginTransaction();
        ft4.replace(R.id.container_body, fragment);
        ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft4.addToBackStack(null);
        ft4.commit();
    }

    private void showSplashPopUpDialogs(List<SplashPopUpDetails> splashPopUpDetailsList) {

        startActivity(
                new Intent(getActivity(), SplashPopUpActivity.class)
                        .putParcelableArrayListExtra("splash_pop_up_list", (ArrayList<? extends Parcelable>) splashPopUpDetailsList)

                        .putExtra("isRoyalUser", false)
        );
        AppConfig.isSplashPopUpSessionActive = false;
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
            // super.onPreExecute();
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

                                Fragment mechTallyInvoiceUploadedfragment = new MechTallyInvoiceUploadedfragment();

                                mechTallyInvoiceUploadedfragment.setArguments(b);
                                FragmentTransaction ftmech = getActivity().getSupportFragmentManager().beginTransaction();
                                ftmech.replace(R.id.container_body, mechTallyInvoiceUploadedfragment);
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
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Fragment mechTallyInvoiceUploadFragment = new MechTallyInvoiceUploadFragment();
                        FragmentTransaction ftTallyInvoiceUpload = getActivity().getSupportFragmentManager().beginTransaction();
                        ftTallyInvoiceUpload.replace(R.id.container_body, mechTallyInvoiceUploadFragment);
                        ftTallyInvoiceUpload.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftTallyInvoiceUpload.addToBackStack(null);
                        ftTallyInvoiceUpload.commit();
                    }
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }

    class GetUploadedIPLChampionStatus extends AsyncTask<String, Void, JSONObject> {

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
            // super.onPreExecute();
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

                        if (jObj.getBoolean("invoice_flag")) {
                            Fragment mechTallyInvoiceUploadFragment = new MechIPLCampaignUploadFragment();
                            FragmentTransaction ftTallyInvoiceUpload = getActivity().getSupportFragmentManager().beginTransaction();
                            ftTallyInvoiceUpload.replace(R.id.container_body, mechTallyInvoiceUploadFragment);
                            ftTallyInvoiceUpload.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            ftTallyInvoiceUpload.addToBackStack(null);
                            ftTallyInvoiceUpload.commit();

                        } else {
                            CustomOKDialog cd = new CustomOKDialog(getActivity(),
                                    "" + jObj.getString("error"),
                                    getActivity().getResources().getString(R.string.app_name));
                            cd.show();
                        }


                    } else {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        CustomOKDialog cd = new CustomOKDialog(getActivity(),
                                "" + jObj.getString("error"),
                                getActivity().getResources().getString(R.string.app_name));
                        cd.show();

                    }
                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
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
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }


}
