package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.interfaces.ItemClickListener;

public class OrderDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView status, name, description, size, price, quantity, seller;
    public ImageView image;
    public ItemClickListener listener;

    public OrderDetailsViewHolder(@NonNull View itemView) {
        super(itemView);

        status = (TextView) itemView.findViewById(R.id.orders_status);
        name = (TextView) itemView.findViewById(R.id.orders_productName);
        description = (TextView) itemView.findViewById(R.id.orders_description);
        size = (TextView) itemView.findViewById(R.id.orders_size);
        price = (TextView) itemView.findViewById(R.id.orders_price);
        quantity = (TextView) itemView.findViewById(R.id.orders_quantity);
        seller = (TextView) itemView.findViewById(R.id.orders_seller);
        image = (ImageView) itemView.findViewById(R.id.orders_productImage);

    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
