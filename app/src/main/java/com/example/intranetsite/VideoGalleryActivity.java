package com.example.intranetsite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class VideoGalleryActivity extends AppCompatActivity {
    private static final String TAG = VideoGalleryActivity.class.getSimpleName();
    private RecyclerView vidRecView;
    private VidGalleryRecViewAdapter adapter;
    private FloatingActionButton actionBtnVid;
    private final Map<String, String> params = new HashMap<>();
    private final ArrayList<Video> videos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallery);
        Intent intent = getIntent();
        if (intent != null){
            params.put("email", intent.getStringExtra("email"));
        }
        new Thread(this::makeServiceRequest).start();
        adapter = new VidGalleryRecViewAdapter(this);
        vidRecView = findViewById(R.id.videoRecView);
        vidRecView.setAdapter(adapter);
        vidRecView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        actionBtnVid = findViewById(R.id.actionBtnVid);
    }

    public void acquireVideos(ArrayList<Video> videos){
        String[] urls = new String[videos.size()];
        for(int i=0;i<videos.size();i++){
            urls[i] = videos.get(i).getUrl();
        }
        actionBtnVid.setOnClickListener(v ->{
            Intent intent1 = new Intent(VideoGalleryActivity.this, VideoPlayerActivity.class);
            intent1.putExtra("urls", urls);
            startActivity(intent1);
        });
    }

    public void makeServiceRequest(){
        new ApiRequestsHandler(this).makeJsonObjectGetRequest(params, "videos", new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                JSONArray data = (JSONArray) response;
                try {
                    if (data.length() > 0){
                        for (int i = 0; i < data.length();i++){
                            JSONObject object = data.getJSONObject(i);
                            String tempTitle = object.getString("title");
                            String title = tempTitle.length() < 29 ? tempTitle : tempTitle.substring(0, 29) + "...";
                            videos.add(new Video(
                                    object.getInt("id"),
                                    title,
                                    object.getString("video"),
                                    object.getString("description"),
                                    object.getString("thumbnail")));
                        }
                    }
                    adapter.setVideos(videos);
                    acquireVideos(videos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.e(TAG, "onError: " + error);
                Toast.makeText(VideoGalleryActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });

    }

}