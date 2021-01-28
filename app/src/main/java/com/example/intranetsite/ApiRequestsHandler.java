package com.example.intranetsite;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

/**
 * Handles all requests and responses to and from the server
 */
public class ApiRequestsHandler {
//    private static final String BASE_URL  = "http://192.168.43.13:8000/api/";
    private static final String BASE_URL  = "https://intranetsite.herokuapp.com/api/";
    private final Context context;

    public ApiRequestsHandler(Context context) {
        this.context = context;
    }

    /**
     * Makes a get request and relays responses or errors to the callback
     * @param getParams request get parameters
     * @param volleyResponseListener callback
     *
     */
    public void makeJsonObjectGetRequest(Map<String, String> getParams, VolleyResponseListener volleyResponseListener){
        String email = getParams.get("email");
        String password = getParams.get("password");
        String queryString = "users/?email=" + email + "&password=" + password;
        JsonObjectRequest request = new JsonObjectRequest(BASE_URL + queryString, null, volleyResponseListener::onResponse, volleyResponseListener::onError);
        RequestQueueSingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Generate a random string to be used as the token
     * @return String generated token
     */
    private String generateRandom(){
        String alphaNumeric = "abcdefg12hijkl89mnopqrstu345vwxyz670";
        StringBuilder token  = new StringBuilder(20);
        for (int i = 0; i < 20; i++) {
            int index = (int) (alphaNumeric.length()*Math.random());
            token.append(alphaNumeric.charAt(index));
        }
        return token.toString();
    }

    /**
     * This method makes a POST request and also posts the data into the database.
     * @param postParams data to be posted with the request
     * @param volleyResponseListener callback
     */

    public void makeJsonObjectPostRequest(Map<String, String> postParams, VolleyResponseListener volleyResponseListener){
        String token = generateRandom();
        postParams.put("token", token);
        JSONObject jsonRequest = new JSONObject(postParams);
        String queryString = "users/?token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(BASE_URL + queryString, jsonRequest, volleyResponseListener::onResponse, volleyResponseListener::onError);
        RequestQueueSingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     *
     * @param updateParams Fields to be updated
     * @param volleyResponseListener the callback
     */
    public void updateUserDetailsRequest(Map<String, String> updateParams, VolleyResponseListener volleyResponseListener){
        String email = updateParams.get("email");
        String password = updateParams.get("password");
        JSONObject jsonRequest = new JSONObject(updateParams);
        String queryString = "users/?email=" + email + "&password=" + password;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASE_URL + queryString, jsonRequest, volleyResponseListener::onResponse, volleyResponseListener::onError);
        RequestQueueSingleton.getInstance(context).addToRequestQueue(request);
    }

}
