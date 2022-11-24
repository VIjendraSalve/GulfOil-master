package com.taraba.gulfoilapp;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.model.Notification;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Mansi on 6/2/16.
 */
public class DisplayNotification extends Fragment {

    ListView lst_notifications;
    NotificationAdapter notificationAdapter;
    ArrayList<Notification> notifications;
    TextView txt_not;
    String mStringMobileNo;
    int chk = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.display_notification, container, false);
        txt_not = (TextView) view.findViewById(R.id.txt_no_notifications);
        lst_notifications = (ListView) view.findViewById(R.id.lstNotifications);

        chk = Check_total_notification_count();
        Log.d("chk_count", "" + chk);
        if (chk == 0) {
            txt_not.setVisibility(View.VISIBLE);
            lst_notifications.setVisibility(View.GONE);


        } else {
            txt_not.setVisibility(View.GONE);
            lst_notifications.setVisibility(View.VISIBLE);
            load();

        }

        load();

        return view;
    }

    public class NotificationAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<Notification> notifications;

        public NotificationAdapter(Context context, ArrayList<Notification> notifications) {
            mContext = context;
            this.notifications = notifications;
        }

        @Override
        public int getCount() {
            return notifications.size();
        }

        @Override
        public Object getItem(int position) {
            return notifications.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)
                    getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.country_layout, null);
                holder = new ViewHolder();
                holder.txt_notifications = (TextView) convertView.findViewById(R.id.txt_notifications);
                holder.lin_not = (LinearLayout) convertView.findViewById(R.id.lin_not);
                holder.txt_no_notifications = (TextView) convertView.findViewById(R.id.txt_no_notifications);
                holder.txt_date = (TextView) convertView.findViewById(R.id.txt_dateTime);
                holder.txt_body = (TextView) convertView.findViewById(R.id.txt_body);
                holder.btnMore = (Button) convertView.findViewById(R.id.btnMore);
                holder.unread_textView = (TextView) convertView.findViewById(R.id.unread_textView);
//holder.btnMore.setTransformationMethod(null);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Notification rowItem = (Notification) notifications.get(position);
            holder.txt_notifications.setText(rowItem.getTitle());
            holder.txt_body.setText(rowItem.getBody());
            holder.txt_date.setText(rowItem.getMdate());
            Log.e("", "STATUS!!" + rowItem.getStatus());

            if (rowItem.getStatus().equals("1")) {
                Log.e("", "IN IF!!" + "IN IF");
                // read status
                // holder.txt_body.setTypeface(null, Typeface.NORMAL);
                // holder.txt_date.setTypeface(null, Typeface.NORMAL);
                holder.unread_textView.setVisibility(View.GONE);
                // holder.txt_notifications.setBackgroundColor(Color.parseColor("#9e9e9e"));

            } else {
                Log.e("", "IN ELSE!!" + "IN ELSE");

                //unread status
                //  holder.txt_body.setTypeface(null, Typeface.BOLD);
                // holder.txt_date.setTypeface(null, Typeface.BOLD);
                holder.unread_textView.setVisibility(View.VISIBLE);
                // holder.txt_notifications.setBackgroundColor(Color.parseColor("#bdc3c7"));
            }

            return convertView;
        }

        class ViewHolder {
            TextView txt_notifications, txt_no_notifications, txt_date, txt_body, unread_textView;
            Button btnMore;
            LinearLayout lin_not;
        }
    }

    public void load() {


        notifications = new ArrayList<Notification>();

        final UserTableDatasource ctdUser = new UserTableDatasource(getActivity());
        try {
            ctdUser.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SharedPreferences preferences1 = getActivity().getSharedPreferences(
                "signupdetails", Context.MODE_PRIVATE);
        String mobno = preferences1.getString("uname", "");

        Log.e("uname", "" + mobno);
        //------------------- get data from notification-----------------------
        notifications = ctdUser.getNotification("" + mobno);

        for (int i = 0; i < notifications.size(); i++)
            Log.e("", "Notification_data:" + notifications.get(i));
        ctdUser.close();

       /* ArrayAdapter<Notification> adapter = new ArrayAdapter<Notification>(getActivity(),
                R.layout.notification_list_item,R.id.txtListNotificationTitle, notifications);*/

        notificationAdapter = new NotificationAdapter(getActivity(), notifications);
        lst_notifications.setAdapter(notificationAdapter);

        lst_notifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notification not = notifications.get(position);

                // update notification table
                try {
                    ctdUser.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                int trno = notifications.get(position).getTrno();
                int res = ctdUser.updateNotification(String.valueOf(trno));

                Log.e("", "Update notification result:" + res);
                ctdUser.close();

                Intent i = new Intent(getActivity(), NotificationDetailActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("notifiy", not);
                b.putString("displayAction", "MechanicNotification");
                i.putExtras(b);
                startActivity(i);


            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    public int Check_total_notification_count() {
        int count = 0;
        try {

            final UserTableDatasource ctdUser1 = new UserTableDatasource(getActivity());
            try {

                //if(ctdUser.)

                ctdUser1.open();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            SharedPreferences preferences1 = getActivity().getSharedPreferences(
                    "signupdetails", Context.MODE_PRIVATE);

            mStringMobileNo = preferences1.getString("uname", "");


            count = ctdUser1.getStatusCount("" + mStringMobileNo);

           /* SharedPreferences preferences1 = getActivity().getSharedPreferences(
                    "signupdetails", Context.MODE_PRIVATE);

            mStringMobileNo=preferences1.getString("uname", "");


            count=ctdUser.getStatusCount(""+mStringMobileNo);
*/

            Log.e("notification chkcount", String.valueOf(count));
            ctdUser1.close();

//Toast.makeText(getApplicationContext(),MyConstants.eventID,Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }//Check_notification_count
}