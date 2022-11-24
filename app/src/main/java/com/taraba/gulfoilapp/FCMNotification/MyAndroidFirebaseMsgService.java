package com.taraba.gulfoilapp.FCMNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.taraba.gulfoilapp.Constants;
import com.taraba.gulfoilapp.DashboardFragment;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.SplashActivity;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;

/**
 * Created by pc on 10/14/2017.
 */

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    String body = "", message = "", type = "", url = "", channel_type = "";
    int resourceID;
    Intent intentw = null;
    NotificationCompat.Builder mBuilder;
    Intent notificationIntent;
    Uri notifySound;
    private String image = "";
    private String channelId = "DEFAULT_NOTIFICATION";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().toString());
        saveNotification(remoteMessage);
        createNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"));
        //create notification
        //*****************************************
    }

    private void saveNotification(RemoteMessage remoteMessage) {
        try {
            SharedPreferences prefs = getSharedPreferences(
                    Constants.APP_NAMESPACE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            if (remoteMessage.getData().containsKey("type"))
                type = remoteMessage.getData().get("type");
            String title = remoteMessage.getData().get("title");
            String description = remoteMessage.getData().get("body");
            image = remoteMessage.getData().get("image");
            if (remoteMessage.getData().containsKey("channel_type"))
                channel_type = remoteMessage.getData().get("channel_type");
            Log.d(TAG, "Notification Message Body: type = " + type);


            final UserTableDatasource ctdUser = new UserTableDatasource(this);

            try {
                ctdUser.open();
                SharedPreferences preferences1 = getSharedPreferences(
                        "signupdetails", Context.MODE_PRIVATE);
                String uname = preferences1.getString("uname", "");

                ctdUser.insertIntoNotification(type, uname, title,
                        description,
                        image,
                        channel_type);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNotification(String messageBody, String title) {

        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        if (!TextUtils.isEmpty(image)) {
            long[] v = {500, 1000};
            Bitmap remote_picture = null;
            try {

                remote_picture = BitmapFactory.decodeStream((InputStream) new URL("" + image).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            notiStyle.bigPicture(remote_picture);
        }
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(notificationSoundURI)
                .setStyle(notiStyle)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, mNotificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notification_logo_marshmallow : R.drawable.powerlogo;
    }

    private void sendNotification(String msg, String imgUrl) {

        Bitmap remote_picture = null;

        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setSummaryText("" + message);

        long[] v = {500, 1000};

        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL("" + imgUrl).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (imgUrl.equals("") || imgUrl == null) {
            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();

            // You can specify sound
            notifySound = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            mBuilder = new NotificationCompat.Builder(this);
            //mBuilder.setSmallIcon(R.mipmap.notification_icon); // You can change your

            mBuilder.setSmallIcon(getNotificationIcon()); // You can change your

            //  icon
            mBuilder.setContentText("" + body);
            mBuilder.setContentTitle("" + message);
            mBuilder.setSound(notifySound);
            mBuilder.setAutoCancel(true);
            mBuilder.setColor(Color.parseColor("#cf1429"));

            long[] v1 = {500, 1000};
            mBuilder.setVibrate(v1);
            PendingIntent pIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100),
                    intentw, 0);

            mBuilder.setContentIntent(pIntent);

            NotificationManager mNotificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            //mNotificationManager.notify(resourceID, mBuilder.build());
            mNotificationManager.notify((int) (Math.random() * 100), mBuilder.build());//This will generate seperate notification each time server sends.
            //mNotificationManager.notify(resourceID, mBuilder.build());
            notificationIntent = new Intent(this, DashboardFragment.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            // notificationIntent.putExtra("notification", notification);
            notificationIntent.setData((Uri.parse("custom://"
                    + System.currentTimeMillis())));
            notificationIntent.setAction("actionstring"
                    + System.currentTimeMillis());
        } else {
            notiStyle.bigPicture(remote_picture);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent = null;

            Intent gotoIntent = new Intent();
            gotoIntent.setClassName(this, String.valueOf(intentw));//Start activity wh`en user taps on notification.
            contentIntent = PendingIntent.getActivity(this,
                    (int) (Math.random() * 100), intentw,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    getApplicationContext());
            mBuilder.setColor(Color.parseColor("#cf1429"));
            android.app.Notification notification = mBuilder.setSmallIcon(getNotificationIcon()).setTicker("Gulf Oil").setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle("" + message)
                    .setContentText("" + body)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText("" + message))
                    .setContentIntent(contentIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setVibrate(v)
                    // .setContentText("" + data)
                    .setStyle(notiStyle).build();

            notification.flags = android.app.Notification.FLAG_AUTO_CANCEL;
            //count++;
            notificationManager.notify((int) (Math.random() * 100), notification);//This will generate seperate notification each time server sends.
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);
        // store device token for use latter in application.
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // 0 - for private mode
        final SharedPreferences.Editor editor2 = pref.edit();
        editor2.putString("FCM_DeviceToken", token);
        editor2.apply();
    }
}