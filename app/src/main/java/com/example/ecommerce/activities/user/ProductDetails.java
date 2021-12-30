package com.example.ecommerce.activities.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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
import com.example.ecommerce.models.Item;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductDetails extends AppCompatActivity {

   // private FloatingActionButton addToWishlistButton;
    private ElegantNumberButton numberButton;
    private Spinner sizeSpinner;
    private TextView productCompany, productDescription, productName, productPrice;
    private String companyName, pID, pGender, pPrice, pName, pDescription;

    private String selected_id = "";

    ImageSlider mainslider;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference().child("Products");
    List<String> sizeList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getSupportActionBar().hide();

        companyName = getIntent().getStringExtra("companyName");
        pID = getIntent().getStringExtra("ID");
        pGender = getIntent().getStringExtra("gender");
        pPrice = getIntent().getStringExtra("price");
        pName = getIntent().getStringExtra("name");
        pDescription = getIntent().getStringExtra("description");


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
        }


        sizeSpinner = (Spinner) findViewById(R.id.productdetails_size_spinner);

       // addToWishlistButton = (FloatingActionButton) findViewById(R.id.productDetails_wishlistButton);
        numberButton = (ElegantNumberButton) findViewById(R.id.productdetails_elegantButton);

        productCompany = (TextView) findViewById(R.id.productCompany_name);
        productName = (TextView) findViewById(R.id.productDetails_name);
        productDescription = (TextView) findViewById(R.id.productDetails_description);
        productPrice = (TextView) findViewById(R.id.productDetails_Price);


        productCompany.setText(companyName);
        productName.setText(pName);
        productDescription.setText(pDescription);
        productPrice.setText(pPrice);


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
                            }

                            if (selected_id.equals(cropped_id)) {
                                remoteimages.add(new SlideModel(data.child("imageUrl").getValue().toString()
                                        , ScaleTypes.CENTER_CROP));
                                sizeList.add(cropped_size);
                            }
                        }
                        mainslider.setImageList(remoteimages, ScaleTypes.CENTER_CROP);
                      /*  mainslider.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {
                                Toast.makeText(getApplicationContext(), remoteimages.get(i).getTitle().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
*/
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

        // if-else statement for, if the product is a clothing item, shoe or a beauty item.
        // Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(ProductDetails.this, android.R.layout.simple_spinner_item, sizeList);
       /* ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(ProductDetails.this,
                R.array.clothingsizes_array, sizeList);*/
        // Specify the layout to use when the list of choices appears
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sizeSpinner.setAdapter(sizeAdapter);
    }
}