package com.taraba.gulfoilapp.royalty_user_view.order_confirm_screen;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;
import com.taraba.gulfoilapp.MainDashboardActivity;
import com.taraba.gulfoilapp.MyTrackConstants;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.Product;
import com.taraba.gulfoilapp.royalty_user_view.order_confirm_screen.OrderConfirmDetailsFragmentDirections.ActionOrderConfirmationDetailsFragmentToProceedOrderOTPFragment;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Created by Mansi on 3/14/16.
 * Modified by Mansi
 */
public class OrderConfirmDetailsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "OrderConfirmDetailsFrag";
    //    ImageView iv_product;
//    TextView tv_name, tv_code, tv_points, txt_bonus_desc;
//    GridView gv_bonusDetails;
//    Button btn_confirm_order;
    String product_code;
    SharedPreferences PREF_participant_login_id;
    String isDisable = "";
    String bonus_products = "";
    String strdateOrg = "", currentdate;
    //WebView v;
    //private ImageView tv_minus, tv_plus;
    //private TextView tv_qty, tv_total_point_value;
    private String currentRedeemPoints;
    //private RelativeLayout rl_multi_qty;
    private TextView
            tvRewardName,
            tvPoints,
            tvCode,
            tvTdsValue,
            tv_tds_percent,
            tvRewardValue,
            tvConfirmOrderDescription, tvTotalPoints;
    //            tvConfirmOrderTermAndCondition;
    private ImageView ivProduct;
    private AppCompatSpinner spnQuantity;
    private AppCompatButton btnPlaceOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutUserWise(), container, false);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_order_details));
        }
        initComp(view);
        //Call to toll free number
