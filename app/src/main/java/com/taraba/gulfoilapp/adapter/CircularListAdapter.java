package com.taraba.gulfoilapp.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.ClaimHistoryFragment;
import com.taraba.gulfoilapp.NewEditRegistration;
import com.taraba.gulfoilapp.OrderHistoryFragment;
import com.taraba.gulfoilapp.Otp_fragment;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.TargetMeterCategoryFragment;
import com.taraba.gulfoilapp.contentproviders.UserModel;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.model.SearchMemberButton;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android3 on 12/24/15.
 * Modified by Mansi
 */
public class CircularListAdapter extends ArrayAdapter<UserModel> implements SearchMemberButtonAdapter.SearchMemberButtonCallBack {

    private final List<UserModel> values;
    private Context context = getContext();
    public static String hideRegButton = "";
    Boolean isMgmtVerified = false, isButtonset = false;
    private List<SearchMemberButton> listOfButton;
    int pos;
    private RecyclerView rvSearchMemberButton;
    private int adapterPosition = 0;
    private String userType;


    public CircularListAdapter(Context context, List<UserModel> values) {
        super(context, R.layout.mechanical_list, values);
        this.values = values;
        this.context = context;
        this.listOfButton = new ArrayList<>();
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public int getPosition(UserModel item) {
        return super.getPosition(item);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mechanical_list, parent,
                    false);
        }

        adapterPosition = position;

        rvSearchMemberButton = (RecyclerView) convertView.findViewById(R.id.rvSearchMemberButton);
        rvSearchMemberButton.setLayoutManager(new GridLayoutManager(context, 4));
//        Button btnProfile, btnSubmitotp,/* btnAccuCode, */btnResendotp, btnSubmitPend,
//                btnClaimHistory, btnOrderStatus, btnViewTarget,
//                btnRedeem;

//        btnRedeem = (Button) convertView
//                .findViewById(R.id.btnRedeem);
//
//        btnProfile = (Button) convertView
//                .findViewById(R.id.btnProfile);
//        btnSubmitotp = (Button) convertView
//                .findViewById(R.id.btnSubmitotp);
//        btnResendotp = (Button) convertView
//                .findViewById(R.id.btnResendotp);
//        btnOrderStatus = (Button) convertView
//                .findViewById(R.id.btnorderdetail);
//        btnViewTarget = (Button) convertView
//                .findViewById(R.id.btnviewtarget);
//       /* btnAccuCode = (Button) convertView
//                .findViewById(R.id.btnAccuCode);*/
//        btnSubmitPend = (Button) convertView
//                .findViewById(R.id.btnSubmitPending);
//        btnClaimHistory = (Button) convertView
//                .findViewById(R.id.btnClaimHistory);
        TextView txtID = (TextView) convertView
                .findViewById(R.id.lblid);
        TextView txtMobNo = (TextView) convertView
                .findViewById(R.id.lblmobno);
        TextView txtWorkshopName = (TextView) convertView
                .findViewById(R.id.lblworkshopname);
        TextView txtStatus = (TextView) convertView
                .findViewById(R.id.lblstatus);
       /* TextView txtState = (TextView) convertView
                .findViewById(R.id.lblstate);
        TextView txtDistrict = (TextView) convertView
                .findViewById(R.id.lbldistrict);*/
        TextView txt_balance_points = (TextView) convertView
                .findViewById(R.id.edt_balance_points);
        TextView txt_total_points = (TextView) convertView
                .findViewById(R.id.txt_total_points);
        TextView txt_reedem_points = (TextView) convertView
                .findViewById(R.id.edt_redeem_points);


        txtID.setText(values.get(position).getParticipant_code());
        txtMobNo.setText(values.get(position).getMobile_no());
        txtWorkshopName.setText(values.get(position).getWorkshopname());

        if (values.get(position).getStatus().equals("mgmt verified"))
            txtStatus.setText("Registered");
        else
            txtStatus.setText(values.get(position).getStatus());
      /*  txtState.setText("State: " + values.get(position).getState());
        txtDistrict.setText("District: " + values.get(position).getDistrict());*/

