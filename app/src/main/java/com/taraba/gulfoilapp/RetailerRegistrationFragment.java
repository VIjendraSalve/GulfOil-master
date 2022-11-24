package com.taraba.gulfoilapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.taraba.gulfoilapp.adapter.RetailerListAdapter;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.model.RegistrationDetail;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by android on 7/5/16.
 */
public class RetailerRegistrationFragment extends Fragment {
    public static ArrayList<RegistrationDetail> retailerList;
    public static int strid, position1;
    TextView txtMsg;
    EditText edt_search;
    LinearLayout vw_head;
    Button btn_search;
    public static String edit;
    private ListView listView;
    private ArrayList<RegistrationDetail> searchlist;
    private RetailerListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.retailer_list, container, false);
        edt_search = (EditText) view.findViewById(R.id.edt_search);
        txtMsg = (TextView) view.findViewById(R.id.txt_msg1);
        vw_head = (LinearLayout) view.findViewById(R.id.vw_head);
        btn_search = (Button) view.findViewById(R.id.btn_search);


        listView = (ListView) view.findViewById(R.id.lstRetailerList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment notFrag1 = new EditRegistrationRetailer();

                position1 = position;
                Log.e("list click position", "" + position1);


               /* Fragment editRetailerFragment = new EditRegistrationRetailer();
                Bundle bundle = new Bundle();
                bundle.putString("edit", "list");
                editRetailerFragment.setArguments(bundle);*/


                SharedPreferences preferences1 = getActivity().getSharedPreferences("Retailer", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences1.edit();
                editor.putString("edit", "list");
                editor.commit();


              /*  Bundle  bundl = new Bundle();
                bundl.putString("edit", "list");

                EditRegistrationRetailer dv = new EditRegistrationRetailer();
                dv.setArguments(bundl);*/

                FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.container_body, notFrag1);

                ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft1.addToBackStack(null);

                ft1.commit();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_key = edt_search.getText().toString();
                if (search_key.equals("")) {
                    new RetailerDetails().execute();
                } else {
                    ArrayList<RegistrationDetail> retailerList1 = new ArrayList<RegistrationDetail>();
                    try {
                        for (int i = 0; i < searchlist.size(); i++) {
                            if (searchlist.get(i).getRetailer_code().equalsIgnoreCase(search_key)) {
                                Log.e("key found", "ok found");
                                retailerList1.add(searchlist.get(i));
                            }
                        }

                    } catch (Exception e) {
                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "Enter Valid Dealer Code", "Gulf Oil");
                        cdd.show();
                    }
                    if (!retailerList1.isEmpty()) {
                        adapter = new RetailerListAdapter(getActivity(), retailerList1);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        listView.setVisibility(View.VISIBLE);
                        txtMsg.setVisibility(View.GONE);

                    } else {
                        listView.setVisibility(View.GONE);
                        txtMsg.setVisibility(View.VISIBLE);
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

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edt_search.getText().toString().trim().equals("")) {
//                    mLinearFilter.removeAllViewsInLayout();
                    new RetailerDetails().execute();
                }

            }
        });


        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            SharedPreferences preferences1 = getActivity().getSharedPreferences(
                    "signupdetails", getActivity().MODE_PRIVATE);
            String loginId = preferences1.getString("usertrno", "");
            new RetailerDetails().execute();

           /* SharedPreferences preferences1 = getActivity().getSharedPreferences(
                    "signupdetails", getActivity().MODE_PRIVATE);
            String loginId = preferences1.getString("usertrno", "");
            String user_type = preferences1.getString("user_type", "");

            new RetailerDetails().execute(new String[]{"9730489966", "Search By Mobile Number", ""+user_type, ""+loginId});*/
        } else {
            Toast.makeText(getActivity(),
                    "Internet Disconnected", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private class RetailerDetails extends AsyncTask<String[], Void, JSONObject> {
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
            Log.e("result", "" + params);
            try {

                SharedPreferences preferences1 = getActivity().getSharedPreferences(
                        "signupdetails", getActivity().MODE_PRIVATE);
                String loginId = preferences1.getString("usertrno", "");


                jObj = new UserFunctions().GetPendingParticipant("" + loginId);

                Log.e("jobj", "" + jObj);


            } catch (Exception e) {
                // TODO: handle exception
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(),
                                "Network Error...", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if (jsonObject != null) {
                try {
                    String mStringStatus = jsonObject.getString("status");
                    if (mStringStatus.equals("failure")) {
                        progressDialog.dismiss();

                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jsonObject.getString("error"), "Gulf Oil");
                        cdd.show();


                    } else if (mStringStatus.equals("success")) {

                        JSONArray resultJArray = jsonObject.getJSONArray("participant_data");
                        Log.e("particpant_data : ", "particpant_data : " + resultJArray);

                        retailerList = new ArrayList<RegistrationDetail>();
                        searchlist = new ArrayList<>();
                        for (int i = 0; i < resultJArray.length(); i++) {

                            JSONObject result = resultJArray.getJSONObject(i);
                            Log.e("result : ", "result : " + result);


                            RegistrationDetail _news = new RegistrationDetail();

                            strid = Integer.parseInt(result.getString("participant_id_pk"));
                            _news.setParticipant_id_pk(result.getInt("participant_id_pk"));
                            _news.setRetailer_code(result.getString("retailer_code"));
                            _news.setLogin_id_fk(result.getString("login_id_fk"));
                            _news.setWorkshop_name(result.getString("workshop_name"));
                            _news.setWorkshop_address(result.getString("workshop_address"));
                            _news.setWorkshop_address2(result.getString("workshop_address2"));

                            _news.setEmail_id(result.getString("email_id"));
                            _news.setMobile_no(result.getString("mobile_no"));
                            _news.setAlternate_mobile_no(result.getString("alternate_mobile_no"));
                            _news.setLandline_no(result.getString("landline_no"));
                            _news.setAnniversary_date(result.getString("anniversary_date"));
                            _news.setState(result.getString("state"));
                            _news.setDistrict(result.getString("district"));
                            _news.setCity(result.getString("city"));
                            _news.setSub_district(result.getString("sub_district"));
                            _news.setDob(result.getString("dob"));
                            _news.setPincode(result.getString("pincode"));
                            //      _news.setCountry(result.getString("country"));
                            _news.setLandmark(result.getString("landmark"));
                            _news.setClassification(result.getString("classification"));
                            _news.setType(result.getString("type"));
                            _news.setSpouse_name(result.getString("spouse_name"));
                            _news.setNo_of_children(result.getString("no_of_children"));
                            _news.setParticipant_gender(result.getString("participant_gender"));
                            _news.setResidential_address(result.getString("residential_address"));
                            _news.setResidential_address2(result.getString("residential_address2"));
                            _news.setResidential_landmark(result.getString("residential_landmark"));
                            _news.setResidential_pincode(result.getString("residential_pincode"));
                            _news.setResidential_city(result.getString("residential_city"));
                            _news.setResidential_state(result.getString("residential_state"));
                            _news.setCommunication_consent(result.getString("communication_consent"));
                            _news.setLucky_draw_consent(result.getString("lucky_draw_consent"));
                            //  _news.setLucky_draw_a1(result.getString("lucky_draw_a1"));
                            _news.setLucky_draw_a1("option1");
                            // _news.setLucky_draw_a2(result.getString("lucky_draw_a2"));
                            _news.setLucky_draw_a2("Option2");
                            _news.setStatus(result.getString("status"));
                            _news.setDb_code(result.getString("db_code"));
                            _news.setDb_name(result.getString("db_name"));

                            _news.setFull_name("" + result.getString("first_name"));

                            retailerList.add(_news);
                            searchlist.add(_news);
                            Log.e("retailerlist", "" + retailerList);

                        }


                        if (retailerList.size() <= 0) {
                            //txtMsg.setVisibility(View.VISIBLE);

                            Toast.makeText(getActivity(), "No Record", Toast.LENGTH_LONG).show();
                            adapter = new RetailerListAdapter(getActivity(), retailerList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        } else {

                            // txtMsg.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            txtMsg.setVisibility(View.GONE);
                            adapter = new RetailerListAdapter(getActivity(), retailerList);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }


                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            } // end onpostecxe
        }



           /* retailerList = new ArrayList<RegistrationDetail>();

            RegistrationDetail _news = new RegistrationDetail();

                _news.setFull_name("");

            retailerList.add(_news);
            if (retailerList.size() <= 0) {
                //txtMsg.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(),"No Record",Toast.LENGTH_LONG).show();
                adapter = new RetailerListAdapter(getActivity(), retailerList);
                listView.setAdapter(adapter);
                progressDialog.dismiss();
            } else {

               // txtMsg.setVisibility(View.GONE);
                adapter = new RetailerListAdapter(getActivity(), retailerList);
                listView.setAdapter(adapter);
                progressDialog.dismiss();
            }*/


    }
}

