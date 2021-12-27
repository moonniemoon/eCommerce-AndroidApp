package com.example.ecommerce.fragments.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.R;
import com.example.ecommerce.activities.user.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Register extends Fragment {

    private String firstName, lastName, email, password;
    private EditText firstNameInput, lastNameInput, emailInput, passwordInput;
    FirebaseAuth firebaseAuth;
    private DatabaseReference customerRef;
    private Button registerButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAccount newInstance(String param1, String param2) {
        FragmentAccount fragment = new FragmentAccount();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_register, container, false);

        firstNameInput = (EditText)  RootView.findViewById(R.id.firstNameEditText);
        lastNameInput = (EditText)  RootView.findViewById(R.id.lastNameEditText);
        emailInput = (EditText)  RootView.findViewById(R.id.emailAddressEditText);
        passwordInput = (EditText)  RootView.findViewById(R.id.passwordEditText);
       Button registerButton = (Button) RootView.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               validateData();
            }
        });
        ImageView backButton = (ImageView) RootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        return RootView;
    }

    private void validateData(){
        firstName = firstNameInput.getText().toString();
        lastName = lastNameInput.getText().toString();
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        if(TextUtils.isEmpty(firstName)){
            Toast.makeText(getActivity(), "Please enter a first name" , Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(lastName)){
            Toast.makeText(getActivity(), "Please enter a last name" , Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(getActivity(), "Please enter a valid email" , Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(getActivity(), "Please enter a password" , Toast.LENGTH_LONG).show();
        }
        else if(password.length()<8){
            Toast.makeText(getActivity(), "Password must be at least 8 characters" , Toast.LENGTH_LONG).show();
        }
        else{
            createUser();
        }
    }

    private void createUser(){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(getActivity(), "Welcome to the app!" , Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage() , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_register, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(this);
        fragmentTransaction.commit();
    }
}
