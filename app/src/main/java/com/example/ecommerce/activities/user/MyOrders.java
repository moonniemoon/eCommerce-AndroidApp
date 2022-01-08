package com.example.ecommerce.activities.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.AddressViewHolder;
import com.example.ecommerce.ViewHolder.MyOrdersViewHolder;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.models.AddressDetail;
import com.example.ecommerce.models.Item;
import com.example.ecommerce.models.OrderedProduct;
import com.example.ecommerce.models.Shipments;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyOrders extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private DatabaseReference OrderReference;
    private Query query;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_my_orders);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        OrderReference = FirebaseDatabase.getInstance().getReference().child("Ordered Products").child(user.getUid()).child("Current Orders");

        recyclerView = findViewById(R.id.myorders_recycler_menu);
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

        query = FirebaseDatabase.getInstance().getReference().child("Shipments").orderByChild("userUID").equalTo(user.getUid());

        FirebaseRecyclerOptions<Shipments> ordered = new FirebaseRecyclerOptions.Builder<Shipments>()
                .setQuery(query, Shipments.class)
                .build();

        FirebaseRecyclerAdapter<Shipments, MyOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<Shipments, MyOrdersViewHolder>(ordered) {
            @Override
            protected void onBindViewHolder(@NonNull MyOrdersViewHolder myOrdersViewHolder, int i, @NonNull Shipments orderedProduct1) {
                myOrdersViewHolder.date.setText(orderedProduct1.getDate());

                myOrdersViewHolder.detailsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyOrders.this, OrderDetails.class);
                        intent.putExtra("purchaseDate", orderedProduct1.getDate());
                        startActivity(intent);
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