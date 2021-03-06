package com.example.ecommerce.activities.user;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.BoutiqueViewHolder;
import com.example.ecommerce.ViewHolder.ShoppingBagViewHolder;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.activities.company.CompanyAccount;
import com.example.ecommerce.models.Boutiques;
import com.example.ecommerce.models.Item;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference BoutiqueRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter adapter;
    BottomNavigationView bottomNavigationView;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private FirebaseUser user;
    private String gender;
    private EditText search_bar;

    private DatabaseReference CompanyReference;
    private Query query;

    private boolean spinnerInitialized = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        CompanyReference = FirebaseDatabase.getInstance().getReference();
        query = CompanyReference.child("Companies");


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
                        checkUserType();
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

        Button categories_Button = findViewById(R.id.button_categories);
        categories_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Search.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        search_bar =  findViewById(R.id.search_bar);
        search_bar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Intent intent = new Intent(HomeActivity.this, SearchResults.class);
                    intent.putExtra("editTextValue", search_bar.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RadioGroup myRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton radioWomen = (RadioButton) findViewById(R.id.radio_women);
        RadioButton radioMen = (RadioButton) findViewById(R.id.radio_men);

        radioWomen.setChecked(true);
        gender = "women";
        onStart();

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
                    gender = "men";
                    onStart();
                } else if (checkedId == R.id.radio_women){
                    gender = "women";
                    onStart();
                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Boutiques> items = new FirebaseRecyclerOptions.Builder<Boutiques>()
                .setQuery(query, Boutiques.class)
                .build();

        FirebaseRecyclerAdapter<Boutiques, BoutiqueViewHolder> adapter = new FirebaseRecyclerAdapter<Boutiques, BoutiqueViewHolder>(items) {
            @Override
            protected void onBindViewHolder(@NonNull BoutiqueViewHolder shoppingBagViewHolder, int i, @NonNull Boutiques item) {
                Picasso.get().load(item.getCompanyLogoURL()).into(shoppingBagViewHolder.boutique_logo);
                Picasso.get().load(item.getBackgroundURL()).into(shoppingBagViewHolder.boutique_image);

                shoppingBagViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HomeActivity.this, InsideBoutique.class);
                        intent.putExtra("companyName", item.getCompanyName());
                        intent.putExtra("gender", gender);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                });

            }

            @NonNull
            @Override
            public BoutiqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_boutiques_layout, parent, false);
                BoutiqueViewHolder holder = new BoutiqueViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkUserType() {
        reference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userType = dataSnapshot.getKey();

                if (userType == "user") {
                    startActivity(new Intent(HomeActivity.this, Account.class));
                    overridePendingTransition(0,0);
                } else if (userType == "company") {
                    startActivity(new Intent(HomeActivity.this, CompanyAccount.class));
                    overridePendingTransition(0,0);
                } else {
                    startActivity(new Intent(HomeActivity.this, UserLogIn.class));
                    overridePendingTransition(0,0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
