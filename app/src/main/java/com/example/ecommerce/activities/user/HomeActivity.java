package com.example.ecommerce.activities.user;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.BoutiqueViewHolder;
import com.example.ecommerce.accounts.Company;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference BoutiqueRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter adapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home_Screen);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home_Screen:
                        return true;
                    case R.id.search:
                        startActivity(new Intent(HomeActivity.this, Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.wishlist:
                        startActivity(new Intent(HomeActivity.this, Wishlist.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.shopping_Bag:
                        startActivity(new Intent(HomeActivity.this, ShoppingBag.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.account:
                        startActivity(new Intent(HomeActivity.this, Account.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });


        BoutiqueRef = FirebaseDatabase.getInstance().getReference().child("Companies");


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RadioGroup myRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton radioWomen = (RadioButton) findViewById(R.id.radio_women);
        RadioButton radioMen = (RadioButton) findViewById(R.id.radio_men);

        radioWomen.setChecked(true);

        radioWomen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Make the text underlined
                    SpannableString content = new SpannableString(getString((R.string.women_text)));
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    buttonView.setText(content);
                } else {
                    //Change the color here and make the Text bold
                    SpannableString content = new SpannableString(getString(R.string.women_text));
                    content.setSpan(null, 0, content.length(), 0);
                    buttonView.setText(content);
                    Typeface font = getResources().getFont(R.font.constan);
                    buttonView.setTypeface(font);
                }
            }
        });

        radioMen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Make the text underlined
                    SpannableString content = new SpannableString(getString((R.string.men_text)));
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    buttonView.setText(content);
                } else {
                    //Change the color here and make the Text bold
                    SpannableString content = new SpannableString(getString(R.string.men_text));
                    content.setSpan(null, 0, content.length(), 0);
                    buttonView.setText(content);
                    Typeface font = getResources().getFont(R.font.constan);
                    buttonView.setTypeface(font);
                }
            }
        });

        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_men) {
                    // Send info to the new fragment/activity, that in this class, MEN was checked
                    // which means, only men's products must be displayed.
                } else if (checkedId == R.id.radio_women){

                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Company> options =
                new FirebaseRecyclerOptions.Builder<Company>()
                        .setQuery(BoutiqueRef, Company.class)
                        .build();


        FirebaseRecyclerAdapter<Company, BoutiqueViewHolder> adapter =
                new FirebaseRecyclerAdapter<Company, BoutiqueViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull BoutiqueViewHolder holder, int position, @NonNull Company model)
                    {
                        Picasso.get().load(model.getBackgroundURL()).into(holder.boutique_image);
                        Picasso.get().load(model.getCompanyLogoURL()).into(holder.boutique_logo);
                    }

                    @NonNull
                    @Override
                    public BoutiqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_boutiques_layout, parent, false);
                        BoutiqueViewHolder holder = new BoutiqueViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
