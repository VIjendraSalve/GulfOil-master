package com.taraba.gulfoilapp.royalty_user_view.target_meter;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.TargetMeterCategory;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoyaltyTargetMeterCategoryFragment extends Fragment {

    private RecyclerView rv_target_meter_category;
    private List<TargetMeterCategory> targetMeterCategoryList;
    private RoyaltyTargetMeterCategoryAdapter adapter;
    private String loginId;

    public RoyaltyTargetMeterCategoryFragment() {
        // Required empty public constructor
    }

    public static RoyaltyTargetMeterCategoryFragment newInstance(String loginID) {
        RoyaltyTargetMeterCategoryFragment tmcf = new RoyaltyTargetMeterCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("loginId", loginID);
        tmcf.setArguments(bundle);
        return tmcf;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            loginId = getArguments().getString("loginId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_royalty_target_meter_category, container, false);

        init(view);
        callTargetMeterCategoryWS();

        return view;
    }

    private void callTargetMeterCategoryWS() {
        new TargetMeterCategoryTask().execute();
    }

    private void init(View view) {
        targetMeterCategoryList = new ArrayList<>();
        rv_target_meter_category = (RecyclerView) view.findViewById(R.id.rv_target_meter_category);
        rv_target_meter_category.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_target_meter_category.setHasFixedSize(true);

    }

    private void setTargetMeterCategoryAdapter() {
        adapter = new RoyaltyTargetMeterCategoryAdapter(getActivity(), targetMeterCategoryList);
        rv_target_meter_category.setAdapter(adapter);
        adapter.setOnItemClickListner(new RoyaltyTargetMeterCategoryAdapter.OnItemClickListner() {
            @Override
            public void onClick(int position) {
                replaceFragment(RoyaltyTargetViewFragment.newInstance(loginId, targetMeterCategoryList.get(position)));
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction ft4 = getFragmentManager().beginTransaction();
        ft4.replace(R.id.container_body, fragment);
        ft4.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft4.addToBackStack(null);
        ft4.commit();
    }

    class TargetMeterCategoryTask extends AsyncTask<String[], Void, JSONObject> {
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
                jObj = new UserFunctions().TargetMeterCategoryWS();

                Log.e("", "Target Master Category Response:---" + jObj);
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


            progressDialog.dismiss();

            if (jObj != null) {
                try {
                    if (jObj.getString("status").equals("success")) {
                        JSONArray jsonArray = jObj.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            targetMeterCategoryList.add(new TargetMeterCategory(jsonArray.getJSONObject(i)));
                        }

                        setTargetMeterCategoryAdapter();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
