package com.example.ecommerce.activities.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.BoutiqueViewHolder;
import com.example.ecommerce.ViewHolder.InsideBoutiqueViewHolder;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.activities.company.AddClothingItem;
import com.example.ecommerce.activities.company.NewProductCategorySelection;
import com.example.ecommerce.models.Item;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PlayGamesAuthCredential;
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

public class ProductDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FloatingActionButton addToWishlistButton;
    private ElegantNumberButton numberButton;
    private Spinner sizeSpinner;
    private Button cartButton;
    private TextView productCompany, productDescription, productName, productPrice;
    private String companyName, pID, pGender, pPrice, pName, pDescription, pCategory, pColour, pImageURL, pQuantity, pSize;
    private Double d_Price;
    private Integer i_Quantity;
    private ImageView backButton;
    private String pageFrom;

    private String selected_id = "";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    ImageSlider mainslider;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().child("Wishlist");
    List<String> sizeList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        companyName = getIntent().getStringExtra("companyName");
        pID = getIntent().getStringExtra("ID");
        pGender = getIntent().getStringExtra("gender");
        pPrice = getIntent().getStringExtra("price");
        pName = getIntent().getStringExtra("name");
        pDescription = getIntent().getStringExtra("description");
        pCategory = getIntent().getStringExtra("category");
        pColour = getIntent().getStringExtra("colour");
        pImageURL = getIntent().getStringExtra("imageURL");
        pageFrom = getIntent().getStringExtra("page");



        if (pID.contains("-xxsmall")) {
            selected_id = pID.replace("-xxsmall", "");
        } else if (pID.contains("-xsmall")) {
            selected_id = pID.replace("-xsmall", "");
        } else if (pID.contains("-small")) {
            selected_id = pID.replace("-small", "");
        } else if (pID.contains("-medium")) {
            selected_id = pID.replace("-medium", "");
        } else if (pID.contains("-large")) {
            selected_id = pID.replace("-large", "");
        } else if (pID.contains("-xlarge")) {
            selected_id = pID.replace("-xlarge", "");
        } else if (pID.contains("-xxlarge")) {
            selected_id = pID.replace("-xxlarge", "");
        } else if(pID.contains("-nosize")){
            selected_id = pID.replace("-nosize", ""); // add more validations for shoe sizes
        }


        addToWishlistButton = (FloatingActionButton) findViewById(R.id.productDetails_wishlistButton);
        numberButton = (ElegantNumberButton) findViewById(R.id.productdetails_elegantButton);
        cartButton = (Button) findViewById(R.id.cart_butButton);
        productCompany = (TextView) findViewById(R.id.productCompany_name);
        productName = (TextView) findViewById(R.id.productDetails_name);
        productDescription = (TextView) findViewById(R.id.productDetails_description);
        productPrice = (TextView) findViewById(R.id.productDetails_Price);
        sizeSpinner = (Spinner) findViewById(R.id.productdetails_size_spinner);


        productCompany.setText(companyName);
        productName.setText(pName);
        productDescription.setText(pDescription);
        productPrice.setText(pPrice);


        d_Price = Double.parseDouble(pPrice);


        backButton = (ImageView) findViewById(R.id.backkkkButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageFrom.equals("inside")) {
                    Intent intent = new Intent(ProductDetails.this, InsideBoutique.class);
                    intent.putExtra("companyName", companyName);
                    startActivity(intent);
                } else if (pageFrom.equals("wishlist")) {
                    Intent intent = new Intent(ProductDetails.this, Wishlist.class);
                    startActivity(intent);
                }
            }


        });


        // https://www.informationraja.com/2020/08/firebase-image-slider-source-code.html
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mainslider = findViewById(R.id.image_slider);

        final List<SlideModel> remoteimages = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Products").child(companyName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {

                            String p_id = data.getKey();
                            String cropped_id = "";
                            String cropped_size = "";
                            if (p_id.contains("-xxsmall")) {
                                cropped_id = p_id.replace("-xxsmall", "");
                                cropped_size = "XXS";
                            } else if (p_id.contains("-xsmall")) {
                                cropped_id = p_id.replace("-xsmall", "");
                                cropped_size = "XS";
                            } else if (p_id.contains("-small")) {
                                cropped_id = p_id.replace("-small", "");
                                cropped_size = "S";
                            } else if (p_id.contains("-medium")) {
                                cropped_id = p_id.replace("-medium", "");
                                cropped_size = "M";
                            } else if (p_id.contains("-large")) {
                                cropped_id = p_id.replace("-large", "");
                                cropped_size = "L";
                            } else if (p_id.contains("-xlarge")) {
                                cropped_id = p_id.replace("-xlarge", "");
                                cropped_size = "XL";
                            } else if (p_id.contains("-xxlarge")) {
                                cropped_id = p_id.replace("-xxlarge", "");
                                cropped_size = "XXL";
                            } // add more validations for shoe sizes

                            if (selected_id.equals(cropped_id)) {
                                remoteimages.add(new SlideModel(data.child("imageUrl").getValue().toString()
                                        , ScaleTypes.CENTER_CROP));
                                sizeList.add(cropped_size);
                            }
                        }
                        mainslider.setImageList(remoteimages, ScaleTypes.CENTER_CROP);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        // if-else statement for, if the product is a clothing item, shoe or a beauty item.
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(ProductDetails.this,
                R.array.clothingsizes_array, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);



        if (pageFrom.equals("inside")) {
            addToWishlistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addingToWishlist();
                    addToWishlistButton.setImageResource(R.drawable.heart_pink);
                    if (selected_id.contains("-")) {
                        selected_id = selected_id.substring(0, selected_id.indexOf("-"));
                    }
                }
            });
        } else if (pageFrom.equals("wishlist")) {
            addToWishlistButton.setImageResource(R.drawable.heart_pink);
            addToWishlistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(pID);
                    addToWishlistButton.setImageResource(R.drawable.productdetails_icon_heart);
                    if (selected_id.contains("-")) {
                        selected_id = selected_id.substring(0, selected_id.indexOf("-"));
                    }
                }
            });
        }

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCart();
            }
        });
    }


    private void addingToWishlist() {

        pQuantity = numberButton.getNumber();
        i_Quantity = Integer.parseInt(pQuantity);

        pSize = sizeSpinner.getSelectedItem().toString();

        if (pSize.equals("XXS")){
            selected_id += "-xxsmall";
        } else if (pSize.equals("XS")){
            selected_id += "-xsmall";
        } else if (pSize.equals("S")){
            selected_id += "-small";
        } else if (pSize.equals("M")){
            selected_id += "-medium";
        } else if (pSize.equals("L")){
            selected_id += "-large";
        } else if (pSize.equals("XL")){
            selected_id += "-xlarge";
        } else if (pSize.equals("XXL")){
            selected_id += "-xxlarge";
        } else if (pSize.equals("One size fits all")){
            selected_id += "-one";
        }

        Item item = new Item(selected_id, pName, pDescription, pGender, pSize, pColour, i_Quantity, pCategory, companyName, pImageURL, d_Price);

        FirebaseDatabase.getInstance().getReference("Wishlist").child(user.getUid()).child(selected_id).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProductDetails.this, "Item added successfully to your wishlist!", Toast.LENGTH_LONG).show();
                } else {
                    String message = task.getException().toString();
                    Toast.makeText(ProductDetails.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }


    private void removeItem(String idName){
       databaseReference.child(user.getUid()).child(idName).removeValue();
        Toast.makeText(ProductDetails.this, "You have successfully deleted this product from your wishlist!", Toast.LENGTH_LONG).show();
    }


    private void addingToCart() {
        pQuantity = numberButton.getNumber();
        i_Quantity = Integer.parseInt(pQuantity);

        pSize = sizeSpinner.getSelectedItem().toString();

        if (pSize.equals("XXS")){
            selected_id += "-xxsmall";
        } else if (pSize.equals("XS")){
            selected_id += "-xsmall";
        } else if (pSize.equals("S")){
            selected_id += "-small";
        } else if (pSize.equals("M")){
            selected_id += "-medium";
        } else if (pSize.equals("L")){
            selected_id += "-large";
        } else if (pSize.equals("XL")){
            selected_id += "-xlarge";
        } else if (pSize.equals("XXL")){
            selected_id += "-xxlarge";
        } else if (pSize.equals("One size fits all")){
            selected_id += "-one";
        }

        Item item = new Item(selected_id, pName, pDescription, pGender, pSize, pColour, i_Quantity, pCategory, companyName, pImageURL, d_Price);

        FirebaseDatabase.getInstance().getReference("Shopping Bags").child(user.getUid()).child("Items").child(selected_id).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProductDetails.this, "Item added successfully to your shopping bag!", Toast.LENGTH_LONG).show();
                } else {
                    String message = task.getException().toString();
                    Toast.makeText(ProductDetails.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}