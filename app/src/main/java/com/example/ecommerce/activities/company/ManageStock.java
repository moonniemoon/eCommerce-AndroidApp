package com.example.ecommerce.activities.company;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ProductViewHolder;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.activities.user.HomeActivity;
import com.example.ecommerce.models.Item;
import com.example.ecommerce.models.RestockInfo;
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

public class ManageStock extends AppCompatActivity {

    private Button addNewItemButton;
    private ImageView backButton;

    private DatabaseReference ItemReference, CompanyReference;
    private Query query;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private String companyName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managestock);
        getSupportActionBar().hide();

        CompanyReference = FirebaseDatabase.getInstance().getReference("Companies");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        ItemReference = FirebaseDatabase.getInstance().getReference().child("Products");
        addNewItemButton = (Button) findViewById(R.id.addItemButton);
        addNewItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageStock.this, NewProductCategorySelection.class);
                intent.putExtra("companyN", companyName);   // This sends companyName to NewProductCategorySelection.class
                                                                  // Has to be accessed in that class too!
                startActivity(intent);
                //startActivity(new Intent(ManageStock.this, NewProductCategorySelection.class));
            }
        });
        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageStock.this, CompanyAccount.class));
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

        CompanyReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Company companyDetails = snapshot.getValue(Company.class);
                companyName = companyDetails.getCompanyName();
                query = ItemReference.child(companyName);

                FirebaseRecyclerOptions<Item> items = new FirebaseRecyclerOptions.Builder<Item>()
                        .setQuery(query, Item.class)
                        .build();

                FirebaseRecyclerAdapter<Item, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Item, ProductViewHolder>(items) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Item item) {
                        productViewHolder.productId.setText("Item ID: " + item.getID());
                        productViewHolder.productName.setText("Item Name: " + item.getName());
                        productViewHolder.productDescription.setText("Item Description: " + item.getDescription());
                        productViewHolder.productSize.setText("Item Size: " + item.getSize());
                        productViewHolder.productQuantity.setText("Item Quantity: " + item.getQuantity().toString());
                        productViewHolder.productPrice.setText("$" + item.getPrice().toString());
                        Picasso.get().load(item.getImageUrl()).into(productViewHolder.productImage);

                        productViewHolder.removeProductButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                removeItem(companyName, item);
                            }
                        });
                        productViewHolder.moreOptions.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PopupMenu popup = new PopupMenu(productViewHolder.moreOptions.getContext(), view);
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        switch (menuItem.getItemId()) {
                                            case R.id.renameOption:
                                                renameItem(companyName, item);
                                                return true;
                                            case R.id.restockOption:
                                                restockItem(companyName, item);
                                                return true;
                                            default:
                                                return false;
                                        }
                                    }
                                });
                                // here you can inflate your menu
                                popup.inflate(R.menu.managestock_options_menu);
                                popup.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
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

    private void removeItem(String companyName, Item item){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        ItemReference.child(companyName).child(item.getID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ManageStock.this, "Item removed successfully!", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    String message = task.getException().toString();
                                    Toast.makeText(ManageStock.this, message, Toast.LENGTH_LONG).show();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(ManageStock.this);
        builder.setMessage("Are you sure you want to remove this item?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void renameItem(String companyName, Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter new name of item: ");

        final EditText newNameInput = new EditText(this);
        newNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(newNameInput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ItemReference.child(companyName).child(item.getID()).child("name").setValue(newNameInput.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ManageStock.this, "Item renamed successfully!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ManageStock.this, "Server error, please try again later.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void saveRestockInfoToDatabase(String companyName, Item item, int quantity){
        Date currentTime = Calendar.getInstance().getTime();

        double totalCost = item.getPrice() * item.getQuantity();

        RestockInfo restockInfo = new RestockInfo(item.getID(), item.getName(), currentTime.toString(), quantity, totalCost);

        FirebaseDatabase.getInstance().getReference("Restock Records").child(companyName).child(currentTime.toString()).setValue(restockInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ManageStock.this, "Record of restock has been made", Toast.LENGTH_LONG).show();
                } else {
                    String message = task.getException().toString();
                    Toast.makeText(ManageStock.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void restockItem(String companyName, Item item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter item quantity: ");

        final EditText stockAmountInput = new EditText(this);
        stockAmountInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(stockAmountInput);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ItemReference.child(companyName).child(item.getID()).child("quantity").setValue(Integer.parseInt(stockAmountInput.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ManageStock.this, "Item restocked successfully!", Toast.LENGTH_LONG).show();
                            saveRestockInfoToDatabase(companyName, item, Integer.parseInt(stockAmountInput.getText().toString()));
                            CompanyReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Company companyDetails = snapshot.getValue(Company.class);
                                    double currentRevenue = companyDetails.getRevenue();
                                    double totalCost = item.getPrice()* item.getQuantity();
                                    calculateRevenue(currentRevenue, totalCost);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } else {
                            Toast.makeText(ManageStock.this, "Server error, please try again later.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void calculateRevenue(double currentRevenue, double totalCost){
        double newRevenue = currentRevenue - totalCost;

        CompanyReference.child(user.getUid()).child("revenue").setValue(newRevenue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ManageStock.this, "Revenue updated successfully.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ManageStock.this, "Server error, please try again later.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
