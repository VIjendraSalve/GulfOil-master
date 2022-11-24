package com.taraba.gulfoilapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.RegistrationDetail;

import java.util.ArrayList;

/**
 * Created by android on 7/5/16.
 */
public class RetailerListAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<RegistrationDetail> registrationDetails;

    public RetailerListAdapter(Context c, ArrayList<RegistrationDetail> registrationDetails) {
        mContext = c;
        this.registrationDetails = registrationDetails;

    }

    @Override
    public int getCount() {
        return registrationDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return registrationDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return registrationDetails.get(position).getParticipant_id_pk();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.cell_retailer, null);
        } else {
            grid = convertView;
        }


        TextView txt_retailer_name = (TextView) grid.findViewById(R.id.name);
        //TextView txt_retailer_workshopname = (TextView) grid.findViewById(R.id.workshop_name);
        TextView txt_retailer_mob = (TextView) grid.findViewById(R.id.mobile_number);

        txt_retailer_name.setText("Name: " + registrationDetails.get(position).getFull_name());
        //txt_retailer_workshopname.setText(registrationDetails.get(position).getWorkshop_name());
        txt_retailer_mob.setText("Mobile No.: " + registrationDetails.get(position).getMobile_no());
        return grid;
    }
}

