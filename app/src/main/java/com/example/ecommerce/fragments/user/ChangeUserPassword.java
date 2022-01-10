package com.example.ecommerce.fragments.user;

import android.os.Bundle;
import android.provider.DocumentsContract;
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
import com.example.ecommerce.activities.user.ProductDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeUserPassword extends Fragment {

    private FirebaseUser user;
    private String currentPassword, newPassword, repeatNewPassword;
    private EditText currentPasswordEditText, newPasswordEditText, repeatPasswordEditText;
    private Button changePasswordButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangeUserPassword() {
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
    public static ChangeUserPassword newInstance(String param1, String param2) {
        ChangeUserPassword fragment = new ChangeUserPassword();
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
        View RootView = inflater.inflate(R.layout.fragment_changepassword, container, false);

        currentPasswordEditText = (EditText) RootView.findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = (EditText) RootView.findViewById(R.id.newPasswordEditText);
        repeatPasswordEditText = (EditText) RootView.findViewById(R.id.repeatPasswordEditText);

        ImageView backButton= (ImageView) RootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new AccountDetails());
            }
        });
        changePasswordButton = (Button) RootView.findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePassword();
            }
        });
        return RootView;
    }

    private void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_changepassword, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(this);
        fragmentTransaction.commit();
    }

    private void validatePassword(){
        currentPassword = currentPasswordEditText.getText().toString();
        newPassword = newPasswordEditText.getText().toString();
        repeatNewPassword = repeatPasswordEditText.getText().toString();

        if(TextUtils.isEmpty(currentPassword)){
            currentPasswordEditText.setError("Please enter current password.");
            currentPasswordEditText.requestFocus();
        }
        else if(TextUtils.isEmpty(newPassword)){
            newPasswordEditText.setError("Please enter new password.");
            newPasswordEditText.requestFocus();
        }
        else if(TextUtils.isEmpty(repeatNewPassword)){
            repeatPasswordEditText.setError("Please enter new password again.");
            repeatPasswordEditText.requestFocus();
        }
        else if(newPassword.length() < 8){
            repeatPasswordEditText.setError("Password must be at least 8 characters.");
            repeatPasswordEditText.requestFocus();
        }
        else if(!(newPassword.equals(repeatNewPassword))){
            repeatPasswordEditText.setError("Passwords do not match.");
            repeatPasswordEditText.requestFocus();
        }
        else{
            changePassword(currentPassword, newPassword);
        }


    }

    private void changePassword(String currentPassword, String newPassword){
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email,currentPassword);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("password").setValue(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getActivity(), "Password changed successfully!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else {
                                Toast.makeText(getActivity(), "Server error, please try again later.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    currentPasswordEditText.setError("Incorrect password.");
                    currentPasswordEditText.requestFocus();
                }
            }
        });
    }
}
