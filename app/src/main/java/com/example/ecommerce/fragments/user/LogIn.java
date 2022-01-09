package com.example.ecommerce.fragments.user;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends Fragment {

    private String email, password;
    private EditText emailInput, passwordInput;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogIn() {
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
        View RootView = inflater.inflate(R.layout.activity_userlogin, container, false);

        emailInput = (EditText) RootView.findViewById(R.id.loginEditTextEmailAddress);
        passwordInput = (EditText) RootView.findViewById(R.id.loginEditTextPassword);

        ImageView xButton = (ImageView) RootView.findViewById(R.id.xButton);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new AccountDetails());
            }
        });
        Button logInButton = (Button) RootView.findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    logIn();
            }
        });
        Button becomeAMemberButton = (Button) RootView.findViewById(R.id.becomeAMemberButton);
        becomeAMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new Register());
            }
        });
        return RootView;
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
                    Toast.makeText(getActivity(), "Welcome back!" , Toast.LENGTH_LONG).show();
                    changeFragment(new AccountDetails());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage() , Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    private void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_login, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(this);
        fragmentTransaction.commit();
    }
}
