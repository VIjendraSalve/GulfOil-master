package com.taraba.gulfoilapp.royalty_user_view.order_details;


import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.OrderHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by android3 on 3/16/16.
 * Modified by Mansi
 */
public class NewOrderDetailsFragment extends Fragment {

    TextView txt_order_id, txt_code, txt_type, txt_status, txt_order_date,
            txt_courier_name, txt_awb_no, text_recipient_name, delivery_date_and_time;
    ListView lst_order_details;
    String orderDetailsJson = "", order_request_no = "";
    JSONArray objectDetailsArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details_elite, container, false);

        txt_order_id = (TextView) view.findViewById(R.id.txt_detail_normal_order_id);
        txt_code = (TextView) view.findViewById(R.id.txt_detail_normal_product_code);
        txt_type = (TextView) view.findViewById(R.id.txt_detail_normal_product_type);
        txt_status = (TextView) view.findViewById(R.id.txt_detail_normal_order_status);
        txt_order_date = (TextView) view.findViewById(R.id.txt_detail_normal_order_date);
        txt_courier_name = (TextView) view.findViewById(R.id.txt_detail_normal_courier_name);
        txt_awb_no = (TextView) view.findViewById(R.id.txt_detail_normal_awb_no);
        lst_order_details = (ListView) view.findViewById(R.id.lst_order_details);
        delivery_date_and_time = (TextView) view.findViewById(R.id.txt_delivery_date_and_time);
        text_recipient_name = (TextView) view.findViewById(R.id.text_recipient_name);


        Bundle b = getArguments();
        orderDetailsJson = b.getString("Order_Details", "");
        order_request_no = b.getString("order_request_no", "");

        // Toast.makeText(getActivity(),"Order request no in order details fragment is:"+order_request_no,Toast.LENGTH_LONG).show();

        String product_name = b.getString("product_name", "");
        Log.e("OrderDetailsFragment", "******************Order details frament json is:*********************" + orderDetailsJson);

        ArrayList<OrderHistory> arrayListOrderDetails = new ArrayList<OrderHistory>();

        try {
            objectDetailsArray = new JSONArray(orderDetailsJson);


            for (int i = 0; i < objectDetailsArray.length(); i++) {
                JSONObject jsonObject = objectDetailsArray.getJSONObject(i);
                String order_id = jsonObject.optString("order_detail_id_pk");
                String product_code = jsonObject.optString("product_code");
                String product_type = jsonObject.optString("product_type");
                String order_status = jsonObject.optString("order_status");
                String record_date = jsonObject.optString("record_date");
                String courier_name = jsonObject.optString("courier_name");
                String awb_no = jsonObject.optString("awb_no");

                String delivery_date = jsonObject.optString("delivery_date");
                String recipient_nameStr = jsonObject.optString("receiver_name");

                if (product_type.equals("normal")) {
                    txt_order_id.setText(Html.fromHtml("" + order_request_no));
                    txt_code.setText(Html.fromHtml(" " + product_code));
                    txt_type.setText(Html.fromHtml("" + product_type));
                    txt_status.setText(Html.fromHtml(" " + order_status));
                    txt_order_date.setText(Html.fromHtml("" + record_date));
                    txt_courier_name.setText(Html.fromHtml("" + courier_name));
                    txt_awb_no.setText(Html.fromHtml(" " + awb_no));

                    if (delivery_date != null && !delivery_date.equalsIgnoreCase("null"))
                        delivery_date_and_time.setText(Html.fromHtml(" " + delivery_date));
                    if (recipient_nameStr != null && !recipient_nameStr.equalsIgnoreCase("null"))
                        text_recipient_name.setText(Html.fromHtml(" " + recipient_nameStr));
                } else {
                    //arrayListOrderDetails.add(new OrderHistory("" + order_request_no, "" + product_name, "" + product_type, "" + order_status, "" + record_date, "" + courier_name, ""));
                    arrayListOrderDetails.add(new OrderHistory("" + order_request_no, "" + product_name, "" + product_type, "" + order_status, "" + record_date, "" + courier_name, "" + awb_no));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lst_order_details.setAdapter(new RoyaltyOrderDetailsAdapter(getActivity(), arrayListOrderDetails));
        return view;
    }
}