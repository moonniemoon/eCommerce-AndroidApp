package com.example.ecommerce.activities.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.AddressViewHolder;
import com.example.ecommerce.ViewHolder.PaymentViewHolder;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PaymentMethod extends AppCompatActivity {

    private DatabaseReference ShoppingCartReference, ProductsReference;
    private Query query;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private String chosenAddress;
    private ImageView backButton;
    private Button orderButton;
    private TextView priceText;
    private double dlTotal = 0.0, itemTotalPrice;
    String currentDateandTime;
    private String address, city, date, personName,  phone, userUID, status, paymentMethod, country;
    private Double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_payment_method);


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userUID = user.getUid();

        ProductsReference = FirebaseDatabase.getInstance().getReference().child("Products");
        ShoppingCartReference = FirebaseDatabase.getInstance().getReference();
        query = ShoppingCartReference.child("Shopping Bags").child(user.getUid()).child("Items");

        backButton = (ImageView) findViewById(R.id.backbackbackbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentMethod.this, ChooseAddressForShipping.class);
                intent.putExtra("comingFrom", "payment");
                startActivity(intent);
            }
        });

        chosenAddress = getIntent().getStringExtra("shortcutName");
        priceText = (TextView) findViewById(R.id.payment_price);
        calculateTotalPrice();

        recyclerView = findViewById(R.id.payment_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        RadioGroup myRadioGroup = (RadioGroup) findViewById(R.id.payment_radioGroup);
        RadioButton radioDelivery = (RadioButton) findViewById(R.id.radio_cashOnDelivery);
        RadioButton radioCredit = (RadioButton) findViewById(R.id.radio_creditCard);

        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_cashOnDelivery) {
                    // if delivery is checked
                    // make paymentMethod = "cash on delivery"
                    paymentMethod = "cash on delivery";

                } else if (checkedId == R.id.radio_creditCard){
                    // uncheck credit
                    // toast "payment method with card is unavailable atm."
                    // check radio_cashOnDelivery
                    radioCredit.setChecked(false);
                    Toast.makeText(PaymentMethod.this, "Payment method with card is unavailable at the moment!", Toast.LENGTH_LONG).show();
                    radioDelivery.setChecked(true);
                }
            }
        });

        orderButton = (Button) findViewById(R.id.payment_button);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myRadioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(PaymentMethod.this, "Please select a payment method!", Toast.LENGTH_LONG).show();
                } else {
                    SimpleDateFormat currentDate = new SimpleDateFormat("yyyy MM dd 'at' HH:mm:ss z");
                    currentDateandTime = currentDate.format(new Date());

                    placeOrderInOrderedProducts();
                    // addToSalesRecords();

                    Intent intent = new Intent(PaymentMethod.this, ShoppingBag.class);
                    startActivity(intent);

                }
            }
        });

    }

    private void placeOrderInOrderedProducts() {

        status = "not delivered";

        FirebaseDatabase.getInstance().getReference().child("Shopping Bags").child(user.getUid()).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for (DataSnapshot data : snapshot.getChildren()) {
                            Item item = data.getValue(Item.class);

                            String productID = item.getID();
                            String productImage = item.getImageUrl();
                            String productName = item.getName();
                            Integer productQuantity = item.getQuantity();
                            Double productPrice = item.getPrice();
                            String productSeller = item.getSeller();
                            String productSize = item.getSize();



                            // Create/Put order in 'Current Orders'
                            OrderedProduct orderedProduct = new OrderedProduct(productID, productName, productSeller, productSize, status, productImage, productQuantity, productPrice, currentDateandTime);

                            FirebaseDatabase.getInstance().getReference("Ordered Products")
                                    .child(userUID).child("Current Orders").child(currentDateandTime).child(productID)
                                    .setValue(orderedProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(PaymentMethod.this, "Order has been successfully added to ordered products!", Toast.LENGTH_LONG).show();
                                    } else {
                                        String message = task.getException().toString();
                                        Toast.makeText(PaymentMethod.this, message, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                            // Subtract quantity from seller .
                            FirebaseDatabase.getInstance().getReference().child("Products").child(productSeller).child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    Double oldQuantity = Double.parseDouble(snapshot.child("quantity").getValue().toString());
                                    Double newQuantity = oldQuantity - productQuantity;

                                    if (newQuantity < 0) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentMethod.this);
                                        builder.setMessage("Sorry, we don't have enough stock for" + productName + "...\nWe are sorry for the inconvenience...")
                                                .setPositiveButton("OK",  new    DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //Do your redirect here
                                                        startActivity(new Intent(PaymentMethod.this, ShoppingBag.class));
                                                    }
                                                });
                                    } else {
                                        // Subtracting purchased quantity
                                        ProductsReference.child(productSeller).child(productID).child("quantity").setValue(newQuantity);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { }
                            });


                            // Remove item from shopping bag.
                            FirebaseDatabase.getInstance().getReference().child("Shopping Bags").child(userUID).child("Items").child(productID).removeValue();


                            // place order in 'Shipments'
                            FirebaseDatabase.getInstance().getReference().child("Address Details").child(user.getUid()).child(chosenAddress)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                AddressDetail addressBook = snapshot.getValue(AddressDetail.class);
                                                address = addressBook.getAddress();
                                                city = addressBook.getTownCity();
                                                personName = addressBook.getFirstName() +  " " + addressBook.getLastName();
                                                country = addressBook.getCountry();
                                                phone = addressBook.getPhone();

                                                Shipments shipments = new Shipments(address, city, currentDateandTime, personName, phone, userUID, status, paymentMethod, country, dlTotal);

                                                FirebaseDatabase.getInstance().getReference("Shipments").child(currentDateandTime).setValue(shipments).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(PaymentMethod.this, "Your shipment has been placed!", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            String message = task.getException().toString();
                                                            Toast.makeText(PaymentMethod.this, message, Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) { }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }



    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Item> address = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        FirebaseRecyclerAdapter<Item, PaymentViewHolder> adapter = new FirebaseRecyclerAdapter<Item, PaymentViewHolder>(address) {
            @Override
            protected void onBindViewHolder(@NonNull PaymentViewHolder addressViewHolder, int i, @NonNull Item addressDetail) {

                addressViewHolder.productName.setText(addressDetail.getName());
                addressViewHolder.productCompany.setText(addressDetail.getSeller());
                addressViewHolder.productId.setText(addressDetail.getID());
                addressViewHolder.productCategory.setText(addressDetail.getCategory());
                addressViewHolder.productQuantity.setText("Quantity: " + addressDetail.getQuantity().toString());
                addressViewHolder.productPrice.setText(addressDetail.getPrice().toString());
                Picasso.get().load(addressDetail.getImageUrl()).into(addressViewHolder.productImage);

            }

            @NonNull
            @Override
            public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_summary_layout, parent, false);
                PaymentViewHolder holder = new PaymentViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void calculateTotalPrice(){
        dlTotal = 0.0;
        ShoppingCartReference.child("Shopping Bags").child(user.getUid()).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Item item = snapshot.getValue(Item.class);
                            itemTotalPrice = item.getPrice() * item.getQuantity();
                            dlTotal += itemTotalPrice;
                        }
                        priceText.setText(String.valueOf(dlTotal));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

}