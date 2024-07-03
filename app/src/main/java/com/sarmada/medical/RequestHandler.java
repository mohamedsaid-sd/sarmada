package com.sarmada.medical;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestHandler {
    @SuppressLint("StaticFieldLeak")
    private static RequestHandler mInstance ;
    private RequestQueue mRequestQueue ;
    private final Context mCxt;

    private RequestHandler(Context context){
        mCxt = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RequestHandler getInstance(Context context){
        if(mInstance == null ){
            mInstance = new RequestHandler(context);
        }
        return mInstance ;
    }

    private RequestQueue getRequestQueue() {
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mCxt.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
