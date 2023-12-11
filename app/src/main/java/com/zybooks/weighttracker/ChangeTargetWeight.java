package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ChangeTargetWeight extends AppCompatActivity {

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_target_weight);

        dbHelper = new DBHelper(this);

        String username = getIntent().getStringExtra("USERNAME");
        Log.d("ChangeTargetWeight", "Username: " + username);
        EditText editTextTargetWeight = findViewById(R.id.editTextTargetWeight);
        Button buttonSubmitTargetWeight = findViewById(R.id.buttonSubmitTargetWeight);
        buttonSubmitTargetWeight.setOnClickListener(view -> {
            String targetWeightString = editTextTargetWeight.getText().toString();
            if (isValidDouble(targetWeightString)) {
                double targetWeight = Double.parseDouble(targetWeightString);
                Toast.makeText(ChangeTargetWeight.this, "Thank you!", Toast.LENGTH_SHORT).show();
                dbHelper.insertTargetWeight(username, targetWeight);
                finish();
            }
            else {
                // Handle validation errors (e.g., show a message)
                Toast.makeText(ChangeTargetWeight.this, "Enter target weight", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isValidDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}