package com.example.ecommerce.fragments.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.ecommerce.R;
import com.example.ecommerce.accounts.User;
import com.example.ecommerce.activities.company.CompanyAccount;
import com.example.ecommerce.activities.user.Account;
import com.example.ecommerce.activities.user.ShoppingBag;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContactUs extends Fragment {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private FirebaseUser user;
    private ImageView backButton;
    private EditText queryInput;
    private Button submitQueryButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactUs() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment
     */
    // TODO: Rename and change types and number of parameters
    public static AboutUs newInstance(String param1, String param2) {
        AboutUs fragment = new AboutUs();
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
        View RootView = inflater.inflate(R.layout.fragment_contactus, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        backButton = (ImageView) RootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserType();
            }
        });
        queryInput = (EditText) RootView.findViewById(R.id.queryEditText);
        submitQueryButton = (Button) RootView.findViewById(R.id.submitQueryButton);
        submitQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(queryInput.getText().toString().equals("")){
                    emptyQueryPopup();
                }
                else {
                    queryInput.getText().clear();
                    thankYouPopup();
                }
            }
        });
        return RootView;
    }

    private void thankYouPopup(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // No action
                        break;

                }
            };
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Thank you for your query, it means a lot to us! We will contact you as soon as possible.").setPositiveButton("OK", dialogClickListener).show();
    }

    private void emptyQueryPopup(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // No action
                        break;

                }
            };
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Uh oh, you seem to haven't written anything yet. Compose your query and tap the SUBMIT QUERY button again!").setPositiveButton("GOT IT", dialogClickListener).show();
    }


    private void checkUserType() {
        reference = FirebaseDatabase.getInstance().getReference("Users");
        user = firebaseAuth.getCurrentUser();
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userDetails = dataSnapshot.getValue(User.class);
                try {
                    String userType = userDetails.getUserType();
                    if (userType.equals("user")) {
                        startActivity(new Intent(getActivity(), Account.class));

                    }
                }
                catch(Exception e){
                    startActivity(new Intent(getActivity(), CompanyAccount.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
