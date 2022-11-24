package com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_detail.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_detail.adapter.KnowledgeCornerCategoryAdapter;
import com.taraba.gulfoilapp.royalty_user_view.knowledge_corner_detail.presenter.KnowledgeCornerDeailsPresenter;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeCornerDeailsFragment extends Fragment implements KnowledgeCornerDeailsView {
    public static final String TYPE_OF_VEHICLE = "type_of_vehilce";
    private KnowledgeCornerDeailsPresenter presenter;
    private String type_of_vehicle;
    private Spinner spn_category1;
    private Spinner spn_category2;
    private KnowledgeCornerCategoryAdapter categoryOneAdapter;
    private KnowledgeCornerCategoryAdapter categoryTwoAdapter;
    private ConnectionDetector cd;
    private TextView tv_product_name, tv_product_description;
    private ImageView iv_product_image;
    private LinearLayout ll_product_details_container;

    public static KnowledgeCornerDeailsFragment getInstance(String type) {
        KnowledgeCornerDeailsFragment deailsFragment = new KnowledgeCornerDeailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE_OF_VEHICLE, type);
        deailsFragment.setArguments(bundle);
        return deailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type_of_vehicle = getArguments().getString(TYPE_OF_VEHICLE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_knowledge_corner_deails_fragment, container, false);

        init(view);
        setListeners(view);

        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            new GetKcCategoryOne().execute();
        } else {
            Toast.makeText(getActivity(), "Internet Disconnected",
                    Toast.LENGTH_LONG).show();
        }

        return view;
    }

    private void setListeners(View view) {
        spn_category1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    new GetKcCategoryTwo().execute(spn_category1.getSelectedItem().toString());

                } else {
                    Toast.makeText(getActivity(), "Internet Disconnected",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_category2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    if (NetworkUtils.isNetworkAvailable(getActivity())) {
                        new GetKcProductDetails().execute(spn_category1.getSelectedItem().toString(), spn_category2.getSelectedItem().toString());

                    } else {
                        Toast.makeText(getActivity(), "Internet Disconnected",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void init(View view) {
        spn_category1 = view.findViewById(R.id.spn_category1);
        spn_category2 = view.findViewById(R.id.spn_category2);
        tv_product_name = view.findViewById(R.id.tv_product_name);
        tv_product_description = view.findViewById(R.id.tv_product_description);
        iv_product_image = view.findViewById(R.id.iv_product_image);
        ll_product_details_container = view.findViewById(R.id.ll_product_details_container);
    }

    class GetKcCategoryOne extends AsyncTask<Void, Void, JSONObject> {

        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            Log.e("insert :", "in pre execute of get order history");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Void... params) {
            Log.e("insert :", "in do in background of get order history");
            JSONObject jObj = null;
            try {
                jObj = new UserFunctions().getKcCategoryOne("" + type_of_vehicle);
                Log.e("", "order history json:" + jObj);

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
            Log.e("insert :", "in post execute of proceed order" + jObj);
            progressDialog.dismiss();


            if (jObj != null) {
                try {

                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("success")) {
                        JSONArray jsonArrayCategoryOne = jObj.getJSONObject("data").getJSONArray("category1_list");

                        List<String> kcCategoryOneList = new Gson().fromJson(jsonArrayCategoryOne.toString(), new TypeToken<List<String>>() {
                        }.getType());
                        categoryOneAdapter = new KnowledgeCornerCategoryAdapter(getActivity(), (ArrayList<String>) kcCategoryOneList);
                        spn_category1.setAdapter(categoryOneAdapter);

                    } else {

                       /* alertDialog2(
                                getResources().getString(R.string.app_name),
                                "" + jObj.getString("error"));*/

                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), jObj.getString("error"), "Gulf Oil");
                        cdd.show();

                        progressDialog.dismiss();
                    }

                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }


    class GetKcCategoryTwo extends AsyncTask<String, Void, JSONObject> {

        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            Log.e("insert :", "in pre execute of get order history");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(String... params) {
            Log.e("insert :", "in do in background of get order history");
            JSONObject jObj = null;

            try {
                jObj = new UserFunctions().getKcCategoryTwo("" + type_of_vehicle, params[0]);
                Log.e("", "order history json:" + jObj);

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
            Log.e("insert :", "in post execute of proceed order" + jObj);
            progressDialog.dismiss();


            if (jObj != null) {
                try {

                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("success")) {
                        JSONArray jsonArrayCategoryOne = jObj.getJSONObject("data").getJSONArray("category2_list");

                        List<String> kcCategoryTwoList = new Gson().fromJson(jsonArrayCategoryOne.toString(), new TypeToken<List<String>>() {
                        }.getType());
                        kcCategoryTwoList.add(0, "Select");
                        categoryTwoAdapter = new KnowledgeCornerCategoryAdapter(getActivity(), (ArrayList<String>) kcCategoryTwoList);
                        spn_category2.setAdapter(categoryTwoAdapter);

                    } else {

                       /* alertDialog2(
                                getResources().getString(R.string.app_name),
                                "" + jObj.getString("error"));*/

                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), jObj.getString("error"), "Gulf Oil");
                        cdd.show();

                        progressDialog.dismiss();
                    }

                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }

    class GetKcProductDetails extends AsyncTask<String, Void, JSONObject> {

        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            Log.e("insert :", "in pre execute of get order history");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(String... params) {
            Log.e("insert :", "in do in background of get order history");
            JSONObject jObj = null;

            try {
                jObj = new UserFunctions().getKcVehicleDetils("" + type_of_vehicle, params[0], params[1]);
                Log.e("", "order history json:" + jObj);

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
            Log.e("insert :", "in post execute of proceed order" + jObj);
            progressDialog.dismiss();


            if (jObj != null) {
                try {

                    String mStringStatus = jObj.getString("status");

                    if (mStringStatus.equals("success")) {
                        ll_product_details_container.setVisibility(View.VISIBLE);
                        JSONArray jsonArrayCategoryOne = jObj.getJSONObject("data").getJSONArray("details");

                        JSONObject jsonObject = jsonArrayCategoryOne.getJSONObject(0);
                        String imgUrl = jsonObject.getString("image");
                        if (!TextUtils.isEmpty(imgUrl)) {
                            Picasso.with(getActivity()).load(imgUrl).into(iv_product_image);
                        }

                        tv_product_name.setText(jsonObject.getString("title"));
                        tv_product_description.setText(jsonObject.getString("description"));

                    } else {

                       /* alertDialog2(
                                getResources().getString(R.string.app_name),
                                "" + jObj.getString("error"));*/

                        CustomOKDialog cdd = new CustomOKDialog(getActivity(), jObj.getString("error"), "Gulf Oil");
                        cdd.show();

                        progressDialog.dismiss();
                    }

                } catch (Exception e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }
}
