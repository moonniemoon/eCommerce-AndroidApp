package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.interfaces.ItemClickListener;

public class BoutiqueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView boutique_image, boutique_logo, emptyThing;
    public ItemClickListener listener;

    public BoutiqueViewHolder(@NonNull View itemView) {
        super(itemView);

        this.emptyThing = (ImageView) itemView.findViewById(R.id.boutique_logo_background);
        this.boutique_image = (ImageView) itemView.findViewById(R.id.boutique_image);
        this.boutique_logo = (ImageView) itemView.findViewById(R.id.boutique_logo);

    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