//        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GulfOilUtils.callTollFree(getActivity());
//            }
//        });
        Bundle b = getArguments();
        currentRedeemPoints = b.getString("currentRedeemPoints");
        Log.e(TAG, "onCreateView: Current Redeem Points" + currentRedeemPoints);
        Log.e(TAG, "isDisable Flag" + isDisable);
        isDisable = b.getString("isDisable");
        product_code = b.getString(MyTrackConstants._mStringProductCode);


        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                "signupdetails", getActivity().MODE_PRIVATE);

        String user_type = preferences1.getString("user_type", "");
        if(user_type.toLowerCase().equals("retailer")){
            btnPlaceOrder.setVisibility(View.VISIBLE);
        }else {
            btnPlaceOrder.setVisibility(View.GONE);
        }

       /* if (isDisable.equals("true")) {
            //btn_confirm_order.setVisibility(View.GONE);
            btnPlaceOrder.setVisibility(View.GONE);
            //rl_multi_qty.setVisibility(View.GONE);
        } else {
            //btn_confirm_order.setVisibility(View.VISIBLE);
//            rl_multi_qty.setVisibility(View.VISIBLE);
            btnPlaceOrder.setVisibility(View.VISIBLE);
        }*/

        SharedPreferences preferences = getActivity().getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);

        int mech_trno = preferences.getInt("Mechanic_trno_to_redeem", 0);
        Log.e("", "Mechanic TRNO : " + mech_trno);

        Log.e("", "Product code in grid redeem details:-----------------############ " + product_code);
        final ConnectionDetector cd = new ConnectionDetector(
                getActivity());

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            new GetProductDetails().execute();

        } else {

            final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
            try {
                ctdUser.open();
                String prod_details = ctdUser.getSelectedProductDetails(product_code);


                JSONArray arr = new JSONArray(prod_details);
                JSONObject obj = arr.getJSONObject(0);
                String detail_code = obj.optString("product_code");
                String TdsValue = obj.optString("tds_value");
                String rewardValue = obj.optString("rewards_value");
                String TdsValuePercent = obj.optString("tds_percent");
                String detail_name = obj.optString("name");
                String detail_large_desc = obj.optString("large_description");
                String detail_point = obj.optString("points");
                String detail_img = obj.optString("large_image_link");
                tvRewardName.setText("detail name " + detail_name);
                tvCode.setText(detail_code);
                tvPoints.setText(detail_point);
                tvTdsValue.setText(TdsValue);
                tvRewardValue.setText(rewardValue);
                tv_tds_percent.setText("TDS ("+TdsValuePercent+" %)  :  ");
                tvConfirmOrderDescription.setText(GulfOilUtils.getHtmlText(detail_large_desc));
                //tv_small_desc.setText(Html.fromHtml(""+detail_large_desc));

                String text = "<html><body style=\"text-align:justify;color: #ba8e57; background-color: #000;\"> %s </body></Html>";

                // v.loadData(String.format(text, "" + detail_large_desc), "text/html", "utf-8");
                Picasso.with(getActivity())
                        .load(detail_img)
                        .resize(480, 300)                                // optional
                        .into(ivProduct);

                ArrayList<Product> bonus = new ArrayList<Product>();

                String bonus_detals = ctdUser.getBonus_details(product_code);

                ctdUser.close();

                Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
            } catch (NullPointerException e) {
                Toast.makeText(getActivity(), "Please enable your internet", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        // calling Url

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBalance(spnQuantity.getSelectedItemPosition())) {
                    if (NetworkUtils.isNetworkAvailable(getActivity())) {
                        btnPlaceOrder.setEnabled(false);
                        new Handler().postDelayed(() -> btnPlaceOrder.setEnabled(true), 2000    //Specific time in milliseconds
                        );
                        Log.e(TAG, "onClick: NewRedemtionDetailsTask");

                        NewRedemtionDetailsTask redemtionDetailsTask = new NewRedemtionDetailsTask();
                        redemtionDetailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB) {
//                        redemtionDetailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                    }else{
//                        redemtionDetailsTask.execute();
//                    }
                    } else {
                        Toast.makeText(getActivity(), "Please enable internet connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                            .setTitle(getString(R.string.str_error))
                            .setDescription("Insufficient balance. Please select quantity again.")
                            .setPosButtonText(getString(R.string.str_ok), null)
                            .show();
                }

            }
        });
        return view;


    }


    private boolean dateValidationReedem(String fromdate, String todate) {
        Boolean flag = false;
        currentdate = GulfOilUtils.getCurrentDate();
        if (!currentdate.equals("") || !currentdate.equals(null)) {
            if ((currentdate.compareTo(fromdate) >= 0) && currentdate.compareTo(todate) <= 0) {
                Log.e("Valid date", "check");
                flag = true;
            } else {
                Log.e("not valid", "date");
                flag = false;
            }
        } else {
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            String selected1 = dateFormat1.format(today);
            if ((selected1.compareTo(fromdate) >= 0) && selected1.compareTo(todate) <= 0) {
                Log.e("Valid date", "check");
                flag = true;
            } else {
                Log.e("not valid", "date");
                flag = false;
            }
        }

        return flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_plus:
                plusQty();
                break;
            case R.id.tv_minus:
                minusQty();
                break;
        }
    }

    private void plusQty() {

        int productPoints = Integer.parseInt(tvPoints.getText().toString());
        int currentbalance = Integer.parseInt(currentRedeemPoints);

        int i = Integer.parseInt(getQty());
        int price = productPoints * (i + 1);

        if (price > currentbalance) {
            Toast.makeText(getActivity(), "Insufficient balance", Toast.LENGTH_SHORT).show();
        } else {
            i++;
//            tv_qty.setText("" + i);
//            tv_total_point_value.setText("" + price);
        }

    }

    private void minusQty() {
        int productPoints = Integer.parseInt(tvPoints.getText().toString());
        int currentbalance = Integer.parseInt(currentRedeemPoints);

        int i = Integer.parseInt(getQty());


        if (i > 1) {
            i--;
            int price = productPoints * i;
//            tv_qty.setText("" + i);
//            tv_total_point_value.setText("" + price);
        }
    }

    private String getQty() {
        return "";
    }

    public void initComp(View view) {
        ivProduct = view.findViewById(R.id.ivProduct);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivProduct.setClipToOutline(true);
        }
        tvRewardName = view.findViewById(R.id.tvRewardName);
        tvPoints = view.findViewById(R.id.tvPoints);
        tvTotalPoints = view.findViewById(R.id.tvTotalPoints);
        tvCode = view.findViewById(R.id.tvCode);
        tvTdsValue = view.findViewById(R.id.tvTdsValue);
        tvRewardValue = view.findViewById(R.id.tvRewardValue);
        tv_tds_percent = view.findViewById(R.id.tv_tds_percent);
        spnQuantity = view.findViewById(R.id.spnQuantity);
        spnQuantity.setOnItemSelectedListener(this);
        tvConfirmOrderDescription = view.findViewById(R.id.tvConfirmOrderDescription);
        tvConfirmOrderDescription.setMovementMethod(new ScrollingMovementMethod());
        //tvConfirmOrderTermAndCondition = view.findViewById(R.id.tvConfirmOrderTermAndCondition);
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder);
        spnQuantity.setAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.list_item_small, getResources().getStringArray(R.array.spinner_quantity)));
