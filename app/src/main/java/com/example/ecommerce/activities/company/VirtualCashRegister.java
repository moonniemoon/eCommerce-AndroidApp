package com.example.ecommerce.activities.company;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.RestockViewHolder;
import com.example.ecommerce.ViewHolder.SellViewHolder;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.models.RestockInfo;
import com.example.ecommerce.models.Sell;
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

public class VirtualCashRegister extends AppCompatActivity {

    private enum Tab{
        salesTab, restocksTab;
    }

    private DatabaseReference SalesReference, RestockReference, CompanyReference;
    private Query query;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private String companyName;
    private Tab tab;
    private TextView revenueTextView;
    private ImageView backButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtualcashregister);
        getSupportActionBar().hide();

        SalesReference = FirebaseDatabase.getInstance().getReference("Sells Records");
        RestockReference = FirebaseDatabase.getInstance().getReference("Restock Records");
        CompanyReference = FirebaseDatabase.getInstance().getReference("Companies");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        RadioGroup myRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        RadioButton radioSales = (RadioButton) findViewById(R.id.radio_sales);
        RadioButton radioRestocks = (RadioButton) findViewById(R.id.radio_restoks);
        revenueTextView = (TextView) findViewById(R.id.revenue);

        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VirtualCashRegister.this, CompanyAccount.class));
            }
        });


        radioSales.setChecked(true);

        CompanyReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Company companyDetails = snapshot.getValue(Company.class);

                revenueTextView.setText("$"+String.format("%.2f", companyDetails.getRevenue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        radioSales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Make the text underlined
                    SpannableString content = new SpannableString(getString((R.string.sales)));
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    buttonView.setText(content);
                } else {
                    //Change the color here and make the Text bold
                    SpannableString content = new SpannableString(getString(R.string.sales));
                    content.setSpan(null, 0, content.length(), 0);
                    buttonView.setText(content);
                    Typeface font = getResources().getFont(R.font.constan);
                    buttonView.setTypeface(font);
                }
            }
        });

        radioRestocks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Make the text underlined
                    SpannableString content = new SpannableString(getString((R.string.restocks)));
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    buttonView.setText(content);
                } else {
                    //Change the color here and make the Text bold
                    SpannableString content = new SpannableString(getString(R.string.restocks));
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
                if (checkedId == R.id.radio_restoks) {
                    tab = Tab.restocksTab;
                    onStart();
                } else if (checkedId == R.id.radio_sales){
                    tab = Tab.salesTab;
                    onStart();
                }
            }
        });

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(tab == Tab.restocksTab) {
            FirebaseDatabase.getInstance().getReference("Companies").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Company companyDetails = snapshot.getValue(Company.class);
                    companyName = companyDetails.getCompanyName();
                    query = RestockReference.child(companyName);

                    FirebaseRecyclerOptions<RestockInfo> restocks = new FirebaseRecyclerOptions.Builder<RestockInfo>()
                            .setQuery(query, RestockInfo.class)
                            .build();

                    FirebaseRecyclerAdapter<RestockInfo, RestockViewHolder> adapter = new FirebaseRecyclerAdapter<RestockInfo, RestockViewHolder>(restocks) {
                        @Override
                        protected void onBindViewHolder(@NonNull RestockViewHolder restockViewHolder, int i, @NonNull RestockInfo restockInfo) {
                            restockViewHolder.restockTime.setText(restockInfo.getRestockTime());
                            restockViewHolder.itemID.setText("Item ID: "+restockInfo.getItemID());
                            restockViewHolder.itemName.setText("Item Name: "+restockInfo.getItemName());
                            restockViewHolder.itemQuantity.setText("Quantity: "+restockInfo.getQuantity());
                            restockViewHolder.totalCost.setText("Total Cost: -"+String.format("%.2f", restockInfo.getTotalCost()));
                        }

                        @NonNull
                        @Override
                        public RestockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restock_record_layout, parent, false);
                            RestockViewHolder holder = new RestockViewHolder(view);
                            return holder;
                        }
                    };
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else{
            FirebaseDatabase.getInstance().getReference("Companies").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Company companyDetails = snapshot.getValue(Company.class);
                    companyName = companyDetails.getCompanyName();
                    query = SalesReference.child(companyName);

                    FirebaseRecyclerOptions<Sell> sells = new FirebaseRecyclerOptions.Builder<Sell>()
                            .setQuery(query, Sell.class)
                            .build();

                    FirebaseRecyclerAdapter<Sell, SellViewHolder> adapter = new FirebaseRecyclerAdapter<Sell, SellViewHolder>(sells) {
                        @Override
                        protected void onBindViewHolder(@NonNull SellViewHolder sellViewHolder, int i, @NonNull Sell sell) {
                            sellViewHolder.sellTime.setText(sell.getSellTime());
                            sellViewHolder.itemID.setText("Item ID: "+sell.getItemID());
                            sellViewHolder.itemName.setText("Item Name: "+sell.getItemName());
                            sellViewHolder.itemQuantity.setText("Quantity: "+sell.getQuantity().toString());
                            sellViewHolder.itemPrice.setText("Item Price: "+sell.getPrice().toString());
                            sellViewHolder.totalIncome.setText("Total Income: +"+String.format("%.2f", sell.getTotalIncome()));
                        }

                        @NonNull
                        @Override
                        public SellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_record_layout, parent, false);
                            SellViewHolder holder = new SellViewHolder(view);
                            return holder;
                        }
                    };
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}
