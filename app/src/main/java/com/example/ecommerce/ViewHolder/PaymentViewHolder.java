package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.interfaces.ItemClickListener;

public class PaymentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productId, productName, productPrice, productCategory, productCompany, productQuantity;
    public ImageView productImage;

    public ItemClickListener listener;

    public PaymentViewHolder(@NonNull View itemView) {
        super(itemView);

        productCompany = (TextView) itemView.findViewById(R.id.payment_productCompanyName);
        productId = (TextView) itemView.findViewById(R.id.payment_productID);
        productName = (TextView) itemView.findViewById(R.id.payment_productName);
        productCategory = (TextView) itemView.findViewById(R.id.payment_productCategory);
        productPrice = (TextView) itemView.findViewById(R.id.payment_price);
        productImage = (ImageView) itemView.findViewById(R.id.payment_productImage);
        productQuantity = (TextView) itemView.findViewById(R.id.payment_productQuantity);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}