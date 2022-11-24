package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.taraba.gulfoilapp.DigitalOrderDetailsFragment;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.DigitalOrderHistory;

import java.util.List;

/**
 * Created by Mansi on 3/16/16.
 */
public class DigitalOrderHistoryAdapter extends BaseAdapter {

    private final Context context;
    List<DigitalOrderHistory> arrayList;
    FragmentManager fragmentManager;
    private boolean fromMechSearch;

    public DigitalOrderHistoryAdapter(Context context, List<DigitalOrderHistory> arrList, FragmentManager fm, boolean fromMechSearch) {
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
            convertView = inflater.inflate(R.layout.digital_order_history_list, parent,
                    false);
        }

        TextView tv_order_id, tv_reward_name, tv_order_date, tv_order_quantity, tv_order_status;


        tv_order_date = (TextView) convertView.findViewById(R.id.tv_order_date);
        tv_order_id = (TextView) convertView.findViewById(R.id.tv_order_id);
        tv_reward_name = (TextView) convertView.findViewById(R.id.tv_reward_name);
        tv_order_quantity = (TextView) convertView.findViewById(R.id.tv_order_quantity);
        tv_order_status = (TextView) convertView.findViewById(R.id.tv_order_status);

        if (!TextUtils.isEmpty(arrayList.get(position).getOrdersRecordDate()))
            tv_order_date.setText(arrayList.get(position).getOrdersRecordDate());
        if (!TextUtils.isEmpty(arrayList.get(position).getOrderId()))
            tv_order_id.setText(arrayList.get(position).getOrderId());
        if (!TextUtils.isEmpty(arrayList.get(position).getRewardName()))
            tv_reward_name.setText(arrayList.get(position).getRewardName());
        if (!TextUtils.isEmpty(arrayList.get(position).getQuantity()))
            tv_order_quantity.setText(arrayList.get(position).getQuantity());
        if (!TextUtils.isEmpty(arrayList.get(position).getStatus()))
            tv_order_status.setText(arrayList.get(position).getStatus());

        convertView.findViewById(R.id.btn_view_codes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment detailsFragment = new DigitalOrderDetailsFragment();
                Bundle b = new Bundle();
                //b.putSerializable("Order_Details", arrayList.get(position));
                b.putString("order_details_id", arrayList.get(position).getOrderDetailId());
                detailsFragment.setArguments(b);


                FragmentTransaction ftmech = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                //FragmentTransaction ftmech = ((OrderHistoryFragment) context).getSu()
                ftmech.replace(R.id.container_body, detailsFragment);
                ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftmech.addToBackStack(null);
                ftmech.commit();
            }
        });

        return convertView;
    }
}