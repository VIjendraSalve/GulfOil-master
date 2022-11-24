package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.OrderHistory;

import java.util.ArrayList;


/**
 * Created by Mansi on 3/16/16.
 */
public class OrderDetailsAdapter extends BaseAdapter {

    Context context;
    ArrayList<OrderHistory> arrayList;

    public OrderDetailsAdapter(Context context, ArrayList<OrderHistory> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_details_item, parent,
                    false);
        }

        TextView txt_detail_order_id, txt_detail_product_code, txt_detail_product_type, txt_detail_order_status, txt_detail_order_date, txt_detail_courier_name, txt_awb_no;

        txt_detail_order_id = (TextView) convertView.findViewById(R.id.txt_detail_order_id);
        txt_detail_product_code = (TextView) convertView.findViewById(R.id.txt_detail_product_code);
        txt_detail_product_type = (TextView) convertView.findViewById(R.id.txt_detail_product_type);
        txt_detail_order_status = (TextView) convertView.findViewById(R.id.txt_detail_order_status);
        txt_detail_order_date = (TextView) convertView.findViewById(R.id.txt_detail_order_date);
        txt_detail_courier_name = (TextView) convertView.findViewById(R.id.txt_detail_courier_name);
        txt_awb_no = (TextView) convertView.findViewById(R.id.txt_awb_no);


        txt_detail_order_id.setText(Html.fromHtml("<b>Order Id:</b> " + arrayList.get(position).getOrder_id()));
        txt_detail_product_code.setText(Html.fromHtml("<b>Product Code:</b> " + arrayList.get(position).getName()));
        txt_detail_product_type.setText(Html.fromHtml("<b>Product Type:</b> " + arrayList.get(position).getGp()));
        txt_detail_order_status.setText(Html.fromHtml("<b>Order Status:</b> " + arrayList.get(position).getStatus()));
        txt_detail_order_date.setText(Html.fromHtml("<b>Order Date:</b> " + arrayList.get(position).getDate()));
        txt_detail_courier_name.setText(Html.fromHtml("<b>Courier Name:</b> " + arrayList.get(position).getOrder_request_no()));
        txt_awb_no.setText("<b>AWB No:</b> " + arrayList.get(position).getOrder_details());
        return convertView;
    }
}