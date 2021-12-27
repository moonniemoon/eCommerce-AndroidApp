package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView productName, productDescription;
    public ImageView productImage;
    public Button removeProduct;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);


    }

    @Override
    public void onClick(View view) {

    }
}
