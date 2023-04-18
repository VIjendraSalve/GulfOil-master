package com.taraba.gulfoilapp.util;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.taraba.gulfoilapp.BuildConfig;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


/**
 * Created by android1 on 12/17/15.
 */
public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    Context context;

    // constructor
    public JSONParser() {

    }

    public JSONObject getJSONFromUrl(String url, JSONObject params) {

        Log.e("", "webservice request : " + params.toString());
        // Making HTTP request
        try {
            // defaultHttpClient
           /* DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

           HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();*/

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            //  httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpPost.setEntity(new StringEntity(params.toString(), "UTF-8"));


            String source = BuildConfig.AUTH_USERNAME + ":" + BuildConfig.AUTH_PASSWORD;

            /* This is old Authorization key*/
            // String source = "empower" + ":" + "empower123";

            String ret = "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
            httpPost.setHeader("Authorization", ret);
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("HTTP ERROR", e.toString() + "");

            //new SinkActivity().alertDialog("DonBosco School", "Server Encountered an Error. please Try again later.!!!!");

            //   new AlertDialogManager().showAlertDialog(new SinkActivity(), "DonBosco School", "Server Encountered an Error. please Try again later.!!!!", true);
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();
            Log.e("JSON Data  : ", "JSON DATA Response : " + sb.toString());
            json = sb.toString();
            //     createCacheFile(context, "response.txt", ""+jObj);
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);

        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public JSONObject getJSONFromUrlForFeedback(String url, JSONObject params) {

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(params.toString(), HTTP.UTF_8));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("HTTP ERROR", e.toString() + "");

            //new SinkActivity().alertDialog("DonBosco School", "Server Encountered an Error. please Try again later.!!!!");

            //   new AlertDialogManager().showAlertDialog(new SinkActivity(), "DonBosco School", "Server Encountered an Error. please Try again later.!!!!", true);
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();
            json = sb.toString();
            //     createCacheFile(context, "response.txt", ""+jObj);
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);

        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    public static File createCacheFile(Context context, String fileName, String json) {
    /*	String extStorageDirectory = Environment.getExternalStorageDirectory()
                .toString();
		File folder = new File(extStorageDirectory, MyConstants.directory_name);
		folder.mkdir();*/
        File cacheFile = new File(context.getFilesDir(), fileName);
        //	File cacheFile = new File(folder, fileName);
        try {
            FileWriter fw = new FileWriter(cacheFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();

            cacheFile = null;
        }

        return cacheFile;
    }

    public JSONObject getJSON(String url) {

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            ///  httpPost.addHeader("Authorization", "Basic "+ Base64.encodeBytes("login:password".getBytes()));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}