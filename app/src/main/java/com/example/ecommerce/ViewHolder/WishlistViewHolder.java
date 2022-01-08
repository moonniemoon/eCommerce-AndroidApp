package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.interfaces.ItemClickListener;
import com.google.android.material.imageview.ShapeableImageView;

public class WishlistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productId, productName, productPrice, productCategory, productCompany;
    public ShapeableImageView productImage;
    public AppCompatButton cartButton;

    public ItemClickListener listener;

    public WishlistViewHolder(@NonNull View itemView) {
        super(itemView);

        productCompany = (TextView) itemView.findViewById(R.id.wishlist_productCompanyName);
        productId = (TextView) itemView.findViewById(R.id.wishlist_productID);
        productName = (TextView) itemView.findViewById(R.id.wishlist_productName);
        productCategory = (TextView) itemView.findViewById(R.id.wishlist_productCategory);
        productPrice = (TextView) itemView.findViewById(R.id.wishlist_price);
        productImage = (ShapeableImageView) itemView.findViewById(R.id.wishlist_productImage);
        cartButton = (AppCompatButton) itemView.findViewById(R.id.wishlist_cartButton);

    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