//        tv_minus = (ImageView) view.findViewById(R.id.tv_minus);
//        tv_minus.setOnClickListener(this);
//        tv_plus = (ImageView) view.findViewById(R.id.tv_plus);
//        tv_plus.setOnClickListener(this);
//        tv_qty = (TextView) view.findViewById(R.id.tv_qty);
//        tv_total_point_value = (TextView) view.findViewById(R.id.tv_total_point_value);
        //rl_multi_qty = (RelativeLayout) view.findViewById(R.id.rl_multi_qty);

//        iv_product = (ImageView) view.findViewById(R.id.iv_product_data);
//        gv_bonusDetails = (GridView) view.findViewById(R.id.grid_data);
//        tv_name = (TextView) view.findViewById(R.id.txt_name_data);
//        tv_code = (TextView) view.findViewById(R.id.txt_code_data);
//        tv_points = (TextView) view.findViewById(R.id.txt_points_data);
        //tv_small_desc=(TextView)view.findViewById(R.id.txt_points_data);
//        txt_bonus_desc = (TextView) view.findViewById(R.id.txt_bonus_desc);
/*
        txt_large_desc_data=(TextView)view.findViewById(R.id.txt_large_desc_data);
        txt_site_price_data=(TextView)view.findViewById(R.id.txt_site_price_data);
*/
        // tv_small_desc=(TextView)view.findViewById(R.id.txt_points_data);


//        v = new WebView(getActivity());
//        v.setVerticalScrollBarEnabled(true);
//
//        ((LinearLayout) view.findViewById(R.id.webviewData)).addView(v);


//        btn_confirm_order = (Button) view.findViewById(R.id.btn_confirm_order);
    }

    //prashant add changes
