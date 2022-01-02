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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.BoutiqueViewHolder;
import com.example.ecommerce.ViewHolder.WishlistViewHolder;
import com.example.ecommerce.models.Boutiques;
import com.example.ecommerce.models.Item;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class Wishlist extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private DatabaseReference BoutiqueRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter adapter;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private FirebaseUser user;

    private DatabaseReference WishlistReference, databaseReference;
    private Query query;

    private boolean spinnerInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_wishlist);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        WishlistReference = FirebaseDatabase.getInstance().getReference();
        query = WishlistReference.child("Wishlist").child(user.getUid());
        databaseReference = WishlistReference.child("Wishlist").child(user.getUid());


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.wishlist);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home_Screen:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.wishlist:
                        return true;
                    case R.id.shopping_Bag:
                        startActivity(new Intent(getApplicationContext(), ShoppingBag.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(), Account.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

        recyclerView = findViewById(R.id.wishlist_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<com.example.ecommerce.models.Wishlist> products =
                new FirebaseRecyclerOptions.Builder<com.example.ecommerce.models.Wishlist>()
                .setQuery(query, com.example.ecommerce.models.Wishlist.class)
                .build();

        FirebaseRecyclerAdapter<com.example.ecommerce.models.Wishlist, WishlistViewHolder> adapter = new FirebaseRecyclerAdapter<com.example.ecommerce.models.Wishlist, WishlistViewHolder>(products) {
            @Override
            protected void onBindViewHolder(@NonNull WishlistViewHolder wishlistViewHolder, int i, @NonNull com.example.ecommerce.models.Wishlist wishlist) {

                wishlistViewHolder.productId.setText(wishlist.getId());
                wishlistViewHolder.productName.setText(wishlist.getname());
                wishlistViewHolder.productCompany.setText(wishlist.getSeller());
                wishlistViewHolder.productCategory.setText(wishlist.getCategory());
                wishlistViewHolder.productPrice.setText(wishlist.getprice().toString());
                Picasso.get().load(wishlist.getImageUrl()).into(wishlistViewHolder.productImage);

                wishlistViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Wishlist.this, ProductDetails.class);
                        intent.putExtra("page", "wishlist");
                        intent.putExtra("companyName", wishlist.getSeller());
                        intent.putExtra("ID", wishlist.getId());
                        intent.putExtra("category", wishlist.getCategory());
                        intent.putExtra("colour", wishlist.getColour());
                        intent.putExtra("description", wishlist.getdescription());
                        intent.putExtra("imageURL", wishlist.getImageUrl());
                        intent.putExtra("gender", wishlist.getGender());
                        intent.putExtra("price", wishlist.getprice().toString());
                        intent.putExtra("name", wishlist.getname());
                        startActivity(intent);
                    }
                });


                wishlistViewHolder.cartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Item item = new Item(
                                wishlist.getId(),
                                wishlist.getname(),
                                wishlist.getdescription(),
                                wishlist.getGender(),
                                wishlist.getsize(),
                                wishlist.getColour(),
                                wishlist.getquantity(),
                                wishlist.getCategory(),
                                wishlist.getSeller(),
                                wishlist.getImageUrl(),
                                wishlist.getprice());

                        FirebaseDatabase.getInstance().getReference("Shopping Bags").child(user.getUid()).child("Items").child(wishlist.getId()).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    removeItem(wishlist.getId());
                                } else {
                                    String message = task.getException().toString();
                                    Toast.makeText(Wishlist.this, message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

            }

            @NonNull
            @Override
            public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_product_layout, parent, false);
                WishlistViewHolder holder = new WishlistViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeItem(String id) {
        databaseReference.child(id).removeValue();
        Toast.makeText(Wishlist.this, "Added to cart!", Toast.LENGTH_LONG).show();
    }

}