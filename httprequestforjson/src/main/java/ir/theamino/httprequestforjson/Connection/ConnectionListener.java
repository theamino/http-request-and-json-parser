package ir.theamino.httprequestforjson.Connection;

import android.content.Context;

import org.json.JSONObject;

public interface ConnectionListener {
    void onConnectionSuccessfullyLoadData(Context context, JSONObject jsonObject);
    void onConnectionError(Context context, String message);
}
