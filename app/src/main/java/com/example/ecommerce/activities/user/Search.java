package com.example.ecommerce.activities.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.ecommerce.R;
import com.example.ecommerce.fragments.user.AccountDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Search extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private Button searchButton;
    private EditText inputText;
    private String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_search);

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

        inputText = findViewById(R.id.search_bar);

        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, SearchResults.class);
                intent.putExtra("editTextValue", inputText.getText().toString());
                startActivity(intent);
            }
        });

        CardView searchOuterwear = (CardView) findViewById(R.id.search_cardview_outerwear);
        searchOuterwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, SearchResults.class);
                intent.putExtra("searchResult", "outerwear");
                startActivity(intent);
            }
        });

        CardView searchAccesories = (CardView) findViewById(R.id.search_cardview_accessories);
        searchAccesories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, SearchResults.class);
                intent.putExtra("searchResult", "accessories");
                startActivity(intent);
            }
        });

        CardView searchFootwear = (CardView) findViewById(R.id.search_cardview_footwear);
        searchFootwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, SearchResults.class);
                intent.putExtra("searchResult", "footwear");
                startActivity(intent);
            }
        });

        CardView searchShirts = (CardView) findViewById(R.id.search_cardview_shirts);
        searchShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, SearchResults.class);
                intent.putExtra("searchResult", "shirts");
                startActivity(intent);
            }
        });

        CardView searchTrousers = (CardView) findViewById(R.id.search_cardview_trousers);
        searchTrousers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, SearchResults.class);
                intent.putExtra("searchResult", "trousers");
                startActivity(intent);
            }
        });

        CardView searchBeauty = (CardView) findViewById(R.id.search_cardview_beauty);
        searchBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, SearchResults.class);
                intent.putExtra("searchResult", "beauty");
                startActivity(intent);
            }
        });

        CardView searchSkirts = (CardView) findViewById(R.id.search_cardview_skirts);
        searchSkirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, SearchResults.class);
                intent.putExtra("searchResult", "skirts");
                startActivity(intent);
            }
        });

        CardView searchTshirts= (CardView) findViewById(R.id.search_cardview_tshirts);
        searchTshirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search.this, SearchResults.class);
                intent.putExtra("searchResult", "tshirts");
                startActivity(intent);
            }
        });
    }
}