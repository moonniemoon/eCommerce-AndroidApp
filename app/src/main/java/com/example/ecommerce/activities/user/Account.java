package com.example.ecommerce.activities.user;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.R;
import com.example.ecommerce.fragments.user.AccountDetails;
import com.example.ecommerce.accounts.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Account extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;
    BottomNavigationView bottomNavigationView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.fragment_account);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if(user!=null ) {
           bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.account);

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
                            startActivity(new Intent(getApplicationContext(), ShoppingBag.class));
                            overridePendingTransition(0,0);
                            return true;
                        case R.id.account:
                            return true;
                    }

                    return false;
                }
            });

            reference = FirebaseDatabase.getInstance().getReference("Users");
            userID = user.getUid();
            TextView firstName = (TextView) findViewById(R.id.userFirstNameText);
            reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User userDetails = snapshot.getValue(User.class);

                    if (userDetails != null) {
                        firstName.setText(userDetails.getFirstName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Account.this, "Server error, please try again.", Toast.LENGTH_LONG).show();
                }
            });

            FrameLayout accountDetailsLayout= (FrameLayout) findViewById(R.id.accountDetailsFrameLayout);
            accountDetailsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeFragment(new AccountDetails());
                }
            });
            FrameLayout myOrdersLayout = (FrameLayout) findViewById(R.id.myOrdersFrameLayout);
            myOrdersLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Account.this, MyOrders.class);
                    startActivity(intent);
                }
            });
            FrameLayout addressBookLayout = (FrameLayout) findViewById(R.id.addressBookFrameLayout);
            addressBookLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Account.this, ChooseAddressForShipping.class);
                    intent.putExtra("comingFrom", "account");
                    startActivity(intent);
                }
            });
            FrameLayout joinUsAsASellerLayout = (FrameLayout) findViewById(R.id.joinUsAsASellerFrameLayout);
            joinUsAsASellerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Account.this, JoinUsAsASeller.class));
                }
            });
        }
        else {
            startActivity(new Intent(Account.this, UserLogIn.class));
        }
    }

    private void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_account, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
