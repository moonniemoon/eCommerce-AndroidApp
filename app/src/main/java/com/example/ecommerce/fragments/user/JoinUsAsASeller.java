package com.example.ecommerce.fragments.user;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class JoinUsAsASeller extends Fragment {

    // https://www.youtube.com/watch?v=emDhMx_2-1E&list=PLxefhmF0pcPlqmH_VfWneUjfuqhreUz-O&index=13

    private Boolean flag = false;
    private String companyName, companyName2, email, phone, password;
    private EditText inputCompanyName, inputEmail, inputPhone, inputPassword;
    private Button attachLogo, submitForm;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String companyRandomKey, downloadImageUrl;
    private StorageReference companyLogo;
    private DatabaseReference CompanyRef;
    FirebaseAuth firebaseAuth;
    private String parentDatabaseName = "Companies";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JoinUsAsASeller() {
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
        View RootView = inflater.inflate(R.layout.fragment_joinusasaseller, container, false);

        companyLogo = FirebaseStorage.getInstance().getReference().child("Company Logo");
        CompanyRef = FirebaseDatabase.getInstance().getReference().child("Companies");

        attachLogo = (Button) RootView.findViewById(R.id.attachLogo_Button);
        submitForm = (Button) RootView.findViewById(R.id.submitFormButton);
        inputCompanyName = (EditText) RootView.findViewById(R.id.companyNameEditText);
        inputEmail = (EditText) RootView.findViewById(R.id.companyEmailEditText);
        inputPhone = (EditText) RootView.findViewById(R.id.phoneNumberEditText);
        inputPassword = (EditText) RootView.findViewById(R.id.passwordEditText);

        attachLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateCompanyData();

            }
        });
        return RootView;

    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == Activity.RESULT_OK && data!=null) {
            ImageUri = data.getData();
        }
    }


    private void ValidateCompanyData() {
        companyName = inputCompanyName.getText().toString();
        email = inputEmail.getText().toString();
        phone = inputPhone.getText().toString();
        password = inputPassword.getText().toString();

        if (ImageUri == null) {
            Toast.makeText(getActivity(), "Please attach a logo and background image... " , Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(companyName)) {
            Toast.makeText(getActivity(), "Please enter a company name..." , Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Please enter an email..." , Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getActivity(), "Please enter a phone number..." , Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please enter a password..." , Toast.LENGTH_LONG).show();
        } else {
            StoreCompanyInformation();
        }
    }


    private void StoreCompanyInformation() {
        companyName2 = companyName.replace(" ", "");
        companyRandomKey = companyName2.toLowerCase();

        final StorageReference filePath1 = companyLogo.child(ImageUri.getLastPathSegment() + companyRandomKey + ".png");

        final UploadTask uploadTask1 = filePath1.putFile(ImageUri);

        uploadTask1.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
            }

        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Image uploaded successfully..." , Toast.LENGTH_LONG).show();

                Task<Uri> urlTask1 = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath1.getDownloadUrl().toString();
                        return filePath1.getDownloadUrl();
                    }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            //createCompany();
                        }
                    }
                });
            }
        });
    }


    private void addCompanyToDatabase(){
        HashMap<String, Object> companyMap = new HashMap<>();
        companyMap.put("cID", companyRandomKey);
        companyMap.put("name", companyName);
        companyMap.put("email", email);
        companyMap.put("phone", phone);
        companyMap.put("password", password);
        companyMap.put("logo", downloadImageUrl);

        CompanyRef.child(companyRandomKey).updateChildren(companyMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "SUCCESS!" , Toast.LENGTH_LONG).show();

                        }
                        else {
                            Toast.makeText(getActivity(), "UNSUCCESSFUL!" , Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_joinUsAsASeller, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
