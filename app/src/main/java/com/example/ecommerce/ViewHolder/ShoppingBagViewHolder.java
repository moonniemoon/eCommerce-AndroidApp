package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.activities.company.AddClothingItem;
import com.example.ecommerce.activities.user.ShoppingBag;
import com.example.ecommerce.interfaces.ItemClickListener;

public class ShoppingBagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView productId, productName, productDescription, productSize, productPrice, productCategory;
    public Spinner quantitySpinner;
    public ImageView productImage;
    public Button removeProductButton;
    public ItemClickListener itemClickListener;

    public ShoppingBagViewHolder(@NonNull View itemView) {
        super(itemView);

        productId = (TextView) itemView.findViewById(R.id.itemId);
        productName = (TextView) itemView.findViewById(R.id.itemName);
        productDescription = (TextView) itemView.findViewById(R.id.itemDescription);
        productSize = (TextView) itemView.findViewById(R.id.itemSize);
        productCategory = (TextView) itemView.findViewById(R.id.itemCategory);
        productPrice = (TextView) itemView.findViewById(R.id.itemPrice);
        quantitySpinner = (Spinner) itemView.findViewById(R.id.itemQuantitySpinner);


        productImage = (ImageView) itemView.findViewById(R.id.itemImage);
        removeProductButton = (Button) itemView.findViewById(R.id.removeItem);
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }


    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false );
    }
}
