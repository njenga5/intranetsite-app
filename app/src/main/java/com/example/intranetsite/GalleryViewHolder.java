package com.example.intranetsite;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

public class GalleryViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgPhoto, expand;
    public TextView description;
    public MaterialCardView parent;
    public GalleryViewHolder(@NonNull View itemView) {
        super(itemView);
        imgPhoto = itemView.findViewById(R.id.imgPhoto);
        expand = itemView.findViewById(R.id.expand);
        description = itemView.findViewById(R.id.description);
        parent = itemView.findViewById(R.id.photoParent);
    }
}
