package com.example.intranetsite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GalleryActivity extends AppCompatActivity {
    private static final String TAG = GalleryActivity.class.getSimpleName();
    Intent intent;
    Map<String, String> params;
    ArrayList<Photo> photos = new ArrayList<>();
    RecyclerView recView;
    GalleryRecViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        intent = getIntent();

        if (intent != null){
        params = new HashMap<>();
            params.put("email", intent.getStringExtra("email"));
        }

        new ApiRequestsHandler(this).makeJsonObjectGetRequest(params, "photos", new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                JSONArray data = (JSONArray) response;
                try {
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); i++){
                            JSONObject o = data.getJSONObject(i);
                            photos.add(new Photo(o.getInt("id"), o.getString("description"), o.getString("picture")));
                        }
                        adapter.setPictures(photos);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.e(TAG, "onError: " + error.getMessage());
                Toast.makeText(GalleryActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new GalleryRecViewAdapter(this);
        recView = findViewById(R.id.photoRecView);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            if (intent != null) {
                Intent intent1 = new Intent(GalleryActivity.this, ProfileActivity.class);
                intent1.putExtra("email", intent.getStringExtra("email"));
                intent1.putExtra("name", intent.getStringExtra("name"));
                intent1.putExtra("phoneNumber", intent.getStringExtra("phoneNumber"));
                intent1.putExtra("password", intent.getStringExtra("password"));
                startActivity(intent1);
            }
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

}