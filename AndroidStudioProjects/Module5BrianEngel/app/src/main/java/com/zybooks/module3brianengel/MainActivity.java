package com.zybooks.module3brianengel;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textGreeting;
    private EditText nameText;
    private Button buttonSayHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize views
        textGreeting = findViewById(R.id.textGreeting);
        nameText = findViewById(R.id.nameText);
        buttonSayHello = findViewById(R.id.buttonSayHello);
        textGreeting.setText("You must enter a name");
        // Add a TextWatcher to nameText
        nameText.addTextChangedListener(new TextWatcher() {
            // it seems you have to define all 3 of these even though I only needed the onTextChange
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                buttonSayHello.setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        buttonSayHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SayHello(view);
                nameText.getText().clear();
            }
        });
    }

    public void SayHello(View view) {
        // Get the name from the EditText
        String name = nameText.getText().toString();
        // trim whitespace
        String trimmedName = name.trim();
        // Check if the name is not null or empty
        if (!trimmedName.isEmpty()) {
            // If not null, display a greeting
            textGreeting.setText("Hello, " + trimmedName + "!");
        } else {
            // If null, display an error message
            textGreeting.setText("You must enter a name");
        }
    }
}