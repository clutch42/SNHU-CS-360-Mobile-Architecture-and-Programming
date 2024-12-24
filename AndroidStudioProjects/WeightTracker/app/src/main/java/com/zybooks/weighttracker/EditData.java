package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditData extends AppCompatActivity {
    private EditText editTextDate;
    private EditText editTextWeight;

    private Button buttonSubmitData;
    private long defaultId;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        // Initialize EditText fields
        editTextDate = findViewById(R.id.editTextDate);
        editTextWeight = findViewById(R.id.editTextWeight);
        buttonSubmitData = findViewById(R.id.buttonSubmitData);

        // Retrieve date and weight values from the intent
        Intent intent = getIntent();
        if (intent != null) {
            defaultId = intent.getLongExtra("id", 0);
            username = intent.getStringExtra("username");
            String defaultDate = intent.getStringExtra("date");
            double defaultWeight = intent.getDoubleExtra("weight", 0.0); // 0.0 is a default value, change it if needed

            // Set default values in the EditText fields
            editTextDate.setText(defaultDate);
            editTextWeight.setText(String.valueOf(defaultWeight));
        }

        buttonSubmitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated values from the EditText fields
                String updatedDate = editTextDate.getText().toString();
                double updatedWeight = Double.parseDouble(editTextWeight.getText().toString());

                // Update the database with the new values
                updateDatabase(defaultId, updatedDate, updatedWeight);
                Toast.makeText(EditData.this, "Data Updated", Toast.LENGTH_SHORT).show();
                if (updatedWeight == getTargetWeight(username)) {
                    CheckWeight.showWeightAlert(EditData.this);
                }
                else {
                    finish();
                }
            }
        });
    }

    private void updateDatabase(long id, String updatedDate, double updatedWeight) {
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.updateDailyWeight(id, updatedDate, updatedWeight);
        dbHelper.close();
    }

    private double getTargetWeight(String username) {
        DBHelper dbHelper = new DBHelper(this);
        double targetWeight = dbHelper.getTargetWeightForUser(username);
        dbHelper.close();
        return targetWeight;
    }
}