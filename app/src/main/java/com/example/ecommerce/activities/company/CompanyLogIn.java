package com.example.ecommerce.activities.company;

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
import androidx.appcompat.widget.AppCompatButton;

import com.example.ecommerce.R;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.accounts.User;
import com.example.ecommerce.activities.user.Account;
import com.example.ecommerce.activities.user.Register;
import com.example.ecommerce.activities.user.UserLogIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompanyLogIn extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private FirebaseUser user;
    private Button logInButton;
    private ImageView xButton;
    private AppCompatButton becomeAMemberButton;
    private String email, password;
    private EditText emailInput, passwordInput;
    private String expectedUserType = "company";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companylogin);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        logInButton = (Button) findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
        becomeAMemberButton = (AppCompatButton) findViewById(R.id.becomeAMemberButton);
        becomeAMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CompanyLogIn.this, Register.class));
                overridePendingTransition(0,0);
            }
        });
        xButton = (ImageView) findViewById(R.id.xButton);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CompanyLogIn.this, UserLogIn.class));
                overridePendingTransition(0,0);
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
        reference = FirebaseDatabase.getInstance().getReference("Companies");
        user = firebaseAuth.getCurrentUser();
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Company companyDetails = dataSnapshot.getValue(Company.class);

                if (companyDetails.getUserType().equals(expectedUserType)) {
                    startActivity(new Intent(CompanyLogIn.this, CompanyAccount.class));
                    overridePendingTransition(0,0);
                } else{
                    Toast.makeText(CompanyLogIn.this, "Server error, please try again." , Toast.LENGTH_LONG).show();
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
                    checkUserType();
                } else {
                    emailInput.setError("The email address does not exist.");
                    emailInput.requestFocus();
                }
            }
        });
    }
}
