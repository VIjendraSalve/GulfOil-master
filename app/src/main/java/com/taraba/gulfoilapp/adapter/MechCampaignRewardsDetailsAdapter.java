package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.RewardOrderDetail;

import java.util.List;

public class MechCampaignRewardsDetailsAdapter extends BaseAdapter {
    List<RewardOrderDetail> arrayList;
    Context context;
    private String rewardCode;
    private String rewardValue;

    public long getItemId(int i) {
        return 0;
    }

    public MechCampaignRewardsDetailsAdapter(Context context2, List<RewardOrderDetail> list, String str, String str2) {
        this.context = context2;
        this.arrayList = list;
        this.rewardValue = str;
        this.rewardCode = str2;
    }

    public int getCount() {
        return this.arrayList.size();
    }

    public Object getItem(int i) {
        return this.arrayList.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.digital_order_details_item, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_voucher_pin);
        TextView textView2 = (TextView) view.findViewById(R.id.lbl_evoucher_pin);
        TextView textView3 = (TextView) view.findViewById(R.id.tv_expiry_date);
        ((TextView) view.findViewById(R.id.tv_sr_no)).setText("Reward Code " + (i + 1));
        ((TextView) view.findViewById(R.id.tv_reward_code)).setText(this.rewardCode);
        ((TextView) view.findViewById(R.id.tv_reward_value)).setText(this.rewardValue);
        ((TextView) view.findViewById(R.id.tv_voucher_code)).setText(this.arrayList.get(i).getEvCode());
        if (!TextUtils.isEmpty(this.arrayList.get(i).getEvPin())) {
            textView.setText(this.arrayList.get(i).getEvPin());
            textView.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
            textView2.setVisibility(View.GONE);
        }
        textView3.setText(this.arrayList.get(i).getExpiryDate());
        return view;
    }
}
