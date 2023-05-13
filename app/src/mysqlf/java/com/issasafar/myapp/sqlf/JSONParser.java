package com.issasafar.myapp.sqlf;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class JSONParser {
    static InputStream is = null;
    static JSONObject sObject = null;
    static String json = "";
    static String error = "";

    public JSONParser() {
    }

    public JSONObject makeHttpRequest(String url, String method, ArrayList<NameValuePair> params) {
        try {
            if (method.equals("POST")) {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                try {
                    Log.e("API123::stream2", "" + stream2string(httpPost.getEntity().getContent()));
                    Log.e("API123::uri", httpPost.getURI().toString());
                } catch (Exception e) {
                    Log.e("API123::excp",e.getMessage());
                }

                HttpResponse httpResponse = httpClient.execute(httpPost);
                Log.e("API123::post", "helllo");
                Log.e("API123::response", "" + httpResponse.getStatusLine().getStatusCode());
                error = String.valueOf(httpResponse.getStatusLine().getStatusCode());
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            } else if (method.equals("GET")) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                Log.d("API123::paramString", paramString);
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
        } catch (IOException e) {
            Log.e("API123::excp",e.getMessage());
            e.printStackTrace();
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.d("API123", json);
        } catch (Exception e) {
            Log.e("Buffer Error","Error converting result "+e.toString());
        }
        try {
            sObject = new JSONObject(json);
            sObject.put("error_code", error);
        } catch (JSONException e) {
            Log.e("JSON Parser","Error parsing data "+e.toString());
        }
        return sObject;
    }

    private String stream2string(InputStream is) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line=null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
}
