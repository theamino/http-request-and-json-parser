package ir.theamino.httprequestforjson.Connection;

import android.content.Context;
import android.os.AsyncTask;

import com.malek.xibo.mehr.malekdsm.R;
import com.malek.xibo.mehr.malekdsm.Utilities.JSONParser;
import com.malek.xibo.mehr.malekdsm.Utilities.JSONReceivedListener;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;

public class ConnectionTask extends AsyncTask<Void, Void, Void> implements JSONReceivedListener {

    Context context;
    private String url;
    List<NameValuePair> nameValuePairs;
    ConnectionListener listener;
    private int timeOut = 5000;
    JSONParser jsonParser = new JSONParser();

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
            listener.onConnectionError(context, R.string.error_unknown);
            e.printStackTrace();
        }
    }

    @Override
    public void onJSONErrorListener() {
        listener.onConnectionError(context, R.string.error_unknown);
    }
}
