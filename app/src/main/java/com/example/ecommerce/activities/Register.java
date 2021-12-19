package com.example.ecommerce.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.R;
import com.example.ecommerce.fragments.FragmentAccount;
import com.example.ecommerce.fragments.Home_Screen;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Register extends AppCompatActivity {

    private String firstName, lastName, email, password;
    private EditText firstNameInput, lastNameInput, emailInput, passwordInput;
    FirebaseAuth firebaseAuth;
    private DatabaseReference customerRef;
    private Button registerButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        firstNameInput = (EditText)  findViewById(R.id.firstNameEditText);
        lastNameInput = (EditText)  findViewById(R.id.lastNameEditText);
        emailInput = (EditText)  findViewById(R.id.emailEditText);
        passwordInput = (EditText)  findViewById(R.id.passwordEditText);
        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        ImageView backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new Home_Screen());
            }
        });
    }

    private void validateData(){
        firstName = firstNameInput.getText().toString();
        lastName = lastNameInput.getText().toString();
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(firstName)){
            Toast.makeText(this, "Please enter a first name" , Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(lastName)){
            Toast.makeText(this, "Please enter a last name" , Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter a valid email" , Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter a password" , Toast.LENGTH_LONG).show();
        }
        else if(password.length()<8){
            Toast.makeText(this, "Password must be at least 8 characters" , Toast.LENGTH_LONG).show();
        }
        else{
            createUser();
        }
    }

    private void createUser(){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(Register.this, "Welcome to the app!" , Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this, e.getMessage() , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void changeFragment(Fragment fragment){
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_login, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
    }
}
