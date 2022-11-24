package com.taraba.gulfoilapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.model.PointsCalculatorMaster;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.PointsCalculatorUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PointsCalculatorFragment extends Fragment {
    private static final String TAG = "PointsCalculatorFragmen";
    private EditText edt_quantity;
    private Spinner sp_pack_size_description;
    private Spinner sp_brand_description;
    private Spinner sp_product_segment_description;
    private Spinner sp_product_category;
    private Button btn_calculate_points;
    private LinearLayout ll_point_calculator_result;
    private TextView tv_points_result;
    private ArrayAdapter<String> productCategoryAdapter;
    private ArrayAdapter<String> productSegmentDescdAdapter;
    private ArrayAdapter<String> brandDescAdapter;
    private ArrayAdapter<String> packSizeDescAdapter;
    private List<String> productCategoryList;
    private List<String> productSegmentDescList;
    private List<String> brandDescList;
    private List<String> packSizeDescList;
    private List<PointsCalculatorMaster> pointsCalculatorMasterList;

    public PointsCalculatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_points_calculator, container, false);
        init(view);
        getMasters();
        setListners();
        return view;
    }

    private void getMasters() {
        new PointsMasterTask().execute();
    }

    private void setListners() {
        sp_product_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    setProductSegmentDesAdapter();
                } else {
                    sp_product_segment_description.setAdapter(null);
                    sp_brand_description.setAdapter(null);
                    sp_pack_size_description.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_product_segment_description.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    setBrandDescAdapter();
                } else {
                    sp_brand_description.setAdapter(null);
                    sp_pack_size_description.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_brand_description.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    setPackSizeDescAdapter();
                } else {
                    sp_pack_size_description.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_calculate_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_point_calculator_result.setVisibility(View.GONE);
                tv_points_result.setText("");
                if (validateFields()) {
                    if (NetworkUtils.isNetworkAvailable(getActivity())) {
                        calculatePointsWS();
                    } else {
                        Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void setPackSizeDescAdapter() {
        packSizeDescList = new PointsCalculatorUtils().getPackSizeList(pointsCalculatorMasterList, sp_product_category.getSelectedItem().toString(), sp_product_segment_description.getSelectedItem().toString(), sp_brand_description.getSelectedItem().toString());
        packSizeDescAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, packSizeDescList);
        sp_pack_size_description.setAdapter(packSizeDescAdapter);
    }

    private void setBrandDescAdapter() {
        brandDescList = new PointsCalculatorUtils().getBrandDescList(pointsCalculatorMasterList, sp_product_category.getSelectedItem().toString(), sp_product_segment_description.getSelectedItem().toString());
        brandDescAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, brandDescList);
        sp_brand_description.setAdapter(brandDescAdapter);
    }

    private void setProductSegmentDesAdapter() {
        productSegmentDescList = new PointsCalculatorUtils().getSegDesList(pointsCalculatorMasterList, sp_product_category.getSelectedItem().toString());
        productSegmentDescdAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productSegmentDescList);
        sp_product_segment_description.setAdapter(productSegmentDescdAdapter);
    }


    private void calculatePointsWS() {
        new CalculatePointsTask().execute(sp_pack_size_description.getSelectedItem().toString(), edt_quantity.getText().toString());
    }

    private boolean validateFields() {
        boolean flag = true;
        if (sp_product_category.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Select Item Category Description", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (sp_product_segment_description.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Select Item Description", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (sp_brand_description.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Select Product Classification", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (sp_pack_size_description.getSelectedItemPosition() == 0) {
            Toast.makeText(getActivity(), "Select Material Code", Toast.LENGTH_SHORT).show();
            flag = false;
        } else if (edt_quantity.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Enter Quantity", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }

    private void init(View view) {
        edt_quantity = (EditText) view.findViewById(R.id.edt_quantity);
        sp_brand_description = (Spinner) view.findViewById(R.id.sp_brand_description);
        sp_pack_size_description = (Spinner) view.findViewById(R.id.sp_pack_size_description);
        sp_product_category = (Spinner) view.findViewById(R.id.sp_product_category);
        sp_product_segment_description = (Spinner) view.findViewById(R.id.sp_product_segment_description);
        btn_calculate_points = (Button) view.findViewById(R.id.btn_calculate_points);
        ll_point_calculator_result = (LinearLayout) view.findViewById(R.id.ll_point_calculator_result);
        tv_points_result = (TextView) view.findViewById(R.id.tv_points_result);


        pointsCalculatorMasterList = new ArrayList<>();


    }

    private void setProductCategoryAdapter() {
        productCategoryList = new PointsCalculatorUtils().getCategoryList(pointsCalculatorMasterList);
        productCategoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productCategoryList);
        sp_product_category.setAdapter(productCategoryAdapter);
    }

    class PointsMasterTask extends AsyncTask<String[], Void, JSONObject> {
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
                jObj = new UserFunctions().PointsMasterWS();

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


            progressDialog.dismiss();

            if (jObj != null) {
                try {
                    if (jObj.getString("status").equals("success")) {
                        JSONArray jsonArray = jObj.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            PointsCalculatorMaster pcm = new PointsCalculatorMaster();
                            pcm.setCategory(jsonObject.getString("category"));
                            pcm.setProduct_segment_description(jsonObject.getString("product_segment_description"));
                            pcm.setBrand_description(jsonObject.getString("brand_description"));
                            pcm.setPack_size_description(jsonObject.getString("pack_size_description"));
                            pointsCalculatorMasterList.add(pcm);
                        }

                        Log.e(TAG, "onPostExecute: PCM Size: " + pointsCalculatorMasterList.size());

                        setProductCategoryAdapter();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    } // end onpostecxe

    class CalculatePointsTask extends AsyncTask<String, Void, JSONObject> {
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
        protected JSONObject doInBackground(String... params) {
            JSONObject jObj = null;
            try {
                jObj = new UserFunctions().calculatePointsWS(params[0].toString(), params[1].toString());

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


            progressDialog.dismiss();

            if (jObj != null) {
                try {
                    if (jObj.getString("status").equals("success")) {
                        String totalPoints = jObj.getJSONObject("data").getString("total_points");
                        ll_point_calculator_result.setVisibility(View.VISIBLE);
                        tv_points_result.setText(totalPoints);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
