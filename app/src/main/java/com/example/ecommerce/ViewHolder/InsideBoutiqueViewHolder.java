package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.interfaces.ItemClickListener;
import com.google.android.material.imageview.ShapeableImageView;

public class InsideBoutiqueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView productName, productPrice;
    public ShapeableImageView productImage;

    public ItemClickListener listener;

    public InsideBoutiqueViewHolder(@NonNull View itemView) {
        super(itemView);

        productName = (TextView) itemView.findViewById(R.id.text_name);
        productPrice = (TextView) itemView.findViewById(R.id.text_price);
        productImage = (ShapeableImageView) itemView.findViewById(R.id.imageView);

    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
