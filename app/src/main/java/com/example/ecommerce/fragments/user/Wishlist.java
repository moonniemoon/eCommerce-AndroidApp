package com.example.ecommerce.fragments.user;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Wishlist#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Wishlist extends Fragment {

    private EditText inputCompanyID;
    private Button attachBackground, submit;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String downloadImageUrl;
    public String companyID;
    private StorageReference companyBackground;
    private DatabaseReference CompanyRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Wishlist() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Wishlist.
     */
    // TODO: Rename and change types and number of parameters
    public static Wishlist newInstance(String param1, String param2) {
        Wishlist fragment = new Wishlist();
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
        View RootView = inflater.inflate(R.layout.fragment_wishlist, container, false);


        attachBackground = (Button) RootView.findViewById(R.id.wish_attachBackground_Button);
        submit = (Button) RootView.findViewById(R.id.wish_submitButton);
        // companyID = ((EditText) RootView.findViewById(R.id.wish_companyIDEditText)).getText().toString();


        CompanyRef = FirebaseDatabase.getInstance().getReference();
        companyBackground = FirebaseStorage.getInstance().getReference().child("Company Background");



        attachBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
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

        if (ImageUri == null) {
            Toast.makeText(getActivity(), "Please attach a logo and background image... " , Toast.LENGTH_LONG).show();
        } else {
            StoreCompanyInformation();
        }
    }

    private void StoreCompanyInformation() {


        final StorageReference filePath1 = companyBackground.child(ImageUri.getLastPathSegment() + ".png");

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
                            Toast.makeText(getActivity(), "saved url to database successfully!" , Toast.LENGTH_LONG).show();


                            DatabaseReference newRef = CompanyRef.child("Companies").child("bershka");  // added every companies background by manually typing their name
                                                                                                            // after login-register(user panel) is created,
                                                                                                            // change "bershka" to companysName or ID, has to be string tho
                            ValueEventListener valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    snapshot.child("background").getRef().setValue(downloadImageUrl);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // add error
                                }
                            };
                            newRef.addListenerForSingleValueEvent(valueEventListener);
                        }
                    }
                });
            }
        });
    }
}