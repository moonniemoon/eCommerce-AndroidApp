package com.example.ecommerce.activities.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.ecommerce.R;
import com.example.ecommerce.adapters.SearchAdapter;
import com.example.ecommerce.models.Item;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchResults extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    RecyclerView recyclerView;
    HashMap<String, List<Item>> allProducts;
    List<Item> allItems;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    String fromCardView, searchInput;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_search_results);

        fromCardView = getIntent().getStringExtra("searchResult");
        searchInput = getIntent().getStringExtra("editTextValue");

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.search);

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
                        startActivity(new Intent(getApplicationContext(), Wishlist.class));
                        overridePendingTransition(0,0);
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

        backButton = (ImageView) findViewById(R.id.backkkButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchResults.this, Search.class));
            }
        });

        allProducts = new HashMap<>();
        allItems = new ArrayList<>();

        recyclerView = findViewById(R.id.searchResults_recycler_menu);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        FirebaseDatabase.getInstance().getReference("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    for (DataSnapshot s : snap.getChildren()) {
                        Item item = s.getValue(Item.class);

                        if (searchInput != null) {
                            if (allItems == null) {
                                allItems = new ArrayList<>();
                            }

                            String searchTotal = item.getSeller() + " " + item.getName() + " " + item.getDescription() + " " + item.getCategory() + " " + item.getColour();
                            String searchLowerCase = item.getSeller().toLowerCase() + " " +
                                    item.getName().toLowerCase() + " " + item.getDescription().toLowerCase() + " " +
                                    item.getCategory().toLowerCase() + " " + item.getColour().toLowerCase();

                            if ((searchTotal.contains(searchInput) || searchLowerCase.contains(searchInput)) && item.getDuplicateItems().equals(false)) {
                                allItems.add(item);
                            }

                            adapter = new SearchAdapter(allItems, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        }

                        else {
                            if (allProducts.get("Shirts") == null) {
                                allProducts.put("Shirts", new ArrayList<Item>());
                            }
                            if (allProducts.get("Tees") == null) {
                                allProducts.put("Tees", new ArrayList<Item>());
                            }
                            if (allProducts.get("Trousers") == null) {
                                allProducts.put("Trousers", new ArrayList<Item>());
                            }
                            if (allProducts.get("Skirts") == null) {
                                allProducts.put("Skirts", new ArrayList<Item>());
                            }
                            if (allProducts.get("Accessories") == null) {
                                allProducts.put("Accessories", new ArrayList<Item>());
                            }
                            if (allProducts.get("Beauty") == null) {
                                allProducts.put("Beauty", new ArrayList<Item>());
                            }


                            if (item.getCategory().equals("Shirts") && item.getDuplicateItems().equals(false)) {
                                allProducts.get("Shirts").add(item);
                            } else if (item.getCategory().equals("Tees") && item.getDuplicateItems().equals(false)) {
                                allProducts.get("Tees").add(item);
                            } else if (item.getCategory().equals("Trousers") && item.getDuplicateItems().equals(false)) {
                                allProducts.get("Trousers").add(item);
                            } else if (item.getCategory().equals("Skirts") && item.getDuplicateItems().equals(false)) {
                                allProducts.get("Skirts").add(item);
                            } else if (item.getCategory().equals("Accessories") && item.getDuplicateItems().equals(false)) {
                                allProducts.get("Accessories").add(item);
                            } else if (item.getCategory().equals("Beauty") && item.getDuplicateItems().equals(false)) {
                                allProducts.get("Beauty").add(item);
                            }


                            switch (fromCardView) {
                                case "shirts":
                                    openAdapter("Shirts");
                                    break;
                                case "tshirts":
                                    openAdapter("Tees");
                                    break;
                                case "trousers":
                                    openAdapter("Trousers");
                                    break;
                                case "skirts":
                                    openAdapter("Skirts");
                                    break;
                                case "accessories":
                                    openAdapter("Accessories");
                                    break;
                                case "beauty":
                                    openAdapter("Beauty");
                                    break;
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void openAdapter(String key) {
        adapter = new SearchAdapter(allProducts.get(key), getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}