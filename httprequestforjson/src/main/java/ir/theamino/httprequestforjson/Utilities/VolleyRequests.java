package ir.theamino.httprequestforjson.Utilities;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

public class VolleyRequests {
    private VolleyRequests(Context context) {mContext = context;
        mRequestQueue = getRequestQueue();
        VolleyLog.DEBUG = true;
    }

    private static VolleyRequests instance;

    private RequestQueue mRequestQueue;
    private static Context mContext;
    private String TAG = "";

    public static synchronized VolleyRequests getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new VolleyRequests(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag)
    {
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag)
    {
        if (mRequestQueue != null)
        {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void cancelPendingRequests()
    {
        if (mRequestQueue != null)
        {
            mRequestQueue.cancelAll(TAG);
        }
    }

}
