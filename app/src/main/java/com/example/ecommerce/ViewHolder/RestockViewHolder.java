package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.interfaces.ItemClickListener;

public class RestockViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView restockTime, itemID, itemName, itemQuantity, totalCost;
    public ItemClickListener itemClickListener;

    public RestockViewHolder(@NonNull View itemView) {
        super(itemView);

        restockTime = (TextView) itemView.findViewById(R.id.restockTime);
        itemID = (TextView) itemView.findViewById(R.id.itemId);
        itemName = (TextView) itemView.findViewById(R.id.itemName);
        itemQuantity = (TextView) itemView.findViewById(R.id.itemQuantity);
        totalCost = (TextView) itemView.findViewById(R.id.totalCost);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }


    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
