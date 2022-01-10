package com.example.ecommerce.fragments.company;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.R;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.activities.company.CompanyAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompanyDetails extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference reference;

    private String companyID;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CompanyDetails() {
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
    public static CompanyDetails newInstance(String param1, String param2) {
        CompanyDetails fragment = new CompanyDetails();
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
        View RootView = inflater.inflate(R.layout.fragment_companydetails, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Companies");
        companyID = user.getUid();

        EditText companyName = (EditText) RootView.findViewById(R.id.companyNameEditTextUneditable);
        EditText phoneNumber = (EditText) RootView.findViewById(R.id.phoneNumberEditTextUneditable);
        EditText email = (EditText) RootView.findViewById(R.id.companyEmailEditTextUneditable);
        EditText revenue = (EditText) RootView.findViewById(R.id.revenueEditTextUneditable);

        reference.child(companyID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Company companyDetails = snapshot.getValue(Company.class);

                if(companyDetails!=null){
                    companyName.setText(companyDetails.getCompanyName());
                    phoneNumber.setText(companyDetails.getPhone());
                    email.setText(companyDetails.getEmail());
                    revenue.setText(String.valueOf(companyDetails.getRevenue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Server error, please try again." , Toast.LENGTH_LONG).show();
            }
        });
        ImageView backButton= (ImageView) RootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CompanyAccount.class));
            }
        });
        FrameLayout changePasswordLayout= (FrameLayout) RootView.findViewById(R.id.changePasswordFrameLayout);
        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new ChangeCompanyPassword());
            }
        });
        return RootView;
    }

    private void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_companydetails, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
