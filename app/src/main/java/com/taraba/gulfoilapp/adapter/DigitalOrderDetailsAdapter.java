package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.RewardOrderDetail;

import java.util.List;


/**
 * Created by Mansi on 3/16/16.
 */
public class DigitalOrderDetailsAdapter extends BaseAdapter {

    Context context;
    List<RewardOrderDetail> arrayList;
    private String rewardValue;
    private String rewardCode;


    public DigitalOrderDetailsAdapter(Context context, List<RewardOrderDetail> arrayList, String rewardValue, String rewardCode) {
        this.context = context;
        this.arrayList = arrayList;
        this.rewardValue = rewardValue;
        this.rewardCode = rewardCode;
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
            convertView = inflater.inflate(R.layout.digital_order_details_item, parent,
                    false);
        }

        TextView tv_reward_code,
                tv_reward_value,
                tv_voucher_code,
                tv_voucher_pin, tv_sr_no,
                tv_expiry_date, tv_view_details,
                lbl_evoucher_pin;

        tv_reward_code = (TextView) convertView.findViewById(R.id.tv_reward_code);
        tv_reward_value = (TextView) convertView.findViewById(R.id.tv_reward_value);
        tv_voucher_code = (TextView) convertView.findViewById(R.id.tv_voucher_code);
        tv_voucher_pin = (TextView) convertView.findViewById(R.id.tv_voucher_pin);
        lbl_evoucher_pin = (TextView) convertView.findViewById(R.id.lbl_evoucher_pin);
        tv_expiry_date = (TextView) convertView.findViewById(R.id.tv_expiry_date);
        tv_sr_no = (TextView) convertView.findViewById(R.id.tv_sr_no);
        tv_view_details = (TextView) convertView.findViewById(R.id.tv_view_details);

        tv_sr_no.setText("Reward Code " + (position + 1));
        tv_reward_code.setText(rewardCode);
        tv_reward_value.setText(rewardValue);
        tv_voucher_code.setText(arrayList.get(position).getEvCode());
        if (!TextUtils.isEmpty(arrayList.get(position).getEvPin())) {
            tv_voucher_pin.setText(arrayList.get(position).getEvPin());
            tv_voucher_pin.setVisibility(View.VISIBLE);
            lbl_evoucher_pin.setVisibility(View.VISIBLE);
        } else {
            tv_voucher_pin.setVisibility(View.GONE);
            lbl_evoucher_pin.setVisibility(View.GONE);
        }

        tv_expiry_date.setText(arrayList.get(position).getExpiryDate());
        if (arrayList.get(position).getGulfVoucher().equalsIgnoreCase("true")) {
            tv_view_details.setVisibility(View.VISIBLE);
        } else {
            tv_view_details.setVisibility(View.INVISIBLE);
        }
        tv_view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "DigitalOrderVoucherDetailsFragment", Toast.LENGTH_SHORT).show();
                /*Fragment detailsFragment = new DigitalOrderVoucherDetailsFragment();
                Bundle b = new Bundle();
                // b.putSerializable("Order_Details", arrayList.get(position));
                b.putString("voucher_id", arrayList.get(position).getVoucherId());
                b.putString("rewardCode", "Reward Code " + String.valueOf(position + 1));
                detailsFragment.setArguments(b);


                FragmentTransaction ftmech = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                //FragmentTransaction ftmech = ((OrderHistoryFragment) context).getSu()
                ftmech.replace(R.id.container_body, detailsFragment);
                ftmech.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ftmech.addToBackStack(null);
                ftmech.commit();*/
            }
        });
        return convertView;
    }
}