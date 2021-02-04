package com.example.intranetsite;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

/**
 * Handles all requests and responses to and from the API endpoints
 */
public class ApiRequestsHandler {
    private static final String BASE_URL  = "http://192.168.43.13:8000/api/";
//    private static final String BASE_URL  = "https://intranetsite.herokuapp.com/api/";
    private final Context context;

    public ApiRequestsHandler(Context context) {
        this.context = context;
    }

    /**
     * Handles all get requests to the API endpoints
     * @param getParams request get parameters
     * @param volleyResponseListener callback
     * @param endPoint url endPoint
     *
     */
    public void makeJsonObjectGetRequest(Map<String, String> getParams, String endPoint, VolleyResponseListener volleyResponseListener){
        String email = getParams.get("email");
        String password = getParams.get("password");
        String queryString;
        Request<?> request;
        if (endPoint.equals("users")){
            queryString = "users?email=" + email + "&password=" + password;
            request = new JsonObjectRequest(BASE_URL + queryString, null, volleyResponseListener::onResponse, volleyResponseListener::onError);
        }else {
            queryString = String.format("%s/%s", endPoint, email);
            request = new JsonArrayRequest(BASE_URL + queryString, volleyResponseListener::onResponse, volleyResponseListener::onError);
        }
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
     * Handles all POST requests to the API endpoints.
     * @param postParams data to be posted with the request
     * @param volleyResponseListener callback
     * @param endPoint url endpoint
     */

    public void makeJsonObjectPostRequest(Map<String, String> postParams, String endPoint, VolleyResponseListener volleyResponseListener){
        String token = generateRandom();
        postParams.put("token", token);
        JSONObject jsonRequest = new JSONObject(postParams);
        String queryString;
        if (endPoint.equals("users")){
            queryString = "users?token=" + token;
        }else {
            queryString = String.format("%s/%s", endPoint, postParams.get("email"));
        }
        JsonObjectRequest request = new JsonObjectRequest(BASE_URL + queryString, jsonRequest, volleyResponseListener::onResponse, volleyResponseListener::onError);
        RequestQueueSingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     *
     * @param updateParams Fields to be updated
     * @param volleyResponseListener the callback
     * @param endpoint url endpoint
     */
    public void updateDetailsRequest(Map<String, String> updateParams, String endpoint, VolleyResponseListener volleyResponseListener){
        String email = updateParams.get("email");
        String password = updateParams.get("password");
        JSONObject jsonRequest = new JSONObject(updateParams);
        String queryString;
        if (endpoint.equals("users")){
            queryString = "users?email=" + email + "&password=" + password;
        }else{
            queryString = String.format("%s/%s", endpoint, email);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASE_URL + queryString, jsonRequest, volleyResponseListener::onResponse, volleyResponseListener::onError);
        RequestQueueSingleton.getInstance(context).addToRequestQueue(request);
    }

}
