package com.taraba.gulfoilapp.royalty_user_view.view_rewards;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.taraba.gulfoilapp.FlsDashboardActivity;
import com.taraba.gulfoilapp.MainDashboardActivity;
import com.taraba.gulfoilapp.MyTrackConstants;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.contentproviders.Database;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.customdialogs.CustomOKDialog;
import com.taraba.gulfoilapp.model.Product;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mansi on 3/14/16.
 * Modified by Mansi
 */
public class RewardsFragment extends Fragment implements RecyclerViewOnClickListener {

    private static final String TAG = "RedeemMainFragment";
    public static String path = "";
    public static GetAllCategory getAllCategoryAsyncTask;
    Spinner spCategory, spFilter;
    GridView grid;
    int participant_login_id;
    String mStringStatus;
    ArrayList<String> categories = new ArrayList<String>();
    SharedPreferences PREF_Category_date;
    String cat_updateDate, prod_updateDate;
    EditText edt_search;
    ImageView btn_search;
    String isDisable = "";
    private ArrayList<Product> products = new ArrayList<Product>();
    private String currentBalance;
    private boolean initialCategoryLoad = true;


    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = activity.getCurrentFocus();
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutUserWise(), container, false);
        if (getActivity() instanceof MainDashboardActivity) {
            ((MainDashboardActivity) getActivity()).hideShowView(true);
            ((MainDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_rewards));
            ((MainDashboardActivity) getActivity()).setToolbarActionTitle(getString(R.string.str_pending_orders), false);
        } else if (getActivity() instanceof FlsDashboardActivity) {
            ((FlsDashboardActivity) getActivity()).hideShowView(true);
            ((FlsDashboardActivity) getActivity()).setToolbarTitle(getString(R.string.str_rewards));
        }
        initcomp(view);
