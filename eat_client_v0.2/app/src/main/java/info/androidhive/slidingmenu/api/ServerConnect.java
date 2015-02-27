//package edu.dartmouth.cs.layouts.utils;
package info.androidhive.slidingmenu.api;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import info.androidhive.slidingmenu.interfaces.ApiAsyncResponse ;
import info.androidhive.slidingmenu.interfaces.LoginAsyncResponse;


/**
 * Created by Dharmanshu on 2/18/15.
 */

public class ServerConnect extends AsyncTask<String , Void, String>{
    public LoginAsyncResponse delegate= null;
    protected String url = "http://ec2-54-191-192-216.us-west-2.compute.amazonaws.com:8001/api/";

    protected String connect(String type , String path , String params) {
        url += path;
        String post_string = "";

        try {
            if (type == "GET") {
                url += "?";
                url +=params;

            } else {

//                JSONObject json_data = new JSONObject(params);
//                StringEntity se = new StringEntity(json_data.toString());
                post_string = params;
            }

            URL u = new URL(url);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod(type);
            c.setRequestProperty("Content-type" , "Application/json");
            c.setDoOutput(true);
            c.setDoOutput(true);
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);

            if (type == "GET") {
                c.connect();
            } else {
                try {
                    DataOutputStream wr = new DataOutputStream(c.getOutputStream());
                    wr.write(post_string.getBytes(Charset.forName("utf-8")));
                } catch (Exception e) {
                }
            }

            int status = c.getResponseCode();
            Log.i("Status" , Integer.toString(c.getResponseCode()));
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    try {
                        JSONObject jsonObj = new JSONObject(sb.toString());
                        jsonObj.put("code" , Integer.toString(status));
                        return jsonObj.toString();
                    } catch(JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }
                case 401:
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("error" , "Invalid credentials.");
                        jsonObject.put("code" , "401");
                    } catch (Exception e) {

                    }
                    return jsonObject.toString();
            }

            return "Error detected";
        }
        catch (MalformedURLException ex) {
//            Logger.getLogger(DebugServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
//            Logger.getLogger(DebugServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    protected String doInBackground(String... params) {
        return connect(params[0] , params[1] , params[2]);
    }

    protected void onPostExecute(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            if (jsonObj.get("code") == "200") {
                delegate.processLoginSuccessful(result);
            }
        } catch (Exception e) {
            delegate.processFailed("Incorrect String");
        }
//        Log.i("onPostExecute" , result);

    }
}
