package com.taraba.gulfoilapp.royalty_user_view.order_history;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.SubmitOtpFragment;
import com.taraba.gulfoilapp.model.OrderHistory;
import com.taraba.gulfoilapp.royalty_user_view.order_details.NewOrderDetailsFragment;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mansi on 3/16/16.
 */
public class NewOrderHistoryAdapter extends BaseAdapter {

    private final Context context;
    String orderId, productName, gp, status, orderDate;
    ArrayList<OrderHistory> arrayList = new ArrayList<OrderHistory>();
    String order_request_no = "";
    int mech_trno;
    String loginId = "";
    String orderDetailsJson;
    FragmentManager fragmentManager;
    private boolean fromMechSearch;

    public NewOrderHistoryAdapter(Context context, ArrayList<OrderHistory> arrList, FragmentManager fm, boolean fromMechSearch) {
        this.arrayList = arrList;
        this.context = context;
        this.fragmentManager = fm;
        this.fromMechSearch = fromMechSearch;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_order_history_royal, parent,
                    false);
        }

        SharedPreferences preferences = ((Activity) context).getSharedPreferences(
                "userinfo", Context.MODE_PRIVATE);


        mech_trno = preferences.getInt("Mechanic_trno_to_redeem", 0);

        SharedPreferences preferences1 = ((Activity) context).getSharedPreferences(
                "signupdetails", ((Activity) context).MODE_PRIVATE);
        loginId = preferences1.getString("usertrno", "");


        Log.e("", "In orderHistoryAdapter *****************" + arrayList.get(position).getOrder_id() + ", " + arrayList.get(position).getName() + ", " + arrayList.get(position).getGp() + ", " + arrayList.get(position).getDate() + ", " + arrayList.get(position).getStatus());
        Log.e("", "In orderHistoryAdapter" + arrayList.size());

        TextView txt_id, txt_name, txt_gp, txt_status, txt_date;
        Button btn_resenedOtp, btn_submitOtp, btn_cancel, btn_orderDetails;


        txt_id = (TextView) convertView.findViewById(R.id.lblId);
        txt_name = (TextView) convertView.findViewById(R.id.lblProductName);
        txt_gp = (TextView) convertView.findViewById(R.id.lblGp);
        txt_status = (TextView) convertView.findViewById(R.id.lblOrderHistorystatus);
        txt_date = (TextView) convertView.findViewById(R.id.lblOrderHistoryOrderDate);

        order_request_no = arrayList.get(position).getOrder_request_no();

        Log.e("", "######################### ORDER REQUEST NO############################" + order_request_no);

        orderDetailsJson = arrayList.get(position).getOrder_details();

        txt_id.setText(Html.fromHtml("" + arrayList.get(position).getOrder_request_no()));
        txt_name.setText(Html.fromHtml("" + arrayList.get(position).getName()));
        txt_gp.setText(Html.fromHtml("" + arrayList.get(position).getGp()));
        txt_status.setText(Html.fromHtml(" " + arrayList.get(position).getStatus()));
        txt_date.setText(Html.fromHtml("" + arrayList.get(position).getDate()));

        btn_cancel = (Button) convertView.findViewById(R.id.btnCancelOrder);
        btn_orderDetails = (Button) convertView.findViewById(R.id.btnOrderDetails);
        btn_resenedOtp = (Button) convertView.findViewById(R.id.btn_Resendotp);
        btn_submitOtp = (Button) convertView.findViewById(R.id.btn_Submitotp);

        if (arrayList.get(position).getStatus().equals("Pending Verification")) {
            btn_resenedOtp.setVisibility(View.VISIBLE);
            btn_submitOtp.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.VISIBLE);
        } else {
            btn_resenedOtp.setVisibility(View.GONE);
            btn_submitOtp.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
        }

        if (fromMechSearch) {
            btn_cancel.setVisibility(View.GONE);
            btn_submitOtp.setVisibility(View.GONE);
            btn_resenedOtp.setVisibility(View.GONE);
        }

        final ConnectionDetector cd = new ConnectionDetector(
                context);


        btn_resenedOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (NetworkUtils.isNetworkAvailable(context)) {

                    order_request_no = arrayList.get(position).getOrder_request_no();
                    new ResendOtp().execute();

                } else {
                    Toast.makeText(context, "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                }

            }
        });

        btn_submitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context,"Order request no in submit otp is:"+order_request_no,Toast.LENGTH_LONG).show();
                String product_name = arrayList.get(position).getName();
                Fragment detailsFragment = new SubmitOtpFragment();
                Bundle b = new Bundle();
                b.putString("order_request_no", arrayList.get(position).getOrder_request_no());
                b.putString("user_login_id", loginId);
                b.putString("product_name", product_name);
                detailsFragment.setArguments(b);

                //FragmentTransaction ftmech = ((Activity)context).getFragmentManager().beginTransaction();
                FragmentTransaction ftmech = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                ftmech.replace(R.id.container_body, detailsFragment);
                ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftmech.addToBackStack(null);
                ftmech.commit();

            }
        });
        btn_orderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(context,"position in btn oder details click:"+position,Toast.LENGTH_LONG).show();
                // Toast.makeText(context,"Order request no in order details is:"+order_request_no,Toast.LENGTH_LONG).show();
                Fragment detailsFragment = new NewOrderDetailsFragment();
                Bundle b = new Bundle();
                b.putString("Order_Details", "" + arrayList.get(position).getOrder_details());
                String oh = arrayList.get(position).getOrder_request_no();
                b.putString("order_request_no", "" + oh);
                detailsFragment.setArguments(b);


                FragmentTransaction ftmech = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                //FragmentTransaction ftmech = ((OrderHistoryFragment) context).getSu()
                ftmech.replace(R.id.container_body, detailsFragment);
                ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftmech.addToBackStack(null);
                ftmech.commit();


            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkUtils.isNetworkAvailable(context)) {

                    order_request_no = arrayList.get(position).getOrder_request_no();
                    new CancelOrder().execute();

                } else {
                    Toast.makeText(context, "Internet Disconnected...!!", Toast.LENGTH_LONG).show();
                }

            }
        });
        return convertView;
    }

    public void alertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
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

    public void alertDialogCancelOrder(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        fragmentManager.popBackStack();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    class ResendOtp extends AsyncTask<Void, Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        @Override
        protected void onPreExecute() {
            Log.e("insert :", "in pre execute of progees order");
            progressDialog = new ProgressDialog(context);
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
                jObj = new UserFunctions().resendOtp("" + order_request_no);
                Log.e("", "resend otp json:" + jObj);
            } catch (Exception e) {
                // TODO: handle exception
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context,
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
                       /* alertDialog(
                                ((Activity) context).getString(R.string.app_name),
                                "" + jObj.getString("error"));*/
                        CustomOKDialog cdd = new CustomOKDialog(context, "" + jObj.getString("error"), "Gulf Oil");
                        cdd.show();

                        progressDialog.dismiss();
                    } else if (mStringStatus.equals("Success")) {
                       /* alertDialog(
                                ((Activity) context).getString(R.string.app_name),
                                "" + jObj.getString("error"));*/
                        CustomOKDialog cdd = new CustomOKDialog(context, "" + jObj.getString("error"), "Gulf Oil");
                        cdd.show();


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
            progressDialog = new ProgressDialog(context);
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
                jObj = new UserFunctions().cancelOrder("" + order_request_no, "" + loginId);
            } catch (Exception e) {
                // TODO: handle exception
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context,
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

                   /* alertDialogCancelOrder(
                            ((Activity) context).getString(R.string.app_name),
                            "" + jObj.getString("error"));*/

                        CustomOKDialog cdd = new CustomOKDialog(context, "" + jObj.getString("error"), "Gulf Oil");
                        cdd.show();


                        progressDialog.dismiss();
                    } else if (mStringStatus.equals("success")) {

                   /* alertDialogCancelOrder(
                            ((Activity) context).getString(R.string.app_name),
                            "" + jObj.getString("message"));*/

                        CustomOKDialog cdd = new CustomOKDialog(context, "Order Cancelled Successfully", "Gulf Oil");
                        cdd.show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class CustomOKDialog extends Dialog implements
            View.OnClickListener {

        public Context c;
        public Dialog d;
        public Button ok;
        String msg, heading;
        TextView txtMsg, txtHeading;
        String type;
        FragmentManager fm;

        public CustomOKDialog(Context a, String msg, String heading/*,String type,FragmentManager fm*/) {
            super(a);
            // TODO Auto-generated constructor stub
            this.c = a;
            this.msg = msg;
            this.heading = heading;
            this.type = type;
            this.fm = fm;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog_login_invalid);
            ok = (Button) findViewById(R.id.btn_Ok);
            txtMsg = (TextView) findViewById(R.id.txtMsg);
            txtHeading = (TextView) findViewById(R.id.txtHeading);
            txtMsg.setText("" + msg);
            txtHeading.setText("" + heading);
            ok.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_Ok:
                    dismiss();
                    fragmentManager.popBackStack();

                    break;
                default:
                    break;
            }
            dismiss();
        }
    }
}