package com.example.ecommerce.activities.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.ecommerce.R;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.fragments.company.AddClothingItem;
import com.example.ecommerce.fragments.company.CompanyDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompanyAccount extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String companyID;
    private String parentDatabaseName = "Companies";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companyaccount);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(parentDatabaseName);
        companyID = user.getUid();

        TextView companyName = (TextView) findViewById(R.id.greetingsCompanyNameText);
        ImageView companyBackground = (ImageView) findViewById(R.id.companyBackground);

        reference.child(companyID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                      @Override
                                                                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                          Company companyDetails = snapshot.getValue(Company.class);

                                                                          if (companyDetails != null) {
                                                                              companyName.setText(companyDetails.getCompanyName());
                                                                              if(companyDetails.getBackgroundURL()!=null) {
                                                                                  Glide.with(CompanyAccount.this).load(companyDetails.backgroundURL).into(companyBackground);
                                                                              }
                                                                          }
                                                                          else{
                                                                              Toast.makeText(CompanyAccount.this, "Server error, please try again." , Toast.LENGTH_LONG).show();
                                                                          }
                                                                      }

                                                                      @Override
                                                                      public void onCancelled(@NonNull DatabaseError error) {
                                                                          Toast.makeText(CompanyAccount.this, "Server error, please try again." , Toast.LENGTH_LONG).show();
                                                                      }
                                                                  });

            FrameLayout companyDetailsLayout= (FrameLayout) findViewById(R.id.companyDetailsFrameLayout);
        companyDetailsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  changeFragment(new CompanyDetails());
                }
            });
            FrameLayout manageStockLayout = (FrameLayout) findViewById(R.id.manageStockFrameLayout);
        manageStockLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(new AddClothingItem());
                }
            });
            FrameLayout cashRegisterLayout = (FrameLayout) findViewById(R.id.virtualCashRegisterFrameLayout);
            cashRegisterLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            FrameLayout contactUsLayout = (FrameLayout) findViewById(R.id.contactUsFrameLayout);
            contactUsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
    }

    private void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_companyaccount, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
