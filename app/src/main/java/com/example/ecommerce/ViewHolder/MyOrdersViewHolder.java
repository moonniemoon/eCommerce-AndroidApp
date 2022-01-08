package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.interfaces.ItemClickListener;

public class MyOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView quantity, total, date, status;
    public AppCompatButton detailsButton;
    public ItemClickListener listener;

    public MyOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        quantity = (TextView) itemView.findViewById(R.id.myorders_quantity);
        total = (TextView) itemView.findViewById(R.id.myorders_Total);
        date = (TextView) itemView.findViewById(R.id.myorders_date);
        status = (TextView) itemView.findViewById(R.id.myorders_status);
        detailsButton = (AppCompatButton) itemView.findViewById(R.id.myorders_detailsButton);

    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
