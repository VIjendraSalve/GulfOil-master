package com.taraba.gulfoilapp.royalty_user_view.view_transaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.HelperNew.SharedPref;
import com.taraba.gulfoilapp.MainDashboardActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.ClaimHistoryModel;
import com.taraba.gulfoilapp.adapter.MyPointRewardListAdapter;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.model.KeyValue;
import com.taraba.gulfoilapp.model.THPeriodResponse;
import com.taraba.gulfoilapp.model.THRequest;
import com.taraba.gulfoilapp.model.THResponse;
import com.taraba.gulfoilapp.model.TranscationHistoryModel;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by android3 on 1/28/16.
 * Modified by Mansi
 * Modified by Chaitali on 09/02/2016
 * Transaction History webservice call
 */
public class MyPointsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "MyPointsFragment";
    TextView txtMsg, tvTHTitle;
    String strParticipnt_login_id;
    TextView txt_next, txt_previous, txt_first, txt_last;
    private boolean isFragmentOnCreateViewCall;
    TextView txt_mechanic_code, tv_retailor_id, total_earned_points, total_redeemed_points, total_balance;
    EditText edt_search;
    private ImageView ivPoints,
            ivRedeem,
            ivBalance;
    //int main_total = 0;
    int no_of_rows;
    private AppCompatSpinner spnYear;
    private RecyclerView rvTransactionHistory;
    LinearLayout lin_txt_pages;
    int size;
    UserTableDatasource ctdUser;
    String login_id;
    String action, strBalancedPoints, strTotalPoints, strRedeemPoints;
    // private ExpandableListView listView;
    private List<ClaimHistoryModel> claimHistoryList;
    private RoyaltyClaimHistoryListAdapter adapter;
    // TextView txt_total_points;
    private LinearLayout mLinearScroll;
    private List<ClaimHistoryModel> claimHistoryListTemp;
    private String total_earned_pointsStr = "", total_redeemed_pointsStr = "", total_balanceStr = "";
    private ProgressDialog progressDialog;
    private Disposable disposable;
    private SharedPreferences userPref;
    private List<String> yearsList;
    private String login_id_pk="";
    private Boolean isFromFLS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutUserWise(), container, false);
        isFragmentOnCreateViewCall = true;
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_my_points));
        }
        initComp(view);
        setIconUserWise();
        getTHPeriod();
//        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GulfOilUtils.callTollFree(getActivity());
//            }
//        });



        Bundle extras = getActivity().getIntent().getExtras();
        /*extras.getString("ITEM_NAME");*/

        isFromFLS = extras.getBoolean("isFromFLS", true);
        if(isFromFLS){
            login_id_pk = extras.getString("retailerLoginId");
            Log.d("isFromFLS", "isFromFLS: "+isFromFLS);
            Log.d("isFromFLS", "login_id_pk: "+ SharedPref.getPrefs(getActivity(), "flsID"));

        }

        SharedPreferences preferences = (getActivity()).getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);

        action = preferences.getString("action", "");

        SharedPreferences preferences1 = (getActivity()).getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);


//        Bundle bundle = this.getArguments();
        strParticipnt_login_id = preferences1.getString("usertrno", "");
        Log.e("strParticipnt_login_id ", "login_id: " + strParticipnt_login_id);
        // strParticipnt_login_id = 39;