//Call to toll free number
//        view.findViewById(R.id.tvFooterTollFreeNumber).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                GulfOilUtils.callTollFree(getActivity());
//            }
//        });
        new getCurrentBal().execute();

        PREF_Category_date = getActivity().getSharedPreferences(
                "category_update_date", Context.MODE_PRIVATE);

        cat_updateDate = PREF_Category_date.getString(MyTrackConstants._mStringCategoryUpdateDate, "");
        prod_updateDate = PREF_Category_date.getString(MyTrackConstants._mStringProductUpdatedate, "");


        SharedPreferences preferences = getActivity().getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);


        mStringStatus = preferences.getString(" Mechanic_status", "");
        int mech_trno = preferences.getInt("Mechanic_trno_to_redeem", 0);
        Log.e("Mechanic ", "trno to RedeemMainFragment : " + mech_trno);

        Bundle b = getArguments();

        if (b != null) {
            isDisable = b.getString("isDisable");
            path = b.getString("path");

            /*SharedPreferences.Editor editor=preferences.edit();
            editor.putString("path",""+path);
            editor.commit();*/

            if (isDisable.equals("true")) // rewards from MSR/DSR login
            {
                //grid.setEnabled(false);
                grid.setClickable(false);
                if (getActivity() instanceof MainDashboardActivity)
                    ((MainDashboardActivity) getActivity()).setToolbarActionTitle("", true);

            } else   // rewards from mechanic login or from member search
            {
                grid.setEnabled(true);
                grid.setClickable(true);
                if (getActivity() instanceof MainDashboardActivity)
                    ((MainDashboardActivity) getActivity()).setToolbarActionTitle(getString(R.string.str_order_history), false);
            }
        }
        final ConnectionDetector cd = new ConnectionDetector(
                getActivity());
        if (cd.isNetworkAvailable(getActivity())) {

            //if (getActivity().getSupportFragmentManager().findFragmentById(R.id.container_body) instanceof RewardsFragment) {
            //new GetAllCategory().execute();

            if (getAllCategoryAsyncTask != null && getAllCategoryAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
                getAllCategoryAsyncTask.cancel(true);
            getAllCategoryAsyncTask = new GetAllCategory(); //every time create new object, as AsynTask will only be executed one time.
            getAllCategoryAsyncTask.execute();
               /* if(Build.VERSION.SDK_INT >= 11)
                {
                    getAllCategoryAsyncTask.executeOnExecutor(GetAllCategory.THREAD_POOL_EXECUTOR);
                }else {

                    getAllCategoryAsyncTask.execute();
                }*/
            //  }

        } else {
            final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
            try {
                ctdUser.open();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            categories = ctdUser.getCategoryName();
            ctdUser.close();
            spCategory.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item, categories));
            Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
        }


        grid.setOnItemClickListener((parent, view1, position, id) -> {
//    start            Added by Pravin Dharam 22-5-2017

// end
           /* hideKeyboard(getActivity());
            String name = ((Product) grid.getItemAtPosition(position)).getProduct_code();
            Log.e("Name : ", "-----------------------" + name);
            Fragment detailsFragment = new OrderConfirmDetailsFragment();
            FragmentTransaction ftmech = getActivity().getSupportFragmentManager().beginTransaction();
            ftmech.replace(R.id.container_body, detailsFragment);

            Bundle b = new Bundle();

            b.putString("isDisable", "" + isDisable);
//                b.putString("currentRedeemPoints", "" + txt_currentbal.getText());
            b.putString(MyTrackConstants._mStringProductCode, "" + name);
            detailsFragment.setArguments(b);
            ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ftmech.addToBackStack(null);
            ftmech.commit();*/
            Toast.makeText(getActivity(), "Item Click", Toast.LENGTH_SHORT).show();


        });

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(getActivity());
                if (initialCategoryLoad) {
                    initialCategoryLoad = false;
                    return;
                }
                if (categories != null) {
                    String cat_id = categories.get(position);
                    String filter = "";
                    if (spFilter.getSelectedItem().equals("Low to high")) {
                        filter = "asc";
                    } else {
                        filter = "desc";
                    }
                    final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
                    try {
                        ctdUser.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    products = ctdUser.getProductDetails(filter, "" + cat_id, "" + edt_search.getText().toString());
                    products = setColorInProducts(products);

                    ctdUser.close();
                    Log.e(TAG, "onItemSelected: Category: ");
                    setGrid(products);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(getActivity());
                String filter1 = spFilter.getSelectedItem().toString();
                String filter = "";
                if (filter1.equals("Low to high")) {
                    filter = "asc";
                } else {
                    filter = "desc";
                }
                final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
                try {
                    ctdUser.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Log.e("values : ", "VAlues : " + spCategory.getSelectedItem() + "  ");
                products = ctdUser.getProductDetails(filter, "" + spCategory.getSelectedItem(), "" + edt_search.getText().toString());
                products = setColorInProducts(products);
                ctdUser.close();
                Log.e(TAG, "onItemSelected: Filter");
                setGrid(products);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard(getActivity());

                String cat_ = spCategory.getSelectedItem().toString();
                String filter1 = spFilter.getSelectedItem().toString();
                String filter = "";
                if (filter1.equals("Low to high")) {
                    filter = "asc";
                } else {
                    filter = "desc";
                }
                final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
                try {
                    ctdUser.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String search = edt_search.getText().toString();

                products = ctdUser.getProductDetails(filter, "" + spCategory.getSelectedItem(), "" + search);

                if (products.size() == 0) {
                    /*alertDialog2("Gulf Oil","No search result found.");*/
                    CustomOKDialog cdd = new CustomOKDialog(getActivity(), "No search result found.", "Gulf Oil");
                    cdd.show();
                }
                products = setColorInProducts(products);
                ctdUser.close();
                Log.e(TAG, "onClick: Search button");
                setGrid(products);
            }
        });

        return view;
    }

    private void navigateToConfirmOrderDetailsFragment(View view, int position) {
        this.initialCategoryLoad = true;
        String product_code = ((Product) this.grid.getItemAtPosition(position)).getProduct_code();
        Bundle bundle = new Bundle();
        bundle.putString("isDisable", "" + this.isDisable);
        bundle.putString(Database.PRODUCT_TABLE_PRODUCT_CODE, "" + product_code);
        bundle.putString("currentRedeemPoints", this.currentBalance);
        Navigation.findNavController(view).navigate((int) R.id.orderConfirmationDetailsFragment, bundle);
    }

    private ArrayList<Product> setColorInProducts(ArrayList<Product> productList) {
        int colors[] = {R.color.org101, R.color.org102, R.color.org103};
        int colorPosition = 0;
        for (int i = 0; i < productList.size(); i++) {
            int temp = i + 1;
            productList.get(i).setColor(colors[colorPosition]);
            if (temp % 3 == 0) {
                colorPosition = 0;
            } else {
                ++colorPosition;
            }

        }
        return productList;
    }

    public void initcomp(View view) {
        spCategory = (Spinner) view.findViewById(R.id.spn_category);

        spFilter = (Spinner) view.findViewById(R.id.spn_filter);
        String[] filteArray = getResources().getStringArray(R.array.spinner_filter_entries);
        spFilter.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item, filteArray));

        grid = (GridView) view.findViewById(R.id.grid);
        edt_search = (EditText) view.findViewById(R.id.edt_search);
        btn_search = (ImageView) view.findViewById(R.id.btn_search);
