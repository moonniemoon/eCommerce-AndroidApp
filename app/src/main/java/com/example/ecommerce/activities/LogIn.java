package com.example.ecommerce.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ecommerce.R;
import com.example.ecommerce.fragments.AccountDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
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

    private void logIn(){
        // Get credentials
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        //Validate data
        if(TextUtils.isEmpty(email)){
            emailInput.setError("Please enter your email");
            emailInput.requestFocus();
        }
        else if(TextUtils.isEmpty(password)){
            passwordInput.setError("Please enter your password");
            passwordInput.requestFocus();
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(LogIn.this, "Welcome back!" , Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LogIn.this, Account.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LogIn.this, e.getMessage() , Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
