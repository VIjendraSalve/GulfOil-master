package com.taraba.gulfoilapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taraba.gulfoilapp.R;

import java.util.ArrayList;

/**
 * Created by android3 on 1/4/16.
 */
public class SpinnerDataAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ArrayList<String>> data;
    //int mIntLayoutresource;
    String mStringAct;

    public SpinnerDataAdapter(Context context,
                              ArrayList<ArrayList<String>> items, String mStringact) {
        super();
        this.mContext = context;
        this.data = items;
        this.mStringAct = mStringact;
        // TODO Auto-generated constructor stub
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        grid = new View(mContext);
        ArrayList<String> mgrid_Item = data.get(position);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        grid = inflater.inflate(R.layout.email_layout, null);
        TextView TvCompanyName = (TextView) grid.findViewById(R.id.text_name);
        //	TextView TvID = (TextView) grid.findViewById(R.id.txt_trno);
        if (mStringAct.equals("Status")) {
            TvCompanyName.setText("" + mgrid_Item.get(1).substring(0, 1).toUpperCase() + mgrid_Item.get(1).substring(1));
        } else if (mStringAct.equals("spinner")) {
            TvCompanyName.setText("" + mgrid_Item.get(0).trim());
        } else {
            TvCompanyName.setText("" + mgrid_Item.get(1).trim());
        }

        //	TvID.setText("" + mgrid_Item.get(0).trim());

        return grid;
    }

}