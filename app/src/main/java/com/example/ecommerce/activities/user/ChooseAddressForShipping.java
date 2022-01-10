package com.example.ecommerce.activities.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.AddressViewHolder;
import com.example.ecommerce.ViewHolder.BoutiqueViewHolder;
import com.example.ecommerce.models.AddressDetail;
import com.example.ecommerce.models.Boutiques;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ChooseAddressForShipping extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private DatabaseReference AddressReference;
    private Query query;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ImageView backButton;
    private String comingFrom, totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address_for_shipping);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        comingFrom = getIntent().getStringExtra("comingFrom");


        AddressReference = FirebaseDatabase.getInstance().getReference();
        query = AddressReference.child("Address Details").child(user.getUid());

        recyclerView = findViewById(R.id.choose_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        backButton = (ImageView) findViewById(R.id.bbackbackbackbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (comingFrom.equals("bagUserFound")) {
                    startActivity(new Intent(ChooseAddressForShipping.this, ShoppingBag.class));
                    overridePendingTransition(0,0);
                }
                else if (comingFrom.equals("book")) {
                    startActivity(new Intent(ChooseAddressForShipping.this, Account.class));
                    overridePendingTransition(0,0);
                }
                else if (comingFrom.equals("account")) {
                    startActivity(new Intent(ChooseAddressForShipping.this, Account.class));
                    overridePendingTransition(0,0);
                }
                else if (comingFrom.equals("payment")) {
                    startActivity(new Intent(ChooseAddressForShipping.this, ShoppingBag.class));
                    overridePendingTransition(0,0);
                }
                else {
                    startActivity(new Intent(ChooseAddressForShipping.this, Account.class));
                    overridePendingTransition(0,0);
                }
            }
        });

        FrameLayout addAddress = (FrameLayout) findViewById(R.id.choose_frameLayout);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAddressForShipping.this, AddressBook.class);
                intent.putExtra("comingFrom", "chooseAddress");
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<AddressDetail> address = new FirebaseRecyclerOptions.Builder<AddressDetail>()
                .setQuery(query, AddressDetail.class)
                .build();

        FirebaseRecyclerAdapter<AddressDetail, AddressViewHolder> adapter = new FirebaseRecyclerAdapter<AddressDetail, AddressViewHolder>(address) {
            @Override
            protected void onBindViewHolder(@NonNull AddressViewHolder addressViewHolder, int i, @NonNull AddressDetail addressDetail) {

                addressViewHolder.addressFirst.setText(addressDetail.getFirstName());
                addressViewHolder.addressLast.setText(addressDetail.getLastName());
                addressViewHolder.addressPhone.setText(addressDetail.getPhone());
                addressViewHolder.addressCity.setText(addressDetail.getTownCity());
                addressViewHolder.addressAddress.setText(addressDetail.getAddress());
                addressViewHolder.addressCountry.setText(addressDetail.getCountry());
                addressViewHolder.addressShortcut.setText(addressDetail.getShortcutName());

                addressViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (comingFrom.equals("bagUserFound")) {
                            Intent intent = new Intent(ChooseAddressForShipping.this, PaymentMethod.class);
                            intent.putExtra("shortcutName", addressDetail.getShortcutName());
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        } else {
                            Intent intent = new Intent(ChooseAddressForShipping.this, AddressBook.class);
                            intent.putExtra("comingFrom", "recyclerView");
                            intent.putExtra("shortcutName", addressDetail.getShortcutName());
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }
                    }
                });

            }

            @NonNull
            @Override
            public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_addresses_layout, parent, false);
                AddressViewHolder holder = new AddressViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}