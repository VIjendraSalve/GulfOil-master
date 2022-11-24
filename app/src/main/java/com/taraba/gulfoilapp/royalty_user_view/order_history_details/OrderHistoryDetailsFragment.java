package com.taraba.gulfoilapp.royalty_user_view.order_history_details;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.constant.UserType;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.model.OrderHistory;
import com.taraba.gulfoilapp.royalty_user_view.order_history_details.OrderHistoryDetailsFragmentDirections.ActionOrderHistoryDetailsFragmentToProceedOrderOTPFragment;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.taraba.gulfoilapp.royalty_user_view.order_history_details.OrderHistoryDetailsFragmentDirections.actionOrderHistoryDetailsFragmentToProceedOrderOTPFragment;

public class OrderHistoryDetailsFragment extends Fragment implements View.OnClickListener {

    private ImageView ivProduct;
    private TextView tvRewardName;
    private TextView tvOrderId;
    private TextView tvProductCode;
    private TextView tvProductType;
    private TextView tvOrderDate;
    private TextView tvQuantity;
    private TextView tvStatus;
    private Button btnResendOTP;
    private Button btnSubmitOTP;
    private Button btnCancelOrder;
    private OrderHistory orderHistory;


    public OrderHistoryDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderHistory = OrderHistoryDetailsFragmentArgs.fromBundle(getArguments()).getOrderHistory();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutUserWise(), container, false);
        init(view);
        setListeners();
        setData();
        return view;
    }


    private void init(View view) {
        ivProduct = view.findViewById(R.id.ivProduct);
        tvRewardName = view.findViewById(R.id.tvRewardName);
        tvOrderId = view.findViewById(R.id.tvOrderId);
        tvProductCode = view.findViewById(R.id.tvProductCode);
        tvProductType = view.findViewById(R.id.tvProductType);
        tvOrderDate = view.findViewById(R.id.tvOrderDate);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        tvStatus = view.findViewById(R.id.tvStatus);
        btnResendOTP = view.findViewById(R.id.btnResendOTP);
        btnSubmitOTP = view.findViewById(R.id.btnSubmitOTP);
        btnCancelOrder = view.findViewById(R.id.btnCancelOrder);
    }

    private void setListeners() {
        btnResendOTP.setOnClickListener(this);
        btnSubmitOTP.setOnClickListener(this);
        btnCancelOrder.setOnClickListener(this);
    }

    private void setData() {
        if (orderHistory != null) {
            Glide.with(getActivity())
                    .load(orderHistory.getProduct_image())
                    .placeholder(R.drawable.no_image_available)
                    .into(ivProduct);
            tvRewardName.setText(orderHistory.getName());
            tvOrderId.setText(orderHistory.getOrder_id());
            tvProductCode.setText("_");
            tvProductType.setText("_");
            tvOrderDate.setText(orderHistory.getDate());
            tvQuantity.setText(orderHistory.getQty());
            tvStatus.setText(orderHistory.getStatus());
            try {
                JSONArray jsonArray = new JSONArray(orderHistory.getOrder_details());
                tvProductCode.setText(jsonArray.getJSONObject(0).getString("product_code"));
                tvProductType.setText(jsonArray.getJSONObject(0).getString("product_type"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnResendOTP:
                if (ConnectionDetector.isNetworkAvailable(getActivity())) {
                    new ResendOtp().execute();
                } else {
                    new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                            .setTitle(getString(R.string.str_error))
                            .setDescription(getString(R.string.internet_connection_error))
                            .setPosButtonText(getString(R.string.str_ok), null)
                            .show();
                }
                break;
            case R.id.btnSubmitOTP:
                goToProceedOrderOTPScreen(v);
                break;
            case R.id.btnCancelOrder:
                if (ConnectionDetector.isNetworkAvailable(getActivity())) {
                    new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                            .setTitle(getString(R.string.str_confirmation))
                            .setDescription(getString(R.string.str_cance_order_confirmation_msg))
                            .setPosButtonText(getString(R.string.str_yes), dialog -> {
                                dialog.dismiss();
                                new CancelOrder().execute();
                            }).setNegButtonText(getString(R.string.str_no), null)
                            .show();
                } else {
                    new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                            .setTitle(getString(R.string.str_error))
                            .setDescription(getString(R.string.internet_connection_error))
                            .setPosButtonText(getString(R.string.str_ok), null)
                            .show();
                }
                break;
        }
    }

    private void goToProceedOrderOTPScreen(View v) {
        ActionOrderHistoryDetailsFragmentToProceedOrderOTPFragment action = actionOrderHistoryDetailsFragmentToProceedOrderOTPFragment();
        action.setFromOrderHistoryScreen(true);
        action.setProductCode(tvProductCode.getText().toString());
        action.setMultiQty(orderHistory.getQty());
        action.setOrderId(orderHistory.getOrder_id());
        Navigation.findNavController(v).navigate(action);
    }

    private int getLayoutUserWise() {
        UserType userType = new GulfOilUtils().getUserType();
        if (userType == UserType.ROYAL)
            return R.layout.fragment_order_history_details_royal;
        else if (userType == UserType.ELITE)
            return R.layout.fragment_order_history_details_elite;
        else if (userType == UserType.CLUB)
            return R.layout.fragment_order_history_details_club;
        else
            return R.layout.fragment_order_history_details_royal;
    }

    class ResendOtp extends AsyncTask<Void, Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            Log.e("insert :", "in pre execute of progees order");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            Log.e("insert :", "in do in background of resend otp");
            JSONObject jObj = null;
            try {
                //jObj = new UserFunctions().resendOtp(""+order_request_no);
                jObj = new UserFunctions().resendOtp("" + orderHistory.getOrder_id());
                Log.e("", "resend otp json:" + jObj);
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
            Log.e("insert :", "in post execute of get order details");
            // showToast("Calling set up views");
            progressDialog.dismiss();
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {
                        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle(getString(R.string.str_error))
                                .setDescription("" + jObj.getString("error"))
                                .setPosButtonText(getString(R.string.str_ok), null)
                                .show();

                    } else if (mStringStatus.equals("Success")) {
                        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle(getString(R.string.str_success))
                                .setDescription("" + jObj.getString("error"))
                                .setPosButtonText(getString(R.string.str_ok), new GulfUnnatiDialog.OnPositiveClickListener() {
                                    @Override
                                    public void onClick(GulfUnnatiDialog dialog) {
                                        dialog.dismiss();
                                        goToProceedOrderOTPScreen(getView());
                                    }
                                })
                                .show();


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class CancelOrder extends AsyncTask<Void, Void, JSONObject> {

        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            Log.e("insert :", "in pre execute of progees order");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please  wait!!!");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }


        @Override
        protected JSONObject doInBackground(Void... params) {
            Log.e("insert :", "in do in background of proceed order");
            JSONObject jObj = null;
            try {
                SharedPreferences preferences1 = getActivity().getSharedPreferences(
                        "signupdetails", getActivity().MODE_PRIVATE);
                String loginId = preferences1.getString("usertrno", "");
                jObj = new UserFunctions().cancelOrder("" + orderHistory.getOrder_id(), "" + loginId);
            } catch (Exception e) {
                // TODO: handle exception
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(),
                        "Network Error...", Toast.LENGTH_LONG).show());
            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            Log.e("insert :", "in post execute of get order details");
            // showToast("Calling set up views");
            progressDialog.dismiss();
            if (jObj != null) {
                try {
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("failure")) {
                        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle(getString(R.string.str_error))
                                .setDescription("" + jObj.getString("error"))
                                .setPosButtonText(getString(R.string.str_ok), null)
                                .show();
                    } else if (mStringStatus.equals("success")) {

                        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType())
                                .setTitle(getString(R.string.str_success))
                                .hideDialogCloseButton(true)
                                .setDescription("" + jObj.getString("message"))
                                .setPosButtonText(getString(R.string.str_ok), dialog -> {
                                    dialog.dismiss();
                                    Navigation.findNavController(getView()).popBackStack();
                                })
                                .show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}