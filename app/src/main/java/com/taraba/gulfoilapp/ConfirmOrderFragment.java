package com.taraba.gulfoilapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by android on 3/15/16.
 * Modified by Mansi
 */
public class ConfirmOrderFragment extends Fragment {

    RadioButton rdbtn_resident_address, rdbtn_workshop_address;
    TextView txt_resident_address, txt_workshop_address;
    Button btn_proceed, btn_cancel;
    SharedPreferences PREF_participant_login_id;
    //List<UserModel> user ;
    String product_code, dealer_code;
    LinearLayout workshop_address_layout, resident_address_layout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_confirm_order, container, false);

        Bundle b = getArguments();
        product_code = b.getString(MyTrackConstants._mStringProductCode);

        initComp(view);
        SharedPreferences preferences = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);

        String uname = preferences.getString("uname", "");
        Log.e("Mechanic", " uname : " + uname);

        int mech_trno = preferences.getInt("Mechanic_trno_to_redeem", 0);
        Log.e("Mechanic", "Mechanic TRNO : " + mech_trno);

        SharedPreferences prefs = getActivity().getSharedPreferences("retailer_detail", Context.MODE_PRIVATE);
        dealer_code = prefs.getString("retail_code", "");
        Log.e("Retailer", "code : " + dealer_code);

        try {
            new GetAddress().execute();
        } catch (Exception e) {

        }

       /* user = new ArrayList<>();

        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
        try {
            ctdUser.open();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        user = ctdUser.getAllUserDataAddress(mech_trno);
        txt_machanic_address.setText(Html.fromHtml("Mechanic Address : <br/>"+user.get(0).getWorkshopname()+" , "+user.get(0).getLandmark()+" "+user.get(0).getCity()+" , "+user.get(0).getDistrict()+" , "+user.get(0).getPincode()));
*/
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Fragment detailsFragment =new ProceedOrderFragment();
                Bundle b=new Bundle();
                b.putString(MyTrackConstants._mStringProductCode,""+product_code);
                detailsFragment.setArguments(b);
                FragmentTransaction ftmech = getActivity().getSupportFragmentManager().beginTransaction();
                ftmech.replace(R.id.container_body, detailsFragment);
                ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftmech.addToBackStack(null);
                ftmech.commit();*/
                String addressType = "";
                if (rdbtn_resident_address.isChecked()) {
                    addressType = "resident";
                } else {
                    addressType = "workshop";
                }

                Log.e("addressType", "addressType:: " + addressType);
                SharedPreferences prefs = getActivity().getSharedPreferences("address_key", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("address_type", addressType);
                editor.commit();

                Log.e("addressType", "prefs addressType:: " + prefs.getString("address_type", ""));

                Bundle b = new Bundle();
                b.putString(MyTrackConstants._mStringProductCode, "" + product_code);
                Intent i = new Intent(getActivity(), OtpActivity.class);
                i.putExtras(b);
                startActivity(i);
                //getFragmentManager().popBackStack();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        rdbtn_resident_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rdbtn_resident_address.isChecked()) {
                    rdbtn_workshop_address.setChecked(false);
                } else {
                    rdbtn_resident_address.setChecked(false);
                }
            }
        });

        rdbtn_workshop_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rdbtn_workshop_address.isChecked()) {
                    rdbtn_resident_address.setChecked(false);
                } else {
                    rdbtn_workshop_address.setChecked(false);
                }
            }
        });

        return view;
    }

    public void initComp(View view) {
        rdbtn_resident_address = (RadioButton) view.findViewById(R.id.rdbtn_mechanic_address);
        rdbtn_workshop_address = (RadioButton) view.findViewById(R.id.rdbtn_workshop_address);
        txt_resident_address = (TextView) view.findViewById(R.id.txt_my_mechanic_address);
        txt_workshop_address = (TextView) view.findViewById(R.id.txt_my_workshop_address);
        resident_address_layout = (LinearLayout) view.findViewById(R.id.resident_address_layout);
        workshop_address_layout = (LinearLayout) view.findViewById(R.id.workshop_address_layout);
        btn_proceed = (Button) view.findViewById(R.id.btn_proceed);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
    }


    class GetAddress extends AsyncTask<Void, Void, JSONObject> {

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
                jObj = new UserFunctions().getUserAddress(dealer_code);
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
            Log.e("post :", "execute of get address details");
            // showToast("Calling set up views");
            progressDialog.dismiss();
            if (jObj != null) {
                try {

                    String status = jObj.getString("status");
                    if (status.compareTo("success") == 0) {
                        resident_address_layout.setVisibility(View.VISIBLE);
                        workshop_address_layout.setVisibility(View.VISIBLE);
                        String data = jObj.getString("data");
                        JSONObject data_object = new JSONObject(data);
                        if (data_object != null) {
                            JSONObject resident_object = new JSONObject(data_object.getString("resident"));
                            Log.e("residential_object : ", ": " + resident_object);

                            JSONObject workshop_object = new JSONObject(data_object.getString("workshop"));
                            Log.e("workshop_object : ", ": " + workshop_object);

                            if (resident_object != null) {
                                String residential_address = resident_object.getString("residential_address");
                                Log.e("residential_address : ", ": " + residential_address);
                                txt_resident_address.setText(Html.fromHtml(resident_object.getString("residential_address") + " , " +
                                        resident_object.getString("residential_address2") + " " + resident_object.getString("residential_landmark") + " " +
                                        resident_object.getString("residential_city") + " , " + resident_object.getString("residential_state") + " , " +
                                        resident_object.getString("residential_pincode")));
                            }
                            if (workshop_object != null) {

                                txt_workshop_address.setText(Html.fromHtml(workshop_object.getString("workshop_address") + " , " +
                                        workshop_object.getString("workshop_address2") + " " + workshop_object.getString("workshop_city") + " " +
                                        workshop_object.getString("workshop_state") + " , " + workshop_object.getString("workshop_pincode")));
                            }
                        }
                    } else {
                        resident_address_layout.setVisibility(View.VISIBLE);
                        workshop_address_layout.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "No address found", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }
}