//
//        if (bundle.getString("participant_code") == null) {
//            tv_retailor_id.setText("" + preferences1.getString("uname", ""));
//            lin_mech_code_clam.setVisibility(View.GONE);
//        } else {
//            lin_mech_code_clam.setVisibility(View.VISIBLE);
//            tv_retailor_id.setText("Retailer ID: " + bundle.getString("participant_code"));
//            txt_mechanic_code.setText("Dealer ID : " + preferences1.getString("uname", ""));
//        }

        login_id = String.valueOf(strParticipnt_login_id);
        Log.e("converted ", "login_id: " + login_id);
        claimHistoryList = new ArrayList<ClaimHistoryModel>();
        ctdUser = new UserTableDatasource(getActivity());
        ConnectionDetector cd = new ConnectionDetector(
                getActivity());

       /* if(action.equals("") || action==null)
        {
            action=bundle.getString("action","");
        }
         if(action.equals("participant"))
        {
            strTotalPoints = bundle.getString("total_points");
            strBalancedPoints = bundle.getString("balanced_points");
            strRedeemPoints = bundle.getString("redeem_points");

            llPoints.setVisibility(View.VISIBLE);
            vHLine1.setVisibility(View.VISIBLE);
            vHLine2.setVisibility(View.VISIBLE);

            TextView txt_balance_points = (TextView) view
                    .findViewById(R.id.edt_balance_points);
            TextView txt_total_points = (TextView) view
                    .findViewById(R.id.txt_total_points);
            TextView txt_reedem_points = (TextView) view
                    .findViewById(R.id.edt_redeem_points);


            if(strRedeemPoints.equals("null") || strRedeemPoints==null)
                strRedeemPoints="0";

            if(strTotalPoints.equals("null") || strTotalPoints==null)
                strTotalPoints="0";

            if(strBalancedPoints.equals("null") || strBalancedPoints==null)
                strBalancedPoints="0";
            txt_balance_points.setText(Html.fromHtml("<b>Balance UP </b><br/>" + strBalancedPoints));
            txt_total_points.setText(Html.fromHtml("<b>Total UP </b><br/>" +  strTotalPoints));
            txt_reedem_points.setText(Html.fromHtml("<b>Redeemed UP </b><br/>" +  strRedeemPoints));

        }
        else
        {
            llPoints.setVisibility(View.GONE);
            vHLine1.setVisibility(View.GONE);
            vHLine2.setVisibility(View.GONE);
        }


        Log.e("", "action in Claim History fragment:" + action);
      //  Toast.makeText(getActivity(), "action in claim history fragment:" + action, Toast.LENGTH_SHORT).show();



        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int height = point.y;

        int listheight = height - dpToPx(300);
        int row_height = dpToPx(40);

        no_of_rows = listheight / row_height;



        Log.e("no_of_rows :   ", " no_of_rows : " + no_of_rows);

        try {
            ctdUser.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        claimHistoryList = new ArrayList<ClaimHistoryModel>();
        claimHistoryListTemp = new ArrayList<ClaimHistoryModel>();

        *//**
         * create dynamic button according the size of array
         *//*

        String totptn = ctdUser.getTotalPoints(strParticipnt_login_id);

    //    txt_total_points.setText(Html.fromHtml("<b>Total Points </b><br/>" + totptn));
        //-------------------------------------------------

    //    Toast.makeText(getActivity(), "participant code:" + bundle.getString("participant_code"), Toast.LENGTH_SHORT).show();

        if (bundle.getString("participant_code") == null) {
            lin_mech_code_clam.setVisibility(View.GONE);
        } else {
            lin_mech_code_clam.setVisibility(View.VISIBLE);
            txt_mechanic_code.setText("Dealer ID : " + bundle.getString("participant_code"));
        }

        claimHistoryList = ctdUser.getClaimHistry(strParticipnt_login_id);
        setClaimHistory(claimHistoryList);

      txt_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_txt_pages.removeAllViews();
                if (size > 3) {
                    setFirstBottomLayout(size - 4, 2, 3);
                } else {
                    //  setFirstBottomLayout(size, 2, size);
                }

                txt_last.setVisibility(View.GONE);
                txt_next.setVisibility(View.GONE);
                txt_first.setVisibility(View.VISIBLE);
                txt_previous.setVisibility(View.VISIBLE);
            }
        });

      txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int txt_id = 0;
                int active_page_no = 0;
                for (int i = 0; i < lin_txt_pages.getChildCount(); i++) {
                    if (((TextView) lin_txt_pages.getChildAt(i)).getCurrentTextColor() == getResources().getColor(R.color.selected_textcolor)) {
                        Log.e("i ", "i : " + i);
                        active_page_no = i;
                        txt_id = ((TextView) lin_txt_pages.getChildAt(i)).getId();
                        break;
                    }
                }
                Log.e("i ", "txt_id : " + txt_id + " , active_page_no : " + active_page_no);
                if ((txt_id + 3) < size) {
                    if (active_page_no < 1) {
                        lin_txt_pages.removeAllViews();
                        Log.e("In txt_next", "Txt Next : " + (txt_id - 1) + ", " + (active_page_no + 1));
                        if(size>3)
                        {
                            setFirstBottomLayout((txt_id - 1), (active_page_no + 1), 3);
                        }
                        else
                        {

                        }

                        txt_first.setVisibility(View.VISIBLE);
                        txt_previous.setVisibility(View.VISIBLE);
                    } else {
                        active_page_no = 0;
                        lin_txt_pages.removeAllViews();
                        Log.e("In txt_next", "Txt Next : " + (txt_id) + ", " + active_page_no);

                        if(size>3)
                        {
                            setFirstBottomLayout((txt_id), (active_page_no), 3);
                        }
                        else
                        {

                        }

                        txt_first.setVisibility(View.VISIBLE);
                        txt_previous.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (active_page_no < 1) {
                        lin_txt_pages.removeAllViews();
                        Log.e("In txt_next", "Txt Next : " + (txt_id) + ", " + active_page_no);

                        if(size>3)
                        {
                            setFirstBottomLayout((size - 4), (active_page_no + 1), 3);
                        }
                        else
                        {

                        }
                        txt_last.setVisibility(View.GONE);
                    } else {
                        lin_txt_pages.removeAllViews();
                        Log.e("In txt_next", "Txt Next : " + (txt_id) + ", " + active_page_no);

                        if(size>3)
                        {
                            setFirstBottomLayout((size - 4), 2, 3);
                        }
                        else
                        {

                        }
                        txt_last.setVisibility(View.GONE);
                        txt_next.setVisibility(View.GONE);
                    }
                }
            }
        });

        txt_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_txt_pages.removeAllViews();

                if(size>3)
                {
                    setFirstBottomLayout(0, 0, 3);
                }
                else
                {

                }

                txt_first.setVisibility(View.GONE);
                txt_previous.setVisibility(View.GONE);
                txt_last.setVisibility(View.VISIBLE);
                txt_next.setVisibility(View.VISIBLE);
            }
        });
   txt_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int active_page_no = 0;
                int txt_id = 0;
                for (int i = 0; i < lin_txt_pages.getChildCount(); i++) {
                    if (((TextView) lin_txt_pages.getChildAt(i)).getCurrentTextColor() == getResources().getColor(R.color.selected_textcolor)) {
                        active_page_no = i;
                        txt_id = ((TextView) lin_txt_pages.getChildAt(i)).getId();
                        break;
                    }
                }

                if ((txt_id - 4) >= 0) {
                    if ((active_page_no) > 1) {
                        lin_txt_pages.removeAllViews();
total_points
                        if(size>3)
                        {
                            setFirstBottomLayout((txt_id - 3), active_page_no - 1, 3);
                        }
                        else
                        {

                        }
                        //addItem(btnPage.getId());
                        txt_last.setVisibility(View.VISIBLE);
                        txt_next.setVisibility(View.VISIBLE);
                    } else {
                        active_page_no = 2;
                        lin_txt_pages.removeAllViews();

                        if(size>3)
                        {
                            setFirstBottomLayout((txt_id - 4), active_page_no, 3);
                        }
                        else
                        {

                        }
                        //addItem(btnPage.getId());
                        txt_last.setVisibility(View.VISIBLE);
                        txt_next.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (active_page_no > 1) {
                        lin_txt_total_pointspages.removeAllViews();
                        Log.e("In txt_next", "Txt Next : " + (txt_id) + ", " + active_page_no);

                        if(size>3)
                        {
                            setFirstBottomLayout(0, (active_page_no - 1), 3);
                        }
                        else
                        {

                        }
                        txt_first.setVisibility(View.GONE);

                    } else {
                        lin_txt_pages.removeAllViews();
                        Log.e("In txt_next", "Txt Next : " + (txt_id) + ", " + active_page_no);

                        if(size>3)
                        {
                            setFirstBottomLayout(0, 0, 3);
                        }
                        else
                        {

                        }
                        txt_first.setVisibility(View.GONE);
                        txt_previous.setVisibility(View.GONE);
                    }

                }
            }
        });

       edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                claimHistoryList.clear();
                try {
                    ctdUser.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                claimHistoryList = ctdUser.getSearchClaimHistory(strParticipnt_login_id, "" + s);
                setClaimHistory(claimHistoryList);
                    ctdUser.close();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/
        return view;
    }//End of oncreateView

 /*   public void setClaimHistory(List<ClaimHistoryModel> claimHistoryList) {

        listView.deferNotifyDataSetChanged();
        if (lin_txt_pages.getChildCount() <= 0) {

        } else {
            lin_txt_pages.removeAllViews();
        }

        Log.e(" Data :", "claimHistoryList :" + claimHistoryList.size() + " " + claimHistoryList);
        if (claimHistoryList.size() <= 0) {
            txtMsg.setVisibility(View.VISIBLE);
        } else {
            txtMsg.setVisibility(View.GONE);
            addItem(0);
        }

        //-------------------------------------------------_
        int rem = claimHistoryList.size() % no_of_rows;
        size = claimHistoryList.size() / no_of_rows;

        Log.e("", "size-------:" + size);
        Log.e("rem and size", "rem : " + rem + " , size : " + size);
        if (rem > 0) {
            size = size + 2;
            Log.e("", "size in if:" + size);
            *//*for (int i = 0; i < no_of_rows - rem; i++) {
                //claimHistoryList.add(claimHistoryListTemp);
                claimHistoryList.add(i, claimHistoryList.get(i));
            }*//*
        } else {
            size = size + 1;
        }

        Log.e("claimHistoryList :   ", ".claimHistoryList : " + claimHistoryList.size());
        Log.e("Size :   ", "Size : " + size);
        if (size > 0) {
            if (size > 3) {
                Log.e("", "in if size>3");
                setFirstBottomLayout(0, 0, 3);
                txt_first.setVisibility(View.GONE);
                txt_previous.setVisibility(View.GONE);
                txt_last.setVisibility(View.VISIBLE);
                txt_next.setVisibility(View.VISIBLE);
            } else {
                if (rem > 0 && (claimHistoryList.size() / no_of_rows) == 1) {
                    size = size - 1;

                }
                Log.e("", "in else size<=3");
                setFirstBottomLayout(0, 0, size);
                txt_first.setVisibility(View.GONE);
                txt_previous.setVisibility(View.GONE);
                if (size == 1) {
                    Log.e("", "in if size<=3 size==1");
                    txt_last.setVisibility(View.GONE);
                    txt_next.setVisibility(View.GONE);
                } else if (size == 2) {
                    txt_next.setVisibility(View.GONE);
                    txt_last.setVisibility(View.GONE);
                } else {

                    Log.e("", "in else size<=3 size!=1");
                    txt_last.setVisibility(View.GONE);
                    txt_next.setVisibility(View.VISIBLE);

                }
            }
        } else {
            Log.e("", "in else size<=0");
            txt_first.setVisibility(View.GONE);
            txt_previous.setVisibility(View.GONE);
            txt_last.setVisibility(View.GONE);
            txt_next.setVisibility(View.GONE);
        }
    }*/

    public void setFirstBottomLayout(int page_no, int page_active, int size_pages) {
        final int page_no_set = page_no;
        int j = 0;
        for (; j < size_pages; j++) {
            // int k;
            final TextView btnPage = new TextView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 2, 5, 2);
            btnPage.setTextColor(Color.WHITE);
            btnPage.setTextSize(17.0f);
            btnPage.setPadding(10, 0, 10, 0);

            Log.e("Page no : ", "Page No : " + page_no);
            int id = (page_no + 1);
            btnPage.setId(page_no + 1);
            Log.e("Page no ", "Id : " + id);
            btnPage.setText("  " + id + "  ");
            lin_txt_pages.addView(btnPage, lp);
            page_no = page_no + 1;
            btnPage.setBackgroundResource(R.drawable.pagination_bg);
            final int k = j;
            Log.e("Page no ", "Page active : " + page_active);
            Log.e("Page : ", "Page No Set : " + page_no_set + " ,  k : " + k);
            if (j == page_active) {
                btnPage.setTextColor(getResources().getColor(R.color.selected_textcolor));
                addItem(btnPage.getId() - 1);
            } else {
                btnPage.setTextColor(getResources().getColor(R.color.unselected_textcolor));
            }


            btnPage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    lin_txt_pages.removeAllViews();
                    Log.e("Page : ", "Page No Set : " + page_no_set + " ,  k : " + k);
                   /* if(k==2)
                    {
                        if(((page_no    _set-3)>=0)) {
                            setFirstBottomLayout((page_no_set + 2), 0);
                        } else if((page_no_set+4)<=(size)){
                            setFirstBottomLayout((page_no_set), 2);
                        } else {
                            setFirstBottomLayout((page_no_set), k);
                        }
                    }
                    else
                    {*/
                    if (size > 3) {
                        setFirstBottomLayout(page_no_set, k, 3);
                    } else {
                        setFirstBottomLayout(0, k, size);

                        txt_next.setVisibility(View.GONE);
                    }

                    //  }

                    addItem(btnPage.getId() - 1);
                }
            });
        }
    }

    public void initComp(View view) {
        //  txt_total_points = (TextView) view.findViewById(R.id.txt_total_points);
        userPref = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
//        listView = view.findViewById(R.id.lstClaimHistory);
        spnYear = view.findViewById(R.id.spnYear);
        tvTHTitle = view.findViewById(R.id.tvTHTitle);
        ivPoints = view.findViewById(R.id.ivPoints);
        tv_retailor_id = view.findViewById(R.id.tv_retailor_id);
        tv_retailor_id.setText("" + userPref.getString("username", ""));
        ivRedeem = view.findViewById(R.id.ivRedeem);
        ivBalance = view.findViewById(R.id.ivBalance);

        rvTransactionHistory = view.findViewById(R.id.rvTransactionHistory);
        spnYear.setOnItemSelectedListener(this);
        txtMsg = view.findViewById(R.id.txt_msgclaimhistory);

        mLinearScroll = view.findViewById(R.id.linear_scroll);

        lin_txt_pages = view.findViewById(R.id.linear_text);
        txt_first = view.findViewById(R.id.btn_first);
        txt_last = view.findViewById(R.id.btn_last);
        txt_next = view.findViewById(R.id.btn_next);
        txt_previous = view.findViewById(R.id.btn_previous);
        txt_mechanic_code = view.findViewById(R.id.txt_mechanic_code);
        edt_search = view.findViewById(R.id.edt_search);

        total_earned_points = view.findViewById(R.id.total_earned_points);
        total_redeemed_points = view.findViewById(R.id.total_redeemed_points);
        total_balance = view.findViewById(R.id.total_balance);

    }

    private void setYearAdapter(THPeriodResponse response) {
//        List<String> years = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            years.add("202" + i + "-202" + (i + 1));
//        }
        yearsList = response.data;
        spnYear.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item_small, response.data));

    }

    //create dynamic temp array list from main-list
    public void addItem(int count) {
        claimHistoryListTemp.clear();
        count = count * no_of_rows;
        /**
         * fill temp array list to set on page change
         */
        int remaining_rows = 0;

        int rem = claimHistoryList.size() % no_of_rows;
        if (rem > 0) {
            remaining_rows = rem;
        }
        int rem_ros = claimHistoryList.size() - count;
        Log.e("Log ", " Count : " + count + " , rem : " + rem + " , ");
        if (claimHistoryList.size() <= no_of_rows) {
            for (int j = 0; j < claimHistoryList.size(); j++) {
                claimHistoryListTemp.add(j, claimHistoryList.get(j));
                // count = count + 1;
            }
        } else {
            if (rem_ros < no_of_rows) {
                Log.e("------------------", "---------------This is last Pag e : ");
                for (int j = 0; j < rem_ros; j++) {
                    claimHistoryListTemp.add(j, claimHistoryList.get(count));
                    count = count + 1;
                }
            } else {
                for (int j = 0; j < no_of_rows; j++) {
                    claimHistoryListTemp.add(j, claimHistoryList.get(count));
                    count = count + 1;
                }
            }

        }

        // set view
        setView();

    }

    public void setView() {
//        adapter = new RoyaltyClaimHistoryListAdapter(getActivity(), claimHistoryList);
//        listView.setAdapter(adapter);
//        txt_main_total.setText("" + total_balanceStr);
        //adapter.notifyDataSetChanged();

        total_balance.setText(Html.fromHtml("" + total_balanceStr));
        total_earned_points.setText(Html.fromHtml("" + total_earned_pointsStr));
        total_redeemed_points.setText(Html.fromHtml("" + total_redeemed_pointsStr));
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    class GetTransactionHistory extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;


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
                Log.e("data : ", " Login id : " + params[0][0]);
                jObj = new UserFunctions().Transaction_webservice_call(params[0][0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    Log.e("transaction", "status: " + mStringStatus);

                    total_earned_pointsStr = jObj.getString("earned_points");
                    Log.e("transaction", "total_earned_pointsStr: " + total_earned_pointsStr);
                    total_redeemed_pointsStr = jObj.getString("redeemed_points");
                    Log.e("transaction", "total_redeemed_pointsStr: " + total_redeemed_pointsStr);
                    total_balanceStr = jObj.getString("total_balance");
                    Log.e("transaction", "total_balanceStr: " + total_balanceStr);

                    if (mStringStatus.equals("success")) {
                        try {
                            ctdUser.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        ctdUser.deleteClaimHistory();

                        ctdUser.insertBulkClaimHistry(jObj.getJSONArray("transaction_history"), getActivity(), login_id);
                        //  String strJson="[{  \"total_points\": \"272\",\"type\": \"base\", \"month\": \"2016-07-01\"},{ \"total_points\": \"171\",\"type\": \"base\",  \"month\": \"2016-09-01\"},{ \"total_points\": \"112\",\"type\": \"base\",  \"month\": \"2016-10-01\"},{ \"total_points\": \"6\",\"type\": \"bonus\",  \"month\": \"2016-10-01\"},{ \"total_points\": \"465\",\"type\": \"base\",  \"month\": \"2016-11-01\"}]";
                        //  JSONArray jsonArray = new JSONArray(strJson);
                        //  Log.e("test jsonArray",""+jsonArray.toString());
                        //  ctdUser.insertBulkClaimHistry(jsonArray,getActivity(), login_id);
                        // ctdUser.insertBulkClaimHistry([{"total_points"/: "272","type": "base", "month": "2016-07-01"}]);

                        claimHistoryList = ctdUser.getClaimHistry(strParticipnt_login_id);
                        //setClaimHistory(claimHistoryList);
                        Log.e("from", "DB: " + claimHistoryList.size());
                        //   main_total = 0;
                        for (ClaimHistoryModel claimHistoryModel : claimHistoryList) {
                            int single_total = 0, base_point = 0, bonus_point = 0;
                            if (claimHistoryModel.getClaim_uid_status() == null || claimHistoryModel.getClaim_uid_status().equals("")) {
                                // txtStatus.setText("-");
                                base_point = 0;
                                Log.e("frg_base_point: ", "" + base_point);
                            } else {
                                //txtStatus.setText("" + t);
                                base_point = Integer.parseInt(claimHistoryModel.getClaim_uid_status());
                                Log.e("frg_base_point: ", "" + base_point);
                            }
                            if (claimHistoryModel.getClaim_point() == null
                                    || claimHistoryModel.getClaim_point().equals("")) {
                                // txtPoints.setText("-");
                                bonus_point = 0;
                                Log.e("frg_bonus_point: ", "" + bonus_point);
                            } else {
                                //txtPoints.setText("" + values.get(position).getClaim_point());
                                bonus_point = Integer.parseInt(claimHistoryModel.getClaim_point());
                                Log.e("frg_bonus_point: ", "" + bonus_point);
                            }
                            single_total = base_point + bonus_point;
                            Log.e("frg_main frg", "single_total: " + single_total);
//                            main_total = main_total + single_total;
//                            Log.e("frg_main frg", "main_toatl: " + main_total);
                        }
                        setView();
                        /*JSONArray jarray = jObj.getJSONArray("transaction_history");
                        Log.e("transaction", "array len: "+jarray.length());
                        if (jarray.length()!=0){
                            for (int i=0; i<jarray.length(); i++){
                                JSONObject obj = jarray.getJSONObject(i);
                                String total_points = obj.getString("total_points");
                                Log.e("total_points",": "+total_points);
                                String type = obj.getString("type");
                                Log.e("type",": "+type);
                                String month = obj.getString("month");
                                Log.e("month",": "+month);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                try {
                                    Date date = sdf.parse(month);
                                    String formated = new SimpleDateFormat("MMM").format(date);
                                    Log.e("format","mnth : "+formated);
                                    //int month1 =  Integer.parseInt(formated);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }*/
                    } else {
                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jObj.getString("error"), "Gulf Oil");
                        cdd.show();
                        Log.e("No", "History");
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (isFragmentOnCreateViewCall) {
            isFragmentOnCreateViewCall = false;
        } else {
            callTransactionHistory(spnYear.getSelectedItem().toString());
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getTHPeriod() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            Log.e(TAG, "callYDRAPI: Unnati Fragment");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getTHPeriod()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::thPeriodResponse, this::thPerodError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void thPerodError(Throwable throwable) {
        progressDialog.dismiss();
        callTransactionHistory("");
        Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void thPeriodResponse(THPeriodResponse response) {
        progressDialog.dismiss();
        callTransactionHistory("");
        if (ServiceBuilder.isSuccess(response.status)) {
            setYearAdapter(response);
        }

    }


    private void callTransactionHistory(String year) {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            Log.e(TAG, "callYDRAPI: Unnati Fragment");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            THRequest request = new THRequest();
            UserType userType = new GulfOilUtils().getUserType();
            if (userType == UserType.ROYAL) {
               // request.setFls_login_id(userPref.getString("usertrno", ""));
                request.setPeriod(year);
                request.setLogin_id(userPref.getString("usertrno", ""));
            } else if (userType == UserType.ELITE) {
                request.setPeriod(year);
                request.setLogin_id(userPref.getString("usertrno", ""));
            } else if (userType == UserType.CLUB) {
                request.setPeriod(year);
                request.setLogin_id(userPref.getString("usertrno", ""));
            } else {
                request.setFls_login_id(userPref.getString("usertrno", ""));
                request.setPeriod(year);
                request.setLogin_id(SharedPref.getPrefs(getActivity(), "flsID"));
            }




            Log.d(TAG, "callTransactionHistory: "+SharedPref.getPrefs(getActivity(), "flsID"));
            Log.d(TAG, "callTransactionHistory: "+request.getLogin_id());
            Log.d(TAG, "callTransactionHistory: "+userPref.getString("usertrno", ""));
            Log.d(TAG, "callTransactionHistory: "+request.getPeriod());
            disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .getTH(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::thResponse, this::thError);
        } else {
            Toast.makeText(getActivity(), getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void thError(Throwable throwable) {
        progressDialog.dismiss();
        Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
    }

    private void thResponse(THResponse response) {
        progressDialog.dismiss();
        if (ServiceBuilder.isSuccess(response.getStatus())) {
            setData(response);
        }
    }

    private void setData(THResponse response) {
        tvTHTitle.setText(response.getData().transaction_history_label);
        total_balance.setText(Html.fromHtml("" + response.getData().total_balance));
        total_earned_points.setText(Html.fromHtml("" + response.getData().earned_points));
        total_redeemed_points.setText(Html.fromHtml("" + response.getData().redeemed_points));
        if (yearsList != null && yearsList.size() > 0) {
            for (int i = 0; i < yearsList.size(); i++) {
                if (yearsList.get(i).equals(response.getData().period)) {
                    spnYear.setSelection(i);
                }
            }
        }
        setTransactionHistoryAdapter(response);
    }

    private void setTransactionHistoryAdapter(THResponse response) {
        List<TranscationHistoryModel> thList = new ArrayList<>();

        TranscationHistoryModel aprilTHModel = new TranscationHistoryModel();
        aprilTHModel.setMonth("April");
        List<KeyValue> aprilKeyValueList = new ArrayList<>();
        for (int i = 0; i < response.getData().transaction_history.april.size(); i++) {
            String key = response.getData().transaction_history.april.get(i).key;
            String value = response.getData().transaction_history.april.get(i).value;
            aprilKeyValueList.add(new KeyValue(key, value));

        }
        aprilTHModel.setKeyValueList(aprilKeyValueList);
        thList.add(aprilTHModel);

        TranscationHistoryModel mayTHModel = new TranscationHistoryModel();
        mayTHModel.setMonth("May");
        List<KeyValue> mayKeyValueList = new ArrayList<>();
        for (int i = 0; i < response.getData().transaction_history.may.size(); i++) {
            String key = response.getData().transaction_history.may.get(i).key;
            String value = response.getData().transaction_history.may.get(i).value;
            mayKeyValueList.add(new KeyValue(key, value));

        }
        mayTHModel.setKeyValueList(mayKeyValueList);
        thList.add(mayTHModel);

        TranscationHistoryModel juneTHModel = new TranscationHistoryModel();
        juneTHModel.setMonth("June");
        List<KeyValue> juneKeyValueList = new ArrayList<>();
        for (int i = 0; i < response.getData().transaction_history.june.size(); i++) {
            String key = response.getData().transaction_history.june.get(i).key;
            String value = response.getData().transaction_history.june.get(i).value;
            juneKeyValueList.add(new KeyValue(key, value));

        }
        juneTHModel.setKeyValueList(juneKeyValueList);
        thList.add(juneTHModel);

        TranscationHistoryModel julyTHModel = new TranscationHistoryModel();
        julyTHModel.setMonth("July");
        List<KeyValue> julyKeyValueList = new ArrayList<>();
        for (int i = 0; i < response.getData().transaction_history.july.size(); i++) {
            String key = response.getData().transaction_history.july.get(i).key;
            String value = response.getData().transaction_history.july.get(i).value;
            julyKeyValueList.add(new KeyValue(key, value));

        }
        julyTHModel.setKeyValueList(julyKeyValueList);
        thList.add(julyTHModel);

        TranscationHistoryModel augTHModel = new TranscationHistoryModel();
        augTHModel.setMonth("August");
        List<KeyValue> augKeyValueList = new ArrayList<>();
        for (int i = 0; i < response.getData().transaction_history.august.size(); i++) {
            String key = response.getData().transaction_history.august.get(i).key;
            String value = response.getData().transaction_history.august.get(i).value;
            augKeyValueList.add(new KeyValue(key, value));

        }
        augTHModel.setKeyValueList(augKeyValueList);
        thList.add(augTHModel);

        TranscationHistoryModel sepTHModel = new TranscationHistoryModel();
        sepTHModel.setMonth("September");
        List<KeyValue> sepKeyValueList = new ArrayList<>();
        for (int i = 0; i < response.getData().transaction_history.september.size(); i++) {
            String key = response.getData().transaction_history.september.get(i).key;
            String value = response.getData().transaction_history.september.get(i).value;
            sepKeyValueList.add(new KeyValue(key, value));

        }
        sepTHModel.setKeyValueList(sepKeyValueList);
        thList.add(sepTHModel);

        TranscationHistoryModel octTHModel = new TranscationHistoryModel();
        octTHModel.setMonth("October");
        List<KeyValue> octKeyValueList = new ArrayList<>();
        for (int i = 0; i < response.getData().transaction_history.october.size(); i++) {
            String key = response.getData().transaction_history.october.get(i).key;
            String value = response.getData().transaction_history.october.get(i).value;
            octKeyValueList.add(new KeyValue(key, value));

        }
        octTHModel.setKeyValueList(octKeyValueList);
        thList.add(octTHModel);

        TranscationHistoryModel novTHModel = new TranscationHistoryModel();
        novTHModel.setMonth("November");
        List<KeyValue> novKeyValueList = new ArrayList<>();
        for (int i = 0; i < response.getData().transaction_history.november.size(); i++) {
            String key = response.getData().transaction_history.november.get(i).key;
            String value = response.getData().transaction_history.november.get(i).value;
            novKeyValueList.add(new KeyValue(key, value));

        }
        novTHModel.setKeyValueList(novKeyValueList);
        thList.add(novTHModel);

        TranscationHistoryModel decTHModel = new TranscationHistoryModel();
        decTHModel.setMonth("December");
        List<KeyValue> decKeyValueList = new ArrayList<>();
        for (int i = 0; i < response.getData().transaction_history.december.size(); i++) {
            String key = response.getData().transaction_history.december.get(i).key;
            String value = response.getData().transaction_history.december.get(i).value;
            decKeyValueList.add(new KeyValue(key, value));

        }
        decTHModel.setKeyValueList(decKeyValueList);
        thList.add(decTHModel);

        TranscationHistoryModel janTHModel = new TranscationHistoryModel();
        janTHModel.setMonth("January");
        List<KeyValue> janKeyValueList = new ArrayList<>();
        for (int i = 0; i < response.getData().transaction_history.january.size(); i++) {
            String key = response.getData().transaction_history.january.get(i).key;
            String value = response.getData().transaction_history.january.get(i).value;
            janKeyValueList.add(new KeyValue(key, value));

        }
        janTHModel.setKeyValueList(janKeyValueList);
        thList.add(janTHModel);

        TranscationHistoryModel febTHModel = new TranscationHistoryModel();
        febTHModel.setMonth("February");
        List<KeyValue> febKeyValueList = new ArrayList<>();
        for (int i = 0; i < response.getData().transaction_history.february.size(); i++) {
            String key = response.getData().transaction_history.february.get(i).key;
            String value = response.getData().transaction_history.february.get(i).value;
            febKeyValueList.add(new KeyValue(key, value));

        }
        febTHModel.setKeyValueList(febKeyValueList);
        thList.add(febTHModel);

        TranscationHistoryModel marTHModel = new TranscationHistoryModel();
        marTHModel.setMonth("March");
        List<KeyValue> marKeyValueList = new ArrayList<>();
        for (int i = 0; i < response.getData().transaction_history.march.size(); i++) {
            String key = response.getData().transaction_history.march.get(i).key;
            String value = response.getData().transaction_history.march.get(i).value;
            marKeyValueList.add(new KeyValue(key, value));

        }
        marTHModel.setKeyValueList(marKeyValueList);
        thList.add(marTHModel);

        rvTransactionHistory.setAdapter(new MyPointRewardListAdapter(getActivity(), thList));

    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_my_points_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_my_points_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_my_points_club;
        else
            return R.layout.fragment_my_points_royal;
    }

    private void setIconUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        int point_bg = 0;
        int balance_bg = 0;
        int redeem_bg = 0;
        if (userType == UserType.ROYAL) {
            point_bg = R.drawable.royal_point_bg;
            balance_bg = R.drawable.royal_magic_bonanza_bg;
            redeem_bg = R.drawable.royal_milestone_bg;
        } else if (userType == UserType.ELITE) {
            point_bg = R.drawable.elite_point_bg;
            balance_bg = R.drawable.elite_balance_bg;
            redeem_bg = R.drawable.elite_redeem_bg;
        } else if (userType == UserType.CLUB) {
            point_bg = R.drawable.club_point_bg;
            balance_bg = R.drawable.club_balance_bg;
            redeem_bg = R.drawable.club_redeem_bg;
        } else {
            point_bg = R.drawable.royal_point_bg;
            balance_bg = R.drawable.royal_magic_bonanza_bg;
            redeem_bg = R.drawable.royal_milestone_bg;
        }

        ivPoints.setImageDrawable(getActivity().getResources().getDrawable(point_bg));
        ivRedeem.setImageDrawable(getActivity().getResources().getDrawable(redeem_bg));
        ivBalance.setImageDrawable(getActivity().getResources().getDrawable(balance_bg));


    }
}
