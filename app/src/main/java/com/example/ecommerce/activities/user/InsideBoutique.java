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
import android.provider.ContactsContract;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.BoutiqueViewHolder;
import com.example.ecommerce.ViewHolder.InsideBoutiqueViewHolder;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.activities.company.CompanyAccount;
import com.example.ecommerce.activities.company.ManageStock;
import com.example.ecommerce.activities.company.NewProductCategorySelection;
import com.example.ecommerce.adapters.SearchAdapter;
import com.example.ecommerce.models.Item;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsideBoutique extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ImageView backButton;
    private EditText search_bar;
    private Query query;

    private String companyName = "", productID, productSize, genderCategory = "";

    HashMap<String, List<Item>> allProducts;
    RecyclerView.Adapter adapter;

    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                overridePendingTransition(0,0);
            }
        });

        search_bar = findViewById(R.id.search_bar1);
        search_bar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Intent intent = new Intent(InsideBoutique.this, SearchResults.class);
                    intent.putExtra("editTextValue", search_bar.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

        companyName = getIntent().getStringExtra("companyName");
        genderCategory = getIntent().getStringExtra("gender");

        allProducts = new HashMap<>();

        recyclerView = findViewById(R.id.grid_menu);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);



        FirebaseDatabase.getInstance().getReference("Products").child(companyName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        Item item = s.getValue(Item.class);

                        if (genderCategory != null) {
                            if (allProducts.get("women") == null) {
                                allProducts.put("women", new ArrayList<Item>());
                            }
                            if (allProducts.get("men") == null) {
                                allProducts.put("men", new ArrayList<Item>());
                            }


                            if (item.getGender().equals("Woman") && item.getDuplicateItems().equals(false)) {
                                allProducts.get("women").add(item);
                            } else if (item.getGender().equals("Man") && item.getDuplicateItems().equals(false)) {
                                allProducts.get("men").add(item);
                            }


                            switch (genderCategory) {
                                case "women":
                                    adapter = new SearchAdapter(allProducts.get("women"), InsideBoutique.this, "inside", "women", companyName);
                                    recyclerView.setAdapter(adapter);
                                    break;
                                case "men":
                                    adapter = new SearchAdapter(allProducts.get("men"), InsideBoutique.this, "inside", "men", companyName);
                                    recyclerView.setAdapter(adapter);
                                    break;
                            }

                        } else {
                            Toast.makeText(InsideBoutique.this, "Empty", Toast.LENGTH_LONG).show();
                        }
                    }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}

