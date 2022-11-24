package com.taraba.gulfoilapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.contentproviders.UserModel;

import java.util.List;

/**
 * Created by android3 on 12/24/15.
 * Modified by Mansi
 */
public class WorkshopListAdpter extends ArrayAdapter<UserModel> {

    private final List<UserModel> values;
    private Context context = getContext();

    Boolean isMgmtVerified = false;

    public WorkshopListAdpter(Context context, List<UserModel> values) {
        super(context, R.layout.mechanical_list, values);
        this.values = values;
        this.context = context;
    }

    @Override
    public int getPosition(UserModel item) {
        return super.getPosition(item);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.workshop_row, parent, false);
        }

        TextView txt_workshopname = (TextView) convertView
                .findViewById(R.id.txt_workshopname);
        TextView txt_participant_code = (TextView) convertView
                .findViewById(R.id.txt_participant_code);
        TextView txt_mobile_no = (TextView) convertView
                .findViewById(R.id.txt_mobile_no);

        txt_participant_code.setText(values.get(position).getParticipant_code());
        txt_mobile_no.setText(values.get(position).getMobile_no());
        txt_workshopname.setText(values.get(position).getWorkshopname());
        return convertView;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}