package com.example.ecommerce.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.InsideBoutiqueViewHolder;
import com.example.ecommerce.ViewHolder.ShippingViewHolder;
import com.example.ecommerce.activities.company.AddClothingItem;
import com.example.ecommerce.activities.company.NewProductCategorySelection;
import com.example.ecommerce.activities.company.ShipmentDetails;
import com.example.ecommerce.activities.user.OrderDetails;
import com.example.ecommerce.activities.user.PaymentMethod;
import com.example.ecommerce.activities.user.ProductDetails;
import com.example.ecommerce.activities.user.Wishlist;
import com.example.ecommerce.models.Item;
import com.example.ecommerce.models.OrderedProduct;
import com.example.ecommerce.models.Shipments;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShipmentAdapter extends RecyclerView.Adapter<ShippingViewHolder> {

    List<Shipments> recyclerList;
    Context context;
    String userID, orderDate, companyName;
    String everythingIsShipped = "";


    public ShipmentAdapter(List<Shipments> recyclerList, Context context, String userID, String orderDate, String companyName) {
        this.recyclerList = recyclerList;
        this.context = context;
        this.userID = userID;
        this.orderDate = orderDate;
        this.companyName = companyName;
    }

    @NonNull
    @Override
    public ShippingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shippments_orders_layout, parent, false);
        ShippingViewHolder holder = new ShippingViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShippingViewHolder holder, int position) {
        Shipments ship = recyclerList.get(position);
        holder.name.setText(ship.getName());
        holder.phone.setText(ship.getPhone());
        holder.totalAmount.setText("Total Amount: $" + ship.getTotalAmount().toString());
        holder.address.setText("Address: " + ship.getAddress());
        holder.time.setText("Ordered date: " + ship.getDate());

        holder.detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShipmentDetails.class);
                intent.putExtra("userID", ship.getUserUID());
                intent.putExtra("orderDate", ship.getDate());
                intent.putExtra("companyName", companyName);
                context.startActivity(intent);
            }
        });

        checkIfEverythingShipped();

        holder.shipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // +++++++++ change in "ordered products", the specific products state = "shipping"
                // if all the products in the "ordered products" are shipped, change state of the order to "shipped" and delete it from the "shipments"
                // create another table named "shipping history" and add the shipping to it.
                changeOrderedProductsState(
                        ship.getAddress(),
                        ship.getCity(),
                        ship.getDate(),
                        ship.getName(),
                        ship.getPhone(),
                        ship.getUserUID(),
                        ship.getPaymentMethod(),
                        ship.getCountry(),
                        ship.getTotalAmount(),
                        ship.getSellers()
                );
            }
        });

    }

    private void checkIfEverythingShipped() {
        everythingIsShipped = "";
        FirebaseDatabase.getInstance().getReference().child("Ordered Products").child(userID).child("Current Orders").child(orderDate)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            OrderedProduct orderDetails = snap.getValue(OrderedProduct.class);
                            if (orderDetails.getSeller().equals(companyName)) {
                                everythingIsShipped += " shipping";
                            } else {
                                String checkif = " " + orderDetails.getStatus();
                                everythingIsShipped += checkif;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    private void changeOrderedProductsState(String address, String city, String currentDateandTime, String personName, String phone, String normalUserUID, String paymentMethod, String country, Double dlTotal, String sellers) {

        String status = "shipping";

        FirebaseDatabase.getInstance().getReference().child("Ordered Products").child(userID).child("Current Orders").child(orderDate)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        OrderedProduct orderDetails = snap.getValue(OrderedProduct.class);
                        if (orderDetails.getSeller().equals(companyName)) {
                            FirebaseDatabase.getInstance().getReference().child("Ordered Products").child(userID)
                                    .child("Current Orders").child(orderDate).child(snap.getKey()).child("status").setValue(status);
                        }


                        FirebaseDatabase.getInstance().getReference().child("Shipments").child(orderDate).child("sellers").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String oldSellers = snapshot.getValue().toString();
                                if (oldSellers.contains(companyName)) {
                                    oldSellers = oldSellers.replace(companyName, "");
                                }
                                FirebaseDatabase.getInstance().getReference().child("Shipments").child(orderDate).child("sellers").setValue(oldSellers);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        if (!everythingIsShipped.contains("not delivered")) {

                            Shipments shipments = new Shipments(address, city, currentDateandTime, personName, phone, normalUserUID, status, paymentMethod, country, dlTotal, sellers);

                            FirebaseDatabase.getInstance().getReference("Shipping History").child(currentDateandTime).setValue(shipments).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference().child("Shipments").child(orderDate).removeValue();
                                    } else {
                                        String message = task.getException().toString();
                                    }
                                }
                            });

                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @Override
    public int getItemCount() {
        return recyclerList.size();
    }
}
