package com.taraba.gulfoilapp;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.taraba.gulfoilapp.adapter.CircularListAdapter;
import com.taraba.gulfoilapp.adapter.SpinnerDataAdapter;
import com.taraba.gulfoilapp.contentproviders.Database;
import com.taraba.gulfoilapp.contentproviders.StateTableDatasource;
import com.taraba.gulfoilapp.contentproviders.UserModel;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;

import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by android on 12/18/15.
 * Modified by Mansi
 */
public class MainFragment extends Fragment {

    Spinner mSpinnerState, mSpinnerDistrict;
    // DataBaseHelper mDatabaseHelper;
    ArrayList<String> states;
    ArrayList<ArrayList<String>> statesfinal, districtfinal;
    private ListView listView;
    private List<UserModel> circularList;
    private CircularListAdapter adapter;
    TextView txtMsg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.main_fragment, container, false);

        initComp(view);

        //DataBaseHelper db = new DataBaseHelper(getActivity());
        final StateTableDatasource ctd = new StateTableDatasource(getActivity());
        ctd.open();
        /*if (ctd.getAllCirculars().size() <= 0) {
            ctd.setTempData();
        }*/

        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
        try {
            ctdUser.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (ctdUser.getAllUserData().size() <= 0) {
            // ctdUser.setTempData();
        }
        //newsListDivider = findViewById(R.id.newsListDivider);
       /* circularList = new ArrayList<UserModel>();

        circularList= ctdUser.getAllUserData();
        Log.e("adapter circularList","circularList :" +circularList);
        adapter = new CircularListAdapter(getActivity(), circularList);
        listView.setAdapter(adapter);*/

        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        String mStringType = preferences1.getString("user_type", "");
        Log.e("type in main:", "User Type : " + mStringType);
        states = new ArrayList<String>();

        states = ctd.getDataFromTableForSpinnerNew(Database.USER_TABLE, new String[]{"distinct(state)"}, "type='" + mStringType + "'", "state asc");
        statesfinal = convertState(states.get(0).toString());
        //states.add(0, "--Select State--");
        Log.e("Satate Data :", "State : " + states);
        Log.e("Satate Data :", "State string : " + statesfinal);
        // ArrayAdapter<String> stateTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.email_layout, R.id.text_name, statesfinal);

        mSpinnerState.setAdapter(new SpinnerDataAdapter(getActivity(),
                statesfinal, "spsearchBy"));

        //mSpinnerState.setAdapter(stateTypeAdapter);

        mSpinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                ArrayList<String> district = new ArrayList<String>();

               /* if (mSpinnerState.getSelectedItem().toString().startsWith("--")) {

                } else {
                    ctd.open();
                  //  district = ctd.getDataFromTableForSpinner(Database.USER_TABLE, new String[]{"distinct(district)"}, " LOWER(state)='" + mSpinnerState.getSelectedItem().toString().toLowerCase() + "'", "district asc");

                }*/
                //}

                districtfinal = convertDistrict(states.get(0).toString());
                //String strselectedstate = arg0.getItemAtPosition(arg2).toString();
                //states.add(0, "--Select State--");

                Log.e("Satate Data :", "State string : " + districtfinal + " : ");
                // ArrayAdapter<String> stateTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.email_layout, R.id.text_name, statesfinal);

                mSpinnerDistrict.setAdapter(new SpinnerDataAdapter(getActivity(),
                        districtfinal, "spsearchBy"));
                /*ArrayAdapter<String> districtTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.email_layout, R.id.text_name, district);
                mSpinnerDistrict.setAdapter(districtTypeAdapter);*/


                // String strselectedstate =  statesfinal.get(mSpinnerState.getSelectedItemPosition()).get(0);
                // Log.e("Satate Data :", "State strselectedstate : " + strselectedstate);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        mSpinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                ArrayList<String> district = new ArrayList<String>();

               /* if (mSpinnerDistrict.getSelectedItem().toString().startsWith("--")) {

                } else {*/
                try {
                    ctdUser.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                circularList = new ArrayList<UserModel>();
                String strselectedstate = districtfinal.get(mSpinnerDistrict.getSelectedItemPosition()).get(0);
                //  Log.e("Satate Data :", "State strselectedstate : " + strselectedstate);
                Log.e(" Data :", "State spsearchBy: " + statesfinal.get(mSpinnerState.getSelectedItemPosition()).get(0));
                Log.e(" Data :", "District Spinner:" + districtfinal.get(mSpinnerDistrict.getSelectedItemPosition()).get(0));

                circularList = ctdUser.getSelectedStateDistrict(statesfinal.get(mSpinnerState.getSelectedItemPosition()).get(0).toString().toLowerCase(), districtfinal.get(mSpinnerDistrict.getSelectedItemPosition()).get(0).toString().toLowerCase());

                Log.e(" Data :", "circularList :" + circularList);
                //  }
                //}

                if (circularList.size() <= 0) {
                    txtMsg.setVisibility(View.VISIBLE);
                    adapter = new CircularListAdapter(getActivity(), circularList);
                    listView.setAdapter(adapter);
                } else {
                    txtMsg.setVisibility(View.GONE);
                    adapter = new CircularListAdapter(getActivity(), circularList);
                    listView.setAdapter(adapter);
                }
                /*ArrayAdapter<String> districtTypeAdapter = new ArrayAdapter<String>(getActivity(), R.layout.email_layout, R.id.text_name, district);
                mSpinnerDistrict.setAdapter(districtTypeAdapter);*/
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        ctd.close();
        ctdUser.close();
        return view;
    }

    public void initComp(View view) {
        mSpinnerState = (Spinner) view.findViewById(R.id.state);
        mSpinnerDistrict = (Spinner) view.findViewById(R.id.district);
        listView = (ListView) view.findViewById(R.id.lstMain);
        txtMsg = (TextView) view.findViewById(R.id.txt_msg);
    }


    public ArrayList<ArrayList<String>> convertState(String string) {
        ArrayList<ArrayList<String>> ModelModel = new ArrayList<>();
        try {
            JSONObject jobjcat = new JSONObject(string);
            JSONObject jobstate = jobjcat.getJSONObject("state");
            //  JSONArray mJsonArray = new JSONArray(string);
            Log.e("Whole JSON ARRAy", "Model data :" + jobstate);
            Log.e("Whole ", "Whole JSon Size : " + jobstate.length());
            Iterator<String> iterator = jobstate.keys();
            while (iterator.hasNext()) {
                ArrayList<String> Cat = new ArrayList<String>();
                String key = iterator.next();
                Log.i("TAG", "key:" + key + "--Value::" + jobstate.optString(key));

                Cat.add(jobstate.optString(key));

                ModelModel.add(Cat);
            }

            /*for (int i = 0; i < jobstate.length(); i++) {
                ArrayList<String> Cat=new ArrayList<String>();
              //  String strstate = jobjcat.getString("state");

                Log.e("JSON json object", "model : "+jobstate);
                Cat.add(jobstate.getString("maharashtra"));
                //= CategoryModel + jobjcat.getString("Categories") + "-";
                //JSONArray mJsonModel = jobjcat.getJSONArray("model");
                //String S1 =jobjcat.getJSONArray("ProductModel").toString();
                //Cat.add(mJsonModel.toString());
						*//*		for (int j = 0; j < mJsonProduct.length(); j++) {
                            JSONObject jobjmodel = mJsonProduct.getJSONObject(j);
								//CategoryModel = CategoryModel + jobjmodel.getString("Product") + "-";
								Cat.add(jobjmodel.getString("Product"));
								JSONArray mJsonModel = jobjmodel.getJSONArray("Models");
						for(int k=0; k< mJsonModel.length(); k++)
						{
							Log.e("JSON json object", "Product : "+jobjmodel);
							CategoryModel = CategoryModel + mJsonModel.getJSONObject(k).getString("model") + ",";
						}
						CategoryModel = CategoryModel.substring(0, CategoryModel.lastIndexOf(",")) + ",\n";
						}*//*

                ModelModel.add(Cat);
            }*/

            //CategoryModel = CategoryModel.substring(0, CategoryModel.lastIndexOf(","));
            Log.e("Category Data : ", "Model Data Cnverted : " + ModelModel);
        } catch (Exception e) {

        }
        return ModelModel;
    }

    public ArrayList<ArrayList<String>> convertDistrict(String string) {
        ArrayList<ArrayList<String>> ModelModel = new ArrayList<>();
        try {
            JSONObject jobjcat = new JSONObject(string);
            JSONObject jobdistrict = jobjcat.getJSONObject("district");
            //  JSONArray mJsonArray = new JSONArray(string);
            Log.e("Whole JSON ARRAy", "Model data :" + jobdistrict);
            Log.e("Whole ", "Whole JSon Size : " + jobdistrict.length());
            Iterator<String> iterator = jobdistrict.keys();
            while (iterator.hasNext()) {
                ArrayList<String> Cat = new ArrayList<String>();
                String key = iterator.next();
                Log.i("TAG", "key:" + key + "--Value::" + jobdistrict.optString(key));

                Cat.add(jobdistrict.optString(key));

                ModelModel.add(Cat);
            }

            /*for (int i = 0; i < jobdistrict.length(); i++) {
                ArrayList<String> Cat=new ArrayList<String>();
                //  String strstate = jobjcat.getString("state");

                Log.e("JSON json object", "model : "+jobdistrict);
                Cat.add(jobdistrict.getString("akola"));
                //= CategoryModel + jobjcat.getString("Categories") + "-";
                //JSONArray mJsonModel = jobjcat.getJSONArray("model");
                //String S1 =jobjcat.getJSONArray("ProductModel").toString();
                //Cat.add(mJsonModel.toString());
						*//*		for (int j = 0; j < mJsonProduct.length(); j++) {
                            JSONObject jobjmodel = mJsonProduct.getJSONObject(j);
								//CategoryModel = CategoryModel + jobjmodel.getString("Product") + "-";
								Cat.add(jobjmodel.getString("Product"));
								JSONArray mJsonModel = jobjmodel.getJSONArray("Models");
						for(int k=0; k< mJsonModel.length(); k++)
						{
							Log.e("JSON json object", "Product : "+jobjmodel);
							CategoryModel = CategoryModel + mJsonModel.getJSONObject(k).getString("model") + ",";
						}
						CategoryModel = CategoryModel.substring(0, CategoryModel.lastIndexOf(",")) + ",\n";
						}*//*

                ModelModel.add(Cat);
            }*/

            //CategoryModel = CategoryModel.substring(0, CategoryModel.lastIndexOf(","));
            Log.e("Category Data : ", "Model Data Cnverted : " + ModelModel);
        } catch (Exception e) {
        }
        return ModelModel;
    }
}


