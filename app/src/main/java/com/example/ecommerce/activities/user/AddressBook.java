package com.example.ecommerce.activities.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.example.ecommerce.activities.company.AddClothingItem;
import com.example.ecommerce.activities.company.NewProductCategorySelection;
import com.example.ecommerce.models.AddressDetail;
import com.example.ecommerce.models.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AddressBook extends AppCompatActivity {

    private EditText bookFirstName, bookLastName, bookPhone, bookCity, bookAddress, bookPostcode, bookCountry, bookShortcut;
    private String firstName, lastName, phone, city, address, postcode, country, shortcutName;
    private TextView titleText;
    private Button saveButton, deleteButton;
    private ImageView backButton;

    private String comingFrom;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        comingFrom = getIntent().getStringExtra("comingFrom");

        titleText = (TextView) findViewById(R.id.book_textAddressBook);
        bookFirstName = (EditText) findViewById(R.id.book_editFirstName);
        bookLastName = (EditText) findViewById(R.id.book_editLastName);
        bookPhone = (EditText) findViewById(R.id.book_editPhone);
        bookCity = (EditText) findViewById(R.id.book_editTownCity);
        bookAddress = (EditText) findViewById(R.id.book_editAddress);
        bookPostcode = (EditText) findViewById(R.id.book_editPostCode);
        bookCountry = (EditText) findViewById(R.id.book_editCountry);
        bookShortcut = (EditText) findViewById(R.id.book_editShortcutName);
        saveButton = (Button) findViewById(R.id.book_saveButton);


        if (comingFrom.equals("bagUserNotFound")) {
            titleText.setText("Add address");
            saveButton.setText("Save address & Continue");
        }


        backButton = (ImageView) findViewById(R.id.backbackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (comingFrom.equals("bagUserNotFound")) {
                    startActivity(new Intent(AddressBook.this, ShoppingBag.class));
                }
                else if (comingFrom.equals("recyclerView")) {
                    Intent intent = new Intent(AddressBook.this, ChooseAddressForShipping.class);
                    intent.putExtra("comingFrom", "book");
                    startActivity(intent);
                }
                else if (comingFrom.equals("chooseAddress")) {
                    Intent intent = new Intent(AddressBook.this, ChooseAddressForShipping.class);
                    intent.putExtra("comingFrom", "book");
                    startActivity(intent);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
                String shortcutN = bookShortcut.getText().toString();
                Intent intent = new Intent(AddressBook.this, PaymentMethod.class);
                intent.putExtra("shortcutName", shortcutN);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }

    private void validateData() {
        firstName = bookFirstName.getText().toString();
        lastName = bookLastName.getText().toString();
        phone = bookPhone.getText().toString();
        city = bookCity.getText().toString();
        address = bookAddress.getText().toString();
        postcode = bookPostcode.getText().toString();
        country = bookCountry.getText().toString();
        shortcutName = bookShortcut.getText().toString();

        if(TextUtils.isEmpty(firstName)){
            bookFirstName.setError("Please enter your first name!");
            bookFirstName.requestFocus();
        }
        else if(TextUtils.isEmpty(lastName)){
            bookLastName.setError("Please enter your last name!");
            bookLastName.requestFocus();
        }
        else if(TextUtils.isEmpty(phone)){
            bookPhone.setError("Please enter your phone number!");
            bookPhone.requestFocus();
        }
        else if(TextUtils.isEmpty(city)){
            bookCity.setError("Please enter your city!");
            bookCity.requestFocus();
        }
        else if(TextUtils.isEmpty(address)){
            bookAddress.setError("Please enter your address!");
            bookAddress.requestFocus();
        }
        else if(TextUtils.isEmpty(postcode)){
            bookPostcode.setError("Please enter your postcode!");
            bookPostcode.requestFocus();
        }
        else if(TextUtils.isEmpty(country)){
            bookCountry.setError("Please enter your country!");
            bookCountry.requestFocus();
        }
        else if(TextUtils.isEmpty(shortcutName)){
            bookShortcut.setError("Please enter your shortcut name!");
            bookShortcut.requestFocus();
        }
        else{
            uploadData();
        }
    }

    private void uploadData() {

        AddressDetail addressDetail = new AddressDetail(firstName, lastName, phone, city, address, postcode, country, shortcutName);

        FirebaseDatabase.getInstance().getReference("Address Details").child(user.getUid()).child(shortcutName).setValue(addressDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddressBook.this, "Item added successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddressBook.this, ChooseAddressForShipping.class));
                } else {
                    String message = task.getException().toString();
                    Toast.makeText(AddressBook.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}