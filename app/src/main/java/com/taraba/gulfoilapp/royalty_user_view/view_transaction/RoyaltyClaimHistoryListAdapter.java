package com.taraba.gulfoilapp.royalty_user_view.view_transaction;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.ClaimHistoryModel;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by android3 on 1/28/16.
 * Modified by Chaitali on 09/05/16
 */
public class RoyaltyClaimHistoryListAdapter extends ArrayAdapter<ClaimHistoryModel> {

    private final List<ClaimHistoryModel> values;
    private final Context context;
    private final int width;
    private final int height;

    public RoyaltyClaimHistoryListAdapter(Context context, List<ClaimHistoryModel> values) {
        super(context, R.layout.row_royalty_claim_history_list, values);
        this.values = values;
        this.context = context;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }

    @Override
    public int getPosition(ClaimHistoryModel item) {
        return super.getPosition(item);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_royalty_claim_history_list, parent,
                    false);
        }
        TextView txtID = (TextView) convertView
                .findViewById(R.id.lblUid);
        TextView txtlbltotalPoint = (TextView) convertView
                .findViewById(R.id.lbltotalPoint);

        TextView txtStatus = (TextView) convertView
                .findViewById(R.id.lblstatus);

        TextView txtPoints = (TextView) convertView
                .findViewById(R.id.lblPoint);

        try {
            int single_total = 0, base_point = 0, bonus_point = 0;


            txtID.setPaintFlags(txtID.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            txtID.setText("" + values.get(position).getClaim_uid());
            String t = "";
            //t = values.get(position).getClaim_uid_status();
            t = values.get(position).getClaim_uid_status();
            if (t == null || t.equals("")) {
                txtStatus.setText("-");
                base_point = 0;
                Log.e("base_point: ", "" + base_point);
            } else {
                txtStatus.setText("" + t);
                base_point = Integer.parseInt(t);
                Log.e("base_point: ", "" + base_point);
            }
            if (values.get(position).getClaim_point() == null
                    || values.get(position).getClaim_point().equals("")) {
                txtPoints.setText("-");
                bonus_point = 0;
                Log.e("bonus_point: ", "" + bonus_point);
            } else {
                txtPoints.setText("" + values.get(position).getClaim_point());
                bonus_point = Integer.parseInt(values.get(position).getClaim_point());
                Log.e("bonus_point: ", "" + bonus_point);
            }

            single_total = base_point + bonus_point;
            Log.e("single_total: ", "" + single_total);
            txtlbltotalPoint.setText("" + String.valueOf(single_total));
            Log.e("size adapter : ", "" + values.size());
            //main_total=main_total+single_total;
            //Log.e("main_total: ",""+main_total);
        } catch (Exception e) {
            e.printStackTrace();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Dialog dialog = new Dialog(context);
                // Include dialog.xml file
                dialog.setContentView(R.layout.royalty_point_details);
                dialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);
                // Set dialog title
                //dialog.setTitle("Participant Claim History");

                // set values for custom dialog components - text, image and button
                Button declineButton;
                TextView Tv_uid, Tv_validation_status, Tv_point, Tv_part_no, Tv_record_date, Tv_record_time, Tv_uid_status;

                Tv_uid = (TextView) dialog.findViewById(R.id.Tv_Uid);
                Tv_validation_status = (TextView) dialog.findViewById(R.id.TV_ValidationStatus);
                Tv_point = (TextView) dialog.findViewById(R.id.TV_point);
                Tv_part_no = (TextView) dialog.findViewById(R.id.TV_partno);
                Tv_record_date = (TextView) dialog.findViewById(R.id.TV_recorddate);
                Tv_record_time = (TextView) dialog.findViewById(R.id.TV_recordtime);
                Tv_uid_status = (TextView) dialog.findViewById(R.id.TV_uidstatus);

                Tv_uid.setText(" Month : " + values.get(position).getClaim_uid());
                //  String[] dt = (ptim.purchase_date).split("-");
                // Tv_validation_status.setText(" Validation Status : " + values.get(position).getClaim_validation_status());
                String t = "";
                t = values.get(position).getClaim_point();
                if (t == null || t.equals("")) {
                    // txtStatus.setText("-");
                    Tv_point.setText(" Bonus Points : -");
                } else {
                    Tv_point.setText(" Bonus Points : " + t);
                }

                //Tv_part_no.setText(" Part No. : " + values.get(position).getClaim_part_no());

               /* String finaldob = SplitDateFormat(values.get(position).getClaim_record_date()); // <---  yyyy-mm-dd
                Tv_record_date.setText(" Record Date : " + convertDateFormat(finaldob));
                Tv_record_time.setText(" Record Time : " + SplitTimeFormat(values.get(position).getClaim_record_date()));*/

                if (values.get(position).getClaim_uid_status() == null
                        || values.get(position).getClaim_uid_status().equals("")) {
                    Tv_uid_status.setText(" Base Points : -");
                } else {
                    Tv_uid_status.setText(" Base Points : " + values.get(position).getClaim_uid_status());
                }


                dialog.show();

                declineButton = (Button) dialog.findViewById(R.id.OkBT);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        dialog.dismiss();
                    }
                });


             /*  if(llDetails.getVisibility()==View.GONE) {
                   title.setText("Participant Claim History");
                   year.setText(" Month : " + values.get(position).getClaim_uid());
                   bonusPoint.setText(" Bonus Points : " + values.get(position).getClaim_point());
                   basePoint.setText(" Base Points : " +  values.get(position).getClaim_uid_status());

                   llDetails.setVisibility(View.VISIBLE);
                   imageExpand.setImageResource(R.drawable.minus);
               }else
               {
                   llDetails.setVisibility(View.GONE);
                   imageExpand.setImageResource(R.drawable.plus);
               }*/


            }
        });
        return convertView;
    }

    public String convertDateFormat(String datetoconvert) {
        String[] s = datetoconvert.split("-");
        return s[2] + "-" + s[1] + "-" + s[0];
    }

    public String SplitDateFormat(String datetosplit) {
        StringTokenizer tk = new StringTokenizer(datetosplit);
        String finaldob = tk.nextToken();  // <---  yyyy-mm-dd
        return finaldob;
    }

    public String SplitTimeFormat(String datetosplit) {
        StringTokenizer tk = new StringTokenizer(datetosplit);
        String finaldob = tk.nextToken();  // <---  yyyy-mm-dd
        String finaltime = tk.nextToken();  // <---  hh:mm:ss
        return finaltime;
    }
}

/*public class ClaimHistoryListAdapter extends BaseExpandableListAdapter {

    private Context context;
    List<ClaimHistoryModel> values;


    public ClaimHistoryListAdapter(Context context, List<ClaimHistoryModel> values) {


        this.context = context;
        this.values = values;

    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}*/