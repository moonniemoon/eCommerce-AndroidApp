package com.example.ecommerce.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.example.ecommerce.accounts.User;
import com.example.ecommerce.activities.company.CompanyAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogIn extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private FirebaseUser user;
    private Button logInButton, becomeAMemberButton;
    private String email, password;
    private EditText emailInput, passwordInput;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        logInButton = (Button) findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
        becomeAMemberButton = (Button) findViewById(R.id.becomeAMemberButton);
        becomeAMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, Register.class));
            }
        });
        emailInput = (EditText) findViewById(R.id.loginEditTextEmailAddress);
        passwordInput = (EditText) findViewById(R.id.loginEditTextPassword);
    }

    private void logIn() {
        // Get credentials
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        //Validate data
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Please enter your email");
            emailInput.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Please enter your password");
            passwordInput.requestFocus();
        } else {
            allowAccessToAccount();
        }
    }

    private void checkUserType() {
        reference = FirebaseDatabase.getInstance().getReference();
        user = firebaseAuth.getCurrentUser();
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userType = dataSnapshot.getKey();

                if(userType == "user"){
                    startActivity(new Intent(LogIn.this, Account.class));
                }
                else{
                    startActivity(new Intent(LogIn.this, CompanyAccount.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void allowAccessToAccount() {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> loginTask) {
                if (loginTask.isSuccessful()) {
                    Toast.makeText(LogIn.this, "Welcome back!", Toast.LENGTH_LONG).show();
                    checkUserType();
                } else {
                    emailInput.setError("The email address does not exist.");
                    emailInput.requestFocus();
                }
            }
        });
    }
}
