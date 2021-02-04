package com.example.intranetsite;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

public class VidGalleryViewHolder extends RecyclerView.ViewHolder {
    ImageView vidThumbnail, expandIcon, collapseIcon;
    TextView txtTitle, txtDesc, txtDelete;
    MaterialCardView parent;
    public VidGalleryViewHolder(@NonNull View itemView) {
        super(itemView);
        vidThumbnail = itemView.findViewById(R.id.vidThumbnail);
        expandIcon = itemView.findViewById(R.id.expandIcon);
        txtTitle = itemView.findViewById(R.id.txtTitle);
        txtDesc = itemView.findViewById(R.id.txtDesc);
        parent = itemView.findViewById(R.id.vidParent);
        collapseIcon = itemView.findViewById(R.id.collapseIcon);
        txtDelete = itemView.findViewById(R.id.txtDelete);
    }
}
