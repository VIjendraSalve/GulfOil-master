package com.taraba.gulfoilapp.adapter;

/**
 * Created by android5 on 2/22/16.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.DashboardMenu;

import java.util.List;

public class DashboardAdapter extends BaseAdapter {

    private final Context context;
    private List<DashboardMenu> dashboardMenuList;
    private static LayoutInflater inflater = null;
    int check = 0;
    String type = null;


    public DashboardAdapter(Context context, List<DashboardMenu> dashboardMenuList, int check, String type) {
        // TODO Auto-generated constructor stub
        //     result=prgmNameList;
        this.context = context;
        this.dashboardMenuList = dashboardMenuList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.check = check;
        this.type = type;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView = convertView;

        rowView = inflater.inflate(R.layout.activity_dashboard_image, null);
        rowView.setBackgroundColor(dashboardMenuList.get(position).getColor());
        // holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
        holder.tv = (TextView) rowView.findViewById(R.id.badge_textView);
        //   holder.tv.setText(result[position]);
        Log.e("POSITION", "POSITION:" + dashboardMenuList.get(position).getImage());
        holder.img.setImageResource(dashboardMenuList.get(position).getImage());
        if (dashboardMenuList.get(position).getLebal().equals(context.getString(R.string.ipl_offer))) {
            holder.tv.setText("");
        } else {
            holder.tv.setText(dashboardMenuList.get(position).getLebal());
        }


        return rowView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dashboardMenuList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }
}

