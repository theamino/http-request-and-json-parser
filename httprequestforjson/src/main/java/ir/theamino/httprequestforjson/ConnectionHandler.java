package ir.theamino.httprequestforjson;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;

import java.util.List;

import ir.theamino.httprequestforjson.Connection.ConnectionListener;
import ir.theamino.httprequestforjson.Connection.ConnectionTask;

public class ConnectionHandler {
    private ConnectionHandler(){}

    private static ConnectionHandler instance = new ConnectionHandler();

    public static ConnectionHandler getInstance() {
        return instance;
    }

    public void ConnectAndReceive(Context context, String url, List<NameValuePair> nameValuePairs, ConnectionListener listener) {
        new ConnectionTask(context, url, nameValuePairs, listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void ConnectAndReceive(Context context, String url, List<NameValuePair> nameValuePairs, ConnectionListener listener, int timeout)  {
        new ConnectionTask(context, url, nameValuePairs, listener, timeout).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
