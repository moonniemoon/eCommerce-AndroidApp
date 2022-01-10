package com.example.ecommerce.activities.company;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.ecommerce.R;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.adapters.SearchAdapter;
import com.example.ecommerce.adapters.ShipmentAdapter;
import com.example.ecommerce.models.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shipments extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String companyID, companyName, companyN = "";
    private DatabaseReference CompanyReference;
    private String parentDatabaseName = "Companies";

    RecyclerView recyclerView;
    List<com.example.ecommerce.models.Shipments> allItems;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_shipments);

        CompanyReference = FirebaseDatabase.getInstance().getReference("Companies");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(parentDatabaseName);
        companyID = user.getUid();

        allItems = new ArrayList<>();

        recyclerView = findViewById(R.id.shipping_recycler_menu);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        FirebaseDatabase.getInstance().getReference("Shipments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    com.example.ecommerce.models.Shipments ship = snap.getValue(com.example.ecommerce.models.Shipments.class);

                    CompanyReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Company companyDetails = snapshot.getValue(Company.class);
                            companyName = companyDetails.getCompanyName();

                            String comName = companyName.toLowerCase();
                            String containsName = ship.getSellers();

                            if (containsName.toLowerCase().contains(comName.toLowerCase())) {
                                if (allItems == null) {
                                    allItems = new ArrayList<>();
                                }

                                allItems.add(ship);

                                adapter = new ShipmentAdapter(allItems, Shipments.this, ship.getUserUID(), ship.getDate(), companyName);
                                recyclerView.setAdapter(adapter);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            }
        }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private String getCompanyName() {
        companyN = "";
        CompanyReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 Company companyDetails = snapshot.getValue(Company.class);
                companyN = companyDetails.getCompanyName();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        return companyN;
    }
}