package com.example.ecommerce.activities.company;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;

public class AddShoeItem extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_addshoeitem);
        getSupportActionBar().hide();

        Spinner sizeSpinner = (Spinner) findViewById(R.id.size_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(AddShoeItem.this,
                R.array.shoesizes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sizeSpinner.setAdapter(sizeAdapter);

        Spinner colourSpinner = (Spinner) findViewById(R.id.colour_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> colourAdapter = ArrayAdapter.createFromResource(AddShoeItem.this,
                R.array.itemcolours_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        colourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colourSpinner.setAdapter(colourAdapter);

    }
}
