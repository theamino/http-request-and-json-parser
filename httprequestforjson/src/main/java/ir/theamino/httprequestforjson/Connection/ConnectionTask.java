package ir.theamino.httprequestforjson.Connection;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;

import ir.theamino.httprequestforjson.Utilities.JSONParser;
import ir.theamino.httprequestforjson.Utilities.JSONReceivedListener;

public class ConnectionTask extends AsyncTask<Void, Void, Void> implements JSONReceivedListener {

    Context context;
    private String url;
    List<NameValuePair> nameValuePairs;
    ConnectionListener listener;
    private int timeOut = 5000;
    JSONParser jsonParser = new JSONParser();

    private final String UNKNOWN_ERROR = "مشکلی پیش آمد";

    public ConnectionTask(Context context, String url, List<NameValuePair> nameValuePairs, ConnectionListener listener, int timeOut) {
        this.context = context;
        this.url = url;
        this.nameValuePairs = nameValuePairs;
        this.listener = listener;
        this.timeOut = timeOut;
    }

    public ConnectionTask(Context context, String url, List<NameValuePair> nameValuePairs, ConnectionListener listener) {
        this.context = context;
        this.url = url;
        this.nameValuePairs = nameValuePairs;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        jsonParser.jsonRequest(context, url, nameValuePairs, this, timeOut);
        return null;
    }

    @Override
    public void onJSONReceivedListener(JSONObject jsonObject) {
        try {
            listener.onConnectionSuccessfullyLoadData(context, jsonObject);
        } catch (Exception e) {
            listener.onConnectionError(context, UNKNOWN_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void onJSONErrorListener() {
        listener.onConnectionError(context, UNKNOWN_ERROR);
    }
}
