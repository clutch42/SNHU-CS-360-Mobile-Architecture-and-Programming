package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddWeight extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_weight);

        dbHelper = new DBHelper(this);

        // Retrieve the username from the intent
        String username = getIntent().getStringExtra("USERNAME");

        // Reference to UI elements
        EditText editTextDate = findViewById(R.id.editTextDate);
        EditText editTextWeight = findViewById(R.id.editTextWeight);
        Button buttonSubmitWeight = findViewById(R.id.buttonSubmitWeight);

        // Set the initial value of the date to today's date
        editTextDate.setText(getCurrentDate());

        // Handle button click event
        buttonSubmitWeight.setOnClickListener(view -> {
            // Get the entered date and weight
            String date = editTextDate.getText().toString();
            String weightString = editTextWeight.getText().toString();
            editTextDate.setText(getCurrentDate());
            editTextWeight.setText("");
            // Validate date and weight
            if (!date.isEmpty() && isValidDouble(weightString)) {
                double weight = Double.parseDouble(weightString);
                Toast.makeText(AddWeight.this, "Thank you!", Toast.LENGTH_SHORT).show();
                dbHelper.insertDailyWeight(username, date, weight);
                // Check if the weight matches the target weight
                if (weight == getTargetWeight(username)) {
                    CheckWeight.showWeightAlert(AddWeight.this);
                }
                else {
                    finish();
                }
            } else {
                // Handle validation errors (e.g., show a message)
                Toast.makeText(AddWeight.this, "Enter date and weight", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getCurrentDate() {
        // Get the current date in the format "yyyy-MM-dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    private boolean isValidDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private double getTargetWeight(String username) {
        // Retrieve the target weight for the user
        return dbHelper.getTargetWeightForUser(username);
    }
}