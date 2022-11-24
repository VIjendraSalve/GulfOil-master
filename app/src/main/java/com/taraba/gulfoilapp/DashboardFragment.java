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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.taraba.gulfoilapp.adapter.CircularListAdapter;
import com.taraba.gulfoilapp.adapter.DashboardAdapter;
import com.taraba.gulfoilapp.contentproviders.UserModel;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.fragment.HelpTabFragment;
import com.taraba.gulfoilapp.fragment.SchemeLetterFragment;
import com.taraba.gulfoilapp.fragment.WhatsNewFragment;
import com.taraba.gulfoilapp.model.DashboardMenu;
import com.taraba.gulfoilapp.royalty_user_view.campaign_rewards.CampaignRewardsFragment;
import com.taraba.gulfoilapp.util.DataProvider;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.MyProfileFragment;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by android3 on 2/12/16.
 * Modified by Mansi
 */
public class DashboardFragment extends Fragment {
    /// FrameLayout dashboardframe;

    Button appDemo;
    GridView gv;
    private List<DashboardMenu> dashboardMenuList;
    Context context;
    String mStringAction;
    ArrayList prgmName;
    private ListView listView;
    private List<UserModel> circularList;
    private CircularListAdapter adapter;
    TextView txtMsg;
    int strid;
    String mStringMobileNo;
    UserTableDatasource ctdUser;

    private Thread r;
    Long time;
    int old_check = 0;
    int new_check = 0;
    public android.os.Handler mHandler;
    private static final String TAG = "DashboardFragment";

    public static String[] prgmNameList = {"  My Profile", "  New Registration", "   Notifications", "  Member Search", "  Genuine Verification", "  Rewards", "  Help", "   Change Password", "   Logout"};

    public static int[] prgmImages = {R.mipmap.my_profile_icon1, R.mipmap.new_registration_new,
            R.mipmap.notications_new, R.mipmap.member_search_new
            , R.mipmap.change_password1, R.mipmap.help_new, R.mipmap.view_rewards
    };

    ImageView imgvmyprofile, imgvnew_registration, imgvnotifications, imgvtransactions, imgvrewards,
            imgvgenuine_verification, imgvmember_search, imgvabout, imgvhelp;

    String action;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_dashboard, container, false);

        initComp(view);
        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });
        SharedPreferences preferences = (getActivity()).getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);

        action = preferences.getString("action", "");


        Log.e("", "action in Dashboard fragment:" + action);

        int check = Check_notification_count();
        DataProvider dataProvider = new DataProvider();
        dashboardMenuList = dataProvider.getRetailorDashboardMenuList(getActivity());
        gv = (GridView) view.findViewById(R.id.gridView1);
        appDemo = (Button) view.findViewById(R.id.btnappdemo);
        appDemo.setVisibility(View.GONE);
        appDemo.setEnabled(false);

        if (appDemo.equals("false")) // rewards from MSR/DSR login
        {
            appDemo.setEnabled(true);
            appDemo.setClickable(true);
            appDemo.setVisibility(View.INVISIBLE);
        } else   // rewards from mechanic login or from member search
        {
            appDemo.setEnabled(false);
            appDemo.setClickable(false);
            appDemo.setVisibility(View.GONE);
        }

        gv.setAdapter(new DashboardAdapter(view.getContext(), dashboardMenuList, check, "Participant"));


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

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("Position", "" + position);
                String menuName = dashboardMenuList.get(position).getLebal();
                switch (menuName) {
                    case "View Profile":

                        replaceFragment(new MyProfileFragment());
                        break;

                    case "Edit Registration":
                        SharedPreferences preferences = getActivity().getSharedPreferences(
                                "userinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();

                        edit.putString("Mechanic_status", "main");
                        edit.commit();

                        replaceFragment(new RetailerRegistrationFragment());
                        break;

                    case "":
                        Log.e("Tag ", " Tag : Notificsatioon");

                        replaceFragment(new ComingSoonFragment());
                        break;

                    case "Member Search":
                        SharedPreferences preferences_search = getActivity().getSharedPreferences("searchdetails", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor edit_search = preferences_search.edit();
                        edit_search.putString(MyTrackConstants._mStringSerchData, "");
                        edit_search.putString(MyTrackConstants._mStringSerchtype, "");
                        edit_search.putString("action", action);
                        edit_search.commit();

                        replaceFragment(new MechanicalSearchFragment());
                        break;
                    case "Change Password":

                        replaceFragment(new ChangePasswordFragment());
                        break;

                    case "Help":
                        Log.e("Tag ", " Tag : Help");
                        replaceFragment(new HelpTabFragment());
                        break;
                    case "View Rewards":
                        //int n = 1;
//                        if (n == 1) {
//                            com.taraba.gulfoilapp.customdialogs.CustomOKDialog cod = new com.taraba.gulfoilapp.customdialogs.CustomOKDialog(getActivity(), getString(R.string.window_cannot_open), "Gulf Oil");
//                            cod.show();
//                        } else {
                        Fragment mech2 = new RedeemMainFragment();

                        Bundle bundleRewards = new Bundle();
                        bundleRewards.putString("isDisable", "true");
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
                    //}
                    case "Target Meter":
                        Fragment fragment = new TargetMeterFragment();
                        replaceFragment(fragment);
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
                    case "App Demo":
                        replaceFragment(new AppdemoFragment());
                        break;
                    case "Points Calculator":
                        replaceFragment(new WebViewPointCalculator());
                        break;
                    case "Campaign Rewards":
                        replaceFragment(new CampaignRewardsFragment());
                        break;
                }
            }
        });

        return view;
    }

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

    public Long date_get() {
        Long time = null;
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "http://www.timeapi.org/utc/now";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date date = simpleDateFormat.parse(response);

                            TimeZone tz = TimeZone.getTimeZone("Israel");
                            SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            destFormat.setTimeZone(tz);

                            String result = destFormat.format(date);

                            Log.d(TAG, "onResponse: " + result.toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w(TAG, "onErrorResponse: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
        return time;

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
}
