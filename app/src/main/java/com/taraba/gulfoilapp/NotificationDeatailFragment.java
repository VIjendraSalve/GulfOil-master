package com.taraba.gulfoilapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.taraba.gulfoilapp.model.Notification;

/**
 * Created by Mansi on 6/2/16.
 */
public class NotificationDeatailFragment extends Fragment {

    TextView txtNotificationTitle, txtNotificationBody;
    ImageView ivNotificationImage;
    String title = "", body = "", isImg = "", action = "", url;
    Bundle b;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.notification_details, container, false);
        txtNotificationTitle = (TextView) view.findViewById(R.id.txtNotificationTitle);
        txtNotificationBody = (TextView) view.findViewById(R.id.txtNotificationBody);
        ivNotificationImage = (ImageView) view.findViewById(R.id.ivNotificationImage);

        try {
            b = getArguments();
            action = b.getString("displayAction");
            Log.d("Notification_action", "" + action);
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
        SharedPreferences prefs = getActivity().getSharedPreferences(
                Constants.APP_NAMESPACE, Context.MODE_PRIVATE);

        if (action.equals("") || action == null)  // if notification comes
        {
            try {
                title = prefs.getString("title", "");
                body = prefs.getString("body", "");
                isImg = prefs.getString("isImg", "");

                txtNotificationTitle.setText("" + title);
                txtNotificationBody.setText("" + body);

                if (isImg.equals("true")) {
                    String url = prefs.getString("img_url", "");

                    if (url.equals("") || url == null) {
                        ivNotificationImage.setVisibility(View.GONE);
                    } else {
                        ivNotificationImage.setVisibility(View.VISIBLE);
                        Picasso.with(getActivity())
                                .load(url)
                                .resize(500, 500)
                                .placeholder(getResources().getDrawable(R.drawable.loading)).error(getResources().getDrawable(R.drawable.about1))
                                .into(ivNotificationImage);
                    }
                } else {
                    ivNotificationImage.setVisibility(View.GONE);
                }
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }
        } else  // if notification from mechanic dashboard
        {

            try {
                Notification notification = (Notification) b.getSerializable("notifiy");
                title = notification.getTitle();
                body = notification.getBody();
                url = notification.getImg_url();

                txtNotificationTitle.setText("" + title);
                txtNotificationBody.setText("" + body);
                Log.e("", "URL : " + isImg);
                if (url.equals("") || url == null) {
                    ivNotificationImage.setVisibility(View.GONE);
                } else {
                    ivNotificationImage.setVisibility(View.VISIBLE);
                    Log.e("Image URL : ", " Image URL  :" + isImg);
                    Picasso.with(getActivity())
                            .load(url)
                            .resize(500, 500)
                            .placeholder(getResources().getDrawable(R.drawable.loading)).error(getResources().getDrawable(R.drawable.about1))
                            .into(ivNotificationImage);
                }
            } catch (NullPointerException ne) {
                Log.e("dsfadsf", "drstrt : " + ne.toString());
            }
        }
        return view;
    }
}