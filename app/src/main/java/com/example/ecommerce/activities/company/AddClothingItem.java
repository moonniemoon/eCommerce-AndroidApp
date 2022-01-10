package com.example.ecommerce.activities.company;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.InsideBoutiqueViewHolder;
import com.example.ecommerce.accounts.Company;
import com.example.ecommerce.activities.user.JoinUsAsASeller;
import com.example.ecommerce.models.Item;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class AddClothingItem extends AppCompatActivity {

    private String categoryName, itemID, itemName, itemDescription, itemSize, itemGender, itemQuantityString, itemColor, itemPicture, itemSeller;
    private Integer itemQuantity;
    private Double itemPrice;
    private ImageView backButton;
    private EditText itemIdInput, itemNameInput, itemDescriptionInput, itemQuantityInput, itemPriceInput;
    private RadioButton womanItemButton, manItemButton;
    private Spinner sizeSpinner, colourSpinner;
    private TextView itemGenderTextView;
    private Button addItemImageButton;
    private AppCompatButton addItemButton;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private StorageReference itemImage;
    private String downloadImageUrl;
    private String parentDatabaseName = "Companies";
    private String companyID, companyName;

    private Boolean duplicateItems;
    List<String> idList = new ArrayList<String>();
    private String companyN = "";

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
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(AddClothingItem.this,
                R.array.clothingsizes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sizeSpinner.setAdapter(sizeAdapter);

        colourSpinner = (Spinner) findViewById(R.id.colour_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> colourAdapter = ArrayAdapter.createFromResource(AddClothingItem.this,
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
                Toast.makeText(AddClothingItem.this, "Server error, please try again." , Toast.LENGTH_LONG).show();
            }
        });

        companyName = getIntent().getStringExtra("companyN");

        itemIdInput = (EditText) findViewById(R.id.itemIdEditText);
        itemNameInput = (EditText) findViewById(R.id.itemNameEditText);
        itemDescriptionInput = (EditText) findViewById(R.id.productDescriptionEditText);
        itemQuantityInput = (EditText) findViewById(R.id.itemQuantityEditText);
        itemPriceInput = (EditText) findViewById(R.id.itemPriceEditText);
        womanItemButton = (RadioButton) findViewById(R.id.womanRadioButton);
        manItemButton = (RadioButton) findViewById(R.id.manRadioButton);
        itemGenderTextView = (TextView) findViewById(R.id.itemGenderTextView);
        addItemImageButton = (Button) findViewById(R.id.addItemPhotoButton);
        addItemButton = (AppCompatButton) findViewById(R.id.addItemButton);
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
                startActivity(new Intent(AddClothingItem.this, NewProductCategorySelection.class));
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
            itemQuantityInput.setError("Please enter integer");
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

        // This adds products size name to the end of it's ID
        if (itemSize.equals("XXS")){
            itemID += "-xxsmall";
        } else if (itemSize.equals("XS")) {
            itemID += "-xsmall";
        } else if (itemSize.equals("S")) {
            itemID += "-small";
        } else if (itemSize.equals("M")) {
            itemID += "-medium";
        } else if (itemSize.equals("L")) {
            itemID += "-large";
        } else if (itemSize.equals("XL")) {
            itemID += "-xlarge";
        } else if (itemSize.equals("XXL")) {
            itemID += "-xxlarge";
        }


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
            Toast.makeText(AddClothingItem.this, "Please upload item image", Toast.LENGTH_LONG).show();
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
                Toast.makeText(AddClothingItem.this, "Error: "+message, Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddClothingItem.this, "File uploaded successfully.", Toast.LENGTH_LONG).show();

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
                            Toast.makeText(AddClothingItem.this, "Image uploaded successfully.", Toast.LENGTH_LONG).show();
                            saveItemToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void saveItemToDatabase() {

        // This is to check if a product named itemID, already exists in the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference()
                .child("Products").child(companyName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String p_id = snapshot.getKey();
                            String iiii = "";
                            if (p_id.contains("-xxsmall")) {
                                iiii = p_id.replace("-xxsmall", "");
                            } else if (p_id.contains("-xsmall")) {
                                iiii = p_id.replace("-xsmall", "");
                            } else if (p_id.contains("-small")) {
                                iiii = p_id.replace("-small", "");
                            } else if (p_id.contains("-medium")) {
                                iiii = p_id.replace("-medium", "");
                            } else if (p_id.contains("-large")) {
                                iiii = p_id.replace("-large", "");
                            } else if (p_id.contains("-xlarge")) {
                                iiii = p_id.replace("-xlarge", "");
                            } else if (p_id.contains("xxlarge")) {
                                iiii = p_id.replace("xxlarge", "");
                            }

                            if (!idList.contains(iiii)) {
                                idList.add(iiii);
                            }
                        }

                        String iput = itemIdInput.getText().toString();

                        if (idList.contains(iput))
                            duplicateItems = true;
                        else duplicateItems = false;

                        Item item = new Item(itemID, itemName, itemDescription, itemGender, itemSize, itemColor, itemQuantity, categoryName, companyName, downloadImageUrl, itemPrice, duplicateItems);
                        reference = FirebaseDatabase.getInstance().getReference("Products");

                        FirebaseDatabase.getInstance().getReference("Products").child(companyName).child(itemID).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddClothingItem.this, "Item added successfully!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(AddClothingItem.this, NewProductCategorySelection.class));
                                    overridePendingTransition(0,0);
                                } else {
                                    String message = task.getException().toString();
                                    Toast.makeText(AddClothingItem.this, message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
