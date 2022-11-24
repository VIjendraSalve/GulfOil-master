package com.taraba.gulfoilapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by android1 on 12/17/15.
 */
public class ConnectionDetector {


    private static Context _context;

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

//    public static boolean isConnectingToInternet() {
//       /* ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
//          if (connectivity != null)
//          {
//              NetworkInfo[] info = connectivity.getAllNetworkInfo();
//              if (info != null)
//                  for (int i = 0; i < info.length; i++)
//                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
//                      {
//                          return true;
//                      }
//          }
//          return false;*/
//        try {
//            ConnectivityManager cm = (ConnectivityManager) _context
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//
//            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//            if (null != activeNetwork) {
//                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
//                    return true;
//
//                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
//                    return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    public static String postPushRegID(String url, String regID, Context con) {
        /*String paramString = "{\"registrationId\":\"" + regID
                + "\",\"mobilePlatformId\":\"1\",\"projectAppNumber\":"
				+ Constants.PUSH_APP_NO + "}";*/
        Log.e("", "URl : " + url);
        HttpEntity httpEntity = null;
        try {
            httpEntity = new StringEntity(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String response = get(url, httpEntity, con);
        Log.e("response : ", "response : " + response);
        return response;
    }

    private static String get(String url, HttpEntity entity, Context context) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json");
        String responseString = null;
        response = executeService(httpclient, httpGet, context);
        if (response == null) {
            responseString = null;
        } else {
            StatusLine statusLine = response.getStatusLine();
            Log.d("response line", String.valueOf(statusLine.getStatusCode()));
            if (statusLine.getStatusCode() == HttpStatus.SC_CREATED
                    || statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    response.getEntity().writeTo(out);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                responseString = out.toString();
                Log.d("response", responseString);
            }
        }
        return responseString;
    }

    public static boolean isNetworkAvailable(Context con) {
        ConnectivityManager connectivityManager = (ConnectivityManager) con
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static HttpResponse executeService(HttpClient httpClient,
                                               HttpUriRequest httpMethod, Context con) {
        HttpResponse response = null;
        if (isNetworkAvailable(con)) {
            try {
                response = httpClient.execute(httpMethod);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Toast.makeText(con, R.string.internet_unavailable_alert,
            // Toast.LENGTH_SHORT).show();
        }
        return response;
    }

    public static int getConnectivityStatus() {
        ConnectivityManager cm = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static int getSubType() {
        ConnectivityManager cm = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            return activeNetwork.getSubtype();
        }
        return activeNetwork.getSubtype();
    }

    public static String getConnectivityStatusString() {
        int conn = ConnectionDetector.getConnectivityStatus();
        String status = null;
        if (conn == ConnectionDetector.TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == ConnectionDetector.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == ConnectionDetector.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

    public static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            WifiManager wifiManager = (WifiManager) _context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            boolean flag = false;
            if (wifiInfo != null) {
                Integer linkSpeed = wifiInfo.getLinkSpeed();
                Log.e("Error : ", "Error : " + linkSpeed);
                if (linkSpeed <= 1) {
                    flag = false;
                } else {
                    flag = true;
                }
            }
            return flag;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
            /*
             * Above API level 7, make sure to set android:targetSdkVersion
             * to appropriate level to use these
             */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }
}
