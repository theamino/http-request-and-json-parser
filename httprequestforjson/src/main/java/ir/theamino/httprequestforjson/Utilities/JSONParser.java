package ir.theamino.httprequestforjson.Utilities;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONParser {

    static final int TIMEOUT = 5000;
    static java.io.InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jArray = null;
    static String json = "";

    public JSONParser() {

    }


    public JSONObject makeHttpRequest(String url, String method,
                                      List<NameValuePair> params) {

        is = null;
        jObj = null;
        json = "";
        try {

            if (method.contentEquals("POST")) {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
                httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } else if (method.contentEquals("GET")) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
                httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
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
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        try {
            if (json.isEmpty()) {
                json = "{\"success\"=0,\"message\"=\"Connection timeout\"}";
            }
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jObj;

    }

    public JSONArray makeHttpRequestForArray(String url) {
        List<NameValuePair> params = new ArrayList<>();
        return makeHttpRequestForArray(url, "GET", params);
    }

    public JSONArray makeHttpRequestForArray(String url, String method,
                                             List<NameValuePair> params) {

        is = null;
        jObj = null;
        json = "";
        try {

            if (method.contentEquals("POST")) {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
                httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } else if (method.contentEquals("GET")) {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
                httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, Charset.forName("UTF-8")), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        try {
            if (json.isEmpty()) {
                json = "{\"success\"=0,\"message\"=\"Connection timeout\"}";
            }
            jArray = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jArray;

    }

    public void jsonRequest(Context context, final String url , final List<NameValuePair> nameValuePairs, final JSONReceivedListener jsonReceivedListener) {


        JSONObject jsonData = new JSONObject();
        try {
            for (NameValuePair nv: nameValuePairs
                 ) {
                jsonData.put(nv.getName(), nv.getValue().toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int state = -1;
                Log.e("REQ" , response.toString());
                jsonReceivedListener.onJSONReceivedListener(response);

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("REQ" , error.getMessage());

                jsonReceivedListener.onJSONErrorListener();
                error.printStackTrace();
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonData, listener, errorListener) {
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Content-Type", "application/json; charset=utf-8");
                return hashMap;
            }
        };
        Log.e("REQ2 " , request.toString());
        VolleyRequests.getInstance(context).addToRequestQueue(request);
    }

    public void jsonRequestForArray(Context context, final String url , final List<NameValuePair> nameValuePairs, final JSONReceivedListener jsonReceivedListener) {
        HashMap<String, String> map = new HashMap<String, String>();
        for (NameValuePair nv: nameValuePairs
        ) {
            map.put(nv.getName(), nv.getValue());
        }
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int state = -1;
                Log.e("REQ" , response.toString());
                jsonReceivedListener.onJSONReceivedListener(response);

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("REQ" , error.getMessage());

                jsonReceivedListener.onJSONErrorListener();
                error.printStackTrace();
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(map), listener, errorListener) {
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Content-Type", "application/json; charset=utf-8");
                return hashMap;
            }
        };
        Log.e("REQ2 " , request.toString());
        VolleyRequests.getInstance(context).addToRequestQueue(request);
    }

    public void jsonRequest(Context context, final String url , final List<NameValuePair> nameValuePairs, final JSONReceivedListener jsonReceivedListener, int timeout) {


        JSONObject jsonData = new JSONObject();
        try {
            for (NameValuePair nv: nameValuePairs
            ) {
                jsonData.put(nv.getName(), nv.getValue().toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int state = -1;
                Log.e("REQ" , response.toString());
                jsonReceivedListener.onJSONReceivedListener(response);

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("REQ" , error.getMessage());

                jsonReceivedListener.onJSONErrorListener();
                error.printStackTrace();
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonData, listener, errorListener) {
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Content-Type", "application/json; charset=utf-8");
                return hashMap;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Log.e("DATA", jsonData.toString());
        VolleyRequests.getInstance(context).addToRequestQueue(request);
    }
}