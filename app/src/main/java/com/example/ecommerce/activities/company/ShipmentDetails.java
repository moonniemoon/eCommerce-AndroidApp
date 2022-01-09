package com.example.ecommerce.activities.company;

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
import android.widget.ImageView;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.MyOrdersViewHolder;
import com.example.ecommerce.ViewHolder.OrderDetailsViewHolder;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.activities.user.MyOrders;
import com.example.ecommerce.activities.user.OrderDetails;
import com.example.ecommerce.models.OrderedProduct;
import com.example.ecommerce.models.Shipments;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShipmentDetails extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference reference, OrderReference;
    private String companyID, companyN = "", userUID, orderDate, companyName;
    private DatabaseReference CompanyReference;
    private String parentDatabaseName = "Companies";
    private Query query;

    RecyclerView recyclerView;
    List<Shipments> allItems;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_shipment_details);

        CompanyReference = FirebaseDatabase.getInstance().getReference("Companies");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(parentDatabaseName);
        companyID = user.getUid();

        userUID = getIntent().getStringExtra("userID");
        orderDate = getIntent().getStringExtra("orderDate");
        companyName = getIntent().getStringExtra("companyName");


        allItems = new ArrayList<>();


        recyclerView = findViewById(R.id.shipmentdetails_recycler_menu);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        final DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().child("Ordered Products").child(userUID).child("Current Orders").child(orderDate);

        query = orderReference.orderByChild("seller").equalTo(companyName);

        FirebaseRecyclerOptions<OrderedProduct> ordered = new FirebaseRecyclerOptions.Builder<OrderedProduct>()
                .setQuery(query, OrderedProduct.class)
                .build();

        FirebaseRecyclerAdapter<OrderedProduct, OrderDetailsViewHolder> adapter = new FirebaseRecyclerAdapter<OrderedProduct, OrderDetailsViewHolder>(ordered) {
            @Override
            protected void onBindViewHolder(@NonNull OrderDetailsViewHolder myOrdersViewHolder, int i, @NonNull OrderedProduct orderedProduct1) {
                String status = orderedProduct1.getStatus();
                if (status.equals("not delivered")) {
                    myOrdersViewHolder.status.setTextColor(Color.parseColor("#e60000"));
                    status = "Waiting to be shipped";
                } else if (status.equals("shipping")) {
                    myOrdersViewHolder.status.setTextColor(Color.parseColor("#ffbf00"));
                    status = "Order is shipped.";
                } else {
                    myOrdersViewHolder.status.setTextColor(Color.parseColor("#00cc00"));
                    status = "Order is delivered.";
                }

                myOrdersViewHolder.status.setText(status);
                myOrdersViewHolder.name.setText(orderedProduct1.getName());
                myOrdersViewHolder.price.setText("$" + orderedProduct1.getPrice().toString());
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

    }


}