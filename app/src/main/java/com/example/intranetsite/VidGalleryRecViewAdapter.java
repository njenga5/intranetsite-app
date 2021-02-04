package com.example.intranetsite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class VidGalleryRecViewAdapter extends RecyclerView.Adapter<VidGalleryViewHolder> {
    ArrayList<Video> videos = new ArrayList<>();
    Context context;

    public VidGalleryRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public VidGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vid_gallery_list_item, parent, false);
        return new VidGalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VidGalleryViewHolder holder, int position) {
        String desc = videos.get(position).getDescription();
        holder.txtTitle.setText(videos.get(position).getTitle());
        if (!desc.isEmpty()){
            holder.txtDesc.setText(desc);
        }
        Glide.with(context)
                .asBitmap()
                .load(videos.get(position).getThumbnailUrl())
                .into(holder.vidThumbnail);
        holder.parent.setOnClickListener(v ->{
            Intent intent = new Intent(context, VideoPlayerActivity.class);
            intent.putExtra("url", videos.get(position).getUrl());
            context.startActivity(intent);
        });
        holder.expandIcon.setOnClickListener(v ->{
            holder.txtDesc.setVisibility(View.VISIBLE);
            holder.collapseIcon.setVisibility(View.VISIBLE);
            holder.txtDelete.setVisibility(View.VISIBLE);
            holder.expandIcon.setVisibility(View.GONE);
        });
        holder.collapseIcon.setOnClickListener(v ->{
            holder.txtDesc.setVisibility(View.GONE);
            holder.collapseIcon.setVisibility(View.GONE);
            holder.txtDelete.setVisibility(View.GONE);
            holder.expandIcon.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void setVideos(ArrayList<Video> videos){
        this.videos = videos;
        notifyDataSetChanged();
    }
}
