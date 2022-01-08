package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.interfaces.ItemClickListener;

public class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView addressShortcut, addressFirst, addressLast, addressPhone, addressCity, addressCountry, addressAddress;
    public ItemClickListener listener;

    public AddressViewHolder(@NonNull View itemView) {
        super(itemView);

        addressShortcut = (TextView) itemView.findViewById(R.id.saved_shortcutName);
        addressFirst = (TextView) itemView.findViewById(R.id.saved_firstName);
        addressLast = (TextView) itemView.findViewById(R.id.saved_lastName);
        addressAddress = (TextView) itemView.findViewById(R.id.saved_address);
        addressPhone = (TextView) itemView.findViewById(R.id.saved_phone);
        addressCity = (TextView) itemView.findViewById(R.id.saved_town);
        addressCountry = (TextView) itemView.findViewById(R.id.saved_country);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
