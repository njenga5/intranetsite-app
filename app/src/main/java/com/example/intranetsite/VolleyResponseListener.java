package com.example.intranetsite;


import com.android.volley.VolleyError;

public interface VolleyResponseListener {
    void onResponse(Object response);
    void onError(VolleyError error);
}
