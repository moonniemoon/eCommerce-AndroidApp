package com.example.ecommerce.activities.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.MyOrdersViewHolder;
import com.example.ecommerce.ViewHolder.OrderDetailsViewHolder;
import com.example.ecommerce.models.OrderedProduct;
import com.example.ecommerce.models.Shipments;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class OrderDetails extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private DatabaseReference OrderReference;
    private Query query;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private String purchasedDate = "";
    private TextView statusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_order_details);

        purchasedDate = getIntent().getStringExtra("purchaseDate");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //OrderReference = FirebaseDatabase.getInstance().getReference().child("Ordered Products").child(user.getUid()).child("Current Orders").child("2022 01 04 at 15:06:29 GMT +02:00");
        //query = OrderReference.child("2022 01 04 at 15:06:29 GMT +02:00");

        recyclerView = findViewById(R.id.orderdetails_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
/*
        FirebaseDatabase.getInstance().getReference("Shipments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Shipments ship = snapshot.getValue(Shipments.class);*/

        final DatabaseReference  productsRef = FirebaseDatabase.getInstance().getReference().child("Ordered Products").child(user.getUid()).child("Current Orders").child(purchasedDate);
        query = productsRef.orderByChild("date").equalTo(purchasedDate);

        FirebaseRecyclerOptions<OrderedProduct> ordered = new FirebaseRecyclerOptions.Builder<OrderedProduct>()
                .setQuery(productsRef, OrderedProduct.class)
                .build();

        FirebaseRecyclerAdapter<OrderedProduct, OrderDetailsViewHolder> adapter = new FirebaseRecyclerAdapter<OrderedProduct, OrderDetailsViewHolder>(ordered) {
            @Override
            protected void onBindViewHolder(@NonNull OrderDetailsViewHolder myOrdersViewHolder, int i, @NonNull OrderedProduct orderedProduct1) {
                String status = orderedProduct1.getStatus();
                if (status.equals("not delivered")) {
                    myOrdersViewHolder.status.setTextColor(Color.parseColor("#e60000"));
                    status = "Your order is being processed.";
                } else if (status.equals("shipping")) {
                    myOrdersViewHolder.status.setTextColor(Color.parseColor("#ffbf00"));
                    status = "Your order has been shipped.";
                } else {
                    myOrdersViewHolder.status.setTextColor(Color.parseColor("#00cc00"));
                    status = "Your order has been delivered.";
                }

                myOrdersViewHolder.status.setText(status);
                myOrdersViewHolder.name.setText(orderedProduct1.getName());
                myOrdersViewHolder.price.setText("Price: $" + orderedProduct1.getPrice().toString());
                myOrdersViewHolder.quantity.setText("Quantity: " + orderedProduct1.getQuantity().toString());
                myOrdersViewHolder.seller.setText("Seller: " + orderedProduct1.getSeller());
                Picasso.get().load(orderedProduct1.getImage()).into(myOrdersViewHolder.image);

                String shortSize = orderedProduct1.getSize();
                if (shortSize.contains("-")) {
                    shortSize = shortSize.substring(shortSize.indexOf("-"), shortSize.length());
                }

                myOrdersViewHolder.size.setText("Size: " + shortSize);

            }

            @NonNull
            @Override
            public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetails_orders_layout, parent, false);
                OrderDetailsViewHolder holder = new OrderDetailsViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
   /*             }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/

       /* FirebaseDatabase.getInstance().getReference("Ordered Products").child(user.getUid()).child("Current Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {


                   *//*     OrderedProduct orderedProduct = d.getValue(OrderedProduct.class);
                        String orderDate = orderedProduct.getDate();*//*
                    query = OrderReference.child(data.getKey());


                    FirebaseRecyclerOptions<OrderedProduct> ordered = new FirebaseRecyclerOptions.Builder<OrderedProduct>()
                            .setQuery(query, OrderedProduct.class)
                            .build();

                    FirebaseRecyclerAdapter<OrderedProduct, MyOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<OrderedProduct, MyOrdersViewHolder>(ordered) {
                        @Override
                        protected void onBindViewHolder(@NonNull MyOrdersViewHolder myOrdersViewHolder, int i, @NonNull OrderedProduct orderedProduct1) {
                            myOrdersViewHolder.date.setText(orderedProduct1.getDate());

                            FirebaseDatabase.getInstance().getReference("Shipments").child(orderedProduct1.getDate()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Shipments ship = snapshot.getValue(Shipments.class);

                                        myOrdersViewHolder.total.setText("$" + ship.getTotalAmount().toString());
                                        myOrdersViewHolder.status.setText(ship.getStatus());

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                        @NonNull
                        @Override
                        public MyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorders_order_layout, parent, false);
                            MyOrdersViewHolder holder = new MyOrdersViewHolder(view);
                            return holder;
                        }
                    };
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });*/

    }
}