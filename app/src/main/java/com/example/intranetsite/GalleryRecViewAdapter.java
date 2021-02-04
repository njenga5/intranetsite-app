package com.example.intranetsite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryRecViewAdapter extends RecyclerView.Adapter<GalleryViewHolder> {
    ArrayList<Photo> photos = new ArrayList<>();
    Context context;

    public GalleryRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_list_item, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        String desc = photos.get(position).getDescription();
        if (desc.isEmpty()){
            holder.description.setVisibility(View.GONE);
            holder.expand.setVisibility(View.GONE);
        }else {
            holder.description.setText(desc);
        }
        Glide.with(context).asBitmap()
                .load(photos.get(position).getUrl())
                .into(holder.imgPhoto);
        holder.parent.setOnClickListener(v -> {
            Toast.makeText(context, photos.get(position).getId() + " Selected", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void setPictures(ArrayList<Photo> photos){
        this.photos = photos;
        notifyDataSetChanged();
    }
}
