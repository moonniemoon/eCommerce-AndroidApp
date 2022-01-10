package com.example.ecommerce.activities.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.AddressViewHolder;
import com.example.ecommerce.ViewHolder.MyOrdersViewHolder;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.activities.company.VirtualCashRegister;
import com.example.ecommerce.models.AddressDetail;
import com.example.ecommerce.models.Item;
import com.example.ecommerce.models.OrderedProduct;
import com.example.ecommerce.models.Shipments;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyOrders extends AppCompatActivity {

    private enum Tab {
        activesTab, shippedTab;
    }

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private DatabaseReference OrderReference;
    private Query query;

    private RecyclerView recyclerView, recyclerView2;
    RecyclerView.LayoutManager layoutManager, layoutManager2;
    private MyOrders.Tab tab;
    List<OrderDetails> allItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_my_orders);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        OrderReference = FirebaseDatabase.getInstance().getReference().child("Ordered Products").child(user.getUid()).child("Current Orders");

        recyclerView = findViewById(R.id.myorders_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        RadioGroup myRadioGroup = (RadioGroup) findViewById(R.id.orders_radioGroup);
        RadioButton radioActive = (RadioButton) findViewById(R.id.radio_active);
        RadioButton radioShipped = (RadioButton) findViewById(R.id.radio_shipped);


        tab = Tab.activesTab;
        onStart();


        radioActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Make the text underlined
                    SpannableString content = new SpannableString(getString((R.string.active_text)));
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    buttonView.setText(content);
                } else {
                    //Change the color here and make the Text bold
                    SpannableString content = new SpannableString(getString(R.string.active_text));
                    content.setSpan(null, 0, content.length(), 0);
                    buttonView.setText(content);
                    Typeface font = getResources().getFont(R.font.constan);
                    buttonView.setTypeface(font);
                }
            }
        });



        radioShipped.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Make the text underlined
                    SpannableString content = new SpannableString(getString((R.string.shipped_text)));
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    buttonView.setText(content);
                } else {
                    //Change the color here and make the Text bold
                    SpannableString content = new SpannableString(getString(R.string.shipped_text));
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
                if (checkedId == R.id.radio_active) {
                    tab = Tab.activesTab;
                    onStart();
                } else if (checkedId == R.id.radio_shipped) {
                    tab = Tab.shippedTab;
                    onStart();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
/*
        FirebaseDatabase.getInstance().getReference("Shipments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Shipments ship = snapshot.getValue(Shipments.class);*/
        if (tab == Tab.activesTab) {
            query = FirebaseDatabase.getInstance().getReference().child("Shipments").orderByChild("userUID").equalTo(user.getUid());

            FirebaseRecyclerOptions<Shipments> ordered = new FirebaseRecyclerOptions.Builder<Shipments>()
                    .setQuery(query, Shipments.class)
                    .build();

            FirebaseRecyclerAdapter<Shipments, MyOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<Shipments, MyOrdersViewHolder>(ordered) {
                @Override
                protected void onBindViewHolder(@NonNull MyOrdersViewHolder myOrdersViewHolder, int i, @NonNull Shipments orderedProduct1) {
                    String date = orderedProduct1.getDate();
                    date = date.substring(0, date.indexOf('a'));

                    myOrdersViewHolder.date.setText(date);
                    myOrdersViewHolder.total.setText("$ " + orderedProduct1.getTotalAmount().toString());
                    myOrdersViewHolder.quantity.setText(orderedProduct1.getAddress());

                    String status = orderedProduct1.getStatus();
                    if (status.equals("not delivered")) {
                        myOrdersViewHolder.status.setTextColor(Color.parseColor("#e60000"));
                        status = "In progress";
                    } else if (status.equals("shipping")) {
                        myOrdersViewHolder.status.setTextColor(Color.parseColor("#ffbf00"));
                        status = "Shipped";
                    } else {
                        myOrdersViewHolder.status.setTextColor(Color.parseColor("#00cc00"));
                        status = "Delivered";
                    }

                    myOrdersViewHolder.status.setText(status);

                    myOrdersViewHolder.detailsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MyOrders.this, OrderDetails.class);
                            intent.putExtra("purchaseDate", orderedProduct1.getDate());
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }
                    });
                }

                @NonNull
                @Override
                public MyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorders_order_layout, parent, false);
                    MyOrdersViewHolder holder = new MyOrdersViewHolder(view);
                    return holder;
                }
            };
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        } else if (tab == Tab.shippedTab) {
            query = FirebaseDatabase.getInstance().getReference().child("Shipping History").orderByChild("userUID").equalTo(user.getUid());

            FirebaseRecyclerOptions<Shipments> ordered = new FirebaseRecyclerOptions.Builder<Shipments>()
                    .setQuery(query, Shipments.class)
                    .build();

            FirebaseRecyclerAdapter<Shipments, MyOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<Shipments, MyOrdersViewHolder>(ordered) {
                @Override
                protected void onBindViewHolder(@NonNull MyOrdersViewHolder myOrdersViewHolder, int i, @NonNull Shipments orderedProduct1) {
                    String date = orderedProduct1.getDate();
                    date = date.substring(0, date.indexOf('a'));

                    myOrdersViewHolder.quantity.setText(orderedProduct1.getAddress());
                    myOrdersViewHolder.date.setText(date);
                    myOrdersViewHolder.total.setText("$ " + orderedProduct1.getTotalAmount().toString());

                    String status = orderedProduct1.getStatus();
                    if (status.equals("not delivered")) {
                        myOrdersViewHolder.status.setTextColor(Color.parseColor("#e60000"));
                        status = "In progress";
                    } else if (status.equals("shipping")) {
                        myOrdersViewHolder.status.setTextColor(Color.parseColor("#ffbf00"));
                        status = "Shipped";
                    } else {
                        myOrdersViewHolder.status.setTextColor(Color.parseColor("#00cc00"));
                        status = "Delivered";
                    }

                    myOrdersViewHolder.status.setText(status);

                    myOrdersViewHolder.detailsButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MyOrders.this, OrderDetails.class);
                            intent.putExtra("purchaseDate", orderedProduct1.getDate());
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }
                    });
                }

                @NonNull
                @Override
                public MyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorders_order_layout, parent, false);
                    MyOrdersViewHolder holder = new MyOrdersViewHolder(view);
                    return holder;
                }
            };
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }
    }
}