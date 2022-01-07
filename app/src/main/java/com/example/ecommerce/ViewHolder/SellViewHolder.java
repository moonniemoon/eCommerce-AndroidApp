package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.interfaces.ItemClickListener;

public class SellViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView sellTime, itemID, itemName, itemQuantity, itemPrice, totalIncome;
    public ItemClickListener itemClickListener;

    public SellViewHolder(@NonNull View itemView) {
        super(itemView);

        sellTime = (TextView) itemView.findViewById(R.id.sellTime);
        itemID = (TextView) itemView.findViewById(R.id.itemId);
        itemName = (TextView) itemView.findViewById(R.id.itemName);
        itemQuantity = (TextView) itemView.findViewById(R.id.itemQuantity);
        itemPrice = (TextView) itemView.findViewById(R.id.itemPrice);
        totalIncome = (TextView) itemView.findViewById(R.id.totalIncome);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }


    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
