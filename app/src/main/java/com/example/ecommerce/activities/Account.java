package com.example.ecommerce.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.example.ecommerce.fragments.AccountDetails;
import com.example.ecommerce.fragments.JoinUsAsASeller;
import com.google.firebase.auth.FirebaseAuth;

public class Account extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            FrameLayout accountDetailsLayout= (FrameLayout) findViewById(R.id.accountDetailsFrameLayout);
            accountDetailsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            FrameLayout myOrdersLayout = (FrameLayout) findViewById(R.id.myOrdersFrameLayout);
            myOrdersLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            FrameLayout addressBookLayout = (FrameLayout) findViewById(R.id.addressBookFrameLayout);
            addressBookLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            FrameLayout joinUsAsASellerLayout = (FrameLayout) findViewById(R.id.joinUsAsASellerFrameLayout);
            joinUsAsASellerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        else {
            startActivity(new Intent(Account.this, LogIn.class));
        }
    }
}
