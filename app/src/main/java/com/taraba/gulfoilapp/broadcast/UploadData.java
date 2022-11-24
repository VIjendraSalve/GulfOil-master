package com.taraba.gulfoilapp.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.SplashActivity;
import com.taraba.gulfoilapp.adapter.BarcodeHistoryModel;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;
import com.taraba.gulfoilapp.util.NetworkUtils;
import com.taraba.gulfoilapp.util.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by android3 on 2/12/16.
 */
public class UploadData extends BroadcastReceiver {
    NotificationCompat.Builder mBuilder;
    Intent notificationIntent;
    int mNotificationId = 001;
    Uri notifySound;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (NetworkUtils.isNetworkAvailable(context)) {
            try {
                SharedPreferences preferences = context.getSharedPreferences(
                        "signupdetails", Context.MODE_PRIVATE);

                String mStringlogin_id = preferences.getString("usertrno", "");
                UserTableDatasource ctdUser = new UserTableDatasource(context);
                ctdUser.open();
                ArrayList<BarcodeHistoryModel> circularList = new ArrayList<BarcodeHistoryModel>();
                ArrayList<String> arrParty = new ArrayList<String>();
                arrParty = ctdUser.getAllParticipantIds();

                if (arrParty.size() > 0) {
                    for (int part = 0; part < arrParty.size(); part++) {
                        circularList = ctdUser.getAllBarcode(arrParty.get(part));
                        JSONObject mStringcodes = new JSONObject();
                        for (int j = 0; j < circularList.size(); j++) {
                            try {

                                mStringcodes.put(String.valueOf(j), circularList.get(j).getText());
                                ctdUser.updatePublishStattus(circularList.get(j).getId());
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        if (mStringcodes.length() <= 0) {

                        } else {
                            Log.e(" BarcodeList", " Size : " + circularList.size() + " Codes :" + mStringcodes);
                            new UploadCodes(context).execute(new String[]{mStringlogin_id, String.valueOf(arrParty.get(part)), mStringcodes.toString()});
                        }
                    }
                } else {

                }
            } catch (Exception e) {

            }
        }
    }

    class UploadCodes extends AsyncTask<String[], Void, JSONObject> {
        private ProgressDialog progressDialog;
        private Context mContext;

        public UploadCodes(Context context) {
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String[]... params) {
            JSONObject jObj = null;
            try {
                Log.e("Image in befor pass: ", "in Befor pass :" + params[0][0]);
                Log.e("in befor pass: ", "UserData pass :" + params[0][1]);
                Log.e("in befor pass: ", "UserData pass :" + params[0][2]);

                jObj = new UserFunctions().uploadCodes(params[0][0], params[0][1], params[0][2]);
            } catch (Exception e) {
                // TODO: handle exception

            } // end catch
            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject jObj) {
            super.onPostExecute(jObj);
            // showToast("Calling set up views");
            if (jObj != null) {

                Log.e("Json Data : ", "Json data : " + jObj);
                try {
                    ArrayList<String> CatSuccess = new ArrayList<String>();
                    ArrayList<String> CatFailed = new ArrayList<String>();
                    String mStringStatus = jObj.getString("status");
                    if (mStringStatus.equals("success")) {

                        NotificationFun(mContext);
/*
                        String strSuccessCode = null, strFailedCode = null;
                        Object json_successtoken = new JSONTokener(jObj.getString("succ_codes"));
                        // JSONArray jObjSuccessCode = jObj.getJSONArray("succ_codes");
                        if (json_successtoken instanceof JSONArray) {
                            JSONArray jarrSuccessCode = (JSONArray)json_successtoken;
                            if (jarrSuccessCode.length() <= 0) {
                                strSuccessCode = "Valid Codes not available";
                            } else {
                                for(int p=0; p<jarrSuccessCode.length(); p++ ) {
                                    strSuccessCode = strSuccessCode+""+jarrSuccessCode.get(p)+"\n";
                                }
                            }
                        } else {
                            JSONObject jObjSuccessCode1 = (JSONObject)json_successtoken;
                            Iterator<String> iterator = jObjSuccessCode1.keys();
                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                Log.e("TAG", "key : " + key + "--Value :: " + jObjSuccessCode1.optString(key));
                                CatSuccess.add(jObjSuccessCode1.optString(key));
                            }
                            // Converting ArrayList to String in Java using advanced for-each loop
                            StringBuilder sb = new StringBuilder();
                            for (String str : CatSuccess) {
                                sb.append(str).append("\n"); //separating contents using semi colon
                            }
                            strSuccessCode = sb.toString();
                            //strSuccessCode = "Valid Codes not available";
                        }
                        //-------------------------------------------------------------------------------------------------------------
                        Object json_failtoken = new JSONTokener(jObj.getString("failed_codes"));
                        // JSONObject jObjFailedCode = jObj.getJSONObject("failed_codes");
                        if (json_failtoken instanceof JSONObject) {

                            JSONObject jObjFailedCode = (JSONObject)json_failtoken;
                            Iterator<String> iterator = jObjFailedCode.keys();
                            while (iterator.hasNext()) {

                                String key = iterator.next();
                                Log.e("TAG", "key : " + key + "--Value :: " + jObjFailedCode.optString(key));

                                CatFailed.add(jObjFailedCode.optString(key));
                            }
                            // Converting ArrayList to String in Java using advanced for-each loop
                            StringBuilder sb = new StringBuilder();
                            for (String str : CatFailed) {
                                sb.append(str).append("\n"); //separating contents using semi colon
                            }
                            strFailedCode = sb.toString();
                        } else {
                            JSONArray jarrFailCode = (JSONArray)json_failtoken;
                            if (jarrFailCode.length() <= 0) {
                                strFailedCode = "Valid Codes not available";
                            } else {
                                for(int p=0; p<jarrFailCode.length(); p++ ) {
                                    strFailedCode = strFailedCode+""+jarrFailCode.get(p)+"\n";
                                }
                            }
                        }*/
//-------------------------------------------------------------------------------------------------------------
                    } else {

                    }
                } catch (JSONException e) {
                    Log.e("Error : ", "Error : " + e.toString());
                }
            }
        }
    }


    public void NotificationFun(Context context) {
        int icon = R.mipmap.ic_launcher;
        String strtitle = context.getString(R.string.app_name);
        Intent intent = new Intent(context, SplashActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
/*

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Accumulate code data submited successfully. You can check status in claim history.")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Accumulate code data submited successfully. You can check status in claim history."))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("Accumulate code data submited successfully. You can check status in claim history.")
                .addAction(R.mipmap.ic_launcher, "Action Button", pIntent)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

// Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
// Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());
*/


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context);
        Notification notification = mBuilder.setSmallIcon(icon).setTicker(strtitle).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(strtitle)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Accumulate code data submited successfully. You can check status in claim history."))
                .setContentIntent(pIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentText("Accumulate code data submited successfully. You can check status in claim history.").build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationId, notification);

    }

/*
    public void generateNotification(Context context)
    {

        NotificationManager notif=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification(R.mipmap.ic_launcher,"Grass Root App",System.currentTimeMillis());
        PendingIntent pending= PendingIntent.getActivity(context.getApplicationContext(), 0, new Intent(), 0);

        notify.setLatestEventInfo(context.getApplicationContext(),subject,body,pending);
        notif.notify(0, notify);

        // notificationIntent = new Intent(context, SplashActivity.class);
        notifySound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder = new NotificationCompat.Builder(context);
       // mBuilder.setSmallIcon(R.mipmap.ic_launcher); //You can change your icon
        mBuilder.setContentText("Accumulate code data submited successfully. You can check status in claim history.");
        mBuilder.setContentTitle("Grass Root App");
        mBuilder.setSound(notifySound);
        mBuilder.setAutoCancel(true);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, SplashActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(mNotificationId, mBuilder.build());
    }*/
}