        /*UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
        try {
            ctdUser.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String totptn = ctdUser.getTotalPoints(strParticipnt_login_id);*/
        if ((values.get(position).getBalance_points()).equals("null") || (values.get(position).getBalance_points()) == null)
            txt_balance_points.setText(Html.fromHtml("<b>Total Balance </b><br/>0"));
        else
            txt_balance_points.setText(Html.fromHtml("<b>Total Balance</b><br/>" + values.get(position).getBalance_points()));
        if ((values.get(position).getTotal_point()).equals("null") || (values.get(position).getTotal_point()) == null)
            txt_total_points.setText(Html.fromHtml("<b>Total Earned Points </b><br/>0"));
        else
            txt_total_points.setText(Html.fromHtml("<b>Total Earned Points </b><br/>" + values.get(position).getTotal_point()));

        if ((values.get(position).getRedeem_point()).equals("null") || (values.get(position).getRedeem_point()) == null)
            txt_reedem_points.setText(Html.fromHtml("<b>Total Redeemed Points </b><br/>0"));
        else
            txt_reedem_points.setText(Html.fromHtml("<b>Total Redeemed Points </b><br/>" + values.get(position).getRedeem_point()));
       /* try {
            UserTableDatasource utd = new UserTableDatasource(context);
            utd.open();
            String total_points = utd.getTotalPoints(values.get(position).getId());
            utd.close();


            txtpoints.setText("Total Points : "+total_points);
        } catch (Exception e) {

        }
*/
        if (!isButtonset) {
            listOfButton.add(new SearchMemberButton("Profile", R.color.gridbutton_b));
        }
        if (values.get(position).getStatus().equals("otp verified")) {
            if (!isButtonset) {
                listOfButton.add(new SearchMemberButton("Transaction History", R.color.gridbutton_r));
            }
//            btnSubmitotp.setVisibility(View.GONE);
//            btnResendotp.setVisibility(View.GONE);
            /* btnAccuCode.setVisibility(View.VISIBLE);*/
//            btnClaimHistory.setVisibility(View.VISIBLE);
            // btnRedeem.setVisibility(View.GONE);
//            btnOrderStatus.setVisibility(View.GONE);
//            btnViewTarget.setVisibility(View.GONE);
            isMgmtVerified = false;
        } else if (values.get(position).getStatus().equals("mgmt verified")) {
            if (!isButtonset) {
                listOfButton.add(new SearchMemberButton("Submit OTP", R.color.gridbutton_o));
                listOfButton.add(new SearchMemberButton("Reset OTP", R.color.gridbutton_r));
            }
//            btnSubmitotp.setVisibility(View.VISIBLE);
//            btnResendotp.setVisibility(View.VISIBLE);
            //  hideRegButton="hide";
            /*  btnAccuCode.setVisibility(View.GONE);*/
//            btnClaimHistory.setVisibility(View.GONE);
            //btnRedeem.setVisibility(View.GONE);
//            btnOrderStatus.setVisibility(View.GONE);
//            btnViewTarget.setVisibility(View.GONE);
            isMgmtVerified = true;
        } else if (values.get(position).getStatus().equals("Pending Verification") || values.get(position).getStatus().equals("pending verification")) {
//            btnSubmitotp.setVisibility(View.GONE);
//            btnResendotp.setVisibility(View.GONE);
            // hideRegButton="hide";
            /*  btnAccuCode.setVisibility(View.GONE);*/
            // btnSubmitPend.setVisibility(View.VISIBLE);
//            btnClaimHistory.setVisibility(View.GONE);
            //btnRedeem.setVisibility(View.GONE);
//            btnOrderStatus.setVisibility(View.GONE);
//            btnViewTarget.setVisibility(View.GONE);
            isMgmtVerified = false;

        } else if (values.get(position).getStatus().equals("Active".toLowerCase())) {
            if (!isButtonset) {
                listOfButton.add(new SearchMemberButton("Transaction History", R.color.gridbutton_r));
                listOfButton.add(new SearchMemberButton("Order Status", R.color.gridbutton_o));
                listOfButton.add(new SearchMemberButton("View Target", R.color.lightpic));
            }
//            btnSubmitotp.setVisibility(View.GONE);
//            btnResendotp.setVisibility(View.GONE);
            //  hideRegButton="hide";
            /* btnAccuCode.setVisibility(View.VISIBLE);*/
//            btnClaimHistory.setVisibility(View.VISIBLE);
            // btnRedeem.setVisibility(View.VISIBLE);
//            btnOrderStatus.setVisibility(View.VISIBLE);
//            btnViewTarget.setVisibility(View.VISIBLE);
            isMgmtVerified = false;
        } else {
            if (!isButtonset) {
                listOfButton.add(new SearchMemberButton("Submit OTP", R.color.gridbutton_o));
            }
//            btnSubmitotp.setVisibility(View.VISIBLE);
            /*  btnAccuCode.setVisibility(View.GONE);*/
//            btnClaimHistory.setVisibility(View.GONE);
            // btnRedeem.setVisibility(View.GONE);
//            btnOrderStatus.setVisibility(View.GONE);
//            btnViewTarget.setVisibility(View.GONE);
            isMgmtVerified = false;
        }

