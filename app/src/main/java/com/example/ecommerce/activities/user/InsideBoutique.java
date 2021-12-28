package com.example.ecommerce.activities.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.BoutiqueViewHolder;
import com.example.ecommerce.ViewHolder.InsideBoutiqueViewHolder;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.activities.company.CompanyAccount;
import com.example.ecommerce.activities.company.ManageStock;
import com.example.ecommerce.activities.company.NewProductCategorySelection;
import com.example.ecommerce.models.Item;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class InsideBoutique extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ImageView backButton;

    private DatabaseReference ItemReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_boutique);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_inside_boutique);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home_Screen:
                        startActivity(new Intent(InsideBoutique.this, HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.search:
                        startActivity(new Intent(InsideBoutique.this, Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.wishlist:
                        startActivity(new Intent(InsideBoutique.this, Wishlist.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.shopping_Bag:
                        startActivity(new Intent(InsideBoutique.this, ShoppingBag.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(InsideBoutique.this, Account.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        backButton = (ImageView) findViewById(R.id.backkkButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InsideBoutique.this, HomeActivity.class));
            }
        });

        ItemReference = FirebaseDatabase.getInstance().getReference().child("Products").child("6470168700");

        recyclerView = findViewById(R.id.grid_menu);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Item> items = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(ItemReference, Item.class)
                .build();

        FirebaseRecyclerAdapter<Item, InsideBoutiqueViewHolder> adapter = new FirebaseRecyclerAdapter<Item, InsideBoutiqueViewHolder>(items) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(@NonNull InsideBoutiqueViewHolder insideBoutiqueViewHolder, int i, @NonNull Item item) {

                insideBoutiqueViewHolder.productName.setText(item.getSeller() + " " + item.getName());
                insideBoutiqueViewHolder.productPrice.setText(item.getPrice().toString());
                Picasso.get().load(item.getImageUrl()).into(insideBoutiqueViewHolder.productImage);
            }

            @NonNull
            @Override
            public InsideBoutiqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_products_layout, parent, false);
                InsideBoutiqueViewHolder holder = new InsideBoutiqueViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}