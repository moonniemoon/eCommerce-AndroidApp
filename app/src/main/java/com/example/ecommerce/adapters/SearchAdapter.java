package com.example.ecommerce.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.InsideBoutiqueViewHolder;
import com.example.ecommerce.activities.user.InsideBoutique;
import com.example.ecommerce.activities.user.ProductDetails;
import com.example.ecommerce.models.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<InsideBoutiqueViewHolder> {

    List<Item> recyclerList;
    Context context;

    public SearchAdapter(List<Item> recyclerList, Context context) {
        this.recyclerList = recyclerList;
        this.context = context;
    }

    @NonNull
    @Override
    public InsideBoutiqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_products_layout, parent, false);
        InsideBoutiqueViewHolder holder = new InsideBoutiqueViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull InsideBoutiqueViewHolder holder, int position) {
        Item item = recyclerList.get(position);
        holder.productPrice.setText(item.getPrice().toString());
        holder.productName.setText(item.getSeller() + " " + item.getName());
        Picasso.get().load(item.getImageUrl()).into(holder.productImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("page", "inside");
                intent.putExtra("companyName", item.getSeller());
                intent.putExtra("ID", item.getID());
                intent.putExtra("category", item.getCategory());
                intent.putExtra("colour", item.getColour());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("imageURL", item.getImageUrl());
                intent.putExtra("gender", item.getGender());
                intent.putExtra("price", item.getPrice().toString());
                intent.putExtra("name", item.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recyclerList.size();
    }
}