//        txt_currentbal = (TextView) view.findViewById(R.id.txt_currentbal);
//        tvBalance = (TextView) view.findViewById(R.id.tvBalance);
//        txt_currentbal.setTypeface(null, Typeface.BOLD);
//        txt_currentbal.setTypeface(txt_currentbal.getTypeface(), Typeface.BOLD);
//        tvBalance.setTypeface(null, Typeface.BOLD);
//        tvBalance.setTypeface(tvBalance.getTypeface(), Typeface.BOLD);


    }

    public void setData() {
        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
        try {
            ctdUser.open();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        try {
            categories = ctdUser.getCategoryName();
            products = ctdUser.getAllProductDetails();
            products = setColorInProducts(products);
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
        //ctdUser.close();

        spCategory.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.list_item, categories));

        Log.e(TAG, "setData: Initial set data");
        setGrid(products);
    }

    public void setGrid(ArrayList<Product> products) {
        Log.e(TAG, "setGrid: " + products.size());
        RewardsProductGridAdapter gridAdapter = new RewardsProductGridAdapter(getActivity(), products, "product", "" + isDisable);
        gridAdapter.setOnClickListener(this);
        grid.setAdapter(gridAdapter);
    }

    public void alertDialog2(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
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

    @Override
    public void onDestroy() {
        //you may call the cancel() method but if it is not handled in doInBackground() method
        if (getAllCategoryAsyncTask != null && getAllCategoryAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
            getAllCategoryAsyncTask.cancel(true);
        super.onDestroy();
    }

    @Override
    public void onRecyclerViewItemClick(View v, int position) {
        if (v.getId() == R.id.btnViewDetails) {
            navigateToConfirmOrderDetailsFragment(v, position);
        }
    }

    class GetAllCategory extends AsyncTask<Void, Void, JSONObject> {

        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            Log.e("insert :", "in pre execute of Get All Category");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            Log.e("insert :", "in do in background of Get All Category");
            JSONObject jObj = null;
            try {

                SharedPreferences preferences4 = getActivity().getSharedPreferences(
                        "signupdetails", Context.MODE_PRIVATE);
                String login_id4 = preferences4.getString("usertrno", "");

                Log.e("", "Update category date" + cat_updateDate);
                if (cat_updateDate.equals("") || cat_updateDate == null)
                    jObj = new UserFunctions().getAllCategories(null, login_id4);
                else {
                    jObj = new UserFunctions().getAllCategories("" + cat_updateDate, login_id4);


                }
                // Log.e("", "Category json" + jObj);
            } catch (Exception e) {
                // TODO: handle exception
                progressDialog.dismiss();
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
            Log.e("insert :", "in post execute of Get All Category");
            // showToast("Calling set up views");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cat_updateDate = sdf.format(new Date());

            Log.e("", "Update date:-------------------" + cat_updateDate);
            SharedPreferences.Editor edit = PREF_Category_date.edit();
            edit.putString(MyTrackConstants._mStringCategoryUpdateDate, cat_updateDate);
            edit.commit();
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {

                       /* alertDialog2(
                                getResources().getString(R.string.app_name),
                                "" + jObj.getString("error"));*/

                       /* CustomOKDialog cdd=new CustomOKDialog(getActivity(),""+jObj.getString("error"),"Gulf Oil");
                        cdd.show();*/

                    } else if (mStringStatus.equals("success")) {

                        JSONArray resultJArray = jObj.getJSONArray("categories");
                        Log.d(TAG, "onPostExecute: Categories : " + resultJArray);

                        JSONObject result = resultJArray.getJSONObject(0);
                        Log.e("result : ", "result : " + result);

                        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
                        try {
                            ctdUser.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < resultJArray.length(); i++) {
                            ctdUser.insertIntoCategory(resultJArray.getJSONObject(i));
                        }

                        categories = ctdUser.getCategoryName();
                        ctdUser.close();
                        // spCategory.setAdapter(new RedeemCategoryAdapter(getActivity(), categories));

                    }


                    if (NetworkUtils.isNetworkAvailable(getActivity())) {
                        new GetProductList(progressDialog).execute();
                    } else {
                        //Toast.makeText(getActivity(), "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
            progressDialog.dismiss();
        }
    }

    //Update By Almas 19 Sep 17
    class GetProductList extends AsyncTask<Void, Void, JSONObject> {

        private ProgressDialog progressDialog;
        private Context mContext;

        public GetProductList(ProgressDialog pd) {
            this.progressDialog = pd;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            Log.e("insert :", "in do in background of GetProductList");
            JSONObject jObj = null;
            try {

                SharedPreferences preferences4 = getActivity().getSharedPreferences(
                        "signupdetails", Context.MODE_PRIVATE);
                String login_id = preferences4.getString("usertrno", "");

                Log.e("", "Update GetProductList date" + prod_updateDate);
                if (prod_updateDate.equals("") || prod_updateDate == null)
                    jObj = new UserFunctions().getProductList(null, login_id);
                else {
                    //MODIFIED BY PRAVIN DHARAM SEND UPDATED DATE BLANK EVERY TIME
                    //MODIFIED DATE 29M19
                    jObj = new UserFunctions().getProductList(null, login_id);
                }
            } catch (Exception e) {
                // TODO: handle exception
                progressDialog.dismiss();
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
            Log.e("insert :", "in post execute of GetProductList");
            // showToast("Calling set up views");
            try {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            } catch (Exception e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            prod_updateDate = sdf.format(new Date());
           /* SharedPreferences.Editor edit = PREF_Category_date.edit();

            edit.putString(MyTrackConstants._mStringProductUpdatedate, prod_updateDate);
            edit.commit();*/
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {

/*                        alertDialog2(
                                getResources().getString(R.string.app_name),
                                "" + jObj.getString("error"));*/

                      /*  CustomOKDialog cdd=new CustomOKDialog(getActivity(),""+jObj.getString("error"),"Gulf Oil");
                        cdd.show();
*/
                        progressDialog.dismiss();
                    } else if (mStringStatus.equals("success")) {

                        JSONArray resultJArray = jObj.getJSONArray("products");
                        Log.e("GetProductList data : ", "products : " + resultJArray);
                        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
                        try {
                            ctdUser.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        //CLEAR ALL PRODUCT FROM TABLE
                        ctdUser.deleteAllProduct();

                        for (int i = 0; i < resultJArray.length(); i++) {
                            ctdUser.insertIntoProduct(resultJArray.getJSONObject(i));
                        }

                        products = ctdUser.getProductDetails("asc", "0", "");

                        for (int i = 0; i < products.size(); i++)
                            Log.e("", "Product name:" + products.get(i).getName());
                        ctdUser.close();
                        SharedPreferences.Editor edit = PREF_Category_date.edit();

                        edit.putString(MyTrackConstants._mStringProductUpdatedate, prod_updateDate);
                        edit.commit();
                        //setGrid(products);
                        progressDialog.dismiss();
                    }
                    setData();
                    getAllCategoryAsyncTask.cancel(true);
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_rewards_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_rewards_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_rewards_club;
        else
            return R.layout.fragment_rewards_fls;
    }

    public void showOrderHistory() {
        hideKeyboard(getActivity());
        Log.e("", "Before ");
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.orderHistoryFragment);
    }

    //Update By Almas Sayyed
    private class getCurrentBal extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            Log.e("insert :", "in do in background of GetProductList");
            JSONObject jObj = null;
            try {

                jObj = new UserFunctions().getcurrentbal();

            } catch (Exception e) {
                // TODO: handle exception

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Network Error...", Toast.LENGTH_LONG).show();
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

                    } else if (mStringStatus.equals("success")) {
                        String str = jsonObject.getString("data");
                        currentBalance = str;
//                        txt_currentbal.setText(str);
                    }

                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
            /**/
        }

    }
}

