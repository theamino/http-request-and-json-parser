package ir.theamino.httprequestforjson.Utilities;

import org.json.JSONObject;

public interface JSONReceivedListener {
    public void onJSONReceivedListener(JSONObject jsonObject);
    public void onJSONErrorListener();
}
