package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.interfaces.ItemClickListener;
import com.google.android.material.imageview.ShapeableImageView;

public class ShippingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView name, phone, totalAmount, address, time;
    public AppCompatButton shipButton, detailsButton;
    public ItemClickListener listener;

    public ShippingViewHolder(@NonNull View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.shipping_name);
        phone = (TextView) itemView.findViewById(R.id.shipping_phone);
        totalAmount = (TextView) itemView.findViewById(R.id.shipping_totalamount);
        address = (TextView) itemView.findViewById(R.id.shipping_address);
        time = (TextView) itemView.findViewById(R.id.shipping_time);
        shipButton = (AppCompatButton) itemView.findViewById(R.id.shipping_shipProductsButton);
        detailsButton = (AppCompatButton) itemView.findViewById(R.id.shipping_showProductsButton);

    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}