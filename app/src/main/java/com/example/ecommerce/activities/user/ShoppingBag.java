package com.example.ecommerce.activities.user;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.ViewHolder.ShoppingBagViewHolder;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.accounts.User;
import com.example.ecommerce.activities.company.AddClothingItem;
import com.example.ecommerce.activities.company.ManageStock;
import com.example.ecommerce.models.Item;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.AccessController;

public class ShoppingBag extends AppCompatActivity {

    private DatabaseReference ShoppingBagReference, ItemReference;
    private Query query;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private boolean spinnerInitialized = false;

    RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    BottomNavigationView bottomNavigationView;

    private TextView totalPrice;
    private Button buyButton;

    private double dlTotal = 0.0, itemTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_shopping_bag);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        ShoppingBagReference = FirebaseDatabase.getInstance().getReference().child("Shopping Bags");
        query = ShoppingBagReference.child(user.getUid()).child("Items");
        ItemReference = ShoppingBagReference.child(user.getUid()).child("Items");

        buyButton = (Button) findViewById(R.id.buyButton);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        calculateTotalPrice();


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.shopping_Bag);


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
                        return true;
                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(), Account.class));
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


        // i'm getting data if the user already has an address or not. - Seli
        DatabaseReference addressRef = FirebaseDatabase.getInstance().getReference().child("Address Details");
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutManager.getItemCount() == 0) {
                   emptyShoppingBagPopup();
                }
                else {
                    addressRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // send to 'choose address'
                                Intent intent = new Intent(ShoppingBag.this, ChooseAddressForShipping.class);
                                intent.putExtra("comingFrom", "bagUserFound");
                                startActivity(intent);
                            } else {
                                // send to 'create an address'
                                Intent intent = new Intent(ShoppingBag.this, AddressBook.class);
                                intent.putExtra("comingFrom", "bagUserNotFound");
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Item> items = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        FirebaseRecyclerAdapter<Item, ShoppingBagViewHolder> adapter = new FirebaseRecyclerAdapter<Item, ShoppingBagViewHolder>(items) {
            @Override
            protected void onBindViewHolder(@NonNull ShoppingBagViewHolder shoppingBagViewHolder, int i, @NonNull Item item) {
                shoppingBagViewHolder.productId.setText("Item ID: "+item.getID());
                shoppingBagViewHolder.productName.setText(item.getName());
                shoppingBagViewHolder.productDescription.setText(item.getDescription());
                shoppingBagViewHolder.productCategory.setText(item.getCategory());
                double total = item.getPrice() * item.getQuantity();
                shoppingBagViewHolder.productPrice.setText("$"+ String.format("%.2f",total));
                Picasso.get().load(item.getImageUrl()).into(shoppingBagViewHolder.productImage);

                ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(ShoppingBag.this,
                        R.array.quantities_array, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                shoppingBagViewHolder.quantitySpinner.setAdapter(sizeAdapter);
                shoppingBagViewHolder.quantitySpinner.setSelection(item.getQuantity()-1);

                shoppingBagViewHolder.quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        double total = item.getPrice() * item.getQuantity();
                        shoppingBagViewHolder.productPrice.setText("$"+String.format("%.2f",total));
                        spinnerInitialized = true;
                        ShoppingBagReference.child(user.getUid()).child("Items").child(item.getID()).child("quantity").setValue(shoppingBagViewHolder.quantitySpinner.getSelectedItemPosition()+1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                    if(spinnerInitialized) {
                                        calculateTotalPrice();
                                        Toast.makeText(ShoppingBag.this, "Quantity updated successfully." , Toast.LENGTH_LONG).show();
                                        spinnerInitialized = false;
                                    }
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                shoppingBagViewHolder.removeProductButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       removeItem(item);
                    }
                });

            }

            @NonNull
            @Override
            public ShoppingBagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppingbag_items_layout, parent, false);
                ShoppingBagViewHolder holder = new ShoppingBagViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeItem(Item item){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        spinnerInitialized = true;
                        ItemReference.child(item.getID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    if(spinnerInitialized) {
                                        Toast.makeText(ShoppingBag.this, "Item removed successfully!", Toast.LENGTH_LONG).show();
                                        calculateTotalPrice();
                                    }
                                    spinnerInitialized = false;
                                }
                                else{
                                    String message = task.getException().toString();
                                    Toast.makeText(ShoppingBag.this, message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingBag.this);
        builder.setMessage("Are you sure you want to remove this item?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void calculateTotalPrice(){
        dlTotal = 0.0;
        ShoppingBagReference.child(user.getUid()).child("Items")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Item item = snapshot.getValue(Item.class);
                            itemTotalPrice = item.getPrice() * item.getQuantity();
                            dlTotal += itemTotalPrice;
                        }
                        totalPrice.setText(String.format("%.2f",dlTotal));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    private void emptyShoppingBagPopup(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            };
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingBag.this);
        builder.setMessage("Your shopping bag is empty. Start shopping with us by picking out items from the Home tab!").setPositiveButton("OK", dialogClickListener)
                .setNegativeButton("CANCEL", dialogClickListener).show();
    }
}