package com.taraba.gulfoilapp.royalty_user_view.voucher_details.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.royalty_user_view.voucher_details.model.InvoiceDetails;

import java.util.List;

public class RoyaltyVoucherListAdapter extends BaseAdapter {
    List<InvoiceDetails> arrayList;
    private final Context context;

    public long getItemId(int i) {
        return 0;
    }

    public RoyaltyVoucherListAdapter(Context context2, List<InvoiceDetails> list) {
        this.arrayList = list;
        this.context = context2;
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
            view = layoutInflater.inflate(R.layout.royalty_voucher_list, viewGroup, false);
        }
        ((TextView) view.findViewById(R.id.tv_sr_no)).setText("Invoice " + (i + 1));
        ((TextView) view.findViewById(R.id.tv_invoice_no)).setText(this.arrayList.get(i).getInvoice_no());
        ((TextView) view.findViewById(R.id.tv_invoice_date)).setText(this.arrayList.get(i).getInvoice_date());
        ((TextView) view.findViewById(R.id.tv_redeem_amount)).setText(this.arrayList.get(i).getVoucher_redeemed_amount());
        return view;
    }
}
