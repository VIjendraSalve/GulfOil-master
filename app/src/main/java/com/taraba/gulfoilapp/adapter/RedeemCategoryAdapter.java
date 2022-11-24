package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taraba.gulfoilapp.R;

import java.util.ArrayList;

/**
 * Created by android on 3/14/16.
 */
public class RedeemCategoryAdapter extends BaseAdapter {

    ArrayList<String> categories;
    Context context;
    LayoutInflater li;

    public RedeemCategoryAdapter(Context c, ArrayList<String> categories) {
        super();
        this.context = c;
        this.categories = categories;
        try {
            li = LayoutInflater.from(c);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = li.inflate(R.layout.spinner_category, null);
            holder = new ViewHolder();
            holder.txtpname = (TextView) convertView.findViewById(R.id.txt_sp_party_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtpname.setText(categories.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView txtpname;
    }
}
