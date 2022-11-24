package com.taraba.gulfoilapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.adapter.CircularListAdapter;
import com.taraba.gulfoilapp.adapter.WorkshopListAdpter;
import com.taraba.gulfoilapp.contentproviders.UserModel;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by android3 on 1/5/16.
 * Modified by Mansi
 */
public class MechanicalSearchFragment extends Fragment {

    private static final String TAG = "MechanicalSearchFragment";
    EditText /*mEditTextParticepentCode,*/ mEditTextMobNo;
    /*mEditTextWorkshopName*/;
    Button mButtonSearchMech;
    String /*mStringParticepantId,*/ mStringMobileNo/*, mStringWorkshopName*/;
    public static String gender = "";
    private ListView listView;
    public static List<UserModel> circularList;
    private CircularListAdapter adapter;
    WorkshopListAdpter adapter1;
    TextView txtMsg;
    private ArrayList<String> all_workshop = new ArrayList<String>();
    int strid;
    UserTableDatasource ctdUser;
    Spinner mSpinnerSearchby;
    String mStringType;
    private ArrayList<UserModel> workshop_list;
    AlertDialog.Builder alertDialog;
    JSONArray resultJArray;
    int workshop_pos;
    JSONObject result;
    AlertDialog ad;
    Fragment fr = null;
    private InputFilter filter;
    String mobile_no, type;
    private String loginId;
    private String user_type;

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e("on ", "viewrestoresavedInstance: ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.search_mechanic, container, false);
        initComp(view);
        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });
        ctdUser = new UserTableDatasource(getActivity());
        try {
            ctdUser.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SharedPreferences preferences_search = getActivity().getSharedPreferences(
                "searchdetails", getActivity().MODE_PRIVATE);

        mobile_no = preferences_search.getString(MyTrackConstants._mStringSerchData, "");
        type = preferences_search.getString(MyTrackConstants._mStringSerchtype, "");
        mStringType = type;
        Log.e("", "Edit text search data type:" + mStringType);
        SharedPreferences preferences = getActivity().getSharedPreferences(
                "searchdetails", getActivity().MODE_PRIVATE);

        String searchData = preferences.getString(MyTrackConstants._mStringSerchData, "");
        String search_type = preferences.getString(MyTrackConstants._mStringSerchtype, "");

        if (searchData.equals("") || searchData == null) {

        } else {
            mEditTextMobNo.setText("" + searchData);
        }

        if (mobile_no.equals("") || type.equals("")) {
        } else {
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

        mSpinnerSearchby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpinnerSearchby.getSelectedItem().toString().equals("Search By Mobile Number")) {
                    mEditTextMobNo.setText("");
                    mEditTextMobNo.setHint("Enter Mobile Number");
                    mEditTextMobNo.setInputType(InputType.TYPE_CLASS_NUMBER);
                    if (mobile_no.equals("") || type.equals("")) {
                    } else {
                        mEditTextMobNo.setText("" + mobile_no);
                    }
                } else if (mSpinnerSearchby.getSelectedItem().toString().equals("Search By Dealer Code")) {
                    mEditTextMobNo.setText("");
                    mEditTextMobNo.setHint("Enter Dealer Code");
                    mEditTextMobNo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    filterEdittext();
                    mEditTextMobNo.setFilters(new InputFilter[]{filter});
                    if (mobile_no.equals("") || type.equals("")) {
                    } else {
                        mEditTextMobNo.setText("" + mobile_no);
                    }
                } else if (mSpinnerSearchby.getSelectedItem().toString().equals("Search By Shop Name")) {
                    mEditTextMobNo.setText("");
                    mEditTextMobNo.setHint("Enter Shop Name");
                    mEditTextMobNo.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    filterEdittext();
                    mEditTextMobNo.setFilters(new InputFilter[]{filter});
                    if (mobile_no.equals("") || type.equals("")) {
                    } else {
                        mEditTextMobNo.setText("" + mobile_no);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Log.e("dsdsd ", "User list : " + ctdUser.getAllUserData().size());
        mButtonSearchMech.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                hideKeyboard(getActivity());
                SharedPreferences preferences_search = getActivity().getSharedPreferences(
                        "searchdetails", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences_search.edit();
                edit.putString(MyTrackConstants._mStringSerchData, "");
                edit.putString(MyTrackConstants._mStringSerchtype, "");
                edit.commit();

                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    hideKeybord(view);
                    validate();
                } else {
                    Toast.makeText(getActivity(), "Internet connection not avalible..", Toast.LENGTH_LONG).show();
                }
            }
        });

        mEditTextMobNo.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                hasText(mEditTextMobNo);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        return view;
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

    public void initComp(View view) {
        mEditTextMobNo = (EditText) view.findViewById(R.id.edtMobNo);
        mButtonSearchMech = (Button) view.findViewById(R.id.btnSearchMech);

        listView = (ListView) view.findViewById(R.id.lstMechList);
        txtMsg = (TextView) view.findViewById(R.id.txt_msg);

        mSpinnerSearchby = (Spinner) view.findViewById(R.id.spn_searchby);

        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                "signupdetails", getActivity().MODE_PRIVATE);

        loginId = preferences1.getString("usertrno", "");
        user_type = preferences1.getString("user_type", "");
    }

    public void hideKeybord(View view) {
        view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);
        // length 0 means there is no text
        if (text.length() == 0) {
            //editText.setError("Invalid");
            return false;
        }
        return true;
    }

    private void validate() {
        mStringType = mSpinnerSearchby.getSelectedItem().toString();
        if (mEditTextMobNo.getText().toString().trim().equals("")) {
            if (mStringType.equals("Search By Mobile Number")) {
                mEditTextMobNo.setError("Enter " + getResources().getString(R.string.m_mobile_no));
            } else if (mStringType.equals("Search By Dealer Code")) {
                mEditTextMobNo.setError("Enter " + getResources().getString(R.string.m_membership_id));
            } else {
                mEditTextMobNo.setError("Enter " + getResources().getString(R.string.m_workshop_name));
            }

            mEditTextMobNo.requestFocus();
        } else if (mStringType.equals("Search By Shop Name")) {
            int length = mEditTextMobNo.getText().toString().trim().length();
            if (length < 5) {
                mEditTextMobNo.setError("Enter at least 5 character for" + getResources().getString(R.string.m_workshop_name));
            } else {

                SharedPreferences preferences_search = getActivity().getSharedPreferences(
                        "searchdetails", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences_search.edit();
                edit.putString(MyTrackConstants._mStringSerchData, "" + mEditTextMobNo.getText().toString());
                edit.putString(MyTrackConstants._mStringSerchtype, "" + mStringType);
                edit.commit();


                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    mStringMobileNo = mEditTextMobNo.getText().toString();

                    SharedPreferences preferences1 = getActivity().getSharedPreferences(
                            "signupdetails", getActivity().MODE_PRIVATE);
                    String loginId = preferences1.getString("usertrno", "");
                    String user_type = preferences1.getString("user_type", "");
                    Log.d("Result_Response3", user_type);
                    Fragment fr = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_body);
                    if (fr instanceof MechanicalSearchFragment) {
                        new SearchMechanic().execute(new String[]{mStringMobileNo, mStringType, user_type, loginId});
                    }
                }

            }
        } else {

            SharedPreferences preferences_search = getActivity().getSharedPreferences(
                    "searchdetails", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences_search.edit();
            edit.putString(MyTrackConstants._mStringSerchData, "" + mEditTextMobNo.getText().toString());
            edit.putString(MyTrackConstants._mStringSerchtype, "" + mStringType);
            edit.commit();

            if (NetworkUtils.isNetworkAvailable(getActivity())) {
                mStringMobileNo = mEditTextMobNo.getText().toString();

                SharedPreferences preferences1 = getActivity().getSharedPreferences(
                        "signupdetails", getActivity().MODE_PRIVATE);
                String loginId = preferences1.getString("usertrno", "");
                String user_type = preferences1.getString("user_type", "");

                Fragment fr = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_body);
                if (fr instanceof MechanicalSearchFragment) {
                    new SearchMechanic().execute(new String[]{mStringMobileNo, mStringType, user_type, loginId});
                }
            } else {
                Toast.makeText(getActivity(),
                        "Internet Disconnected", Toast.LENGTH_LONG).show();
            }
        }
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

                        Log.e(TAG, "onPostExecute: json response: " + jObj.toString());
                        resultJArray = jObj.getJSONArray("particpant_data");
                        Log.e("particpant_data : ", "particpant_data : " + resultJArray);


                        if (mStringType.equals("Search By Shop Name")) {
                            workshop_list = new ArrayList<UserModel>();


                            int arr_len = resultJArray.length();


                            all_workshop.clear();
                            for (int i = 0; i < arr_len; i++) {
                                UserModel _workshop = new UserModel();
                                _workshop.setWorkshopname(resultJArray.getJSONObject(i).getString("workshop_name"));
                                _workshop.setParticipant_code(resultJArray.getJSONObject(i).getString("retailer_code"));
                                _workshop.setMobile_no(resultJArray.getJSONObject(i).getString("mobile_no"));
                                workshop_list.add(_workshop);
                                Log.e("WORK : ", "WORK : " + resultJArray.getJSONObject(i).getString("workshop_name"));
                            }

//
                            alertDialog = new AlertDialog.Builder(getActivity());
                            LayoutInflater inflater = getLayoutInflater(null);
                            View convertView = (View) inflater.inflate(R.layout.list, null);
                            alertDialog.setView(convertView);
                            alertDialog.setTitle("Select Shop");

                            ListView lv = (ListView) convertView.findViewById(R.id.lv);

                            adapter1 = new WorkshopListAdpter(getActivity(), workshop_list);
                            lv.setAdapter(adapter1);

                            alertDialog.setCancelable(true);

                           /* Fragment fr = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_body);
                            if (fr instanceof MechanicalSearchFragment){
                                 ad = alertDialog.show();
                            }*/

                            ad = alertDialog.show();
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    workshop_pos = position;
                                    ad.dismiss();
                                    Log.e("IDD : ", "IDD : " + String.valueOf(position));
                                    try {
                                        result = resultJArray.getJSONObject(workshop_pos);
                                        Log.e("result : ", "result : " + result + " position: " + workshop_pos);
                                        SharedPreferences preferences_search = getActivity().getSharedPreferences(
                                                "searchdetails", getActivity().MODE_PRIVATE);
                                        SharedPreferences.Editor edit = preferences_search.edit();
                                        edit.putString("jsonResult_main", "" + jObj);
                                        edit.putInt("workshop_pos", workshop_pos);
                                        edit.putString("workshop_result", "" + result);
                                        edit.commit();

                                        Log.e("jsonResult_main", "prefs: " + preferences_search.getString("jsonResult_main", ""));
                                        Log.e("workshop_pos", "prefs: " + preferences_search.getInt("workshop_pos", 0));
                                        Log.e("workshop_result", "prefs: " + preferences_search.getString("workshop_result", ""));
                                        parseData(jObj);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }// end of if search by Shop name
                        else {
                            try {
                                result = resultJArray.getJSONObject(0);
                                Log.e("result : ", "result : " + result);

                                SharedPreferences preferences_search = getActivity().getSharedPreferences(
                                        "searchdetails", getActivity().MODE_PRIVATE);
                                SharedPreferences.Editor edit = preferences_search.edit();
                                edit.putString("jsonResult_main", "" + jObj);
                                edit.putInt("workshop_pos", -1);
                                edit.putString("workshop_result", "" + result);
                                edit.commit();

                                Log.e("jsonResult_main", "prefs: " + preferences_search.getString("jsonResult_main", ""));
                                Log.e("workshop_pos", "prefs: " + preferences_search.getInt("workshop_pos", 0));
                                Log.e("workshop_result", "prefs: " + preferences_search.getString("workshop_result", ""));

                                parseDataOtherthanShop(jObj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        progressDialog.dismiss();
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

            if (circularList.size() <= 0) {
                txtMsg.setVisibility(View.VISIBLE);
                adapter = new CircularListAdapter(getActivity(), circularList);
                adapter.setUserType(user_type);
                listView.setAdapter(adapter);
            } else {

                txtMsg.setVisibility(View.GONE);
                adapter = new CircularListAdapter(getActivity(), circularList);
                adapter.setUserType(user_type);
                listView.setAdapter(adapter);
            }

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

            if (circularList.size() <= 0) {
                txtMsg.setVisibility(View.VISIBLE);
                adapter = new CircularListAdapter(getActivity(), circularList);
                adapter.setUserType(user_type);
                listView.setAdapter(adapter);
            } else {

                txtMsg.setVisibility(View.GONE);
                adapter = new CircularListAdapter(getActivity(), circularList);
                adapter.setUserType(user_type);
                listView.setAdapter(adapter);
            }

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
                        mEditTextMobNo.setText("");
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
}
