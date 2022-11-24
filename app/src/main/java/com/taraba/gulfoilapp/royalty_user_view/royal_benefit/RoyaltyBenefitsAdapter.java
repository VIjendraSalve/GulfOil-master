package com.taraba.gulfoilapp.royalty_user_view.royal_benefit;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.CampaignRewards;
import com.taraba.gulfoilapp.royalty_user_view.royal_order_details.RoyalOrderRewardsDetailsFragment;

import java.util.List;

public class RoyaltyBenefitsAdapter extends BaseAdapter {
    List<CampaignRewards> arrayList;
    /* access modifiers changed from: private */
    public final Context context;
    FragmentManager fragmentManager;
    private boolean fromMechSearch;

    public long getItemId(int i) {
        return 0;
    }

    public RoyaltyBenefitsAdapter(Context context2, List<CampaignRewards> list, FragmentManager fragmentManager2, boolean z) {
        this.arrayList = list;
        this.context = context2;
        this.fragmentManager = fragmentManager2;
        this.fromMechSearch = z;
    }

    public int getCount() {
        return this.arrayList.size();
    }

    public Object getItem(int i) {
        return this.arrayList.get(i);
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.royale_benefits_rewards_history_list, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_order_date);
        TextView textView2 = (TextView) view.findViewById(R.id.tv_order_id);
        TextView textView3 = (TextView) view.findViewById(R.id.tv_reward_name);
        TextView textView4 = (TextView) view.findViewById(R.id.tv_order_quantity);
        if (!TextUtils.isEmpty(this.arrayList.get(i).getOrders_record_date())) {
            textView.setText(this.arrayList.get(i).getOrders_record_date());
        }
        if (!TextUtils.isEmpty(this.arrayList.get(i).getOrder_id())) {
            textView2.setText(this.arrayList.get(i).getOrder_id());
        }
        if (!TextUtils.isEmpty(this.arrayList.get(i).getReward_name())) {
            textView3.setText(this.arrayList.get(i).getReward_name());
        }
        if (!TextUtils.isEmpty(this.arrayList.get(i).getQuantity())) {
            textView4.setText(this.arrayList.get(i).getQuantity());
        }
        view.findViewById(R.id.btn_view_codes).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RoyalOrderRewardsDetailsFragment royalOrderRewardsDetailsFragment = new RoyalOrderRewardsDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("order_detail_id", RoyaltyBenefitsAdapter.this.arrayList.get(i).getOrder_detail_id());
                royalOrderRewardsDetailsFragment.setArguments(bundle);
                FragmentTransaction beginTransaction = ((FragmentActivity) RoyaltyBenefitsAdapter.this.context).getSupportFragmentManager().beginTransaction();
                beginTransaction.replace(R.id.container_body, royalOrderRewardsDetailsFragment);
                beginTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                beginTransaction.addToBackStack((String) null);
                beginTransaction.commit();
            }
        });
        return view;
    }
}