        //set button using recylerview adapter
        if (!isButtonset) {
            SearchMemberButtonAdapter adapter = new SearchMemberButtonAdapter(context, listOfButton);
            adapter.setSearchMemberButtonCallBack(this);
            rvSearchMemberButton.setAdapter(adapter);
            isButtonset = true;
        }


        return convertView;

    }

    @Override
    public void searchMemberButtonClick(int poistion) {
        switch (listOfButton.get(poistion).getName()) {
            case "Profile":
                if (userType != null && userType.equals("fls")) {
                /*replaceFragment(MyProfileFragment.newInstance("SearchMember",
                        "" + values.get(adapterPosition).getId(),
                        values.get(adapterPosition).getUserfname() + " " + values.get(adapterPosition).getUserlname(),
                        values.get(adapterPosition).getWorkshopname(),
                        values.get(adapterPosition).getMobile_no(),
                        values.get(adapterPosition).getDb_code(),
                        values.get(adapterPosition).getParticipant_code()));*/
                    replaceFragment(NewEditRegistration.newInstance(
                            String.valueOf(values.get(adapterPosition).getId())));
                } else {
//                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Transaction History":
//                Toast.makeText(context, "Transaction History", Toast.LENGTH_SHORT).show();
                hideKeyboard((AppCompatActivity) context);
                Fragment claimhitoryFragment = new ClaimHistoryFragment();
                //Fragment claimhitoryFragment = new ComingSoonFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("participant_login_id", values.get(adapterPosition).getId());
                bundle.putString("total_points", values.get(adapterPosition).getTotal_point());
                bundle.putString("balanced_points", values.get(adapterPosition).getBalance_points());
                bundle.putString("redeem_points", values.get(adapterPosition).getRedeem_point());
                bundle.putString("participant_code", values.get(adapterPosition).getParticipant_code());
                Log.e("part id :", "Part id in adapter : " + values.get(adapterPosition).getId());
                claimhitoryFragment.setArguments(bundle);

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

                FragmentTransaction ft1 = fragmentManager.beginTransaction();
                ft1.replace(R.id.container_body, claimhitoryFragment);
                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft1.addToBackStack(null);
                ft1.commit();
                replaceFragment(claimhitoryFragment);
                break;
            case "Order Status":
//                Toast.makeText(context, "Order Status", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager2 = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction ftmech2 = fragmentManager2.beginTransaction();

                ftmech2.replace(R.id.container_body, OrderHistoryFragment.newInstance(values.get(adapterPosition).getId(), true), "Registration");
                ftmech2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftmech2.addToBackStack(null);
                ftmech2.commit();
                break;
            case "View Target":
//                Toast.makeText(context, "View Target", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager3 = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction ftmech3 = fragmentManager3.beginTransaction();

                ftmech3.replace(R.id.container_body, TargetMeterCategoryFragment.newInstance("" + values.get(adapterPosition).getId()), "Registration");
                ftmech3.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftmech3.addToBackStack(null);
                ftmech3.commit();
                break;
            case "Submit OTP":
                hideKeyboard((AppCompatActivity) context);
                Fragment otpFragment = new Otp_fragment();
                Bundle bundle2 = new Bundle();
                bundle2.putInt("participant_login_id", values.get(adapterPosition).getId());
                bundle2.putString("participant_mob_no", values.get(adapterPosition).getMobile_no());
                bundle2.putString("participant_code", values.get(adapterPosition).getParticipant_code());
                otpFragment.setArguments(bundle2);

                SharedPreferences preferences = ((Activity) context).getSharedPreferences("status", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("status", "profile");
                editor.commit();

                FragmentManager fragmentManager4 = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction ft4 = fragmentManager4.beginTransaction();
                ft4.replace(R.id.container_body, otpFragment);
                ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft4.addToBackStack(null);
                ft4.commit();
                break;
            case "Reset OTP":
//                Toast.makeText(context, "Reset OTP", Toast.LENGTH_SHORT).show();
                hideKeyboard((AppCompatActivity) context);

                if (NetworkUtils.isNetworkAvailable(context)) {
                    pos = adapterPosition;
                    new LogedIn().execute(new String[]{
                            String.valueOf(values.get(adapterPosition).getId()), values.get(adapterPosition).getMobile_no()});
                } else {
                    Toast.makeText(context,
                            "Internet Disconnected", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }


    class LogedIn extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
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
                Log.e("data : ", "Data : Username : " + params[0][0] + " : " + params[0][1]
                );
                jObj = new UserFunctions().Resend_otp(""
                        + params[0][0], params[0][1]);
            } catch (Exception e) {
                // TODO: handle exception
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context,
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
                    progressDialog.dismiss();
                    Log.e("Json Data : ", "Json data : " + jObj);
                    //  String mStringStatus = jObj.getString("success");
                    if (jObj.has("message")) {

                        /*alertDialog(
                                context.getResources().getString(R.string.app_name),
                                jObj.getString("message"));*/
                        CustomOKDialog cdd = new CustomOKDialog(context, "" + jObj.getString("message"), "Gulf Oil");
                        cdd.show();

                        hideKeyboard((AppCompatActivity) context);
                        Fragment otpFragment = new Otp_fragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("participant_login_id", values.get(pos).getId());
                        bundle.putString("participant_mob_no", values.get(pos).getMobile_no());
                        bundle.putString("participant_code", values.get(pos).getParticipant_code());
                        otpFragment.setArguments(bundle);

                        SharedPreferences preferences = ((Activity) context).getSharedPreferences("status", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("status", "profile");
                        editor.commit();

                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.container_body, otpFragment);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.addToBackStack(null);
                        ft.commit();

                      /*  Log.e("In sucess  ", "In sucess  : " + " In sucess 1" );

                        SharedPreferences preferences = getActivity().getSharedPreferences(
                                "signupdetails", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("old_usertrno", "" + preferences.getString("usertrno", ""));
                        Log.e("user id ", "User Id : " + "" + preferences.getString("usertrno", ""));
                        edit.putString("uname", "");
                        edit.putString("userimage", "");
                        edit.putString("usertrno", "");
                        edit.putString("status", "");
                        edit.commit();
                        progressDialog.dismiss();*/

                        /*final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
                        try {
                            ctdUser.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        ctdUser.updateStattus("1","Active");
                        ctdUser.close();*/


                    } else {
                        progressDialog.dismiss();
                       /* Toast.makeText(getActivity(),
                                "Please enter currect OTP",
                                Toast.LENGTH_SHORT).show();*/
                        /*alertDialog(
                                context.getResources().getString(R.string.app_name),
                                jObj.getString("error_message"));*/

                        CustomOKDialog cdd = new CustomOKDialog(context, "" + jObj.getString("error_message"), "Gulf Oil");
                        cdd.show();
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.e("Error : ", "Error : " + e.toString());
                }
            } else {
                progressDialog.dismiss();
            }
        } // end onpostecxe
    }

    public void alertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

        FragmentTransaction ft1 = fragmentManager.beginTransaction();
        ft1.replace(R.id.container_body, fragment);
        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft1.addToBackStack(null);
        ft1.commit();
    }

}