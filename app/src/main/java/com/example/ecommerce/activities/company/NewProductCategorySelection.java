package com.example.ecommerce.activities.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.accounts.Company;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewProductCategorySelection extends AppCompatActivity {

    private FrameLayout tees, shirts, trousers, skirts, footwear, accessories, beauty, outerwear;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newproductcategoryselection);
        getSupportActionBar().hide();

        tees = (FrameLayout)  findViewById(R.id.teesFrameLayout);
        shirts = (FrameLayout)  findViewById(R.id.shirtsFrameLayout);
        trousers = (FrameLayout)  findViewById(R.id.trousersFrameLayout);
        skirts = (FrameLayout)  findViewById(R.id.skirtsFrameLayout);
        footwear = (FrameLayout)  findViewById(R.id.footwearFrameLayout);
        accessories = (FrameLayout)  findViewById(R.id.accessoriesFrameLayout);
        beauty = (FrameLayout)  findViewById(R.id.beautyFrameLayout);
        outerwear = (FrameLayout)  findViewById(R.id.outerwearFrameLayout);


        tees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewProductCategorySelection.this, AddClothingItem.class);
                intent.putExtra("Category", "Tees");
                startActivity(intent);
            }
        });
        shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewProductCategorySelection.this, AddClothingItem.class);
                intent.putExtra("Category", "Shirts");
                startActivity(intent);
            }
        });
        trousers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewProductCategorySelection.this, AddClothingItem.class);
                intent.putExtra("Category", "Trousers");
                startActivity(intent);
            }
        });
        skirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewProductCategorySelection.this, AddClothingItem.class);
                intent.putExtra("Category", "Skirts");
                startActivity(intent);
            }
        });
        footwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewProductCategorySelection.this, AddShoeItem.class);
                intent.putExtra("Category", "Footwear");
                startActivity(intent);
            }
        });
        accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewProductCategorySelection.this, AddOtherItem.class);
                intent.putExtra("Category", "Accessories");
                startActivity(intent);
            }
        });
        beauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewProductCategorySelection.this, AddOtherItem.class);
                intent.putExtra("Category", "Beauty");
                startActivity(intent);
            }
        });
        outerwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewProductCategorySelection.this, AddClothingItem.class);
                intent.putExtra("Category", "Outerwear");
                startActivity(intent);
            }
        });
    }
}
