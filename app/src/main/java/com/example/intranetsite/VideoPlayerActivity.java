package com.example.intranetsite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

public class VideoPlayerActivity extends AppCompatActivity {
    private static final String TAG = VideoPlayerActivity.class.getSimpleName();
    private PlayerView playerView;
    private SimpleExoPlayer player;
    private ProgressBar progressBar;
    private PlaybackStateListener playbackStateListener;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.video_view);
        progressBar = findViewById(R.id.progress);
        playbackStateListener = new PlaybackStateListener();
    }

    private class PlaybackStateListener implements Player.EventListener{
        @Override
        public void onPlaybackStateChanged(int state) {
            switch (state){
                case ExoPlayer.STATE_IDLE:
                    break;
                case ExoPlayer.STATE_ENDED:
                    Log.d(TAG, "onPlaybackStateChanged: playback ended");
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case ExoPlayer.STATE_READY:
                    progressBar.setVisibility(View.GONE);
                    break;
                default:
                    Log.e(TAG, "onPlaybackStateChanged: Unknown State");
            }
        }
    }

    private void initializePlayer() {
        if (player == null) {
            player = new SimpleExoPlayer.Builder(this).build();
            playerView.setPlayer(player);
            Intent intent = getIntent();
            if (intent != null){
                String url = intent.getStringExtra("url");
                if ((url != null)){
                    MediaItem mediaItem = MediaItem.fromUri(Uri.parse(url));
                    player.setMediaItem(mediaItem);
                }else{
                    String [] urls = intent.getStringArrayExtra("urls");
                    for (int i = 0;i < urls.length;i++){
                        if (i == 0){
                            MediaItem initItem = MediaItem.fromUri(Uri.parse(urls[i]));
                            player.setMediaItem(initItem);
                        }else{
                            MediaItem subseqItems = MediaItem.fromUri(Uri.parse(urls[i]));
                            player.addMediaItem(subseqItems);
                        }
                    }
                }
            }
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            player.addListener(playbackStateListener);
            player.prepare();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.removeListener(playbackStateListener);
            player.release();
            player = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }
}