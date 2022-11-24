package com.taraba.gulfoilapp.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.SchemeLetterAdapter;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.model.SchemeLetter;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchemeLetterFragment extends Fragment implements SchemeLetterAdapter.MyCallback {

    private RecyclerView rvSchemeLetter;
    private List<SchemeLetter> schemeLetterList;
    private SchemeLetterAdapter schemeletterAdapter;
    private static final String MECH_TRNO = "mech_trno";
    int mech_trno = 0;

    public SchemeLetterFragment() {
        // Required empty public constructor
    }


    public static SchemeLetterFragment newInstance(int mech_trno, boolean fromSearchMember) {
        SchemeLetterFragment orderHistoryFragment = new SchemeLetterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MECH_TRNO, mech_trno);
        orderHistoryFragment.setArguments(bundle);
        return orderHistoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scheme_letter, container, false);
        init(view);
        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GulfOilUtils.callTollFree(getActivity());
            }
        });
        return view;
    }

    private void init(View v) {

        schemeLetterList = new ArrayList<>();
        rvSchemeLetter = (RecyclerView) v.findViewById(R.id.rv_sheme_letter);
        rvSchemeLetter.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        schemeletterAdapter = new SchemeLetterAdapter(getActivity(), schemeLetterList);
        schemeletterAdapter.setMyCallback(this);
        rvSchemeLetter.setAdapter(schemeletterAdapter);
/*
        final ConnectionDetector cd = new ConnectionDetector(getActivity());

        if (cd.isConnectingToInternet()) {
            new SchemeLetterWS().execute(new String[]{"6"});
        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }*/

        if (mech_trno == 0) {
            SharedPreferences preferences = getActivity().getSharedPreferences(
                    "userinfo", Context.MODE_PRIVATE);
            mech_trno = preferences.getInt("Mechanic_trno_to_redeem", 0);
        }

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            //new SchemeLetterWS().execute(new String[]{"6"});
            new SchemeLetterWS().execute(new String[]{String.valueOf(mech_trno)});
        } else {
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void myClick(int position) {
        SchemeLetterDetailFragmet schemeLetterDetailFragmet = SchemeLetterDetailFragmet.newInstance(schemeLetterList.get(position).getId());
        replaceFragment(schemeLetterDetailFragmet);
    }

    class SchemeLetterWS extends AsyncTask<String[], Void, JSONObject> {
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
                jObj = new UserFunctions().Scheme_letter_webservice("" + params[0][0]);

                Log.e("", "schemeletter Response:---" + jObj);
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
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (jsonObject != null) {
                try {

                    Log.e("Json Data : ", "Json data : " + jsonObject);
                    String mStringStatus = jsonObject.getString("status");
                    if (mStringStatus.equals("success")) {
                        JSONArray schemeLetterJsonArray = jsonObject.getJSONArray("data");
                        if (schemeLetterJsonArray.length() > 0) {
                            for (int i = 0; i < schemeLetterJsonArray.length(); i++) {

                                schemeLetterList.add(new SchemeLetter(schemeLetterJsonArray.getJSONObject(i)));
                            }
                            Log.e("schemeletter", "LIST: " + schemeLetterList);
                            schemeletterAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "No record found!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), "" + jsonObject.getString("error"), "Gulf Oil");
                        cdd.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                progressDialog.dismiss();
            }
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft4 = getFragmentManager().beginTransaction();
        ft4.replace(R.id.container_body, fragment);
        ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft4.addToBackStack(null);
        ft4.commit();
    }
}
