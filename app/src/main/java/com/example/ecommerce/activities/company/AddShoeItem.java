package com.example.ecommerce.activities.company;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.models.Item;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddShoeItem extends AppCompatActivity {

    private String categoryName, itemID, itemName, itemDescription, itemSize, itemGender, itemQuantityString, itemColor, itemPicture, itemSeller;
    private Integer itemQuantity;
    private Double itemPrice;
    private ImageView backButton;
    private EditText itemIdInput, itemNameInput, itemDescriptionInput, itemQuantityInput, itemPriceInput;
    private RadioButton womanItemButton, manItemButton;
    private Spinner sizeSpinner, colourSpinner;
    private TextView itemGenderTextView;
    private Button addItemImageButton, addItemButton;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private StorageReference itemImage;
    private String downloadImageUrl;
    private DatabaseReference ItemReference;
    private String parentDatabaseName = "Companies";
    private String companyID, companyName;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private FirebaseUser user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_addclothingitem);
        getSupportActionBar().hide();

        itemImage = FirebaseStorage.getInstance().getReference().child("Item Image");

        sizeSpinner = (Spinner) findViewById(R.id.size_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(AddShoeItem.this,
                R.array.shoesizes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sizeSpinner.setAdapter(sizeAdapter);

        colourSpinner = (Spinner) findViewById(R.id.colour_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> colourAdapter = ArrayAdapter.createFromResource(AddShoeItem.this,
                R.array.itemcolours_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        colourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colourSpinner.setAdapter(colourAdapter);

        categoryName = getIntent().getStringExtra("Category");
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Companies");
        companyID = user.getUid();

        reference.child(companyID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Company companyDetails = snapshot.getValue(Company.class);

                if(companyDetails!=null){
                    companyName = companyDetails.getCompanyName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddShoeItem.this, "Server error, please try again." , Toast.LENGTH_LONG).show();
            }
        });

        itemIdInput = (EditText) findViewById(R.id.itemIdEditText);
        itemNameInput = (EditText) findViewById(R.id.itemNameEditText);
        itemDescriptionInput = (EditText) findViewById(R.id.productDescriptionEditText);
        itemQuantityInput = (EditText) findViewById(R.id.itemQuantityEditText);
        itemPriceInput = (EditText) findViewById(R.id.itemPriceEditText);
        womanItemButton = (RadioButton) findViewById(R.id.womanRadioButton);
        manItemButton = (RadioButton) findViewById(R.id.manRadioButton);
        itemGenderTextView = (TextView) findViewById(R.id.itemGenderTextView);
        addItemImageButton = (Button) findViewById(R.id.addItemPhotoButton);
        addItemButton = (Button) findViewById(R.id.addItemButton);
        addItemImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateItemData();
            }
        });
        backButton = (ImageView) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddShoeItem.this, NewProductCategorySelection.class));
            }
        });
    }

    private void validateItemData(){
        itemID = itemIdInput.getText().toString();
        itemName = itemNameInput.getText().toString();
        itemDescription = itemDescriptionInput.getText().toString();
        itemQuantityString = itemQuantityInput.getText().toString();
        try{
            itemQuantity = Integer.parseInt(itemQuantityString);
        } catch(Exception e) {
            itemQuantityInput.setError("Please enter a valid number");
            itemQuantityInput.requestFocus();
            e.printStackTrace();
        }
        try{
            itemPrice = Double.parseDouble(itemPriceInput.getText().toString());
        } catch(Exception e) {
            itemPriceInput.setError("Please enter a price");
            itemPriceInput.requestFocus();
            e.printStackTrace();
        }
        itemSize = sizeSpinner.getSelectedItem().toString();
        itemColor = colourSpinner.getSelectedItem().toString();
        if(womanItemButton.isChecked()){
            itemGender = "Woman";
        }
        else if(manItemButton.isChecked()){
            itemGender = "Man";
        }
        else{
            itemGenderTextView.setError("Please select gender");
        }
        itemColor = colourSpinner.getSelectedItem().toString();

        if(ImageUri == null){
            Toast.makeText(AddShoeItem.this, "Please upload item image", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(itemID)){
            itemIdInput.setError("Please enter item ID");
            itemIdInput.requestFocus();
        }
        else if(TextUtils.isEmpty(itemName)){
            itemNameInput.setError("Please enter item name");
            itemNameInput.requestFocus();
        }
        else if(TextUtils.isEmpty(itemDescription)){
            itemDescriptionInput.setError("Please enter item description");
            itemDescriptionInput.requestFocus();
        }
        else if(TextUtils.isEmpty(itemQuantityString)){
            itemQuantityInput.setError("Please enter item quantity");
            itemQuantityInput.requestFocus();
        }
        else if(TextUtils.isEmpty(itemSize)){
            sizeSpinner.setPrompt("Please select item size");
            sizeSpinner.requestFocus();
        }
        else if(TextUtils.isEmpty(itemColor)){
            colourSpinner.setPrompt("Please select item colour");
            colourSpinner.requestFocus();
        }
        else{
            storeItemData();
        }

    }

    private void storeItemData() {
        final StorageReference filePath = itemImage.child(ImageUri.getLastPathSegment() + itemName + ".png");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.getMessage();
                Toast.makeText(AddShoeItem.this, "Error: "+message, Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddShoeItem.this, "File uploaded successfully.", Toast.LENGTH_LONG).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!(task.isSuccessful())){
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AddShoeItem.this, "Image uploaded successfully.", Toast.LENGTH_LONG).show();
                            saveItemToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void saveItemToDatabase() {

        Item item = new Item(itemID, itemName,itemDescription, itemGender, itemSize, itemColor, itemQuantity, categoryName, companyName, downloadImageUrl, itemPrice);
        reference = FirebaseDatabase.getInstance().getReference("Products");

        FirebaseDatabase.getInstance().getReference("Products").child(itemID).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddShoeItem.this, "Item added successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddShoeItem.this, NewProductCategorySelection.class));
                }
                else{
                    String message = task.getException().toString();
                    Toast.makeText(AddShoeItem.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });
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
        if (requestCode == GalleryPick && resultCode == Activity.RESULT_OK && data != null) {
            ImageUri = data.getData();
        }
    }
}