//    class Redemtion_details extends AsyncTask<String[], Void, JSONObject> {
//        private ProgressDialog progressDialog;
//        private Context mContext;
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setMessage("Please  wait!!!");
//            progressDialog.setIndeterminate(true);
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected JSONObject doInBackground(String[]... params) {
//            JSONObject jObj = null;
//            try {
//                jObj = new UserFunctions().Redimption_Window();
//
//                Log.e("", "Login Response:---" + jObj);
//            } catch (Exception e) {
//                // TODO: handle exception
//                getActivity().runOnUiThread(new Runnable() {
//                    public void run() {
//                        Toast.makeText(getActivity(), "Network Error...",
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//            } // end catch
//            return jObj;
//        }
//
//
//        @Override
//        protected void onPostExecute(JSONObject jObj) {
//            super.onPostExecute(jObj);
//            progressDialog.dismiss();
//            Log.e("GridRedeemDetails", "GridRedeemDetails activity loged in json:--------------------------------" + jObj);
//            //Toast.makeText(getActivity(),"Output :"+jObj.toString(),Toast.LENGTH_LONG).show();
//            // if(jObj.getJSONObject())
//            try {
//                String status = jObj.getString("status");
//                JSONObject jsonObjectDate = jObj.getJSONObject("data");
//                String Dfrom = jsonObjectDate.getString("redemption_from");
//                String Dto = jsonObjectDate.getString("redemption_to");
//                String redemption_text = jsonObjectDate.getString("redemption_text");
//                String strDfrom = formatdate(Dfrom);
//                String strDto = formatdate(Dto);
//
//                if (status.equals("success")) {
//                    if (dateValidationReedem(Dfrom, Dto)) {
//
//                        //remove select adderess option
//                        /*Fragment detailsFragment = new ConfirmOrderFragment();
//                        Bundle b = new Bundle();
//                        b.putString(MyTrackConstants._mStringProductCode, "" + product_code);
//                        detailsFragment.setArguments(b);
//
//                        FragmentTransaction ftmech = getActivity().getSupportFragmentManager().beginTransaction();
//                        ftmech.replace(R.id.container_body, detailsFragment);
//                        ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                        ftmech.addToBackStack(null);
//                        ftmech.commit();*/
//                        SharedPreferences prefs = getActivity().getSharedPreferences("address_key", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = prefs.edit();
//                        editor.putString("address_type", "workshop");
//                        editor.commit();
//                        Bundle b = new Bundle();
//                        b.putString(MyTrackConstants._mStringProductCode, "" + product_code);
//                        Intent i = new Intent(getActivity(), OtpActivity.class);
//                        i.putExtras(b);
//                        startActivity(i);
//
//                    } else {
//                        //  CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + "Sorry , currently the redemption window is not open. Redemption window will be open from " + " " + strDfrom + " " + "till" + " " + strDto + " " + ". Only points accumulated up till March 2017 can be redeemed in this period.", "Gulf Oil");
//                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + redemption_text, "Gulf Oil");
//                        cdd.show();
//                    }
//                } else {
//
//                    CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jObj.get("error"), "Gulf Oil");
//                    cdd.show();
//                }
//                //Toast.makeText(getActivity(),"Date :"+Dfrom+Dto,Toast.LENGTH_LONG).show();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }
//get current date

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

    public String formatdate(String fdate) {
        String datetime = null;
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat d = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        } catch (ParseException e) {

        }
        return datetime;


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!TextUtils.isEmpty(tvPoints.getText()) && !TextUtils.isEmpty(currentRedeemPoints)) {
            if (!checkBalance(position)) {
                new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                        .setTitle(getString(R.string.str_error))
                        .setDescription("Insufficient balance.")
                        .setPosButtonText(getString(R.string.str_ok), null)
                        .show();
            }
        }
    }

    private boolean checkBalance(int position) {
        if (!TextUtils.isEmpty(tvPoints.getText()) && !TextUtils.isEmpty(currentRedeemPoints)) {
            int productPoints = Integer.parseInt(tvPoints.getText().toString());
            int currentbalance = Integer.parseInt(currentRedeemPoints);


            int price = productPoints * (position + 1);

            if (price > currentbalance) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL) {
            Log.d(TAG, "getLayoutUserWise: ROYAL");
            return R.layout.fragment_confirm_order_details_royal;
        }
        else if (userType == UserType.ELITE) {
            Log.d(TAG, "getLayoutUserWise: ELITE");
            return R.layout.fragment_confirm_order_details_elite;
        }
        else if (userType == UserType.CLUB) {
            Log.d(TAG, "getLayoutUserWise: CLUB");
            return R.layout.fragment_confirm_order_details_club;
        }
        else {
            Log.d(TAG, "getLayoutUserWise: OTHER");
            return R.layout.fragment_confirm_order_details_royal;
        }
    }

    public class getNetworkTime extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            //Code to get network_security_config current date

            String TIME_SERVER = "time-a.nist.gov";
            NTPUDPClient timeClient = new NTPUDPClient();
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getByName(TIME_SERVER);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            TimeInfo timeInfo = null;
            try {
                timeInfo = timeClient.getTime(inetAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
            long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
            Date time = new Date(returnTime);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(time);
            int month = calendar.get(Calendar.MONTH) + 1;
            //Log.e(TAG, "onCreate: today's date: "+calendar.get(Calendar.DAY_OF_MONTH)+":"+month+":"+calendar.get(Calendar.YEAR));
            strdateOrg = calendar.get(Calendar.YEAR) + "-" + month + "-" + calendar.get(Calendar.DAY_OF_MONTH);
            //Code to get System's current date

            Date date = new Date();
            Calendar calendar1 = new GregorianCalendar();
            calendar1.setTime(date);
            int month1 = calendar1.get(Calendar.MONTH) + 1;
            //.e(TAG, "doInBackground: System date: "+calendar1.get(Calendar.DAY_OF_MONTH)+":"+month1+":"+calendar1.get(Calendar.YEAR));
            return null;
        }
    }

    private class NewRedemtionDetailsTask extends AsyncTask<Void, Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            try {
                Log.e(TAG, "onPreExecute: NewRedemtionDetailsTask");
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please  wait!!!");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
                super.onPreExecute();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "onPreExecute: CATCH BLOCK");
            }
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            Log.e(TAG, "doInBackground: NewRedemtionDetailsTask");
            JSONObject jObj = null;
            try {
                jObj = new UserFunctions().Redimption_Window();

                Log.e("", "Login Response:---" + jObj);
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
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            Log.e(TAG, "onPostExecute: NewRedemtionDetailsTask");
            progressDialog.dismiss();
            Log.e("GridRedeemDetails", "GridRedeemDetails activity loged in json:--------------------------------" + jObj);
            //Toast.makeText(getActivity(),"Output :"+jObj.toString(),Toast.LENGTH_LONG).show();
            // if(jObj.getJSONObject())
            try {
                String status = jObj.getString("status");
                JSONObject jsonObjectDate = jObj.getJSONObject("data");
                String Dfrom = jsonObjectDate.getString("redemption_from");
                String Dto = jsonObjectDate.getString("redemption_to");
                String redemption_text = jsonObjectDate.getString("redemption_text");
                String strDfrom = formatdate(Dfrom);
                String strDto = formatdate(Dto);

                if (status.equals("success")) {
                    if (dateValidationReedem(Dfrom, Dto)) {

                        //remove select adderess option
                        /*Fragment detailsFragment = new ConfirmOrderFragment();
                        Bundle b = new Bundle();
                        b.putString(MyTrackConstants._mStringProductCode, "" + product_code);
                        detailsFragment.setArguments(b);

                        FragmentTransaction ftmech = getActivity().getSupportFragmentManager().beginTransaction();
                        ftmech.replace(R.id.container_body, detailsFragment);
                        ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ftmech.addToBackStack(null);
                        ftmech.commit();*/
                        SharedPreferences prefs = getActivity().getSharedPreferences("address_key", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("address_type", "workshop");
                        editor.commit();
//                        Bundle b = new Bundle();
//                        b.putString(MyTrackConstants._mStringProductCode, "" + product_code);
//                        b.putString("multi_qty", "" + (spnQuantity.getSelectedItemPosition() + 1));
//                        Intent i = new Intent(getActivity(), RoyaltyOtpActivity.class);
//                        i.putExtras(b);
//                        startActivity(i);

                        ActionOrderConfirmationDetailsFragmentToProceedOrderOTPFragment action = OrderConfirmDetailsFragmentDirections.actionOrderConfirmationDetailsFragmentToProceedOrderOTPFragment();
                        action.setMultiQty("" + (spnQuantity.getSelectedItemPosition() + 1));
                        action.setProductCode(product_code);
                        Navigation.findNavController(getView()).navigate(action);


                    } else {

                        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle(getString(R.string.str_error))
                                .setDescription(redemption_text)
                                .setPosButtonText(getString(R.string.str_ok), null)
                                .show();
                    }
                } else {

                    new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                            .setTitle(getString(R.string.str_error))
                            .setDescription(jObj.get("error").toString())
                            .setPosButtonText(getString(R.string.str_ok), null)
                            .show();

                }
                //Toast.makeText(getActivity(),"Date :"+Dfrom+Dto,Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class GetProductDetails extends AsyncTask<Void, Void, JSONObject> {

        private ProgressDialog progressDialog;
        private Context mContext = getActivity();

        @Override
        protected void onPreExecute() {
            Log.e("insert :", "in pre execute of get order details");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            Log.e("insert :", "in do in background of get order details");
            JSONObject jObj = null;
            try {
                jObj = new UserFunctions().getProductData(product_code);
                // Log.e("", "Category json" + jObj);
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
            Log.e("insert :", "in post execute of get order details");
            // showToast("Calling set up views");
            progressDialog.dismiss();
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {

                      /*  alertDialog2(
                                getResources().getString(R.string.app_name),
                                "" + jObj.getString("error"));*/

                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), jObj.getString("error"), "Gulf Oil");
                        cdd.show();


                        progressDialog.dismiss();
                    } else if (mStringStatus.equals("success")) {

                        Log.e("", "Befor getting product json array");
                        JSONArray resultJArray = jObj.getJSONArray("product");
                        Log.e("products data : ", "-------------products : ----------------" + resultJArray);

                        String product_details = resultJArray.toString();

                        Log.e("", "Befor getting bonus product json array");

                        if (jObj.has("bonus_products")) {
                            bonus_products = jObj.getString("bonus_products");
                            Log.e("products data : ", "-------------products : ----------------" + bonus_products);
                            if (bonus_products.equals("")) {

                            } else {
                         /*   Object json_successtoken = new JSONTokener(jObj.getString("succ_codes")).nextValue();//jObj.get("succ_codes");
                            // JSONArray jObjSuccessCode = jObj.getJSONArray("succ_codes");
                            if (json_successtoken instanceof JSONArray) {
                                JSONArray resultJArray1 = jObj.getJSONArray("bonus_products");
                                Log.e("products data : ", "-----------------products : --------------- " + resultJArray1);

                                bonus_details=resultJArray1.toString();
                            } else
                            {
                                JSONObject resultJObject = jObj.getJSONObject("bonus_products");
                                Log.e("products data : ", "-----------------products : --------------- " + resultJObject);

                                bonus_details=resultJObject.toString();
                            }*/
                            }

                        }

                        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
                        try {
                            ctdUser.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Log.e("", "Befor for in oppost execute grid redeemdetails");

                        for (int i = 0; i < resultJArray.length(); i++) {
                            Log.e("", "Product code in onpost exec");
                            String pCode = resultJArray.getJSONObject(i).optString("product_code");
                            if (jObj.has("bonus_products")) {
                                ctdUser.updateProduct(pCode, product_details, bonus_products);
                            } else {
                                ctdUser.updateProduct(pCode, product_details, "");
                            }
                        }
                        Log.e("", "Before getSelected product details");

                        String prod_details = ctdUser.getSelectedProductDetails(product_code);

                        JSONArray arr = new JSONArray(prod_details);
                        JSONObject obj = arr.getJSONObject(0);

                        String detail_code = obj.optString("product_code");
                        String detail_name = obj.optString("name");
                        String detail_large_desc = obj.optString("large_description");
                        String detail_point = obj.optString("points");
                        String detail_img = obj.optString("large_image_link");
                        String tdsValue = obj.optString("tds_value");
                        String TdsValuePercent = obj.optString("tds_percent");
                        String rewardValue = obj.optString("rewards_value");

                        tvRewardName.setText("" + detail_name);
                        tvCode.setText(detail_code);
                        tvPoints.setText(detail_point);
                        tvTdsValue.setText(tdsValue);

                        tvRewardValue.setText(rewardValue);
                        tv_tds_percent.setText("TDS ("+TdsValuePercent+" %)   :  ");
                        tvConfirmOrderDescription.setText(GulfOilUtils.getHtmlText(detail_large_desc));
                        //ADDED BY PRAVIN DHARAM
                        //tv_total_point_value.setText(detail_point);
                        //tv_small_desc.setText(Html.fromHtml(""+detail_large_desc));

                        String text = "<html>" +
                                "<body  style=\"text-align:justify;color: #ba8e57; background-color: #000;\"> %s </body></Html>";
                        //v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.royalty_background));
                        //v.loadData(String.format(text, "" + detail_large_desc), "text/html", "utf-8");
                        /*v.setBackgroundColor(Color.TRANSPARENT);
                        v.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);*/

                        Log.e("", "Selected product name:" + detail_name);
                        Log.e("", "Selected product name:" + detail_code);
                        Log.e("", "Selected product points:" + detail_point);
                        Log.e("", "Selected product arge desc:" + detail_large_desc);

                        Picasso.with(mContext)
                                .load(detail_img)
                                .resize(480, 300)
                                .placeholder(mContext.getResources().getDrawable(R.drawable.loading))
                                .error(mContext.getResources().getDrawable(R.drawable.no_image_available))
                                .into(ivProduct);

                        ArrayList<Product> bonus = new ArrayList<Product>();

                        String bonus_detals = ctdUser.getBonus_details(product_code);

                        ctdUser.close();
                        Product p = new Product();

                        if (bonus_detals.equals("") || bonus_detals == null) {
                            //txt_bonus_desc.setVisibility(View.GONE);
                        } else {
                            //txt_bonus_desc.setVisibility(View.VISIBLE);
                            Object json_successtoken = new JSONTokener(bonus_detals).nextValue();//jObj.get("succ_codes");
                            // JSONArray jObjSuccessCode = jObj.getJSONArray("succ_codes");
                            if (json_successtoken instanceof JSONArray) {
                                JSONArray resultJArray1 = new JSONArray(bonus_detals);
                                Log.e("products data : ", "-----------------bonus products : --------------- " + resultJArray1);

                                for (int i = 0; i < resultJArray1.length(); i++) {
                                    JSONObject result_bonus = resultJArray1.getJSONArray(i).getJSONObject(0);
                                    Log.e("products data : ", "-----------------bonus products : i : " + i + " ,  " + result_bonus);
                                    Product bonus_prod = new Product();
                                    bonus_prod.setSmall_image_link(result_bonus.getString("small_image_link"));
                                    bonus_prod.setName(result_bonus.getString("name"));
                                    bonus_prod.setProduct_code(result_bonus.getString("product_code"));
                                    bonus_prod.setPoints(result_bonus.getInt("points"));

                                    bonus.add(bonus_prod);
                                }

                                // gv_bonusDetails.setAdapter(new BonusProductGridAdapter(getActivity(), bonus, "bonus", ""));
                                //  bonus_details=resultJArray1.toString();
                            } else {
                                JSONObject resultJObject = jObj.getJSONObject(bonus_detals);
                                Log.e("products data : ", "-----------------products : --------------- " + resultJObject);
                                //bonus_details=resultJObject.toString();
                            }
                        }
/*
                        for(int i=0;i<bonus_dataArray.length();i++)
                        {
                            JSONObject jsonObject=bonus_dataArray.getJSONObject(i);

                            String name=jsonObject.optString("name");
                            String product_code=jsonObject.optString("product_code");
                            String small_image_link=jsonObject.optString("small_image_link");

                            p.setName(""+name);
                            p.setProduct_code("" + code);
                            p.setSmall_description("" + small_image_link);

                            bonus.add(p);

                            Log.e("", "Bonus name:" + bonus.get(i).getName());
                            Log.e("","Bonus code:"+bonus.get(i).getProduct_code());
                            Log.e("","Bonus url:"+bonus.get(i).getSmall_image_link());

                        }
                        */
                        progressDialog.dismiss();

                    }
                    new getNetworkTime().execute();
                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }

}