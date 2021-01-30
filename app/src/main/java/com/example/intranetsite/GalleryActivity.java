package com.example.intranetsite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    FloatingActionButton actionBtn, uploadPhoto, toPlayVideo, toPlayAudio;
    boolean showFABMenu = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        initViews();
        intent = getIntent();

        if (intent != null){
        params = new HashMap<>();
            params.put("email", intent.getStringExtra("email"));
        }

        new Thread(this::makeServiceCall).start();

        adapter = new GalleryRecViewAdapter(this);
        recView.setAdapter(adapter);
        recView.setLayoutManager(new GridLayoutManager(this, 2));

        actionBtn.setOnClickListener(v ->{
            if (!showFABMenu){
                displayFABMenu();
                actionBtn.setImageResource(R.drawable.ic_action_cross);
            }else {
                hideFABMenu();
                actionBtn.setImageResource(R.drawable.ic_action_more);
            }
            showFABMenu = !showFABMenu;
        });
        setFABMenuClickListeners();
    }

    private void setFABMenuClickListeners() {
        uploadPhoto.setOnClickListener(v -> {
            Toast.makeText(this, "Navigate to uploading a photo", Toast.LENGTH_SHORT).show();
        });
        toPlayAudio.setOnClickListener(v ->{
            Toast.makeText(this, "Navigate to playing audio", Toast.LENGTH_SHORT).show();
        });
        toPlayVideo.setOnClickListener(v ->{
            Toast.makeText(this, "Navigate to playing video", Toast.LENGTH_SHORT).show();
        });
    }

    private void makeServiceCall() {
        new ApiRequestsHandler(this).makeJsonObjectGetRequest(params, "photos", new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                JSONArray data = (JSONArray) response;
                try {
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); i++){
                            JSONObject object = data.getJSONObject(i);
                            photos.add(new Photo(object.getInt("id"), object.getString("description"), object.getString("picture")));
                        }
                    }
                    adapter.setPictures(photos);
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
    }

    private void displayFABMenu() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) uploadPhoto.getLayoutParams();
        layoutParams.rightMargin += (int) (uploadPhoto.getWidth() * 1.7);
        layoutParams.bottomMargin += (int) (uploadPhoto.getHeight() * 0.25);
        uploadPhoto.setLayoutParams(layoutParams);
        uploadPhoto.setVisibility(View.VISIBLE);
        uploadPhoto.startAnimation(showAnimation(1));
        uploadPhoto.setClickable(true);

        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) toPlayAudio.getLayoutParams();
        layoutParams2.rightMargin += (int) (toPlayAudio.getWidth() * 1.5);
        layoutParams2.bottomMargin += (int) (toPlayAudio.getHeight() * 1.5);
        toPlayAudio.setLayoutParams(layoutParams2);
        toPlayAudio.setVisibility(View.VISIBLE);
        toPlayAudio.startAnimation(showAnimation(2));
        toPlayAudio.setClickable(true);

        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) toPlayVideo.getLayoutParams();
        layoutParams3.rightMargin += (int) (toPlayVideo.getWidth() * 0.25);
        layoutParams3.bottomMargin += (int) (toPlayVideo.getHeight() * 1.7);
        toPlayVideo.setLayoutParams(layoutParams3);
        toPlayVideo.setVisibility(View.VISIBLE);
        toPlayVideo.startAnimation(showAnimation(3));
        toPlayVideo.setClickable(true);
    }

    private void hideFABMenu(){
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) uploadPhoto.getLayoutParams();
        layoutParams.rightMargin -= (int) (uploadPhoto.getWidth() * 1.7);
        layoutParams.bottomMargin -= (int) (uploadPhoto.getHeight() * 0.25);
        uploadPhoto.setLayoutParams(layoutParams);
        uploadPhoto.startAnimation(hideAnimation(1));
        uploadPhoto.setClickable(true);

        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) toPlayAudio.getLayoutParams();
        layoutParams2.rightMargin -= (int) (toPlayAudio.getWidth() * 1.5);
        layoutParams2.bottomMargin -= (int) (toPlayAudio.getHeight() * 1.5);
        toPlayAudio.setLayoutParams(layoutParams2);
        toPlayAudio.startAnimation(hideAnimation(2));
        toPlayAudio.setClickable(true);

        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) toPlayVideo.getLayoutParams();
        layoutParams3.rightMargin -= (int) (toPlayVideo.getWidth() * 0.25);
        layoutParams3.bottomMargin -= (int) (toPlayVideo.getHeight() * 1.7);
        toPlayVideo.setLayoutParams(layoutParams3);
        toPlayVideo.startAnimation(hideAnimation(3));
        toPlayVideo.setClickable(true);
    }

    private void initViews() {
        recView = findViewById(R.id.photoRecView);
        actionBtn = findViewById(R.id.actionBtn);
        uploadPhoto = findViewById(R.id.uploadPhoto);
        toPlayAudio = findViewById(R.id.toPlayAudio);
        toPlayVideo = findViewById(R.id.toPlayVideo);
    }

    private Animation showAnimation(int flag){
        switch (flag){
            case 1:
                return AnimationUtils.loadAnimation(this, R.anim.show_uploadphoto);
            case 2:
                return AnimationUtils.loadAnimation(this, R.anim.show_toplayaudio);
            case 3:
                return AnimationUtils.loadAnimation(this, R.anim.show_toplayvideo);
            default:
                return null;
        }
    }

    private Animation hideAnimation(int flag) {
        switch (flag){
            case 1:
                return AnimationUtils.loadAnimation(this, R.anim.hide_uploadphoto);
            case 2:
                return AnimationUtils.loadAnimation(this, R.anim.hide_toplayaudio);
            case 3:
                return AnimationUtils.loadAnimation(this, R.anim.hide_toplayvideo);
            default:
                return null;
        }
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
        }else if (item.getItemId() == R.id.logoutMenu){
            Intent logoutIntent = new Intent(GalleryActivity.this, MainActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(logoutIntent);
            return true;
        }else if (item.getItemId() == R.id.uploadAudioMenu){
            Toast.makeText(this, "Upload Audio", Toast.LENGTH_SHORT).show();
            return true;
        }else if (item.getItemId() == R.id.uploadVideoMenu){
            Toast.makeText(this, "Upload Video", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

}