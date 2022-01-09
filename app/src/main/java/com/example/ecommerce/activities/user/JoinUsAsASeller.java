package com.example.ecommerce.activities.user;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.activities.company.AddShoeItem;
import com.example.ecommerce.activities.company.CompanyAccount;
import com.example.ecommerce.activities.company.ManageStock;
import com.example.ecommerce.activities.company.NewProductCategorySelection;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class JoinUsAsASeller extends AppCompatActivity {
    private Boolean flag = false;
    private String companyName, companyID, email, phone, password;
    private EditText inputCompanyName, emailInput, phoneInput, passwordInput;
    private ImageView backButton;
    private Button attachLogo, attachBackground, submitForm;
    private static final int GalleryPickLogo = 1, GalleryPickBG = 0;
    private Uri ImageUriLogo, ImageUriBG;
    private String downloadLogoUrl, downloadBackgroundUrl;
    private StorageReference companyLogo;
    private StorageReference companyBackground;
    private FirebaseAuth firebaseAuth;
    private String parentDatabaseName = "Companies";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinusasaseller);
        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();

        companyLogo = FirebaseStorage.getInstance().getReference().child("Company Logo");
        companyBackground = FirebaseStorage.getInstance().getReference().child("Company Background");

        attachLogo = (Button) findViewById(R.id.attachLogo_Button);
        submitForm = (Button) findViewById(R.id.submitFormButton);
        inputCompanyName = (EditText) findViewById(R.id.companyNameEditText);
        emailInput = (EditText) findViewById(R.id.companyEmailEditText);
        phoneInput = (EditText) findViewById(R.id.phoneNumberEditText);
        passwordInput = (EditText) findViewById(R.id.passwordEditText);
        attachBackground = (Button) findViewById(R.id.attachBackgroundButton);
        backButton = (ImageView) findViewById(R.id.backButton);

        attachLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGalleryLogo();
            }
        });

        attachBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGalleryBG();
            }
        });

        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateCompanyData();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JoinUsAsASeller.this, Account.class));
            }
        });
    }

    private void OpenGalleryLogo() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPickLogo);
    }

    private void OpenGalleryBG() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPickBG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPickLogo && resultCode == Activity.RESULT_OK && data!=null) {
            ImageUriLogo = data.getData();
        } else if (requestCode == GalleryPickBG && resultCode == Activity.RESULT_OK && data!=null) {
            ImageUriBG = data.getData();
        }
    }


    private void ValidateCompanyData() {
        companyName = inputCompanyName.getText().toString();
        email = emailInput.getText().toString();
        phone = phoneInput.getText().toString();
        password = passwordInput.getText().toString();

        if (ImageUriLogo == null && ImageUriBG == null) {
            Toast.makeText(JoinUsAsASeller.this, "Please attach a logo and background image... " , Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(companyName)) {
          inputCompanyName.setError("Please enter company name.");
          inputCompanyName.requestFocus();
        } else if (TextUtils.isEmpty(email)) {
            emailInput.setError("Please enter company email.");
            emailInput.requestFocus();
        } else if (TextUtils.isEmpty(phone)) {
            phoneInput.setError("Please enter company phone.");
            phoneInput.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Please enter a password.");
            passwordInput.requestFocus();
        } else {
           uploadLogo();
        }
    }

    private void uploadLogo(){
        final StorageReference filePath = companyLogo.child(ImageUriLogo.getLastPathSegment() + companyName + ".png");

        final UploadTask uploadTask = filePath.putFile(ImageUriLogo);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.getMessage();
                Toast.makeText(JoinUsAsASeller.this, "Error: "+message, Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(JoinUsAsASeller.this, "File uploaded successfully.", Toast.LENGTH_LONG).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!(task.isSuccessful())){
                            throw task.getException();
                        }
                        downloadLogoUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadLogoUrl = task.getResult().toString();
                            Toast.makeText(JoinUsAsASeller.this, "Logo uploaded successfully.", Toast.LENGTH_LONG).show();
                            uploadBackground();
                        }
                    }
                });
            }
        });
    }


    private void uploadBackground() {
        final StorageReference filePath = companyBackground.child(ImageUriBG.getLastPathSegment() + companyName + ".png");

        final UploadTask uploadTask = filePath.putFile(ImageUriBG);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.getMessage();
                Toast.makeText(JoinUsAsASeller.this, "Error: "+message, Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(JoinUsAsASeller.this, "File uploaded successfully.", Toast.LENGTH_LONG).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!(task.isSuccessful())){
                            throw task.getException();
                        }
                        downloadBackgroundUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadBackgroundUrl = task.getResult().toString();
                            Toast.makeText(JoinUsAsASeller.this, "Background uploaded successfully.", Toast.LENGTH_LONG).show();
                            createCompany();
                        }
                    }
                });
            }
        });
    }

    private void createCompany() {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Company company = new Company(companyName, email,phone, password, downloadLogoUrl, downloadBackgroundUrl, 0.0, "company");

                    FirebaseDatabase.getInstance().getReference(parentDatabaseName).child(FirebaseAuth.getInstance().getUid()).setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(JoinUsAsASeller.this, "Welcome to the app!", Toast.LENGTH_LONG).show();
                                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> loginTask) {
                                        if (loginTask.isSuccessful()) {
                                            startActivity(new Intent(JoinUsAsASeller.this, CompanyAccount.class));
                                        } else {
                                            Toast.makeText(JoinUsAsASeller.this, "Server error, please try again." , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            else{
                                try {
                                    throw task.getException();
                                } catch(Exception e) {
                                    // email already in use
                                    emailInput.setError("This email address is already associated with a company.");
                                    emailInput.requestFocus();
                                }
                            }
                        }
                    });

                }
                else{
                    // email already in use
                    emailInput.setError("This email address is already associated with a company.");
                    emailInput.requestFocus();
                }
            }
        });

    }


}
