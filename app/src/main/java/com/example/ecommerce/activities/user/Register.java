package com.example.ecommerce.activities.user;

import android.content.Intent;
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
import com.example.ecommerce.fragments.user.Home_Screen;
import com.example.ecommerce.accounts.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class Register extends AppCompatActivity {

    private String firstName, lastName, email, password, userID;
    private EditText firstNameInput, lastNameInput, emailInput, passwordInput;
    FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;
    private String parentDatabaseName = "Users";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child(parentDatabaseName);

        firstNameInput = (EditText)  findViewById(R.id.firstNameEditText);
        lastNameInput = (EditText)  findViewById(R.id.lastNameEditText);
        emailInput = (EditText)  findViewById(R.id.emailAddressEditText);
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
        userID = generateUserId();

        if(TextUtils.isEmpty(firstName)){
            firstNameInput.setError("Please enter a first name.");
            firstNameInput.requestFocus();
        }
        else if(TextUtils.isEmpty(lastName)){
            lastNameInput.setError("Please enter a last name.");
            lastNameInput.requestFocus();
        }
        else if(TextUtils.isEmpty(email)){
            emailInput.setError("Please enter an email address.");
            emailInput.requestFocus();
        }
        else if(TextUtils.isEmpty(password)){
            emailInput.setError("Please enter a password.");
            emailInput.requestFocus();
        }
        else if(password.length()<8){
            Toast.makeText(this, "Password must be at least 8 characters" , Toast.LENGTH_LONG).show();
            passwordInput.requestFocus();
        }
        else{
            validateCredentials(userID);
        }
    }

    private void validateCredentials(String userID) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child(parentDatabaseName).child(userID).exists())){
                    createUser();
                }
                else{
                    emailInput.setError("This email address is already associated with a user.");
                    emailInput.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createUser(){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(firstName, lastName,email, password, "user");

                    FirebaseDatabase.getInstance().getReference(parentDatabaseName).child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Register.this, "Welcome to the app!", Toast.LENGTH_LONG).show();
                                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> loginTask) {
                                        if (loginTask.isSuccessful()) {
                                            startActivity(new Intent(Register.this, Account.class));
                                        } else {
                                            Toast.makeText(Register.this, "Server error, please try again." , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            else{
                                try {
                                    throw task.getException();
                                } catch(Exception e) {
                                    // email already in use
                                    emailInput.setError("This email address is already associated with a user.");
                                    emailInput.requestFocus();
                                }
                            }
                        }
                    });

                }
                else{
                    // email already in use
                    emailInput.setError("This email address is already associated with a user.");
                    emailInput.requestFocus();
                }
            }
        });
    }

    private String generateUserId(){
        String userID = UUID.randomUUID().toString();
        return userID;
    }

    private void changeFragment(Fragment fragment){
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_register, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
    }
}
