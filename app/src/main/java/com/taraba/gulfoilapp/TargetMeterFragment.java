package com.taraba.gulfoilapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.taraba.gulfoilapp.adapter.CircularListAdapter;
import com.taraba.gulfoilapp.adapter.WorkshopListAdpter;
import com.taraba.gulfoilapp.contentproviders.UserModel;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarabasoftwareiinc on 08/05/17.
 */
public class TargetMeterFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button btnSearch, btnViewTarget;
    private Spinner spsearchBy;
    private EditText etMobileNo;
    private TextView tvRetailerId, tvMobileNo, tvShopName, tvStatus;
    private LinearLayout llMechanicDetails;
    private String mStringType, mobile_no, type, mStringMobileNo;
    private InputFilter filter;
    private ArrayList<UserModel> workshop_list;
    public static List<UserModel> circularList;
    private ArrayList<String> all_workshop = new ArrayList<String>();
    private int strid, workshop_pos;
    private UserTableDatasource ctdUser;
    private CircularListAdapter adapter;
    private WorkshopListAdpter adapter1;
    private AlertDialog.Builder alertDialog;
    protected AlertDialog ad;
    private JSONObject result;
    private static final String TAG = "TargetMeterFragment";
    private String loginID;

    public TargetMeterFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_target_meter, container, false);
        spsearchBy = (Spinner) view.findViewById(R.id.spsearchBy);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnViewTarget = (Button) view.findViewById(R.id.btnViewTarget);

        etMobileNo = (EditText) view.findViewById(R.id.etMobileNo);
        tvRetailerId = (TextView) view.findViewById(R.id.tvRetailerID);
        tvShopName = (TextView) view.findViewById(R.id.tvShopName);
        tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
        tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        llMechanicDetails = (LinearLayout) view.findViewById(R.id.llMechanicDetails);
        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });
        spsearchBy.setOnItemSelectedListener(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSeachCall();
            }
        });

        btnViewTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(loginID)) {
                    Fragment fragment = TargetMeterCategoryFragment.newInstance(loginID);
                    replacfragment(fragment);
                    /*Fragment fragment = TargetViewFragment.newInstance(loginID);
                    replacfragment(fragment);*/
                }


            }

        });

        SharedPreferences preferences_search = AppConfig.getInstance().getSharedPreferences(
                "searchdetails", getActivity().MODE_PRIVATE);

        mobile_no = preferences_search.getString(MyTrackConstants._mStringSerchData, "");
        type = preferences_search.getString(MyTrackConstants._mStringSerchtype, "");
        mStringType = type;
        Log.e("", "Edit text search data type:" + mStringType);
        SharedPreferences preferences = AppConfig.getInstance().getSharedPreferences(
                "searchdetails", Context.MODE_PRIVATE);

        String searchData = preferences.getString(MyTrackConstants._mStringSerchData, "");
        String search_type = preferences.getString(MyTrackConstants._mStringSerchtype, "");
        if (!searchData.equals("") || searchData != null) {
            etMobileNo.setText("" + searchData);
        }

        if (!TextUtils.isEmpty(search_type)) {
            if (search_type.equals("Search By Shop Name")) {
                spsearchBy.setSelection(2);
            } else if (search_type.equals("Search By Dealer Code")) {
                spsearchBy.setSelection(1);
            } else if (search_type.equals("Search By Mobile Number")) {
                spsearchBy.setSelection(0);
            }

        }

        if (!mobile_no.equals("") || !type.equals("")) {
            JSONObject obj = null;
            try {
                obj = new JSONObject(preferences.getString("jsonResult_main", ""));
                workshop_pos = preferences.getInt("workshop_pos", -1);
                result = new JSONObject(preferences.getString("workshop_result", ""));

                Log.e("jsonResult_main", "on create set: " + obj);
                Log.e("workshop_pos", "on create set: " + workshop_pos);
                Log.e("workshop_result", "on create set: " + result);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (mStringType.equals("Search By Shop Name")) {
                parseData(obj);
            } else {
                parseDataOtherthanShop(obj);
            }
        }

        return view;
    }

    private void buttonSeachCall() {
        //clear retailerid and login id when search new mechanic
        loginID = "";
        hideKeyboard(getActivity());
        SharedPreferences preferences_search = AppConfig.getInstance().getSharedPreferences(
                "searchdetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences_search.edit();
        edit.putString(MyTrackConstants._mStringSerchData, "");
        edit.putString(MyTrackConstants._mStringSerchtype, "");
        edit.commit();


        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            hideKeyboard(getActivity());
            validate();
        } else {
            Toast.makeText(getActivity(), "Internet connection not avalible..", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View view) {

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (spsearchBy.getSelectedItem().toString().equals("Search By Mobile Number")) {
            etMobileNo.setText("");
            etMobileNo.setHint("Enter Mobile Number");
            etMobileNo.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (mobile_no.equals("") || type.equals("")) {
            } else {
                etMobileNo.setText("" + mobile_no);
            }
        } else if (spsearchBy.getSelectedItem().toString().equals("Search By Dealer Code")) {
            etMobileNo.setText("");
            etMobileNo.setHint("Enter Dealer Code");
            etMobileNo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            filterEdittext();
            etMobileNo.setFilters(new InputFilter[]{filter});
            if (mobile_no.equals("") || type.equals("")) {
            } else {
                etMobileNo.setText("" + mobile_no);
            }
        } else if (spsearchBy.getSelectedItem().toString().equals("Search By Shop Name")) {
            etMobileNo.setText("");
            etMobileNo.setHint("Enter Shop Name");
            etMobileNo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            filterEdittext();
            etMobileNo.setFilters(new InputFilter[]{filter});
            if (mobile_no.equals("") || type.equals("")) {
            } else {
                etMobileNo.setText("" + mobile_no);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void validate() {
        mStringType = spsearchBy.getSelectedItem().toString();
        if (etMobileNo.getText().toString().trim().equals("")) {
            if (mStringType.equals("Search By Mobile Number")) {
                etMobileNo.setError("Enter " + getResources().getString(R.string.m_mobile_no));
            } else if (mStringType.equals("Search By Dealer Code")) {
                etMobileNo.setError("Enter " + getResources().getString(R.string.m_membership_id));
            } else {
                etMobileNo.setError("Enter " + getResources().getString(R.string.m_workshop_name));
            }

            etMobileNo.requestFocus();
        } else if (mStringType.equals("Search By Shop Name")) {
            int length = etMobileNo.getText().toString().trim().length();
            if (length < 5) {
                etMobileNo.setError("Enter at least 5 character for" + getResources().getString(R.string.m_workshop_name));
            } else {

                SharedPreferences preferences_search = AppConfig.getInstance().getSharedPreferences(
                        "searchdetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences_search.edit();
                edit.putString(MyTrackConstants._mStringSerchData, "" + etMobileNo.getText().toString());
                edit.putString(MyTrackConstants._mStringSerchtype, "" + mStringType);
                edit.commit();


                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    mStringMobileNo = etMobileNo.getText().toString();

                    SharedPreferences preferences1 = AppConfig.getInstance().getSharedPreferences(
                            "signupdetails", Context.MODE_PRIVATE);
                    String loginId = preferences1.getString("usertrno", "");
                    String user_type = preferences1.getString("user_type", "");
                    Log.d("Result_Response3", user_type);
                    Fragment fr = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_body);
                    if (fr instanceof TargetMeterFragment) {
                        new SearchMechanic().execute(new String[]{mStringMobileNo, mStringType, user_type, loginId});
                    }
                }

            }
        } else {

            SharedPreferences preferences_search = AppConfig.getInstance().getSharedPreferences(
                    "searchdetails", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences_search.edit();
            edit.putString(MyTrackConstants._mStringSerchData, "" + etMobileNo.getText().toString());
            edit.putString(MyTrackConstants._mStringSerchtype, "" + mStringType);
            edit.commit();


            if (NetworkUtils.isNetworkAvailable(getActivity())) {
                mStringMobileNo = etMobileNo.getText().toString();

                SharedPreferences preferences1 = AppConfig.getInstance().getSharedPreferences(
                        "signupdetails", Context.MODE_PRIVATE);
                String loginId = preferences1.getString("usertrno", "");
                String user_type = preferences1.getString("user_type", "");

                Fragment fr = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_body);
                if (fr instanceof TargetMeterFragment) {
                    new SearchMechanic().execute(new String[]{mStringMobileNo, mStringType, user_type, loginId});
                }
            } else {
                Toast.makeText(getActivity(),
                        "Internet Disconnected", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void filterEdittext() {
        filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                boolean keepOriginal = true;
                StringBuilder sb = new StringBuilder(end - start);
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (isCharAllowed(c)) // put your condition here
                        sb.append(c);
                    else
                        keepOriginal = false;
                }
                if (keepOriginal)
                    return null;
                else {
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(sb);
                        TextUtils.copySpansFrom((Spanned) source, start, sb.length(), null, sp, 0);
                        return sp;
                    } else {
                        return sb;
                    }
                }
            }

            private boolean isCharAllowed(char c) {
                return Character.isLetterOrDigit(c) || Character.isSpaceChar(c);
            }
        };
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
                Log.e("data : ", " Mobile No : " + params[0][0]);
                jObj = new UserFunctions().GetParticipant(""
                        + params[0][0], "" + params[0][1], "" + params[0][2], "" + params[0][3]);
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
        protected void onPostExecute(final JSONObject jObj) {
            super.onPostExecute(jObj);
            progressDialog.dismiss();
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {

                        /*alertDialog2(
                                getResources().getString(R.string.app_name),
                                ""+jObj.getString("error"));*/
                        progressDialog.dismiss();
                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jObj.getString("error"), "Gulf Oil");
                        cdd.show();


                    } else if (mStringStatus.equals("success")) {
                        llMechanicDetails.setVisibility(View.VISIBLE);


                        Log.e(TAG, "onPostExecute: json response: " + jObj.toString());
                        JSONObject particpantJson = jObj.getJSONArray("particpant_data").getJSONObject(0);
                        tvRetailerId.setText(particpantJson.getString("retailer_code"));
                        tvShopName.setText(particpantJson.getString("workshop_name"));
                        tvMobileNo.setText(particpantJson.getString("mobile_no"));
                        tvStatus.setText(particpantJson.getString("status"));
                        loginID = particpantJson.getString("login_id_pk");

                    }
                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }

            } // end onpostecxe
        }
    }


    private void parseDataOtherthanShop(JSONObject jObj) {
        Log.e("In", "parse OtherthanShop: " + jObj);
        try {
            circularList = new ArrayList<UserModel>();
            UserModel _news = new UserModel();
            strid = Integer.parseInt(result.getString("login_id_pk"));
            _news.setId(Integer.parseInt(result.getString("login_id_pk")));

            if (result.has("image_path")) {
                _news.setPicture(result.getString("image_path"));
            } else {
                _news.setPicture("");
            }
            _news.setUserfname(result.getString("first_name"));
            _news.setUserlname(result.getString("last_name"));

            _news.setGender(result.getString("participant_gender"));
            _news.setSubdistrict(result.getString("sub_district"));
            _news.setAlternate_mobile_no(result.getString("alternate_mobile_no"));

            _news.setLandline(result.getString("landline_no"));
            _news.setResidential_address(result.getString("residential_address"));
            _news.setResidential_address2(result.getString("residential_address2"));
            _news.setResidentialLandmark(result.getString("residential_landmark"));
            _news.setResidentialPincode(result.getString("residential_pincode"));
            _news.setResidentialCityName(result.getString("residential_city"));
            _news.setResidentialState(result.getString("residential_state"));
            _news.setDateofanniversary(result.getString("anniversary_date"));
            _news.setSpouse(result.getString("spouse_name"));
            _news.setDb_name(result.getString("db_name"));
            _news.setDb_code(result.getString("db_code"));

            _news.setNoofchildren(result.getString("no_of_children"));
            _news.setLuckydraw_a1(result.getString("lucky_draw_a1"));
            _news.setLuckydraw_a2(result.getString("lucky_draw_a2"));


            //   _news.setNomini(result.getString("nominee_name"));
            //  _news.setNominirely(result.getString("nominee_relation"));
            //_news.setMothername(result.getString("mothers_maiden_name"));


            _news.setWorkshopname(result.getString("workshop_name"));

            _news.setState(result.getString("state"));
            _news.setDistrict(result.getString("district"));
            _news.setPincode(result.getString("pincode"));
//                                _news.setTaluka(result.getString("taluka"));
            _news.setCity(result.getString("city"));
            _news.setShopadd(result.getString("workshop_address"));
            _news.setLandmark(result.getString("landmark"));
            _news.setMobile_no(result.getString("mobile_no"));

            _news.setEmail(result.getString("email_id"));
            //   _news.setSector(result.getString("sector"));
            // _news.setSpecialization(result.getString("specialization"));
            _news.setDob(result.getString("dob"));
            _news.setRegdate(result.getString("record_date"));
            // _news.setToatalsperconpermonth(result.getString("total_consumption"));
            // _news.setSperpartconformmvehicpermonth(result.getString("spare_consumption"));
            //  _news.setMmgenuspareconpermonth(result.getString("genuine_consumption"));
            //  _news.setTotalvehicalpermonth(result.getString("total_vehicles"));

//                                _news.setNoofmechanics(result.getString("no_of_mechanics"));
            _news.setTotal_point(result.getString("earned_points"));
            _news.setBalance_points(result.getString("points"));
            _news.setRedeem_point(result.getString("redeemed_points"));


            _news.setStatus(result.getString("status"));

            _news.setParticipant_code(result.getString("retailer_code"));
            _news.setParticipant_id_pk(result.getString("participant_id_pk"));
            //    _news.setForm_fillup_date(result.getString("form_fillup_date"));

            circularList.add(_news);

           /* if (circularList.size() <= 0) {
                txtMsg.setVisibility(View.VISIBLE);
                adapter = new CircularListAdapter(getActivity(), circularList);
                listView.setAdapter(adapter);
            } else {

                txtMsg.setVisibility(View.GONE);
                adapter = new CircularListAdapter(getActivity(), circularList);
                listView.setAdapter(adapter);
            }
*/
            final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
            try {
                ctdUser.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (jObj.has("participant_claim_history")) {
                ctdUser.deleteFromClaimHistory(result.getString("login_id_pk"));
                ctdUser.insertIntoHomework(result, jObj.getJSONArray("participant_claim_history").toString());
                //ctdUser.insertBulkClaimHistry(jObj.getJSONArray("participant_claim_history"), getActivity(), result.getString("login_id_pk"));

                ctdUser.close();
            } else {
                ctdUser.insertIntoHomework(result, "");
                ctdUser.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseData(JSONObject jObj) {
        Log.e("In", "parseobject: " + jObj);
        try {
            circularList = new ArrayList<UserModel>();
            UserModel _news = new UserModel();
            strid = Integer.parseInt(result.getString("login_id_pk"));
            _news.setId(Integer.parseInt(result.getString("login_id_pk")));

            if (result.has("image_path")) {
                _news.setPicture(result.getString("image_path"));
            } else {
                _news.setPicture("");
            }
            _news.setUserfname(result.getString("first_name"));
            _news.setUserlname(result.getString("last_name"));

            _news.setGender(result.getString("participant_gender"));
            _news.setSubdistrict(result.getString("sub_district"));
            _news.setAlternate_mobile_no(result.getString("alternate_mobile_no"));

            _news.setLandline(result.getString("landline_no"));
            _news.setResidential_address(result.getString("residential_address"));
            _news.setResidential_address2(result.getString("residential_address2"));
            _news.setResidentialLandmark(result.getString("residential_landmark"));
            _news.setResidentialPincode(result.getString("residential_pincode"));
            _news.setResidentialCityName(result.getString("residential_city"));
            _news.setResidentialState(result.getString("residential_state"));
            _news.setDateofanniversary(result.getString("anniversary_date"));
            _news.setSpouse(result.getString("spouse_name"));
            _news.setDb_name(result.getString("db_name"));
            _news.setDb_code(result.getString("db_code"));

            _news.setNoofchildren(result.getString("no_of_children"));
            _news.setLuckydraw_a1(result.getString("lucky_draw_a1"));
            _news.setLuckydraw_a2(result.getString("lucky_draw_a2"));


            //  Toast.makeText(getActivity(),""+result.get("participant_gender"),Toast.LENGTH_LONG).show();
            // _news.setNomini(result.getString("nominee_name"));
            // _news.setNominirely(result.getString("nominee_relation"));
            // _news.setMothername(result.getString("mothers_maiden_name"));


            _news.setWorkshopname(result.getString("workshop_name"));

            _news.setState(result.getString("state"));
            _news.setDistrict(result.getString("district"));
            _news.setPincode(result.getString("pincode"));
            //  _news.setTaluka(result.getString("taluka"));
            _news.setCity(result.getString("city"));
            _news.setShopadd(result.getString("workshop_address"));
            _news.setLandmark(result.getString("landmark"));
            _news.setMobile_no(result.getString("mobile_no"));

            _news.setEmail(result.getString("email_id"));
            //  _news.setSector(result.getString("sector"));
            //  _news.setSpecialization(result.getString("specialization"));
            _news.setDob(result.getString("dob"));
            _news.setRegdate(result.getString("record_date"));
            // _news.setToatalsperconpermonth(result.getString("total_consumption"));
            //  _news.setSperpartconformmvehicpermonth(result.getString("spare_consumption"));
            //  _news.setMmgenuspareconpermonth(result.getString("genuine_consumption"));
            //  _news.setTotalvehicalpermonth(result.getString("total_vehicles"));

            // _news.setMahindravehicalpermonth(result.getString("client_vehicles"));
            // _news.setNoofmechanics(result.getString("no_of_mechanics"));
            _news.setTotal_point(result.getString("earned_points"));
            _news.setBalance_points(result.getString("points"));
            _news.setRedeem_point(result.getString("redeemed_points"));


            _news.setStatus(result.getString("status"));

            _news.setParticipant_code(result.getString("retailer_code"));
            _news.setParticipant_id_pk(result.getString("participant_id_pk"));
            //  _news.setForm_fillup_date(result.getString("form_fillup_date"));

            circularList.add(_news);

           /* if (circularList.size() <= 0) {
                txtMsg.setVisibility(View.VISIBLE);
                adapter = new CircularListAdapter(getActivity(), circularList);
                listView.setAdapter(adapter);
            } else {

                txtMsg.setVisibility(View.GONE);
                adapter = new CircularListAdapter(getActivity(), circularList);
                listView.setAdapter(adapter);
            }*/

            final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
            try {
                ctdUser.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (jObj.has("participant_claim_history")) {
                ctdUser.deleteFromClaimHistory(result.getString("login_id_pk"));
                ctdUser.insertIntoHomework(result, jObj.getJSONArray("participant_claim_history").toString());
                // ctdUser.insertBulkClaimHistry(jObj.getJSONArray("participant_claim_history"), getActivity(), result.getString("login_id_pk"));

                ctdUser.close();
            } else {
                ctdUser.insertIntoHomework(result, "");
                ctdUser.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void alertDialog2(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        etMobileNo.setText("");
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

   /* @Override
    public void onResume() {
     //   super.onResume();

    }*/

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void replacfragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}




